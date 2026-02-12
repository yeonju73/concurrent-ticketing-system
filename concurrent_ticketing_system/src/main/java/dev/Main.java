package dev;

import javax.swing.SwingUtilities;

import swing.MainFrame;

public class Main {

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			new MainFrame(6, 6); // 좌석 크기 
		});
	}

}
