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
//                    if (child.getName().endsWith(".csv")) System.out.println(readCsvFileFromHardware(child));
                }
            }
        }
    }

    public static List<Float> readCsvFileFromHardware(File file) throws IOException {
        String line;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            List<Float> doubleList = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                    for (String str : line.split(";")) {
                        short num = Short.parseShort(str);
                        String bin = String.format("%16s", Integer.toBinaryString(num)).replace(' ', '0');
//                        bin = bin.substring(bin.indexOf("1"));
                        char[] binaryDigits = bin.toCharArray();
                        float mantissa = 0f;
                        float result;
                        if(binaryDigits[0] == '1') {
                            for (int i = 0; i < binaryDigits.length; i++) {
                                binaryDigits[i] = binaryDigits[i] == '1' ? '0' : '1';
//                            result = binaryDigits[0] == '1' ? mantissa * (-1f) : mantissa;
                            }
                        }
                        for (int i = 0; i < binaryDigits.length; i++) {
                            char digit = binaryDigits[i];
                            switch (digit) {
                                case '1':
                                    mantissa += 1 * Math.pow(2, i);
//                                    System.out.println(mantissa);
                            }
                        }
                        result = (float) (mantissa * (1.0/32768.0));
                        doubleList.add(result);
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

}
