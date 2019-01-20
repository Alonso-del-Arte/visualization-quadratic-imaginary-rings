package com.alonsodelarte.quadraticRings.imaginaryquadraticinteger

object NTFC extends NumberTheoreticFunctionsCalculator {

  def primeFactors(num: ImagQuadInt): java.util.List[ImagQuadInt] = {
    val temp = NumberTheoreticFunctionsCalculator.primeFactors(num)
    val primes = new java.util.ArrayList[ImagQuadInt]
    for (n <- 0 until temp.size()) {
      primes.add(new ImagQuadInt(temp.get(n).getRealPartMult, temp.get(n).getImagPartMult, num.getRing, temp.get(n).getDenominator))
    }
    primes
  }

  def euclideanGCD(a: ImagQuadInt, b: ImagQuadInt): ImagQuadInt = {
    val temp = NumberTheoreticFunctionsCalculator.euclideanGCD(a, b)
    new ImagQuadInt(temp.getRealPartMult, temp.imagPartMult, a.getRing, temp.getDenominator)
  }

  def euclideanGCD(a: Int, b: ImagQuadInt): ImagQuadInt = {
    val temp = NumberTheoreticFunctionsCalculator.euclideanGCD(a, b)
    new ImagQuadInt(temp.getRealPartMult, temp.imagPartMult, b.getRing, temp.getDenominator)
  }

  def euclideanGCD(a: ImagQuadInt, b: Int): ImagQuadInt = {
    val temp = NumberTheoreticFunctionsCalculator.euclideanGCD(a, b)
    new ImagQuadInt(temp.getRealPartMult, temp.imagPartMult, a.getRing, temp.getDenominator)
  }

}
