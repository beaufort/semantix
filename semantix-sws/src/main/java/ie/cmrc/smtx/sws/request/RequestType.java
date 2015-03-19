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

package ie.cmrc.smtx.sws.request;

/**
 * Enumeration of the SWS request types
 * @author Yassine Lassoued <y.lassoued@ucc.ie>
 */
public enum RequestType {
    
    GetCapabilities,
    GetConceptSchemes,
    GetConceptScheme,
    GetCollections,
    GetCollection,
    GetCollectionMembers,
    GetTopConcepts,
    GetBroadestConcepts,
    GetConcepts,
    GetConcept,
    GetConceptHierarchy,
    GetRelatedConcepts,
    GetDirectNarrowerConcepts,
    GetDirectBroaderConcepts,
    SearchConcepts,
    InterpretKeyword;
    
    /**
     * Parses a string and returns the matching {@code RequestType}
     * @param requestString {@code String} to parse
     * @return {@code RequestType} matching the parsed {@code String} if
     * possible, otherwise {@code null}
     */
    public static RequestType fromString(String requestString) {
        try {
            return RequestType.valueOf(requestString);
        }
        catch (IllegalArgumentException ex) {
            return null;
        }
    }
}
