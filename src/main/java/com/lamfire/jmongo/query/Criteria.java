package com.lamfire.jmongo.query;


import com.mongodb.DBObject;



public interface Criteria {

    void addTo(DBObject obj);


    void attach(CriteriaContainer container);


    String getFieldName();
}
