import java.io.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NumberConverter {
    public static void main(String[] args) {
        try {
            System.out.println(readCsvFileFromHardware("C:\\Users\\egodo\\Downloads\\experiment_rez\\Test2.ch2.csv"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Short> readCsvFileFromHardware(String filename) throws IOException {
        String line;
        String[] values;
        int valueCount = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            List<List<String>> list = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                valueCount += line.split(";").length;
                list.add(Arrays.asList(line.split(";")));
            }
//            short[] shorts = new short[valueCount];
            List<Short> shortList = new ArrayList<>();
            for (List<String> stringList: list) {
                for (String str : stringList) {
                    shortList.add(Short.parseShort(str));
                }
            }
//            shorts = (short[]) shortList.toArray();
            return shortList;
        }
    }


    public double[] readBinaryFileFromHardware(File file) throws FileNotFoundException {
        short[] values;
        DataInputStream dis = new DataInputStream(
                new BufferedInputStream(
                        new FileInputStream(file)));
        for (int i = 0; i < 10; i++) {

        }
        while(true) {
            try {
                BigDecimal a = BigDecimal.valueOf(dis.readByte());
                int index;
                //index++;
                System.out.println(a);
            }
            catch(IOException eof) {
                System.out.println ("End of File");
                break;
            }
        }
        return new double[0];
    }



}
