package implementation.avltree;

public class AVLTree {
    class Node {
        int value;
        Node left;
        Node right;

        Node(int value) {
            this.value = value;
            this.right = null;
            this.left = null;
        }

        public void setLeft(Node left) {
            this.left = left;
        }

        public void setRight(Node right) {
            this.right = right;
        }
    }

    private Node root;
    private int size;	


    public AVLTree() {
        this.root = null;
        this.size = 0;
    }

    // 높이 얻기
    public int getHeight(Node n) {
        if (n == null) {
            return 0;
        }

        int leftHeight = getHeight(n.left);
        int rightHeight = getHeight(n.right);

        return Math.max(leftHeight, rightHeight) + 1;
    }

    public Node leftRotate(Node n) {
        Node temp = n.right;
        if (n == root) {
            root = temp;
        }
        n.right = temp.left; // n의 오른쪽 자식의 왼쪽 자식을 n의 오른쪽에 붙이는 과정
        temp.left = n; //  n의 오른쪽 자식의 왼쪽 자식을 n으로 바꿔버림

        return temp;
    }

    public Node rightRotate(Node n) {
        Node temp = n.left;
        if (n == root) {
            root = temp;
        }
        n.left = temp.left; // n의 왼쪽 자식의 오른쪽 자식을 n의 왼쪽에 붙이는 과정
        temp.right = n; //  n의 왼쪽 자식의 오른쪽 자식을 n으로 바꿔버림

        return temp;
    }

    public Node checkAndRotateByCase(Node n, int val) {
        int bf = balanceFactor(n); // 왼  - 오
        
        // LL
        if(bf > 1 && val < n.left.value) {
            return rightRotate(n);
        }

        // RR
        if (bf < -1 && val > n.right.value) {
            return leftRotate(n);
        }

        // RL
        if(bf < -1 && val < n.right.value) {
            n.right = rightRotate(n.right);
            return leftRotate(n);
        }

        // LR
        if(bf > 1 && val > n.left.value) {
            n.left = leftRotate(n.left);
            return rightRotate(n);
        }

        System.out.println("----");
        return n;
    }
    
    
    // 두 서브트리의 높이 차이
    // 균형이려면 -1, 0, 1 중 하나의 값을 리턴해야 한다
    public int balanceFactor(Node n) {
        if (n == null) {
            return 0;
        }

        int leftHeight = getHeight(n.left);
        int rightHeight = getHeight(n.right);

        return leftHeight - rightHeight;
    }

    //  루트삽입
    public void insertRoot(int key) {
        root = insert(root, key);
        // return root;
    } 

    public Node insert(Node n, int val) {
        if (n == null) {
            return new Node(val);
        }

        if (val < n.value) {
            n.left = insert(n.left, val);
        } else if (val > n.value) {
            n.right = insert(n.right, val);
        }

        return checkAndRotateByCase(n, val); // insert재귀 나오면서 확인후 균형이 아니라면 확인후 바꿔줌
    }

    public Node delete(Node n, int val) {
        if (n == null) {
            return null;
        }

        if (val < n.value) {
            n.left = delete(n.left, val);
        } else if (val > n.value) {
            n.right = delete(n.right, val);
        } else {
            // 자식 0개 또는 1개
            if ((n.left == null) || (n.right == null)) {
                Node temp = null;
                int count = 0;
                if (n.left != null) {
                    temp = n.left;
                    count++;
                }
                if (n.right != null) {
                    temp = n.right;
                    count++;
                }

                // 자식 0개
                if (count == 0) {
                    System.out.println("자식이 없습니다.");
                    temp = n;
                    n = null; //없앰
                    size--;
                }

                // 자식 1개
                if (count == 1) {
                    System.out.println("자식이 한개입니다.");
                    if (temp == n.right) {
                        System.out.println("오른쪽 자식만 있습니다.");
                    } else {
                        System.out.println("왼쪽 자식만 있습니다.");
                    }
                    n = temp; // temp 에는 이미 왼쪽 or 오른쪽 노드를 들고있고 재귀 올라가면서 부모와 연결된다
                    size--;

                }

            } else{ // 자식 2개
                // 자식 2개다  있을때
                // 삭제할 노드가 currentNode
                System.out.println("자식이 2개 있습니다.");
                Node temp = n.right;
                Node tempParent = null;

                // 삭제할 노드의 오른쪽 자식을 봤을때
                // 왼쪽 자식이 null이라면 삭제할 노드의 오른쪽 자식이 올라와야한다.
                // 오른쪽 서브트리의 가장 작은 값을 찾아간다.
                while (temp.left != null) {
                    temp = temp.left;
                }

                n.value = temp.value; //값 바꿔줌

                // 오른쪽에서 제일 작은 노드를 찾았는데
                // 그 노드의 오른쪽 노드가 있는 경우
                // 바로 위의 부모에 연결 해줌
                // => 재귀로 해결
                delete(n.right, temp.value);
                size--;
            }
            
        }

        return checkAndRotateByCase(n, val); // delete재귀 나오면서 확인후 균형이 아니라면 확인후 바꿔줌
    }

    public void inOrder(Node n) {
        if(n == null) {
            return;
        }
        inOrder(n.left);
        System.out.println(n.value);
        inOrder(n.right);

    }
}
