/*
 * $Id$
 * ============================================================================
 * The Krysalis Patchy Software License, Version 1.1_01
 * Copyright (c) 2002-2003 Nicola Ken Barozzi.  All rights reserved.
 *
 * This Licence is compatible with the BSD licence as described and
 * approved by http://www.opensource.org/, and is based on the
 * Apache Software Licence Version 1.1.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:
 *       "This product includes software developed for project
 *        Krysalis (http://www.krysalis.org/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "Krysalis" and "Nicola Ken Barozzi" and
 *    "Barcode4J" must not be used to endorse or promote products
 *    derived from this software without prior written permission. For
 *    written permission, please contact nicolaken@krysalis.org.
 *
 * 5. Products derived from this software may not be called "Krysalis"
 *    or "Barcode4J", nor may "Krysalis" appear in their name,
 *    without prior written permission of Nicola Ken Barozzi.
 *
 * 6. This software may contain voluntary contributions made by many
 *    individuals, who decided to donate the code to this project in
 *    respect of this licence, and was originally created by
 *    Jeremias Maerki <jeremias@maerki.org>.
 *
 * THIS SOFTWARE IS PROVIDED ''AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE KRYSALIS PROJECT OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 */
package org.krysalis.barcode4j.output.eps;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;

import org.apache.avalon.framework.CascadingRuntimeException;
import org.krysalis.barcode4j.BarcodeDimension;
import org.krysalis.barcode4j.output.AbstractCanvasProvider;
import org.krysalis.barcode4j.tools.UnitConv;

/**
 * CanvasProvider implementation for EPS output (Encapsulated PostScript).
 * @author Jeremias Maerki
 */
public class EPSCanvasProvider extends AbstractCanvasProvider {

    private OutputStream out;
    private Writer writer;
    private DecimalFormat df;
    private IOException firstError;
    private double height;

    /**
     * Main constructor.
     * @param out OutputStream to write the EPS to
     * @throws IOException in case of an I/O problem
     */
    public EPSCanvasProvider(OutputStream out) throws IOException {
        super();
        this.out = out;
        try {
            this.writer = new java.io.OutputStreamWriter(out, "US-ASCII");
        } catch (UnsupportedEncodingException uee) {
            throw new CascadingRuntimeException(
                    "Incompatible VM: Need US-ASCII encoding", uee);
        }
    }
    
    /**
     * Returns the DecimalFormat instance to use internally to format numbers.
     * @return a DecimalFormat instance
     */
    protected DecimalFormat getDecimalFormat() {
        if (this.df == null) {
            DecimalFormatSymbols dfs = new DecimalFormatSymbols();
            dfs.setDecimalSeparator('.');
            this.df = new DecimalFormat("0.####", dfs);
        }
        return this.df;
    }
    
    private String format(double coord) {
        return getDecimalFormat().format(coord);
    }
    
    private String formatmm(double coord) {
        return getDecimalFormat().format(UnitConv.mm2pt(coord));
    }
    
    private String formatmm(double x, double y) {
        return formatmm(x) + " "  + formatmm(this.height - y);
    }
    
