package dev;

import java.util.LinkedList;
import java.util.Queue;

public class TicketQueue {
//	private final int MAX_SIZE = 10;  // 대기열 최대 인원
	private final Queue<TicketRequest> queue = new LinkedList<>();

	private final Object QUEUE_IS_NOT_FULL = new Object();
	private final Object QUEUE_IS_NOT_EMPTY = new Object();
	
	/**
	 * 프로듀서가 큐에 메시지를 적재, TicketQueue는 컨슈머에게 메시지가 큐에 적재되었음을 알림
	 */
	public void enterQueue(TicketRequest user) {
		queue.add(user);
		System.out.println(user.getName() + " 대기열 입장");
		notifyToConsumer();
		// DataQueue는 컨슈머에게 메시지가 큐에 적재되었음을 알림
	}

	/**
	 * 컨슈머가 큐에서 메시지를 꺼내 소비, TicketQueue는 프로듀서에게 큐에 공간이 생겼음을 알림
	 */
	public TicketRequest processTicket() {
		TicketRequest request = queue.poll();
		System.out.println(request.getName() + " 소비");
		notifyToProducer(); // DataQueue는 프로듀서에게 큐에 공간이 생겼음을 알림
		return request;
	}

	/**
	 * 프로듀서는 큐가 가득 찼을 경우, 큐에 여유 공간이 생길 때까지 대기
	 */
	public void waitUntilIsNotFull() {
		synchronized (QUEUE_IS_NOT_FULL) {
			try {
				QUEUE_IS_NOT_FULL.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 컨슈머는 큐가 비어있을 경우, 큐에 새로운 메시지가 적재될 때까지 대기
	 */
	public void waitUntilIsNotEmpty() {
		synchronized (QUEUE_IS_NOT_EMPTY) {
			try {
				QUEUE_IS_NOT_EMPTY.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 컨슈머가 메세지를 소비하면, DataQueue는 notify()를 통해 프로듀서에게 큐에 공간이 생겼음을 알림
	 */
	private void notifyToProducer() {
		synchronized (QUEUE_IS_NOT_FULL) {
			QUEUE_IS_NOT_FULL.notify();
		}
	}

	/**
	 * 프로듀서가 메시지를 적재하면, DataQueue는 컨슈머에게 알림
	 */
	private void notifyToConsumer() {
		synchronized (QUEUE_IS_NOT_EMPTY) {
			QUEUE_IS_NOT_EMPTY.notifyAll();
		}
	}

	public boolean isFull() {
		return false;
//		return queue.size() == MAX_SIZE;
	}

	public boolean isEmpty() {
		return queue.isEmpty();
	}

	public String getQueueElements() {
		return queue.toString();
	}
	
	public int getWaitingCount() {
	    synchronized (queue) {
	        return queue.size();
	    }
	}

	
}
