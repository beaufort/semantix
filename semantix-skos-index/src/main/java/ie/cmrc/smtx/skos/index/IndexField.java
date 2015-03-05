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

package ie.cmrc.smtx.skos.index;

import ie.cmrc.util.Term;

/**
 * Utility enumeration for storing the conventional SKOS index field names used by the
 * {@link SKOSIndex}
 * @author Yassine Lassoued <y.lassoued@ucc.ie>
 */
public class IndexField {
    
    /**
     * Searchable fields of a {@link SKOSIndex}
     */
    public static enum Searchable {
        /**
        * Concept URI [value = "uri"]
        */
       URI("uri"),

       /**
        * Concept local name [value = "name"]
        */
       NAME("name"),
       
       /**
        * Preferred label of a concept, use this for inexact or fuzzy matches
        * [value = "ixprefLabel"]
        */
       IX_PREF_LABEL("ixprefLabel", true),
       
       /**
        * Preferred label of a concept, use this for exact matches
        * [value = "prefLabel"]
        */
       PREF_LABEL("prefLabel", true),
       
       /**
        * Alternative label of a concept, use this for inexact or fuzzy matches
        * [value = "ixaltLabel"]
        */
       IX_ALT_LABEL("ixaltLabel", true),
       
       /**
        * Alternative label of a concept, use this for exact matches
        * [value = "altLabel"]
        */
       ALT_LABEL("altLabel", true),
       
       /**
        * Hidden label of a concept, use this for inexact or fuzzy matches
        * [value = "ixhiddenLabel"]
        */
       IX_HIDDEN_LABEL("ixhiddenLabel", true),
       
       /**
        * Hidden label of a concept, use this for exact matches
        * [value = "hiddenLabel"]
        */
       HIDDEN_LABEL("hiddenLabel", true),
        
        /**
        * Label of a concept, use this for inexact or fuzzy matches
        * [value = "ixlabel"]
        */
       IX_LABEL("ixlabel", true),
       
       /**
        * Label of a concept, use this for exact matches [value = "label"]
        */
       LABEL("label", true),

       /**
        * Concept definition
        */
       DEFINITION("definition", true);
       
       /**
        * Name of the field 
        */
       private final String fieldName;

       private final boolean multilingual;

       /**
        * Constructs a filtrable field using the provided name. By default the
        * index field is not multilingual
        * @param fieldName name of a field
        */
       private Searchable(String fieldName) {
           this.fieldName = fieldName;
           this.multilingual = false;
       }
       
       /**
        * Constructs a filtrable field using the provided name
        * @param fieldName name of a field
        * @param multilingual indicates if this field is multilingual
        */
       private Searchable(String fieldName, boolean multilingual) {
           this.fieldName = fieldName;
           this.multilingual = multilingual;
       }

       /**
        * Name of the field
        * @return Name of the field
        */
       public String fieldName() {
           return this.fieldName;
       }
       
       /**
        * Indicates whether this filtrable field can be multilingual
        * @return {@code true} is the filtrable index field can be multilingual;
        * {@code false} otherwise
        */
       public boolean isMultilingual() {
           return this.multilingual;
       }

       /**
        * Return a {@link ie.cmrc.util.Term} Term with the name of the field
        * as a value, and {@code null} as a language
        * @return {@link ie.cmrc.util.Term} whose value is the name of this
        * field and language is {@code null}
        */
       public Term field() {
           return new Term(this.fieldName, null);
       }

       /**
        * Return a {@link ie.cmrc.util.Term} Term with the name of the field as
        * a value, and the provided {@code language} as a language
        * @param language Language of the returned {@link ie.cmrc.util.Term}
        * @return {@link ie.cmrc.util.Term} whose value is the name of this
        * field and whose language is: {@code language} if the filtrable index
        * field is multilingual, null otherwise
        */
       public Term field(String language) {
           if (multilingual) return new Term(this.fieldName, language);
           else return new Term(this.fieldName, null);
       }

       /**
        * Returns the {@code Searchable} whose name matches the provided String
        * value
        * @param value String value to parse
        * @return {@code Searchable} whose field name matches {@code value} if
        * any, otherwise {@code null}. This method is <b>case insensitive</b>.
        */
       public static Searchable fromString(String value) {
           if (value != null) {
               for (Searchable searchable: Searchable.values()) {
                   if (value.equals(searchable.fieldName)) return searchable;
               }
           }
           return null;
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
       
       
    }
    
    /**
     * Fields used for filtering index results
     */
    public static enum Filterable {

        /**
         * Resource type (Concept, Collection, or ConceptScheme)
         */
        TYPE("type"),
        
        /**
         * Concept scheme to which a resource belongs
         */
        CS("cs"),

        /**
         * Collection to which a resource belongs
         */
        COLLECTION("collection"),

        /**
         * Collection to which a resource belongs directly or indirectly
         */
        COLLECTION_TRANSITIVE("collTrans");

        /**
         * Name of the field 
         */
        private final String fieldName;

        /**
         * Constructs a string field using the provided name
         * @param name name of a field
         */
        private Filterable(String name) {
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
         * Returns the {@code Filterable} field whose name matches the provided
         * String value
         * @param value String value to parse
         * @return {@code Filterable} whose field name matches {@code value} if
         * any, otherwise {@code null}. This method is <b>case insensitive</b>.
         */
        public static Filterable fromString(String value) {
            if (value != null) {
                for (Filterable filtrable: Filterable.values()) {
                    if (value.matches("(?i)"+filtrable.fieldName)) return filtrable;
                }
            }
            return null;
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
    }
    

    
    
    
    
}
