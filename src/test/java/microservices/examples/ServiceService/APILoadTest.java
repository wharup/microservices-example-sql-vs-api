package microservices.examples.ServiceService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;

import microservices.examples.Application;

@SpringBootTest (classes = Application.class)
@AutoConfigureMockMvc
class APILoadTest {

	@Autowired
	MockMvc mockMvc;	
	
	@Test
	void test() throws Exception {
		mockMvc.perform(get("/services?type=bysql"))
		.andDo(print())
		.andExpect(status().isOk());
	}

}
