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
 * Provides basic metadata for a concept scheme, e.g., name, labels, associated tables, etc.
 * @author Yassine Lassoued
 */
public class CollectionMetadata {
    
    /**
     * URI of the collection
     */
    private String uri = null;
    
    /**
     * Name of the data table that defines the concepts belonging to this collection
     */
    private String conceptsTableName = null;

    /**
     * Constructs an empty CSollectionMetadata object
     */
    public CollectionMetadata() {}
    
    
    /**
     * Get the URI of the collection described by this metadata object
     * @return URI of the collection
     */
    public String getURI() {
        return uri;
    }

    /**
     * Names of the data tables that lists the concepts belonging to this collection
     * @return Name of the data tables that define the concepts belonging to this collection
     */
    public String getConceptsTableName() {
        return conceptsTableName;
    }

    /**
     * Sets the URI of the collection to the provided <code>String</code> value
     * @param uri Value to set as the URI for the collection
     */
    public void setURI(String uri) {
        this.uri = uri;
    }

    /**
     * Sets the name of the table that lists the concepts belonging to this collection
     * @param conceptsTableName <code>String</code> value to set as a table name
     */
    public void setConceptsTableName(String conceptsTableName) {
        this.conceptsTableName = conceptsTableName;
    }
    
    
}
