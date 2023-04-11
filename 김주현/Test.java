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

        AvlTree avl = new AvlTree();
        int[] addList = {5, 4, 3, 2, 1};
        for (int n : addList) {
            System.out.printf("%d 추가!\n", n);
            avl.insert(n);
            avl.printTree();
        }

        avl.showRoot();

        /*avl.delete(6);
        System.out.println("6을 지웠습니다!");
        avl.printTree();*/

        bw.write(sb.toString());
        bw.flush();
        bw.close();
        br.close();
    }
}