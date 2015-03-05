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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.en.KStemFilter;
import org.apache.lucene.analysis.miscellaneous.ASCIIFoldingFilter;
import org.apache.lucene.analysis.standard.ClassicTokenizer;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.util.CharArraySet;

/**
 * An analyzer that is suitable for a given language 
 * @author yassine
 */
public class LanguageBasedAnalyzer extends Analyzer {
    /**
     * List of languages supported by the {@link LanguageBasedAnalyzer} class
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
     * Default standard analyser
     * @return Default {@link LanguageBasedAnalyzer}
     */
    public static final LanguageBasedAnalyzer defaultAnalyzer() {
        return new LanguageBasedAnalyzer(null);
    }
    
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
     * Language of the analyser
     */
    private final String language;
    
    /**
     * Constructs an analyser for the provide language
     * @param languageCode Two-letter code of a language. If the language is not
     * supported by the {@link LanguageBasedAnalyzer}, then a default standard
     * analyser is constructed.
     */
    public LanguageBasedAnalyzer(String languageCode) {
        String lang = languageCode;
        if (lang != null) lang = lang.trim().toLowerCase();
        this.language = lang;
    }
    
    @Override
    protected TokenStreamComponents createComponents(String fieldName, Reader reader) {
        //Tokenizer source = new StandardTokenizer(Version.LUCENE_46, reader);
        //Tokenizer source = new WhitespaceTokenizer(Version.LUCENE_46, reader);
        Tokenizer source = new ClassicTokenizer(reader);
        TokenStream filter = new StandardFilter(source);
        filter = new LowerCaseFilter(filter);
        filter = new StopFilter(filter, this.getStopWordsSet(language));
        filter = new KStemFilter(filter);
        filter = new ASCIIFoldingFilter(filter);
        return new TokenStreamComponents(source, filter);
    }
    
    /**
     * Checks whether the {@link LanguageBasedAnalyzer} class supports the provided language
     * @param language Two-letter code of a language
     * @return {@code true} if {@code language} is supported, {@code false} otherwise
     * or if the {@code language} is {@code null} or empty
     */
    public static boolean supportsLanguage(String language) {
        String lang = language;
        if (lang != null) lang = lang.trim().toLowerCase();
        return SUPPORTED_LANGUAGES.contains(lang);
    }
    
    /**
     * Gets the stop words set for the provided language
     * @param language Two-letter code of a language
     * @return {@code CharArraySet} containing the stop words of the provided language.
     * If the provided language is not supported,then the Lucene standard stop words set
     * if returned.
     */
    private CharArraySet getStopWordsSet(String language) {
        if (SUPPORTED_LANGUAGES.contains(language)) {
            if (language.equals(LANG_EN)) return org.apache.lucene.analysis.en.EnglishAnalyzer.getDefaultStopSet();
            else if (language.equals(LANG_FR)) return org.apache.lucene.analysis.fr.FrenchAnalyzer.getDefaultStopSet();
            else if (language.equals(LANG_ES)) return org.apache.lucene.analysis.es.SpanishAnalyzer.getDefaultStopSet();
            else if (language.equals(LANG_PT)) return org.apache.lucene.analysis.pt.PortugueseAnalyzer.getDefaultStopSet();
            else if (language.equals(LANG_IT)) return org.apache.lucene.analysis.it.ItalianAnalyzer.getDefaultStopSet();
            else if (language.equals(LANG_DE)) return org.apache.lucene.analysis.de.GermanAnalyzer.getDefaultStopSet();
            else if (language.equals(LANG_NO)) return org.apache.lucene.analysis.no.NorwegianAnalyzer.getDefaultStopSet();
        }
        return org.apache.lucene.analysis.standard.StandardAnalyzer.STOP_WORDS_SET;
    }
    
    @Override
    public int getPositionIncrementGap(String fieldName) {
      return 10;
    }

    /**
     * Language of the analyser
     * @return Two-letter code of the language supported by the analyser
     */
    public String getLanguage() {
        return language;
    }
    
    
}
