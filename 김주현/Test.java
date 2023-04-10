import java.io.*;

/*
 *  Level : 0                  6
 *                        /         \
 *  Level : 1            3           20
 *                    /     \      /    \
 *  Level : 2       1        5   8       25
 *                   \            \
 *  Level : 3         2            10
 *                                  \
 *  Level : 4                       19
 *                                 /
 *  Level : 5                     15
 * */

public class Test {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringBuilder sb = new StringBuilder();

        AvlTree bst = new AvlTree();
        int[] addList = {3, 2, 1};
        for (int n : addList) {
            System.out.printf("%d 추가!\n", n);
            bst.insert(n);
            bst.printTree();
        }

        /*bst.delete(6);
        System.out.println("6을 지웠습니다!");
        bst.printTree();*/

        bw.write(sb.toString());
        bw.flush();
        bw.close();
        br.close();
    }
}