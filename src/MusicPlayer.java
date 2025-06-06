// MusicPlayer 클래스: 음악 재생 시뮬레이션
class MusicPlayer {
    private Music currentMusic;
    private boolean isPlaying;

    public MusicPlayer() {
        this.isPlaying = false;
    }

    public void play(Music music) {
        if (music == null) {
            System.out.println("재생할 음악이 없습니다.");
            return;
        }
        if (isPlaying) {
            stop();
        }
        this.currentMusic = music;
        this.isPlaying = true;
        System.out.println("[🎶 음악 재생 시작] - " + currentMusic.getTitle() + " by " + currentMusic.getArtist());
        // 실제 MP3 재생을 위해서는 JLayer 등의 외부 라이브러리 연동이 필요합니다.
    }

    public void pause() {
        if (isPlaying && currentMusic != null) {
            isPlaying = false;
            System.out.println("[⏸️ 음악 일시정지] - " + currentMusic.getTitle());
        }
    }

    public void stop() {
        if (isPlaying || currentMusic != null) {
            isPlaying = false;
            System.out.println("[⏹️ 음악 정지] - " + currentMusic.getTitle());
            this.currentMusic = null;
        }
    }

    public Music getCurrentMusic() {
        return currentMusic;
    }

    public boolean isPlaying() {
        return isPlaying;
    }
}