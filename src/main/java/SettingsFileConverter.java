import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;

public class SettingsFileConverter {
    public static void main(String[] args) {

    }

    public Map<String,String> convertChannelTxtToMap(File channelTxt){
        // STOPSHIPP: 05.05.2023  "signal.level %d %f channel=%d samplingInterval=%d " + "gain=%d delay=%d measureType=%d"
        Map<String, String> tagsMap = new HashMap<>();
        //".\\.ch\\d\\.txt"
//        if (!channelTxt.getName().endsWith(".settings.txt") && channelTxt.getName().endsWith(".txt")) {
//                            tagsMap = new HashMap<>();
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
//            Scanner inFile = new Scanner(child);
//            String stroka;
//            do{
//                stroka = inFile.next();
//            } while (stroka!=null);
//            try (BufferedReader br = new BufferedReader(new FileReader(channelTxt))) {
            try(Scanner in = new Scanner(channelTxt)){
                String channelSetting;
                for (int j = 0; j < 18 && in.next() != null; ++j) {

//                                    br.readLine();
                    channelSetting = in.next();
//                    System.out.println(channelSetting);
                    switch (j) {
                        case 0:
                            expName = channelSetting;
                            tagsMap.put("expName", channelTxt.getName().split("\\.")[0]);
                            break;
                        case 1:
                            date = LocalDate.parse(channelSetting, DateTimeFormatter.ofPattern("dd.MM.yyyy",
                                    new Locale("RU")));
                            tagsMap.put("date", String.valueOf(date));
                            break;
                        case 2:
                            time = LocalTime.parse(channelSetting);
                            tagsMap.put("time", String.valueOf(time));
                        case 3:
                            samplingInterval = Float.parseFloat(channelSetting);
                            tagsMap.put("samplingInterval", String.valueOf(samplingInterval));
                        case 4:
                            multiplyOn = !channelSetting.equalsIgnoreCase("0");
                            tagsMap.put("multiplyOn", String.valueOf(multiplyOn));
                        case 5:
                            multiplier = Integer.parseInt(channelSetting);
                            tagsMap.put("multiplier", String.valueOf(multiplier));
                        case 6:
                            realizationLength = Integer.parseInt(channelSetting);
                            tagsMap.put("realizationLength", String.valueOf(realizationLength));
                        case 7:
                            repeatPeriod = Integer.parseInt(channelSetting);
                            tagsMap.put("repeatPeriod", String.valueOf(repeatPeriod));
                        case 8:
                            realizationCount = Integer.parseInt(channelSetting);
                            tagsMap.put("realizationCount", String.valueOf(realizationCount));
                        case 9:
                            initFreq = Integer.parseInt(channelSetting);
                            tagsMap.put("initFreq", String.valueOf(initFreq));
                        case 10:
                            freqShift = Integer.parseInt(channelSetting);
                            tagsMap.put("freqShift", String.valueOf(freqShift));
                        case 11:
                            sendLength = Integer.parseInt(channelSetting);
                            tagsMap.put("sendLength", String.valueOf(sendLength));
                        case 12:
                            measureType = Integer.parseInt(channelSetting);
                            tagsMap.put("measureType", String.valueOf(measureType));
                        case 13:
                            channelGainFactor = Integer.parseInt(channelSetting);
                            tagsMap.put("channelGainFactor", String.valueOf(channelGainFactor));
                        case 14:
                            channelDelay = Integer.parseInt(channelSetting);
                            tagsMap.put("channelDelay", String.valueOf(channelDelay));
                    }
                    tagsMap.put("channel", channelTxt.getName().split("ch")[1].substring(1));
                }
                // OR tagsMap.put("expName", expName);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        return tagsMap;
    }
    public static Map<String,String> convertSettingsTxtToMap(File settings){

        return null;
    }
}
