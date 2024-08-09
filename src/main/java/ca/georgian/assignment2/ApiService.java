package ca.georgian.assignment2;

import com.google.gson.Gson;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ApiService {

    private String apiKey = "d566e690d0msh9c24b399e17cd8fp1d11c5jsn2c1e6688b43d";
    private String apiHost = "spotify23.p.rapidapi.com";
    private Gson gson = new Gson();

    private String sendRequest(String uri) {
        String json = "";

        try {
            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(uri))
                    .header("x-rapidapi-key", apiKey)
                    .header("x-rapidapi-host", apiHost)
                    .method("GET", HttpRequest.BodyPublishers.noBody())
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            json = response.body();
        } catch (Exception e) {
            System.err.println("Cannot retrieve request with URI of " + uri);
        }

        return json;
    }

    public String searchMusic(String query) {
        // Adjust the endpoint to fit the Spotify API's search functionality
        String uri = "https://spotify23.p.rapidapi.com/search/?type=multi&offset=0&limit=10&numberOfTopResults=5&q=" + query;
        return sendRequest(uri);
    }
}
