package microservices.examples.system.initializer;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import microservices.examples.department.Department;
import microservices.examples.department.DepartmentRepository;

@Service
@Slf4j
public class DepartmentDataGenerator {
	DepartmentRepository departmentRepository;

	Map<Integer, Department> departmentsByIndex = new HashMap<>();
	int count = 0;
	
	@Autowired
	public DepartmentDataGenerator(DepartmentRepository departmentRepository) {
		super();
		this.departmentRepository = departmentRepository;
	}

	public Department getRandomDepartment() {
		if (count == 0) {
			count = (int)departmentRepository.count();
			Iterable<Department> departments = departmentRepository.findAll();
			int i = 0;
			Iterator<Department> iterator = departments.iterator();
			while(iterator.hasNext()) {
				departmentsByIndex.put(i++, iterator.next());
			}
		}
		int index = getRandomNumber(0, count);
		return departmentsByIndex.get(index);
	}
	
	private int getRandomNumber(int min, int max) {
	    return (int) ((Math.random() * (max - min)) + min);
	}

	public void deleteAll() {
		departmentRepository.deleteAll();
	}

	public void createBasicData() {
		List<Department> departments = new ArrayList<>();
		departments.add(aDepartment("콜센터"));
		departments.add(aDepartment("개발팀"));
		departments.add(aDepartment("마케팅"));
		departments.add(aDepartment("영업팀"));
		for (int i = 0; i < 500; i++) {
			departments.add(aDepartment(String.format("부%02d", i)));
		}
		
		try {
			log.error("{}, {}", departments.get(0).getId(), "-");
			Iterable<Department> saveAll = departmentRepository.saveAll(departments);
			log.error("{}, {}", departments.get(0).getId(), saveAll.iterator().next().getId());
		} catch (Exception e2) {
			System.out.println(e2);
		}
	}
	
	private Department aDepartment(String name) {
		Department department = new Department();
		department.setName(name);
		department.setCreated(Instant.now());
		department.setUpdated(Instant.now());
		return department;
	}

}
