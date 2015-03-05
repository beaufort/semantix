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

package ie.cmrc.smtx.base.serialisation;

import java.util.ArrayList;
import java.util.List;

/**
 * An {@code ElementSetName} specifies the set of information about a
 * {@link ie.cmrc.skos.core.SKOSResource} to serialise
 * @author Yassine Lassoued <y.lassoued@ucc.ie>
 */
public enum ElementSetName {
    /**
     * Abstract information. The template for abstract information is:<br/>
     * <pre>
     * ?resource    rdf:type   ?type .}
     * </pre>
     */
    ABSTRACT("abstract"),
    
    /**
     * Brief information. The template for brief information is:<br/>
     * <pre>
     * ?resource    rdf:type        ?type ;<br/>
     *              skos:prefLabel  ?pl .
     * </pre>
     */
    BRIEF("brief"),
    
    /**
     * Summary information. The template for summary information is:<br/>
     * <pre>
     * ?resource    rdf:type        ?type ;<br/>
     *              skos:prefLabel  ?pl ;<br/>
     *              skos:definition ?def ;<br/>
     *              skos:inScheme   ?cs .
     * </pre>
     */
    SUMMARY("summary"),
    
    /**
     * Full information. The template for full information is:<br/>
     * <pre>
     * ?resource    rdf:type            ?type ;<br/>
     *              skos:prefLabel      ?pl ;<br/>
     *              skos:definition     ?def ;<br/>
     *              skos:inScheme       ?cs ;<br/>
     *              skos:altLabel       ?al .
     * </pre>
     */
    FULL("full"),
    
    /**
     * Extended information. The template for abstract information is:<br/>
     * <pre>
     * ?resource    rdf:type            ?type ;<br/>
     *              skos:prefLabel      ?pl ;<br/>
     *              skos:definition     ?def ;<br/>
     *              skos:inScheme       ?cs ;<br/>
     *              skos:altLabel       ?al ;<br/>
     *              skos:hiddenLabel    ?hl ;<br/>
     *              skos:topConceptOf   ?tcs .
     * </pre>
     */
    EXTENDED("extended");

    /**
     * String value of the element set name
     */
    private final String value;

    /**
     * Creates an element set name using the associated String value
     * @param value String value of the element set name
     */
    ElementSetName(String value) {
        this.value = value;
    }

    /**
     * String value of the element set name
     * @return String value of the element set name
     */
    public String value() {
        return this.value;
    }

    /**
     * String representation of the element set name
     * @return String representation of the element set name
     */
    @Override
    public String toString() {
        return this.value;
    }

    /**
     * Parses an element set name from the provide String
     * @param eltSetName String to parse
     * @return Element set name matching the provide text if any, otherwise {@code null}
     */
    public static ElementSetName fromString(String eltSetName) {
        if (eltSetName != null) {
            for (ElementSetName esn: ElementSetName.values()) {
                if (eltSetName.equals(esn.value)) return esn;
            }
        }
        return null;
        //throw new IllegalArgumentException("\""+eltSetName+"\" is not a valid ElementSetName value.");
    }

    /**
     * Lists the possible values of element set names
     * @return {@code List<String>} containing all the possible string values of element set names
     */
    public static List<String> possibleValues() {
        List<String> pv = new ArrayList<String>();
        for (ElementSetName esn: ElementSetName.values()) {
            pv.add(esn.value);
        }

        return pv;
    }
}
