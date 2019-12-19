import java.util.*;

public class AVL<T extends Comparable<T>> implements SortedSet<T> {

    public static class Node<T> {
        T value;
        int height = 0;
        Node<T> left = null;
        Node<T> right = null;

        Node(T value) {
            this.value = value;
        }
    }

    public Node<T> root = null;
    private int size = 0;


    private int height(Node<T> node) {
        if (node == null) return -1;
        return node.height;
    }

    //Разница в высоте между левым и правым поддеревом
    private int balance(Node<T> node) {
        if (node == null) return 0;
        return height(node.left) - height(node.right);
    }

    //Корректировка высоты
    private int newHeight(Node<T> node) {
        int right = height(node.right);
        int left = height(node.left);
        return Math.max(right, left) + 1;
    }

    public Node<T> smallLeftRotation(Node node) {
        Node newNode = node.right;
        node.right = newNode.left;
        newNode.left = node;
        newNode.height = newHeight(newNode);
        node.height = newHeight(node);
        return newNode;
    }

    public Node<T> smallRightRotation(Node node) {
        Node newNode = node.left;
        node.left = newNode.right;
        newNode.right = node;
        newNode.height = newHeight(newNode);
        node.height = newHeight(node);
        return newNode;
    }

    public Node<T> bigLeftRotation(Node<T> node) {
        node.left = smallLeftRotation(node.left);
        return smallRightRotation(node);
    }

    public Node<T> bigRightRotation(Node<T> node) {
        node.right = smallRightRotation(node.right);
        return smallLeftRotation(node);
    }

    //Балансировка для добавлениея в дерево
    public Node<T> balancing(Node<T> node, T data) {
        int balance = balance(node);
        if (balance < -1) {
            if (data.compareTo(node.right.value) > 0) {
                return smallLeftRotation(node);
            } else {
                return bigRightRotation(node);
            }
        }
        if (balance > 1) {
            if (data.compareTo(node.left.value) > 0) {
                return smallRightRotation(node);
            } else {
                bigLeftRotation(node);
            }
        }
        return node;
    }

    //Балансировка для удаления из дерева
    public Node<T> removeBalancing(Node<T> node) {
        if (balance(node) < -1) {
            if (balance(node.right) > 0) {
                node.right = smallRightRotation(node.right);
            }
            return smallLeftRotation(node);
        }
        if (balance(node) > 1) {
            if (balance(node.left) < 0) {
                node.left = smallLeftRotation(node.left);
            }
            return smallRightRotation(node);
        }
        return node;
    }

    @Override
    public boolean add(T t) {
        if (contains(t)) return false;
        root = insert(root, t);
        size++;
        return true;
    }

    public Node<T> insert(Node<T> current, T t) {
        if (current == null) {
            return new Node<>(t);
        }
        if (t.compareTo(current.value) < 0) {
            current.left = insert(current.left, t);
        } else {
            current.right = insert(current.right, t);
        }
        current = balancing(current, t);
        current.height = newHeight(current);
        return current;
    }

    public boolean remove(Object o) {
        T t = (T) o;
        Node<T> parent = root;
        Node<T> delete = root;
        while (delete != null && delete.value != t) {
            parent = delete;
            if (t.compareTo(delete.value) < 0) {
                delete = delete.left;
            } else {
                delete = delete.right;
            }
        }
        if (delete == null) return false;

            //У удаляемого 0 или 1 потомок
        else if (delete.left == null || delete.right == null) {
            if (delete.left == null) changeNode(delete, parent, delete.right);
            else changeNode(delete, parent, delete.left);
        }

        //У удаляемого 2 потомка
        else {
            Node<T> next = delete.right;
            Node<T> nextParent = delete;

            while (next.left != null) {
                nextParent = next;
                next = next.left;
            }

            if (next != delete.right) {
                nextParent.left = next.right;
                next.right = delete.right;
            }
            next.left = delete.left;
            changeNode(delete, parent, next);
        }
        removeBalancing(delete);
        size--;
        return true;
    }

