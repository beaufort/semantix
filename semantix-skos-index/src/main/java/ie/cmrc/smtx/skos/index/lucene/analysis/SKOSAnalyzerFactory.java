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

package ie.cmrc.smtx.skos.index.lucene.analysis;

import ie.cmrc.smtx.lucene.analysis.SmartKeywordAnalyzer;
import ie.cmrc.smtx.lucene.analysis.LanguageBasedAnalyzer;
import ie.cmrc.smtx.lucene.analysis.StandardEuropeanAnalyzer;
import ie.cmrc.smtx.skos.index.IndexField;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.apache.lucene.analysis.miscellaneous.PerFieldAnalyzerWrapper;

/**
 * Factory class for creating suitable analysers used by the
 * {@link ie.cmrc.skos.index.lucene.LuceneSKOSIndex}
 * @author Yassine Lassoued
 */
public class SKOSAnalyzerFactory {
    
    
    
    /**
     * @deprecated Use {@linkplain #createSmartSKOSMultilingualAnalyser() } instead
     * @return Lucene {@code PerFieldanalyzerWrapper}
     */
    public static Analyzer createPerFieldMultilingualAnalyser() {
        return createPerFieldMultilingualAnalyser(null);
    }
    
    /**
     * @deprecated Use {@linkplain #createSmartSKOSMultilingualAnalyser(java.util.Collection, org.apache.lucene.util.Version)} instead
     * @param languages
     * @return Lucene {@code PerFieldanalyzerWrapper}
     */
    public static Analyzer createPerFieldMultilingualAnalyser(Collection<String> languages) {
        Collection<String> langs = languages;
        if (langs==null || langs.isEmpty()) langs = LanguageBasedAnalyzer.SUPPORTED_LANGUAGES;
        
        Analyzer defaultAnalyzer = LanguageBasedAnalyzer.defaultAnalyzer();
        Map<String,Analyzer> analyserMap = new HashMap<>();
        for (String lang: langs) {
            if (LanguageBasedAnalyzer.supportsLanguage(lang)) {
                Analyzer langAnalyser = new LanguageBasedAnalyzer(lang);
                analyserMap.put(IndexField.Searchable.IX_PREF_LABEL.field(lang).getQualifiedString(), langAnalyser);
                analyserMap.put(IndexField.Searchable.IX_LABEL.field(lang).getQualifiedString(), langAnalyser);
                analyserMap.put(IndexField.Searchable.DEFINITION.field(lang).getQualifiedString(), langAnalyser);
            }
        }
        PerFieldAnalyzerWrapper analyser = new PerFieldAnalyzerWrapper(defaultAnalyzer, analyserMap);
        
        return analyser;
    }
    
    /**
     * Creates a multilingual analyser that supports all the languages supported
     * by the {@link StandardEuropeanAnalyzer}
     * @return {@code PerFieldAnalyzerWrapper} that associates with each index field
     * a suitable analyzer
     */
    public static Analyzer createSmartSKOSMultilingualAnalyser() {
        return createSmartSKOSMultilingualAnalyser(null);
    }
    
    /**
     * Creates a multilingual analyser that supports the provided languages
     * @param languages Languages to be supported by the created multilingual
     * analyser
     * @return {@code PerFieldAnalyzerWrapper} that associates with each index field
     * a suitable analyzer
     */
    public static Analyzer createSmartSKOSMultilingualAnalyser(Collection<String> languages) {
        Collection<String> langs = languages;
        if (langs==null || langs.isEmpty()) langs = LanguageBasedAnalyzer.SUPPORTED_LANGUAGES;
        
        Analyzer defaultAnalyzer = new StandardEuropeanAnalyzer();
        Analyzer keywordAnalyzer = new KeywordAnalyzer();
        Analyzer smartKeywordAnalyzer = new SmartKeywordAnalyzer();
        Map<String,Analyzer> analyserMap = new HashMap<>();
        
        analyserMap.put(IndexField.Searchable.URI.fieldName(), keywordAnalyzer);
        analyserMap.put(IndexField.Searchable.NAME.fieldName(), keywordAnalyzer);
        for (String lang: langs) {
            analyserMap.put(IndexField.Searchable.PREF_LABEL.field(lang).getQualifiedString(), smartKeywordAnalyzer);
            analyserMap.put(IndexField.Searchable.ALT_LABEL.field(lang).getQualifiedString(), smartKeywordAnalyzer);
            analyserMap.put(IndexField.Searchable.HIDDEN_LABEL.field(lang).getQualifiedString(), smartKeywordAnalyzer);
            analyserMap.put(IndexField.Searchable.LABEL.field(lang).getQualifiedString(), smartKeywordAnalyzer);
        }
        PerFieldAnalyzerWrapper analyser = new PerFieldAnalyzerWrapper(defaultAnalyzer, analyserMap);
        
        return analyser;
    }
    
    
    
}
