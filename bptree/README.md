# <center>2018 B+ tree implementation assignment</center>



**2017029589 컴퓨터소프트웨어학부 류지범**



## #0 Execute

- main문이 있는 class파일은 bptree로 했다.
- `java -jar bptree.jar`



## #1 출력 형식

- java bptree -p index_file output_file 명령어를 추가해서 output_file에 다음과 같이 NonleafNode는 [ ]로 묶어서 level – order로 줄바꿈하여 출력하였고, LeafNode의 경우 { }로 묶고 <key, value>로 묶어서 출력하도록 함
- index_file의 첫번째 줄에는 max degree를 적고 그 아래로는 key, value를 줄바꿈하여 기록함
- Index_file에 트리를 기록할 때 NonLeafNode는 단지 키를 찾기 위한 index-set이므로 index_file에 기록하지 않았고, 트리의 LeafNode의 데이터만 기록함. 직렬화를 통해 트리를 불러와 원래 있던 트리에 기록하고 싶었지만 방대한 데이터를 기록하기엔 무리가 있었고, 시간이 더 걸릴 수 있지만 데이터를 불러올 때에는 다시 새로운 트리를 만들어 데이터를 삽입하도록함. 트리의 구조가 바뀔 순 있지만 데이터는 바뀌지 않는다는 점에서 이런 방식을 사용함. 트리를 불러오고 만드는 과정은 제일 아래에 내용을 썼다.



## #2 Skeleton

- B+트리는 그 구조가 B트리에 기반을 하고 있고 balance tree이기에 트리 높이의 신장과 감소는 LeafNode에서의 삽입이나 삭제를 통해 재귀적으로 overflow와 underflow를 처리하면서 일어난다.
- 우선 클래스의 구성은 다음과 같다.
  - 우선 Node는 LeafNode와 NonLeafNode 두 종류가 있고 이는 공통적으로 Node라는 특성을 가지므로 Node를 abstract class로 만들어 두 노드가 공통적으로 사용할 만한 method나 member를 기본적으로 생성했다.
  - 또한 각 노드에는 최대로 들어갈 수 있는 키와 포인터의 개수가 존재하는데 이를 m이라 하였고, 포인터는 최대 m개 키는 최대 m-1개를 가질 수 있도록 하였다.
  - 보다 쉽게 key, pointer, value를 관리하기 위해 KeyValue class를 만들어 key, pointer, value를 한번에 관리할 수 있게 하였으며, KeyValue class를 자료형으로 하는 `ArrayList<KeyValue> p`를 만들어 배열로써 관리하였다.
  - 또한 노드에는 부모를 가리키는 pointer와 NonLeafNode 인지 LeafNode인지를 판별하는 isLeaf, merge와 redistribution, LeafNode에서의 doubleLinkedList를 위한 rightSibling과 leftSibling을 멤버로 만들었다.
  - 추가적으로 NonLeafNode에는 가장 우측 포인터를 가리키는 r을 가졌다.

- BPlusTree class는 삽입, 삭제, 검색 연산을 Tree단위로 관리하기 위해 만들었으며, root를 멤버로 가지고 있고 LeafNode와 NonLeafNode의 method를 이용하여 작업을 한다.

 

## #3 Code

- Node
  - Underflow를 재귀적으로 처리하기 위한 `underflow()`가 있다. 이 method는 leafNode일 경우와 NonleafNode일 때 underflow를 각자 노드의 merge와 redistribution을 사용하여 처리하게 하는 코드이며, leftSibling에서 key를 빌릴 수 있으면 빌린 후 리턴, rightSibling에서 빌릴 수 있으면 빌린 후 리턴, 둘 다 안될경우 merge작업을 right 혹은 left와 하며 처리를 한다. 이 모든 작업은 해당 노드의 parent에서 key를 내려주거나 parent로 key를 올려주는 작업과 연관되기 때문에 NonLeafNode의 경우 parent의 merge와 redistribute method를 사용한다. 

- LeafNode

  - 삽입과 삭제 모두 LeafNode에서 기본적으로 일어난다.

  - `search()` -  해당 노드에 key가 있는지 여부를 리턴하며, key의 index를 찾아준다.

    - ````java
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

  - `insertion()` - 정렬을 위해 key가 들어가야할 위치의 index를 파악 후 p배열에 넣어준다.

    - ````java
      public void insertion(KeyValue key) {
      		int keyCount = this.size();
      		int i = 0;
      		while (i < keyCount && key.getKey() > this.p.get(i).getKey())
      			i++;
      
      		this.p.add(i, key);
      	}

  - `delete()` - 삭제할 key의 index를 찾은 후 p배열에서 지운다.

    - ````java
      public boolean delete(int key) {
      		int index = this.search(key);
      
      		if (index == -1)
      			return false;
      		this.p.remove(index);
      
      		return true;
      	}
      
  
  - `split()` - 홀수 짝수 고려하지 않고 splitpoint는 2등분한 부분의 index를 내림하였다. Splitpoint를 기준으로 새 right Node를 만들어서 splitpoint의 내용부터 끝 내용까지 옮겨줬으며 기존 left Node에서는 그 내용을 삭제하였다. 그 후 leftSibling과 rightSibling에 대한 연결을 유지해주었다.
  
    - ````java
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
  
  - `mergeSibling()` - 현재 Node를 left라 하고 right의 모든 원소를 left의 뒤에 추가해주고 sibling연결을 설정하면서 기존 left와 right의 연결은 끊어버린다.
  
    - ````java
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
  
  - `redistributeSibling()` - Left에서는 마지막 원소를 빌려와 현재 노드의 첫번째에 넣고, right에서는 첫번째 원소를 빌려와 현재 노드의 마지막에 넣으며 redistribute해준다. 그 후, 옮겨온 key는 원래 Node에서 삭제해준다. right에서 key를 빌려왔을 경우에는 left의 남은 키 중 첫번째가 upkey로 올라가고, left에서 빌려왔을 경우에는 현재 노드의 첫번째가 upkey로 올라간다. (복사)
  
    - ````java
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



