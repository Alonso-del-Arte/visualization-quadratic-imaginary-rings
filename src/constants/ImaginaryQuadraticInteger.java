package constants;

public class ImaginaryQuadraticInteger {

    private ImaginaryQuadraticInteger() {

    }

    /**
     * The number to determine which imaginary quadratic integer ring diagram
     * will be shown at start-up. This constant corresponds to the ring of
     * Gaussian integers.
     */
    public static final int DEFAULT_RING_D = -1;

    /**
     * The minimum integer the square root of which can be used to generate an
     * imaginary quadratic integer ring for the purpose of display in this ring
     * window. Although technically an ImaginaryQuadraticRing can be defined
     * with the square root of -2147483647 (which is a prime number), this would
     * quickly lead to arithmetic overflow problems. Hopefully this value of
     * 8191 (also a prime) is "small" enough not to cause arithmetic overflow
     * problems with the largest zoom out setting, but "large" enough to be of
     * no interest to most users of this program.
     */
    public static final int MINIMUM_RING_D = -8191;

}
