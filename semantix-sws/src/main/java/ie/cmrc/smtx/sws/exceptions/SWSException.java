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

/**
 *
 * @author yassine
 */
public class SWSException extends Exception {
    private String code;

    /**
     * The optional locator attribute may be used to indicate where an exception was encountered in the request that generated the error.
     */
    private String locator;


    public SWSException(String code) {
        super();
        this.code = code;
    }

    public SWSException(String code, String message) {
        super(message);
        this.code = code;
    }

    public SWSException(String code, String message, String locator) {
        super(message);
        this.code = code;
        this.locator = locator;
    }









    public String getCode() {
        return code;
    }

    public String getLocator() {
        return locator;
    }
}
