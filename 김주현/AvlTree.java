import exceptions.CustomDuplicatedElementException;

import java.util.ArrayDeque;
import java.util.Deque;

public class AvlTree implements SearchTree {
    private static final boolean LEFT = true;
    private static final boolean RIGHT = true;

    private int debug=0;
    static class Node {
        int value;
        int height;
        Node parent, left, right;

        public Node(int value) {
            this.value = value;
            height = 1;
            parent = null;
            left = null;
            right = null;
        }

        boolean isChildEmpty() {
            return left == null && right == null;
        }

        boolean hasAllChild() {
            return left != null && right != null;
        }

        boolean hasOneChild() {
            return !isChildEmpty() && !hasAllChild();
        }

        int getBalanceFactor() {
            int leftNodeHeight = left == null ? 0 : left.height;
            int rightNodeHeight = right == null ? 0 : right.height;
            System.out.printf("%s ----> left : %d, right : %d\n", this, leftNodeHeight, rightNodeHeight);
            return leftNodeHeight - rightNodeHeight;
        }

        @Override
        public String toString() {
            return "Node{" +
                    "value=" + value +
                    ", height=" + height +
                    '}';
        }
    }

    private Node root;
    private int size;

    @Override
    public void insert(int value) {
        if (isEmpty()) {
            root = new Node(value);
        } else {
            insertRecur(root, value);
        }
        size++;
    }

    private void insertRecur(Node node, int value) {
        if (node.value == value) {
            throw new CustomDuplicatedElementException(String.format("이미 %d는 트리에 있습니다.", value));
        }

        if (value < node.value) {
            if (node.left == null) {
                node.left = new Node(value);
                node.left.parent = node;
                updateHeight(node, LEFT);
            } else {
                insertRecur(node.left, value);
            }
        } else {
            if (node.right == null) {
                node.right = new Node(value);
                node.right.parent = node;
                updateHeight(node, RIGHT);
            } else {
                insertRecur(node.right, value);
            }
        }
        // height 갱신
        // makeBalanced
    }


    private void makeBalanced(Node node) {
        if (isBalanced(node)) return;

        if (node.getBalanceFactor() == 2) { // LL, LR
            if (node.left.getBalanceFactor() >= 0) { // LL
                rotateRight(node);
            } else { // LR
                rotateLeft(node.left);
                rotateRight(node);
            }
        } else if (node.getBalanceFactor() == -2) { // RR, RL
            if (node.right.getBalanceFactor() <= 0) { // RR
                rotateLeft(node);
            } else { // RL
                System.out.println("RL!!!");
                rotateRight(node.right);
                System.out.println("RL에서 Right 완료");
                printTree();
                rotateLeft(node);
            }
        }
    }

    private void rotateLeft(Node node) {
        System.out.printf("Rotate Left ---> %s\n", node);
        Node nextCenter = node.right;

        if (nextCenter.left != null) {
            node.right = nextCenter.left;
            nextCenter.left.parent = node;
        } else {
            node.right = null;
        }
    
        if (node.parent != null) {
            if (node.parent.left == node) {
                node.parent.left = nextCenter;
            } else {
                node.parent.right = nextCenter;
            }
            nextCenter.parent = node.parent;
        } else {
            nextCenter.parent = null;
            root = nextCenter;
        }
    
        node.parent = nextCenter;
        nextCenter.left = node;
        changeHeight(node, findMaxHeightOfChild(node)+1);
    }
    
    private void rotateRight(Node node) {
        System.out.printf("rotate Right -- > %s\n", node);
        Node nextCenter = node.left;

        if (nextCenter.right != null) {
            node.left = nextCenter.right;
            nextCenter.right.parent = node;
        } else {
            node.left = null;
        }
    
        if (node.parent != null) {
            if (node.parent.left == node) {
                node.parent.left = nextCenter;
            } else {
                node.parent.right = nextCenter;
            }
            nextCenter.parent = node.parent;
        } else {
            nextCenter.parent = null;
            root = nextCenter;
        }
        
        node.parent = nextCenter;
        nextCenter.right = node;

        System.out.println("right에서 chage 직전");
        printTree();
        changeHeight(node, findMaxHeightOfChild(node)+1);
    }
    
    private int findMaxHeightOfChild(Node node) {
        int leftChildHeight = node.left == null ? 0 : node.left.height;
        int rightChildHeight = node.right == null ? 0 : node.right.height;
        return Math.max(leftChildHeight, rightChildHeight);
    }

    private void updateHeight(Node node, int height) {
//        System.out.printf("update! height : %d\n", height);
        if (node == null) {
            return;
        }

        if (node.height < height) {
            node.height = height;
            updateHeight(node.parent, height + 1);
            if (isUnbalanced(node)) {
                makeBalanced(node);
            }

        }
    }

    private boolean isUnbalanced(Node node) {
        return Math.abs(node.getBalanceFactor()) > 1;
    }

    private boolean isBalanced(Node node) {
        return Math.abs(node.getBalanceFactor()) <= 1;
    }
    
