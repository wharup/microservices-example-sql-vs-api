package microservices.examples.ServiceService;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;

import lombok.extern.slf4j.Slf4j;
import microservices.examples.Application;
import microservices.examples.common.Code;
import microservices.examples.common.CodeRepository;
import microservices.examples.department.Department;
import microservices.examples.department.DepartmentRepository;
import microservices.examples.user.User;
import microservices.examples.user.UserRepository;

@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
@Slf4j
class RepositoryTest {

	@Autowired
	CodeRepository codeRepository;
	
	@Autowired
	UserRepository userRepository;

	@Autowired
	DepartmentRepository departmentRepository;
	
	@Test
	void test() {
		List<String> types = new ArrayList<>();
		types.add("SR_TYPE");
		types.add("SR_STATUS");
		Iterable<Code> codes = codeRepository.findByCodeTypeIn(types);
		log.error("{}", codes);
	}


	@Test
	void 배치_유저부서로조회() {
		Iterable<User> users = userRepository.findAllByDepartmentId("6be1d997-e3ba-40c2-a047-2b9470d9d492", PageRequest.of(3, 1));
		int count = 0;
		for(User user : users) {
			count++;
		}
		log.error("{}:{}", count, users);
	}

	@Test
	void 배치_유저부서로개수세기() {
		long count = userRepository.countByDepartmentId("c8fde0ba-e8de-42b8-978e-861960d6cc68");
		log.error("{}:{}", count);
	}

	@Test
	void 배치_이름으로부서조회() {
		Department department = departmentRepository.findByName("콜센터");
		log.error("{}", department);
	}
	
}
