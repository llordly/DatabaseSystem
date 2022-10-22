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
		// �ɰ� �ΰ��� ��忡 ���� �����͵� �־�����Ѵ�.
		int index = this.search(key.getKey());
		// Ű�� �� �ڸ� �ľ�
		rightNode.parent = this;
		int keyCount = this.size();
		if (index == keyCount) {
			this.p.add(key);
			this.r = rightNode;
		} else {
			this.p.add(index, key);
			this.p.get(index + 1).setPointer(rightNode);
		}
		// Ű�� �־��ش�
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
		// overflow�� �Ͼ�� ��� ��������� insert�ϸ� ó���Ѵ�.
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
		// key�� �������� ���� �ڽĿ��� ������ ������ �ڽĿ��� �������� �����Ѵ�.

		int childIndex = 0;
		while (childIndex < this.size() && this.p.get(childIndex).getPointer() != borrower)
			++childIndex;

		if (LeftOrRight == 0) {
			// lender is right
			// right���� Ű�� �ö���� ������ �������� right���� Ű�� �����ش�.
			int upKey = ((NonleafNode) borrower).redistributeSibling(this.p.get(childIndex).getKey(), lender,
					LeftOrRight);
			this.p.get(childIndex).setKey(upKey);
		} else {
			// lender is left
			// left���� Ű�� �ö���� ������ �������� left���� Ű�� �����ش�.
			int upKey = ((NonleafNode) borrower).redistributeSibling(this.p.get(childIndex - 1).getKey(), lender,
					LeftOrRight);
			this.p.get(childIndex - 1).setKey(upKey);
		}
	}

	protected Node mergeChild(Node leftChild, Node rightChild) {
		// �� ������ Ű�� ������ �� ���� ��쿡 merge�� ���� �ʿ��ϴ�.
		int index = 0;
		while (index < this.size() && this.p.get(index).getPointer() != leftChild)
			++index;

		KeyValue downKey = new KeyValue(this.p.get(index).getKey());
		// �� child�� left�� �� merge�ϰ� �������� key�� ���� child�� ����

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

		// ������ Ű�� ���� �迭���� �����.
		this.deleteAt(index);

		// underflow�� �Ͼ�� ���
		if (this.size() < (this.m - 1) / 2) {
			if (this.parent == null) {
				// �θ� ���� �ڱ��ڽ��� Ű�� ���� ��쿡�� �θ������ ������ ��������� ���� �ڽ��� ��Ʈ�� �ٲ�
				if (this.size() == 0) {
					leftChildNode.parent = null;
					return (Node) leftChildNode;
				} else {
					return null;
				}
			}
			// ���� ��츦 ����� ��������� underflow�� ó���Ѵ�.
			return this.underflow();
		}
		// underflow�� �Ͼ�� �ʾ��� ��쿡�� ����
		return null;
	}

	protected int redistributeSibling(int downKey, Node sibling, int LeftOrRight) {
		// ������ Ű�� ������ �ڸ��� �־��ش�.
		NonleafNode siblingNode = (NonleafNode) sibling;
		if (LeftOrRight == 0) {
			// sibling�� right�̴�.
			// right���� �θ��忡 Ű�� ������ ���� ������ Ű�� �迭�� �������� �־��ְ� ���� Ű�� �����ʹ� r�� �Ű��ش�.
			int index = this.size();
			KeyValue newDownKey = new KeyValue(downKey);
			this.p.add(newDownKey);
			this.p.get(index).setPointer(this.r);
			this.r = siblingNode.p.get(LeftOrRight).getPointer();
			this.r.parent = this;
			siblingNode.p.get(0).setPointer(siblingNode.p.get(1).getPointer());
			// �÷��� Ű�� ������ Ű�̰�, ���������Ƿ� ���� ��������� rightNode�� ù��° Ű�� ����������.
		} else {
			// sibling�� left�̴�.
			// left���� �θ𿡰� Ű�� ������ ���� ������ Ű�� �迭�� ù��°�� �־��־�� �Ѵ�.
			// left�� �������� ��� �ǹǷ� �������� ����Ű�� �ִ� �����ʹ� ������ Ű�� index + 1�� �����Ѿ��Ѵ�.
			// left���� ������ ���� left�� ������ ���� �÷��ְ� left�� ������ �����ʹ� right�� �Ű��ְ�, ������ Ű��
			// right�� ����.
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