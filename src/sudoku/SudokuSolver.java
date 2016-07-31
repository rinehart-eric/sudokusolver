package sudoku;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import exactcover.ExactCoverProblem;

public class SudokuSolver {
	public static void main(String[] args) {
//		int[][] puzzle = {{ -1, -1, 1, 5, -1, 10, -1, 2, -1, 8, -1, -1, 9, -1, 13, -1, -1 },
//				{ -1, -1, -1, 4, -1, 11, -1, 14, -1, -1, 15, -1, -1, 9, 8, 0 },
//				{ -1, 12, -1, -1, -1, -1, -1, 0, 7, 10, -1, 3, 4, -1, -1, -1 },
//				{ -1, 13, -1, -1, 9, 8, -1, -1, -1, -1, 11, -1, 14, 1, -1, 7 },
//				{ -1, -1, -1, -1, 11, -1, -1, 4, 15, -1, -1, -1, 5, 6, 0, -1 },
//				{ -1, 9, -1, 7, -1, -1, -1, -1, 5, -1, -1, -1, -1, -1, -1, 15 },
//				{ -1, -1, -1, -1, 7, 12, -1, -1, -1, 9, 6, -1, -1, -1, -1, -1 },
//				{ -1, 11, 4, -1, 3, 15, 5, -1, -1, 12, 8, -1, -1, -1, -1, -1 },
//				{ -1, -1, -1, -1, -1, 10, 13, -1, -1, -1, -1, 14, -1, 2, 15, 12 },
//				{ 7, -1, 8, -1, -1, -1, 15, -1, -1, 1, -1, 12, -1, -1, 10, -1 },
//				{ -1, 3, -1, -1, -1, -1, 11, -1, 10, -1, -1, -1, 8, 4, 5, -1 },
//				{ -1, -1, 2, 1, -1, -1, 12, 8, 3, 5, -1, -1, -1, -1, -1, 11 },
//				{ 10, -1, -1, -1, 2, 5, -1, -1, -1, -1, 3, -1, 1, -1, -1, 14 },
//				{ 2, -1, -1, -1, -1, -1, -1, -1, 1, -1, -1, 6, -1, 10, -1, -1 },
//				{ 11, 4, -1, 13, -1, -1, -1, -1, -1, -1, -1, -1, 3, -1, -1, 5 },
//				{ -1, 5, -1, -1, 15, -1, -1, 6, 0, 4, -1, -1, 9, -1, -1, -1 }};

		int[][] puzzle = new int[][] {
			{ 1, 0, 0, 2, 3, 4, 0, 0, 12,  },
			{ 0, 0, 0, 7, 4, 0, 8, 0, 9 },
			{ 0, 6, 8, 1, 0, 9, 0, 0, 2 },
			{ 0, 3, 5, 4, 0, 0, 0, 0, 8 },
			{ 6, 0, 7, 8, 0, 2, 5, 0, 1 },
			{ 8, 0, 0, 0, 0, 5, 7, 6, 0 },
			{ 2, 0, 0, 6, 0, 3, 1, 9, 0 },
			{ 7, 0, 9, 0, 2, 1, 0, 0, 0 },
			{ 0, 0, 0, 9, 7, 4, 0, 8, 0 }
		};
		int [][] solution = new SudokuSolver(puzzle).solve();
		System.out.println(Arrays.stream(solution)
				.map(row -> Arrays.stream(row).mapToObj(i -> Integer.toString(i, 16).toUpperCase()).collect(Collectors.joining(" ")))
				.collect(Collectors.joining("\n")));
	}

	private static final int CONSTRAINT_COUNT = 4;

	private int[][] puzzle;
	int size;
	int boxSide;

	public SudokuSolver(int[][] puzzle) {
		this.puzzle = puzzle;
		this.size = puzzle.length;
		this.boxSide = (int) Math.sqrt(size);
	}

	public int[][] solve() {
		List<boolean[]> allValues = new ArrayList<>();
		for (int row = 0; row < size; row++) {
			for (int col = 0; col < size; col++) {
				int value = puzzle[row][col];

				allValues.addAll(getValues(value, row, col));
			}
		}
		boolean[][] puzzleArray = new boolean[allValues.size()][size * size * CONSTRAINT_COUNT];
		for (int row = 0; row < allValues.size(); row++) {
			puzzleArray[row] = allValues.get(row);
		}

		ExactCoverProblem problem = new ExactCoverProblem(puzzleArray);
		problem.solve();

		return convertSolution(problem.getSolution());
	}

	private int[][] convertSolution(List<List<Integer>> solution) {
		int[][] board = new int[size][size];

		for (List<Integer> cell : solution) {
			Collections.sort(cell);
			int valueInRow = cell.get(0);
			int row = valueInRow % size;
			int value = valueInRow / size;
			int valueInCell = cell.get(1);
			int col = valueInCell % size;
			board[row][col] = value;
		}
		return board;
	}

	private List<boolean[]> getValues(int value, int row, int col) {
		return getValues((value == -1) ?
				IntStream.range(0, size).mapToObj(Integer::valueOf).collect(Collectors.toList()) : Arrays.asList(Integer.valueOf(value)),
				row, col, boxForCoords(row, col));
	}

	private List<boolean[]> getValues(List<Integer> values, int row, int col, int box) {
		return values.stream()
				.map(value -> getRow(value, row, col, box))
				.collect(Collectors.toList());
	}

	private boolean[] getRow(int value, int row, int col, int box) {
		boolean[] fullRow = new boolean[size * size * CONSTRAINT_COUNT];
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
		return IntStream.range(0, size * size).mapToObj(index -> (index == constraint1 * size + constraint2)).toArray(Boolean[]::new);
	}

	private int boxForCoords(int row, int col) {
		return (row / boxSide * boxSide) + (col / boxSide);
	}
}
