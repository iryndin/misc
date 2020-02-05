/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *	  https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.test.restservice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class DomainControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testExisting() throws Exception {

        this.mockMvc.perform(get("/domains/check?search=existing"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$[0].tld").value("com"))
                .andExpect(jsonPath("$[0].price").value(8.99))
                .andExpect(jsonPath("$[0].available").value(false))
                .andExpect(jsonPath("$[1].tld").value("net"))
                .andExpect(jsonPath("$[1].price").value(9.99))
                .andExpect(jsonPath("$[1].available").value(true))
                .andExpect(jsonPath("$[2].tld").value("club"))
                .andExpect(jsonPath("$[2].price").value(15.99))
                .andExpect(jsonPath("$[2].available").value(true));
    }

    @Test
    public void testNotExisting() throws Exception {

        this.mockMvc.perform(get("/domains/check?search=new"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$[0].tld").value("com"))
                .andExpect(jsonPath("$[0].price").value(8.99))
                .andExpect(jsonPath("$[0].available").value(true))
                .andExpect(jsonPath("$[1].tld").value("net"))
                .andExpect(jsonPath("$[1].price").value(9.99))
                .andExpect(jsonPath("$[1].available").value(true))
                .andExpect(jsonPath("$[2].tld").value("club"))
                .andExpect(jsonPath("$[2].price").value(15.99))
                .andExpect(jsonPath("$[2].available").value(true));
    }
}
