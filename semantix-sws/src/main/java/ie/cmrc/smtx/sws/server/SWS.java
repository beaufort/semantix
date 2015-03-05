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

import ie.cmrc.smtx.skos.model.SKOS;
import ie.cmrc.smtx.skos.jena.SKOSFactory;
import ie.cmrc.smtx.skos.model.hierarchy.HierarchyMethod;
import ie.cmrc.smtx.skos.index.SKOSIndex;
import ie.cmrc.smtx.skos.index.SKOSIndexFactory;
import ie.cmrc.smtx.sws.config.FilterType;
import ie.cmrc.smtx.sws.exceptions.SWSException;
import ie.cmrc.smtx.sws.exceptions.SWSExceptionCode;
import ie.cmrc.smtx.sws.exceptions.SWSExceptionReport;
import ie.cmrc.smtx.sws.request.OutputFormat;
import ie.cmrc.smtx.sws.request.RequestParam;
import ie.cmrc.smtx.sws.request.RequestType;
import ie.cmrc.smtx.thesaurus.DefaultSWSThesaurus;
import ie.cmrc.smtx.thesaurus.SWSThesaurus;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.lucene.index.CorruptIndexException;

/**
 *
 * @author Yassine Lassoued <y.lassoued@ucc.ie>
 */
public class SWS extends HttpServlet {

    private static final String SERVICE_VERSION = "2.1";

    private Boolean initialised = false;
    
    private static SWSThesaurus thesaurus = null;

    private static SWSHelper swsHelper = null;

    private static final OutputFormat DEFAULT_OUTPUT_FORMAT = OutputFormat.APPLICATION_XML;
    
    private static final List<OutputFormat> SUPPORTED_OUTPUT_FORMATS = Arrays.asList(OutputFormat.APPLICATION_XML, OutputFormat.APPLICATION_JSON);
    
    private static final int DEFAULT_MIN_KW_LENGTH = 2;
    
    private static final FilterType DEFAULT_REQUIRED_FILTER = FilterType.NONE;
    
