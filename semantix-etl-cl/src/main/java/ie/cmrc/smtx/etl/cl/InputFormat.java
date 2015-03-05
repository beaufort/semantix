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

package ie.cmrc.smtx.etl.cl;

/**
 * Utility enumeration for storing the Conventional Field Names (CFN) used by the
 * tabular thesaurus extractor {@link ie.cmrc.swsetl.ext.tabular.TabularExtractor}
 * @author Yassine Lassoued
 */
public enum InputFormat {
    
    
    
    /**
     * Excel format
     */
    excel,
    
    /**
     * ASCII format
     */
    ascii;
    
    
    
    /**
     * {@inheritDoc}
     * @return String representation of this input format. This is the
     * same result as {@code name()}.
     */
    @Override
    public String toString() {
        return this.name();
    }

    /**
     * Parses a string and returns the matching {@code InputFormat}
     * @param string {@code String} to parse
     * @return {@link CFN} matching the parsed {@code String} if
     * possible, otherwise {@code null}
     */
    public static InputFormat fromString(String string) {
        if (string != null) {
            for (InputFormat inputFormat: InputFormat.values()) {
                if (string.trim().toLowerCase().equals(inputFormat.name().toLowerCase())) return inputFormat;
            }
        }
        return null;
    }
    
    
    
    
}
