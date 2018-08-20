package imaginaryquadraticinteger

import org.junit.Test
import org.junit.Assert._
import org.

class ImagQuadIntTest {

  val ring = new ImagQuadRing(-2)

  val operand1 = new ImagQuadInt(3, 5, ring)
  val operand2 = new ImagQuadInt(4, 2, ring)

  /** Test of + method of class ImagQuadInt.
    */
  @Test def testPlus(): Unit = {
    println("operator+")
    val expResult = new ImagQuadInt(7, 7, ring)
    val result = operand1 + operand2
    assertEquals(expResult, result)
  }

  /** Test of - method of class ImagQuadInt.
    */
  @Test def testMinus(): Unit = {
    println("operator-")
    val expResult = new ImagQuadInt(-1, 3, ring)
    val result = operand1 - operand2
    assertEquals(expResult, result)
  }

  /** Test of * method of class ImagQuadInt.
    */
  @Test def testTimes(): Unit = {
    println("operator*")
    val expResult = new ImagQuadInt(-8, 26, ring)
    val result = operand1 * operand2
    assertEquals(expResult, result)
  }

  /** Test of / method of class ImagQuadInt.
    */
  @Test def testDivides(): Unit = {
    println("operator/")
    val dividend = new ImagQuadInt(14, 0, ring)
    val divisor = new ImagQuadInt(0, 1, ring)
    val expResult = new ImagQuadInt(0, -7, ring)
    val result = dividend / divisor
    assertEquals(expResult, result)
  }

  @Test def testAddConjugate(): Unit = {
    println("Addition of a number to its conjugate")
    var conjugate = new ImagQuadInt(3, -5, ring)
    var expResult = new ImagQuadInt(6, 0, ring)
    var result = operand1 + conjugate
    assertEquals(expResult, result)
    conjugate = new ImagQuadInt(4, -2, ring)
    expResult = new ImagQuadInt(8, 0, ring)
    result = operand2 + conjugate
    assertEquals(expResult, result)
  }

  @Test def testSubtractNumberFromItself(): Unit = {
    println("Subtraction of number from itself")
    val expResult = new ImagQuadInt(0, 0, ring)
    val result = operand2 - operand2
    assertEquals(expResult, result)
  }

  @Test(expected = classOf[AlgebraicDegreeOverflowException]) def testTimesDiffRings(): Unit = {
    println("Multiplication by number from another ring")
    val numFromDiffRing = new ImagQuadInt(1, 1, new ImagQuadRing(-5))
    operand1 * numFromDiffRing
  }

  @Test def testDivideByZero(): Unit = {
    print("Division by zero")
    val zero = new ImagQuadInt(0, 0, ring)
    try {
      operand2 / zero
    } catch {
      case iae: IllegalArgumentException => println(" caused IllegalArgumentException \"" + iae.getMessage + "\"")
      case ae: ArithmeticException => println(" caused ArithmeticException \"" + ae.getMessage + "\"")
      case e: Exception => println(" caused " + e.getClass + " \"" + e.getMessage + "\"")
        fail("Division by zero should not have caused " + e.getClass)
    }
  }

}
