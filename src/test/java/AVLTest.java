import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class AVLTest {
    AVL<Integer> tree = new AVL<>();
    AVL<Integer> emptyTree = new AVL<>();

    @BeforeEach
    public void Tree() {
        for (int i = 1; i < 10; i++) {
            tree.add(i);
        }
    }

    @Test
    void add() {
        for (int i = 1; i < 10; i++) {
            assertTrue(tree.contains(i));
        }
        assertFalse(tree.contains(10));
        assertEquals(4, tree.root.value);
        assertEquals(9, tree.find(8).right.value);
        assertEquals(7, tree.find(8).left.value);
    }

    @Test
    void remove() {
        tree.remove(4);
        assertFalse(tree.contains(4));
        assertEquals(5, tree.root.value);
        assertEquals(6, tree.find(5).right.value);
        assertEquals(2, tree.find(5).left.value);
        assertEquals(3, tree.find(2).right.value);
        assertEquals(1, tree.find(2).left.value);
    }

    @Test
    void size() {
        assertEquals(0, emptyTree.size());
        for (int i = 1; i < 10; i++) {
            tree.add(i);
        }
        assertEquals(9, tree.size());
    }

    @Test
    void isEmpty() {
        assertTrue(emptyTree.isEmpty());
        emptyTree.add(7);
        assertFalse(emptyTree.isEmpty());
    }

    @Test
    void contains() {
        assertFalse(emptyTree.contains(5));
        assertTrue(tree.contains(5));
    }

    @Test
    void subSet() {
        SortedSet set = tree.subSet(-1, 15);
        for (int i = 1; i < 10; i++) {
            assertTrue(set.contains(i));
        }
        set = tree.subSet(0, 0);
        for (int i = 1; i < 10; i++) {
            assertFalse(set.contains(i));
        }
        set = tree.subSet(2, 7);
        assertTrue(set.contains(2));
        assertTrue(set.contains(3));
        assertTrue(set.contains(4));
        assertTrue(set.contains(5));
        assertTrue(set.contains(6));
        assertFalse(set.contains(7));

        set = tree.subSet(2, 100);
        tree.add(13);
        assertTrue(set.contains(13));
        set.add(25);
        assertTrue(set.contains(25));
        assertTrue(tree.contains(25));
        tree.remove(5);
        assertFalse(set.contains(5));
        set.remove(7);
        assertFalse(tree.contains(7));
        assertFalse(set.contains(7));

        SortedSet finalSet = set;
        assertThrows(IllegalArgumentException.class, () -> finalSet.add(0));
        assertFalse(set.remove(200));
    }

    @Test
    void headSet() {
        SortedSet set = tree.headSet(15);
        for (int i = 1; i < 10; i++) {
            assertTrue(set.contains(i));
        }

        set = tree.headSet(0);
        for (int i = 1; i < 10; i++) {
            assertFalse(set.contains(i));
        }

        set = tree.headSet(5);
        assertTrue(set.contains(1));
        assertTrue(set.contains(2));
        assertTrue(set.contains(3));
        assertTrue(set.contains(4));
        assertFalse(set.contains(5));

        set = tree.headSet(100);
        tree.add(11);
        assertTrue(set.contains(11));
        set.add(50);
        assertTrue(tree.contains(50));
        tree.remove(11);
        assertFalse(set.contains(11));

        SortedSet finalSet = set;
        assertThrows(IllegalArgumentException.class, () -> finalSet.add(200));
    }

    @Test
    void tailSet() {
        SortedSet set = tree.tailSet(0);
        for (int i = 1; i < 10; i++) {
            assertTrue(set.contains(i));
        }

        set = tree.tailSet(20);
        for (int i = 1; i < 10; i++) {
            assertFalse(set.contains(i));
        }

        set = tree.tailSet(5);
        assertTrue(set.contains(5));
        assertTrue(set.contains(6));
        assertTrue(set.contains(7));
        assertTrue(set.contains(8));
        assertTrue(set.contains(9));
        assertFalse(set.contains(4));

        set = tree.tailSet(-1);
        tree.add(20);
        assertTrue(set.contains(20));
        set.add(15);
        assertTrue(tree.contains(15));
        set.remove(20);
        assertFalse(tree.contains(20));

        SortedSet finalSet = set;
        assertThrows(IllegalArgumentException.class, () -> finalSet.add(-200));
    }

    @Test
    void containsAll() {
        AVL<Integer> tree1 = new AVL<>();
        tree1.add(2);
        tree1.add(5);
        tree1.add(9);
        assertTrue(tree.containsAll(tree1));
        tree1.add(15);
        tree1.add(20);
        assertFalse(tree.containsAll(tree1));
    }

    @Test
    void addAll() {
        AVL<Integer> tree1 = new AVL<>();
        AVL<Integer> tree2 = new AVL<>();
        for (int i = 10; i < 20; i++) {
            tree1.add(i);
        }
        tree2.addAll(tree1);
        assertTrue(tree2.containsAll(tree1));
    }

    @Test
    void retainAll() {
        AVL<Integer> tree1 = new AVL<>();
        tree1.add(1);
        tree1.add(3);
        tree1.add(5);
        tree1.add(7);
        tree1.add(9);
        tree.retainAll(tree1);
        for (int i = 1; i < 10; i += 2) {
            assertTrue(tree.contains(i));
        }
        for (int i = 2; i < 10; i += 2) {
            assertFalse(tree.contains(i));
        }
    }

    @Test
    void removeAll() {
        AVL<Integer> tree1 = new AVL<>();
        for (int i = 1; i < 10; i++) {
            tree1.add(i);
        }
        tree.removeAll(tree1);
        assertTrue(tree.isEmpty());
        Tree();
        tree1 = new AVL<>();
        tree1.add(2);
        tree1.add(5);
        tree1.add(20);
        tree.removeAll(tree1);
        assertFalse(tree.contains(2));
        assertFalse(tree.contains(5));
        assertFalse(tree.contains(20));
    }

    @Test
    void first() {
        assertEquals(1, tree.first());
        tree.remove(1);
        assertEquals(2, tree.first());
        tree.add(0);
        assertEquals(0, tree.first());
    }

    @Test
    void last() {
        assertEquals(9, tree.last());
        tree.remove(9);
        assertEquals(8, tree.last());
        tree.add(100);
        assertEquals(100, tree.last());
    }

    @Test
    void clear() {
        tree.clear();
        assertTrue(tree.isEmpty());
    }

    @Test
    void subTree() {
        SortedSet empty = tree.subSet(15, 15);
        assertTrue(empty.isEmpty());

        SortedSet set = tree.headSet(15);
        SortedSet subSet = set.headSet(25);
        SortedSet finalSubSet = subSet;
        assertThrows(IllegalArgumentException.class, () -> finalSubSet.add(20));

        subSet = set.headSet(5);
        assertEquals(4, subSet.size());
        subSet = set.subSet(2, 4);
        assertEquals(2, subSet.size());
        assertTrue(subSet.contains(2));
        assertTrue(subSet.contains(3));
        assertFalse(subSet.contains(4));
        subSet = set.tailSet(5);
        assertEquals(5, subSet.size());

        tree.add(11);
        assertEquals(6, subSet.size());
        set.add(12);
        set.add(14);
        assertEquals(8, subSet.size());
    }

    @Test
    void changeBordersForSubSet() {
        SortedSet set = tree.subSet(4, 8);
        SortedSet subset = set.subSet(3, 9);
        assertEquals(set.size(), subset.size());
        for (int i = 4; i < 8; i++) {
            assertTrue(subset.contains(i));
        }
        assertFalse(subset.contains(3));
        assertFalse(subset.contains(8));

        subset = set.headSet(9);
        assertEquals(set.size(), subset.size());
        for (int i = 4; i < 8; i++) {
            assertTrue(subset.contains(i));
        }
        assertFalse(subset.contains(2));
        assertFalse(subset.contains(8));

        subset = set.tailSet(2);
        assertEquals(set.size(), subset.size());
        subset = set.subSet(8,11);
        assertEquals(0, subset.size());
    }

    @Test
    void changeBordersForHeadSet() {
        SortedSet set = tree.headSet(5);
        SortedSet subset = set.subSet(2, 8);
        assertEquals(3, subset.size());
        for (int i = 2; i < 5; i++) {
            assertTrue(subset.contains(i));
        }
        assertFalse(subset.contains(5));
        assertFalse(subset.contains(6));
        assertFalse(subset.contains(7));

        subset = set.headSet(10);
        assertEquals(set.size(), subset.size());
        subset = set.tailSet(1);
        assertEquals(4, subset.size());
        subset = set.subSet(8, 10);
        assertEquals(0, subset.size());
    }

    @Test
    void changeBordersForTailSet() {
        SortedSet set = tree.tailSet(3);
        SortedSet subset = set.subSet(2, 6);
        assertEquals(3, subset.size());
        for (int i = 3; i < 6; i++) {
            assertTrue(subset.contains(i));
        }
        assertFalse(subset.contains(2));
        assertFalse(subset.contains(6));

        subset = set.headSet(8);
        assertEquals(5, subset.size());
        subset = set.tailSet(2);
        assertEquals(set.size(), subset.size());
        subset = set.subSet(1, 2);
        assertEquals(0, subset.size());
    }

    @Test
    void subIterator() {
        SortedSet set = tree.headSet(15);
        Iterator<Integer> it = set.iterator();
        for (int i = 1; i < set.size(); i++) {
            assertEquals(i, it.next());
        }

        AVL<Integer> tree2 = new AVL<>();
        tree2.add(5);
        tree2.add(3);
        tree2.add(8);
        tree2.add(2);
        tree2.add(7);
        tree2.add(4);
        tree2.add(9);
        tree2.add(6);

        SortedSet set2 = tree2.subSet(3, 9);
        Iterator<Integer> it2 = set2.iterator();
        for (int i = 3; i < 9; i++) {
            assertEquals(i, it2.next());
        }

        AVL<Integer> tree3 = new AVL<>();
        tree3.add(25);
        tree3.add(15);
        tree3.add(45);
        tree3.add(55);
        tree3.add(35);
        tree3.add(5);

        SortedSet<Integer> set3 = tree3.headSet(30);
        Iterator<Integer> it3 = set3.iterator();
        int check = 5;
        while (it3.hasNext()) {
            assertEquals(check, it3.next());
            check += 10;
        }
    }

    @Test
    void firstAndLast() {
        SortedSet<Integer> set = tree.subSet(3, 7);
        assertEquals(3, set.first());
        assertEquals(6, set.last());
    }

    @Test
    void allSub() {
        SortedSet<Integer> set = tree.subSet(1, 20);
        List<Integer> list = new ArrayList<>();
        list.add(12);
        list.add(15);
        list.add(14);
        set.addAll(list);
        assertTrue(set.containsAll(list));

        set.removeAll(list);
        assertFalse(set.containsAll(list));

        set = tree.subSet(2, 9);
        List<Integer> list2 = new ArrayList<>();
        list2.add(2);
        list2.add(4);
        list2.add(6);
        list2.add(8);
        set.retainAll(list2);
        for (int i = 2; i < 10; i += 2) {
            assertTrue(set.contains(i));
        }
        for (int i = 1; i < 10; i += 2) {
            assertFalse(set.contains(i));
        }
    }

    @Test
    void toArray() {
        Object treeArray[] = tree.toArray();
        for(Object elem : treeArray){
            tree.contains(elem);
        }

        SortedSet<Integer> set = tree.subSet(1, 5);
        Object a[] = set.toArray();
        for(Object elem : a){
            assertTrue(set.contains(elem));
        }
    }
}