- NonLeafNode

  - `search()` - leafNode와 거의 동일

  - `delete()` - 삭제하는 index의 포인터는 건들이지 않고 그 다음부터 한 칸 씩 밀려 앞으로 오게 되고, key의 경우 삭제하는 key의 index로 한 칸 씩 밀려온다. 그 후 마지막 원소를 삭제해준다. 마지막 원소를 삭제할 경우에는 마지막 원소의 포인터를 r로 옮겨주고 삭제한다.

    - ````java
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

    

  - `split()` - LeafNode와는 달리 splitpoint의 키는 위로 올라감과 동시에 삭제된다. 나머지 과정은 LeafNode와 거의 동일

    - ````java
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

  - redistributeSibling()

    - ````java
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
  
  - `mergeChild()` 
  
    - protected Node mergeChild(Node leftChild, Node rightChild) {
      		// 이 과정은 키를 빌려올 수 없을 경우에 merge를 위해 필요하다.
        		int index = 0;
        		while (index < this.size() && this.p.get(index).getPointer() != leftChild)
        			++index;
  
      ```java
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
      ```
  
  - `redistributeChild()`
  
    - ````java
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
  
  - `insertion()`
  
    - ````java
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

 

- BPlusTree
  - `insertion()` - 삽입해야 할 LeafNode까지 찾아가서 삽입 후, overflow가 일어났을 경우에는 leafNode일 경우에는 새 NonleafNode를 만들어서 처리해주고 NonleafNode일 경우에는 재귀적으로 삽입하면서 처리함. 트리가 신장될 때마다 root를 재설정 
  - `singleSearch()` - **단일 검색**. Key가 있을 경우 root Node부터 시작해서 해당 키가 존재하는 LeafNode까지 들렸던 모든 노드의 키를 출력해주고, 마지막에 key의 value를 출력
  - `rangeSearch()` - **범위 검색**. Start부터 end까지 해당하는 LeafNode의 key와 value를 출력하는데 start에 해당하는 키를 포함하는 LeafNode까지 내려가서 오른쪽 노드의 연결을 따라가며 정렬된 키를 출력하는데 end전까지만 출력하도록 한다.
  - `delete()` - 삭제할 key를 포함하는 LeafNode까지 내려가서 삭제하고 재귀적으로 underflow를 해결한다.
  - `search()` -sigleSearch를 하기위해 해당 key가 있는지 없는지를 리턴해주기 위한 method

- Main
  - 트리의 구조를 파일에 쓰기 위해서 Serialization과정을 진행하기 위해 앞선 모든 노드와 트리 class를 serializable interface를 implements하게 했으며, 직렬화가 되지 않는 type은 사용하지 않았다.
  - 하지만 직렬화를 하게 되면 데이터의 양이 많아지는 것을 처리할 수 없어서 직렬화로 file i/o를 하는 것을 포기하고 bufferedwriter와 reader로 처리했다.
  - 명령어는 switch case문을 통해 처리했다.
  - -c 명령어의 경우 새로운 비어있는 Tree를 만들어서 index_file에 쓰도록 했으며 이 경우 index_file에 첫 줄에 max-degree가 기록된다.
  - -i 명령어의 경우 index_file에 있는 첫 줄의 max값을 통해 새로운 트리를 만들고 나머지 줄에 있는 key, value를 통해 새로 트리에 삽입한 후, data_file에 있는 key value를 StringTokenizer를 통해 ,단위로 분리하여 삽입시켰다. 그 후 index_file에 덮어썼다.
  - -d 명령어의 경우 index_file에 있는 Tree를 불러와 다시 새로운 트리를 만들어서 삽입한 후 data_file에 있는 key에 해당하는 부분을 지우고 다시 index_file에 덮어쓰기 하도록 했다.
  - -s 명령어의 경우 singleSearch를 하도록 하는 것이므로 index_file에 있는 Tree를 불러와 새로운 트리를 만들고 데이터를 삽입한 후 해당 key가 있는지 확인 후 있으면 과정을 출력하도록 하고 없으면 “NOT FOUND”를 출력하도록 했다.
  - -r 명령어의 경우 rangeSearch로 index_file에 있는 Tree를 불러와 새로운 트리를 만들고데이터를삽입한 후 start_key와 end_key까지의 모든 key와 value를 줄력하도록 했다.
  - -p 명령어의 경우 output_file에 트리의 정보를 출력할 수 있도록 했습니다. 내부노드의 경우 [ ]로 묶고 단말노드의 경우 { }로 묶고 <key, value>로 표현했다.



## #4 Result

- <img src="/Users/lordly/Desktop/Hanyang/2018_2학기/Database System/bptree_exe.png" alt="bptree_exe" style="zoom:50%;" />
- <img src="/Users/lordly/Desktop/Hanyang/2018_2학기/Database System/inout.png" alt="inout" style="zoom:50%;" />

- max-degree가 8인 tree 를 생성 후, input data를 기반으로 트리를 생성한 후 delete.csv 정보를 기반으로 지우고 output.dat에 출력한 결과
