//package cn.lightfish.offheap;
//
//import cn.lightfish.offHeap.DyStruct;
//import cn.lightfish.offHeap.StructInfo;
//import cn.lightfish.offHeap.Type;
//
//import static cn.lightfish.offHeap.DyStruct.*;
//
//public class Slab {
//    static int slab_pagesize;
//    final static StructInfo slab_header = DyStruct.build("slab_header",
//            "prev", Type.Address,
//            "next", Type.Address,
//            "slots", Type.Long,
//            "refcount", Type.Address,
//            "page", Type.Address,
//            "data", Type.Struct);//特殊的
//
//    final static StructInfo slab_chain = DyStruct.build("slab_chain ",
//            "itemsize", Type.Int,
//            "itemcount", Type.Int,
//            "slabsize", Type.Int,
//            "pages_per_alloc", Type.Int,
//            "initial_slotmask", Type.Address,
//            "empty_slotmask", Type.Address,
//            "alignment_mask", Type.Address,
//            "partial", Type.Address,
//            "empty", Type.Address,
//            "full", Type.Address
//    );//特殊的
//
//    public static void main(String[] args) {
//
//    }
//
//    static boolean POWEROF2(long x) {
//        return ((x) != 0 && ((x) & ((x) - 1)) == 0);
//    }
//
//    final static long NULL = 0L;
//
//    public static void slab_init(long sch, long itemsize) {
//        assert (sch != NULL);
//        assert (itemsize >= 1 && itemsize <= Long.MAX_VALUE);
//        assert (POWEROF2(slab_pagesize));
//
//        $(sch, "itemsize", itemsize);
//
//        final long data_offset = slab_header.getMap().get("data").offset;
//        final long least_slabsize = data_offset + 64 * $int(sch, "itemsize");
//        $(sch, "slabsize", 1 << (int) Math.ceil(Math.log(least_slabsize));
//        $(sch, "itemcount", 64);
//
//        if ($int(sch, "slabsize") - least_slabsize != 0) {
//            final int shrinked_slabsize = $int(sch, "slabsize") >> 1;
//
//            if (data_offset < shrinked_slabsize &&
//                    shrinked_slabsize - data_offset >= 2 * $int(sch, itemsize)) {
//
//                $int(sch, "slabsize", shrinked_slabsize);
//                $int(sch, "itemcount", (shrinked_slabsize - data_offset) / $int(sch, "itemsize");
//            }
//        }
//
//        $int(sch, "pages_per_alloc", (($int(sch, "slabsize") > slab_pagesize) ?
//                $int(sch, "slabsize") : slab_pagesize));
//
//        $(sch, "empty_slotmask", ~SLOTS_ALL_ZERO >> (64 - $int(sch, "itemcount");
//        $(sch, "initial_slotmask", $(sch, "empty_slotmask") ^ SLOTS_FIRST);
//        $(sch, "alignment_mask", ~$int(sch, "slabsize ") - 1);
//        $(sch, "partial", $int(sch, "empty" = sch -> full = NULL;
//
//        assert (slab_is_valid(sch));
//    }
//
//    ;
//
//    public static long slab_alloc(long slab_chain) {
//
//    }
//
//    public static void slab_free(long slab_chain, long ptr) {
//
//    }
//
//    public static void slab_traverse(long slab_chain, long ptr) {
//
//    }
//
//    public static void slab_destroy(long slab_chain) {
//
//    }
//
//    static int slab_is_valid(long sch) {
//        assert(POWEROF2(slab_pagesize));
//        assert(POWEROF2($long(sch,"slabsize")));
//        assert(POWEROF2($long(sch,"pages_per_alloc")));
//
//        assert($int(sch,"itemcount") >= 2 && $int(sch,"itemcount") <= 64);
//        assert($int(sch,"itemsize") >= 1 && $int(sch,"itemsize") <= Integer.MAX_VALUE);
//        assert($int(sch,"pages_per_alloc") >= slab_pagesize);
//        assert($int(sch,"pages_per_alloc") >= $int(sch,"slabsize"));
//
//        assert((slab_header.getMap().get("data").offset) +
//                $int(sch,"itemsize") * $int(sch,"itemcount") <= $int(sch,"slabsize"));
//
//        assert($long(sch,"empty_slotmask") == ~SLOTS_ALL_ZERO >> (64 - $int(sch,"itemcount"));
//        assert($long(sch,"initial_slotmask") == ($int(sch,"empty_slotmask") ^ SLOTS_FIRST));
//        assert($long(sch,"alignment_mask") == ~($int(sch,"slabsize") - 1));
//
//        const struct slab_header *const heads[] ={sch->full, sch->empty, sch->partial};
//
//        for (size_t head = 0; head < 3; ++head) {
//        const struct slab_header *prev = NULL, *slab;
//
//            for (slab = heads[head]; slab != NULL; slab = slab->next) {
//                if (prev == NULL)
//                    assert(slab->prev == NULL);
//                else
//                    assert(slab->prev == prev);
//
//                switch (head) {
//                    case 0:
//                        assert(slab->slots == SLOTS_ALL_ZERO);
//                        break;
//
//                    case 1:
//                        assert(slab->slots == sch->empty_slotmask);
//                        break;
//
//                    case 2:
//                        assert((slab->slots & ~sch->empty_slotmask) == SLOTS_ALL_ZERO);
//                        assert(FREE_SLOTS(slab->slots) >= 1);
//                        assert(FREE_SLOTS(slab->slots) < sch->itemcount);
//                        break;
//                }
//
//                if (slab->refcount == 0) {
//                    assert((uintptr_t) slab % sch->slabsize == 0);
//
//                    if (sch->slabsize >= slab_pagesize)
//                        assert((uintptr_t) slab->page % sch->slabsize == 0);
//                    else
//                        assert((uintptr_t) slab->page % slab_pagesize == 0);
//                } else {
//                    if (sch->slabsize >= slab_pagesize)
//                        assert((uintptr_t) slab % sch->slabsize == 0);
//                    else
//                        assert((uintptr_t) slab % slab_pagesize == 0);
//                }
//
//                prev = slab;
//            }
//        }
//
//        return 1;
//    }
//}
