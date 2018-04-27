

package com.lamfire.jmongo.mapping.lazy;


import com.lamfire.jmongo.Datastore;

import java.io.Serializable;



@Deprecated
public interface DatastoreProvider extends Serializable {

    Datastore get();


    void register(Datastore ds);
}
