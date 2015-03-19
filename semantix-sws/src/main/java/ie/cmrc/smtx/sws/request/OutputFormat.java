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
public enum OutputFormat {
    TEXT_XML("text/xml"),
    APPLICATION_XML("application/xml"),
    APPLICATION_JSON("application/json");
    
    
    /**
     * String value of the {@code OutputFormat}
     */
    private final String value;
    
    /**
     * Creates an {@code OutputFormat} using the associated String value
     * @param value String value of the {@code OutputFormat}
     */
    OutputFormat(String value) {
        this.value = value;
    }

    /**
     * String value of the {@code OutputFormat}
     * @return String value of the {@code OutputFormat}
     */
    public String value() {
        return this.value;
    }

    /**
     * String representation of the {@code OutputFormat}
     * @return String representation of the {@code OutputFormat}
     */
    @Override
    public String toString() {
        return this.value;
    }
    
    
    /**
     * Parses a string and returns the matching {@code OutputFormat}
     * @param stringFormat {@code String} to parse
     * @return {@code OutputFormat} matching the parsed {@code String} if
     * possible, otherwise {@code null}
     */
    public static OutputFormat fromString(String stringFormat) {
        if (stringFormat != null) {
            for (OutputFormat outputFormat: OutputFormat.values()) {
                if (stringFormat.equals(outputFormat.value())) return outputFormat;
            }
        }
        return null;
    }
}
