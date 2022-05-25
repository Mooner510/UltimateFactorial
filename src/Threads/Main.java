package Threads;

public class Main {
    public static String fileLocation = "factorial-{VALUE}.txt";
    public static final String value = "100m";
    public static void main(String[] args) {
        new MultiThread(value);
    }
}
