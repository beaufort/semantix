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

package ie.cmrc.smtx.sws.server;


import ie.cmrc.smtx.base.SemanticEntity;
import ie.cmrc.smtx.base.serialisation.ElementSetName;
import ie.cmrc.smtx.base.serialisation.json.JSONSerialiser;
import ie.cmrc.smtx.base.serialisation.rdfxml.RDFXMLSerialiser;
import ie.cmrc.smtx.skos.model.SKOSCollection;
import ie.cmrc.smtx.skos.model.SKOSConcept;
import ie.cmrc.smtx.skos.model.SKOSConceptScheme;
import ie.cmrc.smtx.skos.model.SKOSSemanticProperty;
import ie.cmrc.smtx.skos.model.hierarchy.HierarchyMethod;
import ie.cmrc.smtx.skos.model.hierarchy.SKOSConceptNode;
import ie.cmrc.smtx.skos.model.util.CloseableIterator;
import ie.cmrc.smtx.skos.index.IndexField;
import ie.cmrc.smtx.sws.config.FilterType;
import ie.cmrc.smtx.sws.exceptions.SWSException;
import ie.cmrc.smtx.sws.exceptions.SWSExceptionCode;
import ie.cmrc.smtx.sws.request.OutputFormat;
import ie.cmrc.smtx.sws.request.RequestParam;
import ie.cmrc.smtx.thesaurus.SWSThesaurus;
import ie.cmrc.util.Term;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.dom4j.Document;
import org.json.simple.JSONObject;


/**
 * 
 * @author Yassine Lassoued <y.lassoued@ucc.ie>
 */
public class SWSHelper {
    
    private final SWSThesaurus thesaurus;
    
    private final FilterType requiredFilter;
    private final HierarchyMethod defaultHierarchyMethod;
    private final int minKeywordLength;

    public SWSHelper(SWSThesaurus thesaurus, FilterType requiredFilter, HierarchyMethod defaultHierarchyMethod, int minKeywordLength) {
        this.thesaurus = thesaurus;
        this.requiredFilter = requiredFilter;
        this.defaultHierarchyMethod = defaultHierarchyMethod;
        this.minKeywordLength = minKeywordLength;
    }
    

    public String getConceptSchemesResponse(HttpServletRequest request, OutputFormat outputFormat) throws SWSException {

        ElementSetName elementSetName = this.getElementSet(request);
        String responseLanguage = this.getResponseLanguage(request);

        CloseableIterator<SKOSConceptScheme> result = thesaurus.listConceptSchemes();
        
        
        return this.resultToString(result, outputFormat, elementSetName, responseLanguage);
    }



    public String getConceptSchemeResponse(HttpServletRequest request, OutputFormat outputFormat) throws SWSException {
        ElementSetName elementSetName = this.getElementSet(request);
        String responseLanguage = this.getResponseLanguage(request);

        String csUri = this.getParameterValue(request, RequestParam.conceptScheme);
        if (csUri != null) {
            if (!csUri.isEmpty()) {
                
                SKOSConceptScheme result = thesaurus.getConceptScheme(csUri);
                
                return this.resultToString(result, outputFormat, elementSetName, responseLanguage);
            }
            else {
                throw (new SWSException(SWSExceptionCode.INVALID_PARAMETER_VALUE, RequestParam.conceptScheme+" value is empty", RequestParam.conceptScheme.name()));
            }
        }
        else {
            throw (new SWSException(SWSExceptionCode.MISSING_PARAMETER, RequestParam.conceptScheme+" parameter is missing", RequestParam.conceptScheme.name()));
        }
    }


    public String getCollectionsResponse(HttpServletRequest request, OutputFormat outputFormat) throws SWSException {

        ElementSetName elementSetName = this.getElementSet(request);
        String responseLanguage = this.getResponseLanguage(request);

        String csUri = this.getParameterValue(request, RequestParam.conceptScheme);
        String collectionUri = this.getParameterValue(request, RequestParam.collection);

        CloseableIterator<SKOSCollection> result = thesaurus.listCollections(csUri, collectionUri);
        
        
        return this.resultToString(result, outputFormat, elementSetName, responseLanguage);
    }



