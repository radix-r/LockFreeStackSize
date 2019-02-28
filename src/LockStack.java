//import org.w3c.dom.Node;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LockStack<T> {

    Lock lock;
    Node head;
    int numOps;

    class Node<T>{
        public T val;
        public Node next;

        public Node(T val){
            this.val = val;

        }
    }

    public LockStack(){
        lock = new ReentrantLock();
        head = null;
    }

    public boolean push(T p){
        lock.lock();
        Node n = new Node(p);
        n.next = head;
        head = n;
        numOps++;
        lock.unlock();
        return true;
    }

    public T pop(){
        lock.lock();
        if (head == null){
            lock.unlock();
            return null;
        }
        Node n = head;
        head = n.next;
        numOps++;
        lock.unlock();
        return (T)n.val;
    }

    public int getNumOps(){
        return numOps;
    }



}
