import javafx.geometry.Bounds;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.data.xy.XYSeries;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.RangeType;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultValueDataset;

import javax.swing.*;
import java.awt.*;

public class FFTGrid extends JFrame{
    private JLabel fftLabel = new JLabel("");
    private JPanel lineGraph = new JPanel();
    private JPanel chartGraph = new JPanel();

    OTSDBFFTransformer transformer = new OTSDBFFTransformer();
    // DefaultCategoryDataset valueDataset = new DefaultCategoryDataset();

    public FFTGrid(){
        super("Быстрое преобразование Фурье модуль для БД");
        showLineGraph();
        drawUI();
    }


    private void showLineGraph() {
//        JFreeChart jfc = ChartFactory.createLineChart("БПФ","частота", "амплитуда",valueDataset,
//                PlotOrientation.VERTICAL,false,true,false);
//        ChartFactory.createXYLineChart()
        double[] input = transformer.createSinWaveBuffer(50, 1000);
        double[] spectr = transformer.fft(input);
        
//        for (int i = 0; i < spectr.length/2 ; i++) {
//            valueDataset.addValue(spectr[i],"значение",String.valueOf(i));
//        }
        XYSeries inputXY = new XYSeries("входной ряд");
        for (int i = 0; i < input.length; i++) {
            inputXY.add(i, input[i]);
        }
        XYSeriesCollection inputCollection = new XYSeriesCollection(inputXY);
        JFreeChart signalChart = ChartFactory.createXYLineChart("исходные данные", "время(мс)",
                "амплитуда", inputCollection);

        XYSeries xySeries = new XYSeries("амплитудный спектр");
        for (int i = 0; i < spectr.length/2; i++) {
            xySeries.add(i, spectr[i]);
        }
        XYSeriesCollection xySeriesCollection = new XYSeriesCollection(xySeries);
        JFreeChart fftChart = ChartFactory.createXYLineChart("амплитудный спектр", "частота",
                "амплитуда", xySeriesCollection,PlotOrientation.VERTICAL,false,false,false );

//        CategoryPlot lineCategory = jfc.getCategoryPlot();
        //lineCategory.setDomainAxis(new CategoryAxis("амплитуда"));
//        lineCategory.setOrientation(PlotOrientation.VERTICAL);
//        lineCategory.setBackgroundPaint(Color.black);
//        XYPlot rangeAxis = (XYPlot) fftChart.getXYPlot();

//        NumberAxis rangeAxis = (NumberAxis) lineCategory.getRangeAxis();
//        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

//        LineAndShapeRenderer lineAndShapeRenderer = (LineAndShapeRenderer) lineCategory.getRenderer();
//        Color lineGraphColor = new Color(255,131,0);
//        lineAndShapeRenderer.setSeriesPaint(0,lineGraphColor);

        ChartPanel lineChartPanel = new ChartPanel(fftChart);
        lineChartPanel.setFillZoomRectangle(true);
        lineChartPanel.setMouseWheelEnabled(true);

        lineGraph.removeAll();
        lineGraph.add(lineChartPanel, BorderLayout.EAST);
        lineGraph.validate();

        // ChartPanel chartPanel = new ChartPanel(signalChart);
        // chartPanel.setFillZoomRectangle(true);
        // chartPanel.setMouseWheelEnabled(true);

        // chartGraph.removeAll();
        // chartGraph.add(chartPanel, BorderLayout.WEST);
        // chartGraph.validate();
    }
    private void drawUI() {
        //JFrame mainFrame = new JFrame("БПФ модуль для БД");
        lineGraph.setForeground(new Color(24, 38, 176));
        chartGraph.setForeground(new Color(176, 38, 24));
        //red - new Color(255, 57, 0)
        super.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        super.setFocusableWindowState(true);
        super.setExtendedState(MAXIMIZED_BOTH);
        super.add(lineGraph);
        // super.add(chartGraph);
        super.setLocationRelativeTo(null);
        super.setVisible(true);
    }

}
