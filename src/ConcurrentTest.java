import java.util.concurrent.CountDownLatch;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

public class ConcurrentTest {

    static LockFreeStack<Integer>  lfs;
    private static final int NUMOPS = 2187; // 3^7 for easy division among 3 functions

    public  static void main(String[] args)throws InterruptedException{

        lfs = new LockFreeStack<Integer>(Integer.class);

        // do 1024 pops and 1024 pushes using different numbers of threads
        long startTime = System.nanoTime();
        long endTime;
        long duration;
        for (int i = 0; i < NUMOPS;i++){

            lfs.push(i);
        }

        for (int i = 0; i < NUMOPS;i++){
            lfs.pop();
            lfs.getSize();
        }
        endTime = System.nanoTime();
        duration = (endTime-startTime)/1000000; // in ms
        System.out.printf("1 threaded execution of %d pushes, %d pops, and %d size: %d ms\n", NUMOPS,NUMOPS,NUMOPS,(int)duration);

        for (int i = 3; i <= 32; i+=3){
            CountDownLatch latch = new CountDownLatch(i);
            int split = i/3;

            Thread[] pushT = new Thread[split];
            Thread[] popT = new Thread[split];
            Thread[] sizeT = new Thread[split];



            // init the threads
            for (int th = 0; th< split; th++){
                pushT[th] = new Thread(new PushThread(NUMOPS/split,latch));
                popT[th] = new Thread(new PopThread(NUMOPS/split,latch));
                sizeT[th] = new Thread(new SizeThread(NUMOPS/split, latch));
            }



            startTime = System.nanoTime();
            // start the threads
            for (int th = 0; th< split; th++){
                pushT[th].start();
                popT[th].start();
                sizeT[th].start();
            }


            latch.await();



            endTime = System.nanoTime();
            duration = (endTime-startTime)/1000000; // in ms

            System.out.printf("%d threaded execution of %d pushes, %d pops, and %d size calls: %d ms\n", i,NUMOPS,NUMOPS,NUMOPS, (int)duration);


        }



    }

    public static class PushThread implements Runnable{
        int numOps;
        private CountDownLatch latch;
        public PushThread(int numOps, CountDownLatch l)
        {
            this.numOps = numOps;
            this.latch = l;
        }


        public void run(){
            for (int i = 0; i < numOps;i++){
                //double toPush = Math.random();
                lfs.push(i);
            }
            latch.countDown();
        }


    }

    public static class PopThread implements Runnable{
        int numOps;
        private CountDownLatch latch;
        public PopThread(int numOps, CountDownLatch l){

            this.numOps = numOps;
            latch = l;

        }
        public void run(){
            for (int i = 0; i <numOps; i++){
                lfs.pop();
            }
            latch.countDown();
        }
    }

    public static class SizeThread implements Runnable{
        int numOps;
        private CountDownLatch latch;
        public SizeThread(int numOps, CountDownLatch l){
            this.numOps = numOps;
            latch = l;
        }
        public void run(){
            for (int i = 0; i <numOps; i++){
                lfs.getSize();
            }
            latch.countDown();
        }
    }

}
