package microservices.examples.user;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import microservices.examples.department.DepartmentService;
import microservices.examples.system.CacheProxy;
import microservices.examples.system.NotFoundException;

@Service
@Slf4j
public class UserService {
	
	UserRepository userRepository;

	DepartmentService departmentService;
	
    private CacheProxy<User> userCache;
    
    @Autowired
	public UserService(UserRepository userRepository, DepartmentService departmentService, CacheManager cacheManager) {
		super();
		this.userRepository = userRepository;
		this.departmentService = departmentService;
		this.userCache = new CacheProxy<>(cacheManager, "users");
	}

	public Iterable<User> findAll() {
		return userRepository.findAll();
	}
	
	public User findById(String callAgentId) {
		Optional<User> result = userRepository.findById(callAgentId);
		if (result.isPresent()) {
			return result.get();
		}
		throw new NotFoundException(callAgentId);
	}

	public User findCachedById(String userId) {
		User user = userCache.get(userId);
		if (user != null) {
			return user;
		}
		user = findById(userId);
		if (user == null) {
			log.info("CANNOT FIND user for {}", userId);
		}
		userCache.put(userId, user);
		return user;
	}
	
	public Iterable<User> findByIds(List<String> userIds) {
		return userRepository.findAllById(userIds);
	}


}
