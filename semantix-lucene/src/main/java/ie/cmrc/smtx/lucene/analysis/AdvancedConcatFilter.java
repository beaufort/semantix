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
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.util.AttributeSource;

/**
 * The {@code AdvancedConcatFilter} allows you to join tokens produced by upstream
 * tokenisers into a single term. This is useful for
 * implementing keyword analysers that perfom some analysis on the text before
 * rather than taking the text as is. For instance, you may perform ASCII folding,
 * stemming, etc., then concatenate the tokens.<br/>
 * This concatenation filter is different than {@link ConcatFilter} in the way
 * that it generates multiple phrase tokens if synonyms are encountered in the
 * tokens (using position increment == 0).
 * @see ConcatFilter
 * @author Yassine Lassoued <y.lassoued@ucc.ie>
 */
public final class AdvancedConcatFilter extends TokenFilter {
    private final CharTermAttribute termAttr;
    private final PositionIncrementAttribute posIncAttr;

    private AttributeSource.State current;
    private final LinkedList<List<String>> words;
    private final LinkedList<String> phrases;

    private boolean concat = false;

    protected AdvancedConcatFilter(TokenStream input) {
        super(input);
        this.termAttr = addAttribute(CharTermAttribute.class);
        this.posIncAttr = addAttribute(PositionIncrementAttribute.class);
        this.words = new LinkedList<>();
        this.phrases = new LinkedList<>();
    }

    @Override
    public boolean incrementToken() throws IOException {
        int i = 0;
        while (input.incrementToken()) {
            String term = new String(termAttr.buffer(), 0, termAttr.length());
            List<String> word = posIncAttr.getPositionIncrement() > 0 ? new ArrayList<String>() : words.removeLast();
            word.add(term);
            words.add(word);
            i++;
        }
        // now write out as a single token
        if (! concat) {
            makePhrases(words, phrases, 0);
            concat = true;
        }
        while (phrases.size() > 0) {
            String phrase = phrases.removeFirst();
            restoreState(current);
            clearAttributes();
            termAttr.copyBuffer(phrase.toCharArray(), 0, phrase.length());
            termAttr.setLength(phrase.length());
            current = captureState();
            return true;
        }
        concat = false;
        return false;
    }

    private void makePhrases(List<List<String>> words, 
        List<String> phrases, int currPos) {
        if (currPos == words.size()) {
            return;
        }
        if (phrases.isEmpty()) {
            phrases.addAll(words.get(currPos));
        } else {
            List<String> newPhrases = new ArrayList<>();
            for (String phrase : phrases) {
                for (String word : words.get(currPos)) {
                    //newPhrases.add(StringUtils.join(new String[] {phrase, word}, " "));
                    newPhrases.add(phrase+" "+word);
                }
            }
            phrases.clear();
            phrases.addAll(newPhrases);
        }
        makePhrases(words, phrases, currPos + 1);
    }
}
