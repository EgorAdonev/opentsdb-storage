import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class OTSDBHTTPWriter {
    public static void main(String[] args) {

    }
    private void write(String metricName, int timestamp, float value, Map<String,String> tags) throws IOException {
        URL url = new URL("http://localhost:4242/api/put");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
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
        
        byte[] out = json.getBytes(StandardCharsets.UTF_8);
        int length = out.length;

        con.setFixedLengthStreamingMode(length);
        con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        con.connect();
        try(OutputStream os = con.getOutputStream()) {
            os.write(out);
        }

    }
}
