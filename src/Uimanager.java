import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class Uimanager {
    private JFrame frame;
    private HashMap<String, User> users = new HashMap<>();
    private User currentUser;

    private MusicLibrary musicLibrary;
    private PlaylistManager playlistManager;
    private EmotionHistoryManager emotionHistoryManager;
    private MusicPlayer musicPlayer;
    private WeatherAPI weatherAPI;
    private MusicRecommender musicRecommender;
    private Chatbot chatbot;

    public Uimanager() {
        // UI 기본값 설정 호출 (가장 먼저 호출되어야 합니다.)
        setupUIDefaults();

        musicLibrary = new MusicLibrary();
        musicPlayer = new MusicPlayer();
        weatherAPI = new WeatherAPI();
        musicRecommender = new MusicRecommender(musicLibrary);
        chatbot = new Chatbot(musicRecommender);
        playlistManager = new PlaylistManager(musicLibrary);
        emotionHistoryManager = new EmotionHistoryManager();

        users.put("testuser", new User("testuser", "password123", "테스트 사용자", "2000-01-01", "Male"));
        users.put("user1", new User("user1", "pass123", "김철수", "1995-05-10", "Female"));
    }

    // UI 기본값 설정 메서드: 폰트, 색상 등을 정의합니다.
    private void setupUIDefaults() {
        // 색상 팔레트 정의
        Color lightBlueBackground = new Color(220, 240, 255); // 밝은 하늘색 배경
        Color darkBlueText = new Color(50, 100, 150);       // 어두운 파란색 글자
        Color buttonBlue = new Color(120, 180, 230);        // 버튼용 파란색
        Color buttonHoverBlue = new Color(90, 150, 210);    // 버튼 호버용 (사용되지 않을 수 있음)
        Color tableHeaderBlue = new Color(180, 210, 240);   // 테이블 헤더용

        // 폰트 설정 (귀여운 느낌을 위해 큰 크기와 굵게)
        // 시스템에 "SansSerif" 폰트가 없는 경우 기본 시스템 폰트가 사용됩니다.
        Font generalFont = new Font("SansSerif", Font.PLAIN, 14);
        Font boldFont = new Font("SansSerif", Font.BOLD, 16);
        Font titleFont = new Font("SansSerif", Font.BOLD, 24); // 로그인 화면 제목용 더 큰 폰트

        // UIManager 기본값 설정: 애플리케이션 전반에 걸쳐 적용됩니다.
        UIManager.put("Label.font", generalFont);
        UIManager.put("Label.foreground", darkBlueText);

        UIManager.put("Button.font", boldFont);
        UIManager.put("Button.background", buttonBlue);
        UIManager.put("Button.foreground", Color.WHITE); // 버튼 글자색 흰색
        UIManager.put("Button.border", BorderFactory.createLineBorder(buttonBlue.darker(), 1)); // 버튼 테두리
        UIManager.put("Button.focusPainted", false); // 포커스 테두리 제거 (더 깔끔하게 보임)

        UIManager.put("TextField.font", generalFont);
        UIManager.put("TextField.background", Color.WHITE);
        UIManager.put("TextField.foreground", darkBlueText.darker());
        UIManager.put("TextField.caretForeground", darkBlueText.darker()); // 커서 색

        UIManager.put("PasswordField.font", generalFont);
        UIManager.put("PasswordField.background", Color.WHITE);
        UIManager.put("PasswordField.foreground", darkBlueText.darker());
        UIManager.put("PasswordField.caretForeground", darkBlueText.darker());

        UIManager.put("TextArea.font", generalFont);
        UIManager.put("TextArea.background", Color.WHITE);
        UIManager.put("TextArea.foreground", darkBlueText.darker());

        UIManager.put("ComboBox.font", generalFont);
        UIManager.put("ComboBox.background", Color.WHITE);
        UIManager.put("ComboBox.foreground", darkBlueText);

        UIManager.put("List.font", generalFont);
        UIManager.put("List.background", Color.WHITE);
        UIManager.put("List.foreground", darkBlueText);
        UIManager.put("List.selectionBackground", buttonBlue.brighter()); // 선택된 항목 배경색
        UIManager.put("List.selectionForeground", Color.WHITE); // 선택된 항목 글자색

        UIManager.put("Table.font", generalFont);
        UIManager.put("Table.background", Color.WHITE);
        UIManager.put("Table.foreground", darkBlueText);
        UIManager.put("Table.selectionBackground", buttonBlue.brighter());
        UIManager.put("Table.selectionForeground", Color.WHITE);

        UIManager.put("TableHeader.font", boldFont);
        UIManager.put("TableHeader.background", tableHeaderBlue);
        UIManager.put("TableHeader.foreground", Color.WHITE);
        UIManager.put("TableHeader.cellBorder", BorderFactory.createLineBorder(tableHeaderBlue.darker(), 1));

        UIManager.put("Panel.background", lightBlueBackground); // 모든 JPanel의 기본 배경색
        UIManager.put("Viewport.background", lightBlueBackground); // JScrollPane 내부 배경색

        // JTabbedPane 스타일
        UIManager.put("TabbedPane.font", boldFont);
        UIManager.put("TabbedPane.background", lightBlueBackground.darker()); // 탭 컨테이너의 배경
        UIManager.put("TabbedPane.selectedBackground", lightBlueBackground); // 선택된 탭의 배경
        UIManager.put("TabbedPane.contentBackground", lightBlueBackground); // 탭 내용 패널의 배경
        UIManager.put("TabbedPane.foreground", darkBlueText); // 탭 글자색
        UIManager.put("TabbedPane.selectedForeground", darkBlueText.darker()); // 선택된 탭 글자색

        // JOptionPane 스타일 (메시지 다이얼로그)
        UIManager.put("OptionPane.background", lightBlueBackground.brighter());
        UIManager.put("Button.background", buttonBlue); // JOptionPane 내 버튼 색
        UIManager.put("Button.foreground", Color.WHITE);

        // TitledBorder 폰트 및 색상 (각 패널의 제목 테두리)
        UIManager.put("TitledBorder.font", boldFont);
        UIManager.put("TitledBorder.titleColor", darkBlueText.darker());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Uimanager().showLogin());
    }

    // 로그인 화면을 표시합니다.
    public void showLogin() {
        if (frame != null) {
            frame.dispose();
        }
        frame = new JFrame("Feel The Melody - 로그인");
        frame.setSize(400, 300); // 제목을 위해 높이 약간 증가
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.getContentPane().setBackground(UIManager.getColor("Panel.background"));

        // 메인 패널 (제목 + 로그인 필드/버튼)
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setOpaque(false); // 배경색을 부모에게서 상속받도록 설정 (투명)

        // 상단 제목 레이블
        JLabel titleLabel = new JLabel("Feel The Melody (F.T.M.)", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 30)); // 더 크고 굵은 폰트
        titleLabel.setForeground(UIManager.getColor("Label.foreground").darker()); // 더 진한 글자색
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0)); // 상하 여백
        mainPanel.add(titleLabel, BorderLayout.NORTH); // 북쪽에 제목 추가

        // 로그인 필드 및 버튼 패널
        JPanel loginInputPanel = new JPanel(new GridLayout(3, 2, 10, 10)); // 3행 2열
        loginInputPanel.setBorder(BorderFactory.createEmptyBorder(10, 30, 20, 30)); // 좌우 여백
        loginInputPanel.setOpaque(false);

        JLabel idLabel = new JLabel("아이디:");
        JTextField idField = new JTextField();
        JLabel pwLabel = new JLabel("비밀번호:");
        JPasswordField pwField = new JPasswordField();
        JButton loginBtn = new JButton("로그인");
        JButton registerBtn = new JButton("회원가입");

        loginInputPanel.add(idLabel); loginInputPanel.add(idField);
        loginInputPanel.add(pwLabel); loginInputPanel.add(pwField);
        loginInputPanel.add(loginBtn); loginInputPanel.add(registerBtn);

        mainPanel.add(loginInputPanel, BorderLayout.CENTER); // 중앙에 입력 패널 추가
        frame.add(mainPanel); // 프레임에 메인 패널 추가


        loginBtn.addActionListener(e -> {
            Loginmanager lm = new Loginmanager(users);
            if (lm.login(idField.getText(), new String(pwField.getPassword()))) {
                currentUser = users.get(idField.getText());
                JOptionPane.showMessageDialog(frame, "로그인 성공!");
                showHome();
            } else {
                JOptionPane.showMessageDialog(frame, "아이디 또는 비밀번호가 잘못되었습니다.", "로그인 실패", JOptionPane.ERROR_MESSAGE);
            }
        });

        registerBtn.addActionListener(e -> showRegister());

        frame.setVisible(true);
    }

    // 회원가입 화면을 표시합니다.
    public void showRegister() {
        JFrame regFrame = new JFrame("Feel The Melody - 회원가입");
        regFrame.setSize(400, 350);
        regFrame.setLayout(new GridLayout(7, 2, 10, 10));
        regFrame.setLocationRelativeTo(null);
        regFrame.getContentPane().setBackground(UIManager.getColor("Panel.background"));

        JTextField idField = new JTextField();
        JPasswordField pwField = new JPasswordField();
        JTextField nameField = new JTextField();
        JTextField birthField = new JTextField();
        JComboBox<String> genderBox = new JComboBox<>(new String[]{"남자", "여자", "기타"});
        JButton registerConfirmBtn = new JButton("회원가입");
        JButton backToLoginBtn = new JButton("뒤로");

        regFrame.add(new JLabel("아이디:")); regFrame.add(idField);
        regFrame.add(new JLabel("비밀번호:")); regFrame.add(pwField);
        regFrame.add(new JLabel("이름:")); regFrame.add(nameField);
        regFrame.add(new JLabel("생년월일(YYYY-MM-DD):")); regFrame.add(birthField);
        regFrame.add(new JLabel("성별:")); regFrame.add(genderBox);
        regFrame.add(registerConfirmBtn); regFrame.add(backToLoginBtn);

        Register reg = new Register(users);
        registerConfirmBtn.addActionListener(e -> {
            String result = reg.registerUser(idField.getText(), new String(pwField.getPassword()), nameField.getText(), birthField.getText(), (String) genderBox.getSelectedItem());
            JOptionPane.showMessageDialog(regFrame, result);
            if (result.equals("회원가입 성공")) {
                regFrame.dispose();
                showLogin();
            }
        });
        backToLoginBtn.addActionListener(e -> {
            regFrame.dispose();
            showLogin();
        });

        regFrame.setVisible(true);
    }

    // 홈 화면을 표시합니다. (로그인 성공 후)
    public void showHome() {
        frame.dispose();

        frame = new JFrame("Feel The Melody - 홈 (" + currentUser.getName() + "님)");
        frame.setSize(1000, 700);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.getContentPane().setBackground(UIManager.getColor("Panel.background"));

        JTabbedPane tabbedPane = new JTabbedPane();

        RecommendPanel recommendPanel = new RecommendPanel(currentUser, musicLibrary, musicPlayer, weatherAPI, chatbot);
        tabbedPane.addTab("추천 음악 & 날씨", recommendPanel);

        PlaylistPanel playlistPanel = new PlaylistPanel(currentUser, musicLibrary, playlistManager, musicPlayer);
        tabbedPane.addTab("플레이리스트", playlistPanel);

        EmotionHistoryPanel emotionHistoryPanel = new EmotionHistoryPanel(currentUser, emotionHistoryManager);
        tabbedPane.addTab("감정 기록", emotionHistoryPanel);

        JButton logoutBtn = new JButton("로그아웃");
        logoutBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(frame, "로그아웃 하시겠습니까?", "로그아웃", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                currentUser = null;
                JOptionPane.showMessageDialog(frame, "로그아웃 되었습니다.");
                showLogin();
            }
        });

        JPanel homePanel = new JPanel(new BorderLayout());
        homePanel.setOpaque(false);
        homePanel.add(tabbedPane, BorderLayout.CENTER);
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footerPanel.setOpaque(false);
        footerPanel.add(logoutBtn);
        homePanel.add(footerPanel, BorderLayout.SOUTH);

        frame.add(homePanel);
        frame.setVisible(true);
    }
}