    private void writeHeader(double width, double height) throws IOException {
        writer.write("%!PS-Adobe-3.0 EPSF-3.0\n");
        double widthpt = UnitConv.mm2pt(width);
        double heightpt = UnitConv.mm2pt(height);
        writer.write("%%BoundingBox: 0 0 " 
                + Math.round(Math.ceil(widthpt)) + " " 
                + Math.round(Math.ceil(heightpt)) + "\n");
        writer.write("%%HiResBoundingBox: 0 0 " 
                + format(widthpt) + " " 
                + format(heightpt) + "\n");
        writer.write("%%Creator: Barcode4J (http://barcode4j.krysalis.org)\n");
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        writer.write("%%CreationDate: " + sdf.format(new java.util.Date()) + "\n");
        writer.write("%%LanguageLevel: 1\n");
        writer.write("%%EndComments\n");
        writer.write("%%BeginProlog\n");
        writer.write("%%BeginProcSet: barcode4j-procset 1.0\n");
        writer.write("/rf {\n"); //rect fill: x y w h rf
        writer.write("newpath\n");
        writer.write("4 -2 roll moveto\n");
        writer.write("dup neg 0 exch rlineto\n");
        writer.write("exch 0 rlineto\n");
        writer.write("0 neg exch rlineto\n");
        writer.write("closepath fill\n");
        writer.write("} def\n");
        writer.write("/ct {\n"); //centered text: (text) middle-x y ct
        writer.write("moveto dup stringwidth\n");
        writer.write("2 div neg exch 2 div neg exch\n");
        writer.write("rmoveto show\n");
        writer.write("} def\n");
        writer.write("/jt {\n"); //justified: (text) x1 x2 y jt
        //Calc string width
        writer.write("4 -1 roll dup stringwidth pop\n");
        //Calc available width (x2-x1) 
        writer.write("5 -2 roll 1 index sub\n");
        //Calc (text-width - avail-width)
        writer.write("3 -1 roll sub\n");
        //Get string length
        writer.write("2 index length\n");
        //avail-width / (string-length - 1) = distributable-space
        writer.write("1 sub div\n");
        //setup moveto and ashow
        writer.write("0 4 -1 roll 4 -1 roll 5 -1 roll\n");
        writer.write("moveto ashow\n");
        writer.write("} def\n");
        writer.write("%%EndProcSet: barcode4j-procset 1.0\n");
        writer.write("%%EndProlog\n");
    }

    /**
     * Writes the EPS trailer. Must be called after barcode painting call 
     * returns.
     * @throws IOException if an I/O error happened during EPS generation
     */
    public void finish() throws IOException {
        if (firstError != null) {
            throw firstError;
        }
        writer.write("%%EOF\n");
        writer.flush();
    }

    /** {@inheritDoc} */
    public void establishDimensions(BarcodeDimension dim) {
        super.establishDimensions(dim);
        if (firstError != null) {
            return;
        }
        this.height = dim.getHeightPlusQuiet();
        try {
            writeHeader(dim.getWidthPlusQuiet(), dim.getHeightPlusQuiet());
        } catch (IOException ioe) {
            firstError = ioe;
        }
    }

    /** {@inheritDoc} */
    public void deviceFillRect(double x, double y, double w, double h) {
        if (firstError != null) {
            return;
        }
        try {
            /*
            writer.write(formatmm(w) + " " + formatmm(h) + " " 
                       + formatmm(x) + " " + formatmm(y) + " rf\n");
            */
            writer.write(formatmm(x, y) + " " 
                       + formatmm(w) + " " + formatmm(h) + " rf\n");
        } catch (IOException ioe) {
            firstError = ioe;
        }
    }

    /** {@inheritDoc} */
    public void deviceJustifiedText(
                String text,
                double x1,
                double x2,
                double y1,
                String fontName,
                double fontSize) {
        if (firstError != null) {
            return;
        }
        try {
            writer.write("/" + fontName + " findfont " 
                    + fontSize + " scalefont setfont\n");

            writer.write("(" + text + ") " 
                    + formatmm(x1) + " " 
                    + formatmm(x2) + " "
                    + formatmm(this.height - y1) + " jt\n");
        } catch (IOException ioe) {
            firstError = ioe;
        }
    }

    /** {@inheritDoc} */
    public void deviceCenteredText(
                String text,
                double x1,
                double x2,
                double y1,
                String fontName,
                double fontSize) {
        if (firstError != null) {
            return;
        }
        try {
            writer.write("/" + fontName + " findfont " 
                    + fontSize + " scalefont setfont\n");
            writer.write("(" + text + ") " 
                    + formatmm((x1 + x2) / 2, y1) + " ct\n");
        } catch (IOException ioe) {
            firstError = ioe;
        }
    }

}