    @Override
    public void init(ServletConfig config) throws ServletException {
        this.initialised = false;
        // Store the ServletConfig object and log the initialization
        super.init(config);

        // Get configuration parameters
        String langStr = getServletConfig().getInitParameter("LANGS");
        List<String> langs = null;
        if (langStr != null && !(langStr=langStr.trim()).isEmpty()) {
            String[] values = langStr.split(",");
            langs = new ArrayList<>(values.length);
            for (String value1 : values) {
                String value = value1.trim();
                if (!value.isEmpty() && !langs.contains(value)) langs.add(value);
            }
        }
        else {
            Logger.getLogger(SWS.class.getName()).log(Level.SEVERE, "SWS languages (parametere LANGS) is null or empty!");
            throw new ServletException("Servlet "+SWS.class.getName()+": initialisation failed: SWS languages (parametere LANGS) is null or empty!");
        }
        
        String minKwLengthStr = getServletConfig().getInitParameter("MIN_KW_LENGTH");
        int minKwLength = DEFAULT_MIN_KW_LENGTH;
        if (minKwLengthStr!=null && !(minKwLengthStr=minKwLengthStr.trim()).isEmpty()) {
            try {
                minKwLength = Integer.parseInt(minKwLengthStr);
            }
            catch (NumberFormatException ex) {
                Logger.getLogger(SWS.class.getName()).log(Level.SEVERE, "SWS minimum search keyword length (parametere MIN_KW_LENGTH) is not a number!", ex);
                throw new ServletException("Servlet "+SWS.class.getName()+": initialisation failed: invalid MIN_KW_LENGTH parameter value!", ex);
            }
        }
        
        
        HierarchyMethod.RootType rootType = HierarchyMethod.DEFAULT_ROOT_TYPE;
        String rootTypeStr = getServletConfig().getInitParameter("HIER_METHOD_ROOT_TYPE");
        if (rootTypeStr!=null && !(rootTypeStr=rootTypeStr.trim()).isEmpty()) {
            rootType = HierarchyMethod.RootType.fromString(rootTypeStr);
            if (rootType == null) {
                Logger.getLogger(SWS.class.getName()).log(Level.SEVERE, "Ivalid HIER_METHOD_ROOT_TYPE parameter value ({0})!", rootTypeStr);
                throw new ServletException("Servlet "+SWS.class.getName()+": initialisation failed: invalid HIER_METHOD_ROOT_TYPE parameter value ("+rootTypeStr+")!");
            }
        }
        
        HierarchyMethod.RelationshipType relType = HierarchyMethod.DEFAULT_RELATIONSHIP_TYPE;
        String relTypeStr = getServletConfig().getInitParameter("HIER_METHOD_REL_TYPE");
        if (relTypeStr!=null && !(relTypeStr=relTypeStr.trim()).isEmpty()) {
            relType = HierarchyMethod.RelationshipType.fromString(relTypeStr);
            if (relType == null) {
                Logger.getLogger(SWS.class.getName()).log(Level.SEVERE, "Ivalid HIER_METHOD_REL_TYPE parameter value ({0})!", relTypeStr);
                throw new ServletException("Servlet "+SWS.class.getName()+": initialisation failed: invalid HIER_METHOD_REL_TYPE parameter value ("+relTypeStr+")!");
            }
        }
        
        HierarchyMethod hm = new HierarchyMethod(rootType, relType);
        
        FilterType requiredFilter = DEFAULT_REQUIRED_FILTER;
        String requiredFilterStr = getServletConfig().getInitParameter("REQUIRED_FILTER");
        if (requiredFilterStr!=null && !(requiredFilterStr=requiredFilterStr.trim()).isEmpty()) {
            requiredFilter = FilterType.fromString(requiredFilterStr);
            if (requiredFilter == null) {
                Logger.getLogger(SWS.class.getName()).log(Level.SEVERE, "Invalid REQUIRED_FILTER parameter value ({0})!", requiredFilterStr);
                throw new ServletException("Servlet "+SWS.class.getName()+": initialisation failed: invalid REQUIRED_FILTER parameter value ("+requiredFilterStr+")!");
            }
        }
        
        String dataPath = getServletConfig().getInitParameter("DATA");

        if (dataPath!=null && !(dataPath=dataPath.trim()).isEmpty()) {

            if (!dataPath.endsWith("/")) dataPath += "/";
            
            String tdbDir = dataPath+"tdb";
            String indexDir = dataPath+"index";
            
            SKOS skos = SKOSFactory.createSKOSThesaurus(tdbDir);

            try {
                SKOSIndex index = SKOSIndexFactory.createLuceneSKOSIndex(indexDir, langs);
                index.setMinKeywordLength(minKwLength);
                thesaurus = new DefaultSWSThesaurus(skos, index);
                swsHelper = new SWSHelper(thesaurus, requiredFilter, hm, minKwLength);
                this.initialised = true;
                Logger.getLogger(SWS.class.getName()).log(Level.INFO, "{0} Servlet started successfully.", SWS.class.getName());
            } catch (IllegalArgumentException ex) {
                Logger.getLogger(SWS.class.getName()).log(Level.SEVERE, "Illegal argument error encountered while trying to initialise Lucene index!", ex);
                throw new ServletException("Illegal argument error encountered while trying to initialise Lucene index!");
            } catch (CorruptIndexException ex) {
                Logger.getLogger(SWS.class.getName()).log(Level.SEVERE, "The provided SKOS index is corrupt!", ex);
                throw new ServletException("The provided SKOS index is corrupt!");
            } catch (IOException ex) {
                Logger.getLogger(SWS.class.getName()).log(Level.SEVERE, "IO error encountered while trying to initialise Lucene index!", ex);
                throw new ServletException("IO error encountered while trying to initialise Lucene index!");
            }
        }
        else {
            Logger.getLogger(SWS.class.getName()).log(Level.SEVERE, "Data directory parameter (DATA) is null or empty!");
            throw new ServletException("Servlet "+SWS.class.getName()+": initialisation failed: data directory parameter (DATA) is null or empty!");
        }
     }

