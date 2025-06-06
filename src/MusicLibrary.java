import java.util.ArrayList;
import java.util.List;

// MusicLibrary 클래스: 시스템이 보유한 전체 음악 목록을 관리합니다.
class MusicLibrary {
    private List<Music> allMusic;

    public MusicLibrary() {
        allMusic = new ArrayList<>();
        initializeDefaultMusic();
    }

    private void initializeDefaultMusic() {
        allMusic.add(new Music("Happy", "Pharrell Williams", "Pop"));
        allMusic.add(new Music("Good Day", "IU", "K-Pop"));
        allMusic.add(new Music("Someone Like You", "Adele", "Pop"));
        allMusic.add(new Music("네버엔딩 스토리", "부활", "Rock Ballad"));
        allMusic.add(new Music("Eye of the Tiger", "Survivor", "Rock"));
        allMusic.add(new Music("River Flows in You", "Yiruma", "New Age"));
        allMusic.add(new Music("Dynamite", "BTS", "K-Pop"));
        allMusic.add(new Music("Butter", "BTS", "K-Pop"));
        allMusic.add(new Music("Blinding Lights", "The Weeknd", "Pop"));
        allMusic.add(new Music("Shape of You", "Ed Sheeran", "Pop"));
        allMusic.add(new Music("Imagine", "John Lennon", "Classic"));
        allMusic.add(new Music("Bohemian Rhapsody", "Queen", "Rock"));
        allMusic.add(new Music("Rainy Day", "Various Artists", "Jazz"));
        allMusic.add(new Music("Snowflake Waltz", "Instrumental", "Classical"));
        allMusic.add(new Music("Thunderstruck", "AC/DC", "Rock"));
    }

    public List<Music> getAllMusic() {
        return new ArrayList<>(allMusic);
    }

    public Music findMusicByTitle(String title) {
        for (Music music : allMusic) {
            if (music.getTitle().equalsIgnoreCase(title)) {
                return music;
            }
        }
        return null;
    }
}
