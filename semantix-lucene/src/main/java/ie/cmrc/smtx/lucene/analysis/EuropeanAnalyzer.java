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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.de.GermanAnalyzer;
import org.apache.lucene.analysis.de.GermanMinimalStemFilter;
import org.apache.lucene.analysis.de.GermanStemFilter;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.en.KStemFilter;
import org.apache.lucene.analysis.en.PorterStemFilter;
import org.apache.lucene.analysis.es.SpanishAnalyzer;
import org.apache.lucene.analysis.es.SpanishLightStemFilter;
import org.apache.lucene.analysis.fr.FrenchAnalyzer;
import org.apache.lucene.analysis.fr.FrenchMinimalStemFilter;
import org.apache.lucene.analysis.it.ItalianAnalyzer;
import org.apache.lucene.analysis.it.ItalianLightStemFilter;
import org.apache.lucene.analysis.no.NorwegianAnalyzer;
import org.apache.lucene.analysis.no.NorwegianMinimalStemFilter;
import org.apache.lucene.analysis.pt.PortugueseAnalyzer;
import org.apache.lucene.analysis.pt.PortugueseMinimalStemFilter;
import org.apache.lucene.analysis.pt.PortugueseStemFilter;
import org.apache.lucene.analysis.snowball.SnowballFilter;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.util.CharArraySet;
import org.tartarus.snowball.ext.FrenchStemmer;
import org.tartarus.snowball.ext.ItalianStemmer;
import org.tartarus.snowball.ext.NorwegianStemmer;
import org.tartarus.snowball.ext.SpanishStemmer;

/**
 *
 * @author Yassine Lassoued <y.lassoued@ucc.ie>
 */
public abstract class EuropeanAnalyzer extends MultilingualAnalyzer {
    
    /**
     * List of languages supported by the {@link EuropeanAnalyzer} class
     */
    public static final List<String> SUPPORTED_LANGUAGES;
    /**
     * English language code
     */
    public static final String LANG_EN = "en";
    /**
     * French language code
     */
    public static final String LANG_FR = "fr";
    /**
     * Spanish language code
     */
    public static final String LANG_ES = "es";
    /**
     * Portuguese language code
     */
    public static final String LANG_PT = "pt";
    /**
     * Italian language code
     */
    public static final String LANG_IT = "it";
    /**
     * German language code
     */
    public static final String LANG_DE = "de";
    /**
     * Norwegian language code
     */
    public static final String LANG_NO = "no";
    /**
     * Stop words set per language
     */
    protected static Map<String, CharArraySet> cache = new HashMap<String, CharArraySet>(8);

    static {
        int n = 7;
        List<String> supportedLangs = new ArrayList<String>(n);
        supportedLangs.add(LANG_EN);
        supportedLangs.add(LANG_FR);
        supportedLangs.add(LANG_ES);
        supportedLangs.add(LANG_PT);
        supportedLangs.add(LANG_IT);
        supportedLangs.add(LANG_DE);
        supportedLangs.add(LANG_NO);
        
        SUPPORTED_LANGUAGES = Collections.unmodifiableList(supportedLangs);
    }
    
    /**
     * {@inheritDoc}
     * @param language {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public boolean supportsLanguage(String language) {
        String lang = language;
        if (lang != null) {
            lang = lang.trim().toLowerCase();
        }
        return SUPPORTED_LANGUAGES.contains(lang);
    }

    /**
     * Gets the stop words set for the provided language
     * @param language Two-letter code of a language
     * @return {@code CharArraySet} containing the stop words of the provided language.
     * If the provided language is not supported,then the Lucene standard stop words set
     * if returned.
     */
    protected CharArraySet getStopWordsSet(String language) {
        String lang = language;
        if (lang != null) lang = lang.trim().toLowerCase();
        CharArraySet charArraySet = cache.get(lang);
        if (charArraySet == null) {
            if (SUPPORTED_LANGUAGES.contains(lang)) {
                if (lang.equals(LANG_EN)) {
                    charArraySet = EnglishAnalyzer.getDefaultStopSet();
                } else if (lang.equals(LANG_FR)) {
                    charArraySet = FrenchAnalyzer.getDefaultStopSet();
                } else if (lang.equals(LANG_ES)) {
                    charArraySet = SpanishAnalyzer.getDefaultStopSet();
                } else if (lang.equals(LANG_PT)) {
                    charArraySet = PortugueseAnalyzer.getDefaultStopSet();
                } else if (lang.equals(LANG_IT)) {
                    charArraySet = ItalianAnalyzer.getDefaultStopSet();
                } else if (lang.equals(LANG_DE)) {
                    charArraySet = GermanAnalyzer.getDefaultStopSet();
                } else if (lang.equals(LANG_NO)) {
                    charArraySet = NorwegianAnalyzer.getDefaultStopSet();
                }
            } else {
                charArraySet = StandardAnalyzer.STOP_WORDS_SET;
            }
            cache.put(lang, charArraySet);
        }
        return charArraySet;
    }
    
    /**
     * Returns a minimal/light stemming filter suitable for the provided language
     * @param language Two-letter code of a language
     * @param input {@code org.apache.lucene.analysis.TokenStream} input to
     * filter
     * @return {@code org.apache.lucene.analysis.TokenStream} that filters the
     * provided {@code input}
     */
    protected TokenStream getMinimalStemFilter(String language, TokenStream input) {
        String lang = language;
        if (lang != null) lang = lang.trim().toLowerCase();
        if (SUPPORTED_LANGUAGES.contains(lang)) {
            if (lang.equals(LANG_EN)) {
                return new KStemFilter(input);
            } else if (lang.equals(LANG_FR)) {
                return new FrenchMinimalStemFilter(input);
            } else if (lang.equals(LANG_ES)) {
                return new SpanishLightStemFilter(input);
            } else if (lang.equals(LANG_PT)) {
                return new PortugueseMinimalStemFilter(input);
            } else if (lang.equals(LANG_IT)) {
                return new ItalianLightStemFilter(input);
            } else if (lang.equals(LANG_DE)) {
                return new GermanMinimalStemFilter(input);
            } else if (lang.equals(LANG_NO)) {
                return new NorwegianMinimalStemFilter(input);
            }
        } 
        return input;
    }
    
    /**
     * Returns an aggressive stemming filter suitable for the provided language
     * @param language Two-letter code of a language
     * @param input {@code org.apache.lucene.analysis.TokenStream} input to
     * filter
     * @return {@code org.apache.lucene.analysis.TokenStream} that filters the
     * provided {@code input}
     */
    protected TokenStream getStemFilter(String language, TokenStream input) {
        String lang = language;
        if (lang != null) lang = lang.trim().toLowerCase();
        if (SUPPORTED_LANGUAGES.contains(lang)) {
            if (lang.equals(LANG_EN)) {
                return new PorterStemFilter(input);
            } else if (lang.equals(LANG_FR)) {
                return new SnowballFilter(input, new FrenchStemmer());
            } else if (lang.equals(LANG_ES)) {
                return new SnowballFilter(input, new SpanishStemmer());
            } else if (lang.equals(LANG_PT)) {
                return new PortugueseStemFilter(input);
            } else if (lang.equals(LANG_IT)) {
                return new SnowballFilter(input, new ItalianStemmer());
            } else if (lang.equals(LANG_DE)) {
                return new GermanStemFilter(input);
            } else if (lang.equals(LANG_NO)) {
                return new SnowballFilter(input, new NorwegianStemmer());
            }
        } 
        return input;
    }
    
}
