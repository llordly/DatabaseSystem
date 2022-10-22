import java.io.Serializable;

public class KeyValue implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private int key;
	private int value;
	private Node pointer;

	public int getKey() {
		return key;
	}

	public void setKey(int key) {
		this.key = key;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public Node getPointer() {
		return pointer;
	}

	public void setPointer(Node pointer) {
		this.pointer = pointer;
	}

	public KeyValue(int key, int value) {
		this.key = key;
		this.value = value;
		this.pointer = null;
	}

	public KeyValue(int key) {
		this.key = key;
		this.pointer = null;
	}
}