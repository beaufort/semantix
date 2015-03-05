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

package ie.cmrc.smtx.skos.jena;

import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.InfModel;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import java.io.IOException;
import java.io.InputStream;

/**
 * Provides methods for creating SKOS thesauri and inferencing
 * @author Yassine Lassoued
 */
public class SKOSFactory {
    
    private static final String SKOS_PATH = "/skos.rdf";
    private static final String SKOSX_PATH = "/skosx.owl";
    
    /**
     * Builds an empty SKOS thesaurus. This method loads the SKOS model and
     * uses it to initialise the thesaurus
     * @return An empty {@link ie.cmrc.skos.core.SKOS} that does not support inferences.
     */
    public static JenaSKOS createDefaultSKOSThesaurus() {
        Model skosModel = createSKOSModel();
        if (skosModel != null) return new JenaSKOS(skosModel);
        else return null;
    }
    
    /**
     * Wraps the provided model as a {@link ie.cmrc.skos.core.jena.JenaSKOS} and
     * inserts the SKOS model if {@code insertSKOS} is {@code true}
     * @param model Jena model ({@code com.hp.hpl.jena.rdf.model.Model}) to wrap
     * as a {@link ie.cmrc.skos.core.jena.JenaSKOS}
     * @param insertSKOS Specifies whether the SKOS model si to be loaded into
     * the model or not. If set to {@code true} then SKOS is loaded.
     * @return A {@link ie.cmrc.skos.core.jena.JenaSKOS} wrapper for the provided {@code model}
     */
    public static JenaSKOS createSKOSThesaurus(Model model, boolean insertSKOS) {
        
        if (insertSKOS && model!=null) {
            Model skosModel = createSKOSModel();
            model.add(skosModel);
        }
        
        JenaSKOS thesaurus =  new JenaSKOS(model);
        return thesaurus;
    }
    
    /**
     * Creates a {@link ie.cmrc.skos.core.jena.TDBSKOS} using the provided
     * directory (path) as a TDB back end.
     * Use this method to create a persistent thesaurus using an existing TDB store.
     * @param tdbDirectory Path to the TDB data directory
     * @return A persistent {@link ie.cmrc.skos.core.jena.TDBSKOS} with {@code tdbDirectory}
     * as a data directory
     */
    public static TDBSKOS createSKOSThesaurus(String tdbDirectory) {
        return createSKOSThesaurus(tdbDirectory, false);
    }
    
    /**
     * Creates a {@link ie.cmrc.skos.core.jena.TDBSKOS} using the provided
     * directory (path) as a TDB back end.
     * Use this method to create a persistent thesaurus using an existing TDB store
     * or to create a new one and insert the SKOS model.
     * @param tdbDirectory Path to the TDB data directory
     * @param insertSKOS if {@code true} then the SKOS model will be loaded into
     * the TDB
     * @return A persistent {@link ie.cmrc.skos.core.jena.TDBSKOS} with {@code tdbDirectory}
     * as a data directory
     */
    public static TDBSKOS createSKOSThesaurus(String tdbDirectory, boolean insertSKOS) {
        Model skosModel = null;
        if (insertSKOS) {
            skosModel = createSKOSModel();
        }
        
        TDBSKOS thesaurus = new TDBSKOS(tdbDirectory);
        if (skosModel != null) {
            thesaurus.add(skosModel);
            thesaurus.sync();
        }
        return thesaurus;
    }
    
    /**
     * Creates a {@link ie.cmrc.skos.core.jena.TDBSKOS} using the provided
     * directory (path) as a TDB back end.
     * Use this method to create a persistent thesaurus using an existing TDB store
     * or to create a new one and insert the SKOS model.
     * @param tdbDirectory Path to the TDB data directory
     * @param insertSKOS If {@code true} then the SKOS model will be loaded into
     * the TDB
     * @param infer If {@code true} then inferences over the SKOS model will be computed
     * @return A persistent {@link ie.cmrc.skos.core.jena.TDBSKOS} with {@code tdbDirectory}
     * as a data directory
     */
    public static TDBSKOS createSKOSThesaurus(String tdbDirectory, boolean insertSKOS, boolean infer) {
        Model model = null;
        if (insertSKOS) {
            Model skosModel = createSKOSModel();
            if (infer) {
                model = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM, skosModel);
            }
            else model = skosModel;
        }
        
