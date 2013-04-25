package com.qrmarketlist.core;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.TableGenerator;

/**
 * <p>Abstract class that implements a GLOBAL ID policy
 * for all entities that extends this class.</p>
 * 
 * <p>An database sequence "SEQ_GLOBAL" will be created.</p>
 * 
 * <p>{@link AbstractEntity}</p>
 * 
 */
@MappedSuperclass
public abstract class AbstractEntity implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private Long id;
	
	@Id
	@TableGenerator(name = "SEQ_GLOBAL", 
		table = "SEQ_GLOBAL", 
		pkColumnName = "GEN_NAME", 
		pkColumnValue = "GLOBAL_ID", 
		valueColumnName = "GEN_VAL", 
		initialValue = 0, 
		allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.TABLE, generator="SEQ_GLOBAL")
	@Column(name = "ID", nullable = false)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || !(o instanceof AbstractEntity)) return false;

		AbstractEntity abstractEntity = (AbstractEntity) o;

		if (abstractEntity.id == null || this.id == null)
			return false;
		if (!this.id.equals(abstractEntity.id))
			return false;

		return true;
	}

	@Override
	public int hashCode() {
		return (this.id != null ? this.id.hashCode() : super.hashCode());
	}
}

