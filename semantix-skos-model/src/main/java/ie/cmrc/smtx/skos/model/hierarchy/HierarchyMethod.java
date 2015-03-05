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

package ie.cmrc.smtx.skos.model.hierarchy;

/**
 * Defines a method for building a hierarchy of concepts. A hierarchy method is
 * defined by the way hierarchy roots are selected, and the type of relationship
 * to use to build the hierarchical links
 * @author Yassine Lassoued <y.lassoued@ucc.ie>
 */
public class HierarchyMethod {
    
    /**
     * Enumerates the possible methods to set the roots of a concept hierarchy
     */
    public static enum RootType {
        /**
         * Only concepts asserted as top concepts of the target concept scheme
         * are considered as roots
         */
        TOP_CONCEPTS("topConcepts"),
        
        /**
         * Only the broadest concepts are considered as roots
         */
        BROADEST_CONCEPTS("broadestConcepts"),
        
        /**
         * Both top and broadest concepts are considered as roots
         */
        BOTH("both");
        
        
        private final String value;
        
        private RootType(String value) {
            this.value = value;
        }
        
        /**
         * String value of the {@code RootType}
         * @return String value of the {@code RootType}
         */
        public String value() {
           return this.value;
        }

        /**
         * String representation of the {@code RootType}
         * @return String representation of the {@code RootType}
         */
        @Override
        public String toString() {
           return this.value;
        }

        /**
         * {@code RootType} matching the provided String
         * @param stringValue String to parse
         * @return {@code RootType} matching the provided text if any, otherwise {@code null}
         */
        public static RootType fromString(String stringValue) {
           if (stringValue != null) {
               for (RootType rootType: RootType.values()) {
                   if (stringValue.equals(rootType.value)) return rootType;
               }
           }
           return null;
        }
        
    }
    
    /**
     * Enumerates the possible types of relationships used to build
     * a concept hierarchy
     */
    public static enum RelationshipType {
        /**
         * For a parent concept, only concepts that are asserted to be narrower
         * than this one, using {@code skos:narrower}, are considered as child
         * concepts
         */
        NARROWER("narrower"),
        
        /**
         * For a parent concept, only concepts that are <b>immediately</b>
         * narrower than this one using {@code skos:narrowerTransitive} are
         * considered as child concepts
         */
        DIRECT_NARROWER("directNarrower"),
        
        /**
         * Both narrower and direct narrower relationships are considered in
         * building a concept hierarchy
         */
        BOTH_NARROWER("bothNarrower");
        
        
        private final String value;
        
        private RelationshipType(String value) {
            this.value = value;
        }
        
        /**
         * String value of the {@code RelationshipType}
         * @return String value of the {@code RelationshipType}
         */
        public String value() {
           return this.value;
        }

        /**
         * String representation of the {@code RelationshipType}
         * @return String representation of the {@code RelationshipType}
         */
        @Override
        public String toString() {
           return this.value;
        }

        /**
         * {@code RelationshipType} matching the provided String
         * @param stringValue String to parse
         * @return {@code RelationshipType} matching the provided text if any, otherwise {@code null}
         */
        public static RelationshipType fromString(String stringValue) {
           if (stringValue != null) {
               for (RelationshipType relType: RelationshipType.values()) {
                   if (stringValue.equals(relType.value)) return relType;
               }
           }
           return null;
        }
    }
    
    /**
     * Default hierarchy root computation method
     */
    public static final RootType DEFAULT_ROOT_TYPE = RootType.TOP_CONCEPTS;
    
    /**
     * Default hierarchy relationship type
     */
    public static final RelationshipType DEFAULT_RELATIONSHIP_TYPE = RelationshipType.NARROWER;
    
    /**
     * Default hierarchy building method: root type is {@code RootType.TOP_CONCEPTS}
     * and relationship type is {@code RelationshipType.NARROWER}
     */
    public static final HierarchyMethod DEFAULT_METHOD = new HierarchyMethod();
    
    
    
    /**
     * Method used to set the roots of a hierarchy
     */
    protected RootType rootType;
    
    /**
     * Type of relationships used to build a hierarchy
     */
    protected RelationshipType relationshipType;

    /**
     * Language the labels of which will be used to sort concepts
     */
    protected String sortLanguage;
    
