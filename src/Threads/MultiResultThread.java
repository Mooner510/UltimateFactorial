package Threads;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.atomic.AtomicLong;

import static Threads.Main.fileLocation;
import static Threads.Utils.getTime;

public class MultiResultThread {
    private final HashSet<BigInteger> values;
    private final ArrayList<BigInteger> input;
    private final AtomicLong threads;
    private final AtomicLong finishedThreads;
    private final long maxThreads;

    public MultiResultThread(HashSet<BigInteger> input, long weight) {
        weight = Math.min(weight, 24);
        MultiThread.resultCount++;
        this.input = new ArrayList<>();
        this.input.addAll(input);
        values = new HashSet<>();
        BigInteger bigInteger = BigInteger.ONE;
        threads = new AtomicLong();
        finishedThreads = new AtomicLong();
        int size = input.size();
        maxThreads = (long) Math.ceil((double) size / weight);
        for(long i = 0; i < size; i += weight) {
            createThread(i, Math.min(size - 1, i + weight));
        }
        while(true) {
            if(finishedThreads.get() >= maxThreads) {
                if(maxThreads > 12) {
                    new MultiResultThread(values, (long) (Math.sqrt(maxThreads) * 1.5));
                    break;
                }
                System.out.println("Waiting for Optimizing...");
                int i = 0;
                for (BigInteger next : values) {
                    Utils.send(++i, maxThreads);
                    bigInteger = bigInteger.multiply(next);
                }
                File file = new File(fileLocation);
                if(!file.exists()) {
                    try {
                        file.createNewFile();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                System.out.println("Waiting for Buffering...");
                try (
                        FileWriter writer = new FileWriter(fileLocation);
                        BufferedWriter w = new BufferedWriter(writer)
                        ) {
                    String s = bigInteger.toString();
                    long length = s.length();
                    int c = 0;
                    long p = Math.max(length / 25, 1);
                    for (byte b : s.getBytes(StandardCharsets.UTF_8)) {
                        Utils.send(++c, length, p);
                        w.write(b);
                        w.flush();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println("Done! " + getTime(System.currentTimeMillis() - MultiThread.startTime));
                break;
            }
        }
    }

    private void createThread(long min, long max) {
        System.out.println("Created Result-" + MultiThread.resultCount + " Thread " + min + " to " + max + " (" + threads.incrementAndGet() + "/" + maxThreads + ")");
        new Thread(() -> {
            BigInteger v = input.get((int) min);
            for(long i = min + 1; i < max; i++) {
                while(true) {
                    if(input.get((int) i) == null) break;
                    try {
                        v = v.multiply(input.get((int) i));
                        break;
                    } catch (Exception ignore) {
                    }
                }
            }
            values.add(v);
            System.out.println("Ended Result-" + MultiThread.resultCount + " Thread " + min + " to " + max + " (" + finishedThreads.incrementAndGet() + "/" + maxThreads + ")");
        }).start();
    }
}
