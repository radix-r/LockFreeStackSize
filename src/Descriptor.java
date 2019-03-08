import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class Descriptor<T>{
    public AtomicInteger size;
    public AtomicReference<WriteDescriptor<T>> writeDescriptor;

    Descriptor(int size, WriteDescriptor<T> writeOp){
        this.size = new AtomicInteger(size) ;
        this.writeDescriptor= new AtomicReference<>(writeOp) ;
    }

    @Override
    public boolean equals(Object other){
        boolean result = false;
        if(other instanceof Descriptor){
            Descriptor that = (Descriptor) other;
            result = this.size.get() == that.size.get()
                    && this.writeDescriptor == that.writeDescriptor;
        }
        return result;
    }
}