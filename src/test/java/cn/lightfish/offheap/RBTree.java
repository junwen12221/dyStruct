package cn.lightfish.offheap;

import cn.lightfish.offHeap.DyStruct;
import cn.lightfish.offHeap.StructInfo;
import cn.lightfish.offHeap.Type;
import cn.lightfish.offHeap.memory.LongAllocInterfaceImpl;
import cn.lightfish.offHeap.memory.MemoryInterface;

import static cn.lightfish.offHeap.DyStruct.*;

public class RBTree {
    final static byte RED = 0;
    final static byte BLACK = 1;
    final static long NULL = 0L;

    /*dystruct offset false*/

    public static void main(String[] args) throws Exception {
        DyStruct.setMemoryInterface(new MemoryInterface(new LongAllocInterfaceImpl()));
        int[] array = {10, 40, 30, 60, 90, 70, 20, 50, 80};
        long root = create_rbtree();
        for (int i : array) {
            try {
                insert_rbtree(root, i);
                print_rbtree(root);
            } catch (Exception e) {
                e.printStackTrace();
                throw e;
            }

        }
        destroy_rbtree(root);
    }

    static final StructInfo RBTreeNode = DyStruct.build("RBTreeNode",
            "color", Type.Byte,
            "key", Type.Long,
            "left", Type.Address,
            "right", Type.Address,
            "parent", Type.Address);
    static final StructInfo rb_root = DyStruct.build("rb_root",
            "node", Type.Address);

    static void rbtree_left_rotate(long root, long x) {
        //long y = x->right;
        long y = $(x, "right");

        //x->right = y->left;
        //if (y->left != NULL)
        //    y->left->parent = x;
        $(x, "right", $(y, "left"));
        if ($address(y, "left") != NULL) {
            $($(y, "left"), "parent", x);
        }

        //    y->parent = x->parent;
        $(y, "parent", $(x, "parent"));
        if ($address(x, "parent") == NULL) {
            //tree = y;
            $(root, "node", y);
        } else {
            /*
            if (x->parent->left == x)
            x->parent->left = y;
             */
            if ($address($(x, "parent"), "left") == x) {
                $($(x, "parent"), "left", y);
            } else {
                /*
                 x->parent->right = y;
                 */
                $($(x, "parent"), "right", y);
            }
        }
        // y->left = x;
        $(y, "left", x);
        // x->parent = y;
        $(x, "parent", y);
    }

    static void rbtree_right_rotate(long root, long y) {
        //long y = x->right;
        long x = $(y, "left");
        /*
            y->left = x->right;
            if (x->right != NULL)
                    x->right->parent = y;
         */
        $(y, "left", $(x, "right"));
        if ($address(x, "right") != NULL) {
            $($(x, "right"), "parent", y);
        }

        //  y->parent = x->parent;
        $(x, "parent", $(y, "parent"));
        // if (y->parent == NULL)
        if ($address(y, "parent") == NULL) {
            //root->node = x;
            $(root, "node", x);
        } else {
            /*
            if (y == y->parent->right)
            y->parent->right = x;
             */
            if ($address($(y, "parent"), "right") == y) {
                $($(y, "parent"), "right", x);
            } else {
                /*
                 y->parent->left = x;
                 */
                $($(y, "parent"), "left", x);
            }
        }

        // x->right = y;
        $(x, "right", y);
        // y->parent = x;
        $(y, "parent", x);
    }

    static long create_rbtree() {
        long root = $malloc(rb_root);
        $(root, "node", NULL);

        return root;
    }

    static long rb_parent(long r) {
        return $(r, "parent");
    }

    static byte rb_color(long r) {
        return $(r, "color");
    }

    static boolean rb_is_red(long r) {
        return ((byte) $(r, "color") == RED);
    }

    static boolean rb_is_black(long r) {
        return ((byte) $(r, "color") == BLACK);
    }

    static void rb_set_black(long r) {
        $(r, "color", BLACK);
    }

    static void rb_set_red(long r) {
        $(r, "color", RED);
    }

    static void rb_set_parent(long r, long p) {
        $(r, "parent", p);
    }

