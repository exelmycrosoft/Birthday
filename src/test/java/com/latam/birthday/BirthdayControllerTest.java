package com.latam.birthday;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.text.ParseException;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import com.latam.birthday.controller.BirthdayController;
import com.latam.birthday.service.BirthdayService;
import com.latam.domain.birthday.BirthdayResponse;

@RunWith(MockitoJUnitRunner.class)
@ActiveProfiles("local")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BirthdayControllerTest {
	
	@InjectMocks
	BirthdayController birthdayController = new BirthdayController();
	
	@Mock
	BirthdayService birthdayService;
	
	@Test
	public void testGetBirthdayPoem() throws ParseException {
		when(birthdayService.getPoems(new String(), new String())).thenReturn(new BirthdayResponse());
		
		ResponseEntity<BirthdayResponse> response = 
				birthdayController.getBirthdayPoem(new String(), new String());
		assertEquals(response.getStatusCode(), HttpStatus.OK);
	}

}
