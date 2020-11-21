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
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
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
@AutoConfigureTestDatabase(replace = Replace.NONE)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SpendServiceReceiveTest {
	
	@Autowired
	private WebApplicationContext context;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@MockBean
	SpendInfoDao spendInfoDao;
	
	private MockMvc mvc;
	
	@Before
	public void setup() {
		this.mvc = MockMvcBuilders.webAppContextSetup(context).build();
	}
	
//	@Test
	public void 뿌린사용자가_요청하는경우_테스트() throws Exception {
		//given
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add("X-USER-ID", "10");
		httpHeaders.add("X-ROOM-ID", "R01");
		
		TokenRequest request = TokenRequest.builder()
				.token("AAA")
				.build();
		
		SpendInfo mockSpendInfo = SpendInfo.builder()
				.token("AAA")
				.spendUserId(10)
				.roomId("R01")
				.spendTime(LocalDateTime.now())
				.totalMoney(50000)
				.build();
		
		BDDMockito.given(spendInfoDao.findByToken("AAA")).willReturn(mockSpendInfo);
		
		//when
		final ResultActions actions = mvc.perform(post("/receive")
			.headers(httpHeaders)
			.content(objectMapper.writeValueAsString(request))
			.contentType(MediaType.APPLICATION_JSON_UTF8))
			.andDo(print());
		
		//then
		actions
			.andExpect(status().is4xxClientError())
			.andExpect(jsonPath("money").isNumber());
	}
	
//	@Test
	public void 다른방에서_요청온경우_테스트() throws Exception {
		//given
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add("X-USER-ID", "12");
		httpHeaders.add("X-ROOM-ID", "ABC");
		
		TokenRequest request = TokenRequest.builder()
				.token("AAA")
				.build();
		
		SpendInfo mockSpendInfo = SpendInfo.builder()
				.token("AAA")
				.spendUserId(10)
				.roomId("R01")
				.spendTime(LocalDateTime.now())
				.totalMoney(50000)
				.build();
		
		BDDMockito.given(spendInfoDao.findByToken("AAA")).willReturn(mockSpendInfo);
		
		//when
		final ResultActions actions = mvc.perform(post("/receive")
			.headers(httpHeaders)
			.content(objectMapper.writeValueAsString(request))
			.contentType(MediaType.APPLICATION_JSON_UTF8))
			.andDo(print());
		
		//then
		actions
			.andExpect(status().is4xxClientError())
			.andExpect(jsonPath("money").isNumber());
	}
	
//	@Test
	public void 받을유효기간_경과_테스트() throws Exception {
		//given
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add("X-USER-ID", "12");
		httpHeaders.add("X-ROOM-ID", "R02");
		
		TokenRequest request = TokenRequest.builder()
				.token("BBB")
				.build();
		
		SpendInfo mockSpendInfo = SpendInfo.builder()
				.token("BBB")
				.spendUserId(11)
				.roomId("R02")
				.spendTime(LocalDateTime.now().minusMinutes(11))
				.totalMoney(50000)
				.build();
		
		BDDMockito.given(spendInfoDao.findByToken("BBB")).willReturn(mockSpendInfo);
		
		//when
		final ResultActions actions = mvc.perform(post("/receive")
			.headers(httpHeaders)
			.content(objectMapper.writeValueAsString(request))
			.contentType(MediaType.APPLICATION_JSON_UTF8))
			.andDo(print());
		
		//then
		actions
			.andExpect(status().is4xxClientError())
			.andExpect(jsonPath("money").isNumber());
	}
	
//	@Test
	public void 받은사람이_다시받는경우_테스트() throws Exception {
		//given
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add("X-USER-ID", "21");
		httpHeaders.add("X-ROOM-ID", "R02");
		
		TokenRequest request = TokenRequest.builder()
				.token("BBB")
				.build();
		
		SpendInfo mockSpendInfo = SpendInfo.builder()
				.token("BBB")
				.spendUserId(11)
				.roomId("R02")
				.spendTime(LocalDateTime.now())
				.totalMoney(50000)
				.build();
		
		BDDMockito.given(spendInfoDao.findByToken("BBB")).willReturn(mockSpendInfo);
		
		//when
		final ResultActions actions = mvc.perform(post("/receive")
			.headers(httpHeaders)
			.content(objectMapper.writeValueAsString(request))
			.contentType(MediaType.APPLICATION_JSON_UTF8))
			.andDo(print());
		
		//then
		actions
			.andExpect(status().is4xxClientError())
			.andExpect(jsonPath("money").isNumber());
	}
	
	@Test
	public void 받기API테스트() throws Exception {
		//given
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add("X-USER-ID", "21");
		httpHeaders.add("X-ROOM-ID", "R01");
		
		TokenRequest request = TokenRequest.builder()
				.token("AAA")
				.build();
		
		SpendInfo mockSpendInfo = SpendInfo.builder()
				.token("AAA")
				.spendUserId(10)
				.roomId("R01")
				.spendTime(LocalDateTime.now())
				.totalMoney(50000)
				.build();
		
		BDDMockito.given(spendInfoDao.findByToken("AAA")).willReturn(mockSpendInfo);
		
		//when
		final ResultActions actions = mvc.perform(post("/receive")
			.headers(httpHeaders)
			.content(objectMapper.writeValueAsString(request))
			.contentType(MediaType.APPLICATION_JSON_UTF8))
			.andDo(print());
		
		//then
		actions
			.andExpect(status().isOk())
			.andExpect(jsonPath("money").isNumber());
	}
}
