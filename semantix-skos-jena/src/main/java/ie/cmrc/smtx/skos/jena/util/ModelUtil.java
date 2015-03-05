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

package ie.cmrc.smtx.skos.jena.util;

import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.InfModel;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import java.io.InputStream;

/**
 * @deprecated 
 * @author Yassine Lassoued
 */
public class ModelUtil {
    
    private static final String SKOS_ONTOLOGY = "/skos.rdf";
    
    /**
     * Builds an empty Jena model and loads the SKOS ontology from
     * the resources directory into it<br/>
     * You may want to improve this by storing SKOS locally...
     * @return A Jena model ({@code com.hp.hpl.jena.rdf.model.Model}) containing the
     * SKOS ontology.
     */
    public static Model buildDefaultSKOSModel() {
        InputStream skosInputStream = ModelUtil.class.getResourceAsStream(SKOS_ONTOLOGY);
        if (skosInputStream != null) {
            Model skosModel = ModelFactory.createDefaultModel();
            skosModel.read(skosInputStream, null);
            return skosModel;
        }
        else {
            return null;
        }
    }
    
    
    /**
     * Builds an empty SKOS model that supports inferences and loads the SKOS ontology from
     * the resources directory into it.<br/>
     * You may need to improve this by storing SKOS locally...
     * @param ontModelSpec A Jena {@code com.hp.hpl.jena.ontology.OntModelSpec} that specifies the
     * reasoner to use for inferencing
     * @return A Jena inference model ({@code com.hp.hpl.jena.rdf.model.InfModel}) containing the
     * SKOS ontology
     */
    public static InfModel buildSKOSInferenceModel(OntModelSpec ontModelSpec) {
        InputStream skosInputStream = ModelUtil.class.getResourceAsStream(SKOS_ONTOLOGY);
        if (skosInputStream != null) {
            Model skosModel = ModelFactory.createDefaultModel();
            skosModel.read(skosInputStream, null);
            InfModel infModel = ModelFactory.createOntologyModel(ontModelSpec);
            infModel.add(skosModel);
            return infModel;
        }
        else {
            return null;
        }
    }
    
}
