package swing;

import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import dev.SeatBookedListener;
import dev.SeatManager;

class SeatPanel extends JPanel implements SeatBookedListener {

    private final SeatManager manager;
    private final JButton[][] buttons;

    public SeatPanel(MainFrame frame, SeatManager manager, int rows, int cols) {

		this.manager = manager;
        this.buttons = new JButton[rows][cols];

        setLayout(new GridLayout(rows, cols));
	
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {

                JButton btn = new JButton();
                btn.setBackground(Color.GREEN);

                buttons[r][c] = btn;
                
                int row = r;
                int col = c;

                btn.addActionListener(e -> {
                	
                    boolean success = manager.getSeat(row, col).book();

                    if (success) {
                        btn.setBackground(Color.RED);
                        JOptionPane.showMessageDialog(frame, "예매 성공");
                    } else {
                        JOptionPane.showMessageDialog(frame, "이미 선택된 좌석입니다.");
                    }
                });

                add(btn);
            }
        }
    }
    
    public void updateSeatUI(int row, int col) {
        SwingUtilities.invokeLater(() -> {
            buttons[row][col].setBackground(Color.RED);
        });
    }

	@Override
	public void onSeatBooked(int row, int col) {
		SwingUtilities.invokeLater(() -> {
			buttons[row][col].setBackground(Color.RED);
	    });
		
	}
}
