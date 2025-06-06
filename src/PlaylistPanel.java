import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
// PlaylistPanel 클래스: 플레이리스트 관리 UI
class PlaylistPanel extends JPanel {
    private User currentUser;
    private MusicLibrary musicLibrary;
    private PlaylistManager playlistManager;
    private MusicPlayer musicPlayer;

    private JList<Playlist> playlistList;
    private DefaultListModel<Playlist> playlistListModel;
    private JList<Music> playlistSongsList;
    private DefaultListModel<Music> playlistSongsListModel;
    private JTextField newPlaylistNameField;
    private JComboBox<String> availableMusicComboBox;

    public PlaylistPanel(User user, MusicLibrary musicLibrary, PlaylistManager playlistManager, MusicPlayer musicPlayer) {
        this.currentUser = user;
        this.musicLibrary = musicLibrary;
        this.playlistManager = playlistManager;
        this.musicPlayer = musicPlayer;

        setLayout(new BorderLayout());
        initComponents();
        loadInitialData(); // 테스트용 초기 데이터 로드 (Uimanager에서 이동)
    }

    private void initComponents() {
        // 왼쪽: 플레이리스트 목록
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBorder(BorderFactory.createTitledBorder("내 플레이리스트"));
        playlistListModel = new DefaultListModel<>();
        playlistList = new JList<>(playlistListModel);
        JScrollPane playlistScrollPane = new JScrollPane(playlistList);
        playlistList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        leftPanel.add(playlistScrollPane, BorderLayout.CENTER);

        // 플레이리스트 생성 부분
        JPanel createPlaylistPanel = new JPanel(new FlowLayout());
        newPlaylistNameField = new JTextField(15);
        JButton createPlaylistButton = new JButton("생성");
        createPlaylistButton.addActionListener(e -> createNewPlaylist());
        createPlaylistPanel.add(new JLabel("새 플레이리스트 이름:"));
        createPlaylistPanel.add(newPlaylistNameField);
        createPlaylistPanel.add(createPlaylistButton);
        leftPanel.add(createPlaylistPanel, BorderLayout.SOUTH);

        add(leftPanel, BorderLayout.WEST);

        // 오른쪽: 선택된 플레이리스트의 곡 목록 및 곡 추가
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBorder(BorderFactory.createTitledBorder("선택된 플레이리스트 곡 목록"));
        playlistSongsListModel = new DefaultListModel<>();
        playlistSongsList = new JList<>(playlistSongsListModel);
        JScrollPane songsScrollPane = new JScrollPane(playlistSongsList);
        rightPanel.add(songsScrollPane, BorderLayout.CENTER);

        // 곡 추가 및 재생 버튼 부분
        JPanel songActionPanel = new JPanel(new FlowLayout());
        JButton playPlaylistSongButton = new JButton("선택 곡 재생");
        playPlaylistSongButton.addActionListener(e -> playSelectedPlaylistSong());
        songActionPanel.add(playPlaylistSongButton);

        List<String> musicTitles = new ArrayList<>();
        musicLibrary.getAllMusic().forEach(music -> musicTitles.add(music.getTitle()));
        availableMusicComboBox = new JComboBox<>(musicTitles.toArray(new String[0]));

        JButton addSongButton = new JButton("곡 추가");
        addSongButton.addActionListener(e -> addSongToSelectedPlaylist());

        songActionPanel.add(new JLabel("음악 선택:"));
        songActionPanel.add(availableMusicComboBox);
        songActionPanel.add(addSongButton);
        rightPanel.add(songActionPanel, BorderLayout.SOUTH);

        add(rightPanel, BorderLayout.CENTER);

        // 플레이리스트 선택 시 곡 목록 업데이트
        playlistList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                updatePlaylistSongsList();
            }
        });
    }

    private void loadInitialData() {
        // 테스트용 초기 플레이리스트 데이터
        playlistManager.createPlaylist(currentUser.getId(), "내 즐겨찾기");
        playlistManager.createPlaylist(currentUser.getId(), "신나는 음악");
        playlistManager.addSongToPlaylist(currentUser.getId(), "내 즐겨찾기", "Happy");
        playlistManager.addSongToPlaylist(currentUser.getId(), "내 즐겨찾기", "Good Day");
        playlistManager.addSongToPlaylist(currentUser.getId(), "신나는 음악", "Dynamite");
        playlistManager.addSongToPlaylist(currentUser.getId(), "신나는 음악", "Butter");
        updatePlaylistList();
    }

    private void updatePlaylistList() {
        playlistListModel.clear();
        for (Playlist p : playlistManager.getPlaylistsForUser(currentUser.getId())) {
            playlistListModel.addElement(p);
        }
    }

    private void updatePlaylistSongsList() {
        playlistSongsListModel.clear();
        Playlist selectedPlaylist = playlistList.getSelectedValue();
        if (selectedPlaylist != null) {
            for (Music song : selectedPlaylist.getSongs()) {
                playlistSongsListModel.addElement(song);
            }
        }
    }

    private void createNewPlaylist() {
        String playlistName = newPlaylistNameField.getText().trim();
        if (playlistName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "플레이리스트 이름을 입력해주세요.", "경고", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (playlistManager.createPlaylist(currentUser.getId(), playlistName)) {
            JOptionPane.showMessageDialog(this, "'" + playlistName + "' 플레이리스트가 생성되었습니다.");
            newPlaylistNameField.setText("");
            updatePlaylistList();
        } else {
            JOptionPane.showMessageDialog(this, "같은 이름의 플레이리스트가 이미 존재합니다.", "오류", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addSongToSelectedPlaylist() {
        Playlist selectedPlaylist = playlistList.getSelectedValue();
        if (selectedPlaylist == null) {
            JOptionPane.showMessageDialog(this, "먼저 플레이리스트를 선택해주세요.", "경고", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String selectedMusicTitle = (String) availableMusicComboBox.getSelectedItem();
        if (selectedMusicTitle == null || selectedMusicTitle.isEmpty()) {
            JOptionPane.showMessageDialog(this, "추가할 음악을 선택해주세요.", "경고", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (playlistManager.addSongToPlaylist(currentUser.getId(), selectedPlaylist.getName(), selectedMusicTitle)) {
            JOptionPane.showMessageDialog(this, "'" + selectedMusicTitle + "'이(가) '" + selectedPlaylist.getName() + "'에 추가되었습니다.");
            updatePlaylistSongsList();
            updatePlaylistList();
        } else {
            JOptionPane.showMessageDialog(this, "곡 추가에 실패했습니다. (이미 존재하거나 곡을 찾을 수 없음)", "오류", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void playSelectedPlaylistSong() {
        Music selectedMusic = playlistSongsList.getSelectedValue();
        if (selectedMusic != null) {
            musicPlayer.play(selectedMusic);
        } else {
            JOptionPane.showMessageDialog(this, "재생할 플레이리스트의 음악을 선택해주세요.", "경고", JOptionPane.WARNING_MESSAGE);
        }
    }
}