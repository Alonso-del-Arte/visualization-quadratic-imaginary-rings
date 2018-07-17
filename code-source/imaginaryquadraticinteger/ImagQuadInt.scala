package imaginaryquadraticinteger

/** An imaginary quadratic integer represented symbolically rather than numerically.
  *
  * @constructor Essentially wraps an [[imaginaryquadraticinteger.ImaginaryQuadraticInteger ImaginaryQuadraticInteger]]
  *              into an object with overloaded operators.
  * @param a The real part of the imaginary quadratic integer, multiplied by 2 if necessary.
  * @param b The imaginary part of the imaginary quadratic integer, multiplied by 2 if necessary.
  * @param R The imaginary quadratic ring, may be
  *          [[imaginaryquadraticinteger.ImaginaryQuadraticRing ImaginaryQuadraticRing]] but preferably
  *          [[imaginaryquadraticinteger.ImagQuadRing ImagQuadRing]].
  * @param denom The denominator, 1 or 2.
  */
class ImagQuadInt(a: Int, b: Int, R: ImaginaryQuadraticRing, denom: Int = 1)
  extends ImaginaryQuadraticInteger(a: Int, b: Int, R: ImaginaryQuadraticRing, denom: Int) {

  /** Gives a text representation of the imaginary quadratic integer, with the real part first and the imaginary part
    * second. Essentially overrides the superclass toString in order to change the radical character to "sqrt". This
    * text representation can be used in a wide variety of contexts, but most importantly in the Scala REPL.
    *
    * @return A String representing the imaginary quadratic integer which can be used in the Scala REPL.
    */
  override def toString: String = super.toString.replace("\u221A", "sqrt")

  /** Computes the conjugate of the given imaginary quadratic integer.
    *
    * @return The conjugate. For example, given 5/2 + sqrt(-7)/2, the conjugate would be 5/2 - sqrt(-7)/2.
    */
  override def conjugate: ImagQuadInt = new ImagQuadInt(this.a, -this.b, this.R, this.denom)

  /** Gets the imaginary quadratic ring which this imaginary quadratic integer belongs to.
    *
    * @return An ImagQuadRing object, which can then be queried for its negRad, absNegRad and absNegRadSqrt
    *         values, and a couple other properties.
    */
  override def getRing: ImagQuadRing = new ImagQuadRing(this.R.negRad)

  /** Adds an ImagQuadInt to this one (overloaded operator for ImaginaryQuadraticInteger.plus().
    *
    * @param summand The ImagQuadInt to add.
    * @return The result of the addition.
    */
  def +(summand: ImagQuadInt): ImagQuadInt = {
    val temp = this.plus(summand)
    new ImagQuadInt(temp.realPartMult, temp.imagPartMult, temp.imagQuadRing, temp.denominator)
  }

  /** Adds an Int to this ImagQuadInt (overloaded operator for ImaginaryQuadraticInteger.plus().
    *
    * @param summand The Int to add, a purely real integer.
    * @return The result of the addition.
    */
  def +(summand: Int): ImagQuadInt = {
    val temp = this.plus(summand)
    new ImagQuadInt(temp.realPartMult, temp.imagPartMult, temp.imagQuadRing, temp.denominator)
  }

  /** Subtracts an ImagQuadInt from this one (overloaded operator for ImaginaryQuadraticInteger.minus().
    *
    * @param subtrahend The ImagQuadInt to subtract.
    * @return The result of the subtraction.
    */
  def -(subtrahend: ImagQuadInt): ImagQuadInt = {
    val temp = this.minus(subtrahend)
    new ImagQuadInt(temp.realPartMult, temp.imagPartMult, temp.imagQuadRing, temp.denominator)
  }

  /** Subtracts an Int from this ImagQuadInt (overloaded operator for ImaginaryQuadraticInteger.minus().
    *
    * @param subtrahend The Int to subtract, a purely real integer.
    * @return The result of the subtraction.
    */
  def -(subtrahend: Int): ImagQuadInt = {
    val temp = this.minus(subtrahend)
    new ImagQuadInt(temp.realPartMult, temp.imagPartMult, temp.imagQuadRing, temp.denominator)
  }

  /** Multiplies an ImagQuadInt by this one (overloaded operator for ImaginaryQuadraticInteger.times().
    *
    * @param multiplicand The ImagQuadInt to multiply by.
    * @return The result of the multiplication.
    */
  def *(multiplicand: ImagQuadInt): ImagQuadInt = {
    val temp = this.times(multiplicand)
    new ImagQuadInt(temp.realPartMult, temp.imagPartMult, temp.imagQuadRing, temp.denominator)
  }

  /** Multiplies an Int by this ImagQuadInt (overloaded operator for ImaginaryQuadraticInteger.times().
    *
    * @param multiplicand The Int to multiply by.
    * @return The result of the multiplication.
    */
  def *(multiplicand: Int): ImagQuadInt = {
    val temp = this.times(multiplicand)
    new ImagQuadInt(temp.realPartMult, temp.imagPartMult, temp.imagQuadRing, temp.denominator)
  }

  /** Divides this ImagQuadInt by another (overloaded operator for ImaginaryQuadraticInteger.divides().
    *
    * @param divisor The ImagQuadInt to divide by.
    * @return The result of the division.
    */
  def /(divisor: ImagQuadInt): ImagQuadInt = {
    val temp = this.divides(divisor)
    new ImagQuadInt(temp.realPartMult, temp.imagPartMult, temp.imagQuadRing, temp.denominator)
  }

  /** Divides this ImagQuadInt by an Int (overloaded operator for ImaginaryQuadraticInteger.divides().
    *
    * @param divisor The Int to divide by.
    * @return
    */
  def /(divisor: Int): ImagQuadInt = {
    val temp = this.divides(divisor)
    new ImagQuadInt(temp.realPartMult, temp.imagPartMult, temp.imagQuadRing, temp.denominator)
  }

}