import java.io.*;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NumberConverter {
    public static void main(String[] args) throws IOException {
        File dir = new File("C:\\Users\\egodo\\Downloads\\experiment_rez");
        if(dir.isDirectory()) {
            File[] directoryListing = dir.listFiles();
            if (directoryListing != null) {
                for (File child : directoryListing) {
                    if (child.getName().endsWith(".csv")) System.out.println(readCsvFileFromHardware(child));
                }
            }
        }
//            System.out.println(readCsvFileFromHardware("C:\\Users\\egodo\\Downloads\\experiment_rez\\Test2.ch2.csv"));
    }

    public static List<Float> readCsvFileFromHardware(File file) throws IOException {
        String line;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            List<Float> doubleList = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                    for (String str : line.split(";")) {
                        short num = Short.parseShort(str);
                                //str.charAt(0) == '-' ? (short) (Short.parseShort(str) * (-1)) : Short.parseShort(str);
                        //num = (short) (0b1 << num);
                        //побитовое отрицание
                        String twoInPower = Integer.toBinaryString((int)Math.pow(2.0,15.0)).replace('0','1');
                        //char[] arr = twoInPower.toCharArray();
                        num &= Integer.parseInt(twoInPower,2)<<1;
                        String bin = Integer.toBinaryString(num);
                        short sh = bin.charAt(0) == '-' ? (short) (Short.parseShort(bin,2) * (-1)) : Short.parseShort(bin,2);
                        //System.out.println(bin.length());
                        bin = String.valueOf(sh);
                        //bin = String.valueOf(Short.parseShort(bin,2));
                        //int i = bin.charAt(0) == '1' ? Integer.parseInt(str) << 0b0 : Integer.parseInt(str) << 0b1;
                        float number = (float) Float.parseFloat(bin);
                               // /Math.pow(10,(double) str.toCharArray().length);
                        doubleList.add(number);
                    }
            }
            return doubleList;
        }
    }

    public static List<Double> readCsvFileFromHardware(String filename) throws IOException {
        String line;
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            List<List<String>> list = new ArrayList<>();
            List<Double> doubleList = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                list.add(Arrays.asList(line.split(";")));
                for (List<String> stringList: list) {
                    for (String str : stringList) {
                        double number = Short.parseShort(str)/Math.pow(10,(double) str.toCharArray().length);
                        doubleList.add(number);
                    }
                }
            }
            return doubleList;
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
