/*
 * Copyright (C) 2018 Alonso del Arte
 *
 * This program is free software: you can redistribute it and/or modify it under 
 * the terms of the GNU General Public License as published by the Free Software 
 * Foundation, either version 3 of the License, or (at your option) any later 
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT 
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS 
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more 
 * details.
 *
 * You should have received a copy of the GNU General Public License along with 
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.alonsodelarte.quadraticRings.clipboardops;

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
// import java.awt.datatransfer.Transferable; Potentially necessary to test lostOwnership()
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests of the ImageSelection class.
 * @author Alonso del Arte
 */
public class ImageSelectionTest {
    
    private static BufferedImage img;
    
    private static ImageSelection imgSel;
    
    private static TestImagePanel imgWindow;
    
    private static Clipboard sysClip;
    
    /**
     * Prints out to the console a list of "data flavors" the system clipboard 
     * can give the contents in. If there is {@link DataFlavor#stringFlavor}, 
     * the String will also appear in the console output.
     */
    private static void reportClip() {
        DataFlavor[] currFlavs = sysClip.getAvailableDataFlavors();
        for (DataFlavor flav : currFlavs) {
            System.out.print("* " + flav.toString());
            if (flav.equals(DataFlavor.stringFlavor)) {
                String fromClip;
                try {
                    fromClip = (String) sysClip.getData(DataFlavor.stringFlavor);
                    System.out.print(" --> \"" + fromClip + "\"");
                } catch (UnsupportedFlavorException ufe) {
                    System.out.println("Encountered UnsupportedFlavorException for DataFlavor.stringFlavor");
                    System.out.println(ufe.getMessage());
                } catch (IOException ioe) {
                    System.out.println("Encountered IOException trying to read text from the clipboard");
                    System.out.println(ioe.getMessage());
                }
            }
            System.out.println();
        }
        System.out.println();
    }
    
    /**
     * Queries the system clipboard prior to the tests, sets up an image to use 
     * in the tests.
     */
    @BeforeClass
    public static void setUpClass() {
        imgWindow = new TestImagePanel();
        sysClip = imgWindow.getToolkit().getSystemClipboard();
        System.out.println("Prior to test set up, clipboard has the following data flavors:");
        reportClip();
        String initClipMsg = "This message was placed by ImageSelectionTest.setUpClass()";
        StringSelection strSel = new StringSelection(initClipMsg);
        sysClip.setContents(strSel, strSel);
        img = new BufferedImage(TestImagePanel.PANEL_WIDTH, TestImagePanel.PANEL_HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D graph = img.createGraphics();
        imgWindow.paint(graph);
        imgSel = new ImageSelection(img);
    }
    
    /**
     * Reports on the contents of the clipboard after running the tests. Also 
     * makes sure to close the window with the test image.
     */
    @AfterClass
    public static void tearDownClass() {
        System.out.println("After running the tests, the clipboard has the following data flavors:");
        reportClip();
        imgWindow.closePanel();
    }
    
    /**
     * Puts the test instance of ImageSelection into the system clipboard.
     */
    @Before
    public void setUp() {
        sysClip.setContents(imgSel, imgSel);
    }
    
    /**
     * Reports on the contents of the clipboard after each test.
     */
    @After
    public void tearDown() {
        System.out.println("Right now the clipboard has the following data flavors:");
        reportClip();
    }

    /**
     * Test of getTransferDataFlavors method, of class ImageSelection.
     */
    @Test
    public void testGetTransferDataFlavors() {
        System.out.println("getTransferDataFlavors");
        DataFlavor[] expResult = {DataFlavor.imageFlavor};
        DataFlavor[] result = imgSel.getTransferDataFlavors();
        assertArrayEquals(expResult, result);
    }

    /**
     * Test of isDataFlavorSupported method, of class ImageSelection. Only 
     * {@link DataFlavor#imageFlavor} should register as supported, the others 
     * should not. That includes the deprecated {@link 
     * DataFlavor#plainTextFlavor}.
     */
    @Test
    public void testIsDataFlavorSupported() {
        System.out.println("isDataFlavorSupported");
        assertTrue(imgSel.isDataFlavorSupported(DataFlavor.imageFlavor));
        assertFalse(imgSel.isDataFlavorSupported(DataFlavor.allHtmlFlavor));
        assertFalse(imgSel.isDataFlavorSupported(DataFlavor.fragmentHtmlFlavor));
        assertFalse(imgSel.isDataFlavorSupported(DataFlavor.javaFileListFlavor));
        assertFalse(imgSel.isDataFlavorSupported(DataFlavor.selectionHtmlFlavor));
        assertFalse(imgSel.isDataFlavorSupported(DataFlavor.stringFlavor));
        assertFalse(imgSel.isDataFlavorSupported(DataFlavor.plainTextFlavor)); // Deprecated
    }

    /**
     * Test of getTransferData method, of class ImageSelection.
     */
    @Test
    public void testGetTransferData() {
        System.out.println("getTransferData");
        Object data;
        try {
            data = imgSel.getTransferData(DataFlavor.imageFlavor);
            assertEquals(img, data);
        } catch (UnsupportedFlavorException ufe) {
            String failMessage = "testGetTransferData triggered UnsupportedFlavorException\n\"" + ufe.getMessage() + "\"";
            fail(failMessage);
        } catch (IOException ioe) {
            String failMessage = "testGetTransferData triggered IOException\n\"" + ioe.getMessage() + "\"";
            fail(failMessage);
        }
    }

    /**
     * Test of lostOwnership method, of class ImageSelection.
     */
    @Ignore
    @Test
    public void testLostOwnership() {
        System.out.println("lostOwnership");
        fail("Haven't written test yet.");
    }
    
    /**
     * Test of hasOwnership method, of class ImageSelection.
     */
    @Test
    public void testHasOwnership() {
        System.out.println("hasOwnership");
        assertTrue(imgSel.hasOwnership());
        String testClipMsg = "This message was placed by ImageSelectionTest.testHasOwnership()";
        StringSelection strSel = new StringSelection(testClipMsg);
        sysClip.setContents(strSel, strSel);
        assertFalse(imgSel.hasOwnership());
    }
    
}
