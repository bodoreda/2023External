package msa.external.v1.nicepay.util;

import java.util.Random;

public class AdditionalUtil {
    public static String randomPrice() {
        Random random = new Random();
        int randomPrice = random.nextInt(501) + 1000;
        return Integer.toString(randomPrice);
    }

}
