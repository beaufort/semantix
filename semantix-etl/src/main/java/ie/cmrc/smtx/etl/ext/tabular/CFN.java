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

package ie.cmrc.smtx.etl.ext.tabular;

import ie.cmrc.util.Term;

/**
 * Utility enumeration for storing the Conventional Field Names (CFN) used by the
 * tabular thesaurus extractor {@link ie.cmrc.smtx.etl.ext.tabular.TabularExtractor}
 * @author Yassine Lassoued
 */
public enum CFN {
    
    // --------------
    // RDF
    
    /**
     * Subject of an RDF triple
     */
    RDF_SUBJECT("rdf:subject"),
    
    /**
     * Predicate of an RDF triple
     */
    RDF_PREDICATE("rdf:predicate"),
    
    /**
     * Object of an RDF triple
     */
    RDF_OBJECT("rdf:object"),

    // --------------
    // RDFS
    
    /**
     * RDFS label annotation of a semantic resource
     */
    RDFS_LABEL("rdfs:label"),
    
    // --------------
    // SKOS
    
    /**
     * SKOS Concept Scheme
     */
    SKOS_CONCEPT_SCHEME("skos:ConceptScheme"),
    
    /**
     * SKOS Concept
     */
    SKOS_CONCEPT("skos:Concept"),
    
    /**
     * SKOS Collection
     */
    SKOS_COLLECTION("skos:Collection"),
    
    /**
     * SKOS member
     */
    SKOS_MEMBER("skos:member"),
    
    /**
     * SKOS preferred label
     */
    SKOS_PREF_LABEL("skos:prefLabel"),
    
    /**
     * SKOS alternative label
     */
    SKOS_ALT_LABEL("skos:altLabel"),
    
    /**
     * SKOS hidden label
     */
    SKOS_HIDDEN_LABEL("skos:hiddenLabel"),
    
    /**
     * SKOS definition
     */
    SKOS_DEFINITION("skos:definition"),
    
    /**
     * SKOS top concept
     */
    SKOS_TOP_CONCEPT("skos:topConcept"),
    
    
    // --------------
    // SWS-ETL specific fields
    
    /**
     * Namespace
     */
    SWS_NAMESPACE("sws:namespace"),
    
    /**
     * Concept name space prefix
     */
    SWS_NS_CONCEPT_PREFIX("sws:conceptPrefix"),
    
    /**
     * Concept Scheme name space prefix
     */
    SWS_NS_SCHEME_PREFIX("sws:conceptSchemePrefix"),
    
    /**
     * Collection name space prefix
     */
    SWS_NS_COLLECTION_PREFIX("sws:collectionPrefix"),
    
    /**
     * Link to the concepts table
     */
    SWS_CONCEPT_LIST("sws:conceptList"),
    
    /**
     * Link to the concept definition table
     */
    SWS_CONCEPT_DEFINITIONS("sws:conceptDefinitions"),
    
    /**
     * List of languages
     */
    SWS_LANG("sws:lang"),
    
    /**
     * Field indicating whether resource definitions are expressed in a tabular format
     */
    SWS_TABULAR("sws:tabular"),
    
    /**
     * Field indicating whether a table is sorted
     */
    SWS_SORTED("sws:sorted"),
    
    /**
     * Field indicating a table name
     */
    SWS_TABLE("sws:table");
    
    
    /**
     * Name of the field 
     */
    private final String fieldName;
    
    /**
     * Constructs a conventional field name using the provided name
     * @param name name of a field
     */
    private CFN(String name) {
        this.fieldName = name;
    }
    
    /**
     * Name of the field
     * @return Name of the field
     */
    public String fieldName() {
        return this.fieldName;
    }
    
    /**
     * Return a {@link ie.cmrc.util.Term} Term with the name of the field as a
     * value, and {@code null} as a language
     * @return {@link ie.cmrc.util.Term} whose value is the name of this
     * field and language is {@code null}
     */
    public Term field() {
        return new Term(this.fieldName, null);
    }
    
    /**
     * Return a {@link ie.cmrc.util.Term} Term with the name of the field as a
     * value, and the provided {@code language} as a language
     * @param language Language of the returned {@link ie.cmrc.util.Term}
     * @return {@link ie.cmrc.util.Term} whose value is the name of this
     * field and language is {@code language} 
     */
    public Term field(String language) {
        return new Term(this.fieldName, language);
    }
    
    /**
     * {@inheritDoc}
     * @return String representation of this field. This is the
     * same result as {@linkplain #fieldName()}.
     */
    @Override
    public String toString() {
        return this.fieldName;
    }

    /**
     * Parses a string and returns the matching {@code CFN}
     * @param stringFieldName {@code String} to parse
     * @return {@link CFN} matching the parsed {@code String} if
     * possible, otherwise {@code null}
     */
    public static CFN fromString(String stringFieldName) {
        if (stringFieldName != null) {
            for (CFN cfn: CFN.values()) {
                if (stringFieldName.toLowerCase().equals(cfn.fieldName.toLowerCase())) return cfn;
            }
        }
        return null;
    }
    
    
    
    
}
