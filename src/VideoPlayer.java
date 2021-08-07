import java.util.*;

public class VideoPlayer {

    private final VideoLibrary videoLibrary;

    public VideoPlayer() {
        this.videoLibrary = new VideoLibrary();
    }

    public void numberOfVideos() {
        System.out.printf("%s videos in the library%n", videoLibrary.getVideos().size());
    }

    // Helper method for showAllVideos and showPlaying
    public String getDisplayVideoLine(String videoTitle, String videoId, List<String> tags) {
        String displayLine;
        Video video = videoLibrary.getVideo(videoId);
        if (video.isFlagged()) {
            displayLine = videoTitle + " (" + videoId + ") " + tags.toString().replaceAll(",", "") + " - FLAGGED (reason: " + video.getFlaggedReason() + ")";
        }
        else {
            displayLine = videoTitle + " (" + videoId + ") " + tags.toString().replaceAll(",", "");
        }
        return displayLine;
    }

    public TreeMap<String,String> getSortedTitleIdPair() {
        TreeMap<String,String> pairs = new TreeMap<String,String>();
        for (Video vid: videoLibrary.getVideos()) {
            pairs.put(vid.getTitle(),vid.getVideoId());
        }
        return pairs;
    }

    public void showAllVideos() {
        System.out.println("Here's a list of all available videos:");
        Video currentVid;
        String displayLine;
        TreeMap<String, String> pairs = getSortedTitleIdPair();
        String id;

        for (Map.Entry<String,String> m:pairs.entrySet()) {
            id = m.getValue();
            currentVid = videoLibrary.getVideo(id);
            displayLine = getDisplayVideoLine(currentVid.getTitle(), currentVid.getVideoId(), currentVid.getTags());
            System.out.println("  " + displayLine);
        }
    }

    public void playVideo(String videoId) {
        boolean playVideo = true;
        try {
            Video currentVideo = videoLibrary.getVideo(videoId);
            String currentVidTitle = currentVideo.getTitle();
            if (currentVideo.isFlagged()) {
                System.out.println("Cannot play video: Video is currently flagged (reason: " + currentVideo.getFlaggedReason() + ")");
                playVideo = false;
            }
            if (playVideo) {
                if (videoLibrary.getVideoCurrentlyPlaying() != null) {
                    System.out.println("Stopping video: " + videoLibrary.getVideoCurrentlyPlaying().getTitle());
                }
                videoLibrary.setVideoCurrentlyPlaying(currentVideo);
                currentVideo.setPaused(false);
                System.out.println("Playing video: " + currentVidTitle);
            }
        }
        catch (NullPointerException exc) {
            System.out.println("Cannot play video: Video does not exist");
        }
    }

    public void stopVideo() {
        Video currentVideoPlaying = videoLibrary.getVideoCurrentlyPlaying();
        if (currentVideoPlaying == null) {
            System.out.println("Cannot stop video: No video is currently playing");
        }
        else {
            System.out.println("Stopping video: " + currentVideoPlaying.getTitle());
            videoLibrary.setVideoCurrentlyPlaying(null);
        }
    }

    public void playRandomVideo() {
        Random rand = new Random();
        int numOfVids = videoLibrary.getVideos().size();
        int randNum = rand.nextInt(numOfVids);
        Video randomChosenVideo;
        Video videoCurrentlyPlaying = videoLibrary.getVideoCurrentlyPlaying();
        if (videoCurrentlyPlaying != null) {
            System.out.println("Stopping video: " + videoCurrentlyPlaying.getTitle());
            videoLibrary.setVideoCurrentlyPlaying(null);
        }
        if (videoLibrary.getNonFlaggedVideos().size() > 0) {
            randomChosenVideo = videoLibrary.getNonFlaggedVideos().get(randNum);
            randomChosenVideo.setPaused(false);
            System.out.println("Playing video: " + randomChosenVideo.getTitle());
            videoLibrary.setVideoCurrentlyPlaying(randomChosenVideo);
        }
        else {
            System.out.println("No videos available");
        }
    }

    public void pauseVideo() {
        Video videoCurrentlyPlaying = videoLibrary.getVideoCurrentlyPlaying();
        if (videoCurrentlyPlaying != null) {
            if (!(videoCurrentlyPlaying.isPaused())) {
                System.out.println("Pausing video: " + videoCurrentlyPlaying.getTitle());
                videoLibrary.getVideoCurrentlyPlaying().setPaused(true);
            }
            else {
                System.out.println("Video already paused: " + videoCurrentlyPlaying.getTitle());
            }
        }
        else {
            System.out.println("Cannot pause video: No video is currently playing");
        }
    }

    public void continueVideo() {
        Video currentPlayingVideo = videoLibrary.getVideoCurrentlyPlaying();
        if (currentPlayingVideo != null) {
            if (currentPlayingVideo.isPaused()) {
                currentPlayingVideo.setPaused(false);
                System.out.println("Continuing video: " + currentPlayingVideo.getTitle());
            }
            else {
                System.out.println("Cannot continue video: Video is not paused");
            }
        }
        else {
            System.out.println("Cannot continue video: No video is currently playing");
        }
    }

