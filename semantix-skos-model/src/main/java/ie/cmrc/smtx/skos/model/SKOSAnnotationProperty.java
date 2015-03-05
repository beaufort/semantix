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

package ie.cmrc.smtx.skos.model;


/**
 * Enumeration of the SKOS annotation properties
 * @author Yassine Lassoued
 */
public enum SKOSAnnotationProperty {
    
    // Lexical labels
    
    /**
     * SKOS preferred label
     */
    prefLabel(true),
    
    /**
     * SKOS alternative label
     */
    altLabel(true),
    
    /**
     * SKOS hidden label
     */
    hiddenLabel(true),
    
    
    // Documentation annotations
    
    /**
     * SKOS definition
     */
    definition,
    
    /**
     * SKOS note
     */
    note,
    
    /**
     * SKOS change note
     */
    changeNote,
    
    /**
     * SKOS editorial note
     */
    editorialNote,
    
    /**
     * SKOS example
     */
    example,
    
    /**
     * SKOS history note
     */
    historyNote,
    
    /**
     * SKOS scope note
     */
    scopeNote;
    
    
    /**
     * Indicates whether this SKOS annotation property is an RDFS label
     */
    private final boolean isRDFSLabel;
    
    /**
     * Default constructor
     */
    private SKOSAnnotationProperty() {
        this(false);
    }
    
    /**
     * Constructs a {@code SKOSAnnotation}
     * @param isRDFSLabel If {@code true} then this SKOSAnnotationProperty is an RDFS label
     */
    private SKOSAnnotationProperty(boolean isRDFSLabel) {
        this.isRDFSLabel = isRDFSLabel;
    }
    
    /**
     * URI of the SKOS annotation property
     * @return URI of the SKOS annotation property
     */
    public String uri() {
        return (SKOS.NAMESPACE+this.name());
    }
    
    /**
     * {@inheritDoc}
     * @return String representation of the annotation property. This returns
     * the same result as {@linkplain #uri()}.
     */
    @Override
    public String toString() {
        return this.uri();
    }

    /**
     * Parses a string and returns the matching {@code SKOSAnnotation}
     * @param skosAnnotation {@code String} to parse this may be the URI or local
     * name of a SKOS annotation
     * @return {@code SKOSAnnotation} matching the parsed {@code String} if
     * possible, otherwise {@code null}
     */
    public static SKOSAnnotationProperty fromString(String skosAnnotation) {
        if (skosAnnotation != null) {
            for (SKOSAnnotationProperty annotation: SKOSAnnotationProperty.values()) {
                if (skosAnnotation.equals(annotation.name())||skosAnnotation.equals("skos:"+annotation.name())||skosAnnotation.equals(annotation.uri())) return annotation;
            }
        }
        return null;
    }
    
    /**
     * Indicates whether this SKOS annotation property may have more than one value per language
     * @return {@code tru} if this SKOS annotation property may have more than
     * one value per language; {@code false} otherwise
     */
    public boolean isMultiple() {
        return this!=prefLabel;
    }
    
    /**
     * Indicates whether this SKOS annotation property is an RDFS label
     * @return {@code true} if this SKOS annotation property is an RDFS label;
     * {@code false} otherwise
     */
    public boolean isRDFSLabel() {
        return this.isRDFSLabel;
    }
}
