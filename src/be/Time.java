package be;

public class Time {
    int movies;
    long seconds;
    public Time(int movies,long seconds){
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
