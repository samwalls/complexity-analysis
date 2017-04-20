package uk.ac.st_andrews.complexity.fourier;

/*************************************************************************
 *  Compilation:  javac DFT.java
 *  Dependencies: Complex.java
 *  
 *************************************************************************/

public class DFT {
    public static Complex [] dft(Complex[] x) {
        int N = x.length;
        Complex [] result = new Complex [N];
        for (int k = 0; k < N; k++) {
            result [k] = new Complex (0,0);
            for (int n = 0; n < N; n++) {
                double knth = -2 * k * n * Math.PI / N;
                Complex wk = new Complex (Math.cos(knth), Math.sin(knth));
                Complex nth = x[n].times(wk);
                result[k] = result[k].plus(nth);
            }
        }
        return result;
    }
}
