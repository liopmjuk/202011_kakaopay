package com.kakaopay.pretask.service;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kakaopay.pretask.dao.SpendInfoDao;
import com.kakaopay.pretask.dto.TokenRequest;
import com.kakaopay.pretask.entity.SpendInfo;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SpendServiceInquiryTest {
	@Autowired
	private WebApplicationContext context;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	private MockMvc mvc;
	
	@Before
	public void setup() {
		this.mvc = MockMvcBuilders.webAppContextSetup(context).build();
	}
	
	@Test
	public void 뿌리지않은_사용자가_상태조회_테스트() throws Exception {
		//given
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add("X-USER-ID", "31");
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
			.andExpect(status().is4xxClientError())
			.andExpect(jsonPath("errorCode").value("I002"))
			.andExpect(jsonPath("errorMsg").value("조회할 수 없는 사용자입니다."));
	}
	
	@Test
	public void 유효하지않은_토큰조회_테스트() throws Exception {
		//given
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add("X-USER-ID", "10");
		httpHeaders.add("X-ROOM-ID", "R01");
		
		TokenRequest request = TokenRequest.builder()
				.token("CCC")
				.build();
		
		final ResultActions actions = mvc.perform(post("/inquiry")
			.headers(httpHeaders)
			.content(objectMapper.writeValueAsString(request))
			.contentType(MediaType.APPLICATION_JSON_UTF8))
			.andDo(print());
		
		//then
		actions
			.andExpect(status().is4xxClientError())
			.andExpect(jsonPath("errorCode").value("I001"))
			.andExpect(jsonPath("errorMsg").value("유효하지 않은 토큰입니다."));
	}
}
