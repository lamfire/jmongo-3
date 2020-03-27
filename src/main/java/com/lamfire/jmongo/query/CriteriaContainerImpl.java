package com.lamfire.jmongo.query;


import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class CriteriaContainerImpl extends AbstractCriteria implements CriteriaContainer {
    private CriteriaJoin joinMethod;
    private List<Criteria> children;

    private Query<?> query;

    protected CriteriaContainerImpl(final Query<?> query, final CriteriaJoin joinMethod) {
        this(joinMethod);
        this.query = query;
    }

    protected CriteriaContainerImpl(final CriteriaJoin joinMethod) {
        this.joinMethod = joinMethod;
        children = new ArrayList<Criteria>();
    }

    @Override
    public void add(final Criteria... criteria) {
        for (final Criteria c : criteria) {
            c.attach(this);
            children.add(c);
        }
    }

    @Override
    public CriteriaContainer and(final Criteria... criteria) {
        return collect(CriteriaJoin.AND, criteria);
    }

    @Override
    public FieldEnd<? extends CriteriaContainer> criteria(final String name) {
        return new FieldEndImpl<CriteriaContainerImpl>(query, name, this);
    }

    @Override
    public CriteriaContainer or(final Criteria... criteria) {
        return collect(CriteriaJoin.OR, criteria);
    }

    @Override
    public void remove(final Criteria criteria) {
        children.remove(criteria);
    }

    @Override
    public void addTo(final DBObject obj) {
        if (joinMethod == CriteriaJoin.AND) {
            final Set<String> fields = new HashSet<String>();
            int nonNullFieldNames = 0;
            for (final Criteria child : children) {
                if (null != child.getFieldName()) {
                    fields.add(child.getFieldName());
                    nonNullFieldNames++;
                }
            }
            if (fields.size() < nonNullFieldNames) {
                //use $and
                final BasicDBList and = new BasicDBList();

                for (final Criteria child : children) {
                    final BasicDBObject container = new BasicDBObject();
                    child.addTo(container);
                    and.add(container);
                }

                obj.put("$and", and);
            } else {
                //no dup field names, don't use $and
                for (final Criteria child : children) {
                    child.addTo(obj);
                }
            }
        } else if (joinMethod == CriteriaJoin.OR) {
            final BasicDBList or = new BasicDBList();

            for (final Criteria child : children) {
                final BasicDBObject container = new BasicDBObject();
                child.addTo(container);
                or.add(container);
            }

            obj.put("$or", or);
        }
    }

    @Override
    public String getFieldName() {
        return joinMethod.toString();
    }


    public List<Criteria> getChildren() {
        return children;
    }



    public void setChildren(final List<Criteria> children) {
        this.children = children;
    }


    public CriteriaJoin getJoinMethod() {
        return joinMethod;
    }



    public void setJoinMethod(final CriteriaJoin joinMethod) {
        this.joinMethod = joinMethod;
    }


    public Query<?> getQuery() {
        return query;
    }


    public void setQuery(final Query<?> query) {
        this.query = query;
    }

    private CriteriaContainer collect(final CriteriaJoin cj, final Criteria... criteria) {
        final CriteriaContainerImpl parent = new CriteriaContainerImpl(query, cj);

        for (final Criteria c : criteria) {
            parent.add(c);
        }

        add(parent);

        return parent;
    }
}
