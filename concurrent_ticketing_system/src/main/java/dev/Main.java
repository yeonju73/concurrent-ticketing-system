package dev;

import javax.swing.SwingUtilities;

import dev.queue.TicketQueue;
import dev.server.SeatManager;
import dev.server.ServerThread;
import dev.swing.MainFrame;

public class Main {
	// 좌석 배치 크기 상수 (행 / 열)
	final static int ROWS = 16;
	final static int COLS = 16;
	
	public static void main(String[] args) {
		// 공유 대기열 생성
        TicketQueue queue = new TicketQueue();
        
        // 좌석 상태를 관리하는 매니저 생성
        SeatManager seatManager = new SeatManager(ROWS, COLS);

        MainFrame frame = new MainFrame(ROWS, COLS, queue, seatManager);
        
        // 서버 스레드 생성 및 시작
        for (int i = 0; i < 16; i++) {
            ServerThread server = new ServerThread(queue, seatManager, frame, frame);
            new Thread(server, "Server-" + i).start();
        }

        SwingUtilities.invokeLater(() -> frame.setVisible(true));
    }

}
