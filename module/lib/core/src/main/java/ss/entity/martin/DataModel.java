package ss.entity.martin;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import java.io.Serializable;

/**
 * DataModel.
 * @author ss
 */
@MappedSuperclass
public abstract class DataModel implements Serializable {
    /** Default UID. */
    private static final long serialVersionUID = 1L;
    /** Primary key. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
