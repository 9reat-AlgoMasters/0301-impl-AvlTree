
package AVL;


public class AVLTree implements IAVLTree {
    static class Node {
        int value;
        int height;
        Node parent;
        Node left;
        Node right;
        
        
        public Node(int value) {
            this.value = value;
            this.height = 1;
            this.parent = null;
            this.left = null;
            this.right = null;
        }
    }
    
    private Node root;
    private int size;
    
    @Override
    public void insert(int value) {
        if(root == null) {
            root = new Node(value);
            size ++;
        }else {
            insertNode(root, value);
        }
    }
    /*
     @ 삽입 연산
     1. 루트에서 시작
     2. 삽입할 값과 루트와 비교
     3. 루트보다 값이 작으면 왼쪽 서브트리에 대해 재귀
        루트보다 값이 크면 오른쪽 서브 트리에 대해 재귀
     4. 자식이 한개이거나 없는 노드에 도달한 후 노드보다 크다면 오른쪽 자식노드로,  작다면 왼쪽 자식노드로 삽입
     */
    //삽입 후 BF가 1,0,-1인지 확인 후 높이 맞추기
    private void insertNode(Node node, int value) {
        if(value < node.value) {
             if(node.left == null) {
                 node.left = new Node(value);
                 node.left.parent = node;
                 size++;
             }else {
                 insertNode(node.left, value);
                 int bf = getBalanceFactor(node);
             }
        }else if(value > node.value){
            if(node.right == null) {
                node.right = new Node(value);
                node.right.parent = node;
                size++;
            }else {
                insertNode(node.right, value);
            }
        }
        //루트에서 단말노드로 재귀로 들어왔던 것들이 기저조건에 닿고 다시 루트로 돌아가면서 다 갱신해준다.
        node.height += Math.max(node.left.height, node.right.height); 
        
        int balance = getBalanceFactor(node);
        
        //왼쪽 - 오른쪽 => 결과가 양수 이면 왼쪽 서브트리의 높이가 더 크다 즉, 왼쪽으로 편향되어 있다.
        //그치만 높이차가 1인 경우까지는 괜찮으니 1보다 큰지 확인해주어야 한다.
        //LL인 경우 => Right Rotation을 진행해주어야 한다.
        if(balance > 1 && value < node.left.value) { //자식의 값보다 더 작다면 (넣은노드(왼쪽)-왼쪽-노드) 일테니까
            rigthRotation(node);
        }
        
        //왼쪽 - 오른쪽 => 결과가 음수이면 오른쪽 서브트리의 높이가 더 크다 즉, 오른쪽으로 편향되어 있다.
        //RR인 경우 =>Left Rotation을 진행해주어야 한다.
        //leftRight => 
        if(balance < -1 && value > node.right.value ) {
            leftRotation(node);
        }
        
        
        // 자식의 값보다 더 크다면 (넣은노드(오른쪽)-왼쪽자식-노드)일테니까 
        //    /    
        //    \
        // LR인 경우  => 아래에 있던 LR이 위로 올라옴
        /*     node             node			 LR
             L        =>      LR       =>      L    node
         *    LR    (Left)  L    b 	  (Right)   a  b
         *   a  b            a  
         */
        if(balance > 1 && value > node.left.value) {
            leftRotation(node.left);
            rigthRotation(node);
        }
        
        // 자식의 값보다 더 작다면 (넣은노드(왼쪽)-오른쪽자식-노드)일테니까 
        //    \   
        //    /
        // RL인 경우 => 아래에 있던 LR이 위로 올라옴
        /*  node             node			      RL
	            R        =>      RL    =>    node    R 
	    *     RL      (Right)  	a  R  (Left)    a   b
	    *    a  b                 b  
	    */
        if(balance < -1 && value < node.right.value) {
        	rigthRotation(node.right);
        	leftRotation(node);
        }
    }
    
