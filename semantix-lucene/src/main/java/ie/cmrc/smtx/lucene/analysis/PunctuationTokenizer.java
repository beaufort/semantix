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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.apache.lucene.analysis.util.CharTokenizer;
import org.apache.lucene.util.AttributeFactory;
import org.apache.lucene.util.Version;

/**
 *
 * @author Yassine Lassoued
 */
public class PunctuationTokenizer extends CharTokenizer {

    public static final char SPACE = ' ';
    public static final char TAB = '\t';
    public static final char RETURN = '\n';
    public static final char COMMA = ',';
    public static final char DOT = '.';
    public static final char QUESTION_MARK = '?';
    public static final char EXCLAMATION_MARK = '!';
    public static final char COLON = ':';
    public static final char SEMI_COLON = ';';
    public static final char SLASH = '/';
    public static final char SINGLE_QUOTE = '\'';
    public static final char DOUBLE_QUOTE = '\"';
    
    
    private static final List<Integer> tokenSeparators = Arrays.asList(
        Character.getNumericValue(SPACE),
        Character.getNumericValue(TAB),
        Character.getNumericValue(RETURN),
        Character.getNumericValue(COMMA),
        Character.getNumericValue(DOT),
        Character.getNumericValue(QUESTION_MARK),
        Character.getNumericValue(EXCLAMATION_MARK),
        Character.getNumericValue(COLON),
        Character.getNumericValue(SEMI_COLON),
        Character.getNumericValue(SLASH),
        Character.getNumericValue(SINGLE_QUOTE),
        Character.getNumericValue(DOUBLE_QUOTE));
    
    
    public PunctuationTokenizer(Version matchVersion, Reader input) {
        super(matchVersion, input);
    }

    public PunctuationTokenizer(Version matchVersion, AttributeFactory factory, Reader input) {
        super(matchVersion, factory, input);
    }
    
    
    @Override
    protected boolean isTokenChar(int i) {
        return (!tokenSeparators.contains(Character.getNumericValue(i)));
    }
    
    private static List<Integer> getTokenSeparators() {
        return Collections.unmodifiableList(tokenSeparators);
    }
    
}
