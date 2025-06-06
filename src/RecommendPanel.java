import javax.swing.*;
import java.awt.*;
import java.util.List;

// RecommendPanel 클래스: 챗봇, 날씨, 추천 음악 UI
class RecommendPanel extends JPanel {
    private User currentUser;
    private MusicLibrary musicLibrary;
    private MusicPlayer musicPlayer;
    private WeatherAPI weatherAPI;
    private Chatbot chatbot;

    private JLabel weatherDisplayLabel;
    private JTextField cityInputField;
    private JTextArea chatLogArea;
    private JTextField chatInputField;
    private JList<Music> recommendedMusicList;
    private DefaultListModel<Music> recommendedMusicListModel;

    public RecommendPanel(User user, MusicLibrary musicLibrary, MusicPlayer musicPlayer, WeatherAPI weatherAPI, Chatbot chatbot) {
        this.currentUser = user;
        this.musicLibrary = musicLibrary;
        this.musicPlayer = musicPlayer;
        this.weatherAPI = weatherAPI;
        this.chatbot = chatbot;

        setLayout(new BorderLayout());
        initComponents();
        updateWeatherDisplay("Daegu"); // 초기 날씨 정보 로드
    }

    private void initComponents() {
        // 날씨 정보 및 도시 입력
        JPanel weatherPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        weatherDisplayLabel = new JLabel("날씨 정보 로딩 중...");
        weatherDisplayLabel.setFont(new Font("맑은 고딕", Font.BOLD, 18));
        cityInputField = new JTextField("Daegu", 10);
        JButton getWeatherButton = new JButton("날씨 업데이트");
        getWeatherButton.addActionListener(e -> updateWeatherDisplay(cityInputField.getText()));

        weatherPanel.add(new JLabel("현재 날씨: "));
        weatherPanel.add(weatherDisplayLabel);
        weatherPanel.add(new JLabel(" | 도시: "));
        weatherPanel.add(cityInputField);
        weatherPanel.add(getWeatherButton);
        add(weatherPanel, BorderLayout.NORTH);

        // 챗봇 대화 및 추천 음악
        JPanel chatAndRecommendPanel = new JPanel(new GridLayout(1, 2));

        // 챗봇 대화 영역
        JPanel chatPanel = new JPanel(new BorderLayout());
        chatPanel.setBorder(BorderFactory.createTitledBorder("챗봇과 대화"));
        chatLogArea = new JTextArea();
        chatLogArea.setEditable(false);
        chatLogArea.setLineWrap(true);
        chatLogArea.setWrapStyleWord(true);
        JScrollPane chatScrollPane = new JScrollPane(chatLogArea);
        chatPanel.add(chatScrollPane, BorderLayout.CENTER);

        JPanel chatInputPanel = new JPanel(new BorderLayout());
        chatInputField = new JTextField();
        JButton sendChatButton = new JButton("전송");
        sendChatButton.addActionListener(e -> processChatInput());
        chatInputField.addActionListener(e -> processChatInput());
        chatInputPanel.add(chatInputField, BorderLayout.CENTER);
        chatInputPanel.add(sendChatButton, BorderLayout.EAST);
        chatPanel.add(chatInputPanel, BorderLayout.SOUTH);

        chatAndRecommendPanel.add(chatPanel);

        // 추천 음악 목록
        JPanel musicListPanel = new JPanel(new BorderLayout());
        musicListPanel.setBorder(BorderFactory.createTitledBorder("추천 음악"));
        recommendedMusicListModel = new DefaultListModel<>();
        recommendedMusicList = new JList<>(recommendedMusicListModel);
        recommendedMusicList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane musicScrollPane = new JScrollPane(recommendedMusicList);
        musicListPanel.add(musicScrollPane, BorderLayout.CENTER);

        JButton playRecommendedButton = new JButton("선택 곡 재생");
        playRecommendedButton.addActionListener(e -> playSelectedRecommendedMusic());
        musicListPanel.add(playRecommendedButton, BorderLayout.SOUTH);

        chatAndRecommendPanel.add(musicListPanel);

        add(chatAndRecommendPanel, BorderLayout.CENTER);
    }

    private void updateWeatherDisplay(String city) {
        new Thread(() -> {
            String weatherInfo = weatherAPI.getCurrentWeather(city);
            SwingUtilities.invokeLater(() -> {
                weatherDisplayLabel.setText(weatherInfo);
            });
        }).start();
    }

    private void processChatInput() {
        String userInput = chatInputField.getText().trim();
        if (userInput.isEmpty()) return;

        chatLogArea.append("사용자: " + userInput + "\n");
        chatInputField.setText("");

        String currentWeatherDisplayText = weatherDisplayLabel.getText();
        String mainWeatherCondition = extractMainWeatherFromDisplay(currentWeatherDisplayText);

        List<Music> recommendations = chatbot.chatAndRecommend(userInput, mainWeatherCondition);

        String chatbotResponse;
        if (recommendations.isEmpty()) {
            chatbotResponse = "챗봇: 죄송합니다. 추천할 음악을 찾을 수 없습니다.";
        } else {
            String mood = chatbot.analyzeMood(userInput);
            if (mood.equals("분석불가")) {
                chatbotResponse = "챗봇: 입력하신 내용을 정확히 이해하지 못했습니다. 당신의 기분에 맞는 음악을 추천해 드릴게요.";
            } else {
                chatbotResponse = "챗봇: '" + mood + "' 감정이 느껴지시네요. 당신의 기분에 맞는 음악을 추천해 드릴게요.";
            }
        }
        chatLogArea.append(chatbotResponse + "\n");

        recommendedMusicListModel.clear();
        for (Music music : recommendations) {
            recommendedMusicListModel.addElement(music);
        }
    }

    private String extractMainWeatherFromDisplay(String weatherDisplayText) {
        if (weatherDisplayText.contains("맑음")) return "Clear";
        if (weatherDisplayText.contains("흐림")) return "Clouds";
        if (weatherDisplayText.contains("비")) return "Rain";
        if (weatherDisplayText.contains("눈")) return "Snow";
        if (weatherDisplayText.contains("천둥")) return "Thunderstorm";
        if (weatherDisplayText.contains("이슬비")) return "Drizzle";
        if (weatherDisplayText.contains("안개")) return "Mist";
        return "Unknown";
    }

    private void playSelectedRecommendedMusic() {
        Music selectedMusic = recommendedMusicList.getSelectedValue();
        if (selectedMusic != null) {
            musicPlayer.play(selectedMusic);
        } else {
            JOptionPane.showMessageDialog(this, "재생할 음악을 선택해주세요.", "경고", JOptionPane.WARNING_MESSAGE);
        }
    }
}