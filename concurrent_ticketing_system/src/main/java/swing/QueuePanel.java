package swing;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

class QueuePanel extends JPanel {

    public QueuePanel(MainFrame frame) {

        setLayout(new BorderLayout());

        JLabel label = new JLabel("대기열..", JLabel.CENTER);

        JButton nextBtn = new JButton("내 차례!");
        nextBtn.addActionListener(e -> frame.showSeat());

        add(label, BorderLayout.CENTER);
        add(nextBtn, BorderLayout.SOUTH);
    }
}
