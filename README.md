# 🎮 Concurrent Ticketing Simulator

## 1. 프로젝트 목적

본 프로그램은 **멀티스레드 환경에서의 동시성 문제와 thread-safe 설계의 중요성**을 시각적으로 보여주기 위한 티켓팅 시뮬레이션 프로그램이다.

* 다수의 **Bot Thread**와 **사용자 Thread**가 동시에 좌석 자원에 접근하는 상황 구성
* 동기화 여부에 따른 데이터 무결성 차이를 실험적으로 확인 가능
* UI를 통해 실시간 좌석 상태 및 대기열 현황을 확인 가능

---

## 2. 전체 시나리오
<img width="533" height="532" alt="image" src="https://github.com/user-attachments/assets/50cb5ac1-cc91-4b8f-948b-37c065aef331" />

1. 사용자가 프로그램 실행
2. “게임 시작” 클릭 → 대기열(Queue) 진입
3. 동시에 여러 Bot Thread도 대기열 Queue 진입
4. **서버 스레드(ServerThread)** 가 일정 간격으로 대기열을 소비
5. 차례가 되면 좌석 선택 화면(SeatPanel) 진입
6. Bot은 랜덤 좌석 선택
7. 사용자와 Bot이 동일 좌석을 공유하며 경쟁
8. 좌석은 한 번만 예매 가능 (**thread-safe 설계**)

---

## 3. 동시성 처리

### 3-1. 공유 자원

본 프로젝트에서 동시성 처리를 위해 고려한 공유 자원은 크게 3가지이다:

| 공유 자원                          | 설명                | thread-safe 처리 방식                                                                  |
| ------------------------------ | ----------------- | ---------------------------------------------------------------------------------- |
| **TicketQueue**                | 대기열에 들어온 사용자/봇 요청 | `BlockingQueue` 사용 → 자동으로 입출력 시 동기화, `put()`/`take()` 메소드로 producer-consumer 패턴 구현 |
| **SeatManager.remainingSeats** | 남은 좌석 수           | `AtomicInteger` 사용 → 좌석 예약 시 decrementAndGet()로 원자적 감소 처리          |
| **Seat(개별 좌석)**                | 실제 좌석 하나          | `book()` 메소드를 통해 예약 시 단일 스레드만 성공 가능하도록 처리 (임계영역 보호)                                |

### 3-2. 처리 순서

1. **Producer (UserThread/BotThread)** → `TicketQueue.add()`로 요청 등록
2. **Consumer (ServerThread)** → `TicketQueue.take()`로 요청 소비
3. **SeatManager.bookSeat(row, col)** 호출

   * 내부적으로 `Seat.book()` 호출 → 단일 스레드만 좌석 예약 성공
   * 예약 성공 시 `remainingSeats.decrementAndGet()` 호출 → `AtomicInteger`로 남은 좌석 수를 원자적으로 감소
4. UI 업데이트는 **리스너(listener)를 통해 Swing EDT에서 안전하게 처리**

> ✔️ `BlockingQueue`와 `synchronized`를 적절히 사용하여 **여러 스레드가 동시에 다른 좌석을 예약** 가능하도록 설계

---

## 4. UI 구조 및 Listener

### 4-1. Swing UI

* **StartPanel** : 게임 시작 버튼
* **QueuePanel** : 대기열 상태 표시, 남은 대기열 수
* **SeatPanel** : 좌석 그리드, 좌석 색상으로 상태 표시 (예약 가능 → 보라, 예약됨 → 회색)
* **StatusPanel** : 총 좌석 수 / 남은 좌석 수 / 예매된 좌석 수 표시

### 4-2. Listener 구조 (Observer Pattern)

* **TicketEventListener** : 사용자 차례가 되었을 때 좌석 화면으로 전환
* **SeatBookedListener** : 좌석이 예약될 때 UI 갱신

```java
public interface TicketEventListener {
    void onUserTurn(); // 사용자의 차례 알림
}

public interface SeatBookedListener {
    void onSeatBooked(int row, int col); // 좌석 예약 시 UI 갱신
}
```

* 서버 스레드는 이벤트를 발생시키고, UI(MainFrame)는 리스너를 통해 **Swing EDT 안전하게 UI 업데이트**
* UI와 로직을 분리하여 **책임 분리 및 확장성 확보**

> ✔️ Observer 패턴을 활용한 이벤트 기반 구조

---

## 5. 테스트 코드

* **SeatTest** : 다수 스레드가 동일 좌석을 동시에 예약 시도 → 동시성 문제 확인
* **TicketQueueTest** : `BlockingQueue`로 producer-consumer 패턴 테스트
* **SeatManagerTest** : 남은 좌석 수, 예약 성공 여부, 동시 예약 시나리오 검증

```java
Runnable bookTask = () -> {
    boolean success = seat.book();
    System.out.println(Thread.currentThread().getName() + " 예약 시도 → " + success);
};

for (int i = 0; i < 1000; i++) {
    new Thread(bookTask).start();
}
```

---

## 6. 설계 특징

1. **Producer-Consumer 패턴**으로 대기열 관리
2. **BlockingQueue + synchronized**로 멀티스레드 안전 보장
3. **각 좌석(Seat)별로 최소 단위로 동기화**하여 동시 예약 가능
4. **리스너 패턴**으로 UI-서버 분리, 이벤트 기반 화면 갱신
5. **Swing Timer** 사용 → 대기열 상태/좌석 상태 실시간 갱신

---

## 7. 사용 방법

1. 프로그램 실행 → StartPanel에서 “게임 시작” 클릭
2. Bot과 사용자가 대기열에 들어가고 서버 스레드가 차례대로 처리
3. 사용자는 차례가 되면 좌석 선택 화면에서 좌석 예약 가능
4. 좌석 예약 시 상단 StatusPanel에 남은 좌석 수와 예약된 좌석 수 자동 업데이트

