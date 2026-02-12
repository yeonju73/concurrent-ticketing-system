package dev;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class TicketQueue {
	private final int MAX_SIZE = 30;  // 대기열 최대 인원
	BlockingQueue<TicketRequest> queue = new ArrayBlockingQueue<>(MAX_SIZE);
	
	/**
	 * 프로듀서가 큐에 메시지를 적재, TicketQueue는 컨슈머에게 메시지가 큐에 적재되었음을 알림
	 */
	public void enterQueue(TicketRequest user) {
		try {
			queue.put(user);
			System.out.println(user.getName() + " 대기열 입장");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 컨슈머가 큐에서 메시지를 꺼내 소비, TicketQueue는 프로듀서에게 큐에 공간이 생겼음을 알림
	 */
	public TicketRequest processTicket() {
		TicketRequest request = null;
		try {
			request = queue.take();
			System.out.println(request.getName() + " 소비");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return request;
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
