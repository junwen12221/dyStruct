package cn.lightfish.offheap;

import cn.lightfish.offHeap.DyStruct;
import cn.lightfish.offHeap.StructInfo;
import cn.lightfish.offHeap.Type;

import static cn.lightfish.offHeap.DyStruct.$;
import static cn.lightfish.offHeap.DyStruct.$malloc;

public class DyStructTest {
    public static void main(String[] args) {
        StructInfo struct = DyStruct.build("MyStruct", "member1", Type.Int, "member2Address", Type.Address);
        long address = DyStruct.$malloc(struct);
        $(address, "member1", 1);
        int member1 = $(address, "member1");
        System.out.println(member1);//print 1

        StructInfo struct2 = DyStruct.build("YourStruct","friend1",struct,"friend2Address",Type.Address);
        long address2 = $malloc(struct2);
        $(address2,"friend1.member1",1);
        member1 =  $(address2,"friend1.member1");
        System.out.println(member1);//print 1
        long friendAddress =  $(address2,"friend1");
        System.out.println(friendAddress);//print 1

        $(address,"member2Address",address2);//address->member2Address = address2;
        member1 = $($(address,"member2Address"),"friend1.member1");//(address->member2Address)->friend1.member1
        System.out.println(member1);//1
        $(address2,"friend2Address",$(address,"member2Address"));// address2->friend2Address = address->member2
        member1 = $($(address2,"friend2Address"),"friend1.member1");// address2->friend2Address->friend1.member1
        System.out.println(member1);//1
    }

}
