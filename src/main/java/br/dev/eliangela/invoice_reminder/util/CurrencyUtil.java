package br.dev.eliangela.invoice_reminder.util;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

public class CurrencyUtil {
    
    private CurrencyUtil() {
    }

    public static String getFormattedCurrency(BigDecimal amount) {
        NumberFormat formatter = NumberFormat.getCurrencyInstance(Locale.of("pt", "BR"));
        return formatter.format(amount.doubleValue());
    }

}
