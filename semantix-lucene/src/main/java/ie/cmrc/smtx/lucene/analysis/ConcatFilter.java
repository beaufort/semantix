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

import java.io.IOException;
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

/**
 * The {@code ConcatFilter} allows you to join tokens produced by upstream
 * tokenisers into a single term. This is useful for
 * implementing keyword analysers that perfom some analysis on the text before
 * rather than taking the text as is. For instance, you may perform ASCII folding,
 * stemming, etc., then concatenate the tokens.
 * @see AdvancedConcatFilter
 * @author Yassine Lassoued <y.lassoued@ucc.ie>
 */
public final class ConcatFilter extends TokenFilter {
    private final static String DEFAULT_TOKEN_SEPARATOR = " ";

    private final CharTermAttribute termAtt = addAttribute(CharTermAttribute.class);
    private String tokenSeparator = null;
    private final StringBuilder builder = new StringBuilder();
    
    /**
     * Creates a {@link ConcatFilter} that concatenates the tokens of the
     * provided input using space (" ") as a separator
     * @param input 
     */
    public ConcatFilter(TokenStream input) {
        this(input, null);
    }
    
    /**
     * Creates a {@link ConcatFilter} that concatenates the tokens of the
     * provided input using the provided separator
     * @param input
     * @param tokenSeparator 
     */
    public ConcatFilter(TokenStream input, String tokenSeparator) {
        super(input);
        this.tokenSeparator = tokenSeparator!=null ? tokenSeparator : DEFAULT_TOKEN_SEPARATOR;
    }

    @Override
    public boolean incrementToken() throws IOException {
        boolean result = false;
        builder.setLength(0);
        while (input.incrementToken()) {
            if (builder.length()>0) {
                // append the token separator
                builder.append(tokenSeparator);
            }
            // append the term of the current token
            builder.append(termAtt.buffer(), 0, termAtt.length());
        }
        if (builder.length()>0) {
            termAtt.setEmpty().append(builder);
            result = true;
        }
        return result;
    }

}
