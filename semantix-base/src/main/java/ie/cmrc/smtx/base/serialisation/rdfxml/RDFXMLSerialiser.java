/* 
 * Copyright 2015 Coastal and Marine Research Centre (CMRC), Beaufort,
 * Environmental Research Institute (ERI), University College Cork (UCC).
 * Yassine Lassoued <y.lassoued@gmail.com, y.lassoued@ucc.ie>.
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

package ie.cmrc.smtx.base.serialisation.rdfxml;

import ie.cmrc.smtx.base.serialisation.Namespaces;
import ie.cmrc.smtx.base.serialisation.ElementSetName;
import java.util.Collection;
import java.util.Iterator;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.QName;

/**
 * An RDFXML serialiser for SKOS resources
 * @author Yassine Lassoued <y.lassoued@ucc.ie>
 */
public class RDFXMLSerialiser {
    
    /**
     * Creates an RDF document containing the XML representation of the provided
     * resource
     * @param resource An {@link RDFXMLisable} instance
     * @param elementSet Specifies the level of information to serialise
     * (see {@link ElementSetName} for more details)
     * @param language Language code. Only annotations in this language will
     * be included in the XML.
     * @return XML document
     */
    public static Document makeRDFXMLDocument(RDFXMLisable resource, ElementSetName elementSet, String language) {
        Element rdfElt = DocumentHelper.createElement(new QName("RDF", Namespaces.RDF));
        rdfElt.add(Namespaces.RDF);
        rdfElt.add(Namespaces.RDFS);
        rdfElt.add(Namespaces.OWL);
        rdfElt.add(Namespaces.SKOS);
        rdfElt.add(Namespaces.XSD);

        if (resource!=null) {
            Element xmlElement = resource.toXMLElement(elementSet, language);
            if (xmlElement != null) rdfElt.add(xmlElement);
        }

        Document doc = DocumentHelper.createDocument(rdfElt);

        return doc;
    }
    
    /**
     * Creates an RDF document containing the XML representation of the provided
     * resources
     * @param resources A collection of {@link RDFXMLisable} instances
     * @param elementSet Specifies the level of information to serialise
     * (see {@link ElementSetName} for more details)
     * @param language Language code. Only annotations in this language will
     * be included in the XML.
     * @return XML document
     */
    public static Document makeRDFXMLDocument(Collection<? extends RDFXMLisable> resources, ElementSetName elementSet, String language) {
        Element rdfElt = DocumentHelper.createElement(new QName("RDF", Namespaces.RDF));
        rdfElt.add(Namespaces.RDF);
        rdfElt.add(Namespaces.RDFS);
        rdfElt.add(Namespaces.OWL);
        rdfElt.add(Namespaces.SKOS);
        rdfElt.add(Namespaces.XSD);

        if (resources!=null) {
            for (RDFXMLisable resource: resources) {
                if (resource!=null) {
                    Element xmlElement = resource.toXMLElement(elementSet, language);
                    if (xmlElement != null) rdfElt.add(xmlElement);
                }
            }
        }

        Document doc = DocumentHelper.createDocument(rdfElt);

        return doc;
    }
    
    /**
     * Creates an RDF document containing the XML representation of the provided
     * resources
     * @param resources An iterator over {@link RDFXMLisable} instances
     * @param elementSet Specifies the level of information to serialise
     * (see {@link ElementSetName} for more details)
     * @param language Language code. Only annotations in this language will
     * be included in the XML.
     * @return XML document
     */
    public static Document makeRDFXMLDocument(Iterator<? extends RDFXMLisable> resources, ElementSetName elementSet, String language) {
        Element rdfElt = DocumentHelper.createElement(new QName("RDF", Namespaces.RDF));
        rdfElt.add(Namespaces.RDF);
        rdfElt.add(Namespaces.RDFS);
        rdfElt.add(Namespaces.OWL);
        rdfElt.add(Namespaces.SKOS);
        rdfElt.add(Namespaces.XSD);

        if (resources!=null) {
            while (resources.hasNext()) {
                RDFXMLisable resource = resources.next();
                if (resource!=null) {
                    Element xmlElement = resource.toXMLElement(elementSet, language);
                    if (xmlElement != null) rdfElt.add(xmlElement);
                }
            }
        }

        Document doc = DocumentHelper.createDocument(rdfElt);

        return doc;
    }
}
