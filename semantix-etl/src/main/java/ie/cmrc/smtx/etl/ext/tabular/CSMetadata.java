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

import java.util.List;

/**
 * Provides basic metadata for a concept scheme, e.g., name, labels, associated tables, etc.
 * @author Yassine Lassoued
 */
public class CSMetadata {
    
    /**
     * URI of the concept scheme
     */
    private String uri = null;
    
    /**
     * Indicates whether the data tables defining the concepts belonging in this
     * concept scheme are in a tabular format. Default value is {@code true}.
     */
    private boolean tabular = true;
    
    /**
     * Name of the data table that lists the concepts belonging in this concept scheme
     */
    private String conceptList = null;
    
    /**
     * Name of the data tables that provide the annotations of the concepts belonging in this concept scheme
     */
    private List<String> conceptDefinitionTableNames = null;
    
    /**
     * Languages supported by this concept scheme
     */
    private List<String> languages = null;
    
    /**
     * Indicates whether the data tables associated with this concept scheme are sorted alphabetically
     */
    private boolean sorted = false;

    /**
     * Constructs an empty CSMetadata object
     */
    public CSMetadata() {}
    
    
    /**
     * Get the URI of the concept scheme described by this metadata object
     * @return URI of the concept scheme
     */
    public String getURI() {
        return uri;
    }

    /**
     * Indicates whether the data tables defining the concepts belonging in this
     * concept scheme are in a tabular format or a (subject, predicate, object) triple format.
     * @return {@code true} if the associated data tables are tabular, {@code false} if
     * they are in a (subject, predicate, object) triple format.
     * The default value is {@code true}.
     */
    public boolean isTabular() {
        return tabular;
    }

    /**
     * Names of the data tables that lists the concepts belonging in this concept scheme
     * @return Name of the data tables that define the concepts belonging in this concept scheme
     */
    public String getConceptListTableName() {
        return conceptList;
    }

    /**
     * Names of the data tables that provides the annotations of the concepts belonging in this concept scheme
     * @return Names of the data tables that provide the annotations of the concepts belonging in this concept scheme
     */
    public List<String> getConceptDefinitionTableNames() {
        return conceptDefinitionTableNames;
    }

    /**
     * Lists the languages supported by this concept scheme
     * @return String list containing the languages supported by this concept scheme, e.g., ["en","fr"]
     */
    public List<String> getLanguages() {
        return languages;
    }

    /**
     * Indicates whether the data tables associated with this concept scheme are sorted alphabetically
     * @return True if the data tables are sorted alphabetically, false otherwise. The default value is {@code false}.
     */
    public boolean isSorted() {
        return sorted;
    }

    /**
     * Sets the URI of the concept scheme to the provided <code>String</code> value
     * @param uri Value to set as the URI for the concept scheme
     */
    public void setURI(String uri) {
        this.uri = uri;
    }
    
    /**
     * Specify whether the data tables defining the concepts belonging to this
     * concept scheme are in a tabular or triple format
     * @param tabular Boolean value to set for the "tabular" property:
     * {@code true} meaning that the concept definition tables are in a tabular format;
     * {@code false} meaning that they are in a (subject, predicate, object) triple format
     */
    public void setTabular(boolean tabular) {
        this.tabular = tabular;
    }

    /**
     * Sets the name of the table that lists the concepts belonging in this concept scheme
     * @param conceptListTableName <code>String</code> value to set as a table name
     */
    public void setConceptListTableName(String conceptListTableName) {
        this.conceptList = conceptListTableName;
    }

    /**
     * Sets the names of the tables that provide the annotation of the concepts belonging in this concept scheme
     * @param conceptDefinitionTableNames {@code List<String>} values to set as a table name
     */
    public void setConceptDefinitionTableNames(List<String> conceptDefinitionTableNames) {
        this.conceptDefinitionTableNames = conceptDefinitionTableNames;
    }

    /**
     * Sets the languages supported by the concept scheme
     * @param languages A <code>String</code> list containing the language codes
     */
    public void setLanguages(List<String> languages) {
        this.languages = languages;
    }

    /**
     * Specify whether the data tables defining the concepts belonging in this concept scheme are sorted
     * @param sorted <code>Boolean</code> value to set for the "sorted" property, true meaning that the data tables are sorted
     */
    public void setSorted(boolean sorted) {
        this.sorted = sorted;
    }
    
    
}
