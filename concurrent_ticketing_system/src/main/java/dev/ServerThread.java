package dev;

// Consumer
public class ServerThread implements Runnable {
	private TicketQueue queue;

	public ServerThread(TicketQueue queue) {
		this.queue = queue;
	}

	@Override
    public void run() {
        while (true) {   // 계속 서버가 동작하도록 루프 추가
            try {

                // 큐가 비어있으면 대기
                if (queue.isEmpty()) {
                    queue.waitUntilIsNotEmpty();
                }

                // 한 명 소비
                String name = queue.processTicket();
                consumeMessage(name);

                // 5초마다 한 명씩 처리
                Thread.sleep(5000);

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;  // 인터럽트 시 종료
            }
        }
    }

	private void consumeMessage(String name) {
		System.out.println(name + " 소비");
	}
}
