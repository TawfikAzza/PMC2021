package gui.Model;

import be.Movie;
import bll.utils.VideoPlayer;

public class VideoModel {
    VideoPlayer videoPlayer;

    public VideoModel() {
         videoPlayer = VideoPlayer.getInstance();
    }

    public void setCurrentMovie(Movie selectedItem) {
        videoPlayer.setCurrentMovie(selectedItem);
    }

    public void playStopMovie() {
        videoPlayer.playStopMovie();
    }
}
