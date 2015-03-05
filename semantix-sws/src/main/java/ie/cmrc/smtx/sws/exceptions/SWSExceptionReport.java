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

package ie.cmrc.smtx.sws.exceptions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Namespace;
import org.dom4j.QName;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author yassine
 */
public class SWSExceptionReport {
    private final String swsURI = "http://cmrc.ucc.ie/sws/2.1";
    private final String xmlURI = "http://www.w3.org/XML/1998/namespace";

    private String language;
    private final String version;
    private final List<SWSException> exceptions;


    public SWSExceptionReport() {
        this.language = "en";
        this.version = SWSExceptionReportVersion.VERSION_1_0;
        this.exceptions = new ArrayList<SWSException>();
    }

    public SWSExceptionReport(String language) {
        this.language = language;
        this.version = SWSExceptionReportVersion.VERSION_1_0;
        this.exceptions = new ArrayList<SWSException>();
    }

    public String getLanguage() {
        return language;
    }

    public String getVersion() {
        return version;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void addException(SWSException owsException) {
        this.exceptions.add(owsException);
    }

    public void addException(int i, SWSException owsException) {
        this.exceptions.add(i, owsException);
    }

    public void removeException(int i) {
        this.exceptions.remove(i);
    }

    public void removeException(SWSException owsException) {
        this.exceptions.remove(owsException);
    }

    public void clear() {
        this.exceptions.clear();
    }

    public void addAll(Collection<SWSException> owsExceptions) {
        this.exceptions.addAll(owsExceptions);
    }

    public void addAll(int i, Collection<SWSException> owsExceptions) {
        this.exceptions.addAll(i, owsExceptions);
    }

    public Exception get(int i) {
        return this.exceptions.get(i);
    }

    public Boolean isEmpty() {
        return exceptions.isEmpty();
    }

    public int size() {
        return exceptions.size();
    }


    public Document getXML() {

        if (!this.isEmpty()) {

            Namespace sws = new Namespace("sws", swsURI);
            Namespace xml = new Namespace("xml", xmlURI);
            //Namespace xsi = new Namespace("xsi", xsiURI);

            QName exReportQN = new QName("ExceptionReport", sws);

            Element exceptionReport = DocumentHelper.createElement(exReportQN);

            exceptionReport.add(xml);
            exceptionReport.add(sws);
            //exceptionReport.add(xsi);


            //QName schemaLocQN = new QName ("schemaLocation", xsi);
            //exceptionReport.addAttribute(schemaLocQN, this.xsiSchameLocation);

            //Language
            if (this.language!=null) {
                if (!this.language.trim().equals("")) {
                    QName langQN = new QName("lang", xml);
                    exceptionReport.addAttribute(langQN, this.language.trim());
                }
            }

            //Version
            exceptionReport.addAttribute("version", this.version);

            QName exceptionQN = new QName("Exception", sws);
            for (SWSException e: this.exceptions) {
                if (e!=null) {
                    Element exception = exceptionReport.addElement(exceptionQN).addAttribute("exceptionCode", e.getCode());
                    if (e.getLocator()!=null) {
                        if (!e.getLocator().trim().equals("")) {
                            exception.addAttribute("locator", e.getLocator().trim());
                        }
                    }

                    if (e.getMessage()!=null) {
                        QName messageQN = new QName("ExceptionText", sws);
                        exception.addElement(messageQN).addText(e.getMessage());
                    }
                }
            }


            Document exceptionReportDoc = DocumentHelper.createDocument(exceptionReport);
            return exceptionReportDoc;

        }
        else {
            //Raise Empty exceptionReport exception
            return null;
        }
    }

    public JSONObject getJSON() {

        if (!this.isEmpty()) {

            JSONObject exceptionReport = new JSONObject();
            exceptionReport.put("version", this.version);
            exceptionReport.put("specifiedBy", this.swsURI);
            
            if (this.language!=null) {
                if (!this.language.trim().equals("")) {
                    exceptionReport.put("language", "en");
                }
            }
            
            JSONArray exceptionArray = new JSONArray();
            
            for (SWSException e: this.exceptions) {
                if (e!=null) {
                    
                    JSONObject exception = new JSONObject();
                    
                    String code = e.getCode();
                    if (code!=null && !code.isEmpty()) {
                        exception.put("code", code);
                    }
                    
                    String locator = e.getLocator();
                    if (locator!=null && !locator.isEmpty()) {
                        exception.put("locator", locator);
                    }

                    String msg = e.getMessage();
                    if (msg!=null && !msg.isEmpty()) {
                        exception.put("message", msg);
                    }
                    
                    exceptionArray.add(exception);
                }
            }
            
            exceptionReport.put("exceptions", exceptionArray);

            return exceptionReport;

        }
        else {
            return null;
        }
    }
    
    
    
    
}
