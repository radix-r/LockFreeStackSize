import org.junit.Assert;
import org.junit.Test;


public class LockFreeStackTest {

    @Test
    public void push() {
        LockFreeStack<Integer> lfs = new LockFreeStack(Integer.class);
        lfs.push(5);
        lfs.push(4);
        lfs.push(3);

        int out = lfs.getAtIndex(0);
        Assert.assertEquals("push 5",5, out);
        out = lfs.getAtIndex(1);
        Assert.assertEquals("push 4",4, out);
        out = lfs.getAtIndex(2);
        Assert.assertEquals("push 3",3, out);
    }

    @Test
    public void pop() {
        LockFreeStack<Integer> lfs = new LockFreeStack(Integer.class);
        lfs.push(5);
        lfs.push(4);
        lfs.push(3);

        int out = lfs.pop();
        Assert.assertEquals("expect 3 at top of stack",3,out);
        out = lfs.pop();
        Assert.assertEquals("expect 4 at top of stack",4,out);
        out = lfs.pop();
        Assert.assertEquals("expect 5 at top of stack",5,out);

        /*
        out = lfs.pop();
        lfs.push(7);
        Assert.assertEquals("expect 7 because pop blocks until push takes effect",7,out);
        */

    }

    @Test
    public void getNumOps() {
    }

    @Test
    public void getSize() {
        LockFreeStack<Integer> lfs = new LockFreeStack(Integer.class);
        lfs.push(5);
        lfs.push(4);
        lfs.push(3);

        int out = lfs.getSize();
        Assert.assertEquals("expect size 3",3,out);

        lfs.pop();

        out = lfs.getSize();
        Assert.assertEquals("expect size 2",2,out);
        lfs.pop();

        out = lfs.getSize();
        Assert.assertEquals("expect size 1",1,out);

        lfs.pop();

        out = lfs.getSize();
        Assert.assertEquals("expect size 0",0,out);
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