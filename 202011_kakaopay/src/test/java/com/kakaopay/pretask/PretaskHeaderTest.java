package com.kakaopay.pretask;

import static org.mockito.ArgumentMatchers.any;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kakaopay.pretask.controller.SpendController;
import com.kakaopay.pretask.dto.SpendInfoRequest;
import com.kakaopay.pretask.dto.TokenResponse;
import com.kakaopay.pretask.service.SpendService;

@RunWith(SpringRunner.class)
@WebMvcTest(SpendController.class)
public class PretaskHeaderTest {
	@Autowired
	private MockMvc mvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@MockBean
	private SpendService spendService;
		
	@Test
	public void 헤더_USERID없는경우_테스트() throws Exception {
		//given
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add("X-USER-ID", "");
		httpHeaders.add("X-ROOM-ID", "");
		
		SpendInfoRequest request = SpendInfoRequest.builder()
				.peopleNum(1)
				.spendMoney(5000L)
				.build();
		
		//when
		final ResultActions actions = mvc.perform(post("/spend")
			.headers(httpHeaders)
			.content(objectMapper.writeValueAsString(request))
			.contentType(MediaType.APPLICATION_JSON_UTF8))
			.andDo(print());
		
		//then
		actions
			.andExpect(status().is4xxClientError())
			.andExpect(jsonPath("code").value("H001"))
			.andExpect(jsonPath("message").value("사용자를 조회할 수 없습니다."));
	}
	
//	@Test
	public void 헤더_ROOMID없는경우_테스트() throws Exception {
		//given
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add("X-USER-ID", "12");
		httpHeaders.add("X-ROOM-ID", "");
		
		SpendInfoRequest request = SpendInfoRequest.builder()
				.peopleNum(1)
				.spendMoney(5000L)
				.build();
		
		//when
		final ResultActions actions = mvc.perform(post("/spend")
			.headers(httpHeaders)
			.content(objectMapper.writeValueAsString(request))
			.contentType(MediaType.APPLICATION_JSON_UTF8))
			.andDo(print());
		
		//then
		actions
			.andExpect(status().is4xxClientError())
			.andExpect(jsonPath("code").value("H002"))
			.andExpect(jsonPath("message").value("대화방을 조회할 수 없습니다."));
	}
	
//	@Test
	public void 헤더있는경우_테스트() throws Exception {
		//given
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add("X-USER-ID", "12");
		httpHeaders.add("X-ROOM-ID", "ABC");
		
		SpendInfoRequest request = SpendInfoRequest.builder()
				.peopleNum(1)
				.spendMoney(5000L)
				.build();
		
		TokenResponse response = TokenResponse.builder()
				.token("ABC")
				.build();
		
		BDDMockito.given(spendService.spendMoney(any(), any())).willReturn(response);
		
		//when
		final ResultActions actions = mvc.perform(post("/spend")
			.headers(httpHeaders)
			.content(objectMapper.writeValueAsString(request))
			.contentType(MediaType.APPLICATION_JSON_UTF8))
			.andDo(print());
		
		//then
		actions
			.andExpect(status().isOk())
			.andExpect(jsonPath("token").value("ABC"));
	}
}