    //LL인 경우
    /*     node           L
          L       =>  LL    node
     *  LL LR              LR
     */
    private void rigthRotation(Node node) {
        Node L = node.left;
        Node LR = node.left.right;
        
        L.parent = node.parent;
        
        node.parent = L;
        node.left = LR;
        
        L.right = node;
        
        LR.parent = node;
        
        node.height = Math.max(node.left.height, node.right.height)+1;
        L.height = Math.max(L.left.height, L.right.height)+1;
    }
    
    
    //RR인 경우
    /*     node           	   R
               R     =>  node     RR 
     * 		 RL RR          RL
     */
    private void leftRotation(Node node) {
        Node R = node.right;
        Node RL = node.right.left;
        
        R.parent = node.parent;
        
        node.parent = R;
        node.right = RL;
        
        R.left = node;
        
        RL.parent = node;
        
        node.height = Math.max(node.left.height, node.right.height)+1;
        R.height = Math.max(R.left.height, R.right.height)+1;
        
    }
    
    
    
    
    //root를 기준으로 왼쪽 서브트리와 오른쪽 서브트리의 차이를 구하는 함수
    //자식에 노드를 삽입한 노드가 밸런스가 맞는지 확인 맞으면 root까지 쭉 확인
    //맞지 않으면 ..? 뭘 반환해야할까.......
    private int getBalanceFactor(Node node) {
        if(node == null) return 0;
        else return node.left.height - node.right.height;
    }
    @Override
    public boolean contains(int value) {
        return containsNode(root, value);
    }
    private boolean containsNode(Node node, int value) {
        if(node == null) {
            return false;
        }
        
        if(node.value == value) {
            return true;
        }
        
        if(value < node.value) {
            return containsNode(node.left, value);
        }else {
            return containsNode(node.right, value);
        }
    }
    

    @Override
    public void delete(int value) {
        Node target = findNodeByValue(root, value);
        
        //삭제할 노드에 자식이 없는 경우
        //-> 부모에서 타겟과 연결된 쪽의 연결 끊기 
        if(target.left == null && target.right == null) {
            if(target == root) {
                root = null;
                return;
            }
            
            if(target.parent.left ==target) {
                target.parent.left = null;
            }else {
                target.parent.right = null;
            }
        }else if(target.left != null && target.right == null) {
            //왼쪽 자식만 있는 경우
            Node child = target.left;
            
            if(target == root) {
                //root를 삭제하므로 루트의 자식을 루트로 올려줌
                root = child;
                return;
            }
            
            child.parent = target.parent; //자식의 부모를 타겟에서 타겟의 부모로 변경
            //타겟의 부모에서 타겟과 연결된 자식이 왼쪽인지 오른쪽인지 찾아서 타켓의 자식과 연결
            if(target.parent.left == target) {
                target.parent.left = child;
            }else {
                target.parent.right = child;
            }
            
        }else if(target.left == null && target.right != null) {
            //오른쪽 자식만 있는 경우
            Node child = target.right;
            
            if(target == root) {
                //root를 삭제하므로 루트의 자식을 루트로 올려줌
                root = child;
                return;
            }
            
            child.parent = target.parent; //자식의 부모를 타겟에서 타겟의 부모로 변경
            //타겟의 부모에서 타겟과 연결된 자식이 왼쪽인지 오른쪽인지 찾아서 타켓의 자식과 연결
            if(target.parent.left == target) {
                 target.parent.left = child;
            }else {
                target.parent.right = child;
            }
            
        }else {
            //자식이 둘 다 있을 경우
            Node replaceNode = findReplaceNode(target.left);
            //왼쪽으로 왜가지.. ? 오른쪽의 가장 작은 값을 찾아야하는거 아닌가...
            
            if(target == root) {
                root = replaceNode;
            }else {
                if(target.parent.left == target) {
                    target.parent.left = replaceNode;
                }else {
                    target.parent.right = replaceNode;
                }
            }
            
            if(replaceNode.left != null) {
                if(replaceNode == target.left) {
                    replaceNode.parent.left = replaceNode.left;
                }else {
                    replaceNode.parent.right = replaceNode.left;
                    
                }
            }else {
                if(replaceNode == target.left) {
                    replaceNode.parent.left = null;
                }else {
                    replaceNode.parent.right = null;
                }
            }
            
            if(target == root) {
                replaceNode.parent = null;
                replaceNode.parent = target.parent;
            }
            target.right.parent = replaceNode;
            
            replaceNode.left = target.left;
            replaceNode.right = target.right;
        }
        size++;
        
        checkBalance(target.parent);
        
        
    }

