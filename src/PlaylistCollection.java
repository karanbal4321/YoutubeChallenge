import java.util.ArrayList;
import java.util.Collections;

public class PlaylistCollection {
    static ArrayList<VideoPlaylist> allPlaylists = new ArrayList<>();

    public static void addPlaylist(VideoPlaylist playlist) {
        allPlaylists.add(playlist);
    }

    public static boolean nameUnique(String playListName) {
        int count = 0;
        for (VideoPlaylist playl: allPlaylists) {
            if (playl.getName().toLowerCase().equals(playListName.toLowerCase())) {
                count++;
            }
        }
        return count == 0;
    }

    public static VideoPlaylist getPlaylist(String playlistName) {
        VideoPlaylist playlist = null;
        for (VideoPlaylist playl: allPlaylists) {
            if (playl.getName().toLowerCase().equals(playlistName.toLowerCase())) {
                playlist = playl;
                break;
            }
        }
        return playlist;
    }


    public static void displayPlaylists() {
        ArrayList<String> names = new ArrayList<>();
        if (allPlaylists.size() == 0) {
            System.out.println("No playlists exist yet");
        }
        else {
            System.out.println("Showing all playlists:");
            for (VideoPlaylist playl: allPlaylists) {
                names.add(playl.getName());
            }
            Collections.sort(names);
            for (String name: names) {
                System.out.println("  " + name);
            }
        }
    }

    public static boolean isVideoInPlaylist(VideoPlaylist playlist, Video video) {
        boolean exists = false;
        for (VideoPlaylist playl: allPlaylists) {
            if (playl.equals(playlist)) {
                if (playl.containsVideo(video)) {
                    exists = true;
                    break;
                }
            }
        }
        return exists;
    }

    public static void clearPlaylistVideos(VideoPlaylist playlist) {
        for (VideoPlaylist playl: allPlaylists) {
            if (playl.equals(playlist)) {
                playl.clear();
                break;
            }
        }
    }

    public static void deletePlaylist(VideoPlaylist playlist) {
        allPlaylists.remove(playlist);
    }



}