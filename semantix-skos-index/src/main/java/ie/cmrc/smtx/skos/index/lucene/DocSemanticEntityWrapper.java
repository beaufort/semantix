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

package ie.cmrc.smtx.skos.index.lucene;

import ie.cmrc.smtx.base.AbstractSemanticEntity;
import ie.cmrc.smtx.base.SemanticEntity;
import ie.cmrc.smtx.base.serialisation.ElementSetName;
import ie.cmrc.smtx.base.serialisation.Namespaces;
import ie.cmrc.smtx.skos.index.IndexField;
import ie.cmrc.util.Term;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexableField;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.QName;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author Yassine Lassoued <y.lassoued@ucc.ie>
 */
class DocSemanticEntityWrapper extends AbstractSemanticEntity implements SemanticEntity {
    
    protected final Document doc;

    protected static final String CONCEPT = "Concept";
    protected static final String PREF_LABEL = "prefLabel";
    
    public DocSemanticEntityWrapper(Document doc) {
        super(doc.get(IndexField.Searchable.URI.fieldName()));
        this.doc = doc;
    }

    @Override
    public String getPrefLabel(String language) {
        return doc.get(IndexField.Searchable.PREF_LABEL.field(language).getQualifiedString());
    }

    @Override
    public List<String> getAltLabels(String language) {
        
        String[] values = doc.getValues(IndexField.Searchable.LABEL.field(language).getQualifiedString());
        // values is never null according to the Document::getValues(String) specification
        List<String> altLabels = new ArrayList<String>(values.length);
        altLabels.addAll(Arrays.asList(values));
        return altLabels;
    }

    @Override
    public SemanticEntity setPrefLabel(String prefLabel, String language) {
        class Local {};
        Method thisMethod = Local.class.getEnclosingMethod();
        throw new UnsupportedOperationException("Method ["+thisMethod.toGenericString()+"] is not supported by this read only implementation.");
    }

    @Override
    public SemanticEntity addAltLabel(String altLabel, String language) {
        class Local {};
        Method thisMethod = Local.class.getEnclosingMethod();
        throw new UnsupportedOperationException("Method ["+thisMethod.toGenericString()+"] is not supported by this read only implementation.");
    }

    @Override
    public SemanticEntity removePrefLabel(String language) {
        class Local {};
        Method thisMethod = Local.class.getEnclosingMethod();
        throw new UnsupportedOperationException("Method ["+thisMethod.toGenericString()+"] is not supported by this read only implementation.");
    }

    @Override
    public SemanticEntity removeAltLabel(String altLabel, String language) {
        class Local {};
        Method thisMethod = Local.class.getEnclosingMethod();
        throw new UnsupportedOperationException("Method ["+thisMethod.toGenericString()+"] is not supported by this read only implementation.");
    }

    @Override
    public SemanticEntity removeAltLabels(String language) {
        class Local {};
        Method thisMethod = Local.class.getEnclosingMethod();
        throw new UnsupportedOperationException("Method ["+thisMethod.toGenericString()+"] is not supported by this read only implementation.");
    }

    @Override
    public boolean hasPrefLabel(String language) {
        return (doc.get(IndexField.Searchable.PREF_LABEL.field(language).getQualifiedString()) != null);
    }

    @Override
    public boolean hasAltLabel(String value, String language) {
        if (value != null) {
            String[] values = doc.getValues(IndexField.Searchable.LABEL.field(language).getQualifiedString());
            for (int i=0; i<values.length; i++) {
                if (values.equals(values[i])) return true;
            }
        }
        return false;
    }

    @Override
    public boolean hasAltLabel(String language) {
        return (doc.get(IndexField.Searchable.LABEL.field(language).getQualifiedString()) != null);
    }

    @Override
    public List<String> getPrefLabelLanguages() {
        List<IndexableField> fields = doc.getFields();
        List<String> languages = new ArrayList<>(fields.size());
        for (IndexableField field: fields) {
            String qname = field.name();
            Term term = new Term(qname);
            if (IndexField.Searchable.PREF_LABEL.fieldName().equals(term.getString())) {
                String language = term.getLanguage();
                if (!languages.contains(language)) languages.add(language);
            }
        }
        return languages;
    }

    @Override
    public List<String> getAltLabelLanguages() {
        List<IndexableField> fields = doc.getFields();
        List<String> languages = new ArrayList<>(fields.size());
        for (IndexableField field: fields) {
            String qname = field.name();
            Term term = new Term(qname);
            if (IndexField.Searchable.LABEL.fieldName().equals(term.getString())) {
                String language = term.getLanguage();
                if (!languages.contains(language)) languages.add(language);
            }
        }
        return languages;
    }

    @Override
    public String getSemanticType() {
        String semanticType = this.doc.get(IndexField.Filterable.COLLECTION.fieldName());
        return semanticType;
    }

    @Override
    public List<String> getSemanticTypes() {
        String[] semTypeArray = this.doc.getValues(IndexField.Filterable.COLLECTION.fieldName());
        if (semTypeArray != null) return Arrays.asList(semTypeArray);
        else return new ArrayList<>(0);
    }

    @Override
    public SemanticEntity addSemanticType(String string) {
        class Local {};
        Method thisMethod = Local.class.getEnclosingMethod();
        throw new UnsupportedOperationException("Method ["+thisMethod.toGenericString()+"] is not supported by this read only implementation.");
    }

