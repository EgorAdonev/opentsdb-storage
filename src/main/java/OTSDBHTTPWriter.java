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
import java.util.*;

public class OTSDBHTTPWriter {
    private static final String EXPERIMENT_DIR = "C:\\Users\\egodo\\Downloads\\experiment_rez";
    private static SettingsFileConverter settingsConverter = new SettingsFileConverter();
    private static NumberConverter numConverter = new NumberConverter();
    public static void main(String[] args) throws IOException {
        iterateExperimentDir();

    }

    private static void iterateExperimentDir() throws IOException {
        File dir = new File(EXPERIMENT_DIR);
        if(dir.isDirectory()) {
            File[] directoryListing = dir.listFiles();
            if (directoryListing != null) {
                Map<String,String> tagsMapFromChannelTxt= new HashMap<>();
                for (File child : directoryListing) {
                    if (child.getName().endsWith(".csv")) {
                        continue;
                    }
                    if (!(child.getName().endsWith(".settings.txt")) && child.getName().endsWith(".txt")) {
                        tagsMapFromChannelTxt = settingsConverter.convertChannelTxtToMap(child);
                    }
                }
                for (File child : directoryListing) {
                    if (child.getName().endsWith(".csv")) {
                        for (int i = 0; i < numConverter.readCsvFileFromHardware(child).size(); i++) {
                            write("signal.level", System.currentTimeMillis(),
                                    numConverter.readCsvFileFromHardware(child).get(i), tagsMapFromChannelTxt);
                        }
                    }
                }
            }
        }
    }

    private static void write(String metricName, long timestamp, float value, Map<String, String> tags) throws IOException {
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
        json=json+"    \"tags\": {\n";
        for (Map.Entry<String,String> entry : tags.entrySet()) {
            String key = entry.getKey();
            String val = entry.getValue();
            if(currTagsCount == tagsCount - 1) json += String.format("     \"%s\": \"%s\"\n",key,val);
            json += String.format("     \"%s\": \"%s\",\n",key,val);

            currTagsCount++;
        }
        json = json + "    }\n";
        json = json + "}\n";

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
