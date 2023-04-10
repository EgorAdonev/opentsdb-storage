import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class GenThread implements Runnable{
    private int number;
    private long fileSize;

    Thread thread;
    String name;
    GenThread(byte number,long fileSize,String threadName){
        name = threadName;
        thread = new Thread(this,threadName);
        System.out.println(thread);
        this.number=number;
        this.fileSize = fileSize;
        thread.start();

    }
    static final String dataFileName = "random-file";
    static long writtenBytes;
    private synchronized static void generateFileWithOpenTSDBData(byte number, long fileSize) throws IOException, InterruptedException {
        int[] array = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32};//seed в рандоме привязка к System.timeMillis
        String fileName = String.format("%s%s", dataFileName,number);
        do{
            int i = 0;
            Thread t = Thread.currentThread();
            t.sleep(1);
            int cpuTagValue = array[i];//ThreadLocalRandom.current().nextInt(1, 32);
            long unixSecondsNow = Instant.now().toEpochMilli();//-cpuTagValue-1;

            //if(cpuTagValue%2==0) unixSecondsNow = Instant.now().toEpochMilli();//+cpuTagValue+1;
            Random rand = new Random();
            int hostTagValue = rand.nextInt(100) + 1;
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
            System.out.format("MB written: %d\n",writtenBytes/(1024*1024));
            i++;
        }
        while (writtenBytes < fileSize);
    }
    @Override
    public void run(){
        try {
//            for (int i = 5; i > 0; i--) {
                generateFileWithOpenTSDBData((byte) number, fileSize);
//                Thread.sleep(1000);
//            }
        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
        System.out.println(name + " has stopped.");
    }
}
