package org.example.account.controller;

import org.example.account.domain.Account;
import org.example.account.type.AccountStatus;
import org.example.account.service.AccountService;
import org.example.account.service.RedisTestService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AccountController.class)
class AccountControllerTest {

    // 생성자 주입이 필요한 빈들 생성
    // 빈으로 등록되는 목이므로 AccountController에 자동 주입됨
    // 때문에 주입 코드를 작성하지 않는다.
    @MockBean
    private AccountService accountService;

    @MockBean
    private RedisTestService redisTestService;

    // @WebMvcTest는 mockMVC를 제공하고 있음
    @Autowired
    private MockMvc mockMvc;

    @Test
    void successGetAccount() throws Exception {
        //given
        given(accountService.getAccount(anyLong()))
                .willReturn(Account.builder()
                        .accountNumber("3456")
                        .accountStatus(AccountStatus.IN_USER)
                        .build());

        //when
        //then
        mockMvc.perform(get("/account/876"))
                .andDo(print())     // request, response 출력
                .andExpect(jsonPath("$.accountNumber").value("3456"))
                .andExpect(jsonPath("$.accountStatus").value("IN_USER"))
                .andExpect(status().isOk());

    }
}