package swing;

public enum QueueState {
    IDLE,          // 기본 상태
    ENTERING,      // 대기열 진입 중
    WAITING,       // 대기 중
    READY          // 곧 입장
}