    public String getCollectionResponse(HttpServletRequest request, OutputFormat outputFormat) throws SWSException {
        ElementSetName elementSetName = this.getElementSet(request);
        String responseLanguage = this.getResponseLanguage(request);

        String collectionUri = this.getParameterValue(request, RequestParam.collection);
        if (collectionUri != null) {

            if (!collectionUri.isEmpty()) {

                SKOSCollection result = thesaurus.getCollection(collectionUri);
                return this.resultToString(result, outputFormat, elementSetName, responseLanguage);
            }
            else {
                throw (new SWSException(SWSExceptionCode.INVALID_PARAMETER_VALUE, RequestParam.collection+" value is empty", RequestParam.collection.name()));
            }
        }
        else {
            throw (new SWSException(SWSExceptionCode.MISSING_PARAMETER, RequestParam.collection+" parameter is missing", RequestParam.collection.name()));
        }
        
    }



    public String getTopConceptsResponse(HttpServletRequest request, OutputFormat outputFormat) throws SWSException {

        ElementSetName elementSetName = this.getElementSet(request);
        String responseLanguage = this.getResponseLanguage(request);
        
        String csUri = this.getParameterValue(request, RequestParam.conceptScheme);
        String collectionUri = this.getParameterValue(request, RequestParam.collection);

        this.checkFilterCondition(csUri, collectionUri);
        
        CloseableIterator<SKOSConcept> result = thesaurus.listTopConcepts(csUri, collectionUri);
        
        
        return this.resultToString(result, outputFormat, elementSetName, responseLanguage);

    }



    public String getBroadestConceptsResponse(HttpServletRequest request, OutputFormat outputFormat) throws SWSException {

        ElementSetName elementSetName = this.getElementSet(request);
        String responseLanguage = this.getResponseLanguage(request);
        
        String csUri = this.getParameterValue(request, RequestParam.conceptScheme);
        String collectionUri = this.getParameterValue(request, RequestParam.collection);

        this.checkFilterCondition(csUri, collectionUri);
        
        List<SKOSConcept> result = thesaurus.getBroadestConcepts(csUri, collectionUri);
        
        return this.resultToString(result, outputFormat, elementSetName, responseLanguage);

    }



    public String getConceptsResponse(HttpServletRequest request, OutputFormat outputFormat) throws SWSException {

        ElementSetName elementSetName = this.getElementSet(request);
        String responseLanguage = this.getResponseLanguage(request);
        
        String csUri = this.getParameterValue(request, RequestParam.conceptScheme);
        String collectionUri = this.getParameterValue(request, RequestParam.collection);

        this.checkFilterCondition(csUri, collectionUri);
        
        CloseableIterator<SKOSConcept> result = thesaurus.listConcepts(csUri, collectionUri);
        
        return this.resultToString(result, outputFormat, elementSetName, responseLanguage);

    }

    public String getConceptResponse(HttpServletRequest request, OutputFormat outputFormat) throws SWSException {

        ElementSetName elementSetName = this.getElementSet(request);
        String responseLanguage = this.getResponseLanguage(request);

        String conceptUri = this.getParameterValue(request, RequestParam.concept);
        if (conceptUri != null) {

            if (!conceptUri.isEmpty()) {

                SKOSConcept result = thesaurus.getConcept(conceptUri);
                return this.resultToString(result, outputFormat, elementSetName, responseLanguage);
            }
            else {
                throw (new SWSException(SWSExceptionCode.INVALID_PARAMETER_VALUE, RequestParam.concept+" value is empty", RequestParam.concept.name()));
            }
        }
        else {
            throw (new SWSException(SWSExceptionCode.MISSING_PARAMETER, RequestParam.concept+" parameter is missing", RequestParam.concept.name()));
        }
    }