    @Override
    public void destroy() {
        try {
            thesaurus.close();
            System.out.println("Thesaurus closed.");
        }
        catch (IOException e) {
            Logger.getLogger(SWS.class.getName()).log(Level.SEVERE, "Could not close thesaurus!", e);
        }
    }



    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        OutputFormat outputFormat = DEFAULT_OUTPUT_FORMAT;
        if (request.getParameterMap().containsKey(RequestParam.outputFormat.name())) {
            String stringFormat = request.getParameter(RequestParam.outputFormat.name());
            outputFormat = OutputFormat.fromString(stringFormat);
                
            if (outputFormat == null) {
                this.raiseException(new SWSException(SWSExceptionCode.INVALID_PARAMETER_VALUE, "Parameter '"+RequestParam.outputFormat+"': Invalid value: "+stringFormat, RequestParam.outputFormat.name()), response, DEFAULT_OUTPUT_FORMAT);
            }
        }
        
        if (SUPPORTED_OUTPUT_FORMATS.contains(outputFormat)) {
        
            String callback = null;
            if (request.getParameterMap().containsKey(RequestParam.callback.toString())) {
                callback = request.getParameter(RequestParam.callback.toString()).trim();
            }

            if (initialised){
                if (request.getParameterMap().containsKey(RequestParam.request.name())) {
                    String req = request.getParameter(RequestParam.request.name());
                    RequestType requestType = RequestType.fromString(req);

                    if (requestType != null) {

                        switch(requestType) {
                            case GetCapabilities: processGetCapabilities(request, response, outputFormat, callback); break;
                            case GetConceptSchemes: processGetConceptSchemes(request, response, outputFormat, callback); break;
                            case GetConceptScheme: processGetConceptScheme(request, response, outputFormat, callback); break;
                            case GetCollections: processGetCollections(request, response, outputFormat, callback); break;
                            case GetCollection: processGetCollection(request, response, outputFormat, callback); break;
                            case GetTopConcepts: processGetExplicitTopConcepts(request, response, outputFormat, callback); break;
                            case GetBroadestConcepts: processGetImplicitTopConcepts(request, response, outputFormat, callback); break;
                            case GetConcepts: processGetConcepts(request, response, outputFormat, callback); break;
                            case GetConcept: processGetConcept(request, response, outputFormat, callback); break;
                            case GetConceptHierarchy: processGetConceptHierarchyRequest(request, response, outputFormat, callback); break;
                            case GetRelatedConcepts: processGetRelatedConceptsRequest(request, response, outputFormat, callback); break;
                            case GetDirectNarrowerConcepts: processGetDirectNarrowerConceptsRequest(request, response, outputFormat, callback); break;
                            case GetDirectBroaderConcepts: processGetDirectBroaderConceptsRequest(request, response, outputFormat, callback); break;
                            case SearchConcepts: processSearchConceptRequest(request, response, outputFormat, callback); break;
                            case InterpretKeyword: processInterpretKeywordRequest(request, response, outputFormat, callback); break;
                        }
                    }
                    else {
                        this.raiseException(new SWSException(SWSExceptionCode.INVALID_REQUEST, "'"+req+"': no such request type.", RequestParam.request.name()), response, outputFormat, callback);
                    }
                } else {
                    this.raiseException(new SWSException(SWSExceptionCode.MISSING_PARAMETER, "Parameter '"+RequestParam.request+"' is missing.", RequestParam.request.name()), response, outputFormat);
                }
            }
            else {
                this.raiseException(new SWSException(SWSExceptionCode.NOT_SUPPORTED, "The requested out put format '"+outputFormat+"': is not supported! Supported output formats are: "+SUPPORTED_OUTPUT_FORMATS.toString(), RequestParam.outputFormat.name()), response, DEFAULT_OUTPUT_FORMAT);
            }
        }
        
