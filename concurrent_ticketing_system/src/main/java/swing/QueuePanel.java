package swing;

import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

import dev.TicketRequest;

public class QueuePanel extends JPanel {

	private final MainFrame frame;
	private final JLabel label;

	public QueuePanel(MainFrame frame) {

		this.frame = frame;

		setLayout(new BorderLayout());

		label = new JLabel("대기열 대기 중", JLabel.CENTER);

		// 주기적 상태 갱신
		Timer timer = new Timer(500, e -> pollQueueState());
		timer.start();

		add(label, BorderLayout.CENTER);
	}

	private void pollQueueState() {
		TicketRequest user = frame.getCurrentUser();

		// 1사용자 없음
		if (user == null) {
			label.setText("사용자 없음");
			return;
		}

		// 아직 ticket 발급 전
		if (user.getTicketNo() <= 0) {
			label.setText("대기열 진입 중...");
			return;
		}

		// 3️⃣ ticket 발급 완료 → 순번 계산
		long remaining = frame.getQueue().getRemainingPosition(user.getTicketNo());

		if (remaining <= 1) {
			label.setText("곧 입장합니다!");
		} else {
			label.setText("내 앞에 " + (remaining - 1) + "명 대기 중");
		}
	}
}
