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

package ie.cmrc.smtx.skos.model.util;

import java.util.Iterator;

/**
 * An iterator that may be closed after use to free up any memory resources used by
 * the iterator. Implementations may chose to close the iterator when it is exhausted.
 * @author Yassine Lassoued <y.lassoued@ucc.ie>
 * @param <T> Type of elements returned by this iterator
 */
public interface CloseableIterator<T> extends Iterator<T> {
    /**
     * Closes the iterator to free any memory resources being used by it. If the
     * iterator is already closed, then calling this method will do nothing.
     */
    void close();
}
