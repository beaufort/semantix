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

package ie.cmrc.smtx.thesaurus.search;

import ie.cmrc.smtx.skos.model.SKOSConcept;

/**
 *
 * @author yassine
 */
public class ConceptMatch implements Comparable<ConceptMatch> {

    

    private SKOSConcept concept;
    private double match;

    public ConceptMatch() {
        super();
    }

    public ConceptMatch(SKOSConcept concept, double match) {
        this.concept = concept;
        this.match = match;
    }

    public SKOSConcept getConcept() {
        return concept;
    }

    public double getMatch() {
        return match;
    }

    public void setConcept(SKOSConcept concept) {
        this.concept = concept;
    }

    public void setMatch(double match) {
        this.match = match;
    }

    @Override
    public int compareTo(ConceptMatch cm) {
        if (this.match > cm.getMatch()) return 1;
        else if (this.match < cm.getMatch()) return -1;
        else return this.concept.compareTo(cm.getConcept());
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) {
            return true;
        }
        if (!(obj instanceof ConceptMatch)) {
              return false;
        }
        ConceptMatch cMatch = (ConceptMatch)obj;

        if (match!=cMatch.match) {
           return false;
        }
        else {
           if (concept!=null) {
               return (concept.equals(cMatch.concept));
           }
           else {
               return (cMatch.concept==null);
           }
        }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + (this.concept != null ? this.concept.hashCode() : 0);
        hash = 41 * hash + (int) (Double.doubleToLongBits(this.match) ^ (Double.doubleToLongBits(this.match) >>> 32));
        return hash;
    }

}
