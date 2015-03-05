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
 *
 * @author yassine
 */
public class SWSVersion {
    public static final String VERSION_2_0 = "2.0";

    public static final String VERSION_2_0_URI = "http://cmrc.ucc.ie/sws/2.0";

    public static Boolean validate(String value) {
        if (value!=null && value.equals(VERSION_2_0)) return true;
        else return false;
    }

    public static String[] possibleValues() {
        String[] values = {VERSION_2_0};
        return values;
    }

    public static String getVersionUri(String version) {
        if (SWSVersion.validate(version)) {
            return ("http://cmrc.ucc.ie/cswm/"+version);
        }
        else return null;
    }
}
