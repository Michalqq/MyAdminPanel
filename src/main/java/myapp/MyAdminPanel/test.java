package myapp.MyAdminPanel;

import java.util.*;

public class test {

    public static void main(String[] args) {

        System.out.println(silnia(0));
    }

    public static int silnia(int liczba) {
        return liczba <= 1 ? 1 : silnia(liczba - 1) * liczba;
    }

}
