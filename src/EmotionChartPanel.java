// EmotionChartPanel.java
// 감정 기록 데이터를 받아 꺾은선 그래프를 직접 그리는 JPanel입니다.
import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

class EmotionChartPanel extends JPanel {
    private List<EmotionEntry> emotionData; // 그래프에 표시할 감정 데이터

    public EmotionChartPanel() {
        this.emotionData = new ArrayList<>();
        // 패널 배경색을 UIManager에서 가져와 설정
        setBackground(UIManager.getColor("Panel.background"));
        setOpaque(true); // 배경색이 보이도록 설정
    }

    // 외부에서 감정 데이터를 설정하는 메서드
    public void setEmotionData(List<EmotionEntry> data) {
        this.emotionData = data;
        repaint(); // 데이터가 변경되면 패널을 다시 그리도록 요청
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); // 기본 그리기 수행
        Graphics2D g2d = (Graphics2D) g;

        // 안티앨리어싱 설정 (선을 부드럽게)
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // 패널 크기
        int width = getWidth();
        int height = getHeight();

        // 여백 설정
        int padding = 30;
        int labelPadding = 20;

        // 그래프 영역 크기
        int chartWidth = width - 2 * padding - labelPadding;
        int chartHeight = height - 2 * padding - labelPadding;

        // 데이터가 없으면 그리지 않음
        if (emotionData.isEmpty() || chartWidth <= 0 || chartHeight <= 0) {
            g2d.setColor(UIManager.getColor("Label.foreground"));
            g2d.setFont(new Font("SansSerif", Font.PLAIN, 12));
            String noDataMsg = "감정 데이터가 없습니다.";
            FontMetrics fm = g2d.getFontMetrics();
            int x = (width - fm.stringWidth(noDataMsg)) / 2;
            int y = (height - fm.getHeight()) / 2 + fm.getAscent();
            g2d.drawString(noDataMsg, x, y);
            return;
        }

        // X축 (날짜)
        // 최근 7일치만 표시 (PDF 문서와 일치)
        int startIndex = Math.max(0, emotionData.size() - 7);
        List<EmotionEntry> dataToDisplay = emotionData.subList(startIndex, emotionData.size());

        // X축 간격 (각 날짜의 X좌표)
        double xStep = (double) chartWidth / (dataToDisplay.size() > 1 ? (dataToDisplay.size() - 1) : 1);

        // Y축 (감정 수치 0-10)
        double yScale = (double) chartHeight / 10; // 0-10 스케일

        // X, Y축 그리기
        g2d.setColor(UIManager.getColor("Label.foreground").darker()); // 축 색상
        g2d.drawLine(padding + labelPadding, height - padding - labelPadding, width - padding, height - padding - labelPadding); // X축
        g2d.drawLine(padding + labelPadding, padding, padding + labelPadding, height - padding - labelPadding); // Y축

        // Y축 레이블 (0, 5, 10)
        g2d.setFont(new Font("SansSerif", Font.PLAIN, 10));
        for (int i = 0; i <= 10; i += 5) {
            int yCoord = height - padding - labelPadding - (int)(i * yScale);
            g2d.drawString(String.valueOf(i), padding + labelPadding - 15, yCoord + 5);
            g2d.drawLine(padding + labelPadding - 3, yCoord, padding + labelPadding, yCoord); // 작은 눈금
        }

        // X축 날짜 레이블
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd");
        for (int i = 0; i < dataToDisplay.size(); i++) {
            EmotionEntry entry = dataToDisplay.get(i);
            String dateLabel = entry.getDate().format(formatter);
            int xCoord = padding + labelPadding + (int)(i * xStep);
            g2d.setFont(new Font("SansSerif", Font.PLAIN, 10));
            FontMetrics fm = g2d.getFontMetrics();
            int labelWidth = fm.stringWidth(dateLabel);
            g2d.drawString(dateLabel, xCoord - labelWidth / 2, height - padding - labelPadding + 15);
        }

        // 감정 꺾은선 그리기
        // Happy: 빨간색
        // Sad: 파란색
        // Angry: 주황색
        Color happyColor = new Color(255, 100, 100);
        Color sadColor = new Color(100, 100, 255);
        Color angryColor = new Color(255, 150, 50);

        Stroke stroke = new BasicStroke(2f); // 선 두께
        g2d.setStroke(stroke);

        // Happy 라인 그리기
        g2d.setColor(happyColor);
        drawEmotionLine(g2d, dataToDisplay, "happy", xStep, yScale, padding + labelPadding, height - padding - labelPadding);

        // Sad 라인 그리기
        g2d.setColor(sadColor);
        drawEmotionLine(g2d, dataToDisplay, "sad", xStep, yScale, padding + labelPadding, height - padding - labelPadding);

        // Angry 라인 그리기
        g2d.setColor(angryColor);
        drawEmotionLine(g2d, dataToDisplay, "angry", xStep, yScale, padding + labelPadding, height - padding - labelPadding);

        // 범례 그리기
        int legendX = width - padding - 80;
        int legendY = padding;
        int lineHeight = 20;
        g2d.setFont(new Font("SansSerif", Font.BOLD, 12));

        g2d.setColor(happyColor); g2d.fillRect(legendX, legendY, 10, 10); g2d.setColor(UIManager.getColor("Label.foreground")); g2d.drawString("행복", legendX + 15, legendY + 9);
        g2d.setColor(sadColor); g2d.fillRect(legendX, legendY + lineHeight, 10, 10); g2d.setColor(UIManager.getColor("Label.foreground")); g2d.drawString("슬픔", legendX + 15, legendY + lineHeight + 9);
        g2d.setColor(angryColor); g2d.fillRect(legendX, legendY + 2 * lineHeight, 10, 10); g2d.setColor(UIManager.getColor("Label.foreground")); g2d.drawString("화남", legendX + 15, legendY + 2 * lineHeight + 9);
    }

    // 꺾은선과 점을 그리는 헬퍼 메서드
    private void drawEmotionLine(Graphics2D g2d, List<EmotionEntry> dataToDisplay, String emotionType, double xStep, double yScale, int xOrigin, int yOrigin) {
        for (int i = 0; i < dataToDisplay.size(); i++) {
            EmotionEntry entry = dataToDisplay.get(i);
            int value;
            if (emotionType.equals("happy")) value = entry.getHappy();
            else if (emotionType.equals("sad")) value = entry.getSad();
            else value = entry.getAngry(); // angry

            int x1 = xOrigin + (int)(i * xStep);
            int y1 = yOrigin - (int)(value * yScale);

            // 점 그리기
            int circleRadius = 4;
            g2d.fillOval(x1 - circleRadius, y1 - circleRadius, 2 * circleRadius, 2 * circleRadius);

            // 다음 점까지 선 그리기
            if (i < dataToDisplay.size() - 1) {
                EmotionEntry nextEntry = dataToDisplay.get(i + 1);
                int nextValue;
                if (emotionType.equals("happy")) nextValue = nextEntry.getHappy();
                else if (emotionType.equals("sad")) nextValue = nextEntry.getSad();
                else nextValue = nextEntry.getAngry();

                int x2 = xOrigin + (int)((i + 1) * xStep);
                int y2 = yOrigin - (int)(nextValue * yScale);
                g2d.drawLine(x1, y1, x2, y2);
            }
        }
    }
}
