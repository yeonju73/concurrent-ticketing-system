# 🎮 Concurrent Ticketing Simulator

## 1. 프로젝트 개요
멀티스레드 환경에서 동시성 문제와 **thread-safe 설계**의 중요성을 시각적으로 보여주는 티켓팅 시뮬레이션 프로그램입니다.

<img src="https://github.com/user-attachments/assets/295aa0bd-8f32-407b-9781-8e61e06eaa39" width="400">

* 다수의 Bot Thread와 사용자 Thread가 동시에 대기열(Queue)에 진입
* 대기열을 통과한 Thread들이 동시에 동일한 좌석에 접근하여 예약 경쟁
* Swing UI를 통해 실시간 좌석 상태 및 대기열 현황 확인

---

## 2. 전체 시나리오




1. 프로그램 실행 후 “게임 시작” 클릭 → 사용자 대기열 진입
2. 동시에 Bot Thread도 대기열에 진입
3. 서버 스레드(ServerThread)가 대기열 요청을 차례대로 처리
4. 좌석 선택 화면(SeatPanel)으로 이동 → 사용자/봇이 랜덤 좌석 선택
5. 동일 좌석에 대한 경쟁 발생 → **thread-safe 설계로 단일 예약만 성공**
6. UI(StatusPanel)에서 총 좌석 수, 남은 좌석 수, 예약된 좌석 수 실시간 갱신

---

## 3. 동시성 처리

### 3-1. 공유 자원

| 공유 자원                      | 설명                | thread-safe 처리 방식                                          |
| -------------------------- | ----------------- | ---------------------------------------------------------- |
| TicketQueue                | 대기열에 들어온 사용자/봇 요청 | `BlockingQueue` 사용<br>put()/take()로 producer-consumer 패턴 구현 |
| TicketQueue.sequence       | 티켓 번호 발급 카운터      | `AtomicLong` 사용<br>`incrementAndGet()`으로 중복 없는 번호 발급        |
| TicketQueue.serving        | 현재 처리 중인 번호 추적    | `AtomicLong` 사용<br>`set()`으로 원자적 갱신                         |
| SeatManager.remainingSeats | 남은 좌석 수           | `AtomicInteger` 사용<br>`decrementAndGet()`으로 원자적 감소           |
| Seat (개별 좌석)               | 실제 좌석 하나          | `synchronized book()` 메소드 사용<br>임계 영역 보호 -> 단일 스레드만 예약 가능       |

### 3-2. 처리 순서
<img width="700" height="233" alt="producer-consumer-1" src="https://github.com/user-attachments/assets/690a19de-4806-4723-a55a-0b37be84b965" />

1. **Producer (User/Bot Thread)** → `TicketQueue.put()`로 요청 등록
2. **Consumer (ServerThread)** → `TicketQueue.take()`로 요청 처리
3. **좌석 예약** → `SeatManager.bookSeat(row, col)` 호출
   * 내부적으로 `Seat.book()` 호출 → 단일 스레드만 예약 성공
   * 성공 시 `remainingSeats.decrementAndGet()` → `AtomicInteger`로 안전하게 남은 좌석 감소
4. **UI 업데이트**


---

## 4. UI 구조 및 Listener

### 4-1. Swing UI

* **StartPanel** : 게임 시작 화면
* **QueuePanel** : 대기열 상태, 남은 대기열 수 표시
* **SeatPanel** : 좌석 그리드, 예약 가능(보라) / 예약됨(회색) 색상 표시
* **StatusPanel** : 총 좌석 수, 남은 좌석 수, 예약된 좌석 수 표시

### 4-2. Listener 구조 (Observer Pattern)

* **TicketEventListener** : 사용자의 차례 알림 → SeatPanel 전환
* **SeatBookedListener** : 좌석 예약 시 UI 갱신

> 서버 스레드에서 이벤트 발생 → UI(MainFrame)에서 리스너를 통해 **EDT에서 안전하게 업데이트**

---

## 5. 테스트

* **SeatTest** : 다수 스레드가 동일 좌석을 동시에 예약 시도
* 테스트 코드 예시:

```java
Runnable bookTask = () -> {
    boolean success = seat.book();
    System.out.println(Thread.currentThread().getName() + " 예약 시도 → " + success);
};
```

* 50개 Thread 동시에 실행 → 최종 좌석 상태 확인 가능
* 멀티스레드 환경에서 동시성 문제 검증 가능

---

## 6. 설계 특징

* Producer-Consumer 패턴으로 대기열 관리
* BlockingQueue + AtomicInteger + synchronized로 **멀티스레드 안전 보장**
* Seat 단위로 최소한 동기화 → 동시 예약 가능
* Observer 패턴으로 UI-서버 분리, 이벤트 기반 화면 갱신
* Swing Timer 사용 → 좌석/대기열 상태 실시간 업데이트

---

## 7. 사용 방법

1. 프로그램 실행 → StartPanel에서 “게임 시작” 클릭
2. Bot과 사용자가 대기열에 들어감
3. 서버 스레드가 요청 처리 → 사용자 차례 시 SeatPanel에서 좌석 선택 가능
4. 좌석 예약 시 StatusPanel에서 남은 좌석 수 및 예약된 좌석 수 자동 갱신

<img width="533" height="532" alt="image" src="https://github.com/user-attachments/assets/50cb5ac1-cc91-4b8f-948b-37c065aef331" />


