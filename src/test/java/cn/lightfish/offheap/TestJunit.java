package cn.lightfish.offheap;

import org.junit.Assert;
import org.junit.Test;

public class TestJunit {
    @Test
    public void test() {
        System.out.println("junit is running");
        Assert.fail();
    }
}
