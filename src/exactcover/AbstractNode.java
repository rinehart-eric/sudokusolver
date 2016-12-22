package exactcover;

public abstract class AbstractNode<H extends AbstractNode<H>> {
	private AbstractNode<?> up;
	private AbstractNode<?> down;
	private H left;
	private H right;
	private Header header;

	public AbstractNode(Header header) {
		up = this;
		down = this;
		this.header = header;
	}

	public AbstractNode<?> getUp() {
		return up;
	}

	public void setUp(AbstractNode<?> up) {
		this.up = up;
	}

	public AbstractNode<?> getDown() {
		return down;
	}

	public void setDown(AbstractNode<?> down) {
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
