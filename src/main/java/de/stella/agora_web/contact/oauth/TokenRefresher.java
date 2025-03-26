package de.stella.agora_web.contact.oauth;

import java.io.InputStreamReader;
import java.util.Collections;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;

public class TokenRefresher {

    public static void main(String[] args) throws Exception {
        try {
            InputStreamReader reader = new InputStreamReader(
                    TokenRefresher.class.getResourceAsStream("/client_secret.json"));
            if (reader == null) {
                System.err.println("Error: client_secret.json not found in resources.");
                return;
            }

            GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(new GsonFactory(), reader);

            GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(new NetHttpTransport(),
                    new GsonFactory(), clientSecrets, Collections.singleton("https://mail.google.com/"))
                            .setAccessType("offline").build();

            Credential credential = new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver())
                    .authorize("user");

            System.out.println("Token refresh: " + credential.getRefreshToken());

        } catch (Exception e) {
            System.err.println("Error during token refresh: " + e.getMessage());
            e.printStackTrace();
        }
    }
}