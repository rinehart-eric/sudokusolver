package exactcover;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ExactCoverProblem {
	private Header root;
	private Header[] headers;
	private AbstractNode<?>[] o;
	private List<List<Integer>> solution;

	public ExactCoverProblem(boolean[][] matrix) {
		solution = new ArrayList<>();
		initialize(matrix);
	}

	public void initialize(boolean[][] matrix) {
		root = new Header(-1);

		int headerCount = Arrays.stream(matrix)
				.mapToInt(row -> row.length)
				.max()
				.getAsInt();
		headers = new Header[headerCount];
		o = new Node[headerCount];
		for (int i = 0; i < headerCount; i++) {
			Header h = new Header(i);
			if (i == 0) {
				linkHoriz(root, h);
			} else {
				linkHoriz(headers[i - 1], h);
			}
			linkHoriz(h, root);
			headers[i] = h;
		}

		for (boolean[] row : matrix) {
			Node lastHoriz = null;
			for (int i = 0; i < row.length; i++) {
				if (!row[i]) {
					continue;
				}
				Header h = headers[i];
				Node n = new Node(h);
				if (lastHoriz != null) {
					Node firstHoriz = lastHoriz.getRight();
					linkHoriz(lastHoriz, n);
					linkHoriz(n, firstHoriz);
				}
				AbstractNode<?> lastVert = h.getUp();
				linkVert(lastVert, n);
				linkVert(n, h);
				h.incrSize();

				lastHoriz = n;
			}
		}
	}

	private <H extends AbstractNode<H>> void linkHoriz(H left, H right) {
		left.setRight(right);
		right.setLeft(left);
	}

	private void linkVert(AbstractNode<?> top, AbstractNode<?> bottom) {
		top.setDown(bottom);
		bottom.setUp(top);
	}

	/**
	 * Covers the column with the given header.
	 * 
	 * This is accomplished as follows: the header is removed from the header linked list
	 * and then for each node in the column, every node in the same row as that node is
	 * removed from its respective column.
	 */
	private void cover(Header header) {
		linkHoriz(header.getLeft(), header.getRight());
		AbstractNode<?> colNode = header.getDown();
		while (colNode != header) {
			AbstractNode<?> rowBase = colNode;
			AbstractNode<?> rowNode = rowBase.getRight();
			while (rowNode != rowBase) {
				linkVert(rowNode.getUp(), rowNode.getDown());
				rowNode.getHeader().decrSize();

				rowNode = rowNode.getRight();
			}

			colNode = colNode.getDown();
		}
	}

	/**
	 * Uncovers the column with the given header.
	 * 
	 * This is accomplished by following the opposite steps as <code>cover()</code>:
	 * For each node in the header's (currently covered) column, every node in the same row
	 * is inserted back into its column. Then the header is inserted back into the header list.
	 */
	private void uncover(Header header) {
		AbstractNode<?> colNode = header.getUp();
		while (colNode != header) {
			AbstractNode<?> rowBase = colNode;
			AbstractNode<?> rowNode = rowBase.getLeft();
			while (rowNode != rowBase) {
				rowNode.getHeader().incrSize();
				rowNode.getDown().setUp(rowNode);
				rowNode.getUp().setDown(rowNode);

				rowNode = rowNode.getLeft();
			}

			colNode = colNode.getUp();
		}

		header.getRight().setLeft(header);
		header.getLeft().setRight(header);
	}

	public void solve() {
		search(0);
	}

	private void search(int i) {
		if (root.getRight() == root) {
			saveSolution(i);
			return;
		}
		Header h = chooseHeader();
		cover(h);

		AbstractNode<?> r = h.getDown();
		while (!isSolved() && r != h) {
			o[i] = r;
			AbstractNode<?> jBase = r;
			AbstractNode<?> j = jBase.getRight();
			while (j != jBase) {
				cover(j.getHeader());

				j = j.getRight();
			}

			search(i + 1);

			j = jBase.getLeft();
			while (j != jBase) {
				uncover(j.getHeader());

				j = j.getLeft();
			}

			r = r.getDown();
		}

		uncover(h);
	}

	private Header chooseHeader() {
		Header h = root.getRight();
		Header minHeader = h;
		int minSize = minHeader.getSize();
		h = h.getRight();
		while (h != root) {
			if (h.getSize() < minSize) {
				minSize = h.getSize();
				minHeader = h;
			}
			h = h.getRight();
		}
		return minHeader;
	}

	private void saveSolution(int k) {
		solution.clear();
		for (int i = 0; i < k; i++) {
			List<Integer> row = new ArrayList<>();
			AbstractNode<?> c = o[i].getRight();
			row.add(o[i].getHeader().getIndex());

			while (c != o[i]) {
				row.add(c.getHeader().getIndex());
				c = c.getRight();
			}
			solution.add(row);
		}
	}

	private boolean isSolved() {
		return !solution.isEmpty();
	}

	public List<List<Integer>> getSolution() {
		return solution;
	}
}
