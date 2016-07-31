package sudokusolver;

import javax.swing.JFrame;

public class SudokuWindow extends JFrame {
	private static final long serialVersionUID = 1L;

	public SudokuWindow() {
		super("Sudoku Puzzle Solver");

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		build();
		setSize(400, 400);
		setVisible(true);
	}

	private void build() {
		
	}
}
