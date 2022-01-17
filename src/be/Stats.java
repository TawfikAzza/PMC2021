package be;

public class Stats {
    int movies;
    long seconds;
    public Stats(int movies, long seconds){
        this.movies=movies;
        this.seconds=seconds;
    }

    public int getMovies() {
        return movies;
    }

    public long getSeconds() {
        return seconds;
    }
}
