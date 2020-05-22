package com.latam.birthday.impl;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.time.format.DateTimeParseException;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.latam.birthday.domain.poem.PoemResponse;

import lombok.extern.slf4j.Slf4j;

@RunWith(MockitoJUnitRunner.class)
@ActiveProfiles("local")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@Slf4j
public class BirthdayImplTest {
	
	private static final String TEST_MESSAGE = "Test Message Logic";

	public static final String SRC_TEST_POEMIST_RESPONSE = "src/test/resources/poemistResponse.json";

	public static final String POEMIST_URL = "https://www.poemist.com/api/v1/randompoems";
	
	@InjectMocks
	BirthdayImpl birthdayImpl;

	@Mock
	RestTemplate restTemplate;

	@Test
	public void testGetPoemForYourBirthDay() throws ParseException {
		PoemResponse[] responsePoemist = loadJson(SRC_TEST_POEMIST_RESPONSE, PoemResponse[].class);
		log.info("responsePoemis: " + responsePoemist);
		when(restTemplate.getForObject(Mockito.anyString(), Mockito.eq(PoemResponse[].class))).thenReturn(responsePoemist);
		birthdayImpl.getPoems("Exe Latam", "22/05/1987");
		assertNotNull(TEST_MESSAGE, responsePoemist);
	}
	
	@Test(expected=DateTimeParseException.class)
	public void testInvalidDate() throws ParseException {
		PoemResponse[] responsePoemist = null;
		log.info("responsePoemis: " + responsePoemist);
		when(restTemplate.getForObject(Mockito.anyString(), Mockito.eq(PoemResponse[].class))).thenReturn(Mockito.any());
		birthdayImpl.getPoems("Exe Latam", "");
		assertNotNull(TEST_MESSAGE, responsePoemist);
	}

	private <T> T loadJson(final String path, Class<T> clazz) {
		File jFile = new File(path);

		ObjectMapper jObject = new ObjectMapper();
		try {
			return jObject.readValue(jFile, clazz);
		} catch (IOException e) {
			return null;
		}
	}

}
