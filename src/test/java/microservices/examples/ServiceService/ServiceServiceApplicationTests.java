package microservices.examples.ServiceService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import microservices.examples.Application;

@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
class ServiceServiceApplicationTests {

	@Autowired
	private MockMvc mockMvc;
	
	
	@Test
	void shouldCallAllAPis222() throws Exception {
		mockMvc.perform(get("/customers"))
				.andDo(print())
				.andExpect(status().isOk());
		
		mockMvc.perform(get("/users"))
		.andDo(print())
		.andExpect(status().isOk());
		
		mockMvc.perform(get("/services"))
		.andDo(print())
		.andExpect(status().isOk());
		
		mockMvc.perform(get("/departments"))
		.andDo(print())
		.andExpect(status().isOk());
		
		mockMvc.perform(get("/codes"))
		.andDo(print())
		.andExpect(status().isOk());
		
	}

	@Test
	void shouldCallAllAPis() throws Exception {
		mockMvc.perform(post("/codes"))
		.andDo(print())
		.andExpect(status().isOk());
		
		mockMvc.perform(post("/departments"))
		.andDo(print())
		.andExpect(status().isOk());
		
		mockMvc.perform(post("/users"))
		.andDo(print())
		.andExpect(status().isOk());
		
		mockMvc.perform(post("/customers"))
		.andDo(print())
		.andExpect(status().isOk());

		mockMvc.perform(post("/services"))
		.andDo(print())
		.andExpect(status().isOk());
	}
	
}
