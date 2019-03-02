import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class Descriptor<T>{
    public AtomicInteger size;
    public int counter;
    public WriteDescriptor<T> writeDescriptor;

    Descriptor(){
        this.size = new AtomicInteger(0) ;
        this.writeDescriptor = null;


    }
}