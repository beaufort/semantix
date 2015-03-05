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

package ie.cmrc.smtx.base;

import ie.cmrc.smtx.base.serialisation.ElementSetName;
import ie.cmrc.util.Multimap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.dom4j.Element;
import org.json.simple.JSONObject;

/**
 * A basic {@link ie.cmrc.smtx.base.SemanticEntity} implementation. This
 * implementation does not support transitive semantic types.
 * @author Yassine Lassoued <y.lassoued@ucc.ie>
 */
public class DefaultSemanticEntity extends AbstractSemanticEntity {

    private final Map<String,String> prefLabels;
    private final Multimap<String,String> altLabels;
    private final List<String> semanticTypes;
    
    public DefaultSemanticEntity(String uri) {
        super(uri);
        this.prefLabels = new HashMap<>();
        this.altLabels = new Multimap<>();
        this.semanticTypes = new ArrayList<>();
    }
    
    @Override
    public String getPrefLabel(String language) {
        return this.prefLabels.get(language);
    }

    @Override
    public List<String> getAltLabels(String language) {
        return this.altLabels.getAll(language);
    }

    @Override
    public SemanticEntity setPrefLabel(String prefLabel, String language) {
        this.prefLabels.put(language, prefLabel);
        return this;
    }

    @Override
    public SemanticEntity addAltLabel(String altLabel, String language) {
        if (!this.altLabels.containsEntry(language, altLabel)) this.altLabels.put(language, altLabel);
        return this;
    }

    @Override
    public SemanticEntity removePrefLabel(String language) {
        this.prefLabels.remove(language);
        return this;
    }

    @Override
    public SemanticEntity removeAltLabel(String altLabel, String language) {
        this.altLabels.remove(language, altLabel);
        return this;
    }

    @Override
    public SemanticEntity removeAltLabels(String language) {
        this.altLabels.removeAll(language);
        return this;
    }

    @Override
    public boolean hasPrefLabel(String language) {
        return this.prefLabels.containsKey(language);
    }

    @Override
    public boolean hasAltLabel(String value, String language) {
        return this.altLabels.containsEntry(language, value);
    }

    @Override
    public boolean hasAltLabel(String language) {
        return this.altLabels.containsKey(language);
    }

    @Override
    public List<String> getPrefLabelLanguages() {
        List<String> prefLabelLangs = new ArrayList<>(this.prefLabels.keySet());
        return Collections.unmodifiableList(prefLabelLangs);
    }

    @Override
    public List<String> getAltLabelLanguages() {
        List<String> altLabelLangs = new ArrayList<>(this.altLabels.keySet());
        return Collections.unmodifiableList(altLabelLangs);
    }

    @Override
    public String getSemanticType() {
        if (!this.semanticTypes.isEmpty()) return this.semanticTypes.get(0);
        else return null;
    }

    @Override
    public List<String> getSemanticTypes() {
        return Collections.unmodifiableList(this.semanticTypes);
    }

    @Override
    public SemanticEntity addSemanticType(String semanticType) {
        if (semanticType!=null && !semanticType.isEmpty() && !this.semanticTypes.contains(semanticType)) this.semanticTypes.add(semanticType);
        return this;
    }

    @Override
    public SemanticEntity removeSemanticType(String semanticType) {
        this.semanticTypes.remove(semanticType);
        return this;
    }

    @Override
    public SemanticEntity removeSemanticTypes() {
        this.semanticTypes.clear();
        return this;
    }

    @Override
    public boolean hasSemanticType(String semanticType) {
        return this.semanticTypes.contains(semanticType);
    }

    /**
     * In this implementation, this is equivalent to {@linkplain #getSemanticTypes()}
     * @return In this implementation, same as {@linkplain #getSemanticTypes()}
     */
    @Override
    public List<String> getSemanticTypesTransitive() {
        return Collections.unmodifiableList(this.semanticTypes);
    }

    /**
     * In this implementation, this is equivalent to {@linkplain #hasSemanticType()}
     * @param semanticType {@inheritDoc}
     * @return In this implementation, same as {@linkplain #hasSemanticType()}
     */
    @Override
    public boolean hasSemanticTypeTransitive(String semanticType) {
        return this.semanticTypes.contains(semanticType);
    }
    
    @Override
    public boolean isReadOnly() {
        return false;
    }

    @Override
    public Element toXMLElement(ElementSetName elementSet, String language) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public JSONObject toJSONObject(ElementSetName elementSet, String language) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
