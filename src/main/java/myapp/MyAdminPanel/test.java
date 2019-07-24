package myapp.MyAdminPanel;

import java.util.HashMap;
import java.util.Map;

public class test {

    public static void main(String[] args) {
        Map<Integer, String> map = new HashMap<>();
        map.put(1, "addco");
        map.put(2, "defi");
        map.put(10, "greddy");

        System.out.println(map.get(2));
    }
}
