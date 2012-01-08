/*
 * Copyright 2002-2004 Jeremias Maerki.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.krysalis.barcode4j.impl.code93;

import org.krysalis.barcode4j.BarcodeDimension;
import org.krysalis.barcode4j.ChecksumMode;
import org.krysalis.barcode4j.ClassicBarcodeLogicHandler;
import org.krysalis.barcode4j.impl.AbstractBarcodeBean;
import org.krysalis.barcode4j.impl.DefaultCanvasLogicHandler;
import org.krysalis.barcode4j.output.Canvas;
import org.krysalis.barcode4j.output.CanvasProvider;

/**
 * This class is an implementation of the Code93 barcode.
 *
 * @author Jeremias Maerki
 * @version $Id$
 */
public class Code93Bean extends AbstractBarcodeBean {

    /** The default module width for Code 93. */
    protected static final double DEFAULT_MODULE_WIDTH = 0.19f; //mm

    private ChecksumMode checksumMode = ChecksumMode.CP_ADD;
    private boolean displayStartStop = false;
    private boolean displayChecksum = false;
    private boolean extendedCharSet = false;

    /** Create a new instance. */
    public Code93Bean() {
        this.moduleWidth = DEFAULT_MODULE_WIDTH;
        setQuietZone(33 * this.moduleWidth);
        setVerticalQuietZone(0); //1D barcodes don't have vertical quiet zones
    }

    /**
     * Sets the checksum mode
     * @param mode the checksum mode
     */
    public void setChecksumMode(ChecksumMode mode) {
        this.checksumMode = mode;
    }

    /**
     * Returns the current checksum mode.
     * @return ChecksumMode the checksum mode
     */
    public ChecksumMode getChecksumMode() {
        return this.checksumMode;
    }

    /**
     * Indicates whether the start and stop character will be displayed as
     * part of the human-readable message.
     * @return true if leading and trailing "*" will be displayed
     */
    public boolean isDisplayStartStop() {
        return this.displayStartStop;
    }

    /**
     * Enables or disables the use of the start and stop characters in the
     * human-readable message.
     * @param value true to enable the start/stop character, false to disable
     */
    public void setDisplayStartStop(boolean value) {
        this.displayStartStop = value;
        if (value) {
            //Checksum must also be enabled if start/stop is shown
            setDisplayChecksum(true);
        }
    }

    /**
     * Indicates whether the checksum will be displayed as
     * part of the human-readable message.
     * @return true if checksum will be included in the human-readable message
     */
    public boolean isDisplayChecksum() {
        return this.displayChecksum;
    }

    /**
     * Enables or disables the use of the checksum in the
     * human-readable message.
     * @param value true to include the checksum in the human-readable message,
     *   false to ignore
     */
    public void setDisplayChecksum(boolean value) {
        if (isDisplayStartStop() && !value) {
            return; //display-checksum may not be false if start/stop is displayed
        }
        this.displayChecksum = value;
    }

    /**
     * Indicates whether the extended character set is enabled.
     * @return true if the extended character set is enabled
     */
    public boolean isExtendedCharSetEnabled() {
        return this.extendedCharSet;
    }

    /**
     * Enables or disables the extended character set. The extended character set enables
     * the whole ASCII 7-bit character set for Code93.
     * @param value true to enable the extended character set
     */
    public void setExtendedCharSetEnabled(boolean value) {
        this.extendedCharSet = value;
    }

    /** {@inheritDoc} */
    public double getBarWidth(int width) {
        return width * moduleWidth;
    }

    /** {@inheritDoc} */
    public void generateBarcode(CanvasProvider canvas, String msg) {
        if ((msg == null)
                || (msg.length() == 0)) {
            throw new NullPointerException("Parameter msg must not be empty");
        }

        ClassicBarcodeLogicHandler handler =
                new DefaultCanvasLogicHandler(this, new Canvas(canvas));

        Code93LogicImpl impl = createLogicImpl();
        impl.generateBarcodeLogic(handler, msg);
    }

    private Code93LogicImpl createLogicImpl() {
        return new Code93LogicImpl(getChecksumMode(),
                isDisplayStartStop(), isDisplayChecksum(), isExtendedCharSetEnabled());
    }

    /** {@inheritDoc} */
    public BarcodeDimension calcDimensions(String msg) {
        Code93LogicImpl impl = createLogicImpl();
        int msglen = impl.prepareMessage(msg).length();
        // 4 = start + stop + 2x checksum
        final double width = (1 + ((msglen + 4) * 9)) * moduleWidth;
        final double qz = (hasQuietZone() ? quietZone : 0);
        return new BarcodeDimension(width, getHeight(),
                width + (2 * qz), getHeight(),
                quietZone, 0.0);
    }

}