    public void showPlaying() {
        Video currentVideoPlaying = videoLibrary.getVideoCurrentlyPlaying();
        if (currentVideoPlaying != null) {
            System.out.print("Currently playing: ");
            if (currentVideoPlaying.isPaused()) {
                System.out.println(getDisplayVideoLine(currentVideoPlaying.getTitle(),currentVideoPlaying.getVideoId(),currentVideoPlaying.getTags()) + " - PAUSED");
            }
            else {
                System.out.println(getDisplayVideoLine(currentVideoPlaying.getTitle(),currentVideoPlaying.getVideoId(),currentVideoPlaying.getTags()));
            }
        }
        else {
            System.out.println("No video is currently playing");
        }
    }

    public void createPlaylist(String playlistName) {
        if (PlaylistCollection.nameUnique(playlistName)) {
            new VideoPlaylist(playlistName);
            System.out.println("Successfully created new playlist: " + playlistName);
        }
        else {
            System.out.println("Cannot create playlist: A playlist with the same name already exists");
        }
    }

    // Extends functionality of addVideoToPlaylist
    public void checkVideoPlaylistExistence(Video video, VideoPlaylist playlist, String playlistName) {
        if (video == null && playlist == null) {
            System.out.println("Cannot add video to " + playlistName + ": Playlist does not exist");
        }
        else if (video == null) {
            System.out.println("Cannot add video to " + playlistName + ": Video does not exist");
        }
        else if (playlist == null) {
            System.out.println("Cannot add video to " + playlistName + ": Playlist does not exist");
        }
    }

    public void addVideoToPlaylist(String playlistName, String videoId) {
        Video video = videoLibrary.getVideo(videoId);
        VideoPlaylist playlist = PlaylistCollection.getPlaylist(playlistName);
        if (video != null) {
            if (video.isFlagged()) {
                System.out.println("Cannot add video to " + playlistName + ": Video is currently flagged (reason: " + video.getFlaggedReason() + ")");
            }
            else {
                if (playlist != null) {
                    boolean success = playlist.addVideo(video);
                    if (success) {
                        System.out.println("Added video to " + playlistName + ": " + video.getTitle());
                    }
                    else {
                        System.out.println("Cannot add video to " + playlistName + ": Video already added");
                    }
                }
            }
            checkVideoPlaylistExistence(video,playlist,playlistName);
        }

    }

    public void showAllPlaylists() {
        PlaylistCollection.displayPlaylists();
    }

    public void showPlaylist(String playlistName) {
        VideoPlaylist playlist = PlaylistCollection.getPlaylist(playlistName);
        ArrayList<Video> videos;
        if (playlist != null) {
            videos = playlist.getPlaylistVideos();
            System.out.println("Showing playlist: " + playlistName);
            if (videos.size() > 0) {
                for (Video vid: videos) {
                    System.out.println("  " + getDisplayVideoLine(vid.getTitle(), vid.getVideoId(), vid.getTags()));
                }
            }
            else {
                System.out.println("  " + "No videos here yet");
            }
        }
        else {
            System.out.println("Cannot show playlist " +  playlistName + ": Playlist does not exist");
        }
    }

    public void removeFromPlaylist(String playlistName, String videoId) {
        VideoPlaylist playlist = PlaylistCollection.getPlaylist(playlistName);
        Video video = videoLibrary.getVideo(videoId);
        if (playlist == null) {
            System.out.println("Cannot remove video from " + playlistName + ": Playlist does not exist");
        }
        else if (video == null) {
            System.out.println("Cannot remove video from " + playlistName + ": Video does not exist");
        }
        else if (!(PlaylistCollection.isVideoInPlaylist(playlist,video))) {
            System.out.println("Cannot remove video from " + playlistName + ": Video is not in playlist");
        }
        else {
            playlist.removeVideo(video);
            System.out.println("Removed video from " + playlistName + ": " + video.getTitle());
        }
    }

    public void clearPlaylist(String playlistName) {
        VideoPlaylist playlist = PlaylistCollection.getPlaylist(playlistName);
        if (playlist != null) {
            PlaylistCollection.clearPlaylistVideos(playlist);
            System.out.println("Successfully removed all videos from " + playlistName);
        }
        else {
            System.out.println("Cannot clear playlist " + playlistName + ": Playlist does not exist");
        }
    }

    public void deletePlaylist(String playlistName) {
        VideoPlaylist playlist = PlaylistCollection.getPlaylist(playlistName);
        if (playlist != null) {
            PlaylistCollection.deletePlaylist(playlist);
            System.out.println("Deleted playlist: " + playlistName);
        }
        else {
            System.out.println("Cannot delete playlist " + playlistName + ": Playlist does not exist");
        }
    }


