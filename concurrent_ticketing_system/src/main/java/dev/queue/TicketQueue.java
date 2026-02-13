package dev.queue;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicLong;

import dev.domain.TicketRequest;

public class TicketQueue {
	private final int MAX_SIZE = 5000; // 대기열 최대 인원
	BlockingQueue<TicketRequest> queue = new ArrayBlockingQueue<>(MAX_SIZE);

	private AtomicLong sequence = new AtomicLong(0); // 발급 번호
	private AtomicLong serving = new AtomicLong(0); // 현재 처리 중 번호

	/**
	 * 대기열 입장
	 */
	public void enterQueue(TicketRequest user) {
		try {
			// 티켓 번호 발급
			user.setTicketNo(sequence.incrementAndGet());

			// 큐에 삽입
			queue.put(user);
			
			System.out.println(user.getName() + " 대기열 입장 : " + user.getTicketNo());

		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 대기열 처리(소비)
	 */
	public TicketRequest processTicket() {
		try {
			TicketRequest request = queue.take();

			serving.set(request.getTicketNo());

			System.out.println(request.getName() + " 소비");

			return request;

		} catch (InterruptedException e) {

			e.printStackTrace();
			throw new RuntimeException(e);
		}

	}

	public boolean isEmpty() {
		return queue.isEmpty();
	}

	public String getQueueElements() {
		return queue.toString();
	}

	public int getWaitingCount() {
		return queue.size();
	}

	public long getRemainingPosition(long ticketNo) {
	    long current = serving.get();
	    return Math.max(ticketNo - current, 0);
	}
}
