import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class Descriptor<T>{
    public AtomicInteger size;
    public AtomicInteger counter;
    public AtomicReference<WriteDescriptor<T>> writeDescriptor;

    Descriptor(int size, WriteDescriptor<T> writeOp){
        this.size = new AtomicInteger(size) ;
        this.counter = new AtomicInteger(0);
        this.writeDescriptor= new AtomicReference<>(writeOp) ;


    }
}