    /**
     * Default constructor. Sets the root type to {@linkplain #DEFAULT_ROOT_TYPE}
     * and the relationship type to {@linkplain #DEFAULT_RELATIONSHIP_TYPE}
     */
    public HierarchyMethod() {
        this(DEFAULT_ROOT_TYPE, DEFAULT_RELATIONSHIP_TYPE, null);
    }

    /**
     * Constructs a {@link HierarchyMethod} with the provided sort language
     * @param sortLanguage Language the labels of which will be used to sort
     * concepts
     */
    public HierarchyMethod(String sortLanguage) {
        this(DEFAULT_ROOT_TYPE, DEFAULT_RELATIONSHIP_TYPE, sortLanguage);
    }
    
    /**
     * Constructs a {@link HierarchyMethod} with the provided relationship type.
     * This sets the root type to {@linkplain  #DEFAULT_ROOT_TYPE}.
     * @param relationshipType Relationship type to use in building the
     * concept hierarchy
     */
    public HierarchyMethod(RelationshipType relationshipType) {
        this(RootType.TOP_CONCEPTS, relationshipType, null);
    }

    /**
     * Constructs a {@link HierarchyMethod} with the provided relationship type
     * and label sort language
     * @param relationshipType Relationship type to use in building the
     * concept hierarchy
     * @param sortLanguage Language the labels of which will be used to sort
     * concepts
     */
    public HierarchyMethod(RelationshipType relationshipType, String sortLanguage) {
        this(RootType.TOP_CONCEPTS, relationshipType, sortLanguage);
    }
    
    /**
     * Constructs a {@link HierarchyMethod} with the provided root type and
     * relationship type
     * @param rootType Method used to set the hierarchy roots
     * @param relationshipType Relationship type to use in building the hierarchy
     */
    public HierarchyMethod(RootType rootType, RelationshipType relationshipType) {
        this(rootType, relationshipType, null);
    }
    
    /**
     * Constructs a {@link HierarchyMethod} with the provided root type,
     * relationship type, and label sort language
     * @param rootType Method used to set the hierarchy roots
     * @param relationshipType Relationship type to use in building the hierarchy
     * @param sortLanguage Language the labels of which will be used to sort
     * concepts
     */
    public HierarchyMethod(RootType rootType, RelationshipType relationshipType, String sortLanguage) {
        this.rootType = rootType;
        this.relationshipType = relationshipType;
        this.sortLanguage = sortLanguage;
    }
    
    /**
     * Method used to set a hierarchy's roots
     * @return {@link HierarchyMethod.RootType} specifying which concepts are
     * considered as hierarchy roots
     */
    public RootType getRootType() {
        return rootType;
    }

    /**
     * Type of relationships used to build a hierarchy
     * @return {@link HierarchyMethod.RelationshipType} specifying the type of
     * relationship used to build a hierarchy
     */
    public RelationshipType getRelationshipType() {
        return relationshipType;
    }

    /**
     * Language the labels of which will be used to sort concepts
     * @return Sort language
     */
    public String getSortLanguage() {
        return sortLanguage;
    }
    
    /**
     * Sets the hierarchy root type
     * @param rootType {@link HierarchyMethod.RootType} specifying which concepts are
     * considered as hierarchy roots. It {@code relType==null} then th
     * root type property will be set to {@linkplain #DEFAULT_ROOT_TYPE}
     * instead.
     */
    public void setRootType(RootType rootType) {
        if (rootType != null) this.rootType = rootType;
        else this.rootType = DEFAULT_ROOT_TYPE;
    }

    /**
     * Sets the hierarchy relationship type
     * @param relationshipType {@link HierarchyMethod.RelationshipType}
     * specifying the type of relationship used in building a hierarchy.
     * If {@code relationshipType==null} then
     * the relationship type property will be set to
     * {@linkplain #DEFAULT_RELATIONSHIP_TYPE} instead.
     */
    public void setRelationshipType(RelationshipType relationshipType) {
        if (relationshipType != null) this.relationshipType = relationshipType;
        else this.relationshipType = DEFAULT_RELATIONSHIP_TYPE;
    }

    /**
     * Sets the sort language
     * @param sortLanguage Language code
     */
    public void setSortLanguage(String sortLanguage) {
        this.sortLanguage = sortLanguage;
    }
}
