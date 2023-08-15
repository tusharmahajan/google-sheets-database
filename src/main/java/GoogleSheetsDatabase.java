import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class GoogleSheetsDatabase {
    private static String spreadsheetId = "1fauX8TlMqIhZ1HGKXw6Z4kW6iDiqPk6g_vbEWrukOAc";
    private static String range = "Sheet1!A:B";

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        System.out.print("Enter name: ");
        String name = reader.readLine();

        System.out.print("Enter mobile number: ");
        String mobileNumber = reader.readLine();
        String accessToken = AccessTokenProvider.getAccessToken();

        // Construct the POST request URL
        String postUrl = makeURL();

        // Construct the request payload
        String jsonPayload = "{ \"values\": [[\"" + name + "\", \"" + mobileNumber + "\"]] }";

        // Set up the HTTP connection
        HttpURLConnection connection = setupConnection(postUrl, accessToken);

        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = jsonPayload.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        // Get the response code
        int responseCode = connection.getResponseCode();
        System.out.println("Response Code: " + responseCode);

        // Read the response content (including error message if applicable)
        readResponseContent(connection);
        connection.disconnect();
    }

    private static String makeURL() {
        return  "https://sheets.googleapis.com/v4/spreadsheets/" + spreadsheetId + "/values/" + range + ":append?valueInputOption=RAW";
    }


    private static HttpURLConnection setupConnection(String postUrl, String accessToken) throws IOException {
        URL url = new URL(postUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Authorization", "Bearer " + accessToken);
        connection.setRequestProperty("Content-Type", "application/json");

        // Enable input/output streams
        connection.setDoOutput(true);
        return connection;
    }

    private static void readResponseContent(HttpURLConnection connection) throws IOException {
        String responseContent;
        if (connection.getErrorStream() != null) {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getErrorStream()))) {
                StringBuilder responseBuilder = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    responseBuilder.append(line);
                }
                responseContent = responseBuilder.toString();
            }
        } else {
            responseContent = "No error content available";
        }

        // Print the response content (error message)
        System.out.println("Response Content: " + responseContent);
    }
}