    public String getConceptHierarchyResponse(HttpServletRequest request, OutputFormat outputFormat) throws SWSException {

        ElementSetName elementSetName = this.getElementSet(request);
        String responseLanguage = this.getResponseLanguage(request);

        String conceptUri = this.getParameterValue(request, RequestParam.concept);
        
        String csUri = this.getParameterValue(request, RequestParam.conceptScheme);
        String collectionUri = this.getParameterValue(request, RequestParam.collection);

        this.checkFilterCondition(csUri, collectionUri);
        
        
        HierarchyMethod.RootType rootType = this.defaultHierarchyMethod.getRootType();
        if (conceptUri==null || conceptUri.isEmpty()) {
            String rootTypeStr = this.getParameterValue(request, RequestParam.rootType);
            if (rootTypeStr!=null && !rootTypeStr.isEmpty()) {
                try {
                    rootType = HierarchyMethod.RootType.fromString(rootTypeStr);
                }
                catch (IllegalArgumentException ex) {
                    throw (new SWSException(SWSExceptionCode.INVALID_PARAMETER_VALUE, "\""+rootTypeStr+"\" is not a valid value for parameter \""+RequestParam.rootType+"\". Possible values are: "+Arrays.toString(HierarchyMethod.RootType.values()), RequestParam.rootType.name()));
                }
            }
        }
        
        HierarchyMethod.RelationshipType relType = this.defaultHierarchyMethod.getRelationshipType();
        String relTypeStr = this.getParameterValue(request, RequestParam.relType);
        if (relTypeStr!=null && !relTypeStr.isEmpty()) {
            try {
                relType = HierarchyMethod.RelationshipType.fromString(relTypeStr);
            }
            catch (IllegalArgumentException ex) {
                throw (new SWSException(SWSExceptionCode.INVALID_PARAMETER_VALUE, "\""+relTypeStr+"\" is not a valid value for parameter \""+RequestParam.relType+"\". Possible values are: "+Arrays.toString(HierarchyMethod.RelationshipType.values()), RequestParam.relType.name()));
            }
        }
        HierarchyMethod hirerachyMethod = new HierarchyMethod(rootType, relType, responseLanguage);
        
        if (conceptUri==null || conceptUri.isEmpty()) {
            List<SKOSConceptNode> result = this.thesaurus.getConceptHierarchy(csUri, collectionUri, hirerachyMethod);
            return this.nodeResultToString(result, outputFormat, elementSetName, responseLanguage);
        }
        else {
            SKOSConceptNode result = this.thesaurus.getConceptTree(conceptUri, csUri, collectionUri, hirerachyMethod);
            return this.nodeResultToString(result, outputFormat, elementSetName, responseLanguage);
        }
    }



    public String getRelatedConceptsResponse(HttpServletRequest request, OutputFormat outputFormat) throws SWSException {

        ElementSetName elementSetName = this.getElementSet(request);
        String responseLanguage = this.getResponseLanguage(request);

        String conceptUri = this.getParameterValue(request, RequestParam.concept);
        
        if (conceptUri != null) {

            if (!conceptUri.isEmpty()) {

                String relationStr = this.getParameterValue(request, RequestParam.relationship);
                if (relationStr!=null) {
                    if (!relationStr.isEmpty()) {
                        SKOSSemanticProperty property = SKOSSemanticProperty.fromString(relationStr);
                        if (property != null) {
                            List<String> conceptSchemeUris = this.getParameterValues(request, RequestParam.conceptScheme);
                            List<String> collectionUris = this.getParameterValues(request, RequestParam.collection);

                            CloseableIterator result = this.thesaurus.listRelatedConcepts(conceptUri, property, conceptSchemeUris, collectionUris);
                            return this.resultToString(result, outputFormat, elementSetName, responseLanguage);
                        }
                        else {
                            throw (new SWSException(SWSExceptionCode.INVALID_PARAMETER_VALUE, "\""+relationStr+"\" is not a valid "+RequestParam.relationship+" value!", RequestParam.relationship.name()));
                        }
                    }
                    else {
                        throw (new SWSException(SWSExceptionCode.INVALID_PARAMETER_VALUE, RequestParam.relationship+" value is empty!", RequestParam.relationship.name()));
                    }
                }
                else {
                    throw (new SWSException(SWSExceptionCode.MISSING_PARAMETER, RequestParam.relationship+" parameter is missing!", RequestParam.relationship.name()));
                }
            }
            else {
                throw (new SWSException(SWSExceptionCode.INVALID_PARAMETER_VALUE, RequestParam.concept+" value is empty!", RequestParam.concept.name()));
            }
        }
        else {
            throw (new SWSException(SWSExceptionCode.MISSING_PARAMETER, RequestParam.concept+" parameter is missing!", RequestParam.concept.name()));
        }
    }


