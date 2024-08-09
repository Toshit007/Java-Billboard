package ca.georgian.assignment2;

public class Song {
    private String id;
    private String title;
    private String artist;
    private int year;
    private String album;

    // Constructor
    public Song(String id, String title, String artist, int year) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.year = year;
        this.album = "";  // Initialize as empty string
    }

    // Getter methods
    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public int getYear() {
        return year;
    }

    public String getAlbum() {
        return album;
    }

    @Override
    public String toString() {
        return title + " by " + artist;
    }
}