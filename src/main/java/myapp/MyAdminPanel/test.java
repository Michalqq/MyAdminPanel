package myapp.MyAdminPanel;

import java.util.*;

public class test {

    public static void main(String[] args) {

        int N = 2231;
        int[] arr = Integer.toString(N).chars().map(c -> c-'0').toArray();
        Arrays.sort(arr);
        StringBuilder strNum = new StringBuilder();
        for (int i = arr.length-1; i >= 0 ; i--) {
            strNum.append(arr[i]);
        }
        int finalInt = Integer.parseInt(strNum.toString());
        System.out.println(finalInt);

    }


    //___________
//
//    int N = 3;
//    int[] arr = new int[N];
//    int sum = 0;
//        for (int i = 0; i < N - 1; i++) {
//        if (N % 2 == 0) {
//            arr[i] = i + 1;
//        } else {
//            arr[i] = -i - 1;
//        }
//        sum += arr[i];
//    }
//    arr[N - 1] = -1 * sum;
//
//        System.out.println(Arrays.toString(arr));

    //___________

//    void {
//        int[] A = new int[]{3, 3};
//        List<Integer> list = new LinkedList<Integer>();
//        for (int currentInt : A) {
//            list.add(currentInt);
//        }
//        for (int i = 0; i < list.size(); i++) {
//            if (i > 0 && list.get(i - 1) == list.get(i)) {
//                list.remove(i);
//            }
//        }
//        int valley = 0;
//        int hill = 0;
//        if (list.size() > 2) {
//            for (int i = 0; i < list.size(); i++) {
//                if (i > 0 && i < list.size() - 1) {
//                    if (list.get(i + 1) < list.get(i) && list.get(i - 1) > list.get(i)) valley++;
//                    if (list.get(i + 1) > list.get(i) && list.get(i - 1) < list.get(i)) hill++;
//                } else if (i < list.size() - 1) {
//                    if (list.get(i + 1) < list.get(i)) valley++;
//                } else {
//                    if (list.get(i - 1) > list.get(i)) valley++;
//                }
//            }
//        } else return 1;
//        System.out.println(valley + hill);
//    }
}
