package microservices.examples.ServiceService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;

import lombok.extern.slf4j.Slf4j;
import microservices.examples.Application;
import microservices.examples.system.CacheProxy;
import microservices.examples.user.User;

@SpringBootTest(classes = Application.class)
@Slf4j
public class CacheProxyTest {

	@Autowired
	CacheManager cacheManager;

	@Test
	void simpleLayoutTest() {
		CacheProxy<User> cache = new CacheProxy<>(cacheManager, "users");
		cache.put("1", new User());
		cache.put("2", new User());
		cache.get("1");
		log.error("{}", cache.getStats());
	}
}