    public String getDirectNarrowerConceptsResponse(HttpServletRequest request, OutputFormat outputFormat) throws SWSException {

        ElementSetName elementSetName = this.getElementSet(request);
        String responseLanguage = this.getResponseLanguage(request);
        
        String csUri = this.getParameterValue(request, RequestParam.conceptScheme);
        String collectionUri = this.getParameterValue(request, RequestParam.collection);
        
        String conceptUri = this.getParameterValue(request, RequestParam.concept);
        
        if (conceptUri != null) {

            if (!conceptUri.isEmpty()) {
                this.checkFilterCondition(csUri, collectionUri);

                List<SKOSConcept> result = thesaurus.getDirectNarrowerConcepts(conceptUri, csUri, collectionUri);

                return this.resultToString(result, outputFormat, elementSetName, responseLanguage);
            }
            else {
                throw (new SWSException(SWSExceptionCode.INVALID_PARAMETER_VALUE, RequestParam.concept+" value is empty!", RequestParam.concept.name()));
            }
        }
        else {
            throw (new SWSException(SWSExceptionCode.MISSING_PARAMETER, RequestParam.concept+" parameter is missing!", RequestParam.concept.name()));
        }
    }


    public String getDirectBroaderConceptsResponse(HttpServletRequest request, OutputFormat outputFormat) throws SWSException {

        ElementSetName elementSetName = this.getElementSet(request);
        String responseLanguage = this.getResponseLanguage(request);
        
        String csUri = this.getParameterValue(request, RequestParam.conceptScheme);
        String collectionUri = this.getParameterValue(request, RequestParam.collection);
        
        String conceptUri = this.getParameterValue(request, RequestParam.concept);
        
        if (conceptUri != null) {

            if (!conceptUri.isEmpty()) {
                this.checkFilterCondition(csUri, collectionUri);

                List<SKOSConcept> result = thesaurus.getDirectBroaderConcepts(conceptUri, csUri, collectionUri);

                return this.resultToString(result, outputFormat, elementSetName, responseLanguage);
            }
            else {
                throw (new SWSException(SWSExceptionCode.INVALID_PARAMETER_VALUE, RequestParam.concept+" value is empty!", RequestParam.concept.name()));
            }
        }
        else {
            throw (new SWSException(SWSExceptionCode.MISSING_PARAMETER, RequestParam.concept+" parameter is missing!", RequestParam.concept.name()));
        }
    }



    public String getSearchConceptResponse(HttpServletRequest request, OutputFormat outputFormat) throws SWSException {

        ElementSetName elementSetName = this.getElementSet(request);
        String responseLanguage = this.getResponseLanguage(request);
        
        String keywordLanguage = this.getParameterValue(request, RequestParam.keywordLanguage);
        int offset = -1;
        
        String offsetString = this.getParameterValue(request, RequestParam.offset);
        if (offsetString!=null && !offsetString.isEmpty()) {
            try {
                offset = Integer.parseInt(offsetString);
            }
            catch (NumberFormatException nfe) {
                throw (new SWSException(SWSExceptionCode.INVALID_PARAMETER_VALUE, "Parameter \""+ RequestParam.offset+"\" must be of type integer!", RequestParam.offset.name()));
            }
        }
        int limit = -1;
        String limitString = this.getParameterValue(request, RequestParam.limit);
        if (limitString!=null && !limitString.isEmpty()) {
            try {
                limit = Integer.parseInt(limitString);
            }
            catch (NumberFormatException nfe) {
                throw (new SWSException(SWSExceptionCode.INVALID_PARAMETER_VALUE, "Parameter \""+ RequestParam.limit+"\" must be of type integer!", RequestParam.limit.name()));
            }
        }
        
        String kw = this.getParameterValue(request, RequestParam.keyword);
        if (kw != null) {
            kw = kw.trim();
        }
        if (kw!=null) {

            if (kw.length()>=this.minKeywordLength) {
                
                IndexField.Searchable field = null;
                String fieldStr = this.getParameterValue(request, RequestParam.field);
                if (fieldStr!=null && !fieldStr.isEmpty()) {
                    field = IndexField.Searchable.fromString(fieldStr);
                    if (field == null) {
                        throw (new SWSException(SWSExceptionCode.INVALID_PARAMETER_VALUE, "\""+fieldStr+"\" is not a valid "+RequestParam.field+" value!", RequestParam.field.name()));
                    }
                }
                
                List<String> conceptSchemeUris = this.getParameterValues(request, RequestParam.conceptScheme);
                List<String> collectionUris = this.getParameterValues(request, RequestParam.collection);
                if (elementSetName.compareTo(ElementSetName.BRIEF)<=0) {
                    List<SemanticEntity> result = this.thesaurus.search(new Term(kw, keywordLanguage), field, conceptSchemeUris, collectionUris, offset, limit);
                    return this.resultToString(result, outputFormat, elementSetName, responseLanguage);
                }
                else {
                    List<SKOSConcept> result = this.thesaurus.searchConcepts(new Term(kw, keywordLanguage), field, conceptSchemeUris, collectionUris, offset, limit);
                    return this.resultToString(result, outputFormat, elementSetName, responseLanguage);
                }
            }
            else {
                throw (new SWSException(SWSExceptionCode.INVALID_PARAMETER_VALUE, "The provided keyword (\""+kw+"\") is too short. The minimum length required for a search keyword is "+this.minKeywordLength+".", RequestParam.keyword.name()));
            }
        }
        else {
            throw (new SWSException(SWSExceptionCode.MISSING_PARAMETER, "Parameter "+RequestParam.keyword+" is missing!", RequestParam.keyword.name()));
        }
    }


