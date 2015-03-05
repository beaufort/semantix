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

import ie.cmrc.util.Multimap;
import java.util.HashMap;
import java.util.List;

/**
 * Enumeration of the SKOS object properties
 * @author Yassine Lassoued
 */
public enum SKOSSemanticProperty implements SKOSObjectProperty {
    
     /**
     * SKOS semantic relation
     */
    semanticRelation(true),
    
    /**
     * SKOS narrower
     */
    narrower(">"),
    
    /**
     * SKOS narrower transitive
     */
    narrowerTransitive,
    
    /**
     * SKOS broader
     */
    broader("<"),
    
    /**
     * SKOS broader transitive
     */
    broaderTransitive,
    
    /**
     * SKOS related
     */
    related("-", true),
    
    /**
     * SKOS mapping relation
     */
    mappingRelation(true),
    
    /**
     * SKOS broad match
     */
    broadMatch("<<"),
    
    /**
     * SKOS close match
     */
    closeMatch("~", true),
    
    /**
     * SKOS exact match
     */
    exactMatch("=", true),
    
    /**
     * SKOS narrow match
     */
    narrowMatch(">>"),
    
    /**
     * SKOS related match
     */
    relatedMatch("--", true);
    
    
    private static HashMap<SKOSSemanticProperty, SKOSSemanticProperty> inverseProperties;
    
    private static Multimap<SKOSSemanticProperty, SKOSSemanticProperty> superProperties;
    
    private static Multimap<SKOSSemanticProperty, SKOSSemanticProperty> directSuperProperties;
    
    static {
        inverseProperties = new HashMap<SKOSSemanticProperty, SKOSSemanticProperty>();
        superProperties = new Multimap<SKOSSemanticProperty, SKOSSemanticProperty>();
        directSuperProperties = new Multimap<SKOSSemanticProperty, SKOSSemanticProperty>();
        
        // Initialise inverse properties map
        inverseProperties  = new HashMap<SKOSSemanticProperty, SKOSSemanticProperty>();
        inverseProperties.put(semanticRelation, semanticRelation);
        inverseProperties.put(narrower, broader);
        inverseProperties.put(broader, narrower);
        inverseProperties.put(broaderTransitive, narrowerTransitive);
        inverseProperties.put(narrowerTransitive, broaderTransitive);
        inverseProperties.put(narrowMatch, broadMatch);
        inverseProperties.put(broadMatch, narrowMatch);
        inverseProperties.put(related, related);
        inverseProperties.put(relatedMatch, relatedMatch);
        inverseProperties.put(mappingRelation, mappingRelation);
        inverseProperties.put(closeMatch, closeMatch);
        inverseProperties.put(exactMatch, exactMatch);
        
        
        // Initialise super properties map
        superProperties.put(broaderTransitive, semanticRelation);
        superProperties.put(broader, broaderTransitive);
        superProperties.put(broader, semanticRelation);
        superProperties.put(broadMatch, broader);
        superProperties.put(broadMatch, broaderTransitive);
        superProperties.put(broadMatch, semanticRelation);
        superProperties.put(broadMatch, mappingRelation);
        
        superProperties.put(narrowerTransitive, semanticRelation);
        superProperties.put(narrower, narrowerTransitive);
        superProperties.put(narrower, semanticRelation);
        superProperties.put(narrowMatch, narrower);
        superProperties.put(narrowMatch, narrowerTransitive);
        superProperties.put(narrowMatch, semanticRelation);
        superProperties.put(narrowMatch, mappingRelation);
        
        superProperties.put(related, semanticRelation);
        superProperties.put(relatedMatch, related);
        superProperties.put(relatedMatch, semanticRelation);
        superProperties.put(relatedMatch, mappingRelation);
        
        superProperties.put(mappingRelation, semanticRelation);
        superProperties.put(closeMatch, mappingRelation);
        superProperties.put(closeMatch, semanticRelation);
        superProperties.put(exactMatch, closeMatch);
        superProperties.put(exactMatch, mappingRelation);
        superProperties.put(exactMatch, semanticRelation);
        
        // Initialise direct super properties map
        directSuperProperties.put(broaderTransitive, semanticRelation);
        directSuperProperties.put(broader, broaderTransitive);
        directSuperProperties.put(broadMatch, broader);
        directSuperProperties.put(broadMatch, mappingRelation);
        
        directSuperProperties.put(narrowerTransitive, semanticRelation);
        directSuperProperties.put(narrower, narrowerTransitive);
        directSuperProperties.put(narrowMatch, narrower);
        directSuperProperties.put(narrowMatch, mappingRelation);
        
        directSuperProperties.put(related, semanticRelation);
        directSuperProperties.put(relatedMatch, related);
        
        directSuperProperties.put(relatedMatch, mappingRelation);
        
        directSuperProperties.put(mappingRelation, semanticRelation);
        directSuperProperties.put(closeMatch, mappingRelation);
        directSuperProperties.put(exactMatch, closeMatch);
        
    }
    
    
    private final boolean symmetric;
    
