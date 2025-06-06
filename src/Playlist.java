import java.util.ArrayList;
import java.util.List;

// Playlist 클래스: 개별 플레이리스트의 이름과 포함된 음악 목록을 관리합니다.
class Playlist {
    private String name;
    private List<Music> songs;

    public Playlist(String name) {
        this.name = name;
        this.songs = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public List<Music> getSongs() {
        return new ArrayList<>(songs);
    }

    public boolean addSong(Music music) {
        if (!songs.contains(music)) {
            songs.add(music);
            return true;
        }
        return false;
    }

    public boolean removeSong(Music music) {
        return songs.remove(music);
    }

    @Override
    public String toString() {
        return name + " (" + songs.size() + "곡)";
    }
}
