/*
 * $Id$
 * ============================================================================
 * The Krysalis Patchy Software License, Version 1.1_01
 * Copyright (c) 2003 Nicola Ken Barozzi.  All rights reserved.
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
package org.krysalis.barcode4j.tools;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.avalon.framework.configuration.Configuration;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import junit.framework.TestCase;

/**
 * Test case for ConfigurationUtil.
 * 
 * @author Jeremias Maerki
 */
public class ConfigurationUtilTestCase extends TestCase {

    public ConfigurationUtilTestCase(String name) {
        super(name);
    }

    public void testDOMLevel1ToConfiguration() throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(false);
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.newDocument();
        Element root = doc.createElement("root");
        root.setAttribute("name", "value");
        doc.appendChild(root);
        Element child = doc.createElement("child");
        child.setAttribute("foo", "bar"); 
        child.appendChild(doc.createTextNode("hello"));
        root.appendChild(child); 
        
        Configuration cfg = ConfigurationUtil.buildConfiguration(root);
        //Configuration cfg = ConfigurationUtil.toConfiguration(root);
        //System.out.println(org.apache.avalon.framework.configuration.ConfigurationUtil.toString(cfg));
        
        checkCfgTree(cfg);
    }
    
    public void testDOMLevel2ToConfiguration() throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.newDocument();
        final String NS = "http://somenamespace";
        Element root = doc.createElementNS(NS, "root");
        root.setAttribute("name", "value");
        doc.appendChild(root);
        Element child = doc.createElementNS(NS, "child");
        child.setAttribute("foo", "bar");
        child.appendChild(doc.createTextNode("hello"));
        root.appendChild(child); 
        
        Configuration cfg = ConfigurationUtil.buildConfiguration(root);
        //Configuration cfg = ConfigurationUtil.toConfiguration(root);
        //System.out.println(org.apache.avalon.framework.configuration.ConfigurationUtil.toString(cfg));
        
        checkCfgTree(cfg);
    }
    
    private void checkCfgTree(final Configuration cfg) throws Exception {
        assertNotNull(cfg);
        assertEquals("root", cfg.getName());
        assertEquals("value", cfg.getAttribute("name"));
        assertNull(cfg.getValue(null));
        Configuration childcfg = cfg.getChild("child");
        assertNotNull(childcfg);
        assertEquals("child", childcfg.getName());
        assertEquals("bar", childcfg.getAttribute("foo"));
        assertEquals("hello", childcfg.getValue());
    }

}