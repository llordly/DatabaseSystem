import java.io.Serializable;
import java.util.ArrayList;

import javax.sound.midi.Synthesizer;

public abstract class Node implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	protected int m;
	protected boolean isLeaf;
	protected Node parent;
	protected ArrayList<KeyValue> p;

	private Node rightSibling;
	private Node leftSibling;

	public Node getRightSibling() {
		return rightSibling;
	}

	public void setRightSibling(Node rightSibling) {
		this.rightSibling = rightSibling;
	}

	public Node getLeftSibling() {
		return leftSibling;
	}

	public void setLeftSibling(Node leftSibling) {
		this.leftSibling = leftSibling;
	}

	public Node(int maxSize) {
		this.m = maxSize;
		this.parent = null;
	}

	public boolean isLeaf() {
		return isLeaf;
	}

	public int size() {
		return this.p.size();
	}

	abstract public int search(int key);

	abstract public Node split();

	public Node underflow() {
		NonleafNode parentNode = null;
		if (this.parent == null)
			return null;
		else {
			parentNode = (NonleafNode) this.parent;
		}
		// 우선 왼쪽 형제노드에서 빌릴 수 있는지 체크 후 빌리는 과정을 실행. 안될 경우 오른쪽 형제노드에서 빌림 - redistribution
		Node leftSibling = this.getLeftSibling();
		Node rightSibling = this.getRightSibling();

		if (this.isLeaf()) {

			int childIndex = 0;
			while (childIndex < parentNode.size() && parentNode.p.get(childIndex).getPointer() != this)
				++childIndex;

			if (leftSibling != null && leftSibling.size() > (leftSibling.m - 1) / 2
					&& this.parent == leftSibling.parent) {
				int upKey = ((LeafNode) this).redistributeSibling(leftSibling, leftSibling.size() - 1);
				parentNode.p.get(childIndex - 1).setKey(upKey);
				return null;
			}
			if (rightSibling != null && rightSibling.size() > (rightSibling.m - 1) / 2
					&& this.parent == rightSibling.parent) {
				int upKey = ((LeafNode) this).redistributeSibling(rightSibling, 0);
				parentNode.p.get(childIndex).setKey(upKey);
				return null;
			}

			// 왼쪽 형제노드와 오른쪽 형제노드에서 빌릴 수 없을 경우에는 merge 과정을 실행한다.
			if (leftSibling != null && this.parent == leftSibling.parent) {
				int index = 0;
				while (index < leftSibling.parent.size() && leftSibling.parent.p.get(index).getPointer() != leftSibling)
					++index;

				((NonleafNode) leftSibling.parent).deleteAt(index);
				((LeafNode) leftSibling).mergeSibling(this);
				if (this.parent.size() < (this.parent.m - 1) / 2) {
					if (this.parent.parent == null) {
						if (this.parent.size() == 0) {
							leftSibling.parent = null;
							return (Node) leftSibling;
						} else {
							return null;
						}
					}
					return this.parent.underflow();
				}
				return null;
			}

			if (rightSibling != null && this.parent == rightSibling.parent) {
				int index = 0;
				while (index < this.parent.size() && this.parent.p.get(index).getPointer() != this)
					++index;
				((NonleafNode) this.parent).deleteAt(index);
				((LeafNode) this).mergeSibling(rightSibling);
				if (this.parent.size() < (this.parent.m - 1) / 2) {
					if (this.parent.parent == null) {
						if (this.parent.size() == 0) {
							this.parent = null;
							return (Node) this;
						} else {
							return null;
						}
					}
					return this.parent.underflow();
				}
				return null;
			}
		} else {
			if (leftSibling != null && leftSibling.size() > (leftSibling.m - 1) / 2
					&& this.parent == leftSibling.parent) {
				parentNode.redistributeChild(this, leftSibling, leftSibling.size() - 1);
				return null;
			}

			if (rightSibling != null && rightSibling.size() > (rightSibling.m - 1) / 2
					&& this.parent == rightSibling.parent) {
				parentNode.redistributeChild(this, rightSibling, 0);
				return null;
			}

			// 왼쪽 형제노드와 오른쪽 형제노드에서 빌릴 수 없을 경우에는 merge 과정을 실행한다.
			if (leftSibling != null && this.parent == leftSibling.parent) {
				return parentNode.mergeChild(leftSibling, this);
			}

			if (rightSibling != null && this.parent == rightSibling.parent) {
				return parentNode.mergeChild(this, rightSibling);
			}
		}
		return null;
	}

}