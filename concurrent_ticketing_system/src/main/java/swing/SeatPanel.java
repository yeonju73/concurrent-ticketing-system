package swing;

import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import dev.SeatManager;

class SeatPanel extends JPanel  {

    private final SeatManager manager;
    private final JButton[][] buttons;

    public SeatPanel(MainFrame frame, SeatManager manager, int rows, int cols) {

		this.manager = manager;
        this.buttons = new JButton[rows][cols];

        // GridLayout으로 행/열 맞추고 간격 5px
        setLayout(new GridLayout(rows, cols, 5, 5)); // hgap, vgap = 5px
        setBackground(Color.DARK_GRAY); // 좌석 사이 간격 색
	
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {

                JButton btn = new JButton();
                btn.setBackground(new Color(123, 104, 238));
                
                // 버튼 테두리 살짝
                btn.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

                buttons[r][c] = btn;
                
                int row = r;
                int col = c;

                btn.addActionListener(e -> {
                	
                    boolean success = manager.getSeat(row, col).book();

                    if (success) {
                        btn.setBackground(Color.LIGHT_GRAY);
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
            buttons[row][col].setBackground(Color.LIGHT_GRAY);
        });
    }
}
