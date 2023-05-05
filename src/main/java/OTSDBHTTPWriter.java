import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class OTSDBHTTPWriter {
    private static final String EXPERIMENT_DIR = "C:\\Users\\egodo\\Downloads\\experiment_rez";
    public static void main(String[] args) throws IOException {
        File dir = new File(EXPERIMENT_DIR);
        if(dir.isDirectory()) {
            File[] directoryListing = dir.listFiles();

            if (directoryListing != null) {
                for (File child : directoryListing) {
                    // STOPSHIP: 05.05.2023  "signal.level %d %f channel=%d samplingInterval=%d " +
                            //"gain=%d delay=%d measureType=%d"
                        Map<String, String> tagsMap = new HashMap<>();
                    //".\\.ch\\d\\.txt"
                        if (!child.getName().endsWith(".settings.txt") && !child.getName().endsWith(".csv")) {
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

                            try (BufferedReader br = new BufferedReader(new FileReader(child))) {
                                for (int j = 0; j < 18 && br.readLine() != null; ++j) {
//                                    br.readLine();
                                    String channelSetting = br.readLine();
                                    System.out.println(channelSetting);
                                    switch (j) {
                                        case 0:
                                            expName = channelSetting;
//                                            break;
                                        case 1:
                                            date = LocalDate.parse(channelSetting, DateTimeFormatter.ofPattern("dd.MM.yyyy",
                                                    new Locale("RU")));
                                        case 2:
                                            time = LocalTime.parse(channelSetting);
                                        case 3:
                                            samplingInterval = Float.parseFloat(channelSetting);
                                        case 4:
                                            multiplyOn = !channelSetting.equalsIgnoreCase("0");
                                        case 5:
                                            multiplier = Integer.parseInt(channelSetting);
                                        case 6:
                                            realizationLength = Integer.parseInt(channelSetting);
                                        case 7:
                                            repeatPeriod = Integer.parseInt(channelSetting);
                                        case 8:
                                            realizationCount = Integer.parseInt(channelSetting);
                                        case 9:
                                            initFreq = Integer.parseInt(channelSetting);
                                        case 10:
                                            freqShift = Integer.parseInt(channelSetting);
                                        case 11:
                                            sendLength = Integer.parseInt(channelSetting);
                                        case 12:
                                            measureType = Integer.parseInt(channelSetting);
                                        case 13:
                                            channelGainFactor = Integer.parseInt(channelSetting);
                                        case 14:
                                            channelDelay = Integer.parseInt(channelSetting);

                                    }
                                }
                                tagsMap.put("expName", child.getName().split("\\.")[0]);
                                tagsMap.put("date", String.valueOf(date));
                                tagsMap.put("time", String.valueOf(time));
                                tagsMap.put("channel", child.getName().split("ch")[1].substring(1));
                                tagsMap.put("samplingInterval", String.valueOf(samplingInterval));
                                tagsMap.put("multiplyOn", String.valueOf(multiplyOn));
                                tagsMap.put("multiplier", String.valueOf(multiplier));
                                tagsMap.put("realizationLength", String.valueOf(realizationLength));
                                tagsMap.put("repeatPeriod", String.valueOf(repeatPeriod));
                                tagsMap.put("realizationCount", String.valueOf(realizationCount));
                                tagsMap.put("initFreq", String.valueOf(initFreq));
                                tagsMap.put("freqShift", String.valueOf(freqShift));
                                tagsMap.put("sendLength", String.valueOf(sendLength));
                                tagsMap.put("measureType", String.valueOf(measureType));
                                tagsMap.put("channelGainFactor", String.valueOf(channelGainFactor));
                                tagsMap.put("channelDelay", String.valueOf(channelDelay));

                            }

                        }
                        for (int i = 0; i < NumberConverter.readCsvFileFromHardware(child).size(); i++) {
                            if (child.getName().endsWith(".csv"))
                                write("signal.level", (int) System.currentTimeMillis(),
                                        NumberConverter.readCsvFileFromHardware(child).get(i), tagsMap);
                        }
                    }
                }
            }
        }
    private static void write(String metricName, int timestamp, float value, Map<String, String> tags) throws IOException {
//        URL url = new URL("http://localhost:4242/api/put");
//        HttpURLConnection con = (HttpURLConnection) url.openConnection();
//        con.setRequestMethod("POST");
        String json = String.format("{\n" +
                "    \"metric\": \"%s\",\n" +
                "    \"timestamp\": %d,\n" +
                "    \"value\": %f,\n"
                ,metricName,timestamp,value);
        int tagsCount = tags.size();
        int currTagsCount = 0;
        json=json+"\"tags\": {\n";
        for (Map.Entry<String,String> entry : tags.entrySet()) {
            String key = entry.getKey();
            String val = entry.getValue();
            if(currTagsCount == tagsCount - 1) json += String.format("     \"%s\": \"%s\"\n",key,val);
            json += String.format("     \"%s\": \"%s\",\n",key,val);

            currTagsCount++;
        }
        json = json + "    }";
        json = json + "    }";

        System.out.println(json);
//        byte[] out = json.getBytes(StandardCharsets.UTF_8);
//        int length = out.length;
//
//        con.setFixedLengthStreamingMode(length);
//        con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
//        con.connect();
//        try(OutputStream os = con.getOutputStream()) {
//            os.write(out);
//        }

    }
}
