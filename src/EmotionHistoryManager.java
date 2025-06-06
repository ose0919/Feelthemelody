import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// EmotionHistoryManager 클래스: 사용자의 감정 기록을 관리하고 조회합니다.
class EmotionHistoryManager {
    private Map<String, List<EmotionEntry>> userEmotionHistory;

    public EmotionHistoryManager() {
        this.userEmotionHistory = new HashMap<>();
    }

    public void addEmotionEntry(String userId, LocalDate date, int happy, int sad, int angry) {
        List<EmotionEntry> history = userEmotionHistory.computeIfAbsent(userId, k -> new ArrayList<>());

        EmotionEntry existingEntry = null;
        for (EmotionEntry entry : history) {
            if (entry.getDate().equals(date)) {
                existingEntry = entry;
                break;
            }
        }

        if (existingEntry != null) {
            history.remove(existingEntry);
            history.add(new EmotionEntry(date, happy, sad, angry));
        } else {
            history.add(new EmotionEntry(date, happy, sad, angry));
        }
        history.sort((e1, e2) -> e1.getDate().compareTo(e2.getDate()));
    }

    public List<EmotionEntry> getEmotionHistoryForUser(String userId) {
        return userEmotionHistory.getOrDefault(userId, new ArrayList<>());
    }

    // JFreeChart를 사용하여 감정 기록 그래프를 생성하는 메서드
    /*public ChartPanel createEmotionChartPanel(String userId) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        List<EmotionEntry> history = getEmotionHistoryForUser(userId);

        int startIndex = Math.max(0, history.size() - 7);
        for (int i = startIndex; i < history.size(); i++) {
            EmotionEntry entry = history.get(i);
            String dateLabel = entry.getDate().format(DateTimeFormatter.ofPattern("MM/dd"));
            dataset.addValue(entry.getHappy(), "행복", dateLabel);
            dataset.addValue(entry.getSad(), "슬픔", dateLabel);
            dataset.addValue(entry.getAngry(), "화남", dateLabel);
        }

        JFreeChart lineChart = ChartFactory.createLineChart(
                "주간 감정 변화", "날짜", "감정 수치 (0-10)",
                dataset, PlotOrientation.VERTICAL, true, true, false
        );
        return new ChartPanel(lineChart);
    }*/
}