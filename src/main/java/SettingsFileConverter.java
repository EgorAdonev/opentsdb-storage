import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class SettingsFileConverter {
    public static void main(String[] args) {

    }

    public Map<String,String> convertChannelTxtToMap(File channelTxt) {
        Map<String, String> tagsMap = new HashMap<>();

        String expName;
        LocalDate date = null;
        LocalTime time = null;
        float samplingInterval = 0;
        boolean multiplyOn = false;
        int multiplier = 0;
        int realizationLength = 0;
        int repeatPeriod = 0;
        int realizationCount = 0;
        int initFreq = 0;
        int freqShift = 0;
        int sendLength = 0;
        int measureType = 0;
        int channelGainFactor = 0;
        int channelDelay = 0;
//        InputStream is;
//        try {
//            is = new FileInputStream(channelTxt);
//        } catch (FileNotFoundException e) {
//            System.out.println(Arrays.toString(e.getStackTrace()));
//            throw new RuntimeException(e);
//        }
//        List<String> fileContent = IOUtils.readLines(is, "UTF-8");
//        System.out.println(fileContent);
//            Scanner inFile = new Scanner(child);
//            String stroka;
//            do{
//                stroka = inFile.next();
//            } while (stroka!=null);
//            try (BufferedReader br = new BufferedReader(new FileReader(channelTxt))) {

        try (Scanner in = new Scanner(channelTxt)) {
            String channelSetting;
            for (int j = 0; j < 18; ++j) {
                channelSetting = in.next();
                switch (j) {
                    case 0:
//                        expName = channelSetting;
//                        tagsMap.put("expName", channelTxt.getName().split("\\.")[0]);
                        break;
                    case 1:
                        date = LocalDate.parse(channelSetting, DateTimeFormatter.ofPattern("dd.MM.yyyy",
                                new Locale("RU")));
                        tagsMap.put("date", String.valueOf(date));
                        break;
                    case 2:
                        time = LocalTime.parse(channelSetting);
                        tagsMap.put("time", String.valueOf(time));
                        break;
                    case 3:
                        samplingInterval = Float.parseFloat(channelSetting);
                        tagsMap.put("samp_interval", String.valueOf(samplingInterval));
                        break;
                    case 4:
                        multiplyOn = !channelSetting.equalsIgnoreCase("0");
                        tagsMap.put("multiply_on", String.valueOf(multiplyOn));
                        break;
                    case 5:
                        multiplier = Integer.parseInt(channelSetting);
                        tagsMap.put("samp_interval_multiply", String.valueOf(multiplier));
                        break;
                    case 6:
                        realizationLength = Integer.parseInt(channelSetting);
                        tagsMap.put("realization_length", String.valueOf(realizationLength));
                        break;
                    case 7:
                        repeatPeriod = Integer.parseInt(channelSetting);
                        tagsMap.put("repeat_period", String.valueOf(repeatPeriod));
                        break;
                    case 8:
                        realizationCount = Integer.parseInt(channelSetting);
                        tagsMap.put("realization_count", String.valueOf(realizationCount));
                        break;
                    case 9:
                        initFreq = Integer.parseInt(channelSetting);
                        tagsMap.put("initial_freq", String.valueOf(initFreq));
                        break;
                    case 10:
                        freqShift = Integer.parseInt(channelSetting);
                        tagsMap.put("freq_shift", String.valueOf(freqShift));
                        break;
                    case 11:
                        sendLength = Integer.parseInt(channelSetting);
                        tagsMap.put("send_time", String.valueOf(sendLength));
                        break;
                    case 12:
                        measureType = Integer.parseInt(channelSetting);
                        tagsMap.put("measure_type", String.valueOf(measureType));
                        break;
                    case 13:
                        channelGainFactor = Integer.parseInt(channelSetting);
                        tagsMap.put("gain", String.valueOf(channelGainFactor));
                        break;
                    case 14:
                        channelDelay = Integer.parseInt(channelSetting);
                        tagsMap.put("delay", String.valueOf(channelDelay));
                        break;
                }
            }

            // OR tagsMap.put("expName", expName);
            return tagsMap;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static Map<String,String> convertSettingsTxtToMap(File settings){
        return null;
    }
}
