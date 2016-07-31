package exactcover;

public class Header extends Node {
	private int size;
	private int index;

	public Header(int index) {
		super(null);
		this.size = 0;
		this.index = index;
	}

	public Header getLeft() {
		return (Header) super.getLeft();
	}

	public void setLeft(Header left) {
		super.setLeft(left);
	}

	public Header getRight() {
		return (Header) super.getRight();
	}

	public void setRight(Header right) {
		super.setRight(right);
	}

	public int getSize() {
		return size;
	}

	public void incrSize() {
		size++;
	}

	public void decrSize() {
		size--;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}
}