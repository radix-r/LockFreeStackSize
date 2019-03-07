import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class LockFreeStackTest {

    @Test
    public void push() {
    }

    @Test
    public void pop() {
    }

    @Test
    public void getNumOps() {
    }

    @Test
    public void getSize() {
    }

    @Test
    public void highestBit() {

        int out = LockFreeStack.highestBit(1);
        Assert.assertEquals("Highest bit of 1",0, out);

        out = LockFreeStack.highestBit(32);
        Assert.assertEquals("Highest bit of 32",5, out);

        out = LockFreeStack.highestBit(33);
        Assert.assertEquals("Highest bit of 33",5, out);

    }
}