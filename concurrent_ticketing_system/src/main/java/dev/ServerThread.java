package dev;

import dev.listener.SeatBookedListener;
import dev.listener.TicketEventListener;

// Consumer
public class ServerThread implements Runnable {
	private final TicketQueue queue;
	private final SeatManager seatManager;
	private final SeatBookedListener listener;
	private final TicketEventListener ticketEventListener;

	public ServerThread(TicketQueue queue, SeatManager seatManager, SeatBookedListener listener, TicketEventListener ticketEventListener) {
		this.queue = queue;
		this.seatManager = seatManager;
		this.listener = listener;
		this.ticketEventListener = ticketEventListener;
	}

	@Override
	public void run() {
		while (true) { // 계속 서버가 동작하도록 루프 추가
			try {
				// 한 명 소비
				TicketRequest request = queue.processTicket();

				if (request == null) {
					System.out.println(String.format("  [%s] 큐가 비어있음 %n", 
							Thread.currentThread().getName()));
				} else {
					if (request.isBot()) {
						consumeBot(request);
					} else {
						consumeUser(request);
					}
				}
				// 0.5초마다 한 명씩 처리
				Thread.sleep(500);

			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				break; // 인터럽트 시 종료
			}
		}
	}

	private void consumeUser(TicketRequest request) {
		System.out.println("🧑 " + request.getName() + " 입장 허용");

		// Swing 화면 전환
		if (ticketEventListener != null) {
			ticketEventListener.onUserTurn();
	    }

	}

	private void consumeBot(TicketRequest request) {
		try {
			System.out.println("🤖 " + request.getName() + " 입장");

			Thread.sleep(500);

			while (!seatManager.isSoldOut()) {

				int row = (int) (Math.random() * seatManager.getRowCount());
				int col = (int) (Math.random() * seatManager.getColCount());

				boolean success = seatManager.bookSeat(row, col);

				if (success) {
					System.out.println(
							"🤖 " + request.getName() + " 좌석 예약 성공 → " + seatManager.getSeat(row, col).getId());
					// UI 에게 변경 알림
					listener.onSeatBooked(row, col);
					return;
				}
			}

			System.out.println("🤖 " + request.getName() + " 실패 (매진)");

		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}

	}
}
