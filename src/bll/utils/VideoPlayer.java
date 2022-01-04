package bll.utils;

import be.Movie;
import javafx.scene.media.Media;
import javafx.scene.media.MediaException;
import javafx.scene.media.MediaPlayer;

public class VideoPlayer {
    private final static float MAX_VOLUME = 200;
    private static VideoPlayer single_instance = null;
    private MediaPlayer player = null;
    private Movie currentMovie;
    private float volume;

    public static VideoPlayer getInstance()
    {
        if (single_instance == null)
            single_instance = new VideoPlayer();

        return single_instance;
    }
    /*
    The default volume should e 50
     */
    public VideoPlayer()
    {
        volume = MAX_VOLUME/2;
    }

    /*
    Plays the current song, if there is any.
    TODO: play/pause function


    */
    public MediaPlayer getPlayer() {
        return player;
    }
    public void playStopMovie() {
        if (currentMovie == null)
        {
            return;
        }
        if(player != null && player.getStatus() == MediaPlayer.Status.PLAYING) {
            player.pause();
            return;
        }
        if (player != null) {
            player.play();
        }
    }

    public void setCurrentMovie(Movie movie) {
        if(player != null && player.getStatus() == MediaPlayer.Status.PLAYING) {
            player.stop();
        }
        try
        {
            this.currentMovie = movie;
           // System.out.println("valid: "+toValidPath(currentMovie.getFileLink().toString()));
            final Media media = new Media(toValidPath(currentMovie.getFileLink().toURI().toString()));

            this.player = new MediaPlayer(media);
        } catch (MediaException e)
        {
            System.out.println("error");
           // throw new SongPlayerException("Cannot find associated song file or the file is corrupted!", e);
        }
    }

    public Movie getCurrentMovie() {
        return currentMovie;
    }

    /*
    Setting the volume from 0-100 and clamped accordingly to the needs of the media player
     */
    public void setVolume(int soundVolume)
    {
        volume = (float) (1 - (Math.log(MAX_VOLUME - soundVolume) / Math.log(MAX_VOLUME)));
        player.setVolume(volume);
    }

    public float getVolume()
    {
        return volume;
    }

    public String toValidPath(String path)
    {
        return path.replace("\\","/").replace(" ", "%20");
    }
}
