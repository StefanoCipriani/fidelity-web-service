package com.xantrix.webapp.controllertest;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.junit.runners.MethodSorters;

import com.xantrix.webapp.Application;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Application.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ClientiControllerTest
{
	private MockMvc mockMvc;

	@Autowired
	private WebApplicationContext wac;
	
	@Before
	public void setup()
	{
		this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
	}

	String JsonData = "{\r\n" + 
			"	\"codFidelity\":	\"67109999\",\r\n" + 
			"	\"nome\":	\"Nicola\",\r\n" + 
			"	\"cognome\":	\"La Rocca\",\r\n" + 
			"	\"mail\":	\"test@gmail.com\",\r\n" + 
			"	\"stato\":	\"1\",\r\n" + 
			"	\"cards\": {\r\n" + 
			"		\"codFidelity\":	\"67109999\",\r\n" + 
			"		\"bollini\": 1280,\r\n" + 
			"		\"ultimaspesa\":	\"2018-10-01\",\r\n" + 
			"		\"obsoleto\":	\"No\"\r\n" + 
			"	}\r\n" + 
			"}";
	
	@Test
	public void A_testInserimento() throws Exception
	{
		mockMvc.perform(MockMvcRequestBuilders.post("/clienti/inserisci")
				.contentType(MediaType.APPLICATION_JSON)
				.content(JsonData)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andDo(print());
	}
			 
	
	@Test
	public void B_listClientiByCodFid() throws Exception
	{
		mockMvc.perform(MockMvcRequestBuilders.get("/clienti/cerca/fidelity/67109999")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.codFidelity").exists())
				.andExpect(jsonPath("$.codFidelity").value("67109999"))
				.andExpect(jsonPath("$.nome").exists())
				.andExpect(jsonPath("$.nome").value("Nicola"))
				.andExpect(jsonPath("$.cognome").exists())
				.andExpect(jsonPath("$.cognome").value("La Rocca"))
				.andExpect(jsonPath("$.mail").exists())
				.andExpect(jsonPath("$.mail").value("test@gmail.com"))
				.andExpect(jsonPath("$.stato").exists())
				.andExpect(jsonPath("$.stato").value("1"))


				.andExpect(jsonPath("$.cards.codFidelity").exists())
				.andExpect(jsonPath("$.cards.codFidelity").value("67109999")) 
				.andExpect(jsonPath("$.cards.bollini").exists())
				.andExpect(jsonPath("$.cards.bollini").value("1280")) 
				.andExpect(jsonPath("$.cards.ultimaspesa").exists())
				.andExpect(jsonPath("$.cards.ultimaspesa").value("2018-10-01")) 
				.andExpect(jsonPath("$.cards.obsoleto").exists())
				.andExpect(jsonPath("$.cards.obsoleto").value("No")) 

				.andDo(print());
	}
	
	@Test
	public void B2_ClientiExceptionTest() throws Exception
	{
		mockMvc.perform(MockMvcRequestBuilders.get("/clienti/cerca/fidelity/67109998")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andDo(print());
	}

	String JsonData2 = "{\r\n" + 
			"	\"codFidelity\":	\"67109999\",\r\n" + 
			"	\"nome\":	\"Nicola\",\r\n" + 
			"	\"cognome\":	\"La Rocca\",\r\n" + 
			"	\"mail\":	\"nicola@gmail.com\",\r\n" + 
			"	\"stato\":	\"1\",\r\n" + 
			"	\"cards\": {\r\n" + 
			"		\"codFidelity\":	\"67109999\",\r\n" + 
			"		\"bollini\": 870,\r\n" + 
			"		\"ultimaspesa\":	\"2018-10-10\",\r\n" + 
			"		\"obsoleto\":	\"No\"\r\n" + 
			"	}\r\n" + 
			"}";
	
	@Test
	public void D_testModifica() throws Exception
	{
		mockMvc.perform(MockMvcRequestBuilders.put("/clienti/modifica").contentType(MediaType.APPLICATION_JSON)
				.content(JsonData2)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andDo(print());
	}
	
	private String JsonData3 = "[" + JsonData2 + "]";

	@Test
	public void E_listCliByCogn() throws Exception
	{
		mockMvc.perform(MockMvcRequestBuilders.get("/clienti/cerca/cognome/la rocca")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(1)))
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(content().json(JsonData3)) 
				.andReturn();
	}
	
	@Test
	public void F_delCliente() throws Exception
	{
		mockMvc.perform(MockMvcRequestBuilders.delete("/clienti/elimina/67109999")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andDo(print());
	}
	
}
