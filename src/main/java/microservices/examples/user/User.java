package microservices.examples.user;

import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;

@Entity
@Table(name = "TB_USER")
@Data
public class User {
	@Id
	@Column(name="id")
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	private String id;
	private String name;
	private String phoneNumber;
	private String email;
	private String departmentId;
	private Instant created;
	private Instant updated;
}
