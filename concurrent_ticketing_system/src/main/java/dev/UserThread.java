package dev;

// Producer
public class UserThread implements Runnable {
	private TicketQueue queue;
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
		// 큐가 가득찼을 경우,
		if (queue.isFull()) {
			// 프로듀서는 큐에 여유 공간이 생길 때까지 대기
			System.out.println(ticketRequest.getName() + " 대기열 가득 → 기다림");
			queue.waitUntilIsNotFull();
		}
		// 이후 대기 상태에서 깨어나면 큐에 메시지를 적재, 대기중인 컨슈머에게 알림
		queue.enterQueue(ticketRequest);

		// 현실성을 위해 랜덤 시간 동안 슬립
		ThreadUtil.sleep((long) (Math.random() * 100));

	}

}
