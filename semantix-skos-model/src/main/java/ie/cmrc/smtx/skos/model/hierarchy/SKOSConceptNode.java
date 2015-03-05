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
import ie.cmrc.smtx.base.serialisation.json.JSONisable;
import ie.cmrc.smtx.base.serialisation.rdfxml.RDFXMLisable;
import java.util.List;

/**
 *
 * @author Yassine Lassoued <y.lassoued@ucc.ie>
 */
public interface SKOSConceptNode extends RDFXMLisable, JSONisable {

    /**
     * Adds a child to the list of child nodes
     * @param child SKOSConcepNode to add
     */
    void addChild(SKOSConceptNode child);

    /**
     * Add the provided concept as a child node
     * @param skosConcept {@link SKOSConcept} to add as a child node
     */
    void addChild(SKOSConcept skosConcept);

    /**
     * Returns the child concept node at the provided index
     * @param index Index of the child node to retrieve
     * @return Child concept node of this node at position {@code index}
     * @throws IndexOutOfBoundsException if the {@code index} is out of bounds
     */
    SKOSConceptNode getChild(int index) throws IndexOutOfBoundsException;

    /**
     * Returns the child concept nodes of this node
     * @return {@code List} of child concept nodes of this node. If this concept
     * has no children then an empty list is returned.
     */
    List<SKOSConceptNode> getChildren();

    /**
     * Returns the concept of this node
     * @return {@link SKOSConcept} of this node
     */
    SKOSConcept getConcept();

    /**
     * Returns the number of immediate children of this Node<T>.
     * @return the number of immediate children.
     */
    int getNumberOfChildren();

    /**
     * Indicates whether this concept node has child nodes
     * @return {@code true} if the node has at least on concept node child,
     * {@code false} otherwise
     */
    Boolean hasChildren();

    /**
     * Inserts the provided concept node as a child at the specified position
     * @param index the position to insert at.
     * @param conceptNode Concept node to inset
     * @throws IndexOutOfBoundsException if {@code index} is out of bounds
     */
    void insertChildAt(int index, SKOSConceptNode conceptNode) throws IndexOutOfBoundsException;

    /**
     * Removes the concept node at the specified position from the list of child nodes
     * @param index Index of the child node to delete
     * @throws IndexOutOfBoundsException if {@code index} is out of bounds
     */
    void removeChildAt(int index) throws IndexOutOfBoundsException;

    /**
     * Sets the child nodes for this node
     * @param children {@code List<SKOSConceptNode>} of concept nodes to set as children
     * for this concept node
     */
    void setChildren(List<SKOSConceptNode> children);

    /**
     * Sets the concept of this node to the provided {@link SKOSConcept}
     * @param concept {@link SKOSConcept} to set as a concept for this node
     */
    void setItem(SKOSConcept concept);
    
}
