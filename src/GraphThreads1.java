import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

public class GraphThreads1 extends ApplicationFrame {

    public GraphThreads1(String title) throws InterruptedException{
        super(title);
        ConcurrentTest ct = new ConcurrentTest();
        JFreeChart lineChart = ChartFactory.createLineChart(
                title,
                "Threads","Time (ms)",
                ct.timeWRToThreadCount(),
                PlotOrientation.VERTICAL,
                false,true,false);

        ChartPanel chartPanel = new ChartPanel( lineChart );
        chartPanel.setPreferredSize( new java.awt.Dimension( 560 , 367 ) );
        setContentPane( chartPanel );
    }

    public static void graph(String title)throws InterruptedException {

        GraphThreads1 chart = new GraphThreads1(title);

        chart.pack( );
        RefineryUtilities.centerFrameOnScreen( chart );
        chart.setVisible( true );
    }
}