package org.kefirsf.bb.conf;

import org.kefirsf.bb.util.ExceptionUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Configuration of bbcode processor.
 * It's thread safe class.
 *
 * @author Vitaliy Samolovskih aka Kefir
 */
public final class Configuration {
    public static final int DEFAULT_NESTING_LIMIT = 500;
    public static final boolean DEFAULT_PROPAGATE_NESTING_EXCEPTION = false;

    private Map<String, Scope> scopes = null;
    private Template prefix = new Template();
    private Template suffix = new Template();
    private Map<String, Object> params = new HashMap<String, Object>();

    private int nestingLimit = DEFAULT_NESTING_LIMIT;
    private boolean propagateNestingException = DEFAULT_PROPAGATE_NESTING_EXCEPTION;

    /**
     * Create the configuration
     */
    public Configuration() {
    }

    /**
     * Set root scope for text processor.
     *
     * @param scopes scopes
     */
    public void setScopes(Iterable<Scope> scopes) {
        this.scopes = new HashMap<String, Scope>();
        for (Scope scope : scopes) {
            this.scopes.put(scope.getName(), scope);
        }
    }

    /**
     * Get scope by name.
     *
     * @param name the scope name
     * @return the scope
     */
    public Scope getScope(String name) {
        return scopes.get(name);
    }

    /**
     * Get root scope
     *
     * @return root scope
     */
    public Scope getRootScope() {
        return getScope(Scope.ROOT);
    }

    public Template getPrefix() {
        return prefix;
    }

    /**
     * Set prefix template. Prefix append to start of processed text.
     *
     * @param prefix template for prefix
     */
    public void setPrefix(Template prefix) {
        if (prefix == null) {
            throw ExceptionUtils.nullArgument("prefix");
        }
        this.prefix = prefix;
    }

    public Template getSuffix() {
        return suffix;
    }

    /**
     * Set suffix template. Suffix append to end of processed text.
     *
     * @param suffix template for suffix
     */
    public void setSuffix(Template suffix) {
        if (suffix == null) {
            throw ExceptionUtils.nullArgument("suffix");
        }

        this.suffix = suffix;
    }

    public Map<String, Object> getParams() {
        return Collections.unmodifiableMap(params);
    }

    /**
     * Set parameters to configuration.
     *
     * @param params the map with parameters. Key is a parameter name.
     */
    public void setParams(Map<String, Object> params){
        this.params = Collections.unmodifiableMap(params);
    }

    public int getNestingLimit() {
        return nestingLimit;
    }

    public void setNestingLimit(int nestingLimit) {
        this.nestingLimit = nestingLimit;
    }

    public boolean isPropagateNestingException() {
        return propagateNestingException;
    }

    public void setPropagateNestingException(boolean propagateNestingException) {
        this.propagateNestingException = propagateNestingException;
    }
}
