package swing;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

class StartPanel extends JPanel {

    public StartPanel(MainFrame frame) {

        setLayout(new BorderLayout());

        JLabel title = new JLabel("티켓팅 게임", JLabel.CENTER);

        JButton startBtn = new JButton("게임 시작");
        startBtn.addActionListener(e -> {

            frame.showQueue();     // 화면 전환
            frame.startBots();	   // 봇 스레드 생성
            frame.startUser();	   // 사용자 스레드 생성
            
        });

        add(title, BorderLayout.CENTER);
        add(startBtn, BorderLayout.SOUTH);
    }
}
