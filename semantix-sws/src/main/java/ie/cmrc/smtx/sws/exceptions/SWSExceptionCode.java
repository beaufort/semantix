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
public class SWSExceptionCode {
    /**
     * The parameter value is not valid.
     */
    public static final String INVALID_PARAMETER_VALUE = "InvalidParameterValue";

    /**
     * The request message is either invalid or is not well-formed.
     */
    public static final String INVALID_REQUEST = "InvalidRequest";

    /**
     * The (abstract) operation has not been implemented.
     */
    public static final String NOT_IMPLEMENTED = "NotImplemented";

    /**
     * The requested resource does not exist or could not be found.
     */
    public static final String RESOURCE_NOT_FOUND = "ResourceNotFound";

    /**
     * The requested resource does not exist or could not be found.
     */
    public static final String RESOURCE_TYPE_MISMATCH = "ResourceTypeMismatch";

    /**
     * A requested resource has a null value.
     */
    public static final String NULL_RESOURCE_VALUE = "NullResourceValue";

    /**
     * A service option, feature, or capability is not supported.
     */
    public static final String NOT_SUPPORTED = "NotSupported";

    /**
     * There is no applicable code to this exception
     */
    public static final String NO_APPLICABLE_CODE = "NoApplicableCode";

    /**
     * A parameter is missing
     */
    public static final String MISSING_PARAMETER = "MissingParameter";

    /**
     * Null value exception: a required parameter or variable is null
     */
    public static final String NULL_VALUE = "NullValue";

    /**
     * Internal error to the CS
     */
    public static final String INTERNAL_ERROR = "InternalError";

    /**
     * Unknown error
     */
    public static final String UNKNOWN_ERROR = "UnknownError";
}
