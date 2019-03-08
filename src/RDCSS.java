
import java.util.concurrent.atomic.AtomicReference;

public class RDCSS<T> {

    /**
     *
     * */
    public AtomicReference<T> swap(AtomicReference<T> a1, AtomicReference<T> o1, AtomicReference<T> a2, AtomicReference<T> o2, AtomicReference<T> n2){
        AtomicReference<T> r = a2;
        if(r.get().equals(o2.get()) && a1.get().equals(o1.get())){
            if (!a2.compareAndSet(r.get(),n2.get())){
                // interrupted! operation no longer valid
                r= null;
            }
            //a2.set(n2.get());
        }

        return r;
    }

    public T read(){
        return null;
    }

}
