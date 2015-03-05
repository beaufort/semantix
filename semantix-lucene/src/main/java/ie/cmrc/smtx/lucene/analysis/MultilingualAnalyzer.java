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

package ie.cmrc.smtx.lucene.analysis;

import ie.cmrc.util.Term;
import java.io.Reader;
import org.apache.lucene.analysis.Analyzer;

/**
 * An abstract multilingual 
 * @author Yassine Lassoued <y.lassoued@ucc.ie>
 */
public abstract class MultilingualAnalyzer extends Analyzer {
    
    @Override
    protected TokenStreamComponents createComponents(String fieldName, Reader reader) {
        Term field = new Term(fieldName);
        
        return this.createComponents(field.getString(), field.getLanguage(), reader);
    }
    
    /**
     * Checks whether this {@link MultilingualAnalyzer} supports the provided
     * language
     * @param language Two-letter code of a language
     * @return {@code true} if {@code language} is supported, {@code false}
     * otherwise or if the {@code language} is {@code null} or empty
     */
    abstract public boolean supportsLanguage(String language);
    
    abstract protected TokenStreamComponents createComponents(String fieldName, String language, Reader reader);
    
}
