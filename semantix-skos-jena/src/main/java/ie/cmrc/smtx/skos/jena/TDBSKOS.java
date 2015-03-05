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

package ie.cmrc.smtx.skos.jena;

import com.hp.hpl.jena.tdb.TDB;
import com.hp.hpl.jena.tdb.TDBFactory;
import ie.cmrc.smtx.skos.model.SKOS;

/**
 * A persistent SKOSThesaurus that uses a Jena TDB as a backend.
 * @author Yassine Lassoued <y.lassoued@ucc.ie>
 */
public class TDBSKOS extends JenaSKOS implements SKOS {
    
    private final String tdbDir;

    /**
     * Constructs a {@link TDBSKOS} with the provided directory as a backend
     * @param tdbDir Path of the data directory
     */
    public TDBSKOS(String tdbDir) {
        super(TDBFactory.createDataset(tdbDir));
        this.tdbDir = tdbDir;
    }

    /**
     * TDB data directory 
     * @return Path of the TDB data directory
     */
    public String getTdbDir() {
        return tdbDir;
    }

    @Override
    public SKOS sync() {
        TDB.sync(this.dataset);
        return this;
    }
    
    
    
}
