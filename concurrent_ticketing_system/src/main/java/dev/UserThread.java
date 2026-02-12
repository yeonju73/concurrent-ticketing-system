package dev;

// Producer
public class UserThread implements Runnable {
	private final TicketQueue queue;
	private TicketRequest ticketRequest;

	public UserThread(TicketQueue queue, TicketRequest ticketRequest) {
		this.queue = queue;
		this.ticketRequest = ticketRequest;
	}

	@Override
	public void run() {
		producer();
	}

	private void producer() {
		System.out.println(ticketRequest.getName() + " 대기열 진입 시도");
		queue.enterQueue(ticketRequest);

		// 현실성을 위해 랜덤 시간 동안 슬립
		ThreadUtil.sleep((long) (Math.random() * 100));

	}

}
