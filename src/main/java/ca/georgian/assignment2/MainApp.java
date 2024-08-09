package ca.georgian.assignment2;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

public class MainApp extends Application {

    private ListView<String> songListView;
    private Label infoLabel;
    private Gson gson = new Gson();

    @Override
    public void start(Stage primaryStage) {
        VBox root = new VBox(10);
        root.setPadding(new javafx.geometry.Insets(10));

        songListView = new ListView<>();
        infoLabel = new Label("Select a song to see stream count");
        Button refreshButton = new Button("Refresh Billboard");

        root.getChildren().addAll(songListView, infoLabel, refreshButton);

        Scene scene = new Scene(root, 400, 500);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Music Billboard");

        fetchBillboardData();

        refreshButton.setOnAction(e -> fetchBillboardData());

        songListView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                String trackId = newSelection.split(" - ")[0];
                fetchTrackStreamData(trackId);
            }
        });

        primaryStage.show();
    }

    private void fetchBillboardData() {
        CompletableFuture.runAsync(() -> {
            try {
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create("https://spotify23.p.rapidapi.com/playlist/?id=37i9dQZF1DX4Wsb4d7NKfP"))
                        .header("x-rapidapi-key", "d566e690d0msh9c24b399e17cd8fp1d11c5jsn2c1e6688b43d")
                        .header("x-rapidapi-host", "spotify23.p.rapidapi.com")
                        .method("GET", HttpRequest.BodyPublishers.noBody())
                        .build();
                HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

                // Print raw response for debugging
                System.out.println(response.body());

                // Parse the response and update the ListView
                Platform.runLater(() -> updateSongList(response.body()));
            } catch (Exception e) {
                e.printStackTrace();
                Platform.runLater(() -> infoLabel.setText("Failed to fetch billboard data"));
            }
        });
    }

    private void fetchTrackStreamData(String trackId) {
        CompletableFuture.runAsync(() -> {
            try {
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create("https://spotify-track-streams-playback-count1.p.rapidapi.com/tracks/spotify_track_streams?spotify_track_id=" + trackId))
                        .header("x-rapidapi-key", "d566e690d0msh9c24b399e17cd8fp1d11c5jsn2c1e6688b43d")
                        .header("x-rapidapi-host", "spotify-track-streams-playback-count1.p.rapidapi.com")
                        .method("GET", HttpRequest.BodyPublishers.noBody())
                        .build();
                HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

                // Print raw response for debugging
                System.out.println(response.body());

                // Update the info label with stream count
                Platform.runLater(() -> updateStreamInfo(response.body()));
            } catch (Exception e) {
                e.printStackTrace();
                Platform.runLater(() -> infoLabel.setText("Failed to fetch stream data"));
            }
        });
    }

    private void updateSongList(String responseBody) {
        try {
            JsonObject root = gson.fromJson(responseBody, JsonObject.class);
            JsonArray tracks = root.getAsJsonObject("tracks").getAsJsonArray("items");
            songListView.getItems().clear();
            for (JsonElement trackElement : tracks) {
                JsonObject track = trackElement.getAsJsonObject().getAsJsonObject("track");
                String songName = track.get("name").getAsString();
                String trackId = track.get("id").getAsString();
                songListView.getItems().add(trackId + " - " + songName);
            }
        } catch (Exception e) {
            e.printStackTrace();
            infoLabel.setText("Error parsing song list");
        }
    }

    private void updateStreamInfo(String responseBody) {
        try {
            JsonObject root = gson.fromJson(responseBody, JsonObject.class);
            JsonObject data = root.getAsJsonObject("data");
            String streamCount = data.get("spotify_all_time_streams").getAsString();
            infoLabel.setText("Stream count: " + streamCount);
        } catch (Exception e) {
            e.printStackTrace();
            infoLabel.setText("Error parsing stream info");
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
