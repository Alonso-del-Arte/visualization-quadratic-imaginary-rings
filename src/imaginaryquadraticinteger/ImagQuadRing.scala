package imaginaryquadraticinteger

/** Represents the imaginary quadratic ring of Q(sqrt(d)), where d is a negative, squarefree integer.
  *
  * @param d A squarefree negative integer, the square root of which is adjoined to Q to form a ring of algebraic
  *          integers.
  */
class ImagQuadRing(d: Int) extends ImaginaryQuadraticRing(d: Int) {

  override def toString: String = super.toASCIIString

}
