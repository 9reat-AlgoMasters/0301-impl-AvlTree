public class AVLTree implements IAVLTree {

    static class Node{
        int value, height;
        Node leftChild;
        Node rightChild;
        Node parent;
        public Node(int value) {
            this.value = value;
            this.height = 1;
        }

        public Node(int value, Node leftChild, Node rightChild) {
            this.value = value;
            this.leftChild = leftChild;
            this.rightChild = rightChild;
        }
    }

    private Node root;
    private int size;

    public AVLTree() {
        root = null;
        size = 0;
    }


    @Override
    public boolean insert(int value) {
        //루트부터 넣을 자리 탐색
        Node node = root;
        Node parent = null;

        //넣을 자리를 찾을 때까지 반복
        while(node != null){
            //넣을 자리를 찾았을 때 node는 null이므로 그 부모 노드를 기록해놓아야 함
            parent = node;
            //넣을 값이 현재 node 값보다 작으면 왼쪽 서브트리로, 크면 오른쪽으로 이동
            if(value < node.value){
                node = node.leftChild;
            }else if (value > node.value){
                node = node.rightChild;
            }else{ // 해당 값이 이미 있으면 추가하지않고 끝
                return false;
            }
        }

        Node newNode = new Node(value);
        //루트가 null인 경우 루트로 추가 그 외에는 값 비교해서 기록해 놓은 부모노드의 자식으로 추가
        if(parent == null){
            root = newNode;
        }else if(value < parent.value){
            parent.leftChild = newNode;
        }else{
            parent.rightChild = newNode;
        }
        newNode.parent = parent;

        //avl트리 균형이 맞는지(두 서브트리 높이 차 1이하인지) 확인
        //부모노드부터 루트노드까지 균형 맞춰줌
        balance(parent, value);
        size++;
        return true;
    }

    public void balance(Node node, int value) {
        while(node != null) {
            //아래(자식 노드)에서 균형 맞추다 변경된 높이를 해당 노드에도 반영
            node.height = Math.max(getHeight(node.leftChild), getHeight(node.rightChild)) + 1;

            int bal = getBalance(node);
            if (bal > 1) {
                //왼쪽 서브트리쪽으로 치우침, 넣을 노드가 자식의 왼쪽 : 오른쪽으로 회전 한번만
            /*     N                            C
                 /                            /   \
               C                 ->          A     N
              /
             A

            왼쪽 서브트리쪽으로 치우침, 넣을 노드가 자식 오른쪽 : 자식 왼쪽 회전 후 노드 오른쪽 회전

                    N                  N            A
                  /                   /            / \
                C             ->    A     ->     C     N
                 \                 /
                  A               C
             */
                if (node.leftChild.value < value) {
                    node.leftChild = rotateLeft(node.leftChild);
                }
                node = rotateRight(node);

            } else if (bal < -1) {
                //오른쪽 서브트리쪽으로 치우침, 넣을 노드가 자식의 오른쪽 : 왼쪽 회전 한번만
                /*      N                          C
                          \                       /  \
                           C         ->          N    A
                             \
                              A
             오른쪽 서브트리쪽으로 치우침, 넣을 노드가 자식 왼쪽 : 자식 오른쪽 회전 후 노드 왼쪽 회전
                         N                 N           A
                           \                \         /  \
                             C       ->      A    -> N    C
                            /                 \
                           A                   C
                 */
                if (node.rightChild.value > value) {
                    node.rightChild = rotateRight(node.rightChild);
                }
                node = rotateLeft(node);

            }
            //부모노드로 올라가서 반복
            node = node.parent;
        }
    }

    @Override
    public Node find(int value) {
        Node node = root;
        
        while(node != null){
            if(value < node.value){
                node = node.leftChild;
            }else if (value > node.value){
                node = node.rightChild;
            }else{ // 값을 찾으면 true 리턴
                return node;
            }
        }
        return null;
    }

    @Override
    public boolean delete(int value) {
        //value를 가진 노드(삭제할 노드) 찾기
        Node node = find(value);

        if(node != null){
            deleteNode(node);
            size--;
            return true;
        }else{
            //삭제하려는 값이 존재하지 않음
            return false;
        }
    }

    private void deleteNode(Node node) {

        //삭제할 노드의 자식이 없음(리프노드) => 부모노드의 자식에서 없애줌
        if(node.leftChild == null && node.rightChild == null){
            //루트를 삭제하는 경우
            if(node.parent == null){
                root = null;
            }else if(node.parent.leftChild == node){
                node.parent.leftChild = null;
            }else{
                node.parent.rightChild = null;
            }
            //부모부터 올라가며 균형 맞춰주기
            balance(node.parent, node.value);

            //삭제할 노드가 한쪽 자식만 가짐 => 그 자식을 삭제할 노드의 부모노드의 자식으로 대체
            //루트면 그 자식이 새로운 루트가 됨
        }else if(node.rightChild == null){
            if(node.parent == null) {
                root = node.leftChild;
            }else if(node.parent.leftChild == node){
                node.parent.leftChild = node.leftChild;
            }else{
                node.parent.rightChild = node.leftChild;
            }
            node.leftChild.parent = node.parent;

            balance(node.parent, node.value);
        }else if(node.leftChild == null){
            if(node.parent == null){
                root = node.rightChild;
            }else if(node.parent.leftChild == node){
                node.parent.leftChild = node.rightChild;
            }else{
                node.parent.rightChild = node.rightChild;
            }
            node.rightChild.parent = node.parent;

            balance(node.parent, node.value);

            //삭제할 노드가 2개의 자식을 가짐
        }else {
            //삭제할 노드의 오른쪽 서브트리에서 가장 작은 값노드를 찾아서 부모노드의 자식 또는 루트로 설정
            Node found = findFromRightSubTree(node);
            Node parent = found.parent;

            //찾아낸 found은 왼쪽 자식이 없음 => 삭제할 노드의 왼쪽 자식을 갖다 붙임
            found.leftChild = node.leftChild;
            node.leftChild.parent = found;

            //found가 삭제할 노드의 오른쪽 자식이었다면 그냥 그대로 삭제할 노드 자리로 올라오면 문제 없음
            //아니라면 found의 기존 오른쪽 자식 노드를 처리한 후 삭제할 노드의 오른쪽 자식을 새로 붙여줘야함
            if(found != node.rightChild){
                if(found.rightChild != null){
                    found.parent.leftChild = found.rightChild;
                    found.rightChild.parent = found.parent;
                }else{
                    found.parent.leftChild = null;
                }
                found.rightChild = node.rightChild;
                node.rightChild.parent = found;
            }else{
                parent = found;
            }

          //found를 삭제될 node 자리로 
            if(node.parent == null){
                root = found;
            }else if(node.parent.leftChild == node){
                node.parent.leftChild = found;
            }else{
                node.parent.rightChild = found;
            }
            found.parent = node.parent;

            balance(parent, node.value);
        }
    }

    public Node findFromRightSubTree(Node node){
        //오른쪽 서브트리에서 제일 작은 값 => 왼쪽으로만 가다가 왼쪽 자식 없으면 해당 노드 리턴
        Node target = node.rightChild;
        while(target.leftChild != null){
            target = target.leftChild;
        }

        return target;
    }

    //노드를 오른쪽으로 회전
    public Node rotateRight(Node node){
        Node temp = node.leftChild;
        Node temp2 = temp.rightChild;
        //temp의 오른쪽 자식을 node의 왼쪽 자식으로 붙여줄 것
        if(temp2 != null){
            temp2.parent = node;
        }

        //temp를 node의 부모로 바꿔주기
        //기존 node의 부모자식 관계를 temp가 이어받도록 함
        temp.rightChild = node;
        temp.parent = node.parent;
        if(temp.parent == null){
            root = temp;
        }else if(node == temp.parent.leftChild){
            temp.parent.leftChild = temp;
        }else{
            temp.parent.rightChild = temp;
        }

        node.leftChild = temp2;
        node.parent = temp;

        node.height = Math.max(getHeight(node.leftChild), getHeight(node.rightChild)) + 1;
        temp.height = Math.max(getHeight(temp.leftChild), getHeight(temp.rightChild)) + 1;

        return temp;
    }


    public Node rotateLeft(Node node){
        Node temp = node.rightChild;
        Node temp2 = temp.leftChild;
        if(temp2 != null){
            temp2.parent = node;
        }

        temp.leftChild = node;
        temp.parent = node.parent;
        if(temp.parent == null){
            root = temp;
        }else if(node == temp.parent.leftChild){
            temp.parent.leftChild = temp;
        }else{
            temp.parent.rightChild = temp;
        }

        node.rightChild = temp2;
        node.parent = temp;

        node.height = Math.max(getHeight(node.leftChild), getHeight(node.rightChild)) + 1;
        temp.height = Math.max(getHeight(temp.leftChild), getHeight(temp.rightChild)) + 1;

        return temp;
    }

    public int getHeight(Node node){
        if(node == null){
            return 0;
        }
        return node.height;
    }

    public int getBalance(Node node){
        if(node == null){
            return 0;
        }
        //왼쪽 서브트리 자식 수가 많으면 양수
        return getHeight(node.leftChild) - getHeight(node.rightChild);
    }

    public void inOrder(Node node){
        if(node != null) {
            inOrder(node.leftChild);
            System.out.println(node.value+" "+node.height);

            inOrder(node.rightChild);
        }
    }

    public void inOrder() {
        inOrder(root);
    }
}
