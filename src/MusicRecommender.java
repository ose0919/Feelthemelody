import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// MusicRecommender 클래스: 날씨 기반 추천 로직 추가
class MusicRecommender {
    private Map<String, List<Music>> moodMusicMap;
    private Map<String, List<Music>> weatherMusicMap;
    private MusicLibrary musicLibrary;

    public MusicRecommender(MusicLibrary musicLibrary) {
        this.musicLibrary = musicLibrary;
        moodMusicMap = new HashMap<>();
        weatherMusicMap = new HashMap<>();
        initializeMusicData();
    }

    private void initializeMusicData() {
        List<Music> allMusic = musicLibrary.getAllMusic();

        moodMusicMap.put("행복", filterMusic(allMusic, "Happy", "Good Day", "Dynamite", "Butter", "Blinding Lights", "Shape of You"));
        moodMusicMap.put("슬픔", filterMusic(allMusic, "Someone Like You", "네버엔딩 스토리"));
        moodMusicMap.put("분노", filterMusic(allMusic, "Eye of the Tiger", "Thunderstruck"));
        moodMusicMap.put("평온", filterMusic(allMusic, "River Flows in You", "Imagine"));
        moodMusicMap.put("기본", filterMusic(allMusic, "Bohemian Rhapsody", "비오는 거리", "어떤 날"));

        weatherMusicMap.put("Clear", filterMusic(allMusic, "Happy", "Good Day", "Dynamite"));
        weatherMusicMap.put("Clouds", filterMusic(allMusic, "Imagine", "River Flows in You"));
        weatherMusicMap.put("Rain", filterMusic(allMusic, "Rainy Day", "Someone Like You"));
        weatherMusicMap.put("Snow", filterMusic(allMusic, "Snowflake Waltz"));
        weatherMusicMap.put("Thunderstorm", filterMusic(allMusic, "Thunderstruck", "Eye of the Tiger"));
        weatherMusicMap.put("Drizzle", filterMusic(allMusic, "Rainy Day"));
        weatherMusicMap.put("Mist", filterMusic(allMusic, "River Flows in You", "Imagine"));
        weatherMusicMap.put("Unknown", moodMusicMap.get("기본")); // 기본 날씨 추천
    }

    private List<Music> filterMusic(List<Music> source, String... titles) {
        List<Music> filtered = new ArrayList<>();
        for (String title : titles) {
            Music music = musicLibrary.findMusicByTitle(title);
            if (music != null) {
                filtered.add(music);
            }
        }
        return filtered;
    }

    public List<Music> recommendMusicByMoodAndWeather(String mood, String weatherCondition) {
        List<Music> moodRecommendations = moodMusicMap.getOrDefault(mood, moodMusicMap.get("기본"));
        List<Music> weatherRecommendations = weatherMusicMap.getOrDefault(weatherCondition, new ArrayList<>());

        List<Music> combinedRecommendations = new ArrayList<>(moodRecommendations);
        for(Music wMusic : weatherRecommendations) {
            if (!combinedRecommendations.contains(wMusic)) {
                combinedRecommendations.add(wMusic);
            }
        }

        if (combinedRecommendations.size() > 5) { // 너무 많은 곡 추천 방지
            return combinedRecommendations.subList(0, Math.min(combinedRecommendations.size(), 5));
        }
        return combinedRecommendations;
    }
}