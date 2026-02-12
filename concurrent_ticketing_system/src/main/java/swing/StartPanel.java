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
            
            // TODO: 여기에 대기열 넣으면 될것같아요!!!!
        });

        add(title, BorderLayout.CENTER);
        add(startBtn, BorderLayout.SOUTH);
    }
}
