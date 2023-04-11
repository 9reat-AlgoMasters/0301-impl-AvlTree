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

        // Otherwise, traverse the tree to the left or right depending on the key
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

        // Node is null if the tree doesn't contain the key
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

        // Traverse the tree to the left or right depending on the key
        if (key < node.data) {
            node.left = deleteNode(key, node.left);
        } else if (key > node.data) {
            node.right = deleteNode(key, node.right);
        }

        // At this point, "node" is the node to be deleted

        // Node has no children --> just delete it
        else if (node.left == null && node.right == null) {
            node = null;
        }

        // Node has only one child --> replace node by its single child
        else if (node.left == null) {
            node = node.right;
        } else if (node.right == null) {
            node = node.left;
        }

        // Node has two children
        else {
            deleteHaveTwoChild(node);
        }

        return node;
    }

    private void deleteHaveTwoChild(Node node) {
        // Find minimum node of right subtree ("inorder successor" of current node)
        Node isS = findMinimum(node.right);

        // Copy inorder successor's data to current node
        node.data = isS.data;

        // Delete inorder successor recursively
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