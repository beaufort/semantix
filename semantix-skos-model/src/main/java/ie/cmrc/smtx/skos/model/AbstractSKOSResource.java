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

import ie.cmrc.smtx.base.AbstractSemanticEntity;
import ie.cmrc.smtx.base.SemanticEntity;
import ie.cmrc.smtx.skos.model.util.CloseableIterator;
import ie.cmrc.smtx.base.serialisation.ElementSetName;
import ie.cmrc.smtx.base.serialisation.Namespaces;
import ie.cmrc.util.Term;
import java.util.ArrayList;
import java.util.List;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.QName;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author Yassine Lassoued
 */
public abstract class AbstractSKOSResource extends AbstractSemanticEntity implements SKOSResource {


    /**
     * Constructs a {@link SKOSResource} with the provided URI
     * @param uri URI of the resource to construct
     */
    protected AbstractSKOSResource(String uri) {
        super(uri);
    }

    /**
     * {@inheritDoc}
     * @param language {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public String getPrefLabel(String language) {
        return this.getAnnotation(SKOSAnnotationProperty.prefLabel, language);
    }

    /**
     * {@inheritDoc}
     * @param language {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public List<String> getAltLabels(String language) {
        return this.getAnnotations(SKOSAnnotationProperty.altLabel, language);
    }

    /**
     * {@inheritDoc}
     * @param prefLabel {@inheritDoc}
     * @param language {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public SKOSResource setPrefLabel(String prefLabel, String language) {
        return this.addAnnotation(SKOSAnnotationProperty.prefLabel, prefLabel, language);
    }

    /**
     * {@inheritDoc}
     * @param altLabel {@inheritDoc}
     * @param language {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public SKOSResource addAltLabel(String altLabel, String language) {
        return this.addAnnotation(SKOSAnnotationProperty.altLabel, altLabel, language);
    }

    /**
     * {@inheritDoc}
     * @param language {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public SKOSResource removePrefLabel(String language) {
        return this.removeAnnotations(SKOSAnnotationProperty.prefLabel, language);
    }

    /**
     * {@inheritDoc}
     * @param altLabel {@inheritDoc}
     * @param language {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public SKOSResource removeAltLabel(String altLabel, String language) {
        return this.removeAnnotation(SKOSAnnotationProperty.altLabel, altLabel, language);
    }

    /**
     * {@inheritDoc}
     * @param language {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public SKOSResource removeAltLabels(String language) {
        return this.removeAnnotations(SKOSAnnotationProperty.altLabel, language);
    }

    /**
     * {@inheritDoc}
     * @param language {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public boolean hasPrefLabel(String language) {
        return this.hasAnnotation(SKOSAnnotationProperty.prefLabel, language);
    }

    /**
     * {@inheritDoc}
     * @param value {@inheritDoc}
     * @param language {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public boolean hasAltLabel(String value, String language) {
        return this.hasAnnotation(SKOSAnnotationProperty.altLabel, value, language);
    }

    /**
     * {@inheritDoc}
     * @param language {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public boolean hasAltLabel(String language) {
        return this.hasAnnotation(SKOSAnnotationProperty.altLabel, language);
    }

    /**
     * {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public List<String> getPrefLabelLanguages() {
        return this.getAnnotationLanguages(SKOSAnnotationProperty.prefLabel);
    }

    /**
     * {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public List<String> getAltLabelLanguages() {
        return this.getAnnotationLanguages(SKOSAnnotationProperty.altLabel);
    }

    /**
     * {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public boolean isReadOnly() {
        return false;
    }
    
    
    /**
     * {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public Boolean isSKOSConceptScheme() {
        return (this instanceof SKOSConceptScheme);
    }

    /**
     * {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public Boolean isSKOSConcept() {
        return (this instanceof SKOSConcept);
    }

    /**
     * {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public Boolean isSKOSCollection() {
        return (this instanceof SKOSCollection);
    }

    /**
     * {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public SKOSConceptScheme asSKOSConceptScheme() {
        return (SKOSConceptScheme) this;
    }

    /**
     * {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public SKOSConcept asSKOSConcept() {
        return (SKOSConcept) this;
    }

    /**
     * {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public SKOSCollection asSKOSCollection() {
        return (SKOSCollection) this;
    }

    @Override
    public String getSemanticType() {
        CloseableIterator<SKOSResource> resources = this.listRelationshipSources(SKOSElementProperty.member);
        if (resources.hasNext()) {
            SKOSResource resource = resources.next();
            if (resource != null) return resource.getURI();
        }
        resources.close();
        return null;
    }

    @Override
    public List<String> getSemanticTypes() {
        List<String> semanticTypes = new ArrayList<>();
        CloseableIterator<SKOSResource> resources = this.listRelationshipSources(SKOSElementProperty.member);
        while (resources.hasNext()) {
            SKOSResource resource = resources.next();
            if (resource != null) semanticTypes.add(resource.getURI());
        }
        resources.close();
        return semanticTypes;
    }

    @Override
    public SemanticEntity addSemanticType(String collectionURI) {
        if (collectionURI != null) this.makeRelation(collectionURI, SKOSElementProperty.member);
        return this;
    }

    @Override
    public SemanticEntity removeSemanticType(String collectionURI) {
        this.removeAsRelation(collectionURI, SKOSElementProperty.member);
        return this;
    }

    @Override
    public SemanticEntity removeSemanticTypes() {
        this.removeAsRelation(SKOSElementProperty.member);
        return this;
    }

    @Override
    public boolean hasSemanticType(String collectionURI) {
        return this.isRelation(collectionURI, SKOSElementProperty.member);
    }

    @Override
    public List<String> getSemanticTypesTransitive() {
        List<String> semanticTypes = new ArrayList<>();
        CloseableIterator<SKOSResource> resources = this.listRelationshipSources(SKOSElementProperty.memberTransitive);
        while (resources.hasNext()) {
            SKOSResource resource = resources.next();
            if (resource != null) semanticTypes.add(resource.getURI());
        }
        resources.close();
        return semanticTypes;
    }

    @Override
    public boolean hasSemanticTypeTransitive(String collectionURI) {
        return this.isRelation(collectionURI, SKOSElementProperty.memberTransitive);
    }
    

    /**
     * {@inheritDoc}<br/>
     * Checks if the argument object is equal to the resource. Two resources
     * are equal if they have the same URI
     * @param obj {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if(this == obj) {
            return true;
        }
        if (obj==null) return false;
        if (!(obj instanceof SKOSResource)) {
              return false;
        }
        SKOSResource res = (SKOSResource)obj;

        if (this.getURI()!=null) {
           return (this.getURI().equals(res.getURI()));
        }
        else {
           return (res.getURI()==null);
        }
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
            
            resourceElt = DocumentHelper.createElement(new QName(this.getSkosType().name(), Namespaces.SKOS));
            QName aboutQN = new QName("about", Namespaces.RDF);
            QName resourceQN = new QName("resource", Namespaces.RDF);

            resourceElt.addAttribute(aboutQN, this.getURI());

            if (esn.compareTo(ElementSetName.BRIEF) >= 0) {            
            
                
                QName xmlLang = new QName("lang", Namespaces.XML);

                List<Element> prefLabelElts = this.getXMLAnnotations(SKOSAnnotationProperty.prefLabel, language);
                for (Element prefLabelElt: prefLabelElts) resourceElt.add(prefLabelElt);
                

                if (esn.compareTo(ElementSetName.SUMMARY) >= 0) {
                    
                    QName inSchemeQN = new QName(SKOSElementProperty.inScheme.name(), Namespaces.SKOS);
                    QName conceptSchemeQN = new QName(SKOSType.ConceptScheme.name(), Namespaces.SKOS);
                    CloseableIterator<SKOSResource> inSchemeIter = this.listRelations(SKOSElementProperty.inScheme);
                    while (inSchemeIter.hasNext()) {
                        SKOSResource cs = inSchemeIter.next();
                        //resourceElt.addElement(inSchemeQN).addAttribute(resourceQN, cs.getURI());
                        resourceElt.addElement(inSchemeQN).addElement(conceptSchemeQN).addAttribute(aboutQN, cs.getURI());
                    }
                    inSchemeIter.close();
                    
                    List<Element> defElts = this.getXMLAnnotations(SKOSAnnotationProperty.definition, language);
                    for (Element defElt: defElts) resourceElt.add(defElt);
                    
                    if (esn.compareTo(ElementSetName.FULL) >= 0) { 
                    
                        List<Element> altLabelElts = this.getXMLAnnotations(SKOSAnnotationProperty.altLabel, language);
                        for (Element altLabelElt: altLabelElts) resourceElt.add(altLabelElt);
                        
                        if (esn.compareTo(ElementSetName.EXTENDED) >= 0) {
                            
                            CloseableIterator<SKOSResource> topConceptOf = this.listRelations(SKOSElementProperty.topConceptOf);
                            while (topConceptOf.hasNext()) {
                                SKOSResource cs = topConceptOf.next();
                                //resourceElt.addElement(inSchemeQN).addAttribute(resourceQN, cs.getURI());
                                resourceElt.addElement(inSchemeQN).addElement(conceptSchemeQN).addAttribute(aboutQN, cs.getURI());
                            }
                            topConceptOf.close();
                            
                            for (SKOSAnnotationProperty property: SKOSAnnotationProperty.values()) {
                                if (property!=SKOSAnnotationProperty.prefLabel && property!=SKOSAnnotationProperty.altLabel && property!=SKOSAnnotationProperty.definition) {
                                    List<Element> annotationElts = this.getXMLAnnotations(property, language);
                                    for (Element annotationElt: altLabelElts) resourceElt.add(annotationElt);
                                }
                            }
                            
                        }
                    }
                }
            }
            return resourceElt;
        }
        else return null;
    }
    
    
    private List<Element> getXMLAnnotations(SKOSAnnotationProperty property, String language) {
        QName xmlLang = new QName("lang", Namespaces.XML);
        
        List<Element> xmlAnnotations = new ArrayList<Element>();
        
        if (language != null) {
            List<String> annotations = this.getAnnotations(property, language);
            for (String annotation: annotations) {
                Element element = DocumentHelper.createElement(new QName(property.name(), Namespaces.SKOS)).addText(annotation);
                element.addAttribute(xmlLang, language);
                xmlAnnotations.add(element);
            }
        }
        else {
            List<Term> annotationTerms = this.getAnnotations(property);
            for (Term annotationTerm: annotationTerms) {
                String annotation = annotationTerm.getString();
                String lang = annotationTerm.getLanguage();
                Element element = DocumentHelper.createElement(new QName(property.name(), Namespaces.SKOS)).addText(annotation);
                if (lang!=null && !lang.isEmpty()) element.addAttribute(xmlLang, language);
                xmlAnnotations.add(element);
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
            jsonObject.put("@type", this.getSkosType().name());

            if (esn.compareTo(ElementSetName.BRIEF) >= 0) {
                

                JSONArray prefLabelArray = this.getJSONAnnotations(SKOSAnnotationProperty.prefLabel, language);
                if (!prefLabelArray.isEmpty()) jsonObject.put(SKOSAnnotationProperty.prefLabel.name(), prefLabelArray);

                if (esn.compareTo(ElementSetName.SUMMARY) >= 0) {
                    
                    JSONArray inSchemes = new JSONArray();
                    CloseableIterator<SKOSResource> inSchemeIter = this.listRelations(SKOSElementProperty.inScheme);
                    while (inSchemeIter.hasNext()) {
                        SKOSResource cs = inSchemeIter.next();
                        inSchemes.add(cs.getURI());
                    }
                    inSchemeIter.close();
                    if (!inSchemes.isEmpty()) jsonObject.put(SKOSElementProperty.inScheme.name(), inSchemes);
                    
                    JSONArray defArray = this.getJSONAnnotations(SKOSAnnotationProperty.definition, language);
                    if (!defArray.isEmpty()) jsonObject.put(SKOSAnnotationProperty.definition.name(), defArray);
                    
                    
                    if (esn.compareTo(ElementSetName.FULL) >= 0) { 
                    
                        JSONArray altLabelArray = this.getJSONAnnotations(SKOSAnnotationProperty.altLabel, language);
                        if (!altLabelArray.isEmpty()) jsonObject.put(SKOSAnnotationProperty.altLabel.name(), altLabelArray);
                        
                        if (esn.compareTo(ElementSetName.EXTENDED) >= 0) {
                            
                            JSONArray topConceptOfArray = new JSONArray();
                            CloseableIterator<SKOSResource> topConceptOf = this.listRelations(SKOSElementProperty.topConceptOf);
                            while (topConceptOf.hasNext()) {
                                SKOSResource cs = topConceptOf.next();
                                topConceptOfArray.add(cs.getURI());
                            }
                            topConceptOf.close();
                            if (!topConceptOfArray.isEmpty()) jsonObject.put(SKOSElementProperty.topConceptOf.name(), topConceptOfArray);
                            
                            for (SKOSAnnotationProperty property: SKOSAnnotationProperty.values()) {
                                if (property!=SKOSAnnotationProperty.prefLabel && property!=SKOSAnnotationProperty.altLabel && property!=SKOSAnnotationProperty.definition) {
                                    JSONArray annotationArray = this.getJSONAnnotations(property, language);
                                    if (!annotationArray.isEmpty()) jsonObject.put(property.name(), annotationArray);
                                }
                            }
                        }
                    }
                }
            }
            return jsonObject;
            
        }
        return null;
    }
    
    private JSONArray getJSONAnnotations(SKOSAnnotationProperty property, String language) {
        JSONArray jsonAnnotations = new JSONArray();
        
        if (language != null) {
            List<String> annotations = this.getAnnotations(property, language);
            for (String annotation: annotations) {
                JSONObject jsonAnnotation = new JSONObject();
                jsonAnnotation.put("@language", language);
                jsonAnnotation.put("@value", annotation);
                jsonAnnotations.add(jsonAnnotation);
            }
        }
        else {
            List<Term> annotationTerms = this.getAnnotations(property);
            if (!annotationTerms.isEmpty()) {
                for (Term annotationTerm: annotationTerms) {
                    String annotation = annotationTerm.getString();
                    String lang = annotationTerm.getLanguage();
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
