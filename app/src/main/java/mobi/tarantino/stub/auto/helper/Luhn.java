package mobi.tarantino.stub.auto.helper;

/**
 * Luhn algorithm for validation credit cart
 */

public class Luhn {
    public static boolean isValid(CharSequence ccNumber) {
        return isValid(ccNumber.toString());
    }

    public static boolean isValid(String ccNumber) {
        if (ccNumber.contains(" ")) {
            ccNumber = ccNumber.replaceAll("[^\\d]", "");
        }


        int sum = 0;
        boolean alternate = false;
        for (int i = ccNumber.length() - 1; i >= 0; i--) {
            int n = Integer.parseInt(ccNumber.substring(i, i + 1));
            if (alternate) {
                n *= 2;
                if (n > 9) {
                    n -= 9;
                }
            }
            sum += n;
            alternate = !alternate;
        }
        return (sum % 10 == 0);
    }
}