        TDBSKOS thesaurus = new TDBSKOS(tdbDirectory);
        if (model != null) {
            thesaurus.add(model);
            thesaurus.sync();
        }
        return thesaurus;
    }
    
    /**
     * Builds an empty SKOS thesaurus that supports inferences. This method loads the SKOS model from
     * http://www.w3.org/2004/02/skos/core# and uses it to initialise the thesaurus<br/>
     * You may need to improve this by storing SKOS locally...
     * @return An empty {@link ie.cmrc.skos.core.SKOS} that supports inferences.
     * The inner Jena Model of this {@link JenaSKOS} is a Jena InfModel
     * ({@code com.hp.hpl.jena.rdf.model.InfModel}) that uses
     * {@code com.hp.hpl.jena.ontology.OntModelSpec.OWL_MEM_MICRO_RULE_INF}
     */
    public static JenaSKOS createInferencingSKOSThesaurus() {
        return SKOSFactory.createInferencingSKOSThesaurus(OntModelSpec.OWL_MEM_MICRO_RULE_INF);
    }
    
    /**
     * Builds an empty SKOS thesaurus that supports inferences using the provided
     * {@code com.hp.hpl.jena.ontology.OntModelSpec}. This method loads the SKOS model from
     * http://www.w3.org/2004/02/skos/core# and uses it to initialise the thesaurus<br/>
     * You may need to improve this by storing SKOS locally...
     * @param ontModelSpec A Jena {@code com.hp.hpl.jena.ontology.OntModelSpec} that specifies the
     * reasoner to use for inferencing
     * @return An empty {@link ie.cmrc.skos.core.SKOS} that supports inferences.
     * The inner Jena Model of this {@link JenaSKOS} is a Jena InfModel
     * ({@code com.hp.hpl.jena.rdf.model.InfModel})
     */
    public static JenaSKOS createInferencingSKOSThesaurus(OntModelSpec ontModelSpec) {
        InfModel infModel = ModelFactory.createOntologyModel(ontModelSpec);
        
        Model skosModel = createSKOSModel();
        if (skosModel != null) {
            
            infModel.add(skosModel);
        }
        return new JenaSKOS(infModel);
    }
    
    /**
     * Uses the provided {@link ie.cmrc.skos.core.SKOS} to compute inferences
     * and returns the result as SKOS thesaurus that supports inferences.
     * @param thesaurus SKOS thesaurus for which inferences are to be computed
     * @return A {@link ie.cmrc.skos.core.SKOS} that contains all the asserted
     * and inferred resources and relationships from the provided {@code thesaurus}.
     * Inferences are computed using the {@code OntModelSpec.OWL_MEM_MICRO_RULE_INF}
     * reasoner.
     */
    public static JenaSKOS createInferencingSKOSThesaurus(JenaSKOS thesaurus) {
        InfModel infModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM_MINI_RULE_INF);
        infModel.add(thesaurus);
        return new JenaSKOS(infModel);
    }
    
    /**
     * Uses the provided {@link ie.cmrc.skos.core.SKOS} to compute inferences
     * and returns the result as SKOS thesaurus that supports inferences.
     * @param thesaurus SKOS thesaurus for which inferences are to be computed
     * @param ontModelSpec A Jena {@code com.hp.hpl.jena.ontology.OntModelSpec} that specifies the
     * reasoner to use for inferencing
     * @return A {@link ie.cmrc.skos.core.SKOS} that contains all the asserted
     * and inferred resources and relationships from the provided {@code thesaurus}.
     * Inferences are computed using the provided {@code ontModelSpec}
     * reasoner.
     */
    public static JenaSKOS createInferencingSKOSThesaurus(JenaSKOS thesaurus, OntModelSpec ontModelSpec) {
        InfModel infModel = ModelFactory.createOntologyModel(ontModelSpec, thesaurus);
        return new JenaSKOS(infModel);
    }
    
    /**
     * Creates a Jena Model that contains the SKOS and SKOSX ontologies
     * @return Jena Model
     */
    private static Model createSKOSModel() {
        InputStream skosInputStream = SKOSFactory.class.getResourceAsStream(SKOS_PATH);
        Model skosModel = ModelFactory.createDefaultModel();
        skosModel.read(skosInputStream, null);
        try {
            skosInputStream.close();
        } catch (IOException ex) {
//            throw new RuntimeException(ex);
        }
        
        InputStream skosxInputStream = SKOSFactory.class.getResourceAsStream(SKOSX_PATH);
        Model skosxModel = ModelFactory.createDefaultModel();
        skosxModel.read(skosxInputStream, null);
        try {
            skosxInputStream.close();
        } catch (IOException ex) {
//            throw new RuntimeException(ex);
        }
        
        skosxModel.add(skosModel);
        return skosxModel;
    }
}
