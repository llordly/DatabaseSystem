import java.util.ArrayList;

public class NonleafNode extends Node {
	
	protected Node r;

	public Node getR() {
		return this.r;
	}
	public NonleafNode(int maxSize) {
		super(maxSize);
		p = new ArrayList<KeyValue>(maxSize);
		this.setRightSibling(null);
		this.setLeftSibling(null);
		isLeaf = false;
	}

	@Override
	public int search(int key) {
		int keyCount = this.size();
		int i = 0;

		while (i < keyCount && key >= this.p.get(i).getKey())
			i++;
		return i;
	}

	@Override
	public Node split() {
		int keyCount = this.size();
		int splitPoint = keyCount / 2;
		NonleafNode newRightNode = new NonleafNode(m);

		for (int i = splitPoint + 1; i < keyCount; ++i) {
			newRightNode.p.add(this.p.get(i));
		}

		for (int i = 0; i < newRightNode.size(); ++i) {
			newRightNode.p.get(i).getPointer().parent = newRightNode;
		}

		for (int i = keyCount - 1; i > splitPoint; --i) {
			this.p.remove(i);
		}

		newRightNode.r = this.r;
		newRightNode.r.parent = newRightNode;
		this.r = this.p.get(splitPoint).getPointer();
		this.p.remove(splitPoint);

		newRightNode.setRightSibling(this.getRightSibling());
		newRightNode.setLeftSibling(this);
		if (this.getRightSibling() != null) {
			this.getRightSibling().setLeftSibling(newRightNode);
		}
		this.setRightSibling(newRightNode);

		return newRightNode;
	}

	public Node insertion(KeyValue key, Node rightNode) {
		// 쪼갠 두개의 노드에 대한 포인터도 넣어줘야한다.
		int index = this.search(key.getKey());
		// 키가 들어갈 자리 파악
		rightNode.parent = this;
		int keyCount = this.size();
		if (index == keyCount) {
			this.p.add(key);
			this.r = rightNode;
		} else {
			this.p.add(index, key);
			this.p.get(index + 1).setPointer(rightNode);
		}
		// 키를 넣어준다
		if (this.size() == m - 1) {
			int splitPoint = this.size() / 2;
			KeyValue upKey = new KeyValue(this.p.get(splitPoint).getKey());

			NonleafNode newRightNode = (NonleafNode) this.split();
			upKey.setPointer(this);

			if (this.parent == null) {
				this.parent = new NonleafNode(m);
			}
			newRightNode.parent = this.parent;
			return ((NonleafNode) this.parent).insertion(upKey, newRightNode);
		} else {
			if (this.parent == null)
				return this;
			else
				return null;
		}
		// overflow가 일어났을 경우 재귀적으로 insert하며 처리한다.
	}

	public void deleteAt(int index) {
		int i = 0;
		int j = 0;

		if (index == this.size() - 1) {
			this.r = this.p.get(index).getPointer();
			this.p.remove(index);
			return;
		}

		for (i = index; i < this.size() - 1; ++i) {
			this.p.get(i).setKey(this.p.get(i + 1).getKey());
		}

		for (j = index; j < this.size() - 2; ++j) {
			this.p.get(j + 1).setPointer(this.p.get(j + 2).getPointer());
		}

		this.p.remove(i);
	}

	protected void redistributeChild(Node borrower, Node lender, int LeftOrRight) {
		// key를 기준으로 왼쪽 자식에서 빌릴지 오른쪽 자식에서 빌릴지를 결정한다.

		int childIndex = 0;
		while (childIndex < this.size() && this.p.get(childIndex).getPointer() != borrower)
			++childIndex;

		if (LeftOrRight == 0) {
			// lender is right
			// right에서 키가 올라오고 포인터 기준으로 right편의 키를 내려준다.
			int upKey = ((NonleafNode) borrower).redistributeSibling(this.p.get(childIndex).getKey(), lender,
					LeftOrRight);
			this.p.get(childIndex).setKey(upKey);
		} else {
			// lender is left
			// left에서 키가 올라오고 포인터 기준으로 left편의 키를 내려준다.
			int upKey = ((NonleafNode) borrower).redistributeSibling(this.p.get(childIndex - 1).getKey(), lender,
					LeftOrRight);
			this.p.get(childIndex - 1).setKey(upKey);
		}
	}

