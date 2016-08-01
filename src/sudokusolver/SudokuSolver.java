package sudokusolver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import exactcover.ExactCoverProblem;

public class SudokuSolver {
	public static void main(String[] args) {
		char[][] puzzle = {
				{ ' ', '1', '5', ' ', 'A', ' ', '2', ' ', '8', ' ', ' ', '9', ' ', 'D', ' ', ' ' },
				{ ' ', ' ', ' ', '4', ' ', 'B', ' ', 'E', ' ', ' ', 'F', ' ', ' ', '9', '8', '0' },
				{ ' ', 'C', ' ', ' ', ' ', ' ', ' ', '0', '7', 'A', ' ', '3', '4', ' ', ' ', ' ' },
				{ ' ', 'D', ' ', ' ', '9', '8', ' ', ' ', ' ', ' ', 'B', ' ', 'E', '1', ' ', '7' },
				{ ' ', ' ', ' ', ' ', 'B', ' ', ' ', '4', 'F', ' ', ' ', ' ', '5', '6', '0', ' ' },
				{ ' ', '9', ' ', '7', ' ', ' ', ' ', ' ', '5', ' ', ' ', ' ', ' ', ' ', ' ', 'F' },
				{ ' ', ' ', ' ', ' ', '7', 'C', ' ', ' ', ' ', '9', '6', ' ', ' ', ' ', ' ', ' ' },
				{ ' ', 'B', '4', ' ', '3', 'F', '5', ' ', ' ', 'C', '8', ' ', ' ', ' ', ' ', ' ' },
				{ ' ', ' ', ' ', ' ', ' ', 'A', 'D', ' ', ' ', ' ', ' ', 'E', ' ', '2', 'F', 'C' },
				{ '7', ' ', '8', ' ', ' ', ' ', 'F', ' ', ' ', '1', ' ', 'C', ' ', ' ', 'A', ' ' },
				{ ' ', '3', ' ', ' ', ' ', ' ', 'B', ' ', 'A', ' ', ' ', ' ', '8', '4', '5', ' ' },
				{ ' ', ' ', '2', '1', ' ', ' ', 'C', '8', '3', '5', ' ', ' ', ' ', ' ', ' ', 'B' },
				{ 'A', ' ', ' ', ' ', '2', '5', ' ', ' ', ' ', ' ', '3', ' ', '1', ' ', ' ', 'E' },
				{ '2', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '1', ' ', ' ', '6', ' ', 'A', ' ', ' ' },
				{ 'B', '4', ' ', 'D', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', '3', ' ', ' ', '5' },
				{ ' ', '5', ' ', ' ', 'F', ' ', ' ', '6', '0', '4', ' ', ' ', '9', ' ', ' ', ' ' }
		};

//		char[][] puzzle = {
//				{'6', '0', '0', '0', '2', '0', '0', '0', '9'}, 
//				{'0', '1', '0', '3', '0', '7', '0', '5', '0'}, 
//				{'0', '0', '3', '0', '0', '0', '1', '0', '0'}, 
//				{'0', '9', '0', '0', '0', '0', '0', '2', '0'}, 
//				{'2', '0', '0', '8', '7', '5', '0', '0', '3'}, 
//				{'0', '0', '5', '0', '1', '0', '4', '0', '0'}, 
//				{'0', '7', '0', '0', '8', '0', '0', '9', '0'}, 
//				{'0', '0', '1', '0', '4', '0', '8', '0', '0'}, 
//				{'0', '0', '0', '2', '5', '9', '0', '0', '0'} 
//		};
		char[][] solution = new SudokuSolver(puzzle, SudokuParams.SIXTEEN_PARAMS).solve();
		for (int row = 0; row < solution.length; row++) {
			char[] rowChars = solution[row];
			for (int col = 0; col < rowChars.length; col++) {
				System.out.print(Character.toUpperCase(rowChars[col]) + " ");
			}
			System.out.print('\n');
		}
	}

	private static final int CONSTRAINT_COUNT = 4;

	private char[][] puzzle;
	private SudokuParams params;

	public SudokuSolver(char[][] puzzle) {
		this(puzzle, SudokuParams.CLASSIC_PARAMS);
	}

	public SudokuSolver(char[][] puzzle, SudokuParams params) {
		this.puzzle = puzzle;
		this.params = params;
	}

	public char[][] solve() {
		List<boolean[]> allValues = new ArrayList<>();
		for (int row = 0; row < params.getSize(); row++) {
			for (int col = 0; col < params.getSize(); col++) {
				char value = puzzle[row][col];

				allValues.addAll(getValues(value, row, col));
			}
		}
		boolean[][] puzzleArray = new boolean[allValues.size()][params.getSize() * params.getSize() * CONSTRAINT_COUNT];
		for (int row = 0; row < allValues.size(); row++) {
			puzzleArray[row] = allValues.get(row);
		}

		ExactCoverProblem problem = new ExactCoverProblem(puzzleArray);
		problem.solve();

		return convertSolution(problem.getSolution());
	}

	private char[][] convertSolution(List<List<Integer>> solution) {
		int size = params.getSize();
		char[][] board = new char[size][size];

		for (List<Integer> cell : solution) {
			Collections.sort(cell);
			int valueInRow = cell.get(0);
			int row = valueInRow % size;
			int value = valueInRow / size;
			int valueInCell = cell.get(1);
			int col = valueInCell % size;
			board[row][col] = params.getCharAt(value);
		}
		return board;
	}

	private List<boolean[]> getValues(char value, int row, int col) {
		return getValues((value == params.getEmptyChar()) ?
				IntStream.range(0, params.getSize()).mapToObj(Integer::valueOf).collect(Collectors.toList()) : Arrays.asList(params.indexOf(value)),
				row, col, boxForCoords(row, col));
	}

	private List<boolean[]> getValues(List<Integer> values, int row, int col, int box) {
		return values.stream()
				.map(value -> getRow(value, row, col, box))
				.collect(Collectors.toList());
	}

	private boolean[] getRow(int value, int row, int col, int box) {
		boolean[] fullRow = new boolean[params.getSize() * params.getSize() * CONSTRAINT_COUNT];
		int offset = 0;
		for (boolean b : getSubRow(value, row)) {
			fullRow[offset] = b;
			offset++;
		}
		for (boolean b : getSubRow(value, col)) {
			fullRow[offset] = b;
			offset++;
		}
		for (boolean b : getSubRow(value, box)) {
			fullRow[offset] = b;
			offset++;
		}
		for (boolean b : getSubRow(row, col)) {
			fullRow[offset] = b;
			offset++;
		}
		return fullRow;
	}

	private Boolean[] getSubRow(int constraint1, int constraint2) {
		return IntStream.range(0, params.getSize() * params.getSize())
				.mapToObj(index -> Boolean.valueOf(index == constraint1 * params.getSize() + constraint2))
				.toArray(Boolean[]::new);
	}

	private int boxForCoords(int row, int col) {
		return (row / params.getBoxSide() * params.getBoxSide()) + (col / params.getBoxSide());
	}
}
