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

import java.util.Objects;

/**
 *
 * @author Yassine Lassoued <y.lassoued@ucc.ie>
 */
public abstract class AbstractSemanticEntity implements SemanticEntity {

    
    /**
     * URI of the semantic entity
     */
    protected String uri;
    
    /**
     * Namespace of the semantic entity
     */
    private String nameSpace;
    
    /**
     * Local name of the semantic entity
     */
    private String localName;

    /**
     * Semantic entity are compared based on their labels. As these are typically
     * in different languages, we need to specify the language to use for
     * comparing entities.
     */
    private String comparisonLanguage = null;

    /**
     * Constructs a {@link AbstractSemanticEntity} with the provided URI
     * @param uri URI of the resource to construct
     */
    protected AbstractSemanticEntity(String uri) {
        this.uri = uri;
        //if (uri==null) throw new IllegalArgumentException("");
        this.parseURI();
    }

    /**
     * Parses the resource's URI to extract the namespace and local name 
     */
    private void parseURI() {
        this.nameSpace = null;
        this.localName = null;
        
        if (this.uri!=null) {
            if (this.uri.startsWith("urn:")) {
                //This is a URN
                String[] tokens = this.uri.split(":");
                int l = tokens.length;
                this.localName = tokens[l-1];
                this.nameSpace = tokens[0];
                for (int i=1; i<l-1; i++) {
                    this.nameSpace += ":" + tokens[i];
                }
            }
            
            else {
                String trimmedUri = this.uri;
                if (trimmedUri.endsWith("/")) trimmedUri = trimmedUri.substring(0, trimmedUri.length()-1);

                String[] hashSplitUri = trimmedUri.split("#");
                if (hashSplitUri.length>1) {
                    int l = hashSplitUri.length;
                    this.localName = hashSplitUri[l-1];
                    this.nameSpace = hashSplitUri[0];
                    for (int i=1; i<l-1; i++) {
                        this.nameSpace += "#" +  hashSplitUri[i];
                    }
                }
                else {
                    String[] slashSplitUri = trimmedUri.split("/");
                    if (slashSplitUri.length>1) {
                        int l = slashSplitUri.length;
                        this.localName = slashSplitUri[l-1];
                        this.nameSpace = slashSplitUri[0];
                        for (int i=1; i<l-1; i++) {
                            this.nameSpace += "/"+slashSplitUri[i];
                        }
                    }
                    else {
                        this.localName = this.uri;
                    }
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public String getLocalName() {
        return this.localName;
    }

    /**
     * {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public String getNameSpace() {
        return this.nameSpace;
    }

    /**
     * {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public String getURI() {
        return this.uri;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 41 * hash + Objects.hashCode(this.getURI());
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (! (obj instanceof SemanticEntity)) {
            return false;
        }
        final SemanticEntity other = (SemanticEntity) obj;
        return Objects.equals(this.getURI(), other.getURI());
    }
    
    
    /**
     * Compares the semantic entity to {@code otherResource} based on their
     * preferred labels, then their local names. The language of the preferred
     * label for each semantic entity is that set using the
     * {@linkplain #setComparisonLanguage(java.lang.String)} method.
     * If the comparison language has not been set previously, then it
     * defaults to {@code null}.
     * @param otherResource Resource to compare this to
     * @return {@inheritDoc}
     */
    @Override
    public int compareTo(SemanticEntity otherResource) {
        if (!this.equals(otherResource)) {
            if (otherResource!=null) {
                String myCompLang = this.getComparisonLanguage();
                String resCompLang = otherResource.getComparisonLanguage();

                String myCompString1 = null;
                if (myCompLang!=null) myCompString1 = this.getPrefLabel(myCompLang);
                if (myCompString1==null) myCompString1 = "";

                String resCompString1 = null;
                if (resCompLang!=null) resCompString1 = otherResource.getPrefLabel(resCompLang);
                if (resCompString1==null) resCompString1 = "";

                if (!myCompString1.equals(resCompString1)) return myCompString1.compareTo(resCompString1);
                else {
                    String myCompString2 = this.getLocalName();
                    if (myCompString2==null) myCompString2 = "";
                    
                    String resCompString2 = otherResource.getLocalName();
                    if (resCompString2==null) resCompString2 = "";
                    
                    
                    if (!myCompString2.equals(resCompString2)) return myCompString2.compareTo(resCompString2);
                    else {
                        String myCompString3 = this.getURI();
                        if (myCompString3==null) myCompString3 = "";

                        String resCompString3 = otherResource.getURI();
                        if (resCompString3==null) resCompString3 = "";

                        return myCompString3.compareTo(resCompString3);
                    }
                }
            }
            else return 1;
        }
        else return 0;
    }

    /**
     * {@inheritDoc}
     * @param lang {@inheritDoc}
     */
    @Override
    public void setComparisonLanguage(String lang) {
        this.comparisonLanguage = lang;
    }

    /**
     * {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public String getComparisonLanguage() {
        return this.comparisonLanguage;
    }
    
}
