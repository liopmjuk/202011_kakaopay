package com.kakaopay.pretask;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kakaopay.pretask.dto.TokenRequest;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PretaskInquiryTest {
	@Autowired
	private WebApplicationContext context;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	private MockMvc mvc;
	
	@Before
	public void setup() {
		this.mvc = MockMvcBuilders.webAppContextSetup(context).build();
	}
	
//	@Test
	public void 다른사용자가_상태조회테스트() throws Exception {
		//given
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add("X-USER-ID", "12");
		httpHeaders.add("X-ROOM-ID", "ABC");
		
		TokenRequest request = TokenRequest.builder()
				.token("AAA")
				.build();
		
		final ResultActions actions = mvc.perform(post("/inquiry")
			.headers(httpHeaders)
			.content(objectMapper.writeValueAsString(request))
			.contentType(MediaType.APPLICATION_JSON_UTF8))
			.andDo(print());
		
		//then
		actions
			.andExpect(status().is4xxClientError());
	}
	
	//@Test
	public void 다른대화방_상태조회테스트() throws Exception {
		//given
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add("X-USER-ID", "10");
		httpHeaders.add("X-ROOM-ID", "ABC");
		
		TokenRequest request = TokenRequest.builder()
				.token("AAA")
				.build();
		
		final ResultActions actions = mvc.perform(post("/inquiry")
			.headers(httpHeaders)
			.content(objectMapper.writeValueAsString(request))
			.contentType(MediaType.APPLICATION_JSON_UTF8))
			.andDo(print());
		
		//then
		actions
			.andExpect(status().is4xxClientError());
	}
	
	@Test
	public void 정상_테스트() throws Exception {
		//given
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add("X-USER-ID", "10");
		httpHeaders.add("X-ROOM-ID", "R01");
		
		TokenRequest request = TokenRequest.builder()
				.token("AAA")
				.build();
		
		final ResultActions actions = mvc.perform(post("/inquiry")
			.headers(httpHeaders)
			.content(objectMapper.writeValueAsString(request))
			.contentType(MediaType.APPLICATION_JSON_UTF8))
			.andDo(print());
		
		//then
		actions
			.andExpect(status().isOk());
	}
}
