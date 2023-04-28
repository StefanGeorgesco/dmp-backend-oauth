package fr.cnam.stefangeorgesco.dmp.domain.model;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.sql.Timestamp;

@MappedSuperclass
public abstract class BaseEntity {

    @CreationTimestamp
    @Column(updatable = false, name = "created_date")
    private Timestamp createdDate;

    @UpdateTimestamp
    @Column(name = "last_modified_date")
    private Timestamp lastModifiedDate;

    public Timestamp getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }

    public Timestamp getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Timestamp lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BaseEntity)) return false;

        BaseEntity that = (BaseEntity) o;

        if (getCreatedDate() != null ? !getCreatedDate().equals(that.getCreatedDate()) : that.getCreatedDate() != null)
            return false;
        return getLastModifiedDate() != null ? getLastModifiedDate().equals(that.getLastModifiedDate()) : that.getLastModifiedDate() == null;
    }

    @Override
    public int hashCode() {
        int result = getCreatedDate() != null ? getCreatedDate().hashCode() : 0;
        result = 31 * result + (getLastModifiedDate() != null ? getLastModifiedDate().hashCode() : 0);
        return result;
    }
}