	protected Node mergeChild(Node leftChild, Node rightChild) {
		// 이 과정은 키를 빌려올 수 없을 경우에 merge를 위해 필요하다.
		int index = 0;
		while (index < this.size() && this.p.get(index).getPointer() != leftChild)
			++index;

		KeyValue downKey = new KeyValue(this.p.get(index).getKey());
		// 두 child를 left에 다 merge하고 내려가는 key는 왼쪽 child에 삽입

		NonleafNode leftChildNode = (NonleafNode) leftChild;
		NonleafNode rightChildNode = (NonleafNode) rightChild;

		downKey.setPointer(leftChildNode.r);
		leftChildNode.p.add(downKey);

		for (int i = 0; i < rightChildNode.size(); ++i) {
			rightChildNode.p.get(i).getPointer().parent = leftChildNode;
			leftChildNode.p.add(rightChildNode.p.get(i));
		}
		leftChildNode.r = rightChildNode.r;
		leftChildNode.r.parent = leftChildNode;
		leftChildNode.setRightSibling(rightChildNode.getRightSibling());
		if (rightChildNode.getRightSibling() != null)
			rightChildNode.getRightSibling().setLeftSibling(leftChildNode);

		// 내려간 키는 현재 배열에서 지운다.
		this.deleteAt(index);

		// underflow가 일어났을 경우
		if (this.size() < (this.m - 1) / 2) {
			if (this.parent == null) {
				// 부모도 없고 자기자신의 키도 없을 경우에는 부모노드와의 연결을 끊어버리고 왼쪽 자식을 루트로 바꿈
				if (this.size() == 0) {
					leftChildNode.parent = null;
					return (Node) leftChildNode;
				} else {
					return null;
				}
			}
			// 위의 경우를 빼고는 재귀적으로 underflow를 처리한다.
			return this.underflow();
		}
		// underflow가 일어나지 않았을 경우에는 종료
		return null;
	}

	protected int redistributeSibling(int downKey, Node sibling, int LeftOrRight) {
		// 내려온 키는 마지막 자리에 넣어준다.
		NonleafNode siblingNode = (NonleafNode) sibling;
		if (LeftOrRight == 0) {
			// sibling은 right이다.
			// right에서 부모노드에 키를 빌려줄 때는 내려온 키는 배열의 마지막에 넣어주고 빌린 키의 포인터는 r로 옮겨준다.
			int index = this.size();
			KeyValue newDownKey = new KeyValue(downKey);
			this.p.add(newDownKey);
			this.p.get(index).setPointer(this.r);
			this.r = siblingNode.p.get(LeftOrRight).getPointer();
			this.r.parent = this;
			siblingNode.p.get(0).setPointer(siblingNode.p.get(1).getPointer());
			// 올려줄 키는 빌려줄 키이고, 빌려줬으므로 원래 형제노드인 rightNode의 첫번째 키는 지워버린다.
		} else {
			// sibling은 left이다.
			// left에서 부모에게 키를 빌려줄 때는 내려온 키는 배열의 첫번째에 넣어주어야 한다.
			// left의 마지막은 비게 되므로 마지막이 가르키고 있던 포인터는 내려온 키의 index + 1이 가르켜야한다.
			// left에서 빌려줄 때는 left의 마지막 값은 올려주고 left의 마지막 포인터는 right로 옮겨주고, 내려온 키는
			// right에 들어간다.
			Node leftNodeChild = siblingNode.r;
			Node rightNodeChild;
			if (this.size() == 0)
				rightNodeChild = this.r;
			else
				rightNodeChild = this.p.get(0).getPointer();
			KeyValue newDownKey = new KeyValue(downKey);

			leftNodeChild.parent = this;
			newDownKey.setPointer(leftNodeChild);

			int index = 0;
			int keyCount = this.size();
			if (index == keyCount) {
				this.p.add(newDownKey);
				this.r = rightNodeChild;
			} else {
				this.p.add(index, newDownKey);
				this.p.get(index + 1).setPointer(rightNodeChild);
			}
		}
		int upKey = siblingNode.p.get(LeftOrRight).getKey();
		siblingNode.deleteAt(LeftOrRight);
		return upKey;
	}

}