import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class OTSDBHTTPWriter {
    private static final String EXPERIMENT_DIR = "C:\\Users\\egodo\\Downloads\\experiment_rez";
    private static SettingsFileConverter settingsConverter = new SettingsFileConverter();
    private static final ZoneId zoneId = ZoneId.systemDefault(); // ZoneId.of("Europe/Moscow");
    public static void main(String[] args) throws IOException {
        iterateExperimentDir();

    }

    private static void iterateExperimentDir() throws IOException {
        File dir = new File(EXPERIMENT_DIR);
        if(dir.isDirectory()) {
            File[] directoryListing = dir.listFiles();
            if (directoryListing != null) {
                Map<String,String> tagsMapFromChannelTxt= new HashMap<>();
                Map<String,String> channelMap= new HashMap<>();
                for (File child : directoryListing) {
//                    if (child.getName().endsWith(".csv")) {
//                        continue;
//                    }
                    if (!(child.getName().endsWith(".settings.txt")) && child.getName().endsWith(".txt")) {
                        tagsMapFromChannelTxt = settingsConverter.convertChannelTxtToMap(child);

                        LocalDateTime time = LocalDateTime.parse(tagsMapFromChannelTxt.get("date")
                                        +"T"+tagsMapFromChannelTxt.get("time"));
                        long epoch = time.atZone(zoneId).toEpochSecond();
                        writeExperiment("experiment.parameters", epoch, 0, tagsMapFromChannelTxt);
                    }
                }
                for (File child : directoryListing) {

                    if (child.getName().endsWith(".csv")) {
                        channelMap.put("channel", child.getName().split("\\.")[1].substring(2));
                        for (int i = 0; i < NumberConverter.readCsvFileFromHardware(child).size(); i++) {
                            LocalDateTime time = LocalDateTime.parse(tagsMapFromChannelTxt.get("date")+
                                            "T"+tagsMapFromChannelTxt.get("time"));
                            long epoch = time.atZone(zoneId).toEpochSecond();
                            writeSignalLevels("signal.level",  Math.round((epoch*1000)+(Float.parseFloat(tagsMapFromChannelTxt.get("samp_interval"))*0.000001))+i,
                                    NumberConverter.readCsvFileFromHardware(child).get(i), channelMap);

                        }
                    }
                }
            }
        }
    }
    private static void writeSignalLevels(String metricName, long timestamp, float value, Map<String, String> tags) throws IOException {
        URL url = new URL("http://localhost:4242/api/put");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode rootNode = mapper.createObjectNode();

//        ObjectNode childNode1 = mapper.createObjectNode();
        rootNode.put("metric", metricName);
        rootNode.put("timestamp", timestamp);
        rootNode.put("value", value);

        ObjectNode tagsNode = mapper.createObjectNode();
        for (Map.Entry<String,String> entry : tags.entrySet()) {
            tagsNode.put(entry.getKey(),entry.getValue());
        }
        rootNode.set("tags", tagsNode);

//        rootNode.replace(childNode1);
        String jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootNode);

        byte[] out = jsonString.getBytes(StandardCharsets.UTF_8);
        int length = out.length;

        con.setFixedLengthStreamingMode(length);
        con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        con.connect();
        try(OutputStream os = con.getOutputStream()) {
            os.write(out);
        }
    }

    private static void writeExperiment(String metricName, long timestamp, float value, Map<String, String> tags) throws IOException {
        URL url = new URL("http://localhost:4242/api/put");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode rootNode = mapper.createObjectNode();

//        ObjectNode childNode1 = mapper.createObjectNode();
        rootNode.put("metric", metricName);
        rootNode.put("timestamp", timestamp);
        rootNode.put("value", value);

        ObjectNode tagsNode = mapper.createObjectNode();
        for (Map.Entry<String,String> entry : tags.entrySet()) {
            tagsNode.put(entry.getKey(),entry.getValue());
        }
        rootNode.set("tags", tagsNode);

//        rootNode.replace(childNode1);
        String jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootNode);

        byte[] out = jsonString.getBytes(StandardCharsets.UTF_8);
        int length = out.length;

        con.setFixedLengthStreamingMode(length);
        con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        con.connect();
        try(OutputStream os = con.getOutputStream()) {
            os.write(out);
        }
    }
}
