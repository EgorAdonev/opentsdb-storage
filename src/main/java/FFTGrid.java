import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.data.xy.XYSeries;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FFTGrid implements ItemListener {


    String title;
    private static int W = 500;
    private static int H = 500;
    private File dir = new File("C:\\Users\\egodo\\Downloads\\experiment_rez");
    private static OTSDBFFTransformer transformer = new OTSDBFFTransformer();
    private static NumberConverter numConverter = new NumberConverter();
    private static JSplitPane splitPane = new JSplitPane();
    private final JFileChooser fc = new JFileChooser();
    private Object[][] input = new Object[(int) Arrays.stream(dir.listFiles()).filter(file -> file.getName().endsWith(".csv")).count()][];
    private Object[][] spectr = new Object[input.length][];

    private int seriesCount;
    private XYSeriesCollection[] xySeriesCollection;
    private XYSeriesCollection[] spectrumCollection;
    private JFreeChart[] signalCharts;
    private XYSeries[] seriesArr;
    private ChartPanel[] signalChartPanels;
    private JButton chooseExpDir;
    private XYSeries[] spectrumArr;
    private JFreeChart[] spectrumCharts;
    private ChartPanel[] spectrumChartPanels;
    private JCheckBox ch1;
    private JCheckBox ch2;
    private JCheckBox ch3;
    private JCheckBox ch4;
    private JCheckBox ch5;
    private JCheckBox ch6;
    private JCheckBox ch7;
    private JCheckBox ch8;
    private HashMap<String, String> channelMap = new HashMap<String, String>();
    private JCheckBox[] channelCheckboxes;

    public FFTGrid(String title) throws IOException {
        this.title = title;
        populateInputArray();
        populateSpectrumArray();
    }


//    double[] spectr = transformer.fft(input);
    //    double[] input = transformer.createSinWaveBuffer(50, 1000);
    private void fillSignalChart() {
    SwingWorker<Void, XYSeries> worker = new SwingWorker<Void, XYSeries>() {
        @Override
        protected Void doInBackground() throws Exception {
            int numberOfElements = 1000;
//            seriesArr = new XYSeries[input.length];
            for (int i = 0; i < input.length; i++) {
                seriesArr[i] = new XYSeries("входной ряд " + i);
                Thread.sleep(100);// just for animation purpose
                for (int j = 0; j < input[i].length; j++) {
                    seriesArr[i].add(j, (Number) input[i][j]);
                }
                publish(seriesArr[i]);
            }
            return null;
        }
        @Override
        protected void process(List<XYSeries> chunks) {
            for(XYSeries series : chunks){
                for (int i = 0; i < input.length; i++) {
                    xySeriesCollection[i].addSeries(series);
                }
            }
        }

    };
    worker.execute();
}
    public JPanel initGUI() throws IOException {
        fillSignalChart();
        JButton clearChart =  new JButton("Очистить график");
        clearChart.addActionListener(e -> {
            for (int i = 0; i < input.length; i++) {
                this.xySeriesCollection[i].removeAllSeries();
            }
        });
        this.seriesArr = new XYSeries[input.length];
        for (int i = 0; i < input.length; i++) {
            seriesArr[i] = new XYSeries("входной ряд " + i);
            for (int j = 0; j < input[i].length; j++) {
                seriesArr[i].add(j, (Number) input[i][j]);
            }
        }
        this.seriesCount=seriesArr.length;
        this.xySeriesCollection = new XYSeriesCollection[seriesCount];
        for (int i = 0; i < seriesArr.length; i++) {
            xySeriesCollection[i] = new XYSeriesCollection(seriesArr[i]);
        }
        JButton fillChart = new JButton("Заполнить график");
        fillChart.addActionListener(e -> {
            for (int i = 0; i < input.length; i++) {
                xySeriesCollection[i].removeAllSeries();
            }
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
        //In response to a button click:
        chooseExpDir = new JButton("Выбрать папку экспериментов");
        chooseExpDir.addActionListener(e -> {
            if (e.getSource() == chooseExpDir) {
                if (fc.showOpenDialog(this.chooseExpDir) == JFileChooser.APPROVE_OPTION) {
                    File file = fc.getSelectedFile();
                    System.out.println("Opening: " + file.getName() + ".");
                } else {
                    System.out.println("Open command cancelled by user.");
                }
            }
        });
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
//        fc.setCurrentDirectory(dir);
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        controlPanel.add(clearChart);
        controlPanel.add(fillChart);
        controlPanel.add(chooseExpDir);

        JPanel content = new JPanel(new BorderLayout(5,5));
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        for (int i = 0; i < input.length; i++) {
            content.add(getSignalChartPanel()[i], BorderLayout.CENTER); //add the ChartPanel here
        }
        mainPanel.add(content);
        mainPanel.add(controlPanel);
        JFrame frame = new JFrame("Сигналы с выходных файлов прибора");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(mainPanel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
//        String s = (String)JOptionPane.showInputDialog(
//                mainPanel,
//                "выберите каналы",
//                "выберите каналы",
//                JOptionPane.PLAIN_MESSAGE,
//                UIManager.getIcon("FileView.fileIcon"),
//                new String[]{"1","2","3","4","5","6","7","8"},
//                null);
        return mainPanel;
    }
    public JPanel initFFTGUI() throws IOException {
        fillSpectrumChart();
        JButton clearChart =  new JButton("Очистить спектр");
        clearChart.addActionListener(e -> {
            for (int i = 0; i < input.length; i++) {
                this.spectrumCollection[i].removeAllSeries();
            }
        });

        this.spectrumArr = new XYSeries[input.length];
        for (int i = 0; i < input.length; i++) {
            spectrumArr[i] = new XYSeries("амплитудный спектр" + i);
            for (int j = 0; j < input[i].length; j++) {
                spectrumArr[i].add(j, (Number) input[i][j]);
            }
        }

        this.spectrumCollection = new XYSeriesCollection[spectrumArr.length];
        for (int i = 0; i < spectrumArr.length; i++) {
            spectrumCollection[i] = new XYSeriesCollection(spectrumArr[i]);
        }
        JButton fillChart = new JButton("Заполнить спектр");
        fillChart.addActionListener(e -> {
            for (int i = 0; i < input.length; i++) {
                spectrumCollection[i].removeAllSeries();
            }
            fillSpectrumChart();
        });
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        controlPanel.add(clearChart);
        controlPanel.add(fillChart);

        channelCheckboxes = new JCheckBox[channelMap.entrySet().size()];
        int boxCount = 0;
        for (Map.Entry<String,String> entry : channelMap.entrySet()) {
            channelCheckboxes[boxCount] = new JCheckBox("ch" + entry.getValue());
            channelCheckboxes[boxCount].setSelected(true);
            channelCheckboxes[boxCount].addItemListener(this);
            System.out.println(entry.getValue());
            boxCount++;
        }

        JPanel content = new JPanel(new BorderLayout(5, 5));
        for (int i = 0; i < input.length; i++) {
            content.add(getSpectrumChartPanel()[i], BorderLayout.CENTER); //add the ChartPanel here
            controlPanel.add(channelCheckboxes[i]);
        }
        content.add(controlPanel, BorderLayout.SOUTH);
        JFrame frame = new JFrame("БПФ");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(content);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);


        return content;
        //In initialization code:

//         ch2 = new JCheckBox("ch2");
//        ch2.setSelected(true);
//
//         ch3 = new JCheckBox("ch3");
//        ch3.setSelected(true);
//
//         ch4 = new JCheckBox("ch4");
//        ch4.setSelected(true);
//
//         ch5 = new JCheckBox("ch5");
//        ch5.setSelected(true);
//
//         ch6 = new JCheckBox("ch6");
//        ch6.setSelected(true);
//
//         ch7 = new JCheckBox("ch7");
//        ch7.setSelected(true);
//
//         ch8 = new JCheckBox("ch8");
//        ch8.setSelected(true);

//        ch1.addItemListener(this);
//        ch2.addItemListener(this);
//        ch3.addItemListener(this);
//        ch4.addItemListener(this);
//        ch5.addItemListener(this);
//        ch6.addItemListener(this);
//        ch7.addItemListener(this);
//        ch8.addItemListener(this);
    }
    public void itemStateChanged(ItemEvent e) {
        Object source = e.getItemSelectable();

        if (source == channelCheckboxes[0]) {

            xySeriesCollection[0].removeAllSeries();
            spectrumCollection[0].removeAllSeries();
            spectrumCollection[0].addSeries(spectrumArr[0]);
            xySeriesCollection[0].addSeries(seriesArr[0]);
        } else if (source == channelCheckboxes[1]) {
            xySeriesCollection[1].removeAllSeries();
            spectrumCollection[1].removeAllSeries();
            spectrumCollection[1].addSeries(spectrumArr[1]);
            xySeriesCollection[1].addSeries(seriesArr[1]);
        } else if (source == channelCheckboxes[2]) {
            xySeriesCollection[2].removeAllSeries();
            spectrumCollection[2].removeAllSeries();
            spectrumCollection[2].addSeries(spectrumArr[2]);
            xySeriesCollection[2].addSeries(seriesArr[2]);
        } else if (source == channelCheckboxes[3]) {
            xySeriesCollection[3].removeAllSeries();
            spectrumCollection[3].removeAllSeries();
            spectrumCollection[3].addSeries(spectrumArr[3]);
            xySeriesCollection[3].addSeries(seriesArr[3]);
        } else if (source == channelCheckboxes[4]) {
            xySeriesCollection[4].removeAllSeries();
            spectrumCollection[4].removeAllSeries();
            spectrumCollection[4].addSeries(spectrumArr[4]);
            xySeriesCollection[4].addSeries(seriesArr[4]);
        } else if (source == channelCheckboxes[5]) {
            xySeriesCollection[5].removeAllSeries();
            spectrumCollection[5].removeAllSeries();
            spectrumCollection[5].addSeries(spectrumArr[5]);
            xySeriesCollection[5].addSeries(seriesArr[5]);
        } else if (source == channelCheckboxes[6]) {
            xySeriesCollection[6].removeAllSeries();
            spectrumCollection[6].removeAllSeries();
            spectrumCollection[6].addSeries(spectrumArr[6]);
            xySeriesCollection[6].addSeries(seriesArr[6]);
        } else if (source == channelCheckboxes[7]) {
            xySeriesCollection[7].removeAllSeries();
            spectrumCollection[7].removeAllSeries();
            spectrumCollection[7].addSeries(spectrumArr[7]);
            xySeriesCollection[7].addSeries(seriesArr[7]);
        }

        if (e.getStateChange() == ItemEvent.DESELECTED && source == channelCheckboxes[0]) {
            xySeriesCollection[0].removeAllSeries();
            spectrumCollection[0].removeAllSeries();
        } else if (e.getStateChange() == ItemEvent.DESELECTED && source == channelCheckboxes[1]) {
            xySeriesCollection[1].removeAllSeries();
            spectrumCollection[1].removeAllSeries();
        } else if (e.getStateChange() == ItemEvent.DESELECTED && source == channelCheckboxes[2]) {
            xySeriesCollection[2].removeAllSeries();
            spectrumCollection[2].removeAllSeries();
        } else if (e.getStateChange() == ItemEvent.DESELECTED && source == channelCheckboxes[3]) {
            xySeriesCollection[3].removeAllSeries();
            spectrumCollection[3].removeAllSeries();
        } else if (e.getStateChange() == ItemEvent.DESELECTED && source == channelCheckboxes[4]) {
            xySeriesCollection[4].removeAllSeries();
            spectrumCollection[4].removeAllSeries();
        } else if (e.getStateChange() == ItemEvent.DESELECTED && source == channelCheckboxes[5]) {
            xySeriesCollection[5].removeAllSeries();
            spectrumCollection[5].removeAllSeries();
        } else if (e.getStateChange() == ItemEvent.DESELECTED && source == channelCheckboxes[6]) {
            xySeriesCollection[6].removeAllSeries();
            spectrumCollection[6].removeAllSeries();
        } else if (e.getStateChange() == ItemEvent.DESELECTED && source == channelCheckboxes[7]) {
            xySeriesCollection[7].removeAllSeries();
            spectrumCollection[7].removeAllSeries();
        }

    }
    public void populateInputArray() throws IOException {
        if(dir.isDirectory()) {
            File[] directoryListing = dir.listFiles();
            if (directoryListing != null) {
                int i = 0;
                for (File child : directoryListing) {
                    if (child.getName().endsWith(".csv")) {
                        channelMap.put(String.valueOf(i), child.getName().split("\\.")[1].substring(2));
                        input[i] = NumberConverter.readCsvFileFromHardware(child).toArray();
                        i++;
                }
                }
            }
        }
    }

    public void populateSpectrumArray() throws IOException {
        for (int i = 0; i < input.length; i++) {
            for (int j = 0; j < input[i].length; j++) {
                spectr[i]=transformer.fft(input[i]);
            }
        }
    }


    private ChartPanel[] getSignalChartPanel() throws IOException {

        this.seriesArr = new XYSeries[input.length];
        for (int i = 0; i < input.length; i++) {
            seriesArr[i] = new XYSeries("входной ряд" + i);
            seriesArr[i].setDescription("");
            for (int j = 0; j < input[i].length; j++) {
                seriesArr[i].add(j, (Number) input[i][j]);
            }
        }

        for (int i = 0; i < seriesArr.length; i++) {
            this.xySeriesCollection[i] = new XYSeriesCollection(seriesArr[i]);
        }

        this.signalCharts = new JFreeChart[xySeriesCollection.length];
        for (int i = 0; i < xySeriesCollection.length; i++) {
            signalCharts[i] = ChartFactory.createXYLineChart("исходные данные", "время(мс)",
                    "амплитуда", xySeriesCollection[i]);
            switch(i){
                case 0:
                    signalCharts[i].setBorderPaint(Color.red);
                case 1:
                    signalCharts[i].setBorderPaint(Color.green);
                case 2:
                    signalCharts[i].setBorderPaint(Color.blue);
                case 3:
                    signalCharts[i].setBorderPaint(Color.orange);
                case 4:
                    signalCharts[i].setBorderPaint(Color.magenta);
                case 5:
                    signalCharts[i].setBorderPaint(Color.cyan);
                case 6:
                    signalCharts[i].setBorderPaint(Color.darkGray);
                case 7:
                    signalCharts[i].setBorderPaint(Color.pink);
            }
        }
        this.signalChartPanels = new ChartPanel[signalCharts.length];
        for (int i = 0; i < signalCharts.length; i++) {
            signalChartPanels[i] = new ChartPanel(signalCharts[i], W, H, W, H, W, H,
                    false, true, true, true, true, true);
            signalChartPanels[i].setFillZoomRectangle(true);
            signalChartPanels[i].setMouseWheelEnabled(true);
        }


        return signalChartPanels;
    }
    private ChartPanel[] getSpectrumChartPanel() throws IOException {
//        JFreeChart jfc = ChartFactory.createLineChart("БПФ","частота", "амплитуда",valueDataset,
//                PlotOrientation.VERTICAL,false,true,false);
//        ChartFactory.createXYLineChart()
        this.spectrumArr = new XYSeries[input.length];
        for (int i = 0; i < input.length; i++) {
            spectrumArr[i] = new XYSeries("амплитудный спектр" + i);
            spectrumArr[i].setDescription("");
            for (int j = 0; j < input[i].length/2; j++) {
                spectrumArr[i].add(j, (Number) spectr[i][j]);
            }
        }
        this.spectrumCollection = new XYSeriesCollection[spectrumArr.length];
        for (int i = 0; i < spectrumArr.length; i++) {
            this.spectrumCollection[i] = new XYSeriesCollection(spectrumArr[i]);
        }

        this.spectrumCharts = new JFreeChart[spectrumCollection.length];

        for (int i = 0; i < spectrumCollection.length; i++) {
            spectrumCharts[i] = ChartFactory.createXYLineChart("амплитудный спектр", "частота",
                    "амплитуда", spectrumCollection[i],PlotOrientation.VERTICAL,false,false,false );
            switch(i){
                case 0:
                    spectrumCharts[i].setBorderPaint(Color.red);
                case 1:
                    spectrumCharts[i].setBorderPaint(Color.green);
                case 2:
                    spectrumCharts[i].setBorderPaint(Color.blue);
                case 3:
                    spectrumCharts[i].setBorderPaint(Color.orange);
                case 4:
                    spectrumCharts[i].setBorderPaint(Color.magenta);
                case 5:
                    spectrumCharts[i].setBorderPaint(Color.cyan);
                case 6:
                    spectrumCharts[i].setBorderPaint(Color.darkGray);
                case 7:
                    spectrumCharts[i].setBorderPaint(Color.pink);
            }
        }
        this.spectrumChartPanels = new ChartPanel[spectrumCharts.length];
        for (int i = 0; i < spectrumCharts.length; i++) {
            spectrumChartPanels[i] = new ChartPanel(spectrumCharts[i], W, H, W, H, W, H,
                    false, true, true, true, true, true);
            spectrumChartPanels[i].setFillZoomRectangle(true);
            spectrumChartPanels[i].setMouseWheelEnabled(true);
        }
        return spectrumChartPanels;
    }


    private void fillSpectrumChart() {
        SwingWorker<Void, XYSeries> worker = new SwingWorker<Void, XYSeries>() {
            @Override
            protected Void doInBackground() throws Exception {
                int numberOfElements = 1000;
//            seriesArr = new XYSeries[input.length];
                for (int i = 0; i < input.length; i++) {
                    spectrumArr[i] = new XYSeries("входной ряд" + i);
                    Thread.sleep(100);// just for animation purpose
                    for (int j = 0; j < spectr[i].length/2; j++) {
                        spectrumArr[i].add(j, (Number) spectr[i][j]);
                    }
                    publish(spectrumArr[i]);
                }
                return null;
            }
            @Override
            protected void process(List<XYSeries> chunks) {
                for(XYSeries series : chunks){
                    for (int i = 0; i < input.length; i++) {
                        spectrumCollection[i].addSeries(series);
                    }
                }
            }

    };
        worker.execute();
    }



}