    private void changeHeight(Node node, int height) {
//        System.out.printf("change! height : %d\n", height);
        if (node == null) {
            return;
        }
        
        node.height = height;
        updateHeight(node.parent, node.parent.left == node);
        if (isUnbalanced(node)) {
            makeBalanced(node);
        }
    }

    @Override
    public boolean contains(int value) {
        return containsRecur(root, value);
    }

    private boolean containsRecur(Node node, int value) {
        if (node == null) {
            return false;
        }

        if (node.value == value) {
            return true;
        }

        if (value < node.value) {
            return containsRecur(node.left, value);
        } else {
            return containsRecur(node.right, value);
        }
    }

    private void updateHeight(Node node, boolean updatedChildSide) {
        System.out.printf("[IN] node : %s\n", node);
//        System.out.printf("update! height : %d\n", height);
        if (node == null) {
            return;
        }

        Node updatedChild = updatedChildSide ? node.left : node.right;
        int maxChildHeight = findMaxHeightOfChild(node);
        int updatedChildHeight = updatedChild == null ? 0 : updatedChild.height;
        System.out.printf("maxChildHeight : %d, updatedChildHeight : %d\n", maxChildHeight, updatedChildHeight);

        boolean canBeUpdated = updatedChildHeight == maxChildHeight;
        if (canBeUpdated) {
            node.height =  updatedChild.height + 1;
        }

        if (isUnbalanced(node)) { // canbeUpdated 가 바뀔 수 있음
            makeBalanced(node);
            node = node.parent;
            canBeUpdated = true;
        }

        if (canBeUpdated && node != root) { //
            updateHeight(node.parent, node.parent.left == node);
        }
    }

    @Override
    public void delete(int value) {
        Node target = findByValue(root, value); // 없으면 CustomDuplicatedElementException 발생
//        System.out.printf("찾은 target : %d\n", target.value);


        if (target.isChildEmpty()) { // 자식이 없을 때 --> 높이만 갱신
            if (target == root) {
                root = null;
                return;
            }

            boolean isTargetLeftChild = target.parent.left == target;
            if (target.parent.left == target) {
                target.parent.left = null;
            } else {
                target.parent.right = null;
            }
            System.out.printf("자식이 없을 때 지우고 %s를 업데이트!\n", target.parent);
            updateHeight(target.parent, isTargetLeftChild);

        } else if (target.hasOneChild()) { // 자식이 하나만 있을 때
            Node next = target.left == null ? target.right : target.left;
            if (target == root) {
                root = next;
                return;
            }

            boolean isTargetLeftChild = target.parent.left == target;
            next.parent = target.parent;
            if (target.parent.left == target) {
                target.parent.left = next;
            } else {
                target.parent.right = next;
            }
            updateHeight(target.parent, isTargetLeftChild);

        } else { // 자식이 둘일 때
            Node replaceNode = findReplaceNode(target.left);
            if (target == root) {
                System.out.println("root!");
                root = replaceNode;
            } else {
                if (target.parent.left == target) {
                    target.parent.left = replaceNode;
                } else {
                    target.parent.right = replaceNode;
                }
            }

            Node updateNode = replaceNode.parent;
            boolean isReplaceNodeLeftChild = replaceNode.parent.left == replaceNode;
            if (replaceNode.left != null) {
                if (replaceNode == target.left) {
                    replaceNode.parent.left = replaceNode.left;
                } else {
                    replaceNode.parent.right = replaceNode.left;
                }
            } else {
                if (replaceNode == target.left) {
                    replaceNode.parent.left = null;
                } else {
                    replaceNode.parent.right = null;
                }
            }

            replaceNode.parent = target == root ? null : target.parent;
            target.right.parent = replaceNode;


            replaceNode.left = target.left;
            replaceNode.right = target.right;

            updateHeight(updateNode, isReplaceNodeLeftChild);
        }

        size--;
    }

    private Node findReplaceNode(Node node) {
        while (node.right != null) {
            node = node.right;
        }
        return node;
    }

    private Node findByValue(Node node, int value) {
        if (node == null) {
            throw new CustomDuplicatedElementException(String.format("%d는 트리에 존재하지 않습니다.", value));
        }

        if (node.value == value) {
            return node;
        }

        if (value < node.value) {
            return findByValue(node.left, value);
        } else {
            return findByValue(node.right, value);
        }
    }

    @Override
    public boolean isEmpty() {
        return size==0;
    }

    @Override
    public int size() {
        return size;
    }

    public void printTree() {
        Deque<Node> q = new ArrayDeque<>();
        q.add(root);
        int level = -1;

        while (!q.isEmpty()) {
            level++;
            int size = q.size();
            System.out.printf("level %d ---> ", level);

            for (int i = 0; i < size; i++) {
                Node now = q.poll();
                System.out.printf("%d(%d) ", now.value, now.height);

                for (int j = 0; j < 2; j++) {
                    Node next = j==0 ? now.left : now.right;
                    if (next == null) continue;
                    q.add(next);
                }
            }
            System.out.println();
        }
    }

    public void showRoot() {
        System.out.println(root);
    }
}