package org.kefirsf.bb;

import org.kefirsf.bb.conf.*;
import org.kefirsf.bb.proc.*;

import java.util.*;

class ProcessorBuilder {
    private final PatternElementFactory patternElementFactory = new PatternElementFactory(this);
    private final TemplateElementFactory templateElementFactory = new TemplateElementFactory();

    private final Configuration conf;

    private Map<Scope, ProcScope> scopes;
    private Map<Code, ProcCode> codes;

    /**
     * @param conf text processor configuration
     */
    ProcessorBuilder(Configuration conf) {
        this.conf = conf;
    }

    /**
     * Build an processor.
     */
    public BBProcessor build() {
        this.scopes = new HashMap<Scope, ProcScope>();
        this.codes = new HashMap<Code, ProcCode>();
        patternElementFactory.cleanConstants();

        BBProcessor processor = new BBProcessor();
        processor.setScope(createScope(conf.getRootScope()));
        processor.setPrefix(createTemplate(conf.getPrefix()));
        processor.setSuffix(createTemplate(conf.getSuffix()));
        processor.setParams(conf.getParams());
        processor.setConstants(patternElementFactory.getConstants());
        processor.setNestingLimit(conf.getNestingLimit());
        processor.setPropagateNestingException(conf.isPropagateNestingException());

        // Init scopes
        for (ProcScope scope : scopes.values()) {
            scope.init();
        }

        return processor;
    }

    /**
     * Find or create the scope.
     *
     * @param scope the scope configuration
     * @return scope scope
     */
    ProcScope createScope(Scope scope) {
        ProcScope created = scopes.get(scope);
        if (created == null) {
            created = new ProcScope(scope.getName());
            scopes.put(scope, created);
            created.setStrong(scope.isStrong());
            created.setIgnoreText(scope.isIgnoreText());
            if (scope.getParent() != null) {
                created.setParent(createScope(scope.getParent()));
            }
            Set<ProcCode> scopeCodes = new HashSet<ProcCode>();
            for (Code code : scope.getCodes()) {
                scopeCodes.add(createCode(code));
            }
            created.setScopeCodes(scopeCodes);
            created.setMin(scope.getMin());
            created.setMax(scope.getMax());
        }
        return created;
    }

    /**
     * Create code from this definition
     *
     * @param defCode code definition
     * @return code object
     */
    private ProcCode createCode(Code defCode) {
        if (!defCode.hasPatterns()) {
            throw new IllegalStateException("Field pattern can't be null.");
        }

        if (defCode.getTemplate() == null) {
            throw new IllegalStateException("Field template can't be null.");
        }

        ProcCode code = codes.get(defCode);
        if (code == null) {
            List<Pattern> confPatterns = defCode.getPatterns();
            List<ProcPattern> procPatterns = new ArrayList<ProcPattern>(confPatterns.size());

            for (Pattern confPattern : confPatterns) {
                procPatterns.add(createPattern(confPattern));
            }

            code = new ProcCode(
                    procPatterns,
                    createTemplate(defCode.getTemplate()),
                    defCode.getName(),
                    defCode.getPriority(),
                    defCode.isTransparent()
            );
            codes.put(defCode, code);
        }
        return code;
    }

    /**
     * Create a template from definition
     *
     * @param template the template definition
     * @return template
     */
    private ProcTemplate createTemplate(Template template) {
        if (!template.isEmpty()) {
            return new ProcTemplate(templateElementFactory.createTemplateList(template.getElements()));
        } else {
            return ProcTemplate.EMPTY;
        }
    }

    /**
     * Create a pattern for text parsing
     *
     * @param pattern pattern definition
     * @return pattern pattern
     */
    private ProcPattern createPattern(Pattern pattern) {
        if (pattern.isEmpty()) {
            throw new IllegalStateException("Pattern elements list can't be empty.");
        }

        List<ProcPatternElement> elements = new ArrayList<ProcPatternElement>();
        for (PatternElement element : pattern.getElements()) {
            elements.add(patternElementFactory.create(element));
        }
        return new ProcPattern(elements);
    }
}
