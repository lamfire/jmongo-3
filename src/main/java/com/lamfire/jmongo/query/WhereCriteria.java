package com.lamfire.jmongo.query;


import com.mongodb.DBObject;
import org.bson.types.CodeWScope;


public class WhereCriteria extends AbstractCriteria {

    private final Object js;


    public WhereCriteria(final String js) {
        this.js = js;
    }


    public WhereCriteria(final CodeWScope js) {
        this.js = js;
    }

    @Override
    public void addTo(final DBObject obj) {
        obj.put(FilterOperator.WHERE.val(), js);
    }

    @Override
    public String getFieldName() {
        return FilterOperator.WHERE.val();
    }

}
