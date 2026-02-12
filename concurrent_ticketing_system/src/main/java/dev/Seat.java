package dev;

public class Seat {
	private final String id;
	private boolean booked = false;

	public Seat(String id) {
		this.id = id;
	}

	public synchronized boolean book() {
		if (!booked) {
			// 동시에 접근 시도 확률를 키우기 위해 임의로 딜레이 설정
			try { Thread.sleep(1); } catch (InterruptedException ignored) {}
			booked = true;
			return true;
		}
		return false;
	}

	public boolean isBooked() {
		return booked;
	}

	public String getId() {
		return id;
	}
}
