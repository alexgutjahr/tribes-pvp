package models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import play.db.jpa.GenericModel;

/**
 * @author <a href="mailto:alexander.hanschke@gmail.com">Alexander Hanschke</a>
 */
@Entity
public class Job extends GenericModel {
	
	@Id
	@GeneratedValue
	public Long id;
	
	public String name;
	public String description;
	
	public Long blood;
	public Long gold;
	public Long xp;
    public Long level;
	
	@Override
	public String toString() {
		return this.name;
	}

}
