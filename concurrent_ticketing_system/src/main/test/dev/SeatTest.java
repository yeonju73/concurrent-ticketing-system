package dev;

import dev.domain.Seat;

public class SeatTest {
    public static void main(String[] args) throws InterruptedException {
        Seat seat = new Seat("A-1");

        Runnable bookTask = () -> {
            boolean success = seat.book();
            System.out.println(Thread.currentThread().getName() + " 예약 시도 → " + success);
        };

        Thread[] threads = new Thread[50];

        for (int i = 0; i < 50; i++) {
            threads[i] = new Thread(bookTask, "Thread-" + i);
            threads[i].start();
        }

        // 모든 스레드가 끝날 때까지 대기
        for (Thread t : threads) {
            t.join();
        }

        System.out.println("최종 상태: booked = " + seat.isBooked());
    }
}
