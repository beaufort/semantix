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

package ie.cmrc.smtx.skos.model.hierarchy;

import ie.cmrc.smtx.skos.model.SKOSConcept;
import ie.cmrc.smtx.skos.model.SKOSSemanticProperty;
import ie.cmrc.smtx.base.serialisation.ElementSetName;
import ie.cmrc.smtx.base.serialisation.Namespaces;
import java.util.ArrayList;
import java.util.List;
import org.dom4j.Element;
import org.dom4j.QName;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * A hierarchical structure for for a concept where the child nodes correspond to
 * narrower concepts
 * @author Yassine Lassoued
 */
public class DefaultSKOSConceptNode implements SKOSConceptNode {

    /**
     * Concept in this node
     */
    protected SKOSConcept concept;
    
    /**
     * List of child concepts (concepts narrower than {@code concept})
     */
    protected List<SKOSConceptNode> children;

    /**
     * Constructs an empty concept node
     */
    public DefaultSKOSConceptNode() {
        super();
    }

    /**
     * Constructs a simple {@link DefaultSKOSConceptNode} with the provided {@link ie.cmrc.skos.core.SKOSConcept}
     * @param concept SKOS concept to create the node with
     */
    public DefaultSKOSConceptNode(SKOSConcept concept) {
        this();
        this.concept = concept;
    }

    /**
     * Returns the child concept nodes of this node
     * @return {@code List} of child concept nodes of this node
     */
    @Override
    public List<SKOSConceptNode> getChildren() {
        if (this.children == null) {
            return new ArrayList<>(0);
        }
        return this.children;
    }

    /**
     * Sets the child nodes for this node
     * @param children {@code List<SKOSConceptNode>} of concept nodes to set as children
     * for this concept node
     */
    @Override
    public void setChildren(List<SKOSConceptNode> children) {
        this.children = children;
    }

    /**
     * Returns the number of immediate children of this Node
     * @return the number of immediate children.
     */
    @Override
    public int getNumberOfChildren() {
        if (children == null) {
            return 0;
        }
        return children.size();
    }

    /**
     * Returns the child concept node at the provided index
     * @param index Index of the child node to retrieve
     * @return Child concept node of this node at position {@code index}
     * @throws IndexOutOfBoundsException if the {@code index} is out of bounds
     */
    @Override
    public SKOSConceptNode getChild(int index) throws IndexOutOfBoundsException {
        return this.children.get(index);
    }


    /**
     * Adds a child to the list of child nodes
     * @param child SKOSConcepNode to add
     */
    @Override
    public void addChild(SKOSConceptNode child) {
        if (children == null) {
            children = new ArrayList<>();
        }
        children.add(child);
    }

    /**
     * Add the provided concept as a child node
     * @param skosConcept {@link SKOSConcept} to add as a child node
     */
    @Override
    public void addChild(SKOSConcept skosConcept) {
        SKOSConceptNode cn = new DefaultSKOSConceptNode(skosConcept);
        this.addChild(cn);
    }



    /**
     * Inserts the provided concept node as a child at the specified position
     * @param index the position to insert at.
     * @param conceptNode Concept node to inset
     * @throws IndexOutOfBoundsException if {@code index} is out of bounds
     */
    @Override
    public void insertChildAt(int index, SKOSConceptNode conceptNode) throws IndexOutOfBoundsException {
        if (index == getNumberOfChildren()) {
            // this is really an append
            addChild(conceptNode);
        } else {
            children.get(index); //just to throw the exception, and stop here
            children.add(index, conceptNode);
        }
    }

    /**
     * Removes the concept node at the specified position from the list of child nodes 
     * @param index Index of the child node to delete
     * @throws IndexOutOfBoundsException if {@code index} is out of bounds
     */
    @Override
    public void removeChildAt(int index) throws IndexOutOfBoundsException {
        children.remove(index);
    }

    /**
     * Returns the concept of this node
     * @return {@link SKOSConcept} of this node
     */
    @Override
    public SKOSConcept getConcept() {
        return this.concept;
    }

    /**
     * Sets the concept of this node to the provided {@link SKOSConcept}
     * @param concept {@link SKOSConcept} to set as a concept for this node 
     */
    @Override
    public void setItem(SKOSConcept concept) {
        this.concept = concept;
    }

    /**
     * Indicates whether this concept node has child nodes
     * @return {@code true} if the node has at least on concept node child,
     * {@code false} otherwise
     */
    @Override
    public Boolean hasChildren() {
        return (this.children!=null && !this.children.isEmpty());
    }



    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{").append(getConcept().toString()).append(",[");
        int i = 0;
        for (SKOSConceptNode e : getChildren()) {
            if (i > 0) {
                sb.append(",");
            }
            sb.append(e.getConcept().toString());
            i++;
        }
        sb.append("]").append("}");
        return sb.toString();
    }

    /**
     * {@inheritDoc}
     * @param elementSet {@inheritDoc}
     * @param language {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public Element toXMLElement(ElementSetName elementSet, String language) {
        if (this.concept != null) {
            Element elt = this.concept.toXMLElement(elementSet, language);

            if(elt!=null && this.hasChildren()) {
                for (SKOSConceptNode child : this.children) {
                    if (child != null) {
                        Element childNodeElt = child.toXMLElement(elementSet, language);
                        if (childNodeElt != null) {
                            Element narrower = elt.addElement(new QName(SKOSSemanticProperty.narrower.name(), Namespaces.SKOS));
                            narrower.add(childNodeElt);
                        }
                    }
                }
            }

            return elt;
        }
        return null;
    }
    
    /**
     * {@inheritDoc}
     * @param elementSet {@inheritDoc}
     * @param language {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public JSONObject toJSONObject(ElementSetName elementSet, String language) {
        if (concept != null) {
            JSONObject jsonObject = concept.toJSONObject(elementSet, language);

            if(jsonObject!=null && this.hasChildren()) {
                JSONArray jsonChildren = new JSONArray();
                for (SKOSConceptNode child : this.children) {
                    if (child != null) {
                        JSONObject childNodeJSON = child.toJSONObject(elementSet, language);
                        if (childNodeJSON != null) {
                            jsonChildren.add(childNodeJSON);
                        }
                    }
                }
                if (!jsonChildren.isEmpty()) jsonObject.put(SKOSSemanticProperty.narrower.name(), jsonChildren);
            }

            return jsonObject;
        }
        return null;
    }

}
