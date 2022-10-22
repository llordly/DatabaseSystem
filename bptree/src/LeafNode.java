import java.util.ArrayList;

public class LeafNode extends Node {

	public LeafNode(int maxSize) {
		super(maxSize);
		p = new ArrayList<KeyValue>(maxSize);
		this.setRightSibling(null);
		this.setLeftSibling(null);
		isLeaf = true;
	}

	@Override
	public int search(int key) {
		int i;
		for (i = 0; i < p.size(); ++i) {
			if (p.get(i).getKey() == key) {
				return i;
			}
		}
		if (i == size())
			return -1;
		return i;
	}

	@Override
	public Node split() {
		int keyCount = this.size();
		int splitPoint = this.size() / 2;

		LeafNode newRightNode = new LeafNode(m);

		for (int i = splitPoint; i < keyCount; ++i) {
			newRightNode.p.add(this.p.get(i));
		}

		for (int i = keyCount - 1; i >= splitPoint; --i) {
			this.p.remove(i);
		}
		newRightNode.setLeftSibling(this);
		newRightNode.parent = this.parent;
		newRightNode.setRightSibling(this.getRightSibling());
		if (this.getRightSibling() != null) {
			this.getRightSibling().setLeftSibling(newRightNode);
		}
		this.setRightSibling(newRightNode);
		return newRightNode;
	}

	public void insertion(KeyValue key) {
		int keyCount = this.size();
		int i = 0;
		while (i < keyCount && key.getKey() > this.p.get(i).getKey())
			i++;

		this.p.add(i, key);
	}

	public boolean delete(int key) {
		int index = this.search(key);

		if (index == -1)
			return false;
		this.p.remove(index);

		return true;
	}

	protected void mergeSibling(Node rightSibling) {
		LeafNode rightSiblingLeaf = (LeafNode) rightSibling;

		for (int i = 0; i < rightSiblingLeaf.size(); ++i) {
			this.p.add(rightSibling.p.get(i));
		}

		this.setRightSibling(rightSiblingLeaf.getRightSibling());
		if (rightSiblingLeaf.getRightSibling() != null)
			rightSiblingLeaf.getRightSibling().setLeftSibling(this);
		rightSiblingLeaf = null;
	}

	protected int redistributeSibling(Node sibling, int LeftOrRight) {
		// if sibling is left, LeftOrRight is not 0
		// if sibling is right, LeftOrRight is 0

		LeafNode siblingLeaf = (LeafNode) sibling;
		this.insertion(siblingLeaf.p.get(LeftOrRight));

		siblingLeaf.p.remove(LeftOrRight);

		if (LeftOrRight == 0)
			return sibling.p.get(0).getKey();
		else
			return this.p.get(0).getKey();
	}

}
