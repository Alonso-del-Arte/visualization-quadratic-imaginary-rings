/*
 * Copyright (C) 2017 AL
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package imaginaryquadraticinteger;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author AL
 */
public class ImaginaryQuadraticRingTest {
    
    public static ImaginaryQuadraticRing ringGaussian;
    public static ImaginaryQuadraticRing ringZ2;
    public static ImaginaryQuadraticRing ringEisenstein;
    public static ImaginaryQuadraticRing ringOQi7;
    
    public ImaginaryQuadraticRingTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
        ringGaussian = new ImaginaryQuadraticRing(-1);
        ringZ2 = new ImaginaryQuadraticRing(-2);
        ringEisenstein = new ImaginaryQuadraticRing(-3);
        ringOQi7 = new ImaginaryQuadraticRing(-7);
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of preferBlackboardBold method, of class ImaginaryQuadraticRing.
     * Without arguments, preferBlackboardBold is the getter method.
     */
    @Test
    public void testPreferBlackboardBold_0args() {
        System.out.println("preferBlackboardBold, no arguments");
        ImaginaryQuadraticRing.preferBlackboardBold(true);
        assertTrue(ImaginaryQuadraticRing.preferBlackboardBold());
        ImaginaryQuadraticRing.preferBlackboardBold(false);
        assertFalse(ImaginaryQuadraticRing.preferBlackboardBold());
    }

    /**
     * Test of preferBlackboardBold method, of class ImaginaryQuadraticRing.
     * With arguments, preferBlackboardBold is the setter method.
     */
    @Test
    public void testPreferBlackboardBold_boolean() {
        System.out.println("preferBlackboardBold, with boolean argument");
        ImaginaryQuadraticRing.preferBlackboardBold(true);
        assertTrue(ImaginaryQuadraticRing.preferBlackboardBold());
        ImaginaryQuadraticRing.preferBlackboardBold(false);
        assertFalse(ImaginaryQuadraticRing.preferBlackboardBold());
    }

    /**
     * Test of toString method, of class ImaginaryQuadraticRing.
     */
    @Test
    public void testToString() {
        System.out.println("toString");
        String expResult = "Z[i]";
        String result = ringGaussian.toString();
        assertEquals(expResult, result);
        expResult = "Z[\u221A-2]";
        result = ringZ2.toString();
        assertEquals(expResult, result);
        expResult = "Z[\u03C9]";
        result = ringEisenstein.toString();
        assertEquals(expResult, result);
        expResult = "O_(Q(\u221A-7))";
        result = ringOQi7.toString();
        assertEquals(expResult, result);
    }

    /**
     * Test of toTeXString method, of class ImaginaryQuadraticRing.
     */
    @Test
    public void testToTeXString() {
        System.out.println("toTeXString");
        ImaginaryQuadraticRing.preferBlackboardBold(true);
        String expResult = "\\mathbb Z[i]";
        String result = ringGaussian.toTeXString();
        assertEquals(expResult, result);
        expResult = "\\mathbb Z[\\sqrt{-2}]";
        result = ringZ2.toTeXString();
        assertEquals(expResult, result);
        expResult = "\\mathbb Z[\\omega]";
        result = ringEisenstein.toTeXString();
        assertEquals(expResult, result);
        expResult = "\\mathcal O_{\\mathbb Q(\\sqrt{-7})}";
        result = ringOQi7.toTeXString();
        assertEquals(expResult, result);
        ImaginaryQuadraticRing.preferBlackboardBold(false);
        expResult = "\\textbf Z[i]";
        result = ringGaussian.toTeXString();
        assertEquals(expResult, result);
        expResult = "\\textbf Z[\\sqrt{-2}]";
        result = ringZ2.toTeXString();
        assertEquals(expResult, result);
        expResult = "\\textbf Z[\\omega]";
        result = ringEisenstein.toTeXString();
        assertEquals(expResult, result);
        expResult = "\\mathcal O_{\\textbf Q(\\sqrt{-7})}";
        result = ringOQi7.toTeXString();
        assertEquals(expResult, result);
    }

    /**
     * Test of toHTMLString method, of class ImaginaryQuadraticRing.
     */
    @Test
    public void testToHTMLString() {
        System.out.println("toHTMLString");
        // PLACEHOLDER FOR toHTMLString() TEST
    }

    /**
     * Test of toFilenameString method, of class ImaginaryQuadraticRing.
     */
    @Test
    public void testToFilenameString() {
        System.out.println("toFilenameString");
        // PLACEHOLDER FOR toFilenameString() TEST
    }
    
    // PLACEHOLDER FOR CONSTRUCTOR TESTS
    
}
