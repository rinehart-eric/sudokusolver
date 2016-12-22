package exactcover;

public class Node extends AbstractNode<Node> {
	public Node(Header header) {
		super(header);
		setLeft(this);
		setRight(this);
	}
}
