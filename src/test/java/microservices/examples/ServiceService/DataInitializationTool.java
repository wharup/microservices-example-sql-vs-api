package microservices.examples.ServiceService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StopWatch;
import org.springframework.util.StopWatch.TaskInfo;

import lombok.extern.slf4j.Slf4j;
import microservices.examples.Application;
import microservices.examples.system.initializer.CodeDataGenerator;
import microservices.examples.system.initializer.CustomerDataGenerator;
import microservices.examples.system.initializer.DepartmentDataGenerator;
import microservices.examples.system.initializer.ServiceRequestDataGenerator;
import microservices.examples.system.initializer.UserDataGenerator;

@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
@Slf4j
class DataInitializationTool {

	@Autowired
	CodeDataGenerator codeGenerator;
	
	@Autowired
	DepartmentDataGenerator departmentGenerator;
	
	@Autowired
	UserDataGenerator userGenerator;
	
	@Autowired
	CustomerDataGenerator customerGenerator;
	
	@Autowired
	ServiceRequestDataGenerator serviceRequestService;
	
	@Test
	public void 기본데이터삭제() throws Exception {
		StopWatch sw = new StopWatch();
		sw.start("delete all data");
		
		serviceRequestService.deleteAll();
		customerGenerator.deleteAll();
		userGenerator.deleteAll();
		departmentGenerator.deleteAll();
		codeGenerator.deleteAll();
		sw.stop();
		log.error("{}", sw);
		for (TaskInfo t : sw.getTaskInfo() ) {
			float insec = (float)t.getTimeMillis() / 1000;
			log.error("{}	{}", t.getTaskName(), insec);
		}
	}
	
	@Test
	public void 기본데이터생성() {
		int userNumber = 10000;
		int customerNumber = 20000;
		int serviceRequestNumber = 1000 * 1000;
//		int userNumber = 1000;
//		int customerNumber = 2000;
//		int serviceRequestNumber = 10000;
		
		log.error("create codes");
		codeGenerator.createBasicData();
		
		log.error("create departments");
		departmentGenerator.createBasicData();
		
				
		for (int i = 0; i < userNumber; i += 1000) {
			log.error("create users {}", i);
			userGenerator.createTestData(1000);
		}	
		
		for (int i = 0; i < customerNumber; i += 1000) {
			log.error("create customers {}", i);
			customerGenerator.createTestData(1000);
		}
		
		for (int i = 0; i < serviceRequestNumber; i+= 10000) {
			log.error("create serviceRequests {}", i);
			serviceRequestService.createTestData(10000);
		}
	}
	
	@Test
	public void 서비스데이터추가() {
		StopWatch sw = new StopWatch();
		sw.start("create serviceRequests");
		serviceRequestService.createTestData(100000);
		serviceRequestService.createTestData(100000);
		serviceRequestService.createTestData(100000);
		serviceRequestService.createTestData(100000);
		serviceRequestService.createTestData(100000);

		serviceRequestService.createTestData(100000);
		serviceRequestService.createTestData(100000);
		serviceRequestService.createTestData(100000);
		serviceRequestService.createTestData(100000);
		serviceRequestService.createTestData(100000);
		sw.stop();
		
		log.error("{}", sw);
		for (TaskInfo t : sw.getTaskInfo() ) {
			float insec = (float)t.getTimeMillis() / 1000;
			log.error("{}	{}", t.getTaskName(), insec);
		}
	}
}