    public String getInterpretKeywordResponse(HttpServletRequest request, OutputFormat outputFormat) throws SWSException {

        ElementSetName elementSetName = this.getElementSet(request);
        String responseLanguage = this.getResponseLanguage(request);
        
        String keywordLanguage = this.getParameterValue(request, RequestParam.keywordLanguage);
        
        
        String kw = this.getParameterValue(request, RequestParam.keyword);
        if (kw != null) {
            kw = kw.trim();
        }
        if (kw!=null) {

            if (kw.length()>=this.minKeywordLength) {
                
                List<String> conceptSchemeUris = this.getParameterValues(request, RequestParam.conceptScheme);
                List<String> collectionUris = this.getParameterValues(request, RequestParam.collection);

                List<SKOSConcept> result =this.thesaurus.interpretKeyword(new Term(kw, keywordLanguage), conceptSchemeUris, collectionUris);

                return this.resultToString(result, outputFormat, elementSetName, responseLanguage);
            }
            else {
                throw (new SWSException(SWSExceptionCode.INVALID_PARAMETER_VALUE, "The provided keyword (\""+kw+"\" is too short. The minimum length required for a search keyword is "+this.minKeywordLength+".", RequestParam.keyword.name()));
            }
        }
        else {
            throw (new SWSException(SWSExceptionCode.MISSING_PARAMETER, "Parameter "+RequestParam.keyword+" is missing!", RequestParam.keyword.name()));
        }
    }

    private void checkFilterCondition(String csUri, String collectionUri) throws SWSException {
        if (((csUri==null || csUri.isEmpty()) && (this.requiredFilter==FilterType.CS || this.requiredFilter==FilterType.CS_AND_COLLECTION)))
            throw (new SWSException(SWSExceptionCode.INVALID_PARAMETER_VALUE, RequestParam.conceptScheme+" request parameter is mandatory!", RequestParam.conceptScheme.name()));
        if (((collectionUri==null || collectionUri.isEmpty()) && (this.requiredFilter==FilterType.COLLECTION || this.requiredFilter==FilterType.CS_AND_COLLECTION)))
            throw (new SWSException(SWSExceptionCode.INVALID_PARAMETER_VALUE, RequestParam.collection+" request parameter is mandatory!", RequestParam.collection.name()));
        if ((csUri==null || csUri.isEmpty()) && (collectionUri==null || collectionUri.isEmpty()) && this.requiredFilter==FilterType.CS_OR_COLLECTION)
            throw (new SWSException(SWSExceptionCode.MISSING_PARAMETER, "At least a target concept scheme or collection URI must must be provided!", null));
    }
    
    private String resultToString(SemanticEntity result, OutputFormat outputFormat, ElementSetName elementSetName, String responseLanguage) {
        if (outputFormat==OutputFormat.APPLICATION_JSON) {
            JSONObject doc = JSONSerialiser.makeRDFJSONDocument(result, elementSetName, responseLanguage);
            return doc.toJSONString();
        }
        else {
            // This must be application/xml
            Document doc = RDFXMLSerialiser.makeRDFXMLDocument(result, elementSetName, responseLanguage);
            return doc.asXML();
        }
    }

