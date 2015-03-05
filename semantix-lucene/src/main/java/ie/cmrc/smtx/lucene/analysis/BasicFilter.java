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
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

/**
 * Not needed anymore. May be deprecated 
 * @author Yassine Lassoued <y.lassoued@ucc.ie>
 */
public class BasicFilter extends TokenFilter {

    private final CharTermAttribute charTermAttr;

    public BasicFilter(TokenStream input) {
        super(input);
        this.charTermAttr = addAttribute(CharTermAttribute.class);
    }
    
    @Override
    public boolean incrementToken() throws IOException {
        if (!input.incrementToken()) {
            return false;
        }

        int length = charTermAttr.length();
        char[] buffer = charTermAttr.buffer();
        char[] newBuffer = new char[length];
        for (int i = 0; i < length; i++) {
            newBuffer[i] = buffer[length - 1 - i];
        }
        charTermAttr.setEmpty();
        charTermAttr.copyBuffer(newBuffer, 0, newBuffer.length);
        return true;
    }
    
}
