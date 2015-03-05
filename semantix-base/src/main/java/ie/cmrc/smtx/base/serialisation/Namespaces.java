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

package ie.cmrc.smtx.base.serialisation;

import org.dom4j.Namespace;

/**
 *
 * @author yassine
 */
public class Namespaces {
    public static final Namespace SKOS = new Namespace("skos", "http://www.w3.org/2004/02/skos/core#");
    public static final Namespace RDF = new Namespace("rdf", "http://www.w3.org/1999/02/22-rdf-syntax-ns#");
    public static final Namespace RDFS = new Namespace("rdfs", "http://www.w3.org/2000/01/rdf-schema#");
    public static final Namespace DC = new Namespace("dc", "http://purl.org/dc/elements/1.1/");
    public static final Namespace DCT = new Namespace("dct", "http://purl.org/dc/terms/");
    public static final Namespace OWL = new Namespace("owl", "http://www.w3.org/2002/07/owl#");
    public static final Namespace XML = new Namespace("xml", "http://www.w3.org/XML/1998/namespace");
    public static final Namespace XSD = new Namespace("xsd", "http://www.w3.org/2001/XMLSchema#");

    public static final Namespace SOAP = new Namespace("soap", "http://www.w3.org/2003/05/soap-envelope");
    public static final Namespace SWS = new Namespace("sws", "http://cmrc.ucc.ie/sws/2.1");
}
