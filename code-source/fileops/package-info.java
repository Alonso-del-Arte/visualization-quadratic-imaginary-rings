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
/**
 * A helper package to move some file operations out of the 
 * imaginaryquadraticinteger package.
 * 
 * <ul>
 * <li>{@link fileops.FileChooserWithOverwriteGuard} extends 
 * {@link javax.swing.JFileChooser} in order to prevent the user from 
 * accidentally overwriting an existing file.</li>
 * <li>{@link fileops.JPEGFileFilter} filters out files that are not JPEG files 
 * from a FileChooser dialog. This filter does not recognize *.jfif as a valid 
 * JPEG file extension.</li>
 * <li>{@link fileops.PNGFileFilter} filters out files that are not PNG files 
 * from a FileChooser dialog.</li>
 * </ul>
 * 
 * <p>Copyright &copy; 2018 Alonso del Arte.</p>
 */
package fileops;
