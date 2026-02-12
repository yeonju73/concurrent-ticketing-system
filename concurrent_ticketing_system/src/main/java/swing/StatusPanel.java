package swing;

import java.awt.FlowLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import dev.SeatManager;

public class StatusPanel extends JPanel {

    private final JLabel infoLabel = new JLabel();
    private final SeatManager seatManager;

    public StatusPanel(SeatManager seatManager) {
        this.seatManager = seatManager;
        setLayout(new FlowLayout(FlowLayout.CENTER));
        add(infoLabel);
        updateStatus();
    }

    public void updateStatus() {
        int total = seatManager.getTotalSeatCount();
        int booked = seatManager.getBookedSeatCount();
        int remain = total - booked;

        infoLabel.setText(
            "총 좌석: " + total +
            " | 예매 완료: " + booked +
            " | 남은 좌석: " + remain
        );
    }
}
