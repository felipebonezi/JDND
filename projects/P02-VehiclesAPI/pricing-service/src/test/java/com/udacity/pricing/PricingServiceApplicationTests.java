package com.udacity.pricing;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.udacity.pricing.domain.price.Price;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.net.URI;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
public class PricingServiceApplicationTests {

	@Autowired
	private MockMvc mvc;

	@Test
	public void whenRequestGetPrice_thenReturn200() throws Exception {
		Long vehicleId = 1L;
		String jsonContent = this.mvc.perform(
				get(URI.create(String.format("/services/price?vehicleId=%d", vehicleId)))
						.contentType(MediaType.APPLICATION_JSON_UTF8)
						.accept(MediaType.APPLICATION_JSON_UTF8))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andReturn().getResponse().getContentAsString();

		Price price = JSON.parseObject(jsonContent, Price.class);
		assertNotNull(price);
	}

	@Test
	public void whenRequestGetPriceWithoutVehicleId_thenReturn400() throws Exception {
		String errorMessage = this.mvc.perform(
				get(URI.create("/services/price"))
						.contentType(MediaType.APPLICATION_JSON_UTF8)
						.accept(MediaType.APPLICATION_JSON_UTF8))
				.andDo(print())
				.andExpect(status().isBadRequest())
				.andReturn().getResponse().getErrorMessage();

		assertNotNull("Error message must be not null.", errorMessage);
		assertFalse("Eerror message must be not empty.", errorMessage.isEmpty());
	}

}
