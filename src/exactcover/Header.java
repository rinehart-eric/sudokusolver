package exactcover;

public class Header extends AbstractNode<Header> {
	private int size;
	private int index;

	public Header(int index) {
		super(null);
		super.setLeft(this);
		super.setRight(this);
		this.size = 0;
		this.index = index;
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