package sudokusolver;

import java.awt.BorderLayout;
import java.util.Arrays;

import javax.swing.DefaultCellEditor;
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumnModel;

public class SudokuWindow extends JFrame {
	private static final long serialVersionUID = 1L;

	private SudokuParams params;

	public SudokuWindow(SudokuParams params) {
		super("Sudoku Puzzle Solver");
		this.params = params;

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		build();
		setSize(400, 400);
		setVisible(true);
	}

	public static void main(String[] args) {
		new SudokuWindow(SudokuParams.CLASSIC_PARAMS);
	}

	private void build() {
		JTable sudokuTable = new JTable(new SudokuTableModel());
		TableColumnModel columnModel = sudokuTable.getColumnModel();
		for (int col = 0; col < columnModel.getColumnCount(); col++) {
			DefaultCellEditor defaultEditor = (DefaultCellEditor) sudokuTable.getDefaultEditor(Object.class);
			defaultEditor.setClickCountToStart(1);
		}

		setLayout(new BorderLayout());
		add(sudokuTable, BorderLayout.CENTER);
	}

	private class SudokuTableModel extends AbstractTableModel {
		private static final long serialVersionUID = 3317989030667938805L;

		public char[][] values;

		public SudokuTableModel() {
			values = new char[params.getSize()][params.getSize()];
			Arrays.stream(values).forEach(arr -> Arrays.fill(arr, params.getEmptyChar()));
		}

		@Override
		public int getColumnCount() {
			return params.getSize();
		}

		@Override
		public int getRowCount() {
			return params.getSize();
		}

		@Override
		public Object getValueAt(int row, int col) {
			return values[row][col];
		}

		@Override
		public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
			values[rowIndex][columnIndex] = aValue.toString().charAt(0);
		}

		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			return true;
		}
	}
}
