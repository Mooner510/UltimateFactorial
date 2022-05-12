package Threads;

import jdk.jshell.execution.Util;

import java.io.File;
import java.math.BigInteger;
import java.util.HashSet;
import java.util.concurrent.atomic.AtomicLong;

import static Threads.Main.fileLocation;

public class MultiThread {
    public static long startTime;
    public static long resultCount = 0;

    private final HashSet<BigInteger> values;
    private final AtomicLong threads;
    private final AtomicLong finishedThreads;
    private final long maxThreads;

    public MultiThread(String v) {
        long value = Utils.numberFromString(v);
        fileLocation = fileLocation.replace("{VALUE}", Utils.numberTic((double) value, 0));
        long weight = 10000;
        startTime = System.currentTimeMillis();
        new File(fileLocation).delete();

        values = new HashSet<>();
        threads = new AtomicLong();
        finishedThreads = new AtomicLong();
        maxThreads = (long) Math.ceil((double) value / weight);
        for(long i = 2; i <= value; i += weight) {
            createThread(i, Math.min(value, i + weight));
        }
        while(true) {
            if(finishedThreads.get() >= maxThreads) {
                new MultiResultThread(values, (long) (Math.sqrt(maxThreads) * 1.5));
                break;
            }
        }
    }

    private void createThread(long min, long max) {
        System.out.println("Created Thread " + min + " to " + max + " (" + threads.incrementAndGet() + "/" + maxThreads + ")");
        new Thread(() -> {
            BigInteger v = BigInteger.valueOf(min + 1);
            for(long i = min + 1; i <= max; i++) {
                v = v.multiply(BigInteger.valueOf(i));
            }
            values.add(v);
            System.out.println("Ended Thread " + min + " to " + max + " (" + finishedThreads.incrementAndGet() + "/" + maxThreads + ")");
        }).start();
    }
}
