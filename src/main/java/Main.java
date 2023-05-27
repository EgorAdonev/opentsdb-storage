import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        int arr[] = new int[(1024/4)*1000000];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = i;
        }
// список экспериментов
        //дизайн с диалоговым окном для введения директории
        //фильтрацию по тэгам и по метрикам
        // дату в таймстемп
//        FFTGrid fftGrid = new FFTGrid();
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new FFTGrid("JFreeChart #1").initSignalGUI();
//                new FFTGrid("JFreeChart #2").initFFTGUI();
            }
        });
    }
}
