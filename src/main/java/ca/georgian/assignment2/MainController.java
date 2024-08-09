package ca.georgian.assignment2;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.ListCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class MainController {

    @FXML
    private ImageView coverImageView;

    @FXML
    private Label errMsgLabel;

    @FXML
    private Button getDetailsButton;

    @FXML
    private TextField playlistIdTextField;

    @FXML
    private ListView<Song> songsListView;

    private Gson gson = new Gson();

    @FXML
    void getPlaylistResults(ActionEvent event) {
        String playlistId = playlistIdTextField.getText();
        fetchBillboardData(playlistId);
    }

    private void fetchBillboardData(String playlistId) {
        CompletableFuture.runAsync(() -> {
            try {
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create("https://spotify23.p.rapidapi.com/playlist/?id=" + playlistId))
                        .header("x-rapidapi-key", "YOUR_API_KEY_HERE")
                        .header("x-rapidapi-host", "spotify23.p.rapidapi.com")
                        .method("GET", HttpRequest.BodyPublishers.noBody())
                        .build();

                HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

                BillboardResponse billboardResponse = new BillboardResponse(response.body());
                List<Song> songs = List.copyOf(billboardResponse.getContent().values());

                Platform.runLater(() -> {
                    if (songs.isEmpty()) {
                        errMsgLabel.setText("No songs found.");
                    } else {
                        errMsgLabel.setText("");
                        updateListView(songs);
                        updateCoverImage(""); // Provide an appropriate URL if available
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                Platform.runLater(() -> errMsgLabel.setText("Failed to fetch playlist data."));
            }
        });
    }

    public void updateListView(List<Song> songs) {
        ObservableList<Song> observableList = FXCollections.observableArrayList(songs);
        songsListView.setItems(observableList);
    }

    private void updateCoverImage(String imageUrl) {
        if (imageUrl != null && !imageUrl.isEmpty()) {
            try {
                Image image = new Image(imageUrl);
                coverImageView.setImage(image);
            } catch (Exception e) {
                e.printStackTrace();
                coverImageView.setImage(null);
            }
        } else {
            coverImageView.setImage(null);
        }
    }

    @FXML
    void initialize() {
        errMsgLabel.setText("");
        getDetailsButton.setDisable(true);

        songsListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            getDetailsButton.setDisable(newValue == null);
        });

        songsListView.setCellFactory(new Callback<ListView<Song>, ListCell<Song>>() {
            @Override
            public ListCell<Song> call(ListView<Song> param) {
                return new ListCell<Song>() {
                    @Override
                    protected void updateItem(Song item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setText(null);
                        } else {
                            setText(item.getTitle() + " - " + item.getArtist());
                        }
                    }
                };
            }
        });
    }

    @FXML
    void getMoreDetails(ActionEvent event) {
        Song selectedSong = songsListView.getSelectionModel().getSelectedItem();
        if (selectedSong != null) {
            fetchTrackStreamData(selectedSong.getId());
        }
    }

    private void fetchTrackStreamData(String trackId) {
        CompletableFuture.runAsync(() -> {
            try {
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create("https://spotify-track-streams-playback-count1.p.rapidapi.com/tracks/spotify_track_streams?spotify_track_id=" + trackId))
                        .header("x-rapidapi-key", "YOUR_API_KEY_HERE")
                        .header("x-rapidapi-host", "spotify-track-streams-playback-count1.p.rapidapi.com")
                        .method("GET", HttpRequest.BodyPublishers.noBody())
                        .build();

                HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

                // Print raw response for debugging
                System.out.println(response.body());

                Platform.runLater(() -> updateStreamInfo(response.body()));
            } catch (Exception e) {
                e.printStackTrace();
                Platform.runLater(() -> errMsgLabel.setText("Failed to fetch stream data."));
            }
        });
    }

    private void updateStreamInfo(String responseBody) {
        try {
            JsonObject root = gson.fromJson(responseBody, JsonObject.class);
            JsonObject data = root.getAsJsonObject("data");
            String streamCount = data.get("spotify_all_time_streams").getAsString();
            errMsgLabel.setText("Stream count: " + streamCount);
        } catch (Exception e) {
            e.printStackTrace();
            errMsgLabel.setText("Error parsing stream info.");
        }
    }
}
