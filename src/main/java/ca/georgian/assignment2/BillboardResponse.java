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
            JsonObject songObject = entry.getValue().getAsJsonObject();
            String rank = entry.getKey();
            String title = songObject.has("title") ? songObject.get("title").getAsString() : "";
            String artist = songObject.has("artist") ? songObject.get("artist").getAsString() : "";
            String image = songObject.has("image") ? songObject.get("image").getAsString() : "";
            int weeksAtNo1 = songObject.has("weeks_at_no_1") ? songObject.get("weeks_at_no_1").getAsInt() : 0;
            int lastWeek = songObject.has("last_week") ? songObject.get("last_week").getAsInt() : 0;
            int peakPosition = songObject.has("peak_position") ? songObject.get("peak_position").getAsInt() : 0;
            int weeksOnChart = songObject.has("weeks_on_chart") ? songObject.get("weeks_on_chart").getAsInt() : 0;
            String detail = songObject.has("detail") ? songObject.get("detail").getAsString() : "";

            Song song = new Song(title, artist, image, weeksAtNo1, lastWeek, peakPosition, weeksOnChart, detail);
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
