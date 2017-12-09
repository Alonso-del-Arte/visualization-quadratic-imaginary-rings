I HAVE DELETED THIS FILE FROM MY LOCAL COPY OF THE PROJECT BUT WILL KEEP THIS FILE HERE FOR HISTORICAL PURPOSES.

public class ImagQuadrIntDisplayComplexPlanePoint extends ImaginaryQuadraticInteger {
    
    protected int coordinateX;
    protected int coordinateY;
    
    /**
     * Gives the x-coordinate in the pixel coordinate system for the display of the imaginary quadratic integer as as a point in the complex plane
     * @return A nonnegative integer which can be used to display the point
     */
    public int getCoordinateX() {
        return coordinateX;
    }
    
    /**
     * Gives the y-coordinate in the pixel coordinate system for the display of the imaginary quadratic integer as as a point in the complex plane
     * @return A nonnegative integer which can be used to display the point
     */
    public int getCoordinateY() {
        return coordinateY;
    }
    
    /**
     * Class constructor
     * @param a The real part of the imaginary quadratic integer, multiplied by 2 when applicable
     * @param b The part to be multiplied by sqrt(d), multiplied by 2 when applicable
     * @param R The ring to which this algebraic integer belongs to
     * @param denom In most cases 1, but may be 2 if a and b have the same parity and d = 1 mod 4
     * @param coordX The x-coordinate in a pixel coordinate system
     * @param coordY The y-coordinate in a pixel coordinate system
     */
    public ImagQuadrIntDisplayComplexPlanePoint(int a, int b, ImaginaryQuadraticRing R, int denom, int coordX, int coordY) {
        super(a, b, R, denom);
        coordinateX = coordX;
        coordinateY = coordY;
    }
    
}