    //삭제한 노드의 부모부터 균형을 확인해가면서 루트까지 올라간다.
    //깨졌으면 회전을 통해 맞추준다.
    void checkBalance(Node node) {
    	
    	node.height += Math.max(node.left.height, node.right.height); 
        
        int balance = getBalanceFactor(node);
        
        //왼쪽 - 오른쪽 => 결과가 양수 이면 왼쪽 서브트리의 높이가 더 크다 즉, 왼쪽으로 편향되어 있다.
        //그치만 높이차가 1인 경우까지는 괜찮으니 1보다 큰지 확인해주어야 한다.
        //LL인 경우 => Right Rotation을 진행해주어야 한다.
        if(balance > 1 && getBalanceFactor(node.left)>=0) { //자식의 값보다 더 작다면 (넣은노드(왼쪽)-왼쪽-노드) 일테니까
            rigthRotation(node);
        }
        
        //왼쪽 - 오른쪽 => 결과가 음수이면 오른쪽 서브트리의 높이가 더 크다 즉, 오른쪽으로 편향되어 있다.
        //RR인 경우 =>Left Rotation을 진행해주어야 한다.
        //leftRight => 
        if(balance < -1 && getBalanceFactor(node.right)<=0 ) {
            leftRotation(node);
        }
        
        
        // 자식의 값보다 더 크다면 (넣은노드(오른쪽)-왼쪽자식-노드)일테니까 
        //    /    
        //    \
        // LR인 경우  => 아래에 있던 LR이 위로 올라옴
        /*     node             node			 LR
             L        =>      LR       =>      L    node
         *    LR    (Left)  L    b 	  (Right)   a  b
         *   a  b            a  
         */
        if(balance > 1 && getBalanceFactor(node.left)<0) {
            leftRotation(node.left);
            rigthRotation(node);
        }
        
        // 자식의 값보다 더 작다면 (넣은노드(왼쪽)-오른쪽자식-노드)일테니까 
        //    \   
        //    /
        // RL인 경우 => 아래에 있던 LR이 위로 올라옴
        /*  node             node			      RL
	            R        =>      RL    =>    node    R 
	    *     RL      (Right)  	a  R  (Left)    a   b
	    *    a  b                 b  
	    */
        if(balance < -1 && getBalanceFactor(node.right)>0) {
        	rigthRotation(node.right);
        	leftRotation(node);
        }
		
        if(node == root) {
    		return;
    	}
        checkBalance(node.parent);
		
	}
	//
    private Node findReplaceNode(Node node) {
        while (node.right != null) {
            node = node.right;
        }
        return node;
    }
    //찾는 값과 같은 값을 가지는 노드를 반환
    private Node findNodeByValue(Node node, int value) {
        if(value == node.value) {
            return node;
        }
        
        if(value < node.value) {
            return findNodeByValue(node.left, value);
        }else {
            return findNodeByValue(node.right, value);
        }
    }

    
    
    @Override
    public int getSize() {
        return size;
    }

}

/*
 * 시간복잡도 => 최선의 경우 삽입, 삭제, 검색 모두 theta(1)
 * 평균과 최악의 경우는 모드 O(logN)
 */


