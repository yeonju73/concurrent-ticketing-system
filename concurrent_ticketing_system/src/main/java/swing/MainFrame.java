package swing;
import java.awt.CardLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class MainFrame extends JFrame {

    private static final long serialVersionUID = 1L;

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel container = new JPanel(cardLayout);

    public MainFrame(int rows, int cols) {

        setTitle("티켓팅 시뮬레이션");

        container.add(new StartPanel(this), "START");
        container.add(new QueuePanel(this), "QUEUE");
        container.add(new SeatPanel(this, rows, cols), "SEAT");

        add(container);

        setSize(600, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);

        showStart();
    }

    public void showStart() {
        cardLayout.show(container, "START");
    }

    public void showQueue() {
        cardLayout.show(container, "QUEUE");
    }

    public void showSeat() {
        cardLayout.show(container, "SEAT");
    }
}
