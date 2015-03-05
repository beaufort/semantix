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

import java.util.List;

/**
 * Enumeration of the SKOS object properties
 * @author Yassine Lassoued
 */
public interface SKOSObjectProperty {
    
    /**
     * URI of the SKOS object property
     * @return URI of the SKOS object property as a {@code String}
     */
    String uri();
    
    /**
     * Name of the SKOS object property
     * @return Name of the SKOS object property
     */
    String name();
    
    /**
     * Indicates whether the SKOS object property is symmetric
     * @return {@code true} of the property id symmetric, {code false} otherwise
     */
    boolean isSymmetric();
    
    /**
     * Returns the inverse property of this object property
     * @return Inverse property of this object property if any, otherwise {@code null}
     */
    SKOSObjectProperty getInverseProperty();
    
    /**
     * Returns the list of all super properties of this object property
     * @return {@code List<SKOSObjectProperty>} containing all the SKOS super
     * properties of this object property
     */
    List<? extends SKOSObjectProperty> getSuperProperties();
    
    /**
     * Returns the list of all direct super properties of this object property
     * @return {@code List<SKOSObjectProperty>} containing all the SKOS direct super
     * properties of this object property
     */
    public List<? extends SKOSObjectProperty> getDirectSuperProperties();
    
    
    
    
}
