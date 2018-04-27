package com.lamfire.jmongo.query;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;


public class Meta {

    private static final String META = "$meta";


    public enum MetaDataKeyword {
        textScore;


        public String getName() {
            return textScore.name();
        }
    }

    private MetaDataKeyword metaDataKeyword;
    private String field;


    public Meta(final MetaDataKeyword metaDataKeyword) {
        this.metaDataKeyword = metaDataKeyword;
        this.field = metaDataKeyword.getName();
    }


    public Meta(final MetaDataKeyword metaDataKeyword, final String field) {
        this.metaDataKeyword = metaDataKeyword;
        this.field = field;
    }


    public String getField() {
        return field;
    }


    public static Meta textScore() {
        return new Meta(MetaDataKeyword.textScore);
    }


    public static Meta textScore(final String field) {
        return new Meta(MetaDataKeyword.textScore, field);
    }

    DBObject toDatabase() {
        BasicDBObject metaObject = new BasicDBObject(META, metaDataKeyword.getName());
        return new BasicDBObject(field, metaObject);
    }
}
