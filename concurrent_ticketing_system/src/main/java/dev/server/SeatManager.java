package dev.server;

import java.util.concurrent.atomic.AtomicInteger;

import dev.domain.Seat;

public class SeatManager {
	private final Seat[][] seats;
	private AtomicInteger remainingSeats;
	private int totalSeats;

	public SeatManager(int rows, int cols) {
		seats = new Seat[rows][cols];
		totalSeats = rows * cols;
		remainingSeats = new AtomicInteger(totalSeats);
		
		init(rows, cols);
	}

	private void init(int rows, int cols) {
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				String id = "A-" + (r + 1) + "-" + (c + 1);
				seats[r][c] = new Seat(id);
			}
		}
	}
	
	// 좌석 예약
	// remainingSeats 보호
	public boolean bookSeat(int row, int col) {
		if (seats[row][col].book()) {
			remainingSeats.decrementAndGet(); // atomic 연산
	        return true;
	    }
	    return false;
	}

	public Seat getSeat(int row, int col) {
		return seats[row][col];
	}

	public int getRowCount() {
		return seats.length;
	}

	public int getColCount() {
		return seats[0].length;
	}
	
	public synchronized boolean isSoldOut() {
	    return remainingSeats.get() == 0;
	}
	
	public int getRemainingSeats() {
		return remainingSeats.get();
	}

	public int getTotalSeatCount() {
	    return totalSeats;
	}

	public int getBookedSeatCount() {
		return totalSeats - remainingSeats.get();
	}

}
