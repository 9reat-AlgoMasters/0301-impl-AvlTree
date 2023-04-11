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
                updateHeight(node, 2);
            } else {
                insertRecur(node.left, value);
            }
        } else {
            if (node.right == null) {
                node.right = new Node(value);
                node.right.parent = node;
                updateHeight(node, 2);
            } else {
                insertRecur(node.right, value);
            }
        }
    }


    private void makeBalanced(Node node) {
        if (isBalanced(node)) return;

        if (node.getBalanceFactor() == 2) { // LL, LR
            if (node.left.getBalanceFactor() == 1) { // LL
                rotateRight(node);
            } else if (node.left.getBalanceFactor() == -1) { // LR
                rotateLeft(node.left);
                rotateRight(node);
            }
        } else if (node.getBalanceFactor() == -2) { // RR, RL
            if (node.right.getBalanceFactor() == -1) { // RR
                rotateLeft(node);
            } else if (node.right.getBalanceFactor() == 1) { // RL
                rotateRight(node.right);
                rotateLeft(node);
            }
        }
    }

    private void rotateLeft(Node node) {
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
        updateHeight(node.parent, height + 1);
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
//        System.out.printf("update! height : %d\n", height);
        if (node == null) {
            return;
        }

        Node updatedChild = updatedChildSide ? node.left : node.right;
        int maxChildHeight = findMaxHeightOfChild(node);

        if (updatedChild.height == maxChildHeight) {
            node.height =  updatedChild.height + 1;
            if (isUnbalanced(node)) {
                makeBalanced(node);
                node = node.parent;
            }
            if (node != root) {
                updateHeight(node.parent, node.parent.left == node);
            }

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
            updateHeight(target.parent, isTargetLeftChild);

        } else if (target.hasOneChild()) { // 자식이 하나만 있을 때
            Node next = target.left == null ? target.right : target.left;
            if (target == root) {
                root = next;
                return;
            }

            next.parent = target.parent;
            if (target.parent.left == target) {
                target.parent.left = next;
            } else {
                target.parent.right = next;
            }
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