    public void askPlaySearchedVideo(TreeMap<String,String> titleIdPairs, int maxNum) {
        Scanner sc = new Scanner(System.in);
        String userInp;
        int iterationNum = 0;
        int choice;
        System.out.println("Would you like to play any of the above? If yes, specify the number of the video.");
        System.out.println("If your answer is not a valid number, we will assume it's a no.");
        userInp = sc.nextLine();
        try {
            choice = Integer.parseInt(userInp);
            if (choice >= 1 && choice <= maxNum) {
                for (Map.Entry<String,String> m: titleIdPairs.entrySet()) {
                    if (choice-1 == iterationNum) {
                        playVideo(m.getValue());
                        break;
                    }
                    iterationNum++;
                }
            }
        }
        catch (Exception ignored) {}
    }

    public void searchVideos(String searchTerm) {
        TreeMap<String, String> pairs = getSortedTitleIdPair();
        int displayNum = 1;
        int loopCount = 0;
        boolean resultsExist = false;
        String title,id;
        Video video;
        for (Map.Entry<String,String> m: pairs.entrySet()) {
            loopCount++;
            title = m.getKey();
            id = m.getValue();
            if (title.toLowerCase().contains(searchTerm.toLowerCase())) {
                if (loopCount == 1) {
                    System.out.println("Here are the results for " + searchTerm + ":");
                }
                resultsExist = true;
                video = videoLibrary.getVideo(id);
                if (!(video.isFlagged())) {
                    System.out.println("  " + displayNum + ") " + getDisplayVideoLine(video.getTitle(),video.getVideoId(),video.getTags()));
                    displayNum++;
                }
            }
        }
        if (!(resultsExist)) {
            System.out.println("No search results for " + searchTerm);
        }
        else {
            askPlaySearchedVideo(pairs,displayNum-1);
        }

    }

    public void searchVideosWithTag(String videoTag) {
        TreeMap<String,String> pairs = getSortedTitleIdPair();
        String id;
        Video video;
        boolean resultsExist = false;
        int loopCount = 0;
        int displayNum = 1;
        for (Map.Entry<String,String> m: pairs.entrySet()) {
            loopCount++;
            id = m.getValue();
            video = videoLibrary.getVideo(id);
            if (video.getTags().toString().toLowerCase().contains(videoTag.toLowerCase()) && videoTag.contains("#")) {
                resultsExist = true;
                if (loopCount == 1) {
                    System.out.println("Here are the results for " + videoTag + ":");
                }
                if (!(video.isFlagged())) {
                    System.out.println("  " + displayNum + ") " + getDisplayVideoLine(video.getTitle(),video.getVideoId(),video.getTags()));
                    displayNum++;
                }
            }
        }
        if (!(resultsExist)) {
            System.out.println("No search results for " + videoTag);
        }
        else {
            askPlaySearchedVideo(pairs,displayNum-1);
        }
    }

    public void stopFlaggedVideo(Video video) {
        if (videoLibrary.getVideoCurrentlyPlaying() != null) {
            if (videoLibrary.getVideoCurrentlyPlaying().equals(video)) {
                stopVideo();
            }
            else if (video.isPaused()) {
                System.out.println("Stopping video: " + video.getTitle());
            }
        }
    }

    public boolean flagVideoCheck(Video video) {
        boolean cont = true;
        if (video == null) {
            cont = false;
            System.out.println("Cannot flag video: Video does not exist");
        }
        else if (video.isFlagged()) {
            cont = false;
            System.out.println("Cannot flag video: Video is already flagged");
        }
        if (cont) {
            stopFlaggedVideo(video);
        }
        return cont;
    }

    public void flagVideo(String videoId) {
        Video video = videoLibrary.getVideo(videoId);
        boolean doFlag = flagVideoCheck(video);
        if (doFlag) {
            video.setFlagged(true);
            videoLibrary.removeFlaggedVideo(video.getVideoId());
            System.out.println("Successfully flagged video: " + video.getTitle() + " (reason: Not supplied)");
        }
    }

    public void flagVideo(String videoId, String reason) {
        Video video = videoLibrary.getVideo(videoId);
        boolean doFlag = flagVideoCheck(video);
        if (doFlag) {
            video.setFlagged(true);
            video.setFlaggedReason(reason);
            videoLibrary.removeFlaggedVideo(video.getVideoId());
            System.out.println("Successfully flagged video: " + video.getTitle() + " (reason: " + reason + ")");
        }
    }

    public void allowVideo(String videoId) {
        Video video = videoLibrary.getVideo(videoId);

        if (video == null) {
            System.out.println("Cannot remove flag from video: Video does not exist");
        }
        else if (!(video.isFlagged())) {
            System.out.println("Cannot remove flag from video: Video is not flagged");
        }
        else {
            video.setFlagged(false);
            System.out.println("Successfully removed flag from video: " + video.getTitle());
        }
    }
}