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
    private void write(String metricName,int timestamp, float value,Map<String,String> tags) throws IOException {
        URL url = new URL("http://localhost:4242/api/put");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
//        HttpRequest request = HttpRequest.
//                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
//                .setHeader("Content-Type","application/json")
//                .uri(create(URI))
//                .build();

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
    private String getParamsString(Map<String, String> params)
            throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
            result.append("&");
        }

        String resultString = result.toString();
        return resultString.length() > 0
                ? resultString.substring(0, resultString.length() - 1)
                : resultString;
    }
}
