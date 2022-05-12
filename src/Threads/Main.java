package Threads;

import java.math.BigDecimal;
import java.math.BigInteger;

public class Main {
    public static String fileLocation = "factorial-{VALUE}.txt";
    public static final String value = "100m";
    public static void main(String[] args) {
        new MultiThread(value);
    }
}