    private String resultToString(CloseableIterator<? extends SemanticEntity> result, OutputFormat outputFormat, ElementSetName elementSetName, String responseLanguage) {
        if (outputFormat==OutputFormat.APPLICATION_JSON) {
            JSONObject doc = JSONSerialiser.makeRDFJSONDocument(result, elementSetName, responseLanguage);
            return doc.toJSONString();
        }
        else {
            // This must be application/xml
            Document doc = RDFXMLSerialiser.makeRDFXMLDocument(result, elementSetName, responseLanguage);
            return doc.asXML();
        }
    }

    private String resultToString(Collection<? extends SemanticEntity> result, OutputFormat outputFormat, ElementSetName elementSetName, String responseLanguage) {
        if (outputFormat==OutputFormat.APPLICATION_JSON) {
            JSONObject doc = JSONSerialiser.makeRDFJSONDocument(result, elementSetName, responseLanguage);
            return doc.toJSONString();
        }
        else {
            // This must be application/xml
            Document doc = RDFXMLSerialiser.makeRDFXMLDocument(result, elementSetName, responseLanguage);
            return doc.asXML();
        }
    }
    
    private String nodeResultToString(SKOSConceptNode result, OutputFormat outputFormat, ElementSetName elementSetName, String responseLanguage) {
        if (outputFormat==OutputFormat.APPLICATION_JSON) {
            JSONObject doc = JSONSerialiser.makeRDFJSONDocument(result, elementSetName, responseLanguage);
            return doc.toJSONString();
        }
        else {
            // This must be application/xml
            Document doc = RDFXMLSerialiser.makeRDFXMLDocument(result, elementSetName, responseLanguage);
            return doc.asXML();
        }
    }

    private String nodeResultToString(Collection<? extends SKOSConceptNode> result, OutputFormat outputFormat, ElementSetName elementSetName, String responseLanguage) {
        if (outputFormat==OutputFormat.APPLICATION_JSON) {
            JSONObject doc = JSONSerialiser.makeRDFJSONDocument(result, elementSetName, responseLanguage);
            return doc.toJSONString();
        }
        else {
            // This must be application/xml
            Document doc = RDFXMLSerialiser.makeRDFXMLDocument(result, elementSetName, responseLanguage);
            return doc.asXML();
        }
    }
    
    private ElementSetName getElementSet(HttpServletRequest request) throws SWSException {
        ElementSetName elementSetName = ElementSetName.ABSTRACT;
        String stringESN = "";
        if (request.getParameterMap().containsKey(RequestParam.elementSet.name())) {
            stringESN = request.getParameter(RequestParam.elementSet.name());
            elementSetName = ElementSetName.fromString(stringESN);
        }
        if (elementSetName!=null) return elementSetName;
        else {
            throw(new SWSException(SWSExceptionCode.INVALID_PARAMETER_VALUE,"\""+stringESN+"\" is not a valid element set name. Possible values are "+ElementSetName.possibleValues().toString(), RequestParam.elementSet.name()));
        }
    }
    
    private String getResponseLanguage(HttpServletRequest request) {
        String responseLanguage = null;
        if (request.getParameterMap().containsKey(RequestParam.responseLanguage.name())) {
            responseLanguage = request.getParameter(RequestParam.responseLanguage.name());
        }
        return responseLanguage;
    }
    
    private String getParameterValue(HttpServletRequest request, RequestParam param) {
        if (request.getParameterMap().containsKey(param.name())) {
            String value = request.getParameter(param.name()).trim();
            return value;
        }
        else return null;
    }
    
    private List<String> getParameterValues(HttpServletRequest request, RequestParam param) {
        List<String> values = new ArrayList<>();
        if (request.getParameterMap().containsKey(param.name())) {
            String[] valueArray = request.getParameterValues(param.name());
            if (valueArray != null) {
                for (String value : valueArray) {
                    if (value!=null && !(value=value.trim()).isEmpty()) {
                        values.add(value);
                    }
                }
            }
        }
        return values;
    }
}
