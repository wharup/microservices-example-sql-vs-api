package microservices.examples.system.initializer;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.github.javafaker.Faker;

import lombok.extern.slf4j.Slf4j;
import microservices.examples.user.User;
import microservices.examples.user.UserRepository;

@Service
@Slf4j
public class UserDataGenerator {
	
	UserRepository userRepository;

	DepartmentDataGenerator departmentService;
	
	Map<String, List<User>> usersByIndex = new HashMap<>();
    Map<String, Integer> departmentUserCount = new HashMap<>();
    int count = 0;
    
    @Autowired
	public UserDataGenerator(UserRepository userRepository, DepartmentDataGenerator departmentService, CacheManager cacheManager) {
		super();
		this.userRepository = userRepository;
		this.departmentService = departmentService;
	}


	public User getRandomUser() {
		if (count == 0) {
			count = (int)userRepository.count();
		}
		int index = getRandomNumber(0, count);
		Iterable<User> users = userRepository.findAll(PageRequest.of(index, 1));
		Iterator<User> itr = users.iterator();
		return itr.next();
	}


	public User getRandomUserByDepartmentId(String departmentId) {
		Integer userCount = departmentUserCount.get(departmentId);
		if (userCount == null || userCount == 0) {
			userCount = (int)userRepository.countByDepartmentId(departmentId);
			departmentUserCount.put(departmentId, userCount);
		}

		if (usersByIndex.isEmpty()) {
			Iterable<User> users = userRepository.findAll();
			Iterator<User> itr = users.iterator();
			while (itr.hasNext()) {
				User next = itr.next();
				List<User> departmentUsers = usersByIndex.get(next.getDepartmentId());
				if (departmentUsers == null) {
					departmentUsers = new ArrayList<>();
					usersByIndex.put(next.getDepartmentId(), departmentUsers);
				}
				departmentUsers.add(next);
			}
		}
		int index = getRandomNumber(0, userCount);
		return usersByIndex.get(departmentId).get(index);
	}
	
	private int getRandomNumber(int min, int max) {
	    return (int) ((Math.random() * (max - min)) + min);
	}

	public void deleteAll() {
		userRepository.deleteAll();
	}

	public void createTestData(int size) {
		Faker f = new Faker(new Locale("ko"));
		List<User> users = new ArrayList<>();
		for (int i = 0; i < 30; i++) {
			users.add(aUser(f));
		}
		
		for (int i = 0 ; i < size; i++) {
			users.add(aUser(f));
		}
		
		try {
			log.error("{}, {}", users.get(0).getId(), "-");
			Iterable<User> saveAll = userRepository.saveAll(users);
			log.error("{}, {}", users.get(0).getId(), saveAll.iterator().next().getId());
		} catch (Exception e2) {
			System.out.println(e2);
		}
	}
	
	private User aUser(Faker f) {
		User user = new User();
		user.setName(f.name().fullName());
		user.setEmail(f.internet().emailAddress());
		user.setPhoneNumber(f.phoneNumber().cellPhone());
		user.setDepartmentId(departmentService.getRandomDepartment().getId());
		user.setCreated(Instant.now());
		user.setUpdated(Instant.now());
		return user;
	}	


}
