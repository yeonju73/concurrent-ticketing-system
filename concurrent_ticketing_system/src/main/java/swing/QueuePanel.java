package swing;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;


class QueuePanel extends JPanel {

    public QueuePanel(MainFrame frame) {

        setLayout(new BorderLayout());

        JLabel label = new JLabel("대기열..", JLabel.CENTER);
        
        Timer timer = new Timer(500, e -> {
            int count = frame.getQueue().getWaitingCount();
            label.setText("앞에 " + count + " 명 대기 중");
        });
        timer.start();

        add(label, BorderLayout.CENTER);
    }
}
