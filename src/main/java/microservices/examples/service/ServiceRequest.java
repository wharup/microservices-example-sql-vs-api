package microservices.examples.service;

import java.time.Instant;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;

@Entity
@Data
@Table(name = "TB_SERVICE_REQUEST")
public class ServiceRequest {
	
	@Id
//	@GeneratedValue(generator = "uuid")
//	@GenericGenerator(name = "uuid", strategy = "uuid2")
	private String id;
	private String title;
	private String customerId;		//
	@Transient
	private String customerName;	//
	private String type;
	@Transient
	private String typeName;
	private String detail;
	private String status;
	@Transient
	private String statusName;
	private String callAgentId;
	@Transient
	private String callAgentName;
	private String vocAssgneeId;
	@Transient
	private String vocAssgneeName;
	private String vocAssgneeDeptId;
	@Transient
	private String vocAssgneeDeptName;
	private Instant created;
	private Instant updated;
}
