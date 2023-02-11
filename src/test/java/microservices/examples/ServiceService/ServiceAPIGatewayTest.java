package microservices.examples.ServiceService;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;

import lombok.extern.slf4j.Slf4j;
import microservices.examples.Application;
import microservices.examples.common.Code;
import microservices.examples.common.CodeService;
import microservices.examples.customer.Customer;
import microservices.examples.customer.CustomerService;
import microservices.examples.department.Department;
import microservices.examples.department.DepartmentService;
import microservices.examples.service.ServiceRequestService;
import microservices.examples.user.User;
import microservices.examples.user.UserService;

@SpringBootTest(classes=Application.class)
@AutoConfigureWebMvc
@Slf4j
class ServiceAPIGatewayTest {

	@Autowired
	CodeService codeService;
	
	@Autowired
	UserService userService;
	
	@Autowired
	CustomerService customerService;
	
	@Autowired
	DepartmentService departmentService;

	@Autowired
	ServiceRequestService serviceRequestService;

	@Test
	void testGetStatus() {
		ServiceRequestService service = new ServiceRequestService();
		String statusName = service.getStatusName("COMPLETED");
		assertEquals("완료", statusName);
	}
	
	@Test
	void testGetSRType() {
		ServiceRequestService service = new ServiceRequestService();
		String statusName = service.getTypeName("SIMPLE_QUESTION");
		assertEquals("단순문의", statusName);
	}
	
	@Test
	void testGetUserName() {
		ServiceRequestService service = new ServiceRequestService();
		String userName = service.getUserName("9bd036b4-2bfe-455b-a932-d37258dd982a");
		assertEquals("강 서연", userName);
	}
	
	@Test
	void testGetCustomerName() {
		ServiceRequestService service = new ServiceRequestService();
		String userName = service.getCustomerName("ec27f81c-0b59-4b3c-8660-85bbc5e4c0a0");
		assertEquals("송 하은", userName);
	}
	
	@Test
	void testGetDepartmentName() {
		ServiceRequestService service = new ServiceRequestService();
		String userName = service.getDepartmentName("c8fde0ba-e8de-42b8-978e-861960d6cc68");
		assertEquals("콜센터", userName);
	}
	
	
	@Test 
	void 내부_코드조회() {
		Code find = codeService.find("SR_STATUS", "COMPLETED");
		assertEquals("완료", find.getValue());

		find = codeService.find("SR_TYPE", "SIMPLE_QUESTION");
		assertEquals("단순문의", find.getValue());
	}
	
	@Test
	void 내부_사용자조회() {
		User user = userService.findById("9bd036b4-2bfe-455b-a932-d37258dd982a");
		log.error("{}", user);
		assertEquals("강 서연", user.getName());

	}
	
	@Test
	void 내부_고객조회() {
		Customer customer = customerService.findById("ec27f81c-0b59-4b3c-8660-85bbc5e4c0a0");
		log.error("{}", customer);
		assertEquals("송 하은", customer.getName());
	}
	
	@Test
	void 내부_부서조회() {
		Department department = departmentService.findById("c8fde0ba-e8de-42b8-978e-861960d6cc68");
		log.error("{}", department);
		assertEquals("콜센터", department.getName());
	}

	@Test
	void 배치_부서조회() {
		Set<String> ids = new HashSet<>();
		ids.add("b307782b-c044-4ded-af8c-bfdb00e9b55c");
		ids.add("598abbb8-6067-410e-8f7a-0e6218de7b1b");
		Map<String, String> departmentNames = serviceRequestService.getDepartmentNames(ids);
		log.error("{}", departmentNames);
	}
	
	@Test
	void 배치_고객조회() {
		Set<String> ids = new HashSet<>();
		ids.add("0602bf8c-0d00-4ada-89b2-76a4116dcbbb");
		ids.add("3c77e973-0493-4937-bc23-c20ac0a3668f");
		Map<String, String> customerNames = serviceRequestService.getCustomerNames(ids);
		log.error("{}", customerNames);
	}

	@Test
	void 배치_유저조회() {
		Set<String> ids = new HashSet<>();
		ids.add("9bd036b4-2bfe-455b-a932-d37258dd982a");
		ids.add("d6822fc1-42eb-459c-9f06-2da35d9ea2a0");
		ids.add("9134f396-9fca-43a2-957f-390dcbe47de1");
		
		Map<String, String> userNames = serviceRequestService.getUserNames(ids);
		log.error("{}", userNames);
	}
	
	@Test
	void 배치_코드조회() {
		Map<String, Map<String, String>> codeNames = serviceRequestService.getCodeNamesByTypes("SR_STATUS", "SR_TYPE");
		log.error("{}", codeNames);
	}
	
	
}
