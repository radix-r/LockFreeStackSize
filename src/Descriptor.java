import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class Descriptor<T>{
    public AtomicInteger size;
    public AtomicInteger counter;
    public AtomicReference<WriteDescriptor<T>> writeDescriptor;

    Descriptor(){
        this.size = new AtomicInteger(0) ;
        this.counter = new AtomicInteger(0);
        this.writeDescriptor= new AtomicReference<>(null) ;


    }
}