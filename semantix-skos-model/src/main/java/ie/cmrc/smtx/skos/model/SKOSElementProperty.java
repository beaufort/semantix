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
public enum SKOSElementProperty implements SKOSObjectProperty {

    /**
     * SKOS in scheme
     */
    inScheme,
    
    /**
     * SKOS has top concept
     */
    hasTopConcept,
    
    /**
     * SKOS to concept of
     */
    topConceptOf,
    
    /**
     * SKOS member
     */
    member,
    
    /**
     * SKOSX member of
     */
    memberOf,
    
    /**
     * SKOSX memberTransitive
     */
    memberTransitive,
    
    /**
     * SKOSX memberOfTransitive
     */
    memberOfTransitive;
    
    
    private static final HashMap<SKOSElementProperty, SKOSElementProperty> inverseProperties;
    
    private static final Multimap<SKOSElementProperty, SKOSElementProperty> superProperties;
    
    private static final Multimap<SKOSElementProperty, SKOSElementProperty> directSuperProperties;
    
    static {
        inverseProperties = new HashMap<>();
        superProperties = new Multimap<>();
        directSuperProperties = new Multimap<>();
        
        // Initialise inverse properties map
        inverseProperties.put(hasTopConcept, topConceptOf);
        inverseProperties.put(topConceptOf, hasTopConcept);
        inverseProperties.put(member, memberOf);
        inverseProperties.put(memberOf, member);
        inverseProperties.put(memberTransitive, memberOfTransitive);
        inverseProperties.put(memberOfTransitive, memberTransitive);
        
        // Initialise super properties map
        superProperties.put(topConceptOf, inScheme);
        superProperties.put(member, memberTransitive);
        superProperties.put(memberOf, memberOfTransitive);
        
        // Initialise direct super properties map
        directSuperProperties.put(topConceptOf, inScheme);
        directSuperProperties.put(member, memberTransitive);
        directSuperProperties.put(memberOf, memberOfTransitive);
    }
    
    
    private final boolean symmetric;
    
    private final String symbol;
    
    private SKOSElementProperty() {
        this.symbol = null;
        this.symmetric = false;
    }
    
    private SKOSElementProperty(String symbol) {
        this.symbol = symbol;
        this.symmetric = false;
    }
    
    private SKOSElementProperty(boolean symmetric) {
        this.symmetric = symmetric;
        this.symbol = null;
    }
    
    private SKOSElementProperty(String symbol, boolean symmetric) {
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
    public SKOSElementProperty getInverseProperty() {
        return inverseProperties.get(this);
    }
    
    /**
     * Returns the list of all super properties of this object property
     * @return {@code List<SKOSObjectProperty>} containing all the SKOS super
     * properties of this object property
     */
    @Override
    public List<SKOSElementProperty> getSuperProperties() {
        return superProperties.getAll(this);
    }
    
    /**
     * Returns the list of all direct super properties of this object property
     * @return {@code List<SKOSObjectProperty>} containing all the SKOS direct super
     * properties of this object property
     */
    @Override
    public List<SKOSElementProperty> getDirectSuperProperties() {
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
     * Parses a string and returns the matching {@code SKOSProperty}
     * @param skosProperty {@code String} to parse this may be the URI or local
     * name of a SKOS object property
     * @return {@code SKOSProperty} matching the parsed {@code String} if
     * possible, otherwise {@code null}
     */
    public static SKOSElementProperty fromString(String skosProperty) {
        if (skosProperty != null) {
            for (SKOSElementProperty property: SKOSElementProperty.values()) {
                if (skosProperty.equals(property.name())||skosProperty.equals("skos:"+property.name())||skosProperty.equals(property.uri())||skosProperty.equals(property.symbol)) return property;
            }
        }
        return null;
    }
    
    
    
    
}
