import java.util.ArrayList;

/** A class used to represent a Playlist */
class VideoPlaylist {
    private String name;
    private ArrayList<Video> playlistVideos;

    public VideoPlaylist(String name) {
        this.name = name;
        playlistVideos = new ArrayList<>();
        PlaylistCollection.addPlaylist(this);
    }

    public String getName() {
        return name;
    }

    public boolean addVideo(Video vid) {
        boolean success = true;
        if (!playlistVideos.contains(vid)) {
            playlistVideos.add(vid);
        }
        else {
            success = false;
        }
        return success;
    }

    public void removeVideo(Video vid) {
        playlistVideos.remove(vid);
    }

    public boolean containsVideo(Video vid) {
        return playlistVideos.contains(vid);
    }

    public void clear() {
        playlistVideos.clear();
    }

    public ArrayList<Video> getPlaylistVideos() {
        return playlistVideos;
    }



}