         else {
            Logger.getLogger(SWS.class.getName()).log(Level.SEVERE, "Servlet SWS had not been initialised");
            this.raiseException(new SWSException(SWSExceptionCode.INTERNAL_ERROR, "The server has encountered an internal error. Please try again later."), response, outputFormat);
        }
    }

    protected void processGetCapabilities(HttpServletRequest request, HttpServletResponse response, OutputFormat outputFormat, String callback) throws ServletException, IOException {
        this.raiseException(new SWSException(SWSExceptionCode.NOT_IMPLEMENTED, "GetCapabilities operation is not yet implemented.", RequestParam.request.name()), response, outputFormat);
    }

    protected void processGetConceptSchemes(HttpServletRequest request, HttpServletResponse response, OutputFormat outputFormat, String callback) throws ServletException, IOException {
        try {
            
            long t0 = System.currentTimeMillis();
            String responseDoc  = swsHelper.getConceptSchemesResponse(request, outputFormat);
            long d = System.currentTimeMillis() - t0;
            this.returnDocumentAsResponse(responseDoc, response, outputFormat, callback, d);
        }
        catch (SWSException e) {
            this.raiseException(e, response, outputFormat, callback);
            }
    }

    protected void processGetConceptScheme(HttpServletRequest request, HttpServletResponse response, OutputFormat outputFormat, String callback) throws ServletException, IOException {
        try {
            //SWS_1_0_Helper swsHelper = new SWS_1_0_Helper(this.persOntConfig);
            long t0 = System.currentTimeMillis();
            String responseDoc  = swsHelper.getConceptSchemeResponse(request, outputFormat);
            long d = System.currentTimeMillis() - t0;
            this.returnDocumentAsResponse(responseDoc, response, outputFormat, callback, d);
        }
        catch (SWSException e) {
            this.raiseException(e, response, outputFormat, callback);
        }
    }

    protected void processGetCollections(HttpServletRequest request, HttpServletResponse response, OutputFormat outputFormat, String callback) throws ServletException, IOException {
        try {
            //SWS_1_0_Helper swsHelper = new SWS_1_0_Helper(this.persOntConfig);
            long t0 = System.currentTimeMillis();
            String responseDoc  = swsHelper.getCollectionsResponse(request, outputFormat);
            long d = System.currentTimeMillis() - t0;
            this.returnDocumentAsResponse(responseDoc, response, outputFormat, callback, d);
        }
        catch (SWSException e) {
            this.raiseException(e, response, outputFormat, callback);
        }
    }

    protected void processGetCollection(HttpServletRequest request, HttpServletResponse response, OutputFormat outputFormat, String callback) throws ServletException, IOException {
        try {
            //SWS_1_0_Helper swsHelper = new SWS_1_0_Helper(this.persOntConfig);
            long t0 = System.currentTimeMillis();
            String responseDoc  = swsHelper.getCollectionResponse(request, outputFormat);
            long d = System.currentTimeMillis() - t0;
            this.returnDocumentAsResponse(responseDoc, response, outputFormat, callback, d);
        }
        catch (SWSException e) {
            this.raiseException(e, response, outputFormat, callback);
        }
    }

    protected void processGetExplicitTopConcepts(HttpServletRequest request, HttpServletResponse response, OutputFormat outputFormat, String callback) throws ServletException, IOException {
        try {
            //SWS_1_0_Helper swsHelper = new SWS_1_0_Helper(this.persOntConfig);
            long t0 = System.currentTimeMillis();
            String responseDoc  = swsHelper.getTopConceptsResponse(request, outputFormat);
            long d = System.currentTimeMillis() - t0;
            this.returnDocumentAsResponse(responseDoc, response, outputFormat, callback, d);
        }
        catch (SWSException e) {
            this.raiseException(e, response, outputFormat, callback);
        }
    }

    protected void processGetImplicitTopConcepts(HttpServletRequest request, HttpServletResponse response, OutputFormat outputFormat, String callback) throws ServletException, IOException {
        try {
            //SWS_1_0_Helper swsHelper = new SWS_1_0_Helper(this.persOntConfig);
            long t0 = System.currentTimeMillis();
            String responseDoc  = swsHelper.getBroadestConceptsResponse(request, outputFormat);
            long d = System.currentTimeMillis() - t0;
            this.returnDocumentAsResponse(responseDoc, response, outputFormat, callback, d);
        }
        catch (SWSException e) {
            this.raiseException(e, response, outputFormat, callback);
        }
    }

    protected void processGetConcepts(HttpServletRequest request, HttpServletResponse response, OutputFormat outputFormat, String callback) throws ServletException, IOException {
        try {
            //SWS_1_0_Helper swsHelper = new SWS_1_0_Helper(this.persOntConfig);
            long t0 = System.currentTimeMillis();
            String responseDoc  = swsHelper.getConceptsResponse(request, outputFormat);
            long d = System.currentTimeMillis() - t0;
            this.returnDocumentAsResponse(responseDoc, response, outputFormat, callback, d);
        }
        catch (SWSException e) {
            this.raiseException(e, response, outputFormat, callback);
            }
    }

    protected void processGetConcept(HttpServletRequest request, HttpServletResponse response, OutputFormat outputFormat, String callback) throws ServletException, IOException {
        try {
            //SWS_1_0_Helper swsHelper = new SWS_1_0_Helper(this.persOntConfig);
            long t0 = System.currentTimeMillis();
            String responseDoc  = swsHelper.getConceptResponse(request, outputFormat);
            long d = System.currentTimeMillis() - t0;
            this.returnDocumentAsResponse(responseDoc, response, outputFormat, callback, d);
        }
        catch (SWSException e) {
            this.raiseException(e, response, outputFormat, callback);
        }
    }

    protected void processGetConceptHierarchyRequest(HttpServletRequest request, HttpServletResponse response, OutputFormat outputFormat, String callback) throws ServletException, IOException {
        try {
            //SWS_1_0_Helper swsHelper = new SWS_1_0_Helper(this.persOntConfig);
            long t0 = System.currentTimeMillis();
            String responseDoc  = swsHelper.getConceptHierarchyResponse(request, outputFormat);
            long d = System.currentTimeMillis() - t0;
            this.returnDocumentAsResponse(responseDoc, response, outputFormat, callback, d);
        }
        catch (SWSException e) {
            this.raiseException(e, response, outputFormat, callback);
        }
    }
    
    protected void processGetRelatedConceptsRequest(HttpServletRequest request, HttpServletResponse response, OutputFormat outputFormat, String callback) throws ServletException, IOException {
        try {
            //SWS_1_0_Helper swsHelper = new SWS_1_0_Helper(this.persOntConfig);
            long t0 = System.currentTimeMillis();
            String responseDoc  = swsHelper.getRelatedConceptsResponse(request, outputFormat);
            long d = System.currentTimeMillis() - t0;
            this.returnDocumentAsResponse(responseDoc, response, outputFormat, callback, d);
        }
        catch (SWSException e) {
            this.raiseException(e, response, outputFormat, callback);
        }
    }

    protected void processGetDirectNarrowerConceptsRequest(HttpServletRequest request, HttpServletResponse response, OutputFormat outputFormat, String callback) throws ServletException, IOException {
        try {
            //SWS_1_0_Helper swsHelper = new SWS_1_0_Helper(this.persOntConfig);
            long t0 = System.currentTimeMillis();
            String responseDoc  = swsHelper.getDirectNarrowerConceptsResponse(request, outputFormat);
            long d = System.currentTimeMillis() - t0;
            this.returnDocumentAsResponse(responseDoc, response, outputFormat, callback, d);
        }
        catch (SWSException e) {
            this.raiseException(e, response, outputFormat, callback);
        }
    }

    protected void processGetDirectBroaderConceptsRequest(HttpServletRequest request, HttpServletResponse response, OutputFormat outputFormat, String callback) throws ServletException, IOException {
        try {
            //SWS_1_0_Helper swsHelper = new SWS_1_0_Helper(this.persOntConfig);
            long t0 = System.currentTimeMillis();
            String responseDoc  = swsHelper.getDirectBroaderConceptsResponse(request, outputFormat);
            long d = System.currentTimeMillis() - t0;
            this.returnDocumentAsResponse(responseDoc, response, outputFormat, callback, d);
        }
        catch (SWSException e) {
            this.raiseException(e, response, outputFormat, callback);
        }
    }

    protected void processSearchConceptRequest(HttpServletRequest request, HttpServletResponse response, OutputFormat outputFormat, String callback) throws ServletException, IOException {
        try {
            //SWS_1_0_Helper swsHelper = new SWS_1_0_Helper(this.persOntConfig);
            long t0 = System.currentTimeMillis();
            String responseDoc  = swsHelper.getSearchConceptResponse(request, outputFormat);
            long d = System.currentTimeMillis() - t0;
            this.returnDocumentAsResponse(responseDoc, response, outputFormat, callback, d);
        }
        catch (SWSException e) {
            this.raiseException(e, response, outputFormat, callback);
        }
    }

    protected void processInterpretKeywordRequest(HttpServletRequest request, HttpServletResponse response, OutputFormat outputFormat, String callback) throws ServletException, IOException {
        try {
            //SWS_1_0_Helper swsHelper = new SWS_1_0_Helper(this.persOntConfig);
            long t0 = System.currentTimeMillis();
            String responseDoc  = swsHelper.getInterpretKeywordResponse(request, outputFormat);
            long d = System.currentTimeMillis() - t0;
            this.returnDocumentAsResponse(responseDoc, response, outputFormat, callback, d);
        }
        catch (SWSException e) {
            this.raiseException(e, response, outputFormat, callback);
        }
    }
    
    
    private void raiseException (SWSException exception, HttpServletResponse response, OutputFormat outputFormat) throws ServletException, IOException {
        this.raiseException(exception, response, outputFormat, null);
    }

    private void raiseException (SWSException exception, HttpServletResponse response, OutputFormat outputFormat, String callback) throws ServletException, IOException {
        if (outputFormat.equals(OutputFormat.APPLICATION_JSON)) {
            response.setContentType("application/json;charset=UTF-8");
        }
        else {
            response.setContentType("text/xml;charset=UTF-8");
        }
        
        
        PrintWriter out = response.getWriter();
        try {
            SWSExceptionReport exceptionReport = new SWSExceptionReport();
            exceptionReport.addException(exception);
            
            if (outputFormat==OutputFormat.APPLICATION_JSON) {
                
                String json = exceptionReport.getJSON().toJSONString();
                
                if (callback!=null && !callback.isEmpty()) out.println(callback+" ("+json+");");
                else out.println(json);
            }
            else {
                out.println(exceptionReport.getXML().asXML());
            }
            
        }
        finally {
            out.close();
        }
    }


    private void returnDocumentAsResponse(String doc, HttpServletResponse response, OutputFormat outputFormat, String callback, long duration) throws ServletException, IOException {
        if (outputFormat==OutputFormat.APPLICATION_JSON) {
            response.setContentType("application/json;charset=UTF-8");
        }
        else {
            response.setContentType("text/xml;charset=UTF-8");
        }
        
        PrintWriter out = response.getWriter();
        try {
            if (outputFormat==OutputFormat.APPLICATION_JSON) {
                if (callback!=null && !callback.isEmpty()) out.println(callback+" ("+doc+");");
                else out.println(doc);
            }
            else {
                out.println(doc);
                out.println("\n"
                        + "<!-- Response returned by SWS "+SERVICE_VERSION+" -->");
                if (duration>=0) out.println("<!--Request prosessed in "+duration+" milliseconds-->");
            }
        }
        finally {
            out.close();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
