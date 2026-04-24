import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class AiServiceClient {

    public static String callAIService(String inputText) {
        try {
            URL url = new URL("http://127.0.0.1:5000/generate-report");

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            String jsonInput = "{ \"text\": \"" + inputText + "\" }";

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonInput.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            int responseCode = conn.getResponseCode();

            return "Response Code: " + responseCode;

        } catch (Exception e) {
            e.printStackTrace();
            return "Error calling AI service";
        }
    }
}