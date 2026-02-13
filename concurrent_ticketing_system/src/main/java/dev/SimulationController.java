package dev;

import dev.domain.TicketRequest;
import dev.queue.TicketQueue;
import dev.swing.MainFrame;

public class SimulationController {

	private final TicketQueue queue;

	public SimulationController(TicketQueue queue) {
		this.queue = queue;
	}

	public void startScenario(MainFrame frame) {

		startBots(1, 130);	// user 앞에 130명

		startUser(frame);	

		startBots(131, 300);	//  user 뒤에 300명
	}

	private void startUser(MainFrame frame) {
		TicketRequest userReq = new TicketRequest("USER", false);

		frame.setCurrentUser(userReq);

		new Thread(new UserThread(queue, userReq), "USER").start();
	}

	// 봇 생성
	private void startBots(int startNo, int count) {

		for (int i = 0; i < count; i++) {

			int botNumber = startNo + i;

			TicketRequest botReq = new TicketRequest("Bot-" + botNumber, true);

			new Thread(new UserThread(queue, botReq), "Bot-" + botNumber).start();
		}
	}
}
