package dev;

import swing.MainFrame;

public class SimulationController {

	private final TicketQueue queue;

	public SimulationController(TicketQueue queue) {
		this.queue = queue;
	}

	public void startScenario(MainFrame frame) {

		startBots(1, 10);

		TicketRequest userReq = new TicketRequest("USER", false);

		frame.setCurrentUser(userReq);

		new Thread(new UserThread(queue, userReq), "USER").start();

		startBots(26, 1000);
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
