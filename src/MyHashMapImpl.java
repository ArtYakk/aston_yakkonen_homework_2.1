import java.util.Objects;

public class MyHashMapImpl<K, V> implements MyHashMap<K, V>{

    @Override
    public V get(Object key) {
        MyHashMapImpl.Node<K,V> e;
        return (e = getNode(key)) == null ? null : e.value;
    }

    @Override
    public V put(K key, V value) {
        return putVal(hash(key), key, value);
    }

    @Override
    public V remove(Object key) {
        MyHashMapImpl.Node<K,V> e;
        return (e = removeNode(hash(key), key)) == null ?
                null : e.value;
    }

    @Override
    public void print(){
        for (Node<K, V> node : table) {
            Node<K, V> el = node;

            if (el == null) {
                continue;
            }

            if (el.next == null) {
                System.out.println(el);
                continue;
            }

            do {
                System.out.println(el);
                el = el.next;
            } while (el != null);
        }
    }

    private static class Node<K,V>{
        final int hash;
        final K key;
        V value;
        MyHashMapImpl.Node<K,V> next;

        Node(int hash, K key, V value, MyHashMapImpl.Node<K,V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }

        public final String toString() { return key + "=" + value; }

        public final int hashCode() {
            return Objects.hashCode(key) ^ Objects.hashCode(value);
        }
    }

    private int hash(Object key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }

    private MyHashMapImpl.Node<K,V>[] table;

    private int size;

    private int threshold;

    private int capacity;

    private static final float LOAD_FACTOR = 0.75f;

    public MyHashMapImpl() {
        this.capacity = 16;
        this.threshold = calculateThreshold(capacity);
        this.table = (MyHashMapImpl.Node<K,V>[])new MyHashMapImpl.Node[capacity];
    }

    private MyHashMapImpl.Node<K,V> getNode(Object key) {
        MyHashMapImpl.Node<K,V>[] tab;
        MyHashMapImpl.Node<K,V> first, e;
        int n, hash;
        K k;
        if ((tab = table) != null && (n = tab.length) > 0 &&
                (first = tab[(n - 1) & (hash = hash(key))]) != null) {
            if (first.hash == hash &&
                    ((k = first.key) == key || (key != null && key.equals(k))))
                return first;
            if ((e = first.next) != null) {
                do {
                    if (e.hash == hash &&
                            ((k = e.key) == key || (key != null && key.equals(k))))
                        return e;
                } while ((e = e.next) != null);
            }
        }
        return null;
    }

    private V putVal(int hash, K key, V value) {
        MyHashMapImpl.Node<K,V>[] tab;
        MyHashMapImpl.Node<K,V> p;
        int n, i;
        tab = table;
        n = tab.length;

        if ((p = tab[i = (n - 1) & hash]) == null)
            tab[i] = newNode(hash, key, value, null);
        else {
            MyHashMapImpl.Node<K,V> e;
            K k;
            if (p.hash == hash &&
                    ((k = p.key) == key || (key != null && key.equals(k))))
                e = p;
            else {
                for ( ; ; ) {
                    if ((e = p.next) == null) {
                        p.next = newNode(hash, key, value, null);
                        break;
                    }
                    if (e.hash == hash &&
                            ((k = e.key) == key || (key != null && key.equals(k))))
                        break;
                    p = e;
                }
            }
            if (e != null) {
                V oldValue = e.value;
                if (oldValue == null)
                    e.value = value;
                return oldValue;
            }
        }
        if (++size > threshold)
            resize();
        return null;
    }

    private MyHashMapImpl.Node<K,V>[] resize() {
        MyHashMapImpl.Node<K,V>[] oldTab = table;
        int oldCap = oldTab.length;
        int newCap, newThr = 0;

        newCap = oldCap * 2;
        newThr = calculateThreshold(newCap);
        threshold = newThr;

        MyHashMapImpl.Node<K,V>[] newTab = (MyHashMapImpl.Node<K,V>[]) new MyHashMapImpl.Node[newCap];

        for (int i = 0; i < oldCap; i++) {
            MyHashMapImpl.Node<K,V> e;
            if ((e = oldTab[i]) != null) {
                oldTab[i] = null;
                if (e.next == null)
                    newTab[e.hash & (newCap - 1)] = e;
                    else {
                        MyHashMapImpl.Node<K,V> loHead = null, loTail = null;
                        MyHashMapImpl.Node<K,V> hiHead = null, hiTail = null;
                        MyHashMapImpl.Node<K,V> next;
                        do {
                            next = e.next;
                            if ((e.hash & oldCap) == 0) {
                                if (loTail == null)
                                    loHead = e;
                                else
                                    loTail.next = e;
                                loTail = e;
                            }
                            else {
                                if (hiTail == null)
                                    hiHead = e;
                                else
                                    hiTail.next = e;
                                hiTail = e;
                            }
                        } while ((e = next) != null);
                        if (loTail != null) {
                            loTail.next = null;
                            newTab[i] = loHead;
                        }
                        if (hiTail != null) {
                            hiTail.next = null;
                            newTab[i + oldCap] = hiHead;
                        }
                    }
                }
            }

        table = newTab;
        return newTab;
    }

    MyHashMapImpl.Node<K,V> newNode(int hash, K key, V value, MyHashMapImpl.Node<K,V> next) {
        return new MyHashMapImpl.Node<>(hash, key, value, next);
    }

    private MyHashMapImpl.Node<K,V> removeNode(int hash, Object key) {
        MyHashMapImpl.Node<K,V>[] tab;
        MyHashMapImpl.Node<K,V> p;
        int n, index;

        tab = table;
        n = tab.length;

        if ((p = tab[index = (n - 1) & hash]) != null) {
            MyHashMapImpl.Node<K,V> node = null, e;
            K k;
            V v;
            if (p.hash == hash &&
                    ((k = p.key) == key || (key != null && key.equals(k))))
                node = p;
            else if ((e = p.next) != null) {
                do {
                    if (e.hash == hash &&
                            ((k = e.key) == key ||
                                    (key != null && key.equals(k)))) {
                        node = e;
                        break;
                    }
                    p = e;
                } while ((e = e.next) != null);
            }
            if (node != null) {
                if (node == p)
                    tab[index] = node.next;
                else
                    p.next = node.next;
                --size;
                return node;
            }
        }
        return null;
    }

    private int calculateThreshold(int capacity){
        return (int) (capacity * LOAD_FACTOR);
    }
}