    @Override
    public SemanticEntity removeSemanticType(String string) {
        class Local {};
        Method thisMethod = Local.class.getEnclosingMethod();
        throw new UnsupportedOperationException("Method ["+thisMethod.toGenericString()+"] is not supported by this read only implementation.");
    }

    @Override
    public SemanticEntity removeSemanticTypes() {
        class Local {};
        Method thisMethod = Local.class.getEnclosingMethod();
        throw new UnsupportedOperationException("Method ["+thisMethod.toGenericString()+"] is not supported by this read only implementation.");
    }

    @Override
    public boolean hasSemanticType(String string) {
        List<String> semTypes = this.getSemanticTypes();
        return semTypes.contains(string);
    }

    @Override
    public List<String> getSemanticTypesTransitive() {
        String[] semTypeArray = this.doc.getValues(IndexField.Filterable.COLLECTION_TRANSITIVE.fieldName());
        if (semTypeArray != null) return Arrays.asList(semTypeArray);
        else return new ArrayList<>(0);
    }

    @Override
    public boolean hasSemanticTypeTransitive(String string) {
        List<String> semTypes = this.getSemanticTypesTransitive();
        return semTypes.contains(string);
    }
    
    @Override
    public boolean isReadOnly() {
        return true;
    }

    /**
     * {@inheritDoc}
     * @param elementSet {@inheritDoc}
     * @param language {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public Element toXMLElement(ElementSetName elementSet, String language) {
        ElementSetName esn = elementSet;
        if (esn == null) esn = ElementSetName.BRIEF;
        if (this.getURI()!=null && !this.getURI().isEmpty()) {
            Element resourceElt;
            
            resourceElt = DocumentHelper.createElement(new QName(CONCEPT, Namespaces.SKOS));
            QName aboutQN = new QName("about", Namespaces.RDF);
            QName resourceQN = new QName("resource", Namespaces.RDF);

            resourceElt.addAttribute(aboutQN, this.getURI());

            if (esn.compareTo(ElementSetName.BRIEF) >= 0) {            
            
                
                QName xmlLang = new QName("lang", Namespaces.XML);

                List<Element> prefLabelElts = this.getXMLPrefLabels(language);
                for (Element prefLabelElt: prefLabelElts) resourceElt.add(prefLabelElt);
            }
            return resourceElt;
        }
        else return null;
    }
    
    
    private List<Element> getXMLPrefLabels(String language) {
        QName xmlLang = new QName("lang", Namespaces.XML);
        
        List<Element> xmlAnnotations = new ArrayList<Element>();
        
        if (language != null) {
            String prefLabel = this.getPrefLabel(language);
            if (prefLabel != null) {
                Element element = DocumentHelper.createElement(new QName(PREF_LABEL, Namespaces.SKOS)).addText(prefLabel);
                element.addAttribute(xmlLang, language);
                xmlAnnotations.add(element);
            }
        }
        else {
            List<String> languages = this.getPrefLabelLanguages();
            for (String lang: languages) {
                String annotation = this.getPrefLabel(lang);
                if (annotation!=null) {
                    Element element = DocumentHelper.createElement(new QName(PREF_LABEL, Namespaces.SKOS)).addText(annotation);
                    if (lang!=null && !lang.isEmpty()) element.addAttribute(xmlLang, language);
                    xmlAnnotations.add(element);
                }
            }
        }
        return xmlAnnotations;
    }
    
    /**
     * {@inheritDoc}
     * @param elementSet {@inheritDoc}
     * @param language {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public JSONObject toJSONObject(ElementSetName elementSet, String language) {
        if (this.getURI()!=null && !this.getURI().isEmpty()) {
            
            ElementSetName esn = elementSet;
            if (esn == null) esn = ElementSetName.BRIEF;
            JSONObject jsonObject = new JSONObject();
            
            jsonObject.put("@id", this.getURI());
            jsonObject.put("@type", CONCEPT);

            if (esn.compareTo(ElementSetName.BRIEF) >= 0) {
                JSONArray prefLabelArray = this.getJSONprefLable(language);
                if (!prefLabelArray.isEmpty()) jsonObject.put(PREF_LABEL, prefLabelArray);
            }
            return jsonObject;
            
        }
        return null;
    }
    
    private JSONArray getJSONprefLable(String language) {
        JSONArray jsonAnnotations = new JSONArray();
        
        if (language != null) {
            String prefLabel = this.getPrefLabel(language);
            if (prefLabel != null) {
                JSONObject jsonAnnotation = new JSONObject();
                jsonAnnotation.put("@language", language);
                jsonAnnotation.put("@value", prefLabel);
                jsonAnnotations.add(jsonAnnotation);
            }
        }
        else {
            List<String> languages = this.getPrefLabelLanguages();
            for (String lang: languages) {
                String annotation = this.getPrefLabel(lang);
                if (annotation!=null) {
                    JSONObject jsonAnnotation = new JSONObject();
                    if (lang != null) jsonAnnotation.put("@language", lang);
                    jsonAnnotation.put("@value", annotation);
                    jsonAnnotations.add(jsonAnnotation);
                }
            }
        }
        return jsonAnnotations;
    }
    
}
