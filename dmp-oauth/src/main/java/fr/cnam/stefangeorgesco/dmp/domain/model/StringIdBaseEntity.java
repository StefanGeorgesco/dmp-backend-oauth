package fr.cnam.stefangeorgesco.dmp.domain.model;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotBlank;

@MappedSuperclass
public abstract class StringIdBaseEntity extends BaseEntity {

    @Id
    @NotBlank(message = "L'identifiant est obligatoire.")
    protected String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StringIdBaseEntity)) return false;
        if (!super.equals(o)) return false;

        StringIdBaseEntity that = (StringIdBaseEntity) o;

        return getId() != null ? getId().equals(that.getId()) : that.getId() == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (getId() != null ? getId().hashCode() : 0);
        return result;
    }
}
