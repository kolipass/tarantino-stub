package mobi.tarantino.stub.auto.helper;

import android.content.res.Resources;
import android.support.annotation.StringRes;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class AmountFormatter {
    private Resources resources;

    public AmountFormatter(Resources resources) {
        this.resources = resources;
    }

    public static String getFormattedAmount(double v) {
        DecimalFormat formatter = new DecimalFormat();
        DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();

        symbols.setGroupingSeparator(' ');
        symbols.setDecimalSeparator(',');
        formatter.setDecimalFormatSymbols(symbols);
        return formatter.format(v);
    }

    public String getFormattedAmount(@StringRes int resId, double v) {
        return resources.getString(resId, getFormattedAmount(v));
    }
}