    static void rb_set_color(long r, byte c) {
        $(r, "color", c);
    }

    static long search(long x, long key) {
        if (x == NULL || $address(x, "key") == key) {
            return x;
        }
        if (key < $address(x, "key")) {
            return search($(x, "left"), key);
        } else {
            return search($(x, "right"), key);
        }
    }

    int rbtree_search(long root, long key) {
        if (root != NULL)
            return search($(root, "node"), key) > 0 ? 0 : -1;

        return 0;
    }

    static long iterative_search(long x, long key) {
        while ((x != NULL) && ($address(x, "key") != key)) {
            if (key < $address(x, "key"))
                x = $(x, "left");
            else
                x = $(x, "right");
        }

        return x;
    }

    static int iterative_rbtree_search(long root, long key) {
        if (root != NULL)
            return iterative_search($(root, "node"), key) > 0 ? 0 : -1;

        return 0;
    }

    static void rbtree_insert_fixup(long root, long node) {
        long parent;
        long gparent;

        while ((parent = rb_parent(node)) != NULL && rb_is_red(parent)) {
            gparent = rb_parent(parent);

            if (parent == $address(gparent, "left")) {
                {
                    long uncle = $(gparent, "right");
                    if (uncle != NULL && rb_is_red(uncle)) {
                        rb_set_black(uncle);
                        rb_set_black(parent);
                        rb_set_red(gparent);
                        node = gparent;
                        continue;
                    }
                }

                if ($address(parent, "right") == node) {
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
                    long uncle = $(gparent, "left");
                    if (uncle != NULL && rb_is_red(uncle)) {
                        rb_set_black(uncle);
                        rb_set_black(parent);
                        rb_set_red(gparent);
                        node = gparent;
                        continue;
                    }
                }

                if ($address(parent, "left") == node) {
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

        rb_set_black($(root, "node"));
    }

    static void rbtree_insert(long root, long node) {
        /*
            Node *y = NULL;
            Node *x = root->node;
         */
        long y = NULL;
        long x = $(root, "node");
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
            if ($address(node, "key") < $address(x, "key"))
                x = $(x, "left");
            else
                x = $(x, "right");
        }
        //rb_parent(node) = y;
        $(node, "parent", y);

        if (y != NULL) {
            if ($address(node, "key") < $address(y, "key")) {
                $(y, "left", node);
            } else {
                $(y, "right", node);
            }
        } else {
            $(root, "node", node);
        }

        $(node, "color", RED);

        rbtree_insert_fixup(root, node);
    }

    static long create_rbtree_node(long key, long parent, long left, long right) {
        long p;

        if ((p = $malloc(RBTreeNode)) == NULL) {
            return NULL;
        }
        $(p, "key", key);
        $(p, "left", left);
        $(p, "right", right);
        $(p, "parent", parent);
        $(p, "color", BLACK);

        return p;
    }

    static int insert_rbtree(long root, long key) {
        long node;

        if (search($(root, "node"), key) != NULL) {
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

        while ((!(node != NULL) || rb_is_black(node)) && node != $address(root, "node")) {
            if ($address(parent, "left") == node) {
                other = $(parent, "right");
                if (rb_is_red(other)) {
                    rb_set_black(other);
                    rb_set_red(parent);
                    rbtree_left_rotate(root, parent);
                    other = $(parent, "right");
                }
                if ((!($address(other, "left") != NULL) || rb_is_black($(other, "left"))) &&
                        (!($address(other, "right") != NULL || rb_is_black($(other, "right"))))) {
                    rb_set_red(other);
                    node = parent;
                    parent = rb_parent(node);
                } else {
                    if (!($address(other, "right") != NULL) || rb_is_black($(other, "right"))) {
                        rb_set_black($(other, "left"));
                        rb_set_red(other);
                        rbtree_right_rotate(root, other);
                        other = $(parent, "right");
                    }
                    rb_set_color(other, rb_color(parent));
                    rb_set_black(parent);
                    rb_set_black($(other, "right"));
                    rbtree_left_rotate(root, parent);
                    node = $(root, "node");
                    break;
                }
            } else {
                other = $(parent, "left");
                if (rb_is_red(other)) {
                    rb_set_black(other);
                    rb_set_red(parent);
                    rbtree_right_rotate(root, parent);
                    other = $(parent, "left");
                }
                if ((!($address(other, "left") != NULL) || rb_is_black($(other, "left"))) &&
                        (!($address(other, "right") != NULL || rb_is_black($(other, "right"))))) {
                    rb_set_red(other);
                    node = parent;
                    parent = rb_parent(node);
                } else {
                    if (!($address(other, "left") != NULL) || rb_is_black($(other, "left"))) {
                        rb_set_black($(other, "right"));
                        rb_set_red(other);
                        rbtree_left_rotate(root, other);
                        other = $(parent, "left");
                    }
                    rb_set_color(other, rb_color(parent));
                    rb_set_black(parent);
                    rb_set_black($(other, "left"));
                    rbtree_right_rotate(root, parent);
                    node = $(root, "node");
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

        if (($address(node, "left") != NULL) && ($address(node, "right") != NULL)) {

            long replace = node;

            replace = $(replace, "right");
            while ($address(replace, "left") != NULL)
                replace = $(replace, "left");

            if (rb_parent(node) != NULL) {
                if ($address(rb_parent(node), "left") == node) {
                    $(rb_parent(node), "left", replace);
                } else {
                    $(rb_parent(node), "right", replace);
                }
            } else {
                $(root, "node", replace);
            }

            child = $(replace, "right");
            parent = rb_parent(replace);

            color = rb_color(replace);

            if (parent == node) {
                parent = replace;
            } else {
                if (child != NULL)
                    rb_set_parent(child, parent);
                $(parent, "left", child);

                $(replace, "right", $(node, "right"));
                rb_set_parent($(node, "right"), replace);
            }

            $(replace, "parent", $(node, "parent"));
            $(replace, "color", $(node, "color"));
            $(replace, "left", $(node, "left"));
            $($(node, "left"), "parent", replace);

            if (color == BLACK) {
                rbtree_delete_fixup(root, child, parent);
            }
            $free(node);

            return;
        }

        if ($address(node, "left") != NULL) {
            child = $(node, "left");
        } else {
            child = $(node, "right");
        }

        parent = $(node, "parent");

        color = $(node, "color");

        if (child != NULL) {
            $(child, "parent", parent);
        }
        if (parent != NULL) {
            if ($address(parent, "left") == node) {
                $(parent, "left", child);
            } else {
                $(parent, "right", child);
            }
        } else {
            $(root, "node", child);
        }
        if (color == BLACK) {
            rbtree_delete_fixup(root, child, parent);
        }
        $free(node);
    }

    static void delete_rbtree(long root, long key) {
        long z;
        long node;

        if ((z = search($(root, "node"), key)) != NULL)
            rbtree_delete(root, z);
    }

    static void rbtree_destroy(long tree) {
        if (tree == NULL)
            return;

        if ($address(tree, "left") != NULL) {
            rbtree_destroy($(tree, "left"));
        }
        if ($address(tree, "right") != NULL) {
            rbtree_destroy($(tree, "right"));
        }
        $free(tree);
    }

    static void destroy_rbtree(long root) {
        if (root != NULL) {
            rbtree_destroy($(root, "node"));
        }
        $free(root);
    }

    static void rbtree_print(long tree, long key, int direction) {
        if (tree != NULL) {
            if (direction == 0)
            {
                System.out.printf("%s is root\n", "" + $(tree, "key"));
            } else
            {
                System.out.printf("%s is %s's %s child\n", "" + $(tree, "key"), rb_is_red(tree) ? "R" : "B", key, direction == 1 ? "right" : "left");
            }
            rbtree_print($(tree, "left"), $(tree, "key"), -1);
            rbtree_print($(tree, "right"), $(tree, "key"), 1);
        }
    }

    static void print_rbtree(long root) {
        if (root != NULL && $address(root, "node") != NULL) {
            rbtree_print($(root, "node"), $($(root, "node"), "key"), 0);
        }
    }
}
