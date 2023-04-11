class Node {
    int data;
    Node left;
    Node right;
    int height;

    public Node(int data) {
        this.data = data;
    }
}
public class AVLTree{
    int size;
    Node root;
    public int height(Node node) {
        return node != null ? node.height : -1;
    }
    public void updateHeight(Node node) { // 던저져지는 node의 height를 자식들의 height와 비교하여, 큰놈에 +1
        int leftChildHeight = height(node.left);
        int rightChildHeight = height(node.right);
        node.height = leftChildHeight -rightChildHeight;
    }
    public int balanceFactor(Node node) {
        return height(node.right) - height(node.left);
    }

    public Node rotateRight(Node node) {
        Node leftChild = node.left;

        node.left = leftChild.right;
        leftChild.right = node;

        updateHeight(node);
        updateHeight(leftChild);

        return leftChild;
    }

    public Node rotateLeft(Node node) {
        Node rightChild = node.right;

        node.right = rightChild.left;
        rightChild.left = node;

        updateHeight(node);
        updateHeight(rightChild);

        return rightChild;
    }

    public Node reBalance(Node node) {
        int balanceFactor = balanceFactor(node);

        // 왼쪽이 더 크면
        if (balanceFactor < -1) {
            node = rotateRight(node); // 우회전
            if (balanceFactor(node.left) > 0) {
                node.left = rotateLeft(node.left); //좌-우회전
            }
        }

        //오른쪽이 더 크면
        if (balanceFactor > 1) {
            node = rotateLeft(node); // 좌회전
            if(balanceFactor(node.right) < 0){
                node.right = rotateRight(node.right); // 우-좌회전
            }
        }

        return node;
    }

    // insert start //
    public void insertKey(int key) {
        root = insert(key, root);
        size++;
    }
    Node insert(int key, Node node) {
        node = insertNode(key, node);

        updateHeight(node);

        return reBalance(node);
    }
    Node insertNode(int key, Node node) {
        // 노드가 null이면 새로운 노드 만들기
        if (node == null) {
            node = new Node(key);
        }

        // key값과 node의 값을 비교하여 왼쪽, 오른쪽에 넣을지 정하기
        else if (key < node.data) {
            node.left = insertNode(key, node.left);
        } else if (key > node.data) {
            node.right = insertNode(key, node.right);
        } else {
            throw new IllegalArgumentException("이미 key 값이 존재합니다. " + key);
        }

        return node;
    }

    // insert end //

    // delete start //

    public void deleteKey(int key) {
        root = delete(key, root);
        size--;
    }
    Node delete(int key, Node node) {
        node = deleteNode(key, node);


        if (node == null) {
            return null;
        }

        updateHeight(node);

        return reBalance(node);
    }

    Node deleteNode(int key, Node node) {
        // No node at current position --> go up the recursion
        if (node == null) {
            return null;
        }

        // key값과 node의 값을 비교하여, 같을때까지 함수를 호출하며 삭제해주기
        if (key < node.data) {
            node.left = deleteNode(key, node.left);
        } else if (key > node.data) {
            node.right = deleteNode(key, node.right);
        }

        // 리프 노드일경우
        else if (node.left == null && node.right == null) {
            node = null;
        }

        // 자식이 한개일 경우
        else if (node.left == null) {
            node = node.right;
        } else if (node.right == null) {
            node = node.left;
        }

        // 자식이 두 명일 경우
        else {
            deleteHaveTwoChild(node);
        }

        return node;
    }

    // 자식이 두개 존재할때 삭제하는 메서드
    private void deleteHaveTwoChild(Node node) {

        //오른쪽 자식노드에 대해서 가장 작은 자식을 찾고
        Node isS = findMinimum(node.right);

        // 교해주며
        node.data = isS.data;

        // 오른쪽 자식노드에 대해서도 deleteNode를 다시 호출하며 수행해주어, 결과적으로 이진탐색트리의 형태를 갖게만듬
        node.right = deleteNode(isS.data, node.right);
    }

    private Node findMinimum(Node node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }
    // delete end //

    public void printHeightByInOrder() {
        inrOrderReCurHeight(root);
        System.out.println();
    }

    private void inrOrderReCurHeight(Node node) {
        if(node == null) return;

        inrOrderReCurHeight(node.left);
        System.out.print(node.data + "[height:"+ node.height+"]  ");
        inrOrderReCurHeight(node.right);
    }

    public void inrOrderPrint() {
        inrOrderReCur(root);
        System.out.println();
    }
    public void inrOrderReCur(Node node) {
        if(node == null) return;

        inrOrderReCur(node.left);
        System.out.print(node.data + " ");
        inrOrderReCur(node.right);

    }

}