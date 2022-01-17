package be;

import java.sql.Date;

public class Stats {
    int movies;
    long seconds;
    Date date;
    public Stats(int movies, long seconds){
        this.movies=movies;
        this.seconds=seconds;
    }
    public Stats(int movies, long seconds, Date date){
        this.movies=movies;
        this.seconds=seconds;
        this.date=date;
    }

    public Date getDate() {
        return date;
    }

    public int getMovies() {
        return movies;
    }

    public long getSeconds() {
        return seconds;
    }
}
