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

/**
 * A Factory class for creating various useful Lucene analysers
 * @author Yassine Lassoued
 */
public class AnalyzerFactory {
    
    
    /**
     * Creates a suitable analyser for the provided language.
     * @param languageCode Two-letter code of a language. Check
     * {@code LanguageBasedAnalyzer.SUPPORTED_LANGUAGES} for the list of supported
     * language codes. Language codes are case-insensitive.
     * @return {@link LanguageBasedAnalyzer} for the provided language. If the
     * requested language is not supported than a standard analyzer is returned.
     */
    public static LanguageBasedAnalyzer createLanguageBasedAnalyzer(String languageCode) {
        return new LanguageBasedAnalyzer(languageCode);
    }
    
    /**
     * Creates a multilingual analyser ({@link StandardEuropeanAnalyzer}) that
     * associates with each field a suitable set of stop words
     * ({@link LanguageBasedAnalyzer}) depending on the field's language.
     * The field language is encoded according to the following convention [field_name]@[language_code],
     * e.g., definition@en, prefLabel@fr. If a field has no language (e.g., id),
     * then a standard analyzer is associated with it.
     * @return ({@link StandardEuropeanAnalyzer}) instance
     */
    public static StandardEuropeanAnalyzer createMultilingualAnalyzer() {
        return new StandardEuropeanAnalyzer();
    }
    
    /**
     * Creates an analyzer that is similar to the Lucene KeywordAnalyzer
     * except that it applies a lower case, ASCII folding, and stemming filters
     * @return {@code KeywordAnalyzer} instance
     */
    public static SmartKeywordAnalyzer createSmartKeywordAnalyzer() {
        return new SmartKeywordAnalyzer();
    }
}