    private final String symbol;
    
    private SKOSSemanticProperty() {
        this.symbol = null;
        this.symmetric = false;
    }
    
    private SKOSSemanticProperty(String symbol) {
        this.symbol = symbol;
        this.symmetric = false;
    }
    
    private SKOSSemanticProperty(boolean symmetric) {
        this.symmetric = symmetric;
        this.symbol = null;
    }
    
    private SKOSSemanticProperty(String symbol, boolean symmetric) {
        this.symmetric = symmetric;
        this.symbol = symbol;
    }
    
    /**
     * URI of the SKOS object property
     * @return URI of the SKOS object property as a {@code String}
     */
    @Override
    public String uri() {
        return (SKOS.NAMESPACE+this.name());
    }
    
    /**
     * Indicates whether the SKOS object property is symmetric
     * @return {@code true} of the property id symmetric, {code false} otherwise
     */
    @Override
    public boolean isSymmetric() {
        return this.symmetric;
    }
    
    /**
     * Returns the inverse property of this object property
     * @return Inverse property of this object property if any, otherwise {@code null}
     */
    @Override
    public SKOSSemanticProperty getInverseProperty() {
        return inverseProperties.get(this);
    }
    
    /**
     * Returns the list of all super properties of this object property
     * @return {@code List<SKOSObjectProperty>} containing all the SKOS super
     * properties of this object property
     */
    @Override
    public List<SKOSSemanticProperty> getSuperProperties() {
        return superProperties.getAll(this);
    }
    
    /**
     * Returns the list of all direct super properties of this object property
     * @return {@code List<SKOSObjectProperty>} containing all the SKOS direct super
     * properties of this object property
     */
    @Override
    public List<SKOSSemanticProperty> getDirectSuperProperties() {
        return directSuperProperties.getAll(this);
    }
    
    /**
     * {@inheritDoc}
     * @return String representation of the object property. This returns
     * the same result as {@linkplain #uri()}.
     */
    @Override
    public String toString() {
        return this.uri();
    }

    /**
     * Parses a string and returns the matching {@code SKOSSemanticProperty}
     * @param skosProperty {@code String} to parse this may be the URI or local
     * name of a SKOS object property
     * @return {@code SKOSSemanticProperty} matching the parsed {@code String} if
     * possible, otherwise {@code null}
     */
    public static SKOSSemanticProperty fromString(String skosProperty) {
        if (skosProperty != null) {
            for (SKOSSemanticProperty property: SKOSSemanticProperty.values()) {
                if (skosProperty.equals(property.name())||skosProperty.equals("skos:"+property.name())||skosProperty.equals(property.uri())||skosProperty.equals(property.symbol)) return property;
            }
        }
        return null;
    }
    
    
    
    
}
