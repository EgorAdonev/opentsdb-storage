import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.data.xy.XYSeries;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FFTGrid {
    private JLabel fftLabel = new JLabel("");
    private JPanel spectrumGraph = new JPanel();

    private JPanel signalGraph = new JPanel();

    String title;


    public FFTGrid(String title){
        this.title = title;
    }


    OTSDBFFTransformer transformer = new OTSDBFFTransformer();
    double[] input = transformer.createSinWaveBuffer(50, 1000);
    double[] spectr = transformer.fft(input);
    private XYSeriesCollection spectrumCollection;
    private XYSeriesCollection xySeriesCollection;


//    public FFTGrid(){
//        super("Быстрое преобразование Фурье модуль для БД");
//        showLineGraph();
//        drawUI();
//    }

    private JPanel getSignalChartPanel(){

        XYSeries inputXY = new XYSeries("входной ряд");
        for (int i = 0; i < input.length; i++) {
            inputXY.add(i, input[i]);
        }
        xySeriesCollection = new XYSeriesCollection(inputXY);
        JFreeChart signalChart = ChartFactory.createXYLineChart("исходные данные", "время(мс)",
                "амплитуда", xySeriesCollection);

        ChartPanel signalChartPanel = new ChartPanel(signalChart);
        signalChartPanel.setFillZoomRectangle(true);
        signalChartPanel.setMouseWheelEnabled(true);

        return signalChartPanel;
    }
//    private JPanel getSpectrumChartPanel() {
////        JFreeChart jfc = ChartFactory.createLineChart("БПФ","частота", "амплитуда",valueDataset,
////                PlotOrientation.VERTICAL,false,true,false);
////        ChartFactory.createXYLineChart()
//
//        XYSeries xySeries = new XYSeries("амплитудный спектр");
//        for (int i = 0; i < spectr.length/2; i++) {
//            xySeries.add(i, spectr[i]);
//        }
//        spectrumCollection = new XYSeriesCollection(xySeries);
//        JFreeChart fftChart = ChartFactory.createXYLineChart("амплитудный спектр", "частота",
//                "амплитуда", spectrumCollection,PlotOrientation.VERTICAL,false,false,false );
//
//        ChartPanel spectrumChartPanel = new ChartPanel(fftChart);
//        spectrumChartPanel.setFillZoomRectangle(true);
//        spectrumChartPanel.setMouseWheelEnabled(true);
//
//        return spectrumChartPanel;
////        spectrumGraph.removeAll();
////        spectrumGraph.add(spectrumChartPanel, BorderLayout.EAST);
////        spectrumGraph.validate();
//
//    }


//    private void fillSpectrumChart() {
//        SwingWorker<Void, XYSeries> worker = new SwingWorker<Void, XYSeries>() {
//            @Override
//            protected Void doInBackground() throws Exception {
//                int numberOfElements = 1000;
////                XYSeries xySeries = new XYSeries("амплитудный спектр");
////                for (int i = 0; i < spectr.length / 2; i++) {
////                    xySeries.add(i, spectr[i]);
////                }
//                XYSeries series = null;
//                for (int y = 0; y < 12; y++) {
//                    series = new XYSeries("Plot " + y);
//                    for (int x = 0; x < numberOfElements; x++) {
//                        series.add(x, y); //add x,y point
//                    }
//                }
//                publish(series);
//                Thread.sleep(100);// just for animation purpose
//                return null;
//            }
//            @Override
//            protected void process(List<XYSeries> chunks) {
//                for(XYSeries series : chunks){
//                    spectrumCollection.addSeries(series);
//                }
//            }
//
//    };
//        worker.execute();
//    }

    private void fillSignalChart() {
        SwingWorker<Void, XYSeries> worker = new SwingWorker<Void, XYSeries>() {
            @Override
            protected Void doInBackground() throws Exception {
                int numberOfElements=1000;
                XYSeries inputXY = new XYSeries("входной ряд");
                for (int i = 0; i < input.length; i++) {
                    inputXY.add(i, input[i]);
                }
//                XYSeries series = null;
//                for (int y = 0; y < 12; y++) {
//                    series = new XYSeries("Plot " + y);
//                    for (int x = 0; x < numberOfElements; x++) {
//                        series.add(x, y); //add x,y point
//                    }
//                }
                publish(inputXY);
                Thread.sleep(100);// just for animation purpose
                return null;
            }
            @Override
            protected void process(List<XYSeries> chunks) {
                for(XYSeries series : chunks){
                    xySeriesCollection.addSeries(series);
                }
            }

        };
        worker.execute();
    }

    public void initSignalGUI(){
        JButton clearChart =  new JButton("Очистить график");
        clearChart.addActionListener(e -> xySeriesCollection.removeAllSeries());

        JButton fillChart = new JButton("Заполнить график") ;
        fillChart.addActionListener(e -> {
            xySeriesCollection.removeAllSeries();
            fillSignalChart();
        });

//        private void drawUI() {
//            //JFrame mainFrame = new JFrame("БПФ модуль для БД");
//            spectrumGraph.setForeground(new Color(24, 38, 176));
//            signalGraph.setForeground(new Color(176, 38, 24));
//            //red - new Color(255, 57, 0)
//            super.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
//            super.setFocusableWindowState(true);
//            super.setExtendedState(MAXIMIZED_BOTH);
//            super.add(spectrumGraph);
//            // super.add(chartGraph);
//            super.setLocationRelativeTo(null);
//            super.setVisible(true);
//        }

        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        controlPanel.add(clearChart);
        controlPanel.add(fillChart);

        JPanel content = new JPanel(new BorderLayout(5, 5));
        content.add(getSignalChartPanel(), BorderLayout.CENTER); //add the ChartPanel here
        content.add(controlPanel, BorderLayout.SOUTH);

        JFrame frame = new JFrame("БПФ модуль для БД");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().add(content);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

//    public void initFFTGUI(){
//
//        JButton clearChart =  new JButton("Очистить график");
//        clearChart.addActionListener(e -> spectrumCollection.removeAllSeries());
//
//        JButton fillChart = new JButton("Заполнить график") ;
//        fillChart.addActionListener(e -> {
//            spectrumCollection.removeAllSeries();
//            fillSpectrumChart();
//        });
//
//
//        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
//        controlPanel.add(clearChart);
//        controlPanel.add(fillChart);
//
//        JPanel content = new JPanel(new BorderLayout(5, 5));
//        content.add(getSpectrumChartPanel(), BorderLayout.CENTER); //add the ChartPanel here
//        content.add(controlPanel, BorderLayout.SOUTH);
//
//        JFrame frame = new JFrame("БПФ модуль для БД");
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.getContentPane().add(content);
//        frame.pack();
//        frame.setLocationRelativeTo(null);
//        frame.setVisible(true);
//    }

}
