import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.concurrent.ThreadLocalRandom;

public class Generator {

    private static final String dataFileName = "random-file";
    private static long writtenBytes;
    private static void generateFileWithOpenTSDBData(byte number, long fileSize) throws IOException, InterruptedException {

        String fileName = String.format("%s%s", dataFileName,number);
        do{
            Thread.sleep(10);
            int cpuTagValue = ThreadLocalRandom.current().nextInt(1, 32);
            long unixSecondsNow = Instant.now().toEpochMilli();//-cpuTagValue-1;
            //if(cpuTagValue%2==0) unixSecondsNow = Instant.now().toEpochMilli();//+cpuTagValue+1;
            int hostTagValue = ThreadLocalRandom.current().nextInt(100, 200) + 1;
            try (FileWriter fileWriter = new FileWriter(fileName,true);
                 BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                 PrintWriter printWriter = new PrintWriter(bufferedWriter)) {
                String tsdbLine = String.format("sys.cpu.user %d %d host=web0%d cpu=%d",
                        unixSecondsNow,hostTagValue,number,cpuTagValue);//add tagv to OpenTSDB
                printWriter.print(tsdbLine);
                printWriter.print(System.lineSeparator());
                writtenBytes += tsdbLine.getBytes(StandardCharsets.UTF_8).length;
                writtenBytes += System.lineSeparator().getBytes(StandardCharsets.UTF_8).length;
            }
            System.out.format("bytes written: %d\n",writtenBytes);
        }
        while (writtenBytes < fileSize);
    }

    public static void main(String[] args) {
        try {
            int randomSize = ThreadLocalRandom.current().nextInt(100 * (1024 * 1024), 200 * (1024 * 1024));
            System.out.format("File generation with size %d MB has started.", randomSize / (1024 * 1024));
            generateFileWithOpenTSDBData((byte) 9, randomSize);

        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }

    }

}

