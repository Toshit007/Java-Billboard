package ca.georgian.assignment2;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.HashMap;
import java.util.Map;

public class BillboardResponse {
    private JsonObject info;
    private Map<String, Song> content;

    public BillboardResponse(String jsonString) {
        JsonObject rootObject = JsonParser.parseString(jsonString).getAsJsonObject();
        this.info = rootObject.getAsJsonObject("info");
        JsonObject contentObject = rootObject.getAsJsonObject("content");

        this.content = new HashMap<>();
        for (Map.Entry<String, JsonElement> entry : contentObject.entrySet()) {
            String rank = entry.getKey();  // This is the rank
            JsonObject songObject = entry.getValue().getAsJsonObject();
            String id = songObject.has("id") ? songObject.get("id").getAsString() : "";
            String title = songObject.has("title") ? songObject.get("title").getAsString() : "";
            String artist = songObject.has("artist") ? songObject.get("artist").getAsString() : "";
            int year = songObject.has("year") ? songObject.get("year").getAsInt() : 0;

            Song song = new Song(id, title, artist, year);
            this.content.put(rank, song);
        }
    }

    public JsonObject getInfo() {
        return this.info;
    }

    public Map<String, Song> getContent() {
        return this.content;
    }
}