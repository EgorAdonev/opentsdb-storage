import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Arrays;

public class Main {
    static FFTGrid fftGrid;

    static {
        try {
            fftGrid = new FFTGrid("JFreeChart #1");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws IOException {

        //дизайн с диалоговым окном для введения директории - готово
        //фильтрацию по тэгам и по метрикам
        //список экспериментов
        //выбор каналов
        //дату в таймстемп
//        FFTGrid fftGrid = new FFTGrid();
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    fftGrid.initGUI();
                    fftGrid.initFFTGUI();
                } catch (IOException e) {
                    System.out.println(Arrays.toString(e.getStackTrace()));
                    throw new RuntimeException(e);
                }
            }
        });
    }
}
