import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// PlaylistManager 클래스: 사용자의 플레이리스트 생성, 추가, 조회 등을 관리합니다.
class PlaylistManager {
    private Map<String, List<Playlist>> userPlaylists;
    private MusicLibrary musicLibrary;

    public PlaylistManager(MusicLibrary musicLibrary) {
        this.userPlaylists = new HashMap<>();
        this.musicLibrary = musicLibrary;
    }

    public List<Playlist> getPlaylistsForUser(String userId) {
        return userPlaylists.computeIfAbsent(userId, k -> new ArrayList<>());
    }

    public boolean createPlaylist(String userId, String playlistName) {
        List<Playlist> playlists = getPlaylistsForUser(userId);
        for (Playlist p : playlists) {
            if (p.getName().equalsIgnoreCase(playlistName)) {
                return false;
            }
        }
        playlists.add(new Playlist(playlistName));
        return true;
    }

    public boolean addSongToPlaylist(String userId, String playlistName, String musicTitle) {
        Music music = musicLibrary.findMusicByTitle(musicTitle);
        if (music == null) {
            return false;
        }

        List<Playlist> playlists = getPlaylistsForUser(userId);
        for (Playlist p : playlists) {
            if (p.getName().equalsIgnoreCase(playlistName)) {
                return p.addSong(music);
            }
        }
        return false;
    }
}