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

import org.apache.avalon.framework.configuration.Configurable;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.krysalis.barcode4j.ChecksumMode;
import org.krysalis.barcode4j.impl.ConfigurableBarcodeGenerator;
import org.krysalis.barcode4j.tools.Length;

/**
 * This class is an implementation of the Code93 barcode.
 *
 * @author Jeremias Maerki
 * @version $Id$
 */
public class Code93 extends ConfigurableBarcodeGenerator
            implements Configurable {

    /** Create a new instance. */
    public Code93() {
        this.bean = new Code93Bean();
    }

    /**
     * @see org.apache.avalon.framework.configuration.Configurable#configure(Configuration)
     */
    public void configure(Configuration cfg) throws ConfigurationException {
        //Module width (MUST ALWAYS BE FIRST BECAUSE QUIET ZONE MAY DEPEND ON IT)
        Length mw = new Length(cfg.getChild("module-width").getValue("0.19mm"), "mm");
        getCode93Bean().setModuleWidth(mw.getValueAsMillimeter());

        super.configure(cfg);

        //Checksum mode
        getCode93Bean().setChecksumMode(ChecksumMode.byName(
            cfg.getChild("checksum").getValue(ChecksumMode.CP_AUTO.getName())));

        if (cfg.getChild("extended-charset", false) != null) {
            getCode93Bean().setExtendedCharSetEnabled(
                    cfg.getChild("extended-charset").getValueAsBoolean());
        }

        Configuration hr = cfg.getChild("human-readable", false);
        if (hr != null) {
            //Display start/stop character and checksum in hr-message or not
            getCode93Bean().setDisplayStartStop(
                    hr.getChild("display-start-stop").getValueAsBoolean(false));
            getCode93Bean().setDisplayChecksum(
                    hr.getChild("display-checksum").getValueAsBoolean(false));
        }
    }

    /**
     * @return the underlying Code93Bean
     */
    public Code93Bean getCode93Bean() {
        return (Code93Bean)getBean();
    }

}
