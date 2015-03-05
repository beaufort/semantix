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

package ie.cmrc.smtx.skos.index;

/**
 * Represents A scored item, i.e., a pair (object, score)
 * @author Yassine Lassoued <y.lassoued@ucc.ie>
 * @param <T> Type of object to be associated with a score
 */
public class Scored<T> {
    /**
     * Item
     */
    private T item;
    
    /**
     * Score associated with the item
     */
    private double score;

    /**
     * Constructs an empty {@code Scored} instance
     */
    public Scored() {
        this.item = null;
        this.score = 0;
    }

    /**
     * Constructs a {@code Scored} instance with the provided item and score
     * @param item Item to score
     * @param score Score associated with {@code item}
     */
    public Scored(T item, double score) {
        this.item = item;
        this.score = score;
    }

    /**
     * Item
     * @return Item
     */
    public T getItem() {
        return item;
    }

    /**
     * Score
     * @return Score
     */
    public double getScore() {
        return score;
    }

    /**
     * Sets the item for this {@code Scored} instance
     * @param item Object to set as item
     */
    public void setItem(T item) {
        this.item = item;
    }

    /**
     * Sets the score for this {@code Scored} instance
     * @param score Score to set
     */
    public void setScore(double score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return "("+item + ")^" + score;
    }
    
}
