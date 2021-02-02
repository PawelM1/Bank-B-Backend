package com.pab.bankb.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pab.bankb.dto.AccountDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ClientControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @Transactional
    void shouldCreateNewClientAndCreateNewAccount() throws Exception {
        mockMvc.perform(post("/api/client").contentType(MediaType.APPLICATION_JSON)
                .content("{\"clientId\": 1 ,\"name_and_address\": \"Carl Johnson, Groove Street\", \"email\": \"carl@xd.pl\", \"phone_number\": \"1232314\"}"))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        MvcResult mvcAccountDto = mockMvc.perform(get("/api/account/carl@xd.pl"))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        AccountDto accountDto = objectMapper.readValue(mvcAccountDto.getResponse().getContentAsString(), AccountDto.class);
        assertThat(accountDto).isNotNull();
        assertThat(accountDto.getAccount_number().length()).isEqualTo(26);
        assertThat(accountDto.getAccount_number().substring(2, 10)).isEqualTo("10302172");
        assertThat(accountDto.getBalance()).isEqualTo(BigDecimal.ZERO);
        assertThat(accountDto.getName_and_address()).isEqualTo("Carl Johnson, Groove Street");
        assertThat(accountDto.getEmail()).isEqualTo("carl@xd.pl");
    }
}
