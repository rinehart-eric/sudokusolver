package sudokusolver;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SudokuParams {
	private List<Character> chars;
	private char emptyChar;
	private int size;
	private int boxSide;

	public SudokuParams(char emptyChar, List<Character> chars) {
		this.chars = chars;
		this.size = chars.size();
		this.boxSide = (int) Math.sqrt(size);

		assert(size == (boxSide* boxSide));
	}

	public int indexOf(char c) {
		return chars.indexOf(c);
	}

	public char getCharAt(int index) {
		return chars.get(index);
	}

	public char getEmptyChar() {
		return emptyChar;
	}

	public int getSize() {
		return size;
	}

	public int getBoxSide() {
		return boxSide;
	}

	public static SudokuParams CLASSIC_PARAMS = new SudokuParams('0', IntStream.rangeClosed(1, 9)
			.mapToObj(Integer::toString)
			.map(intStr -> intStr.charAt(0))
			.collect(Collectors.toList()));
}
