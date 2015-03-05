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

import java.util.NoSuchElementException;

/**
 * A convenience class that defines an empty {@link CloseableIterator}
 * @author Yassine Lassoued <y.lassoued@ucc.ie>
 * @param <T> Type of elements returned by this iterator
 */
public class EmptyCloseableIterator<T> implements CloseableIterator<T> {

    /**
     * Returns {@code false} all the time
     * @return {@code false}
     */
    @Override
    public boolean hasNext() {return false;}

    /**
     * @throws java.util.NoSuchElementException
     */
    @Override
    public T next() {
        throw new NoSuchElementException("Iterator has no more elements!");
    }

    /**
     * @throws UnsupportedOperationException
     */
    @Override
    public void remove() {
        throw new UnsupportedOperationException("Remove() is not supported Not supported by "+EmptyCloseableIterator.class.getName());
    }
    
    /**
     * Does nothing
     */
    @Override
    public void close() {}
}
