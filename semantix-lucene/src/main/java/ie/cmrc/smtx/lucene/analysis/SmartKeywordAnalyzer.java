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
 * An analyser that is similar to the Lucene KeywordAnalyzer
 * except that it applies a lower case and an ASCII folding filters
 * @author Yassine Lassoued <y.lassoued@ucc.ie>
 */
public class SmartKeywordAnalyzer extends EuropeanAnalyzer {

    
    /**
     * Constructs a {@link SmartKeywordAnalyzer}
     */
    public SmartKeywordAnalyzer() {
        super();
    }
    
    @Override
    protected TokenStreamComponents createComponents(String fieldName, String language, Reader reader) {
        Tokenizer source = new StandardTokenizer(reader);
        TokenStream filter = new StandardFilter(source);
        filter = new LowerCaseFilter(filter);
        filter = new StopFilter(filter, this.getStopWordsSet(language));
        filter = getMinimalStemFilter(language, filter);
        filter = new ASCIIFoldingFilter(filter);
        filter = new ConcatFilter(filter);
        return new TokenStreamComponents(source, filter);
    }
    
    
    @Override
    public int getPositionIncrementGap(String fieldName) {
      return 10;
    }
}
