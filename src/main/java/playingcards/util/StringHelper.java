package playingcards.util;

import java.text.DecimalFormat;

public class StringHelper {

    public static String printCommas (int amount) {

        String number = String.valueOf(amount);
        double amountD = Double.parseDouble(number);
        DecimalFormat formatter = new DecimalFormat("#,###");

        return formatter.format(amountD);
    }
}
