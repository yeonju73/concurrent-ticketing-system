package dev;

import javax.swing.SwingUtilities;

import dev.swing.MainFrame;

public class Main {

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			new MainFrame(16, 16); // 좌석 크기 
		});
	}

}
