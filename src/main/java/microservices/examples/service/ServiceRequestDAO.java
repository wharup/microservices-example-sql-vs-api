package microservices.examples.service;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.data.domain.Pageable;

@Mapper
public interface ServiceRequestDAO {

	@Select("select\n"
			+ "	tsr.id,\n"
			+ "	tsr.title,\n"
			+ "	tsr.customer_id customerId,\n"
			+ "	tc.name customerName,\n"
			+ "	tsr.\"type\",\n"
			+ "	(select value from tb_code tco where tco.code = tsr.\"type\" and tco.code_type='SR_TYPE') typeName,\n"
			+ "	tsr.detail,\n"
			+ "	tsr.status,\n"
			+ "	(select value from tb_code tco where tco.code = tsr.status and tco.code_type='SR_STATUS') statusName,\n"
			+ "	tsr.call_agent_id callAgentId,\n"
			+ "	tuc.\"name\" callAgentName,\n"
			+ "	tsr.voc_assgnee_id vocAssgneeId,\n"
			+ "	tua.\"name\" vocAssgneeName,\n"
			+ "	tsr.voc_assgnee_dept_id vocAssgneeDeptId,\n"
			+ "	td.\"name\" vocAssgneeDeptName,\n"
			+ "	tsr.created,\n"
			+ "	tsr.updated\n"
			+ "from\n"
			+ "	tb_service_request tsr\n"
			+ "left join tb_customer tc on\n"
			+ "	tsr.customer_id = tc.id\n"
			+ "left join tb_dept td on\n"
			+ "	tsr.voc_assgnee_dept_id = td.id\n"
			+ "left join tb_user tua on\n"
			+ "	tsr.voc_assgnee_id = tua.id\n"
			+ "left join tb_user tuc on\n"
			+ "	tsr.call_agent_id = tuc.id\n"
			+ "	order by tsr.updated desc\n"
			+ "	limit #{pageSize} offset #{offset} \n"
			+ ";\n"
			+ "")
	public List<ServiceRequest> selectAllWithJoin(Pageable pageable);

	@Select("select\n"
			+ "	tsr.id,\n"
			+ "	tsr.title,\n"
			+ "	tsr.customer_id customerId,\n"
			+ "	tsr.\"type\",\n"
			+ "	tsr.detail,\n"
			+ "	tsr.status,\n"
			+ "	tsr.call_agent_id callAgentId,\n"
			+ "	tsr.voc_assgnee_id vocAssgneeId,\n"
			+ "	tsr.voc_assgnee_dept_id vocAssgneeDeptId,\n"
			+ "	tsr.created,\n"
			+ "	tsr.updated\n"
			+ "from\n"
			+ "	tb_service_request tsr\n"
			+ "	order by tsr.updated desc\n"
			+ "	limit #{pageSize} offset #{offset} \n"
			+ ";\n"
			+ "")
	public List<ServiceRequest> selectAll(Pageable pageable);

}
