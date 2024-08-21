package com.github.repo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import java.util.UUID;

@SpringBootTest
@AutoConfigureMockMvc
class GitHubApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void testProcessUserRepoNotFound() throws Exception {
		String nonExistUsername = UUID.randomUUID().toString();
        String nonExistRepo = UUID.randomUUID().toString();
		mockMvc.perform(MockMvcRequestBuilders.get("/repositories/{owner}/{repositoryName}", nonExistUsername,nonExistRepo)
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.accept(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(MockMvcResultMatchers.status().isNotFound());
	}
	@Test
	public void testProcessUsernameRepoFound() throws Exception {
		String existUsername = "srikanthpragada";
		String existRepo = "09_MAR_2018_JAVAEE_WEBDEMO";
		mockMvc.perform(MockMvcRequestBuilders.get("/repositories/{owner}/{repositoryName}", existUsername,existRepo)
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.accept(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

}
