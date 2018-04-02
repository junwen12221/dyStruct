package cn.lightfish.offheap;

import cn.lightfish.offHeap.DyStruct;

import static cn.lightfish.offHeap.DyStruct.$address;
import static cn.lightfish.offHeap.memory.Buddy.*;
import static cn.lightfish.offheap.RBTree0gc.*;

public class BuddyTest {
    /*
(0:32)
alloc 0 (sz= 4)
((([0:4](4:4))(8:8))(16:16))
size 0 (sz = 4)
alloc 16 (sz= 9)
((([0:4](4:4))(8:8))[16:16])
size 16 (sz = 16)
alloc 4 (sz= 3)
(({[0:4][4:4]}(8:8))[16:16])
size 4 (sz = 4)
alloc 8 (sz= 7)
{{{[0:4][4:4]}[8:8]}[16:16]}
free 4
((([0:4](4:4))[8:8])[16:16])
free 0
(((0:8)[8:8])[16:16])
free 8
((0:16)[16:16])
free 16
(0:32)
alloc 0 (sz= 32)
[0:32]
free 0
(0:32)
alloc 0 (sz= 0)
((((([0:1](1:1))(2:2))(4:4))(8:8))(16:16))
free 0
(0:32)

     */
    static int[] array = {10, 40, 30, 60, 90, 70, 20, 50, 80, 90};

    public static void main(String[] args) {
        long b = buddy_new(10);
        buddy_dump(b);
        long m1 = test_alloc(b, 4);
        test_size(b, m1);
        long m2 = test_alloc(b, 9);
        test_size(b, m2);
        long m3 = test_alloc(b, 3);
        test_size(b, m3);
        long m4 = test_alloc(b, 7);
        test_free(b, m3);
        test_free(b, m1);
        test_free(b, m4);
        test_free(b, m2);

        long m5 = test_alloc(b, 32);
        test_free(b, m5);

        long m6 = test_alloc(b, 0);
        test_free(b, m6);

        buddy_delete(b);
        long root = create_rbtree();
        for (int i : array) {
            insert_rbtree(root, i);
        }
        search($address(root, NODE), 50);
        DyStruct.$free(0);
    }

    static long
    test_alloc(long b, long sz) {
        long r = buddy_alloc(b, sz);
        printf("alloc %d (sz= %d)\n", r, sz);
        buddy_dump(b);
        return r;
    }

    static void
    test_free(long b, long addr) {
        printf("free %d\n", addr);
        buddy_free(b, addr);
        buddy_dump(b);
    }

    static void
    test_size(long b, long addr) {
        long s = buddy_size(b, addr);
        printf("size %d (sz = %d)\n", addr, s);
    }

}
