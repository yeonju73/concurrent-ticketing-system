package dev;

public class Seat {
	private final String id;
	private boolean booked = false;

	public Seat(String id) {
		this.id = id;
	}

	public synchronized boolean book() {
		if (!booked) {
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
