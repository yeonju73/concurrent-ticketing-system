package swing;

import java.awt.CardLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import dev.SeatManager;
import dev.ServerThread;
import dev.TicketEventListener;
import dev.TicketQueue;
import dev.TicketRequest;
import dev.UserThread;

public class MainFrame extends JFrame implements TicketEventListener {

	private static final long serialVersionUID = 1L;

	private final CardLayout cardLayout = new CardLayout();
	private final JPanel container = new JPanel(cardLayout);

	private TicketQueue queue = new TicketQueue();
	private final SeatManager seatManager;
	private final SeatPanel seatPanel;

	public MainFrame(int rows, int cols) {

        setTitle("티켓팅 시뮬레이션");
        
        seatManager = new SeatManager(rows, cols);

        container.add(new StartPanel(this), "START");
        container.add(new QueuePanel(this), "QUEUE");
        
        seatPanel = new SeatPanel(this, seatManager, rows, cols);
        container.add(seatPanel, "SEAT");

        add(container);

        setSize(600, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
        
        //서버 스레드 시작
        for (int i =0; i < 2; i++) {
        	ServerThread server = new ServerThread(
                    queue,
                    seatManager,
                    seatPanel,
                    this
            );
        	new Thread(server).start();
        }
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

	public SeatPanel getSeatPanel() {
		return seatPanel;
	}

	public SeatManager getSeatManager() {
		return seatManager;
	}

	public TicketQueue getQueue() {
		return queue;
	}

	public void startBots() {
		for (int i = 1; i <= 25; i++) {
			UserThread bot = new UserThread(queue, new TicketRequest("Bot-" + i, true));
			new Thread(bot).start();
		}
	}

	public void startUser() {
		TicketRequest request = new TicketRequest("USER", false);
		UserThread userThread = new UserThread(queue, request);
		new Thread(userThread).start();
	}

	@Override
	public void onUserTurn() {
		SwingUtilities.invokeLater(() -> showSeat());
	}
}
