package microservices.examples.common;

import java.io.Serializable;

import lombok.Data;

@Data
public class CodeId implements Serializable{
	private static final long serialVersionUID = 1L;
	private String codeType;
	private String code;
}
