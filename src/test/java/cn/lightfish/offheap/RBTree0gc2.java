package cn.lightfish.offheap;


import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;

/**
 * cjw with karakapi
 */
public class RBTree0gc2 {
    /*dystruct offset false*/
    public final static long NODE = 0L;
    public final static long PARENT = 25L;
    public final static long COLOR = 0L;
    public final static long LEFT = 9L;
    public final static long RIGHT = 17L;
    public final static long KEY = 1L;
    final static byte RED = 0;
    final static byte BLACK = 1;
    final static long NULL = 0L;
    static int[] array = {10, 40, 30, 60, 90, 70, 20, 50, 80, 90};
    ////////////////////////////////////////////////////////////////////////////////////
    static Unsafe memory;

    static {
        try {
            Field f = Unsafe.class.getDeclaredField("theUnsafe");
            f.setAccessible(true);
            Unsafe unsafe = (Unsafe) f.get(null);
            memory = unsafe;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        long root = create_rbtree();
        for (int i : array) {
            insert_rbtree(root, i);
            print_rbtree(root);
        }
        search($address(root, NODE), 50);
        $free(0);

    }

    static void rbtree_left_rotate(long root, long x) {
        //long y = x->right;
        long y = $address(/*RBTreeNode*/x, RIGHT);

        //x->right = y->left;
        //if (y->left != NULL)
        //    y->left->parent = x;
        $address(/*RBTreeNode*/x, RIGHT, $address(/*RBTreeNode*/y, LEFT));
        if ($address(/*RBTreeNode*/y, LEFT) != NULL) {
            $address(/*RBTreeNode*/$address(/*RBTreeNode*/y, LEFT), PARENT, x);
        }

        //    y->parent = x->parent;
        $address(/*RBTreeNode*/y, PARENT, $address(/*RBTreeNode*/x, PARENT));
        if ($address(/*RBTreeNode*/x, PARENT) == NULL) {
            //tree = y;
            $address(/*rb_root*/root, NODE, y);
        } else {
            /*
            if (x->parent->left == x)
            x->parent->left = y;
             */
            if ($address(/*RBTreeNode*/$address(/*RBTreeNode*/x, PARENT), LEFT) == x) {
                $address(/*RBTreeNode*/$address(/*RBTreeNode*/x, PARENT), LEFT, y);
            } else {
                /*
                 x->parent->right = y;
                 */
                $address(/*RBTreeNode*/$address(/*RBTreeNode*/x, PARENT), RIGHT, y);
            }
        }
        // y->left = x;
        $address(/*RBTreeNode*/y, LEFT, x);
        // x->parent = y;
        $address(/*RBTreeNode*/x, PARENT, y);
    }

    static void rbtree_right_rotate(long root, long y) {
        //long y = x->right;
        long x = $address(/*RBTreeNode*/y, LEFT);
        /*
            y->left = x->right;
            if (x->right != NULL)
                    x->right->parent = y;
         */
        $address(/*RBTreeNode*/y, LEFT, $address(/*RBTreeNode*/x, RIGHT));
        if ($address(/*RBTreeNode*/x, RIGHT) != NULL) {
            $address(/*RBTreeNode*/$address(/*RBTreeNode*/x, RIGHT), PARENT, y);
        }

        //  y->parent = x->parent;
        $address(/*RBTreeNode*/x, PARENT, $address(/*RBTreeNode*/y, PARENT));
        // if (y->parent == NULL)
        if ($address(/*RBTreeNode*/y, PARENT) == NULL) {
            //root->node = x;
            $address(/*rb_root*/root, NODE, x);
        } else {
            /*
            if (y == y->parent->right)
            y->parent->right = x;
             */
            if ($address(/*RBTreeNode*/$address(/*RBTreeNode*/y, PARENT), RIGHT) == y) {
                $address(/*RBTreeNode*/$address(/*RBTreeNode*/y, PARENT), RIGHT, x);
            } else {
                /*
                 y->parent->left = x;
                 */
                $address(/*RBTreeNode*/$address(/*RBTreeNode*/y, PARENT), LEFT, x);
            }
        }

        // x->right = y;
        $address(/*RBTreeNode*/x, RIGHT, y);
        // y->parent = x;
        $address(/*RBTreeNode*/y, PARENT, x);
    }

    static long create_rbtree() {
        long root = $malloc(8);
        $address(/*rb_root*/root, NODE, NULL);

        return root;
    }

    static long rb_parent(long r) {
        return $address(/*RBTreeNode*/r, PARENT);
    }

    static byte rb_color(long r) {
        return $byte(/*RBTreeNode*/r, COLOR);
    }

    static boolean rb_is_red(long r) {
        return ((byte) $byte(/*RBTreeNode*/r, COLOR) == RED);
    }

    static boolean rb_is_black(long r) {
        return ((byte) $byte(/*RBTreeNode*/r, COLOR) == BLACK);
    }

    static void rb_set_black(long r) {
        $byte(/*RBTreeNode*/r, COLOR, BLACK);
    }

    static void rb_set_red(long r) {
        $byte(/*RBTreeNode*/r, COLOR, RED);
    }

    static void rb_set_parent(long r, long p) {
        $address(/*RBTreeNode*/r, PARENT, p);
    }

    static void rb_set_color(long r, byte c) {
        $byte(/*RBTreeNode*/r, COLOR, c);
    }

    static long search(long x, long key) {
        if (x == NULL || $address(/*RBTreeNode*/x, KEY) == key) {
            return x;
        }
        if (key < $address(/*RBTreeNode*/x, KEY)) {
            return search($address(/*RBTreeNode*/x, LEFT), key);
        } else {
            return search($address(/*RBTreeNode*/x, RIGHT), key);
        }
    }

    static long iterative_search(long x, long key) {
        while ((x != NULL) && ($address(/*RBTreeNode*/x, KEY) != key)) {
            if (key < $address(/*RBTreeNode*/x, KEY))
                x = $address(/*RBTreeNode*/x, LEFT);
            else
                x = $address(/*RBTreeNode*/x, RIGHT);
        }

        return x;
    }

    static int iterative_rbtree_search(long root, long key) {
        if (root != NULL)
            return iterative_search($address(/*rb_root*/root, NODE), key) > 0 ? 0 : -1;

        return 0;
    }

    static void rbtree_insert_fixup(long root, long node) {
        long parent;
        long gparent;

        while ((parent = rb_parent(node)) != NULL && rb_is_red(parent)) {
            gparent = rb_parent(parent);

            if (parent == $address(/*RBTreeNode*/gparent, LEFT)) {
                {
                    long uncle = $address(/*RBTreeNode*/gparent, RIGHT);
                    if (uncle != NULL && rb_is_red(uncle)) {
                        rb_set_black(uncle);
                        rb_set_black(parent);
                        rb_set_red(gparent);
                        node = gparent;
                        continue;
                    }
                }

                if ($address(/*RBTreeNode*/parent, RIGHT) == node) {
                    long tmp;
                    rbtree_left_rotate(root, parent);
                    tmp = parent;
                    parent = node;
                    node = tmp;
                }

                rb_set_black(parent);
                rb_set_red(gparent);
                rbtree_right_rotate(root, gparent);
            } else {
                {
                    long uncle = $address(/*RBTreeNode*/gparent, LEFT);
                    if (uncle != NULL && rb_is_red(uncle)) {
                        rb_set_black(uncle);
                        rb_set_black(parent);
                        rb_set_red(gparent);
                        node = gparent;
                        continue;
                    }
                }

                if ($address(/*RBTreeNode*/parent, LEFT) == node) {
                    long tmp;
                    rbtree_right_rotate(root, parent);
                    tmp = parent;
                    parent = node;
                    node = tmp;
                }

                rb_set_black(parent);
                rb_set_red(gparent);
                rbtree_left_rotate(root, gparent);
            }
        }

        rb_set_black($address(/*rb_root*/root, NODE));
    }

    static void rbtree_insert(long root, long node) {
        /*
            Node *y = NULL;
            Node *x = root->node;
         */
        long y = NULL;
        long x = $address(/*rb_root*/root, NODE);
        /*
        while (x != NULL)
        {
            y = x;
            if (node->key < x->key)
                x = x->left;
            else
                x = x->right;
        }
        */
        while (x != NULL) {
            y = x;
            if ($address(/*RBTreeNode*/node, KEY) < $address(/*RBTreeNode*/x, KEY))
                x = $address(/*RBTreeNode*/x, LEFT);
            else
                x = $address(/*RBTreeNode*/x, RIGHT);
        }
        //rb_parent(node) = y;
        $address(/*RBTreeNode*/node, PARENT, y);

        if (y != NULL) {
            if ($address(/*RBTreeNode*/node, KEY) < $address(/*RBTreeNode*/y, KEY)) {
                $address(/*RBTreeNode*/y, LEFT, node);
            } else {
                $address(/*RBTreeNode*/y, RIGHT, node);
            }
        } else {
            $address(/*rb_root*/root, NODE, node);
        }

        $byte(/*RBTreeNode*/node, COLOR, RED);

        rbtree_insert_fixup(root, node);
    }

    static long create_rbtree_node(long key, long parent, long left, long right) {
        long p;

        if ((p = $malloc(40)) == NULL) {
            return NULL;
        }
        $long(/*RBTreeNode*/p, KEY, key);
        $address(/*RBTreeNode*/p, LEFT, left);
        $address(/*RBTreeNode*/p, RIGHT, right);
        $address(/*RBTreeNode*/p, PARENT, parent);
        $byte(/*RBTreeNode*/p, COLOR, BLACK);

        return p;
    }

    static int insert_rbtree(long root, long key) {
        long node;

        if (search($address(/*rb_root*/root, NODE), key) != NULL) {
            return -1;
        }

        if ((node = create_rbtree_node(key, NULL, NULL, NULL)) == NULL) {
            return -1;
        }

        rbtree_insert(root, node);

        return 0;
    }

    static void rbtree_delete_fixup(long root, long node, long parent) {
        long other;

        while ((node == NULL || rb_is_black(node)) && node != $address(/*rb_root*/root, NODE)) {
            if ($address(/*RBTreeNode*/parent, LEFT) == node) {
                other = $address(/*RBTreeNode*/parent, RIGHT);
                if (rb_is_red(other)) {
                    rb_set_black(other);
                    rb_set_red(parent);
                    rbtree_left_rotate(root, parent);
                    other = $address(/*RBTreeNode*/parent, RIGHT);
                }
                if (($address(/*RBTreeNode*/other, LEFT) == NULL || rb_is_black($address(/*RBTreeNode*/other, LEFT))) &&
                        (!($address(/*RBTreeNode*/other, RIGHT) != NULL || rb_is_black($address(/*RBTreeNode*/other, RIGHT))))) {
                    rb_set_red(other);
                    node = parent;
                    parent = rb_parent(node);
                } else {
                    if ($address(/*RBTreeNode*/other, RIGHT) == NULL || rb_is_black($address(/*RBTreeNode*/other, RIGHT))) {
                        rb_set_black($address(/*RBTreeNode*/other, LEFT));
                        rb_set_red(other);
                        rbtree_right_rotate(root, other);
                        other = $address(/*RBTreeNode*/parent, RIGHT);
                    }
                    rb_set_color(other, rb_color(parent));
                    rb_set_black(parent);
                    rb_set_black($address(/*RBTreeNode*/other, RIGHT));
                    rbtree_left_rotate(root, parent);
                    node = $address(/*rb_root*/root, NODE);
                    break;
                }
            } else {
                other = $address(/*RBTreeNode*/parent, LEFT);
                if (rb_is_red(other)) {
                    rb_set_black(other);
                    rb_set_red(parent);
                    rbtree_right_rotate(root, parent);
                    other = $address(/*RBTreeNode*/parent, LEFT);
                }
                if (($address(/*RBTreeNode*/other, LEFT) == NULL || rb_is_black($address(/*RBTreeNode*/other, LEFT))) &&
                        (!($address(/*RBTreeNode*/other, RIGHT) != NULL || rb_is_black($address(/*RBTreeNode*/other, RIGHT))))) {
                    rb_set_red(other);
                    node = parent;
                    parent = rb_parent(node);
                } else {
                    if ($address(/*RBTreeNode*/other, LEFT) == NULL || rb_is_black($address(/*RBTreeNode*/other, LEFT))) {
                        rb_set_black($address(/*RBTreeNode*/other, RIGHT));
                        rb_set_red(other);
                        rbtree_left_rotate(root, other);
                        other = $address(/*RBTreeNode*/parent, LEFT);
                    }
                    rb_set_color(other, rb_color(parent));
                    rb_set_black(parent);
                    rb_set_black($address(/*RBTreeNode*/other, LEFT));
                    rbtree_right_rotate(root, parent);
                    node = $address(/*rb_root*/root, NODE);
                    break;
                }
            }
        }
        if (node != NULL)
            rb_set_black(node);
    }

    static void rbtree_delete(long root, long node) {
        long child;
        long parent;
        int color;

        if (($address(/*RBTreeNode*/node, LEFT) != NULL) && ($address(/*RBTreeNode*/node, RIGHT) != NULL)) {

            long replace = node;

            replace = $address(/*RBTreeNode*/replace, RIGHT);
            while ($address(/*RBTreeNode*/replace, LEFT) != NULL)
                replace = $address(/*RBTreeNode*/replace, LEFT);

            if (rb_parent(node) != NULL) {
                if ($address(/*RBTreeNode*/rb_parent(/*RBTreeNode*/node), LEFT) == node) {
                    $address(/*RBTreeNode*/rb_parent(/*RBTreeNode*/node), LEFT, replace);
                } else {
                    $address(/*RBTreeNode*/rb_parent(/*RBTreeNode*/node), RIGHT, replace);
                }
            } else {
                $address(/*rb_root*/root, NODE, replace);
            }

            child = $address(/*RBTreeNode*/replace, RIGHT);
            parent = rb_parent(replace);

            color = rb_color(replace);

            if (parent == node) {
                parent = replace;
            } else {
                if (child != NULL)
                    rb_set_parent(child, parent);
                $address(/*RBTreeNode*/parent, LEFT, child);

                $address(/*RBTreeNode*/replace, RIGHT, $address(/*RBTreeNode*/node, RIGHT));
                rb_set_parent($address(/*RBTreeNode*/node, RIGHT), replace);
            }

            $address(/*RBTreeNode*/replace, PARENT, $address(/*RBTreeNode*/node, PARENT));
            $byte(/*RBTreeNode*/replace, COLOR, $byte(/*RBTreeNode*/node, COLOR));
            $address(/*RBTreeNode*/replace, LEFT, $address(/*RBTreeNode*/node, LEFT));
            $address(/*RBTreeNode*/$address(/*RBTreeNode*/node, LEFT), PARENT, replace);

            if (color == BLACK) {
                rbtree_delete_fixup(root, child, parent);
            }
            $free(node);

            return;
        }

        if ($address(/*RBTreeNode*/node, LEFT) != NULL) {
            child = $address(/*RBTreeNode*/node, LEFT);
        } else {
            child = $address(/*RBTreeNode*/node, RIGHT);
        }

        parent = $address(/*RBTreeNode*/node, PARENT);

        color = $byte(/*RBTreeNode*/node, COLOR);

        if (child != NULL) {
            $address(/*RBTreeNode*/child, PARENT, parent);
        }
        if (parent != NULL) {
            if ($address(/*RBTreeNode*/parent, LEFT) == node) {
                $address(/*RBTreeNode*/parent, LEFT, child);
            } else {
                $address(/*RBTreeNode*/parent, RIGHT, child);
            }
        } else {
            $address(/*rb_root*/root, NODE, child);
        }
        if (color == BLACK) {
            rbtree_delete_fixup(root, child, parent);
        }
        $free(node);
    }

    static void delete_rbtree(long root, long key) {
        long z;
        long node;

        if ((z = search($address(/*rb_root*/root, NODE), key)) != NULL)
            rbtree_delete(root, z);
    }

    static void rbtree_destroy(long tree) {
        if (tree == NULL)
            return;

        if ($address(/*RBTreeNode*/tree, LEFT) != NULL) {
            rbtree_destroy($address(/*RBTreeNode*/tree, LEFT));
        }
        if ($address(/*RBTreeNode*/tree, RIGHT) != NULL) {
            rbtree_destroy($address(/*RBTreeNode*/tree, RIGHT));
        }
        $free(tree);
    }

    static void destroy_rbtree(long root) {
        if (root != NULL) {
            rbtree_destroy($address(/*rb_root*/root, NODE));
        }
        $free(root);
    }

    static void rbtree_print(long tree, long key, int direction) {
        if (tree != NULL) {
            if (direction == 0) {
                System.out.printf("%s is root\n", "" + $long(/*RBTreeNode*/tree, KEY));
            } else {
                System.out.printf("%s is %s's %s child\n", "" + $long(/*RBTreeNode*/tree, KEY), rb_is_red(tree) ? "R" : "B", key, direction == 1 ? "right" : "left");
            }
            rbtree_print($address(/*RBTreeNode*/tree, LEFT), $long(/*RBTreeNode*/tree, KEY), -1);
            rbtree_print($address(/*RBTreeNode*/tree, RIGHT), $long(/*RBTreeNode*/tree, KEY), 1);
        }
    }

    static void print_rbtree(long root) {
        if (root != NULL && $address(/*rb_root*/root, NODE) != NULL) {
            rbtree_print($address(/*rb_root*/root, NODE), $address(/*rb_root*/$address(/*rb_root*/root, NODE), KEY), 0);
        }
    }

    public static long $malloc(long size) {
        long address = memory.allocateMemory(size);
        return address;
    }

    public static void $free(long address) {
        memory.freeMemory(address);
    }

    public static boolean $booelan(long address, long offset) {
        return memory.getByte(address + offset) == 1;
    }

    public static int $int(long address, long offset) {
        return memory.getInt(address + offset);
    }

    public static short $short(long address, long offset) {
        return memory.getShort(address + offset);
    }

    public static float $float(long address, long offset) {
        return memory.getFloat(address + offset);
    }

    public static double $double(long address, long offset) {
        return memory.getDouble(address + offset);
    }

    public static char $char(long address, long offset) {
        return memory.getChar(address + offset);
    }

    public static byte $byte(long address, long offset) {
        return memory.getByte(address + offset);
    }

    public static long $long(long address, long offset) {
        return memory.getLong(address + offset);
    }

    public static long $address(long address, long offset) {
        return memory.getAddress(address + offset);
    }

    public static void $byte(long address, long offset, byte value) {
        memory.putByte(address + offset, value);
    }

    public static void $char(long address, long offset, char value) {
        memory.putChar(address + offset, value);
    }

    public static void $short(long address, long offset, short value) {
        memory.putShort(address + offset, value);
    }

    public static void $int(long address, long offset, int value) {
        memory.putInt(address + offset, value);
    }

    public static void $long(long address, long offset, long value) {
        memory.putLong(address + offset, value);
    }

    public static void $boolean(long address, long offset, boolean value) {
        memory.putByte(address + offset, (byte) (value ? 1 : 0));
    }

    public static void $float(long address, long offset, float value) {
        memory.putFloat(address + offset, value);
    }

    public static void $double(long address, long offset, double value) {
        memory.putDouble(address + offset, value);
    }

    public static void $address(long address, long offset, long value) {
        memory.putAddress(address + offset, value);
    }

    //    @Benchmark
    public void test() {
        AbstractQueuedSynchronizer d;
        long root = create_rbtree();
        for (int i : array) {
            insert_rbtree(root, i);
        }
        search($address(root, NODE), 50);
        $free(0);
    }

    //    @Benchmark
    public void test3() {
        long root = create_rbtree();
        for (int i : array) {
            insert_rbtree(root, i);
        }
        search($address(root, NODE), 50);
        $free(0);
    }

    //@Benchmark
    public void test2() {
        Map<Long, Long> map = new TreeMap<>();
        for (int i : array) {
            map.put((long) i, 1L);
        }
        map.get(50L);
    }

    int rbtree_search(long root, long key) {
        if (root != NULL)
            return search($address(/*rb_root*/root, NODE), key) > 0 ? 0 : -1;

        return 0;
    }

}
