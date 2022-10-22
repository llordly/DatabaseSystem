import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.Queue;
import java.util.StringTokenizer;

public class BPlusTree implements Serializable {

	private static final long serialVersionUID = 1L;

	private Node root;

	public Node getRoot() {
		return root;
	}

	public BPlusTree(int max) {
		this.root = new LeafNode(max);
	}

	public void insertion(int key, int value) throws IOException {
		KeyValue keyValue = new KeyValue(key, value);
		Node findNode = this.root;
		while (!findNode.isLeaf()) {
			if (findNode.size() == findNode.search(key)) {
				findNode = ((NonleafNode) findNode).r;
			} else {
				findNode = ((NonleafNode) findNode).p.get(findNode.search(key)).getPointer();
			}
		}
		LeafNode leafNode = (LeafNode) findNode;
		leafNode.insertion(keyValue);

		if (leafNode.size() == leafNode.m - 1) {
			int splitPoint = leafNode.size() / 2;
			KeyValue upKey = new KeyValue(leafNode.p.get(splitPoint).getKey());
			LeafNode newRightNode = (LeafNode) leafNode.split();
			newRightNode.parent = leafNode.parent;
			upKey.setPointer(leafNode);
			NonleafNode newRoot;
			Node Root = null;

			if (leafNode.parent == null) {
				newRoot = new NonleafNode(leafNode.m);
				newRoot.insertion(upKey, newRightNode);
				this.root = newRoot;
				leafNode.parent = newRoot;
				newRightNode.parent = newRoot;
			} else {
				Root = ((NonleafNode) leafNode.parent).insertion(upKey, newRightNode);
			}
			if (Root != null)
				this.root = Root;
		}
	}

	public int singleSearch(int key) {
		Node findNode = this.root;
		for (int i = 0; i < findNode.size(); ++i) {
			System.out.print(findNode.p.get(i).getKey());
			if (i == findNode.size() - 1)
				break;
			System.out.print(",");
		}
		System.out.println();
		while (!findNode.isLeaf()) {
			if (findNode.size() == findNode.search(key)) {
				findNode = ((NonleafNode) findNode).r;
			} else {
				findNode = ((NonleafNode) findNode).p.get(findNode.search(key)).getPointer();
			}
			if (findNode.isLeaf)
				break;
			for (int i = 0; i < findNode.size(); ++i) {
				System.out.print(findNode.p.get(i).getKey());
				if (i == findNode.size() - 1)
					break;
				System.out.print(",");
			}
			System.out.println();
		}
		LeafNode leafNode = (LeafNode) findNode;
		if (leafNode.search(key) != -1) {
			System.out.println(leafNode.p.get(leafNode.search(key)).getValue());
			return leafNode.p.get(leafNode.search(key)).getKey();
		} else
			return -1;
	}

	public void rangeSearch(int start, int end) {
		Node findNode = this.root;
		while (!findNode.isLeaf()) {
			if (findNode.size() == findNode.search(start)) {
				findNode = ((NonleafNode) findNode).r;
			} else {
				findNode = ((NonleafNode) findNode).p.get(findNode.search(start)).getPointer();
			}
		}
		Node findLeaf = this.root;
		while (!findLeaf.isLeaf()) {
			findLeaf = findLeaf.p.get(0).getPointer();
		}
		
		LeafNode leafNode = (LeafNode) findNode;
		if (leafNode.search(start) == -1) {
			if (start < findLeaf.p.get(0).getKey()) {
				leafNode = (LeafNode) findLeaf;
				for (int i = 0; i < leafNode.size(); ++i) {
					if (leafNode.p.get(i).getKey() <= end) {
						System.out.print(leafNode.p.get(i).getKey() + ",");
						System.out.println(leafNode.p.get(i).getValue());
					}
					else
						return;
				}
			}
			if (leafNode.getRightSibling() != null && leafNode.getRightSibling().p.get(0).getKey() <= end) {
				while (leafNode.getRightSibling() != null) {
					for (int i = 0; i < leafNode.getRightSibling().size(); ++i) {
						if (leafNode.getRightSibling().p.get(i).getKey() <= end) {
							System.out.print(leafNode.getRightSibling().p.get(i).getKey() + ",");
							System.out.println(leafNode.getRightSibling().p.get(i).getValue());
						} else
							return;
					}
					leafNode = (LeafNode) leafNode.getRightSibling();
				}
			}
		} else {
			int index = leafNode.search(start);
			for (int i = index; i < leafNode.size(); ++i) {
				if (leafNode.p.get(i).getKey() <= end) {
					System.out.print(leafNode.p.get(i).getKey() + ",");
					System.out.println(leafNode.p.get(i).getValue());
				} else
					return;
			}
			if (leafNode.getRightSibling() != null && leafNode.getRightSibling().p.get(0).getKey() <= end) {
				while (leafNode.getRightSibling() != null) {
					for (int i = 0; i < leafNode.getRightSibling().size(); ++i) {
						if (leafNode.getRightSibling().p.get(i).getKey() <= end) {
							System.out.print(leafNode.getRightSibling().p.get(i).getKey() + ",");
							System.out.println(leafNode.getRightSibling().p.get(i).getValue());
						} else
							return;
					}
					leafNode = (LeafNode) leafNode.getRightSibling();
				}
			}
		}
		return;
	}

