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

package ie.cmrc.smtx.jena.selector;

import ie.cmrc.smtx.jena.selector.filter.StmtFilter;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Selector;
import com.hp.hpl.jena.rdf.model.SimpleSelector;
import com.hp.hpl.jena.rdf.model.Statement;

/**
 * A simple selector {@code com.hp.hpl.jena.rdf.model.SimpleSelector} that uses
 * a statement filter {@link StmtFilter} to filter accepted statements
 * @author Yassine Lassoued <y.lassoued@ucc.ie>
 */
public class FilteredSelector extends SimpleSelector {
    
    /**
     * Statement filter that defines the constraint of this {@code FilteredSelector}
     */
    protected StmtFilter filter;

    /**
     * Creates a {@link FilteredSelector} that applies the provided filter
     * @param filter Filter to apply to the accepted statements 
     */
    public FilteredSelector(StmtFilter filter) {
        this.filter = filter;
    }
    
    

    /**
     * Creates a {@link FilteredSelector} that applies the provided filter to the
     * provided {@code com.hp.hpl.jena.rdf.model.Selector}
     * @param selector Selector to add filter to
     * @param filter Filter to apply to the accepted statements
     */
    public FilteredSelector(Selector selector, StmtFilter filter) {
        super(selector.getSubject(), selector.getPredicate(), selector.getObject());
        this.filter = filter;
    }

    /**
     * Creates a {@link FilteredSelector} that applies the provided filter to the
     * {@code com.hp.hpl.jena.rdf.model.SimpleSelector} whose subject, predicate and object
     * are the provided {@code subject}, {@code predicate}, and {@code object}
     * @param subject Subject of the SimpleSelector
     * @param predicate Predicate of the SimpleSelector
     * @param object Object of the SimpleSelector
     * @param filter Filter to apply to the accepted statements
     */
    public FilteredSelector(Resource subject, Property predicate, RDFNode object, StmtFilter filter) {
        super(subject, predicate, object);
        this.filter = filter;
    }

    /**
     * Creates a {@link FilteredSelector} that applies the provided filter to the
     * {@code com.hp.hpl.jena.rdf.model.SimpleSelector} whose subject, predicate and object
     * are the provided {@code subject}, {@code predicate}, and {@code object}
     * @param subject Subject of the SimpleSelector
     * @param predicate Predicate of the SimpleSelector
     * @param object Object of the SimpleSelector
     * @param filter Filter to apply to the accepted statements
     */
    public FilteredSelector(Resource subject, Property predicate, boolean object, StmtFilter filter) {
        super(subject, predicate, object);
        this.filter = filter;
    }

    /**
     * Creates a {@link FilteredSelector} that applies the provided filter to the
     * {@code com.hp.hpl.jena.rdf.model.SimpleSelector} whose subject, predicate and object
     * are the provided {@code subject}, {@code predicate}, and {@code object}
     * @param subject Subject of the SimpleSelector
     * @param predicate Predicate of the SimpleSelector
     * @param object Object of the SimpleSelector
     * @param filter Filter to apply to the accepted statements
     */
    public FilteredSelector(Resource subject, Property predicate, long object, StmtFilter filter) {
        super(subject, predicate, object);
        this.filter = filter;
    }

    /**
     * Creates a {@link FilteredSelector} that applies the provided filter to the
     * {@code com.hp.hpl.jena.rdf.model.SimpleSelector} whose subject, predicate and object
     * are the provided {@code subject}, {@code predicate}, and {@code object}
     * @param subject Subject of the SimpleSelector
     * @param predicate Predicate of the SimpleSelector
     * @param object Object of the SimpleSelector
     * @param filter Filter to apply to the accepted statements
     */
    public FilteredSelector(Resource subject, Property predicate, char object, StmtFilter filter) {
        super(subject, predicate, object);
        this.filter = filter;
    }

    /**
     * Creates a {@link FilteredSelector} that applies the provided filter to the
     * {@code com.hp.hpl.jena.rdf.model.SimpleSelector} whose subject, predicate and object
     * are the provided {@code subject}, {@code predicate}, and {@code object}
     * @param subject Subject of the SimpleSelector
     * @param predicate Predicate of the SimpleSelector
     * @param object Object of the SimpleSelector
     * @param filter Filter to apply to the accepted statements
     */
    public FilteredSelector(Resource subject, Property predicate, float object, StmtFilter filter) {
        super(subject, predicate, object);
        this.filter = filter;
    }

    /**
     * Creates a {@link FilteredSelector} that applies the provided filter to the
     * {@code com.hp.hpl.jena.rdf.model.SimpleSelector} whose subject, predicate and object
     * are the provided {@code subject}, {@code predicate}, and {@code object}
     * @param subject Subject of the SimpleSelector
     * @param predicate Predicate of the SimpleSelector
     * @param object Object of the SimpleSelector
     * @param filter Filter to apply to the accepted statements
     */
    public FilteredSelector(Resource subject, Property predicate, double object, StmtFilter filter) {
        super(subject, predicate, object);
        this.filter = filter;
    }

    /**
     * Creates a {@link FilteredSelector} that applies the provided filter to the
     * {@code com.hp.hpl.jena.rdf.model.SimpleSelector} whose subject, predicate and object
     * are the provided {@code subject}, {@code predicate}, and {@code object}
     * @param subject Subject of the SimpleSelector
     * @param predicate Predicate of the SimpleSelector
     * @param object Object of the SimpleSelector
     * @param filter Filter to apply to the accepted statements
     */
    public FilteredSelector(Resource subject, Property predicate, String object, StmtFilter filter) {
        super(subject, predicate, object);
        this.filter = filter;
    }

    /**
     * Creates a {@link FilteredSelector} that applies the provided filter to the
     * {@code com.hp.hpl.jena.rdf.model.SimpleSelector} whose subject, predicate and object
     * are the provided {@code subject}, {@code predicate}, and {@code object}
     * @param subject Subject of the SimpleSelector
     * @param predicate Predicate of the SimpleSelector
     * @param object Object of the SimpleSelector
     * @param filter Filter to apply to the accepted statements
     */
    public FilteredSelector(Resource subject, Property predicate, String object, String language, StmtFilter filter) {
        super(subject, predicate, object, language);
        this.filter = filter;
    }

    /**
     * Creates a {@link FilteredSelector} that applies the provided filter to the
     * {@code com.hp.hpl.jena.rdf.model.SimpleSelector} whose subject, predicate and object
     * are the provided {@code subject}, {@code predicate}, and {@code object}
     * @param subject Subject of the SimpleSelector
     * @param predicate Predicate of the SimpleSelector
     * @param object Object of the SimpleSelector
     * @param filter Filter to apply to the accepted statements
     */
    public FilteredSelector(Resource subject, Property predicate, Object object, StmtFilter filter) {
        super(subject, predicate, object);
        this.filter = filter;
    }
    
    /**
     * Checks whether the provided statement satisfies the constraint (filter)
     * of the {@link FilteredSelector}
     * @param statement Statement to be tested
     * @return {@code true} if the statement satisfies the constraint
     */
    @Override
    public boolean selects(Statement statement) {
        if (this.filter != null) {
            return filter.test(statement);
        }
        else return true;
    } 
    
    
}
