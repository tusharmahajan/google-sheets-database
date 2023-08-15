import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collections;

public class AccessTokenProvider {
    public static String getAccessToken() throws IOException {
        FileInputStream credentialsStream = new FileInputStream("/Users/tusharmahajan/Downloads/xenon-pager-396010-5877eca8b122.json");
        GoogleCredentials credentials = ServiceAccountCredentials.fromStream(credentialsStream);

        if (credentials.createScopedRequired()) {
            credentials = credentials.createScoped(Collections.singletonList("https://www.googleapis.com/auth/spreadsheets"));
        }
        return credentials.refreshAccessToken().getTokenValue();
    }
}
