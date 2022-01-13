package be;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Movie {
    private int id;
    private String name;
    private double rating;
    private double imdbRating;
    private File fileLink;
    private String trailerLink;
    private String lastWatched;
    private String summary;
    private HashMap<Integer,CategoryMovie> movieGenres = new HashMap<>();

    public Movie(int id, String name, double rating, File fileLink, String lastWatched, HashMap<Integer,CategoryMovie> movieGenres) {
        this.id = id;
        this.name = name;
        this.rating = rating;
        this.fileLink = fileLink;
        this.lastWatched = lastWatched;
        this.movieGenres = movieGenres;
    }
    public Movie(int id, String name, double rating, File fileLink, String trailerLink,String lastWatched, HashMap<Integer,CategoryMovie> movieGenres, String summary) {
        this.id = id;
        this.name = name;
        this.rating = rating;
        this.fileLink = fileLink;
        this.lastWatched = lastWatched;
        this.movieGenres = movieGenres;
        this.trailerLink=trailerLink;
        this.summary=summary;
    }


    public Movie(int id, String name, double rating, double imdbRating, File fileLink, String lastWatched){
        this.id = id;
        this.name = name;
        this.rating = rating;
        this.imdbRating = imdbRating;
        this.fileLink = fileLink;
        this.lastWatched = lastWatched;
    }
    public Movie(int id, String name, double rating, double imdbRating, File fileLink, String lastWatched,String trailerLink, String summary){
        this.id = id;
        this.name = name;
        this.rating = rating;
        this.imdbRating = imdbRating;
        this.fileLink = fileLink;
        this.lastWatched = lastWatched;
        this.trailerLink=trailerLink;
        this.summary=summary;
    }
    public Movie(int id, String name, double rating, File fileLink, String lastWatched) {
        this.id = id;
        this.name = name;
        this.rating = rating;
        this.fileLink = fileLink;
        this.lastWatched = lastWatched;

    }

    public String getTrailerLink() {
        return trailerLink;
    }

    public void setTrailerLink(String trailerLink) {
        this.trailerLink = trailerLink;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public HashMap<Integer, CategoryMovie> getMovieGenres() {
        return movieGenres;
    }

    public void setMovieGenres(HashMap<Integer, CategoryMovie> movieGenres) {
        this.movieGenres = movieGenres;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public File getFileLink() {
        return fileLink;
    }
    public double getRating() {
        return rating;
    }

    public double getImdbRating() {
        return imdbRating;
    }
    public String getFileLinkValidPath()
    {
        return fileLink.toString().replace("\\","/").replace(" ", "%20");
    }
    public String getLastWatched() {
        return lastWatched;
    }

    public void setLastWatched(String lastWatched) {
        this.lastWatched = lastWatched;
    }

    @Override
    public String toString() {
        return String.format("%s",getName());
    }
}
