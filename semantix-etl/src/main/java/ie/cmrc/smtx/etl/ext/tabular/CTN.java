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

/**
 * Utility enumeration for storing the Conventional Table Names (CTN) used by
 * the tabular model extractor {@link ie.cmrc.smtx.etl.ext.tabular.TabularExtractor}
 * @author Yassine Lassoued
 */
public enum CTN {
    /**
     * OWL Ontology table name
     */
    OWL_ONTOLOGY("owl-Ontology"),
    
    /**
     * SKOS concept schemes table name
     */
    SKOS_CONCEPT_SCHEME("skos-ConceptScheme"),
    
    /**
     * SKOS collections table name
     */
    SKOS_COLLECTION("skos-Collection"),
    
    /**
     * SKOS collection members table name
     */
    SKOS_MEMBER("skos-member"),
    
    /**
     * SKOS collection-collection membership table name
     */
    SKOS_MEMBER_COLLECTION("skos-member-collection"),
    
    /**
     * SKOS relationships table name
     */
    SKOS_SEMANTIC_RELATION("skos-semanticRelation");
    
    
    
    /**
     * Name of the table 
     */
    private final String tableName;
    
    /**
     * Constructs a conventional table name using the provided name
     * @param name name of a table
     */
    private CTN(String name) {
        this.tableName = name;
    }
    
    /**
     * Name of the table
     * @return Name of the table
     */
    public String tableName() {
        return this.tableName;
    }
    /**
     * {@inheritDoc}
     * @return String representation of this table name. This is the
     * same result as {@link #tableName()}.
     */
    @Override
    public String toString() {
        return this.tableName;
    }

    /**
     * Parses a string and returns the matching {@code CTN}
     * @param stringTableName {@code String} to parse
     * @return {@link CFN} matching the parsed {@code String} if
     * possible, otherwise {@code null}
     */
    public static CTN fromString(String stringTableName) {
        if (stringTableName != null) {
            for (CTN ctn: CTN.values()) {
                if (stringTableName.toLowerCase().equals(ctn.tableName.toLowerCase())) return ctn;
            }
        }
        return null;
    }
    
    
    
    
}
