package ca.georgian.assignment2;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class WorkingWithJson {

    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create(); // For pretty-printing JSON

    // Serialize a Song object to JSON
    static void task2() {
        Song song = new Song("1", "Song Title", "Artist Name", 2024);
        try {
            String json = gson.toJson(song);
            System.out.println("Serialized JSON:");
            System.out.println(json);
        } catch (Exception e) {
            System.err.println("Error serializing Song object: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Deserialize a JSON string to a Song object
    static void createObjectFromJson(String jsonText) {
        try {
            Song song = gson.fromJson(jsonText, Song.class);
            System.out.println("Deserialized Song object:");
            System.out.println(song);
        } catch (Exception e) {
            System.err.println("Error deserializing JSON to Song object: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Handling a JSON string with multiple Song objects
    static void multipleValuesPart1() {
        List<Song> songsList = new ArrayList<>();
        songsList.add(new Song("1", "First Song", "First Artist", 2021));
        songsList.add(new Song("2", "Second Song", "Second Artist", 2022));
        songsList.add(new Song("3", "Third Song", "Third Artist", 2023));

        try {
            String json = gson.toJson(songsList);
            System.out.println("Serialized JSON List:");
            System.out.println(json);
            passingInfoToTheJsonReader(json);
        } catch (Exception e) {
            System.err.println("Error handling JSON with multiple Song objects: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Using Gson to parse multiple Song objects from JSON
    static void passingInfoToTheJsonReader(String jsonText) {
        Type songListType = new TypeToken<List<Song>>(){}.getType();
        try {
            List<Song> songsList = gson.fromJson(jsonText, songListType);
            System.out.println("Deserialized Song List:");
            for (Song song : songsList) {
                System.out.println(song);
            }
        } catch (Exception e) {
            System.err.println("Error parsing JSON to list of Song objects: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Create JSON string with readable formatting
    static void jsonWithReadableFormatting() {
        Song song = new Song("1", "Formatted Song", "Formatted Artist", 2024);
        try {
            String json = gson.toJson(song);
            System.out.println("Formatted JSON:");
            System.out.println(json);
        } catch (Exception e) {
            System.err.println("Error creating formatted JSON string: " + e.getMessage());
            e.printStackTrace();
        }
    }
}