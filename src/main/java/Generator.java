import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.concurrent.ThreadLocalRandom;

public class Generator {


    public static void main(String[] args) {
            int randomSize = ThreadLocalRandom.current().nextInt(1024 * (1024 * 1024), 2000 * (1024 * 1024));
            System.out.format("File generation with size %d MB has started.\n", randomSize / (1024 * 1024));

            GenThread thread1 = new GenThread((byte) 5, randomSize,"t1");
            GenThread thread2 = new GenThread((byte) 5, randomSize, "t2");
            GenThread thread3 = new GenThread((byte) 5, randomSize, "t3");
            GenThread thread4 = new GenThread((byte) 5, randomSize,"t4");
            GenThread thread5 = new GenThread((byte) 5, randomSize, "t5");
            GenThread thread6 = new GenThread((byte) 5, randomSize, "t6");
            try{
                System.out.println("wait threads interruption");
                thread1.thread.join();
                thread2.thread.join();
                thread3.thread.join();
                thread4.thread.join();
                thread5.thread.join();
                thread6.thread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        System.out.println("Main thread stopped.");
        //generateFileWithOpenTSDBData((byte) 10, randomSize);
//        catch (IOException e) {
//            System.out.println(e.getMessage());
//            throw new RuntimeException(e);
//        }

    }

}

