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

package ie.cmrc.smtx.etl.ext.tabular;

/**
 * A utility class for parsing semantic resource names and URIs
 * @author Yassine Lassoued
 */
public class ResourceParser {
    
    /**
     * Parses a resource URI or name and prefixes it with the provided namespace if needed
     * @param value {@code String} value to parse. If the value starts with {@code "<"}
     * and ends with {@code ">"}, then it is interpreted as a URI, otherwise it is
     * interpreted as a local name which will then be be prefixed with the
     * provided default namespace.
     * 
     * @param defaultNameSpace Default namespace to use as a prefix.
     * @return Resource URI.
     * <p>Examples:
     * <p>{@code parseResourceURI("<http://example.com/Concept/Sensor>", "http://domain.com/")}
     * returns {@code "http://example.com/Concept/Sensor"} (note that in this case, the default URI is ignored).
     * <p>{@code parseResourceURI("Sensor", "http://domain.com/Concept/")} returns {@code "http://domain.com/Concept/Sensor"}.
     */
    public static String parseResourceURI(String value, String defaultNameSpace) {
        if (value!=null) {
            String name = value.trim();
            if (!name.isEmpty()) {
                if (name.startsWith("<") && name.endsWith(">")) {
                    name = name.substring(1, name.length()-1).trim();
                }
                else {
                    if (defaultNameSpace != null) name = defaultNameSpace.trim() + name;
                }
                return name;
            }
        }
        return null;
    }
    
}
