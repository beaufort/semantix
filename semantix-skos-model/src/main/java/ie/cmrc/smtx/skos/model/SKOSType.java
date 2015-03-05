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
 * Enumeration of the SKOS types (classes), such as Concept, Concept Scheme,
 * Collection, etc.
 * @author Yassine Lassoued
 */
public enum SKOSType {
    /**
     * SKOS Concept Scheme
     *//**
     * SKOS Concept Scheme
     *//**
     * SKOS Concept Scheme
     *//**
     * SKOS Concept Scheme
     */
    ConceptScheme,
    
    /**
     * SKOS Concept
     */
    Concept,
    
    /**
     * SKOS Collection
     */
    Collection,
    
    /**
     * SKOS Ordered Collection - Not supported
     */
    /*ORDERED_COLLECTION("OrderedCollection")*/;
    
    
    
    /**
     * URI of the SKOS type
     * @return URI of the SKOS type
     */
    public String uri() {
        return (SKOS.NAMESPACE+this.name());
    }
    
    /**
     * {@inheritDoc}
     * @return String representation of the SKOS class. This returns
     * the same result as {@linkplain #uri()}.
     */
    @Override
    public String toString() {
        return this.uri();
    }

    /**
     * Parses a string and returns the matching {@code SKOSType}
     * @param skosType {@code String} to parse
     * @return {@code SKOSProperty} matching the parsed {@code String} if
     * possible, otherwise {@code null}
     */
    public static SKOSType fromString(String skosType) {
        if (skosType != null) {
            for (SKOSType type: SKOSType.values()) {
                if (skosType.equals(type.name()) || skosType.equals(type.uri())) return type;
            }
        }
        return null;
    }
    
}
