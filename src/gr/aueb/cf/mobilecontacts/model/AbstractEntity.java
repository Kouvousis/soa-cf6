package gr.aueb.cf.mobilecontacts.model;

public abstract class AbstractEntity implements IdentifiableEntity{
    private long id;

    @Override
    public long getId() {
        return 0;
    }

    public void setId(long id) {
        this.id = id;
    }
}