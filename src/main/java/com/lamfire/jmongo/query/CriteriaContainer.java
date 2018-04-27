package com.lamfire.jmongo.query;


public interface CriteriaContainer extends Criteria {

    void add(Criteria... criteria);


    CriteriaContainer and(Criteria... criteria);


    FieldEnd<? extends CriteriaContainer> criteria(String field);



    CriteriaContainer or(Criteria... criteria);


    void remove(Criteria criteria);
}
