/**
 *
 * @Author: Ross Wagner
 * 2/3/2019
 *
 * Implementation of a lock free stack using exponential back off
 * Influenced by The example in the book in chapter 11
 *
 * Max capacity is (2^32) -1 =~ 2 billion
 *
 * The lack of locks in this implementation guarantees that at least one method call finishes in a finite number
 * of steps. I use exponential backoff instead a queuing structure so fairness is not guaranteed.
 *
 *
 * To Do
 * -----
 * Descriptor
 *  WriteDescriptor
 * resize()
 * push()
 * pop()
 * */


import java.lang.reflect.Array;
import java.util.EmptyStackException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicReferenceArray;


public class LockFreeStack<T> {


    private AtomicReferenceArray<AtomicReference<T>[]> memory;
    private AtomicInteger numOps;
    private AtomicInteger size;
    //private AtomicInteger numPush; // incremented on push
    //private AtomicInteger numPop;
    private static final int MIN_SLEEP = 2; // min sleep time in ms
    private static final int MAX_SLEEP = 20000; // max sleep time in ms
    private static final int FBS = 2; // size of first bucket
    private static final int INTSIZE = 32; //number of bits in java int
    public AtomicReference<Descriptor<T>> desc;




    /**
     * must supply a class variable (Integer.class) us used to create a generic array and is not inserted
     * into array. Class<T> type
     * */
    public LockFreeStack(Class<T> type){
        this.size = new AtomicInteger(0);
        //this.numPush = new AtomicInteger(0);
        //this.numPop = new AtomicInteger(0);
        this.numOps = new AtomicInteger(0);
        this.memory = new AtomicReferenceArray<AtomicReference<T>[]>(32);
        //@SuppressWarnings("unchecked")
        AtomicReference<T> dummy = new AtomicReference<T>();
        this.memory.set(0,(AtomicReference<T>[]) Array.newInstance(dummy.getClass(),2));
        //this.memory.set(0,new AtomicReferenceArray<T>(2));

        this.desc = new AtomicReference<Descriptor<T>>(new Descriptor<T>());

    }



    public static void main(String args[]){
        // performance testing here

        LockFreeStack<Integer> LFS = new LockFreeStack<Integer>(Integer.class);

        // spawn threads
        LFS.push(10);

        System.out.println(LFS.pop());


    }

    /**
     * Returns value at given index
    * */
    private AtomicReference<T> at(int i){
        int pos = i + FBS;
        int hiBit = highestBit(pos);
        int index = pos ^ (1<<hiBit);
        return memory.get(hiBit-highestBit(FBS))[index];
    }


    /**
     * Exponential backoff. take parameter n, how many times thread has had to back off
     * sleeps for (MIN_SLEEP^n) + small rand milliseconds
    * */
    private void backoff(int n){
        int sleepMillSec = (int)(Math.pow(MIN_SLEEP,n) + Math.random()*10);
        if (sleepMillSec > MAX_SLEEP){
            sleepMillSec = MAX_SLEEP;
        }

        try{
            Thread.sleep(sleepMillSec);
        }catch(InterruptedException e){
            Thread.currentThread().interrupt();  // set interrupt flag
            System.out.println("Wait interrupted.");
        }


    }

    private void completeWrite(WriteDescriptor<T> w){
        if (w.pending.get()){
            at(w.location).compareAndSet(w.oldValue,w.newValue);
            w.pending.set(false);
        }

    }

    protected boolean tryPush(T n){
        Descriptor<T> descCurr = desc.get();

        WriteDescriptor<T> pending = descCurr.writeDescriptor.get();
        if (pending != null){
            completeWrite(pending);
        }


        int bucket = highestBit(descCurr.size.get()+FBS)-highestBit(FBS);

        return false;
    }

    public void push(T p){

        //Node n = new Node(p);
        int tryCount = 0;
        while(true){
            if(tryPush(p)){
                // push successful
                numOps.getAndIncrement();
                return;
            }else{
                backoff(++tryCount);
            }
        }

    }

    protected T tryPop()throws EmptyStackException{

        Descriptor descCurr = desc.get();

        return null;
        /*
        if (oldHead == null){
            //lock.unlock();
            throw new EmptyStackException();
        }

        Node newHead = oldHead.next;

        if(head.compareAndSet(oldHead,newHead)){
            return oldHead;
        }else{
            return null;
        }
        */
    }

    public T pop() throws EmptyStackException {

        int tryCount = 0;
        while(true){
            T returnVal = tryPop();
            if (returnVal != null){
                numOps.getAndIncrement();
                return returnVal;
            }else{
                backoff(++tryCount);
            }
        }


    }

    public int getNumOps(){
        return numOps.get();
    }

    public int getSize(){
        return this.desc.get().size.get();
    }

    /**
     * returns index from right of highest order bit
     * */
    public static int highestBit(int num){
        return INTSIZE-1-Integer.numberOfLeadingZeros(num);
    }


}
