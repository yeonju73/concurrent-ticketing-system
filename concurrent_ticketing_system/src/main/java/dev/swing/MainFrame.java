package dev.swing;

import java.awt.BorderLayout;
import java.awt.CardLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import dev.SimulationController;
import dev.domain.TicketRequest;
import dev.event.SeatBookedListener;
import dev.event.TicketEventListener;
import dev.queue.TicketQueue;
import dev.server.SeatManager;
import dev.server.ServerThread;

public class MainFrame extends JFrame implements TicketEventListener, SeatBookedListener {

	private static final long serialVersionUID = 1L;

	private final CardLayout cardLayout = new CardLayout();
	private final JPanel container = new JPanel(cardLayout);

	private final TicketQueue queue;

	private final SeatManager seatManager;
	private final SeatPanel seatPanel;
	private final StatusPanel statusPanel;
	
	private final SimulationController simulationController;

	// 현재 유저 정보
	private TicketRequest currentUser;

	public MainFrame(int rows, int cols, TicketQueue queue, SeatManager seatManager) {
		this.queue = queue;
		this.seatManager = seatManager;

		setTitle("티켓팅 시뮬레이션");

		statusPanel = new StatusPanel(seatManager);
		
		simulationController = new SimulationController(queue);
		
		container.add(new StartPanel(this), "START");
		container.add(new QueuePanel(this), "QUEUE");

		seatPanel = new SeatPanel(this, seatManager, rows, cols);
		container.add(seatPanel, "SEAT");

		setLayout(new BorderLayout());
		add(statusPanel, BorderLayout.NORTH);
		add(container, BorderLayout.CENTER);
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

	public SeatPanel getSeatPanel() {
		return seatPanel;
	}

	public SeatManager getSeatManager() {
		return seatManager;
	}

	public TicketQueue getQueue() {
		return queue;
	}
	
	public void startSimulation() {
	    simulationController.startScenario(this);
	    showQueue();
	}

	@Override
	public void onUserTurn() {
		SwingUtilities.invokeLater(() -> showSeat());
	}

	@Override
	public void onSeatBooked(int row, int col) {
		seatPanel.updateSeatUI(row, col);
		statusPanel.updateStatus();
	}
	
	public TicketRequest getCurrentUser() {
		return currentUser;
	}
	
	public void setCurrentUser(TicketRequest request) {
		this.currentUser = request;
	}

}
