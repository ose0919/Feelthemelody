import java.util.List;

// Chatbot 클래스: 챗봇 대화 로직
class Chatbot {
    private MusicRecommender recommender;

    public Chatbot(MusicRecommender recommender) {
        this.recommender = recommender;
    }

    public String analyzeMood(String userInput) {
        userInput = userInput.toLowerCase();
        if (userInput.contains("행복") || userInput.contains("기분 좋아") || userInput.contains("신나")) {
            return "행복";
        } else if (userInput.contains("슬퍼") || userInput.contains("우울") || userInput.contains("힘들어")) {
            return "슬픔";
        } else if (userInput.contains("화나") || userInput.contains("짜증나") || userInput.contains("스트레스")) {
            return "분노";
        } else if (userInput.contains("평화") || userInput.contains("편안") || userInput.contains("조용")) {
            return "평온";
        } else {
            return "분석불가";
        }
    }

    public List<Music> chatAndRecommend(String userInput, String weatherCondition) {
        String mood = analyzeMood(userInput);
        List<Music> recommendedSongs;

        if (mood.equals("분석불가")) {
            recommendedSongs = recommender.recommendMusicByMoodAndWeather("기본", weatherCondition);
        } else {
            recommendedSongs = recommender.recommendMusicByMoodAndWeather(mood, weatherCondition);
        }
        return recommendedSongs;
    }
}


