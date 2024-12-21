package com.api.socks;

import com.api.socks.repository.SocksRepositoryCrud;
import com.api.socks.service.RepositoryService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import com.api.socks.models.Socks;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class SocksApplicationTests {

	@Autowired
	private RepositoryService service;
	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private SocksRepositoryCrud repositoryCrud;


	@Test
	void getByIdInPathTest() throws Exception{
		Socks s = new Socks(Socks.SocksColors.black, 50, 34);
		mockMvc.perform(post("/api/socks/income").content(s.toString()).contentType(MediaType.APPLICATION_JSON));
		MvcResult result1 = mockMvc.perform(get("/api/socks/1")).andReturn();
		System.out.println(repositoryCrud.findAll());
		Assertions.assertEquals(result1.getResponse().getStatus(), 200);
		Assertions.assertEquals("{\"id\":1,\"color\":\"black\",\"cotton\":50,\"count\":34}", result1.getResponse().getContentAsString());
		Socks d = new Socks(Socks.SocksColors.white, 55, 100);
		mockMvc.perform(post("/api/socks/income").content(d.toString()).contentType(MediaType.APPLICATION_JSON));
		MvcResult result2 = mockMvc.perform(get("/api/socks/2")).andReturn();
		Assertions.assertEquals(result2.getResponse().getStatus(), 200);
		Assertions.assertEquals("{\"id\":2,\"color\":\"white\",\"cotton\":55,\"count\":100}", result2.getResponse().getContentAsString());
	}

	@Test
	void incomeAndGetCountOfAll() throws Exception{
		Socks s = new Socks(Socks.SocksColors.black, 50, 1);
		mockMvc.perform(post("/api/socks/income").content(s.toString()).contentType(MediaType.APPLICATION_JSON));
		MvcResult result =  mockMvc.perform(get("/api/socks")).andReturn();

		Assertions.assertEquals("1", result.getResponse().getContentAsString());
		mockMvc.perform(post("/api/socks/income").content(s.toString()).contentType(MediaType.APPLICATION_JSON));
		result =  mockMvc.perform(get("/api/socks")).andReturn();
		Assertions.assertEquals("2", result.getResponse().getContentAsString());
	}

	@Test
	void incomeAndRemove()throws Exception{
		Socks s = new Socks(Socks.SocksColors.black, 50, 2);
		mockMvc.perform(post("/api/socks/income").content(s.toString()).contentType(MediaType.APPLICATION_JSON));
		mockMvc.perform(post("/api/socks/outcome").content(s.toString()).contentType(MediaType.APPLICATION_JSON));
		MvcResult result =  mockMvc.perform(get("/api/socks")).andReturn();
		Assertions.assertEquals("0", result.getResponse().getContentAsString());
	}

	@Test
	void incomeAndRemoveExceptionNotFound()throws Exception{
		Socks d = new Socks(Socks.SocksColors.black, 50, 5);
		MvcResult result = mockMvc.perform(post("/api/socks/outcome").content(d.toString()).contentType(MediaType.APPLICATION_JSON)).andReturn();
		Assertions.assertEquals(result.getResponse().getStatus(), 404);
		Assertions.assertEquals(result.getResponse().getContentAsString(), "This id not found");
	}

	@Test
	void incomeAndRemoveExceptionLackOfSocks()throws Exception{
		Socks s = new Socks(Socks.SocksColors.black, 50, 2);
		mockMvc.perform(post("/api/socks/income").content(s.toString()).contentType(MediaType.APPLICATION_JSON));
		Socks d = new Socks(Socks.SocksColors.black, 50, 5);
		MvcResult result = mockMvc.perform(post("/api/socks/outcome").content(d.toString()).contentType(MediaType.APPLICATION_JSON)).andReturn();
		Assertions.assertEquals(result.getResponse().getStatus(), 406);
		Assertions.assertEquals(result.getResponse().getContentAsString(), "lack socks");
	}

	@Test
	void getCountWithoutFilterTest()throws Exception{
		Socks s = new Socks(Socks.SocksColors.black, 50, 34);
		mockMvc.perform(post("/api/socks/income").content(s.toString()).contentType(MediaType.APPLICATION_JSON));
		MvcResult result = mockMvc.perform(get("/api/socks")).andReturn();
		Assertions.assertEquals(result.getResponse().getStatus(), 200);
		Assertions.assertEquals("34", result.getResponse().getContentAsString());
	}
	@Test
	void getCountWithFilterTest()throws Exception{
		Socks s = new Socks(Socks.SocksColors.green, 44, 34);
		mockMvc.perform(post("/api/socks/income").content(s.toString()).contentType(MediaType.APPLICATION_JSON));
		MvcResult result = mockMvc.perform(get("/api/socks?cotton=eq:44&color=green")).andReturn();
		Assertions.assertEquals(200, result.getResponse().getStatus());
		Assertions.assertEquals("34", result.getResponse().getContentAsString());
	}
	@Test
	void getCountWithFilterOnlyColorTest()throws Exception{
		Socks s = new Socks(Socks.SocksColors.green, 44, 34);
		mockMvc.perform(post("/api/socks/income").content(s.toString()).contentType(MediaType.APPLICATION_JSON));
		System.out.println(repositoryCrud.findAll());
		MvcResult result = mockMvc.perform(get("/api/socks?color=green")).andReturn();
		Assertions.assertEquals(200, result.getResponse().getStatus());
		Assertions.assertEquals("34", result.getResponse().getContentAsString());
	}

	@Test
	void getCountWithFilterOnlyColorManyRowsInDbTest()throws Exception{
		Socks s = new Socks(Socks.SocksColors.green, 44, 34);
		Socks d = new Socks(Socks.SocksColors.red, 42, 30);
		mockMvc.perform(post("/api/socks/income").content(s.toString()).contentType(MediaType.APPLICATION_JSON));
		mockMvc.perform(post("/api/socks/income").content(d.toString()).contentType(MediaType.APPLICATION_JSON));
		System.out.println(repositoryCrud.findAll());
		MvcResult result = mockMvc.perform(get("/api/socks?color=green")).andReturn();
		Assertions.assertEquals(200, result.getResponse().getStatus());
		Assertions.assertEquals("34", result.getResponse().getContentAsString());
	}

	@Test
	void getCountWithFilterManyColorsInDbTest()throws Exception{
		Socks s = new Socks(Socks.SocksColors.green, 44, 34);
		Socks d = new Socks(Socks.SocksColors.red, 42, 30);
		mockMvc.perform(post("/api/socks/income").content(s.toString()).contentType(MediaType.APPLICATION_JSON));
		mockMvc.perform(post("/api/socks/income").content(d.toString()).contentType(MediaType.APPLICATION_JSON));
		System.out.println(repositoryCrud.findAll());
		MvcResult result = mockMvc.perform(get("/api/socks?color=green,red")).andReturn();
		Assertions.assertEquals(200, result.getResponse().getStatus());
		Assertions.assertEquals("64", result.getResponse().getContentAsString());
	}

	@Test
	void getCountWithFilterWithoutColorsInDbTest()throws Exception{
		Socks s = new Socks(Socks.SocksColors.green, 44, 34);
		Socks d = new Socks(Socks.SocksColors.red, 42, 30);
		mockMvc.perform(post("/api/socks/income").content(s.toString()).contentType(MediaType.APPLICATION_JSON));
		mockMvc.perform(post("/api/socks/income").content(d.toString()).contentType(MediaType.APPLICATION_JSON));
		System.out.println(repositoryCrud.findAll());
		MvcResult result = mockMvc.perform(get("/api/socks?cotton=gt:40")).andReturn();
		Assertions.assertEquals(200, result.getResponse().getStatus());
		Assertions.assertEquals("64", result.getResponse().getContentAsString());
	}

	@Test
	void getCountWithFilterWithoutColorsWithTwoCottonConditionsTest()throws Exception{
		Socks s = new Socks(Socks.SocksColors.green, 44, 34);
		Socks d = new Socks(Socks.SocksColors.red, 42, 30);
		mockMvc.perform(post("/api/socks/income").content(s.toString()).contentType(MediaType.APPLICATION_JSON));
		mockMvc.perform(post("/api/socks/income").content(d.toString()).contentType(MediaType.APPLICATION_JSON));
		System.out.println(repositoryCrud.findAll());
		MvcResult result = mockMvc.perform(get("/api/socks?cotton=gt:40 and lt:50")).andReturn();
		Assertions.assertEquals(200, result.getResponse().getStatus());
		Assertions.assertEquals("64", result.getResponse().getContentAsString());
	}
}