    public void changeNode(Node<T> del, Node<T> parent, Node<T> newChild) {
        if (del == root) {
            root = newChild;
        } else if (parent.left == del) {
            parent.left = newChild;
        } else {
            parent.right = newChild;
        }
    }

    @Override
    public Comparator<? super T> comparator() {
        return null;
    }

    @Override
    public T first() {
        if (root == null) throw new NoSuchElementException();
        Node<T> current = root;
        while (current.left != null) {
            current = current.left;
        }
        return current.value;
    }

    @Override
    public T last() {
        if (root == null) throw new NoSuchElementException();
        Node<T> current = root;
        while (current.right != null) {
            current = current.right;
        }
        return current.value;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(Object o) {
        @SuppressWarnings("unchecked")
        T t = (T) o;
        Node<T> closest = find(t);
        return closest != null && t.compareTo(closest.value) == 0;
    }

    public Node<T> find(T value) {
        if (root == null) return null;
        return find(root, value);
    }

    private Node<T> find(Node<T> start, T value) {
        int comparison = value.compareTo(start.value);
        if (comparison == 0) {
            return start;
        } else if (comparison < 0) {
            if (start.left == null) return start;
            return find(start.left, value);
        } else {
            if (start.right == null) return start;
            return find(start.right, value);
        }
    }

    @Override
    public Iterator<T> iterator() {
        return new AVLIterator();
    }

    public class AVLIterator implements Iterator<T> {

        private Stack<Node<T>> stack = new Stack<>();
        private Node<T> current = null;

        private AVLIterator() {
            Node<T> node = root;
            while (node != null) {
                stack.push(node);
                node = node.left;
            }
        }


        @Override
        public boolean hasNext() {
            return !stack.isEmpty();
        }

        @Override
        public T next() {
            current = nextNode();
            if (current == null) throw new NoSuchElementException();
            return current.value;
        }

        public Node<T> nextNode() {
            Node<T> node = stack.pop();
            current = node;
            if (node.right != null) {
                node = node.right;
                while (node != null) {
                    stack.push(node);
                    node = node.left;
                }
            }
            return current;
        }

        @Override
        public void remove() {
            AVL.this.remove(current.value);
            size--;
        }
    }


    @Override
    public SortedSet<T> subSet(T fromElement, T toElement) {
        return new SubTree<>(this, fromElement, toElement);
    }

    @Override
    public SortedSet<T> headSet(T toElement) {
        return new SubTree<>(this, null, toElement);
    }

    @Override
    public SortedSet<T> tailSet(T fromElement) {
        return new SubTree<>(this, fromElement, null);
    }

    @Override
    public Object[] toArray() {
        Object[] array = new Object[size()];
        Iterator<T> iterator = iterator();
        for (int i = 0; i < size(); i++) {
            if (iterator.hasNext()) {
                array[i] = iterator.next();
            }
        }
        return array;
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        Object[] array = toArray();
        if (a.length < size) {
            return (T1[]) Arrays.copyOf(array, size, a.getClass());
        }
        System.arraycopy(array, 0, a, 0, size);
        if (a.length > size) {
            a[size] = null;
        }
        return a;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object t : c) {
            if (!contains(t))
                return false;
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        boolean b = false;
        for (T t : c) {
            if (this.add(t)) b = true;
        }
        return b;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean b = false;
        Iterator<T> iterator = iterator();
        while (iterator.hasNext()) {
            if (!c.contains(iterator.next())) {
                iterator.remove();
                b = true;
            }
        }
        return b;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean b = false;
        for (Object t : c) {
            if (this.contains(t)) {
                this.remove(t);
                b = true;
            }
        }
        return b;
    }

    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    public static class SubTree<ST extends Comparable<ST>> extends AVL<ST> {
        AVL<ST> tree;
        ST fromElem, toElem;

        public SubTree(AVL<ST> tree, ST fromElem, ST toElem) {
            this.tree = tree;
            this.fromElem = fromElem;
            this.toElem = toElem;
        }

        private boolean inSubTree(ST elem) {
            if (fromElem != null && toElem != null) {
                return elem.compareTo(fromElem) >= 0 && elem.compareTo(toElem) < 0;
            } else if (fromElem == null) {
                return elem.compareTo(toElem) < 0;
            } else return elem.compareTo(fromElem) >= 0;
        }

        @Override
        public boolean add(ST t) {
            if (inSubTree(t)) {
                return tree.add(t);
            }
            throw new IllegalArgumentException();
        }

        @Override
        public boolean remove(Object o) {
            ST t = (ST) o;
            if (inSubTree(t)) {
                return tree.remove(t);
            }
            return false;
        }

        @SuppressWarnings("unchecked")
        @Override
        public boolean contains(Object o) {
            return (inSubTree((ST) o)) && tree.contains(o);
        }

        @Override
        public boolean containsAll(Collection<?> c) {
            for (Object t : c) {
                if (!contains(t))
                    return false;
            }
            return true;
        }

        @Override
        public SortedSet<ST> subSet(ST fromElement, ST toElement) {
            return new SubTree<>(tree, fromElement, toElement);
        }

        @Override
        public SortedSet<ST> headSet(ST toElement) {
            return new SubTree<>(tree, null, toElement);
        }

        @Override
        public SortedSet<ST> tailSet(ST fromElement) {
            return new SubTree<>(tree, fromElement, null);
        }

        private int subTreeSize(Node<ST> node) {
            int size = 0;
            if (node != null) {
                if (inSubTree(node.value)) {
                    size++;
                }
                size += subTreeSize(node.left);
                size += subTreeSize(node.right);
            }
            return size;
        }

        @Override
        public Iterator<ST> iterator() {
            return new SubTreeIterator();
        }

        @Override
        public int size() {
            return subTreeSize(tree.root);
        }

        public class SubTreeIterator implements Iterator<ST> {

            Iterator<ST> iterator = SubTree.this.tree.iterator();
            ST next = null;

            SubTreeIterator() {
                while (iterator.hasNext()) {
                    ST node = iterator.next();
                    if (inSubTree(node)) {
                        next = node;
                        break;
                    }
                }
            }

            @Override
            public boolean hasNext() {
                return next != null;
            }

            @Override
            public ST next() {
                if (next == null) throw new NoSuchElementException();
                ST node = next;
                if (iterator.hasNext()) {
                    next = iterator.next();
                } else {
                    next = null;
                }
                if (!inSubTree(next)) next = null;
                return node;
            }

            @Override
            public void remove() {
                iterator.remove();
            }
        }

        @Override
        public ST first() {
            for (ST elem : tree) {
                if (inSubTree(elem)) return elem;
            }
            throw new NoSuchElementException();
        }

        @Override
        public ST last() {
            ST last = null;
            for (ST elem : tree) {
                if (inSubTree(elem)) last = elem;
                if (toElem != null && elem.compareTo(toElem) >= 0) break;
            }
            if (last == null) throw new NoSuchElementException();
            return last;
        }

        @Override
        public void clear() {
            tree.clear();
        }

        @Override
        public boolean isEmpty() {
            return size() == 0;
        }

        @Override
        public boolean removeAll(Collection<?> c) {
            boolean b = false;
            for (Object t : c) {
                if (tree.contains(t)) {
                    tree.remove(t);
                    b = true;
                }
            }
            return b;
        }

        @Override
        public boolean addAll(Collection<? extends ST> c) {
            boolean b = false;
            for (ST t : c) {
                if (tree.add(t)) b = true;
            }
            return b;
        }

        @Override
        public boolean retainAll(Collection<?> c) {
            return tree.retainAll(c);
        }

        @Override
        public Object[] toArray() {
            Object[] array = new Object[size()];
            Iterator<ST> iterator = iterator();
            for (int i = 0; i < size(); i++) {
                if (iterator.hasNext()) {
                    array[i] = iterator.next();
                }
            }
            return array;
        }

        @Override
        public <T1> T1[] toArray(T1[] a) {
            Object[] array = toArray();
            if (a.length < size()) {
                return (T1[]) Arrays.copyOf(array, size(), a.getClass());
            }
            System.arraycopy(array, 0, a, 0, size());
            if (a.length > size()) {
                a[size()] = null;
            }
            return a;
        }
    }
}
