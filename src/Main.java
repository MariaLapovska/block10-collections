/**
 * Created by Asus on 09.06.2016.
 */

import com.epam.collections.*;

public class Main {
    public static void main(String[] args) {
        CustomTreeSet<Integer> lol = new CustomTreeSet<>();
        lol.add(25);
        lol.add(26);
        lol.add(33);
        lol.add(28);
        lol.add(76);
        lol.add(26);
        lol.add(85);
        lol.add(10);
        lol.add(38);
        lol.add(19);
        System.out.println(lol.toString());

        CustomLinkedList<Integer> omg = new CustomLinkedList<>();
        omg.add(25);
        omg.add(26);
        omg.add(27);
        omg.add(28);
        omg.add(29);
        omg.add(33);
        omg.add(19);
        lol.retainAll(omg);
        System.out.println(lol.toString());

        //Object[] omg = lol.toArray();
        // for (Object o : omg)
            // System.out.print(o + " ");
    }
}
