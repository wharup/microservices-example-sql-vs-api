package microservices.examples.common;

import java.time.Instant;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "TB_CODE")
@IdClass(CodeId.class)
@Data
public class Code {
	@Id
	private String codeType;
	@Id
	private String code;
	private String value;
	private String active;
	private Instant created;
	private Instant updated;

}
