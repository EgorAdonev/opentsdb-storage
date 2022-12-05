import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.concurrent.ThreadLocalRandom;

public class Generator {

    private static final String dataFileName = "files/random-file";
    private static long writtenBytes;
    private static void generateFileWithOpenTSDBData(long fileSize) throws IOException {

        for (byte i = 5; i < 6; i++) {
            String fileName = String.format("%s%s", dataFileName, i);
            do{
                long unixSecondsNow = Instant.now().toEpochMilli();
                int cpuTagValue = ThreadLocalRandom.current().nextInt(1, 32);
                int hostTagValue = ThreadLocalRandom.current().nextInt(100, 200) + 1;
                try (FileWriter fileWriter = new FileWriter(fileName,true);
                     PrintWriter printWriter = new PrintWriter(fileWriter)) {
                    String tsdbLine = String.format("sys.cpu.user %d %d host=web0%d cpu=%d",
                            unixSecondsNow,hostTagValue,i,cpuTagValue);
                    printWriter.print(tsdbLine);
                    printWriter.print(System.lineSeparator());
                    writtenBytes += tsdbLine.getBytes(StandardCharsets.UTF_8).length;
                    writtenBytes += System.lineSeparator().getBytes(StandardCharsets.UTF_8).length;
                }
                System.out.format("bytes written: %d\n",writtenBytes);
            }
            while (writtenBytes < fileSize);

        }
    }

    public static void main(String[] args) {
        try {
            int randomSize = ThreadLocalRandom.current().nextInt(1000*(1024*1024), 1200*(1024*1024));
            System.out.format("File generation with size %d MB has started.", randomSize/(1024*1024));
            Thread.sleep(2000);
            generateFileWithOpenTSDBData(randomSize);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

}
