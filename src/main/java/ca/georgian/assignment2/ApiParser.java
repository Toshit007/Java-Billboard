package ca.georgian.assignment2;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

public class ApiParser {

    // Exception class for parsing errors
    public static class ParsingException extends Exception {
        public ParsingException(String message) {
            super(message);
        }
    }

    // Parse Billboard API response to extract track details
    public static List<Song> parseBillboardResponse(String jsonResponse) throws ParsingException {
        List<Song> songs = new ArrayList<>();
        try {
            JsonObject jsonObject = JsonParser.parseString(jsonResponse).getAsJsonObject();
            JsonElement songsElement = jsonObject.get("data");
            if (songsElement == null || !songsElement.isJsonArray()) {
                throw new ParsingException("Missing required fields in response: data");
            }

            for (JsonElement itemElement : songsElement.getAsJsonArray()) {
                JsonObject songObject = itemElement.getAsJsonObject();

                String trackName = songObject.has("title") ? songObject.get("title").getAsString() : "Unknown Title";
                String artistName = songObject.has("artist") ? songObject.get("artist").getAsString() : "Unknown Artist";
                String albumName = songObject.has("album") ? songObject.get("album").getAsString() : "Unknown Album";
                int durationMs = songObject.has("duration_ms") ? songObject.get("duration_ms").getAsInt() : 0;

                // Create a new Song object
                songs.add(new Song(trackName, artistName, durationMs));
            }
        } catch (Exception e) {
            throw new ParsingException("Error parsing JSON response: " + e.getMessage());
        }
        return songs;
    }
}
