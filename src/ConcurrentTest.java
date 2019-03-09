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

    static LockFreeStack<Double>  lfs;
    public static final int NUMOPS = 2187; // 3^7 for easy division among 3 functions
    private static final int TIMESTOAVG = 2; // how many times we loop through to get average
    private static final int BUCKETS = (32/3)+1;



    public  static void main(String[] args)throws InterruptedException {
        String title = String.format("Time to execute %d pushes, pops, and size operations with respect to number of threads", ConcurrentTest.NUMOPS);
        //GraphThreads1 graph1 = new GraphThreads1(title);
        GraphThreads1.graph(title);
    }

    public DefaultCategoryDataset timeWRToThreadCount()throws InterruptedException{
        DefaultCategoryDataset dataSet = new DefaultCategoryDataset( );


        int[] sums = new int[BUCKETS];

        for (int a = 0; a < TIMESTOAVG; a++) {

            lfs = new LockFreeStack<Double>(Double.class);

            // do 1024 pops and 1024 pushes using different numbers of threads
            long startTime = System.nanoTime();
            long endTime;
            long duration;
            for (int i = 0; i < NUMOPS; i++) {
                double toPush = Math.random();
                lfs.push(toPush);
            }

            for (int i = 0; i < NUMOPS; i++) {
                lfs.pop();
                lfs.getSize();
            }
            endTime = System.nanoTime();
            duration = (endTime - startTime) / 1000000; // in ms
            System.out.printf("1 threaded execution of %d pushes, %d pops, and %d size: %d ms\n", NUMOPS, NUMOPS, NUMOPS, (int) duration);
            //dataSet.addValue(duration, "time", String.format("%d", 1));
            sums[0] += duration;

            for (int i = 3; i <= 32; i += 3) {
                CountDownLatch latch = new CountDownLatch(i);
                int split = i / 3;

                Thread[] pushT = new Thread[split];
                Thread[] popT = new Thread[split];
                Thread[] sizeT = new Thread[split];


                // init the threads
                for (int th = 0; th < split; th++) {
                    pushT[th] = new Thread(new PushThread(NUMOPS / split, latch));
                    popT[th] = new Thread(new PopThread(NUMOPS / split, latch));
                    sizeT[th] = new Thread(new SizeThread(NUMOPS / split, latch));
                }


                startTime = System.nanoTime();
                // start the threads
                for (int th = 0; th < split; th++) {
                    pushT[th].start();
                    popT[th].start();
                    sizeT[th].start();
                }


                // wait for all threads to finish
                latch.await();


                endTime = System.nanoTime();
                duration = (endTime - startTime) / 1000000; // in ms

                System.out.printf("%d threaded execution of %d pushes, %d pops, and %d size calls: %d ms\n", i, NUMOPS, NUMOPS, NUMOPS, (int) duration);
                //dataSet.addValue(duration, "time", String.format("%d", i));
                sums[split] += duration;
            }
        }

        for(int i = 0; i<BUCKETS;i++){
            String colKey = String.format("%d",i*3);
            if (i==0){
                colKey = String.format("%d",1);
            }
            dataSet.addValue(sums[i]/TIMESTOAVG,"time",colKey);
        }
        return dataSet;

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
                double toPush = Math.random();
                lfs.push(toPush);
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
