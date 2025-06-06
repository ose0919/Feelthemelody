import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

// EmotionEntry 클래스: 특정 날짜의 감정 상태(Happy, Sad, Angry)를 기록합니다.
class EmotionEntry {
    private LocalDate date;
    private int happy;
    private int sad;
    private int angry;

    public EmotionEntry(LocalDate date, int happy, int sad, int angry) {
        this.date = date;
        this.happy = happy;
        this.sad = sad;
        this.angry = angry;
    }

    public LocalDate getDate() {
        return date;
    }

    public int getHappy() {
        return happy;
    }

    public int getSad() {
        return sad;
    }

    public int getAngry() {
        return angry;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return "날짜: " + date.format(formatter) + ", 행복: " + happy + ", 슬픔: " + sad + ", 화남: " + angry;
    }
}