import org.junit.jupiter.api.Test;
import java.util.SortedSet;
import static org.junit.jupiter.api.Assertions.*;

class AVLTest {
    AVL<Integer> tree = new AVL<>();

    public void Tree(){
        for(int i = 1; i < 10; i++){
            tree.add(i);
        }
    }

    @Test
    void add() {
        Tree();
        for(int i = 1; i < 10; i++){
            assertTrue(tree.contains(i));
        }
        assertFalse(tree.contains(10));
        assertEquals(4, tree.root.value);
        assertEquals(9, tree.find(8).right.value);
        assertEquals(7, tree.find(8).left.value);
    }

    @Test
    void remove() {
        Tree();
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
        assertEquals(0, tree.size());
        Tree();
        for(int i=1; i<10; i++){
            tree.add(i);
        }
        assertEquals(9, tree.size());
    }

    @Test
    void isEmpty() {
        assertTrue(tree.isEmpty());
        tree.add(7);
        assertFalse(tree.isEmpty());
    }

    @Test
    void contains() {
        assertFalse(tree.contains(5));
        Tree();
        assertTrue(tree.contains(5));
    }

    @Test
    void subSet() {
        Tree();
        SortedSet set = tree.subSet(-1, 15);
        for(int i=1; i<10; i++){
            assertTrue(set.contains(i));
        }
        set = tree.subSet(0,0);
        for(int i=1; i<10; i++){
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
        assertThrows(IllegalArgumentException.class, () -> {
            finalSet.add(0);
        });
        assertFalse(set.remove(200));
    }

    @Test
    void headSet() {
        Tree();
        SortedSet set = tree.headSet(15);
        for(int i=1; i<10; i++){
            assertTrue(set.contains(i));
        }

        set = tree.headSet(0);
        for(int i=1; i<10; i++){
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
        assertThrows(IllegalArgumentException.class, () -> {
            finalSet.add(200);
        });
    }

    @Test
    void tailSet() {
        Tree();
        SortedSet set = tree.tailSet(0);
        for(int i=1; i<10; i++){
            assertTrue(set.contains(i));
        }

        set = tree.tailSet(20);
        for(int i=1; i<10; i++){
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
        assertThrows(IllegalArgumentException.class, () -> {
                finalSet.add(-200);
        });

    }

    @Test
    void containsAll() {
        Tree();
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
        Tree();
        AVL<Integer> tree1 = new AVL<>();
        for(int i = 10; i < 20; i++){
            tree1.add(i);
        }
        tree.addAll(tree1);
        assertTrue(tree.containsAll(tree1));
    }

    @Test
    void retainAll() {
        Tree();
        AVL<Integer> tree1 = new AVL<>();
        tree1.add(1);
        tree1.add(3);
        tree1.add(5);
        tree1.add(7);
        tree1.add(9);
        tree.retainAll(tree1);
        for(int i = 1; i < 10; i += 2){
            assertTrue(tree.contains(i));
        }
        for(int i = 2; i < 10; i += 2){
            assertFalse(tree.contains(i));
        }
    }

    @Test
    void removeAll() {
        Tree();
        AVL<Integer> tree1 = new AVL<>();
        for(int i = 1; i < 10; i++){
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
        Tree();
        assertEquals(1, tree.first());
        tree.remove(1);
        assertEquals(2, tree.first());
        tree.add(0);
        assertEquals(0, tree.first());
    }

    @Test
    void last() {
        Tree();
        assertEquals(9, tree.last());
        tree.remove(9);
        assertEquals(8, tree.last());
        tree.add(100);
        assertEquals(100, tree.last());
    }

    @Test
    void clear() {
        Tree();
        tree.clear();
        assertTrue(tree.isEmpty());
    }
}