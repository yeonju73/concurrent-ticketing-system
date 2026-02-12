package dev;

import javax.swing.SwingUtilities;

import swing.MainFrame;

public class Main {

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			new MainFrame(30, 30); // 좌석 크기 
		});
	}

}
