import java.util.concurrent.atomic.AtomicBoolean;

public class WriteDescriptor<T>{
    public T oldValue;
    public T newValue;
    public int location;
    public boolean done;
    public AtomicBoolean pending;

    WriteDescriptor(T oldV, T newV, int loc){
        oldValue = oldV;
        newValue = newV;
        location =  loc;
        this.done = false;
        this.pending = new AtomicBoolean(false);

    }

}