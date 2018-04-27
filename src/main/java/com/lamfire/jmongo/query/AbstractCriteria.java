package com.lamfire.jmongo.query;


public abstract class AbstractCriteria implements Criteria {
    private CriteriaContainer attachedTo;

    @Override
    public void attach(final CriteriaContainer container) {
        if (attachedTo != null) {
            attachedTo.remove(this);
        }

        attachedTo = container;
    }


    public CriteriaContainer getAttachedTo() {
        return attachedTo;
    }


    public void setAttachedTo(final CriteriaContainer attachedTo) {
        this.attachedTo = attachedTo;
    }
}
