import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class AVLTreeTest {
    static AVLTree avlt;
    public static void main(String[] args) throws IOException, InterruptedException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        init();

        while (true) {
            System.out.println("======당신의 선택=====");
            System.out.println("(1) 원소 추가");
            System.out.println("(2) 출력(중위순회)");
            System.out.println("(3) 원소 삭제");
            System.out.println("(4) 종료.");
            System.out.print("선택하세요. >> ");
            int choice = Integer.parseInt(br.readLine());

            if (choice == 1) {
                System.out.println("원소를 추가합니다.");
                System.out.println("원소를 입력하세요. (정수 1개) >>");
                int num = Integer.parseInt(br.readLine());
                avlt.insertKey(num);
                System.out.printf("원소 %d을(를) 성공적으로 추가하였습니다.\n", num);
                System.out.printf("현재 Tree size : %d\n", avlt.size);
                continue;
            }
            if (choice == 2) {
                System.out.println("원소를 출력합니다.(중위순회)");
                avlt.inrOrderPrint();
                continue;
            }
            if (choice == 3) {
                System.out.print("삭제할 원소를 입력하세요. >> ");
                int num = Integer.parseInt(br.readLine());
                avlt.deleteKey(num);
                System.out.printf("원소 %d을(를) 성공적으로 삭제하였습니다.\n", num);
                System.out.printf("현재 Tree size : %d\n", avlt.size);
                continue;
            }
            if (choice == 4) {
                System.out.println("====AVL TREE TEST를 종료합니다.====");
                break;
            }
        }
    }

    static void init() throws InterruptedException, IOException {
        System.out.println("==================BST TEST==================");
        Thread.sleep(500);
        System.out.print("몇 개의 원소를 입력하시겠습니까? (정수 1개 입력) >> ");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int N = Integer.parseInt(br.readLine());
        int[] arr = new int[N];

        System.out.printf("========%d개의 원소를 입력합니다.========\n", N);
        Thread.sleep(500);

        for (int i = 0; i < N; i++) {
            System.out.printf("%d번째 원소를 입력하세요. >> ", i+1);
            int num = Integer.parseInt(br.readLine());

            arr[i] = num;
        }
        System.out.println("\n원소의 입력이 끝났습니다.\n");
        Thread.sleep(500);

        System.out.println("========입력된 원소 출력========");
        for (int i = 0; i < N; i++) {
            System.out.print(arr[i] + " ");
        }
        System.out.println("\n");

        System.out.println("AVL Tree를 생성합니다. 잠시만 기다려주세요.\n");
        Thread.sleep(1500);
        System.out.printf("1번째 원소 %d을(를) ROOT노드로 설정합니다.\n", arr[0]);
        avlt = new AVLTree();
        avlt.insertKey(arr[0]);
        Thread.sleep(1500);
        System.out.println("남은 원소를 AVL Tree에 삽입합니다.");
        for (int i = 1; i < N; i++) {
            avlt.insertKey(arr[i]);
        }

        System.out.println("AVL Tree가 생성되었습니다.\n");
        System.out.printf("현재 Tree size : %d\n", avlt.size);
    }


}
