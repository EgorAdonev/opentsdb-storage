import javafx.scene.chart.Chart;
import javafx.scene.chart.ValueAxis;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.RangeType;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultValueDataset;

import javax.swing.*;
import java.awt.*;

public class FFTGrid extends JFrame{
    private JLabel fftLabel;
    private JPanel lineGraph = new JPanel();
    private JEditorPane editorPanel;
    OTSDBFFTransformer transformer = new OTSDBFFTransformer();
    DefaultCategoryDataset valueDataset = new DefaultCategoryDataset();

    public FFTGrid(){
        super("Быстрое преобразование Фурье модуль для БД");
        showLineGraph();
        drawUI();
    }


    private void showLineGraph(){
        JFreeChart jfc = ChartFactory.createLineChart("БПФ","частота", "амплитуда",valueDataset,
                PlotOrientation.VERTICAL,false,true,false);

//        ChartFactory.createXYLineChart()
        //jfc.getLegend().setFrame(BlockBorder.NONE);
//        valueDataset.setValue(100,"значение",String.valueOf(0.25));
//        valueDataset.setValue(150,"значение",String.valueOf(0.5));
//        valueDataset.setValue(200,"значение",String.valueOf(0.75));
//        valueDataset.setValue(250,"значение",String.valueOf(1));
        double[] spectr = transformer.fft(transformer.createSinWaveBuffer(100, 100));
        for (int i = 0; i < spectr.length/2 ; i++) {
            valueDataset.addValue(spectr[i],"значение",String.valueOf(i));
        }

        CategoryPlot lineCategory = jfc.getCategoryPlot();
        //lineCategory.setDomainAxis(new CategoryAxis("амплитуда"));
//        lineCategory.setOrientation(PlotOrientation.VERTICAL);
        lineCategory.setBackgroundPaint(Color.black);
//        XYPlot rangeAxis = (XYPlot) jfc.getXYPlot();
        NumberAxis rangeAxis = (NumberAxis) lineCategory.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());


        LineAndShapeRenderer lineAndShapeRenderer = (LineAndShapeRenderer) lineCategory.getRenderer();
        Color lineGraphColor = new Color(255,131,0);
        lineAndShapeRenderer.setSeriesPaint(0,lineGraphColor);

        ChartPanel lineChartPanel = new ChartPanel(jfc);
        lineChartPanel.setFillZoomRectangle(true);
        lineChartPanel.setMouseWheelEnabled(true);
        lineGraph.removeAll();
        lineGraph.add(lineChartPanel,BorderLayout.CENTER);
        lineGraph.validate();

    }
    private void drawUI() {
        //JFrame mainFrame = new JFrame("БПФ модуль для БД");
        lineGraph.setForeground(new Color(24, 38, 176));
        JButton stopButton = new JButton("\u2715");
        stopButton.setHorizontalAlignment(SwingConstants.CENTER);
        stopButton.setBackground(new Color(255, 57, 0));
//        stopButton.setBorderPainted(false);
        stopButton.setSize(50,50);
        stopButton.addActionListener(event -> {
            System.exit(0);
        });
        super.setFocusableWindowState(true);
        super.setExtendedState(MAXIMIZED_BOTH);
        super.add(stopButton);
//        JPanel listPane = lineGraph;
//        listPane.setLayout(new BoxLayout(lineGraph, BoxLayout.PAGE_AXIS));
        super.add(lineGraph);
        super.setVisible(true);
        super.setBounds(1000,400,512,512);
    }

}
