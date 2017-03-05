package mobi.tarantino.stub.auto.helper;

import org.junit.Test;

import static junit.framework.Assert.assertTrue;

/**

 */
public class LuhnTest {
    @Test
    public void isValid() throws Exception {
        assertTrue(Luhn.isValid("4012888888881881"));
        assertTrue(Luhn.isValid("5432543254325430"));
        assertTrue(Luhn.isValid("5432 5432 5432 5430"));

        assertTrue(Luhn.isValid("6011387276485989"));
    }

}