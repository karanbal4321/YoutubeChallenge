import java.util.Collections;
import java.util.List;

/** A class used to represent a video. */
class Video {

    private final String title;
    private final String videoId;
    private final List<String> tags;
    private boolean isPaused = false;
    private boolean isFlagged = false;
    private String flaggedReason = "Not supplied";

    Video(String title, String videoId, List<String> tags) {
        this.title = title;
        this.videoId = videoId;
        this.tags = Collections.unmodifiableList(tags);
    }

    boolean isFlagged() {
        return isFlagged;
    }

    void setFlagged(boolean flagged) {
        isFlagged = flagged;
    }

    String getFlaggedReason() {
        return flaggedReason;
    }

    void setFlaggedReason(String flaggedReason) {
        this.flaggedReason = flaggedReason;
    }

    boolean isPaused() {
        return isPaused;
    }

    void setPaused(boolean paused) {
        isPaused = paused;
    }

    /** Returns the title of the video. */
    String getTitle() {
        return title;
    }

    /** Returns the video id of the video. */
    String getVideoId() {
        return videoId;
    }

    /** Returns a readonly collection of the tags of the video. */
    List<String> getTags() {
        return tags;
    }
}

