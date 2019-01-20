package imaginaryquadraticinteger.Exceptions;

@Deprecated
@SuppressWarnings("all")
public class CoordinateSystemMismatchException extends IllegalArgumentException {
    
    private static final long serialVersionUID = 1;
    private final String expectedCoordinateSystemExplanation;
    
    /**
     * Just to clarify where the coordinate system expected (0, 0) to be at
     * @return A string saying either "Expected coordinate system has (0, 0) at the center" or "Expected coordinate system has (0, 0) at the top left corner."
     */
    public String getExpectedCoordinateSystemExplanation() {
        return expectedCoordinateSystemExplanation;
    }
    
    /**
     * An exception to be thrown when a function receives coordinates which are outside the expected range.
     * @param message A message to pass on to IllegalArgumentException.
     * @param centeredZeroFlag A boolean specifying whether (0, 0) is at the center of the coordinate system (true) or not (false). expectedCoordinateSystemExplanation is set accordingly.
     */
    public CoordinateSystemMismatchException(String message, boolean centeredZeroFlag) {
        
        super(message);
        if (centeredZeroFlag) {
            expectedCoordinateSystemExplanation = "Expected coordinate system has (0, 0) at the center.";
        } else {
            expectedCoordinateSystemExplanation = "Expected coordinate system has (0, 0) at the top left corner.";
        }
        
    }
    
}
