package dev;

public class SeatManager {
	private final Seat[][] seats;

	public SeatManager(int rows, int cols) {
		seats = new Seat[rows][cols];

		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				String id = "A-" + (r + 1) + "-" + (c + 1);
				seats[r][c] = new Seat(id);
			}
		}
	}
	
	// 좌석 예약
	public boolean bookSeat(int row, int col) {
	    return seats[row][col].book();
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

}
