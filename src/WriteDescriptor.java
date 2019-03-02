import java.util.concurrent.atomic.AtomicBoolean;

public class WriteDescriptor<T>{
    public T oldValue;
    public T newValue;
    public Node<T> location;
    public AtomicBoolean done;

    WriteDescriptor(T oldV, T newV, Node<T> loc){
        oldValue = oldV;
        newValue = newV;
        location =  loc;
        done.set(false);

    }

}