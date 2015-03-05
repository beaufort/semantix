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

package ie.cmrc.smtx.sws.config;

/**
 * Enumerates the possible request filters that may be required by an SWS
 * @author Yassine Lassoued <y.lassoued@ucc.ie>
 */
public enum FilterType {
    
    /**
     * No filter is required for extracting concepts
     */
    NONE("none"),
    
    /**
     * Concept requests must include a filter by concept scheme
     */
    CS("cs"),
    
    /**
     * Concept requests must include a filter by collection
     */
    COLLECTION("collection"),
    
    /**
     * Concept requests must include a filter by concept scheme or collection
     */
    CS_OR_COLLECTION("csOrCollection"),
    
    /**
     * Concept requests must include a filter by concept scheme and collection
     */
    CS_AND_COLLECTION("csAndCollection");
    
    private final String value;
    
    private FilterType(String value) {
        this.value = value;
    }
    
    /**
     * String value of the {@code FilterType}
     * @return String value of the {@code FilterType}
     */
    public String value() {
      return this.value;
    }

    /**
     * String representation of the {@code FilterType}
     * @return String representation of the {@code FilterType}
     */
    @Override
    public String toString() {
      return this.value;
    }

    /**
     * {@code FilterType} matching the provided String
     * @param stringValue String to parse
     * @return {@code FilterType} matching the provided text if any, otherwise {@code null}
     */
    public static FilterType fromString(String stringValue) {
      if (stringValue != null) {
          for (FilterType filterType: FilterType.values()) {
              if (stringValue.equals(filterType.value)) return filterType;
          }
      }
      return null;
    }
    
}
