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

                String id = songObject.has("id") ? songObject.get("id").getAsString() : "";
                String trackName = songObject.has("title") ? songObject.get("title").getAsString() : "Unknown Title";
                String artistName = songObject.has("artist") ? songObject.get("artist").getAsString() : "Unknown Artist";
                int year = songObject.has("year") ? songObject.get("year").getAsInt() : 0;

                // Create a new Song object
                songs.add(new Song(id, trackName, artistName, year));
            }
        } catch (Exception e) {
            throw new ParsingException("Error parsing JSON response: " + e.getMessage());
        }
        return songs;
    }
}