

package com.lamfire.jmongo.mapping.lazy;


import com.lamfire.jmongo.DataStore;

import java.io.Serializable;



@Deprecated
public interface DatastoreProvider extends Serializable {

    DataStore get();


    void register(DataStore ds);
}
