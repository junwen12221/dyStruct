package cn.lightfish.offHeap;

public class DyStruct {
    final static MemoryInterface memory = new MemoryInterface(new HeapInterfaceImpl());
    final static StructObjectFactory globalStructObjectFactory = new StructObjectFactory(memory);

    public static long $malloc(StructInfo structInfo) {
        return globalStructObjectFactory.create(structInfo).address;
    }

    public static boolean $free(long address) {
        if (globalStructObjectFactory.map.remove(address) != null) {
            memory.freeMemory(address);
            return true;
        } else {
            return false;
        }
    }

    public static StructInfo build(String name, Object... map) {
        return Memory.build(name, map);
    }
    public static <T> T $(Long address, String name) {
        StructObject structObject = new StructObject();
        structObject.setStructInfo(globalStructObjectFactory.getStructInfo(address));
        structObject.setAddress(address);
        structObject.setMemory(memory);
        return structObject.$(name);
    }

    public static boolean $booelan(Long address, String name) {
        return $(address, name);
    }

    public static int $int(Long address, String name) {
        return $(address, name);
    }

    public static short $short(Long address, String name) {
        return $(address, name);
    }

    public static float $float(Long address, String name) {
        return $(address, name);
    }

    public static double $double(Long address, String name) {
        return $(address, name);
    }

    public static char $char(Long address, String name) {
        return $(address, name);
    }

    public static byte $byte(Long address, String name) {
        return $(address, name);
    }

    public static long $long(Long address, String name) {
        return $(address, name);
    }

    public static long $address(Long address, String name) {
        return $(address, name);
    }
    public static long $$address(Long address, String name,String name2) {
        return $($(address, name),name2);
    }

    public static void $byte(long address, String name, byte value) {
        $(address, name, value);
    }

    public static void $char(long address, String name, char value) {
        $(address, name, value);
    }

    public static void $short(long address, String name, short value) {
        $(address, name, value);
    }

    public static void $int(long address, String name, int value) {
        $(address, name, value);
    }

    public static void $long(long address, String name, long value) {
        $(address, name, value);
    }

    public static void $boolean(long address, String name, boolean value) {
        $(address, name, value);
    }

    public static void $float(long address, String name, float value) {
        $(address, name, value);
    }

    public static void $double(long address, String name, double value) {
        $(address, name, value);
    }
    public static <R> void $$(Long address, String name,String name2, R value) {
         $($(address,name),name2,value);
    }
    public static <R> void $$$(Long address, String name,String name2,String name3, R value) {
        $($($(address,name),name2),name3,value);
    }
    public static <R> void $(Long address, String name, R value) {
        StructInfo structInfo = globalStructObjectFactory.getStructInfo(address);
        Member member = structInfo.map.get(name);
        long offset = member.offset;
        long finalAddress = offset + address;
        //System.out.printf("%s = %s %n", address + "." + name, value.toString());
        switch (member.type) {
            case Booelean:
                info("memory.putBoolean(address+%d, (boolean) value);", offset);
                memory.putBoolean(finalAddress, (Boolean) value);
                break;
            case Int:
                info("memory.putInt(address+%d, (int) value);", offset);
                memory.putInt(finalAddress, (Integer) value);
                break;
            case Long:
                info("memory.putLong(address+%d, (long) value);", offset);
                memory.putLong(finalAddress, (Long) value);
                break;
            case Short:
                info("memory.putShort(address+%d, (short) value);", offset);
                memory.putShort(finalAddress, (Short) value);
                break;
            case Byte:
                info("memory.putByte(address+%d, (byte) value);", offset);
                memory.putByte(finalAddress, (Byte) value);
                break;
            case Float:
                info("memory.putFloat(address+%d, (float) value);", offset);
                memory.putFloat(finalAddress, (Float) value);
                break;
            case Double:
                info("memory.putDouble(address+%d, (double) value);", offset);
                memory.putDouble(finalAddress, (Double) value);
                break;
            case Char:
                info("memory.putChar(address+%d, (char) value);", offset);
                memory.putChar(finalAddress, (Character) value);
                break;
            case Address:
                info("memory.putAddress(address+%d, (long) value);", offset);
                memory.putLong(finalAddress, (Long) value);
                break;
            case Struct:
                StructObject v = (StructObject) value;
                long start = member.offset + address;
                long size = v.StructInfo.size;
                long copyAddress = v.address;
                for (int i = 0; i < size; i++) {
                    info("memory.copyMemory(address,copyAddress,%s", size);
                    memory.putByte(start++, v.memory.getByte(copyAddress++));
                }
                break;
        }
    }

