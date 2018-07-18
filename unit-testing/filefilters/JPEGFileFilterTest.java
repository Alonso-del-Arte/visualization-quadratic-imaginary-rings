/*
 * Copyright (C) 2018 User
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
package filefilters;

import java.io.File;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author User
 */
public class JPEGFileFilterTest {
    
    private static JPEGFileFilter filter;
    
    public JPEGFileFilterTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
        filter = new JPEGFileFilter();
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
     * Test of accept method, of class JPEGFileFilter.
     */
    @Test
    public void testAccept() {
        System.out.println("accept");
        File file = new File("image.jpg");
        String assertionMessage = "JPEGFileFilter should accept " + file.getName();
        System.out.println(assertionMessage);
        assertTrue(assertionMessage, filter.accept(file));
        file = new File("image.jpeg");
        assertionMessage = "JPEGFileFilter should accept " + file.getName();
        System.out.println(assertionMessage);
        assertTrue(assertionMessage, filter.accept(file));
        file = new File("image.JPG");
        assertionMessage = "JPEGFileFilter should accept " + file.getName();
        System.out.println(assertionMessage);
        assertTrue(assertionMessage, filter.accept(file));
        file = new File("image.JPEG");
        assertionMessage = "JPEGFileFilter should accept " + file.getName();
        System.out.println(assertionMessage);
        assertTrue(assertionMessage, filter.accept(file));
        file = new File("image.png");
        assertionMessage = "JPEGFileFilter should reject " + file.getName();
        System.out.println(assertionMessage);
        assertFalse(assertionMessage, filter.accept(file));
        file = new File("image.PNG");
        assertionMessage = "JPEGFileFilter should reject " + file.getName();
        System.out.println(assertionMessage);
        assertFalse(assertionMessage, filter.accept(file));
        file = new File("index.html");
        assertionMessage = "JPEGFileFilter should reject " + file.getName();
        System.out.println(assertionMessage);
        assertFalse(assertionMessage, filter.accept(file));
        String homeDir = System.getProperty("user.home");
        File dir = new File(homeDir);
        assertionMessage = "JPEGFileFilter should accept directory " + homeDir;
        System.out.println(assertionMessage);
        assertTrue(assertionMessage, filter.accept(dir));
    }

    /**
     * Test of getDescription method, of class JPEGFileFilter.
     */
    @Test
    public void testGetDescription() {
        System.out.println("getDescription");
        String description = filter.getDescription();
        System.out.println("JPEGFileFilter description is \"" + description + "\"");
        String assertionMessage = "Filter description should include \"Joint Photographic Experts Group\"";
        assertTrue(assertionMessage, description.contains("Joint Photographic Experts Group"));
        assertionMessage = "Filter description should include \"jpg\" or \"jpeg\"";
        assertTrue(assertionMessage, description.toLowerCase().contains("jpg") || description.toLowerCase().contains("jpeg"));
    }
    
}
