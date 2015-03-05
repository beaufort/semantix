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

import java.io.Reader;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.miscellaneous.ASCIIFoldingFilter;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;

/**
 * a multiligual analyser that
 * associates with each field a suitable set of stop words
 * ({@link LanguageBasedAnalyzer}) depending on the field's language.
 * The field language is encoded according to the following convention [field_name]@[language_code],
 * e.g., definition@en, prefLabel@fr. If a field has no language (e.g., id),
 * then a standard analyzer is associated with it.
 * @author yassine
 */
public class StandardEuropeanAnalyzer extends EuropeanAnalyzer {

    
    /**
     * Constructs a {@link StandardEuropeanAnalyzer}
     */
    public StandardEuropeanAnalyzer() {
        super();
    }
    
    @Override
    protected TokenStreamComponents createComponents(String fieldName, String language, Reader reader) {
        Tokenizer source = new StandardTokenizer(reader);
        TokenStream filter = new StandardFilter(source);
        filter = new LowerCaseFilter(filter);
        filter = new StopFilter(filter, this.getStopWordsSet(language));
        filter = getStemFilter(language, filter);
        filter = new ASCIIFoldingFilter(filter);
        return new TokenStreamComponents(source, filter);
    }
    
}
