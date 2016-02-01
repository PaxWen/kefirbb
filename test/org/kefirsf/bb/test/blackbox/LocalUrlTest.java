package org.kefirsf.bb.test.blackbox;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.kefirsf.bb.BBProcessorFactory;
import org.kefirsf.bb.TextProcessor;
import org.kefirsf.bb.UrlCollection;

import static org.kefirsf.bb.test.Assert.assertProcess;

/**
 * Test the URL tag with local URLs
 * @author Vitalii Samolovskikh
 */
@RunWith(Parameterized.class)
public class LocalUrlTest {
    private final BBProcessorFactory factory = BBProcessorFactory.getInstance();

    @Parameterized.Parameters
    public static String[] urlCollection() {
        return UrlCollection.LOCAL;
    }

    @Parameterized.Parameter
    public String url;

    @Test
    public void testUrl() {
        TextProcessor processor = factory.createFromResource("org/kefirsf/bb/test/blackbox/config-url.xml");
        assertProcess(processor, "<a href=\"" + url + "\">" + url + "</a>", "("+url+")");
    }
}