	public void delete(int key) {
		Node findNode = this.root;
		while (!findNode.isLeaf()) {
			if (findNode.size() == findNode.search(key)) {
				findNode = ((NonleafNode) findNode).r;
			} else {
				findNode = ((NonleafNode) findNode).p.get(findNode.search(key)).getPointer();
			}
		}
		LeafNode leafNode = (LeafNode) findNode;

		if (leafNode.delete(key)) {
			if (leafNode.size() < (leafNode.m - 1) / 2) {
				Node newRoot = leafNode.underflow();
				if (newRoot != null)
					this.root = newRoot;
			}
		}
	}

	public void printTree(BufferedWriter bw) throws IOException {
		Queue<Node> queue = new LinkedList<Node>();
		Node findLeaf = root;

		queue.offer(root);
		while (!findLeaf.isLeaf()) {
			findLeaf = queue.poll();
			if (findLeaf.isLeaf)
				break;
			bw.write("[");
			for (int i = 0; i < findLeaf.size(); ++i) {
				bw.write(String.valueOf(findLeaf.p.get(i).getKey()));
				if (i == findLeaf.size() - 1) break;
				bw.write(", ");
			}
			bw.write("]");
			for (int i = 0; i < findLeaf.size(); ++i) {
				queue.offer(findLeaf.p.get(i).getPointer());
			}
			queue.offer(((NonleafNode) findLeaf).getR());
			bw.newLine();
		}

		while (findLeaf != null) {
			bw.write("{");
			for (int i = 0; i < findLeaf.size(); ++i) {
				bw.write("<");
				bw.write(String.valueOf(findLeaf.p.get(i).getKey()));
				bw.write(", ");
				bw.write(String.valueOf(findLeaf.p.get(i).getValue()));
				bw.write(">");
				if (i == findLeaf.size() - 1) break;
				bw.write(", ");
			}
			bw.write("} ");
			if (findLeaf.getRightSibling() == null)
				break;
			findLeaf = findLeaf.getRightSibling();
		}

	}
	
	public void writeTree(BufferedWriter bw) throws IOException {
		Node findLeaf = root;
		bw.write(String.valueOf(root.m));
		bw.newLine();
		while (!findLeaf.isLeaf()) {
			findLeaf = findLeaf.p.get(0).getPointer();
		}
		
		while (findLeaf != null) {
			for (int i = 0; i < findLeaf.size(); ++i) {
				bw.write(String.valueOf(findLeaf.p.get(i).getKey()));
				bw.write(",");
				bw.write(String.valueOf(findLeaf.p.get(i).getValue()));
				bw.newLine();
			}
			findLeaf = findLeaf.getRightSibling();
		}
	}
	
	public int search(int key) {
		Node findNode = this.root;
		while (!findNode.isLeaf()) {
			if (findNode.size() == findNode.search(key)) {
				findNode = ((NonleafNode) findNode).r;
			} else {
				findNode = ((NonleafNode) findNode).p.get(findNode.search(key)).getPointer();
			}
		}
		LeafNode leafNode = (LeafNode) findNode;
		return leafNode.search(key);
	}

}