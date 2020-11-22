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
public class SpendServiceMockInquriyTest {
	
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
	
	@Test
	public void 조회유효기간_경과_테스트() throws Exception {
		//given
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add("X-USER-ID", "11");
		httpHeaders.add("X-ROOM-ID", "R02");
		
		TokenRequest request = TokenRequest.builder()
				.token("BBB")
				.build();
		
		SpendInfo mockSpendInfo = SpendInfo.builder()
				.token("BBB")
				.spendUserId(11)
				.roomId("R02")
				.spendTime(LocalDateTime.now().minusDays(8))
				.totalMoney(6000)
				.build();
		
		BDDMockito.given(spendInfoDao.findByToken("BBB")).willReturn(mockSpendInfo);
		
		//when
		final ResultActions actions = mvc.perform(post("/inquiry")
			.headers(httpHeaders)
			.content(objectMapper.writeValueAsString(request))
			.contentType(MediaType.APPLICATION_JSON_UTF8))
			.andDo(print());
		
		//then
		actions
			.andExpect(status().is4xxClientError())
			.andExpect(jsonPath("code").value("I003"))
			.andExpect(jsonPath("message").value("조회 기간을 경과하였습니다."));
	}
	
	@Test
	public void 조회API테스트() throws Exception {
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
				.totalMoney(6000)
				.build();
		
		BDDMockito.given(spendInfoDao.findByToken("AAA")).willReturn(mockSpendInfo);
		
		final ResultActions actions = mvc.perform(post("/inquiry")
			.headers(httpHeaders)
			.content(objectMapper.writeValueAsString(request))
			.contentType(MediaType.APPLICATION_JSON_UTF8))
			.andDo(print());
		
		//then
		actions
			.andExpect(status().isOk())
			.andExpect(jsonPath("spendTime").isString())
			.andExpect(jsonPath("spendMoney").isNumber())
			.andExpect(jsonPath("receivedMoney").isNumber())
			.andExpect(jsonPath("receivedInfos").isArray());
	}
}

