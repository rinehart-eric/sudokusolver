package exactcover;

public abstract class AbstractNode<H extends AbstractNode<?, ?>, V extends AbstractNode<?, ?>> {
	private V up;
	private V down;
	private H left;
	private H right;
	private Header header;

	public AbstractNode(Header header) {
		this.header = header;
	}

	public V getUp() {
		return up;
	}

	public void setUp(V up) {
		this.up = up;
	}

	public V getDown() {
		return down;
	}

	public void setDown(V down) {
		this.down = down;
	}

	public H getLeft() {
		return left;
	}

	public void setLeft(H left) {
		this.left = left;
	}

	public H getRight() {
		return right;
	}

	public void setRight(H right) {
		this.right = right;
	}
	
	public Header getHeader() {
		return header;
	}
}
