// EmotionHistoryPanel.java
// '감정 기록' 탭의 UI를 담당하는 JPanel 클래스입니다.
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;


class EmotionHistoryPanel extends JPanel {
    private User currentUser;
    private EmotionHistoryManager emotionHistoryManager;

    private JTable emotionHistoryTable;
    private DefaultTableModel emotionHistoryTableModel;
    private JTextField happyField, sadField, angryField;
    private JTextField dateField;
    private EmotionChartPanel emotionChartPanel; // 새로운 EmotionChartPanel 인스턴스

    public EmotionHistoryPanel(User user, EmotionHistoryManager emotionHistoryManager) {
        this.currentUser = user;
        this.emotionHistoryManager = emotionHistoryManager;

        setLayout(new BorderLayout()); // 전체 레이아웃은 BorderLayout
        setOpaque(true);
        setBackground(UIManager.getColor("Panel.background"));
        initComponents(); // 컴포넌트 초기화
        loadInitialData(); // 테스트용 초기 데이터 로드 (Uimanager에서 이동)
    }

    private void initComponents() {
        // --- 상단: 감정 입력 필드 ---
        JPanel inputPanel = new JPanel(new GridLayout(5, 2, 5, 5)); // 5행 2열 그리드 레이아웃
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // 여백
        inputPanel.setOpaque(false);

        dateField = new JTextField(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))); // 기본값으로 오늘 날짜 설정
        happyField = new JTextField("0"); // 기본값 "0"
        sadField = new JTextField("0");
        angryField = new JTextField("0");
        JButton recordEmotionButton = new JButton("감정 기록");

        dateField.setFont(UIManager.getFont("TextField.font"));
        dateField.setForeground(UIManager.getColor("TextField.foreground"));
        dateField.setBackground(UIManager.getColor("TextField.background"));
        happyField.setFont(UIManager.getFont("TextField.font"));
        happyField.setForeground(UIManager.getColor("TextField.foreground"));
        happyField.setBackground(UIManager.getColor("TextField.background"));
        sadField.setFont(UIManager.getFont("TextField.font"));
        sadField.setForeground(UIManager.getColor("TextField.foreground"));
        sadField.setBackground(UIManager.getColor("TextField.background"));
        angryField.setFont(UIManager.getFont("TextField.font"));
        angryField.setForeground(UIManager.getColor("TextField.foreground"));
        angryField.setBackground(UIManager.getColor("TextField.background"));
        recordEmotionButton.setFont(UIManager.getFont("Button.font"));

        recordEmotionButton.addActionListener(e -> recordEmotion()); // 버튼 클릭 시 감정 기록

        inputPanel.add(new JLabel("날짜 (yyyy-MM-dd):")); inputPanel.add(dateField);
        inputPanel.add(new JLabel("행복 (0-10):"));         inputPanel.add(happyField);
        inputPanel.add(new JLabel("슬픔 (0-10):"));         inputPanel.add(sadField);
        inputPanel.add(new JLabel("화남 (0-10):"));         inputPanel.add(angryField);
        inputPanel.add(new JLabel("")); // 빈 칸
        inputPanel.add(recordEmotionButton);

        add(inputPanel, BorderLayout.NORTH); // 북쪽 영역에 입력 패널 추가

        // --- 중앙: 감정 기록 테이블 및 그래프 ---
        JPanel historyDisplayPanel = new JPanel(new GridLayout(2, 1)); // 테이블과 그래프를 2행 1열로 배치
        historyDisplayPanel.setOpaque(false);

        // 1. 감정 기록 테이블
        String[] columnNames = {"날짜", "행복", "슬픔", "화남"};
        emotionHistoryTableModel = new DefaultTableModel(columnNames, 0); // 초기 행 0
        emotionHistoryTable = new JTable(emotionHistoryTableModel);
        emotionHistoryTable.setFillsViewportHeight(true); // 테이블이 뷰포트를 채우도록 설정
        emotionHistoryTable.setFont(UIManager.getFont("Table.font"));
        emotionHistoryTable.setForeground(UIManager.getColor("Table.foreground"));
        emotionHistoryTable.setBackground(UIManager.getColor("Table.background"));
        emotionHistoryTable.getTableHeader().setFont(UIManager.getFont("TableHeader.font"));
        emotionHistoryTable.getTableHeader().setForeground(UIManager.getColor("TableHeader.foreground"));
        emotionHistoryTable.getTableHeader().setBackground(UIManager.getColor("TableHeader.background"));

        JScrollPane tableScrollPane = new JScrollPane(emotionHistoryTable); // 스크롤 가능
        historyDisplayPanel.add(tableScrollPane);

        // 2. 감정 변화 그래프 (EmotionChartPanel 사용)
        emotionChartPanel = new EmotionChartPanel(); // EmotionChartPanel 인스턴스 생성
        emotionChartPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(UIManager.getColor("Button.background"), 1), "주간 감정 변화 그래프")); // 경계선 및 제목
        historyDisplayPanel.add(emotionChartPanel); // 중앙 영역에 그래프 패널 추가

        add(historyDisplayPanel, BorderLayout.CENTER); // 전체 표시 패널을 중앙에 추가

        updateEmotionHistoryTableAndChart(); // 초기 테이블 및 그래프 로드
    }

    // 초기 감정 기록 데이터를 로드합니다 (테스트용).
    private void loadInitialData() {
        emotionHistoryManager.addEmotionEntry(currentUser.getId(), LocalDate.of(2025, 5, 3), 1, 5, 1);
        emotionHistoryManager.addEmotionEntry(currentUser.getId(), LocalDate.of(2025, 5, 4), 1, 6, 3);
        emotionHistoryManager.addEmotionEntry(currentUser.getId(), LocalDate.of(2025, 5, 5), 4, 4, 4);
        emotionHistoryManager.addEmotionEntry(currentUser.getId(), LocalDate.of(2025, 5, 6), 3, 1, 1);
        emotionHistoryManager.addEmotionEntry(currentUser.getId(), LocalDate.of(2025, 5, 7), 6, 1, 1);
        emotionHistoryManager.addEmotionEntry(currentUser.getId(), LocalDate.of(2025, 5, 8), 2, 2, 2);
        emotionHistoryManager.addEmotionEntry(currentUser.getId(), LocalDate.of(2025, 5, 9), 1, 1, 5);
        updateEmotionHistoryTableAndChart(); // UI 갱신
    }

    // 사용자가 입력한 감정 데이터를 기록합니다.
    private void recordEmotion() {
        try {
            LocalDate date = LocalDate.parse(dateField.getText().trim(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            int happy = Integer.parseInt(happyField.getText());
            int sad = Integer.parseInt(sadField.getText());
            int angry = Integer.parseInt(angryField.getText());

            // 감정 점수 유효성 검사 (0-10 범위)
            if (happy < 0 || happy > 10 || sad < 0 || sad > 10 || angry < 0 || angry > 10) {
                JOptionPane.showMessageDialog(this, "감정 점수는 0에서 10 사이로 입력해주세요.", "입력 오류", JOptionPane.WARNING_MESSAGE);
                return;
            }

            emotionHistoryManager.addEmotionEntry(currentUser.getId(), date, happy, sad, angry);
            JOptionPane.showMessageDialog(this, "감정 기록이 완료되었습니다.");
            updateEmotionHistoryTableAndChart(); // 기록 후 테이블과 그래프 갱신

            // 입력 필드 초기화
            dateField.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            happyField.setText("0");
            sadField.setText("0");
            angryField.setText("0");

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "행복, 슬픔, 화남은 숫자로 입력해주세요.", "입력 오류", JOptionPane.ERROR_MESSAGE);
        } catch (java.time.format.DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "날짜 형식이 올바르지 않습니다. (yyyy-MM-DD)", "입력 오류", JOptionPane.ERROR_MESSAGE);
        }
    }

    // 감정 기록 테이블과 그래프를 갱신합니다.
    private void updateEmotionHistoryTableAndChart() {
        emotionHistoryTableModel.setRowCount(0); // 기존 테이블 데이터 초기화
        List<EmotionEntry> history = emotionHistoryManager.getEmotionHistoryForUser(currentUser.getId());

        // PDF 문서에 언급된 '일주일치' 기록을 위해 최근 7일치 데이터만 테이블에 표시합니다.
        int startIndex = Math.max(0, history.size() - 7);
        for (int i = startIndex; i < history.size(); i++) {
            EmotionEntry entry = history.get(i);
            emotionHistoryTableModel.addRow(new Object[]{
                    entry.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                    entry.getHappy(),
                    entry.getSad(),
                    entry.getAngry()
            });
        }

        // EmotionChartPanel에 데이터 전달 및 다시 그리기 요청
        emotionChartPanel.setEmotionData(history.subList(startIndex, history.size()));
    }
}
