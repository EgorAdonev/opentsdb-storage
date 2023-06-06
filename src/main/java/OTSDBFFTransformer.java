import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;

import java.util.List;


public class OTSDBFFTransformer {


    private static double[] toDoubleArray(List<Double> values) {
        double[] result = new double[values.size()];
        int i = 0;
        for (Double l : values)
            result[i++] = l;
        return result;
    }
    private static Complex[] toComplexArray(List<Complex> values) {
        Complex[] result = new Complex[values.size()];
        int i = 0;
        for (Complex l : values)
            result[i++] = l;
        return result;
    }
    static List<Double> values;

    protected static final int SAMPLE_RATE = 16 * 1024;
    private int samples;

    public double[] createSinWaveBuffer(double freq, int ms) {
        samples = (int)((ms * SAMPLE_RATE) / 1000);
        double[] output = new double[samples];

        double period = (double)SAMPLE_RATE / freq;
        for (int i = 0; i < output.length; i++) {
            double angle = 2.0 * Math.PI * i / period;
            output[i] = Math.sin(angle)/(1024*8);
        }

        return output;
    }
    public double[] createStepBuffer(int ms) {
        samples = (int)((ms * SAMPLE_RATE) / 1000);
        double[] output = new double[samples];

        for (int i = 0; i < output.length; i++) {
//            double angle = 2.0 * Math.PI * i / period;
            output[i] = 1;
            if(i%2>=samples/2) output[i] = 0;
        }
        return output;
    }

    public static void main(String[] args) {
        long nanoStart = System.nanoTime();
        long timeElapsed = System.nanoTime() - nanoStart;
        //System.out.println(Arrays.toString(spectrumValues)+'\n');
        System.out.println(timeElapsed);
    }
    double[] fft(double[] arr){
        FastFourierTransformer fft = new FastFourierTransformer(DftNormalization.STANDARD);
//        List<Double> collection = new ArrayList<>(samples);
////        Random rand = new Random();
//        for (int i = 0; i < samples; i++) {
//            collection.add(i, arr[i]);
//        }
        double[] spectrumValues = new double[arr.length];
        //Complex[] fftResult = fft.transform(toDoubleArray(collection), TransformType.FORWARD);
        double[] doubles = prefft(arr);
        Complex[] fftResult = fft.transform(doubles, TransformType.FORWARD);
        //вычисляем модуль квадратов действительной и мнимой части для построения амплитудного спектра
        for (int i = 0; i < arr.length; i++) {
            double rr = (fftResult[i].getReal());
            double ri = (fftResult[i].getImaginary());
            spectrumValues[i] = Math.sqrt((rr * rr) + (ri * ri));
        }
        return spectrumValues;
    }

    Object[] fft(Object[] arr){
        FastFourierTransformer fft = new FastFourierTransformer(DftNormalization.STANDARD);
//        List<Double> collection = new ArrayList<>(samples);
////        Random rand = new Random();
//        for (int i = 0; i < samples; i++) {
//            collection.add(i, arr[i]);
//        }
        Object[] objSpectrumValues = new Object[arr.length];
        //Complex[] fftResult = fft.transform(toDoubleArray(collection), TransformType.FORWARD);
        Object[] objects = prefft(arr);
        double[] doubles = new double[objects.length];
        for (int i = 0; i < objects.length; i++) {
            doubles[i] = Double.parseDouble(objects[i].toString().substring(1).split(",")[0]);
        }
        Complex[] fftResult = fft.transform(doubles, TransformType.FORWARD);
        //вычисляем модуль квадратов действительной и мнимой части для построения амплитудного спектра
        for (int i = 0; i < arr.length; i++) {
            double rr = (fftResult[i].getReal());
            double ri = (fftResult[i].getImaginary());
            objSpectrumValues[i] = Math.sqrt((rr * rr) + (ri * ri));
        }
        return objSpectrumValues;
    }

    private Object[] prefft(Object[] x) {
        int N = x.length;
        boolean b = true;
        int i = 0;
        while (b) {
            if (N == Math.pow(2 ,++i)) return x;
            else if (N < Math.pow(2 ,i)) b = false ;
        }
        Complex[] X = new Complex[( int ) Math.pow (2, i)];
        for (int  j = 0;j < Math.pow (2 ,i); j++) {
            if (j < N) X[j] = Complex.valueOf(Double.parseDouble(x[j].toString()));
            else  X [j] = Complex.ZERO;
        }
        return X;
    }