    private static void info(String templeate, Object... args) {
//        System.out.printf(templeate + "%n", args);
//        List<StackWalker.StackFrame> list = new ArrayList<>();
//        StackWalker.getInstance().forEach(list::add);
//        System.out.println(list.get(2));
    }

    final static byte RED = 0;
    final static byte BLACK = 1;
    final static long NULL = 0L;

    public static void main(String[] args) throws Exception {
        StructInfo a = DyStruct.build("test1", "a", Type.Byte);
        StructInfo b = DyStruct.build("test2", "a", a);
        StructInfo c = DyStruct.build("test3", "c", Type.Address);
        long address = $malloc(b);//new struct
        $(address, "a.a", (byte) 1);
        byte aa = $(address, "a.a");// int a = address.a.a;
        long bb = $(address, "a");//struct a = address.a;
        long address2 = $malloc(c);
        $(address2, "c", address);
        $free($(address2, "c"));
        $free(address2);

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

    /*
     * 对红黑树的节点(x)进行左旋转
     *
     * 左旋示意图(对节点x进行左旋)：
     *      px                              px
     *     /                               /
     *    x                               y
     *   /  \      --(左旋)-->           / \                #
     *  lx   y                          x  ry
     *     /   \                       /  \
     *    ly   ry                     lx  ly
     *
     *
     */
    static void rbtree_left_rotate(long root, long x) {
        // 设置x的右孩子为y
        //long y = x->right;
        long y = $(x, "right");

        // 将 “y的左孩子” 设为 “x的右孩子”；
        // 如果y的左孩子非空，将 “x” 设为 “y的左孩子的父亲”
        //x->right = y->left;
        //if (y->left != NULL)
        //    y->left->parent = x;
        $(x, "right", $(y, "left"));
        if ($address(y, "left") != NULL) {
            $$(y, "left", "parent", x);
        }


        // 将 “x的父亲” 设为 “y的父亲”
        //    y->parent = x->parent;
        $(y, "parent", $(x, "parent"));

        if ($address(x, "parent") == NULL) {
            //tree = y;            // 如果 “x的父亲” 是空节点，则将y设为根节点
            $(root, "node", y);            // 如果 “x的父亲” 是空节点，则将y设为根节点
        } else {
            if ($address($(x, "parent"), "left") == x)
                $($(x, "parent"), "left", y);    // 如果 x是它父节点的左孩子，则将y设为“x的父节点的左孩子”
            else
                $($(x, "parent"), "right", y);
            // 如果 x是它父节点的左孩子，则将y设为“x的父节点的左孩子”
        }

        // 将 “x” 设为 “y的左孩子”
        $(y, "left", x);
        // 将 “x的父节点” 设为 “y”
        $(x, "parent", y);
    }

    static void rbtree_right_rotate(long root, long y) {
        // 设置x的右孩子为y
        //long y = x->right;
        long x = $(y, "left");


        // 将 “x的右孩子” 设为 “y的左孩子”；
        // 如果"x的右孩子"不为空的话，将 “y” 设为 “x的右孩子的父亲”
        $(y, "left", $(x, "right"));
        if ($address(x, "right") != NULL) {
            $($(x, "right"), "parent", y);
        }


        // 将 “x的父亲” 设为 “y的父亲”
        //    y->parent = x->parent;
        $(x, "parent", $(y, "parent"));

        if ($address(y, "parent") == NULL) {
            //tree = y;            // 如果 “x的父亲” 是空节点，则将y设为根节点
            $(root, "node", x);            // 如果 “x的父亲” 是空节点，则将y设为根节点
        } else {
            if ($address($(y, "parent"), "right") == y)
                $($(y, "parent"), "right", x);    // 如果 x是它父节点的左孩子，则将y设为“x的父节点的左孩子”
            else
                $($(y, "parent"), "left", x);
            // 如果 x是它父节点的左孩子，则将y设为“x的父节点的左孩子”
        }

        // 将 “x” 设为 “y的左孩子”
        $(x, "right", y);
        // 将 “x的父节点” 设为 “y”
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

    /*
     * (递归实现)查找"红黑树x"中键值为key的节点
     */
    static long search(long x, long key) {
        if (x == NULL || $address(x, "key") == key)
            return x;

        if (key < $address(x, "key"))
            return search($(x, "left"), key);
        else
            return search($(x, "right"), key);
    }

    int rbtree_search(long root, long key) {
        if (root != NULL)
            return search($(root, "node"), key) > 0 ? 0 : -1;

        return 0;
    }

    /*
     * (非递归实现)查找"红黑树x"中键值为key的节点
     */
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

    /*
     * 红黑树插入修正函数
     *
     * 在向红黑树中插入节点之后(失去平衡)，再调用该函数；
     * 目的是将它重新塑造成一颗红黑树。
     *
     * 参数说明：
     *     root 红黑树的根
     *     node 插入的结点        // 对应《算法导论》中的z
     */
    static void rbtree_insert_fixup(long root, long node) {
        long parent;
        long gparent;

        // 若“父节点存在，并且父节点的颜色是红色”
        while ((parent = rb_parent(node)) != NULL && rb_is_red(parent)) {
            gparent = rb_parent(parent);

            //若“父节点”是“祖父节点的左孩子”
            if (parent == $address(gparent, "left")) {
                // Case 1条件：叔叔节点是红色
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

                // Case 2条件：叔叔是黑色，且当前节点是右孩子
                if ($address(parent, "right") == node) {
                    long tmp;
                    rbtree_left_rotate(root, parent);
                    tmp = parent;
                    parent = node;
                    node = tmp;
                }

                // Case 3条件：叔叔是黑色，且当前节点是左孩子。
                rb_set_black(parent);
                rb_set_red(gparent);
                rbtree_right_rotate(root, gparent);
            } else//若“z的父节点”是“z的祖父节点的右孩子”
            {
                // Case 1条件：叔叔节点是红色
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

                // Case 2条件：叔叔是黑色，且当前节点是左孩子
                if ($address(parent, "left") == node) {
                    long tmp;
                    rbtree_right_rotate(root, parent);
                    tmp = parent;
                    parent = node;
                    node = tmp;
                }

                // Case 3条件：叔叔是黑色，且当前节点是右孩子。
                rb_set_black(parent);
                rb_set_red(gparent);
                rbtree_left_rotate(root, gparent);
            }
        }

        // 将根节点设为黑色
        rb_set_black($(root, "node"));
    }

    /*
     * 添加节点：将节点(node)插入到红黑树中
     *
     * 参数说明：
     *     root 红黑树的根
     *     node 插入的结点        // 对应《算法导论》中的z
     */
    static void rbtree_insert(long root, long node) {
        long y = NULL;
        long x = $(root, "node");

        // 1. 将红黑树当作一颗二叉查找树，将节点添加到二叉查找树中。
        while (x != NULL) {
            y = x;
            if ($address(node, "key") < $address(x, "key"))
                x = $(x, "left");
            else
                x = $(x, "right");
        }
        $(node, "parent", y);

        if (y != NULL) {
            if ($address(node, "key") < $address(y, "key"))
                $(y, "left", node);                // 情况2：若“node所包含的值” < “y所包含的值”，则将node设为“y的左孩子”
            else
                $(y, "right", node);            // 情况3：(“node所包含的值” >= “y所包含的值”)将node设为“y的右孩子”
        } else {
            $(root, "node", node);                // 情况1：若y是空节点，则将node设为根
        }

        // 2. 设置节点的颜色为红色
        $(node, "color", RED);

        // 3. 将它重新修正为一颗二叉查找树
        rbtree_insert_fixup(root, node);
    }

    /*
     * 创建结点
     *
     * 参数说明：
     *     key 是键值。
     *     parent 是父结点。
     *     left 是左孩子。
     *     right 是右孩子。
     */
    static long create_rbtree_node(long key, long parent, long left, long right) {
        long p;

        if ((p = $malloc(RBTreeNode)) == NULL)
            return NULL;
        $(p, "key", key);
        $(p, "left", left);
        $(p, "right", right);
        $(p, "parent", parent);
        $(p, "color", BLACK); // 默认为黑色

        return p;
    }

    /*
     * 新建结点(节点键值为key)，并将其插入到红黑树中
     *
     * 参数说明：
     *     root 红黑树的根
     *     key 插入结点的键值
     * 返回值：
     *     0，插入成功
     *     -1，插入失败
     */
    static int insert_rbtree(long root, long key) {
        long node;    // 新建结点

        // 不允许插入相同键值的节点。
        // (若想允许插入相同键值的节点，注释掉下面两句话即可！)
        if (search($(root, "node"), key) != NULL)
            return -1;

        // 如果新建结点失败，则返回。
        if ((node = create_rbtree_node(key, NULL, NULL, NULL)) == NULL)
            return -1;

        rbtree_insert(root, node);

        return 0;
    }

    /*
     * 红黑树删除修正函数
     *
     * 在从红黑树中删除插入节点之后(红黑树失去平衡)，再调用该函数；
     * 目的是将它重新塑造成一颗红黑树。
     *
     * 参数说明：
     *     root 红黑树的根
     *     node 待修正的节点
     */
    static void rbtree_delete_fixup(long root, long node, long parent) {
        long other;

        while ((!(node != NULL) || rb_is_black(node)) && node != $address(root, "node")) {
            if ($address(parent, "left") == node) {
                other = $(parent, "right");
                if (rb_is_red(other)) {
                    // Case 1: x的兄弟w是红色的
                    rb_set_black(other);
                    rb_set_red(parent);
                    rbtree_left_rotate(root, parent);
                    other = $(parent, "right");
                }
                if ((!($address(other, "left") != NULL) || rb_is_black($(other, "left"))) &&
                        (!($address(other, "right") != NULL || rb_is_black($(other, "right"))))) {
                    // Case 2: x的兄弟w是黑色，且w的俩个孩子也都是黑色的
                    rb_set_red(other);
                    node = parent;
                    parent = rb_parent(node);
                } else {
                    if (!($address(other, "right") != NULL) || rb_is_black($(other, "right"))) {
                        // Case 3: x的兄弟w是黑色的，并且w的左孩子是红色，右孩子为黑色。
                        rb_set_black($(other, "left"));
                        rb_set_red(other);
                        rbtree_right_rotate(root, other);
                        other = $(parent, "right");
                    }
                    // Case 4: x的兄弟w是黑色的；并且w的右孩子是红色的，左孩子任意颜色。
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
                    // Case 1: x的兄弟w是红色的
                    rb_set_black(other);
                    rb_set_red(parent);
                    rbtree_right_rotate(root, parent);
                    other = $(parent, "left");
                }
                if ((!($address(other, "left") != NULL) || rb_is_black($(other, "left"))) &&
                        (!($address(other, "right") != NULL || rb_is_black($(other, "right"))))) {
                    // Case 2: x的兄弟w是黑色，且w的俩个孩子也都是黑色的
                    rb_set_red(other);
                    node = parent;
                    parent = rb_parent(node);
                } else {
                    if (!($address(other, "left") != NULL) || rb_is_black($(other, "left"))) {
                        // Case 3: x的兄弟w是黑色的，并且w的左孩子是红色，右孩子为黑色。
                        rb_set_black($(other, "right"));
                        rb_set_red(other);
                        rbtree_left_rotate(root, other);
                        other = $(parent, "left");
                    }
                    // Case 4: x的兄弟w是黑色的；并且w的右孩子是红色的，左孩子任意颜色。
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

    /*
     * 删除结点
     *
     * 参数说明：
     *     tree 红黑树的根结点
     *     node 删除的结点
     */
    static void rbtree_delete(long root, long node) {
        long child;
        long parent;
        int color;

        // 被删除节点的"左右孩子都不为空"的情况。
        if (($address(node, "left") != NULL) && ($address(node, "right") != NULL)) {
            // 被删节点的后继节点。(称为"取代节点")
            // 用它来取代"被删节点"的位置，然后再将"被删节点"去掉。
            long replace = node;

            // 获取后继节点
            replace = $(replace, "right");
            while ($address(replace, "left") != NULL)
                replace = $(replace, "left");

            // "node节点"不是根节点(只有根节点不存在父节点)
            if (rb_parent(node) != NULL) {
                if ($address(rb_parent(node), "left") == node)
                    $(rb_parent(node), "left", replace);
                else
                    $(rb_parent(node), "right", replace);
            } else
                // "node节点"是根节点，更新根节点。
                $(root, "node", replace);

            // child是"取代节点"的右孩子，也是需要"调整的节点"。
            // "取代节点"肯定不存在左孩子！因为它是一个后继节点。
            child = $(replace, "right");
            parent = rb_parent(replace);
            // 保存"取代节点"的颜色
            color = rb_color(replace);

            // "被删除节点"是"它的后继节点的父节点"
            if (parent == node) {
                parent = replace;
            } else {
                // child不为空
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

            if (color == BLACK)
                rbtree_delete_fixup(root, child, parent);
            $free(node);

            return;
        }

        if ($address(node, "left") != NULL)
            child = $(node, "left");
        else
            child = $(node, "right");

        parent = $(node, "parent");
        // 保存"取代节点"的颜色
        color = $(node, "color");

        if (child != NULL)
            $(child, "parent", parent);

        // "node节点"不是根节点
        if (parent != NULL) {
            if ($address(parent, "left") == node)
                $(parent, "left", child);
            else
                $(parent, "right", child);
        } else
            $(root, "node", child);

        if (color == BLACK)
            rbtree_delete_fixup(root, child, parent);
        $free(node);
    }

    /*
     * 删除键值为key的结点
     *
     * 参数说明：
     *     tree 红黑树的根结点
     *     key 键值
     */
    static void delete_rbtree(long root, long key) {
        long z;
        long node;

        if ((z = search($(root, "node"), key)) != NULL)
            rbtree_delete(root, z);
    }

    /*
     * 销毁红黑树
     */
    static void rbtree_destroy(long tree) {
        if (tree == NULL)
            return;

        if ($address(tree, "left") != NULL)
            rbtree_destroy($(tree, "left"));
        if ($address(tree, "right") != NULL)
            rbtree_destroy($(tree, "right"));

        $free(tree);
    }

    static void destroy_rbtree(long root) {
        if (root != NULL)
            rbtree_destroy($(root, "node"));

        $free(root);
    }

    /*
     * 打印"红黑树"
     *
     * tree       -- 红黑树的节点
     * key        -- 节点的键值
     * direction  --  0，表示该节点是根节点;
     *               -1，表示该节点是它的父结点的左孩子;
     *                1，表示该节点是它的父结点的右孩子。
     */
    static void rbtree_print(long tree, long key, int direction) {
        if (tree != NULL) {
            if (direction == 0)    // tree是根节点
                System.out.printf("%s is root\n", "" + $(tree, "key"));
            else                // tree是分支节点
                System.out.printf("%s is %s's %s child\n", "" + $(tree, "key"), rb_is_red(tree) ? "R" : "B", key, direction == 1 ? "right" : "left");

            rbtree_print($(tree, "left"), $(tree, "key"), -1);
            rbtree_print($(tree, "right"), $(tree, "key"), 1);
        }
    }

    static void print_rbtree(long root) {
        if (root != NULL && $address(root, "node") != NULL)
            rbtree_print($(root, "node"), $($(root, "node"), "key"), 0);
    }
}
