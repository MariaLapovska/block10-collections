import com.epam.collections.*;

/**
 * Main class
 */
public class Main {
    public static void main(String[] args) {
        CustomTreeSet<Integer> treeSet = new CustomTreeSet<>();
        treeSet.add(25);
        treeSet.add(26);
        treeSet.add(33);
        treeSet.add(28);
        treeSet.add(76);
        treeSet.add(26);
        treeSet.add(85);
        treeSet.add(10);
        treeSet.add(38);
        treeSet.add(19);
        System.out.println(treeSet.toString());

        CustomLinkedList<Integer> list = new CustomLinkedList<>();
        list.add(25);
        list.add(26);
        list.add(27);
        list.add(28);
        list.add(29);
        list.add(33);
        list.add(19);
        treeSet.retainAll(list);
        System.out.println(treeSet.toString());
    }
}
