import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicReference;

import static org.junit.Assert.*;

public class RDCSSTest {

    @Test
    public void swap(){

        RDCSS<Integer> rdcss = new RDCSS<Integer>();

        AtomicReference<Integer> a1,o1,a2,o2,n2;
        a1 = new AtomicReference<Integer>( new Integer(1));
        o1 = new AtomicReference<Integer>( new Integer(1));
        a2 = new AtomicReference<Integer>(new Integer(2));
        o2 = new AtomicReference<Integer>(new Integer(2));
        n2 = new AtomicReference<Integer>(new Integer(7));

        int out = rdcss.swap(a1,o1,a2,o2,n2).get();


        Assert.assertEquals("expect 7 to be swapped in",7,out);
    }
}