    public static Complex[] prefft(Complex[] x) {
        int N = x.length;
        boolean b = true;
        int i = 0;
        while (b) {
            if (N == Math.pow(2 ,++i)) return x;
            else if (N < Math.pow(2 ,i)) b = false ;
        }
        Complex[] X = new Complex[( int ) Math.pow (2, i)];
        for (int  j = 0;j < Math.pow (2 ,i); j++) {
            if (j < N) X[j] = x[j];
            else  X [j] = Complex.ZERO;
        }
        return X;
    }
    public static double[] prefft(double[] x) {
        int N = x.length;
        boolean b = true;
        int i = 0;
        while (b) {
            if (N == Math.pow(2 ,++i)) return x;
            else if (N < Math.pow(2 ,i)) b = false ;
        }
        double[] X = new double[(int) Math.pow(2,i)];
        for (int  j = 0;j < Math.pow (2 ,i); j++) {
            if (j < N) X[j] = x[j];
            else  X[j] = 0.0;
        }
        return X;
    }
//
//    public static Complex [] fft ( Complex [] x ) {
//        //FFT itself
//        Complex[] X;
//        int N = x.length;
//        if (N == 2) {
//            X = new Complex [ 2 ];
//            X [ 0 ] = x [ 0 ].add( x [ 1 ]);
//            X [ 1 ] = x [ 0 ].subtract( x [ 1 ]);
//        } else {
//            Complex [] x_even = new  Complex [ N / 2 ];
//            Complex [] x_odd = new  Complex [ N / 2 ];
//            for ( int  i = 0 ; i < N / 2 ; i ++) {
//                x_even [ i ] = x [ 2 * i ];
//                x_odd [ i ] = x [ 2 * i + 1 ];
//            }
//            Complex [] X_even = fft ( x_even );
//            Complex [] X_odd = fft ( x_odd );
//            X = new  Complex [ N ];
//            for (int i = 0; i < N / 2; i++) {
//                X [i] = X_even[i].add(W( i , N ).multiply(X_odd[i]));
//                X [i + N / 2] = X_even[i].subtract(W( i , N ).multiply(X_odd[i]));
//            }
//        }
//        return X;
//    }
//
//    public static Complex[] nfft(Complex[] X) {
//        // Centering the spectrum array
//        int  N = X . length ;
//        Complex [] X_n = new  Complex [ N ];
//        for ( int  i = 0 ; i < N / 2 ; i ++) {
//            X_n [ i ] = X [ i + N / 2 ];
//            X_n [ N / 2 + i ] = X [ i ];
//        }
//        return  X_n ;
//    }
// DFT opentsdb
//    @Override
//    public double runDouble(final Doubles values){
//      final int resultSize =  SAMPLE_RATE/2;
//      List<Double> collection = Lists.newArrayList();
//
//      while (values.hasNextValue()) {
//        collection.add(values.nextDoubleValue());
//      }
//      double[] result = toDoubleArray(collection);
//      double[] res = new double[resultSize*2];
//      if(result.length > resultSize * 2) {
//        int diff = result.length - resultSize * 2;
//        for (int i = diff; i < result.length ; i++) {
//          //fill the rest off signal with 0s
//          result[i] = 0.0;
//        }
//      }
//      for (int i = 0; i < res.length / 2; i++) {
//        int frequency = i;
//        for(int j = 0; j < result.length; j++){
//          res[2*i] += result[j] * dcos(j,frequency,SAMPLE_RATE);
//          res[2*i + 1] += result[j] * dsin(j,frequency,SAMPLE_RATE);
//        }
//        res[2*i] = res[2*i]/resultSize;
//        res[2*i+1] = res[2*i+1]/resultSize;
//      }
//      return res[0];
//    }
    //    private long cos(int index, int frequency, int sampleRate) {
//      //or round Math.round(double d)
//      return (long) Math.cos((2 * Math.PI * frequency * index) / sampleRate);
//    }
//
//    private long sin(int index, int frequency, int sampleRate) {
//      return (long) Math.sin((2 * Math.PI * frequency * index) / sampleRate);
//    }
//
//    private long[] toArray(List<Long> values) {
//      long[] result = new long[values.size()];
//      int i = 0;
//      for (Long l : values)
//        result[i++] = l;
//      return result;
//    }

//    @Override
//    public long runLong(final Longs values) {
//      final int resultSize = SAMPLE_RATE / 2;
//      final List<Long> collection = Lists.newArrayList();
//      while (values.hasNextValue()) {
//        collection.add(values.nextLongValue());
//      }
////      long[] result = collection.stream().mapToLong(l -> l).toArray();
//      long[] result = toArray(collection);
//      long[] res = new long[resultSize * 2];
//      if (result.length > resultSize * 2) {
//        int diff = result.length - resultSize * 2;
//        for (int i = diff; i < result.length; i++) {
//          //fill the rest off signal with 0s
//          result[i] = 0L;
//        }
//      }
//
//      //long currentA = values.nextLongValue();
//      long fourierSum = 0L;
//      for (int i = 0; i < res.length / 2; i++) {
//        int frequency = i;
//        for (int j = 0; j < result.length; j++) {
//          res[2 * i] += result[j] * cos(j, frequency, SAMPLE_RATE);
//          res[2 * i + 1] += result[j] * sin(j, frequency, SAMPLE_RATE);
//        }
//        res[2 * i] = res[2 * i] / resultSize;
//        res[2 * i + 1] = res[2 * i + 1] / resultSize;
//      }
//      return res[0];
//    }
//private double dcos(int index, int frequency, int sampleRate) {
//    return Math.cos((2 * Math.PI * frequency * index) / sampleRate);
//}
//
//    private double dsin(int index, int frequency, int sampleRate) {
//        return Math.sin((2 * Math.PI * frequency * index) / sampleRate);
//    }
}