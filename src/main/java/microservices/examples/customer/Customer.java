package microservices.examples.customer;

import java.time.Instant;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;

@Entity
@Data
@Table(name = "TB_CUSTOMER")
public class Customer {
	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	private String id;
	private String name;
	private String birthday;
	private String gender;
	private String address;
	private String phone_number;
	private String type;
	private Instant created;
	private Instant updated;

}
