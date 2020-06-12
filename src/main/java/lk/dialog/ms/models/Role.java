/**
 * The Role class use to persist/retrieve object relational model.
 *
 * @author  Asiri Samaraweera
 * @version 1.0
 *
 */
package lk.dialog.ms.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lk.dialog.ms.audit.Auditable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "roles")
@EntityListeners(AuditingEntityListener.class)
public class Role extends Auditable<String>{
            @Id
        	@GeneratedValue(strategy = GenerationType.IDENTITY)
        	private Integer id;

        	@Enumerated(EnumType.STRING)
        	@Column(length = 20)
        	private ERole name;

        	public Role() {

        	}

        	public Role(ERole name) {
        		this.name = name;
        	}

        	public Integer getId() {
        		return id;
        	}

        	public void setId(Integer id) {
        		this.id = id;
        	}

        	public ERole getName() {
        		return name;
        	}

        	public void setName(ERole name) {
        		this.name = name;
        	}

}