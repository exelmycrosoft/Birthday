package com.latam.birthday.impl;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.latam.birthday.domain.poem.PoemResponse;
import com.latam.birthday.service.BirthdayService;
import com.latam.domain.birthday.BirthdayResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class BirthdayImpl implements BirthdayService {

	// @Autowired
	RestTemplate restTemplate = new RestTemplate();
	String POEMS_URL = "https://www.poemist.com/api/v1/randompoems";
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	LocalDate currentDate = LocalDate.now();		

	@Override
	public BirthdayResponse getPoems(String fullName, String birthDay) throws ParseException {
		BirthdayResponse response = new BirthdayResponse();
		
		response.setStatusCode(0);
		obtainFirstNameAndLastName(response, fullName);
		log.info("firstName: " + response.getFirstName());
		log.info("lastName: " + response.getLastName());
		response.setAge(getAge(birthDay));
		log.info("age: " + response.getAge());
		response.setDaysToYourBirthDay(getDaysToYourBirthDay(birthDay));
		log.info("days to your birthday:" + response.getDaysToYourBirthDay());
		response.setPoemText(obtainPoemContent(response.getDaysToYourBirthDay()));
		log.info("poem: " + response.getPoemText());
		return response;
	}
	
	private BirthdayResponse obtainFirstNameAndLastName(BirthdayResponse response, String fullName) {
		String[] nameParts = fullName.split(" ");
		
		switch (nameParts.length) {
		case 3:
			response.setFirstName(nameParts[0]);
			response.setLastName(nameParts[1]);
			break;
		case 2:
			response.setFirstName(nameParts[0]);
			response.setLastName(nameParts[1]);
			break;
		case 1:
			response.setFirstName(nameParts[0]);
			response.setLastName("");
			break;
		case 0:
			response.setFirstName("");
			response.setLastName("");
			break;

		default:
			response.setFirstName(nameParts[0]);
			response.setLastName(nameParts[2]);
			break;
		}
		
		return response;
	}
	
	private Integer getDaysToYourBirthDay(String birthDay) throws ParseException {
		log.info("currentDate: " + currentDate);
		Integer currentYear = currentDate.getYear();
		
		LocalDate birthDayDate = LocalDate.parse(birthDay.replace("-", "/"), formatter);
		Integer birthDayDay = birthDayDate.getDayOfMonth();
		Integer birthDayMonth = birthDayDate.getMonthValue();
		String newBirthDayString = birthDayDay.toString()
				+ "/"
				+ completeMonth(birthDayMonth) 
				+ "/"
				+ currentYear.toString();
		log.info("newBirthDayString: " + newBirthDayString);
		LocalDate newBirthDayDate = LocalDate.parse(newBirthDayString, formatter);
		
		Long daysLong = ChronoUnit.DAYS.between(currentDate, newBirthDayDate);
		log.info("daysLong:" + daysLong);
		Integer days = daysLong == null ? null : Math.toIntExact(daysLong);
		
		if (currentDate.compareTo(newBirthDayDate) > 0) {
			days = 365 + days;
		}
		return days;
	}
	
	private String completeMonth(Integer monthValue) {

		return monthValue < 10 ? "0"+monthValue.toString() : monthValue.toString();

	}
	
	private Integer getAge(String birthDayString) {
		LocalDate birthDayDate = LocalDate.parse(birthDayString.replace("-", "/"), formatter);
		return Math.toIntExact(ChronoUnit.YEARS.between(birthDayDate, currentDate));
	}
	
	private String obtainPoemContent(int daysToYourBirthDay) {
		if (daysToYourBirthDay == 0) {
			List<PoemResponse> poemList = Arrays.asList(
					restTemplate.getForObject(POEMS_URL, PoemResponse[].class));
			Random random = new Random();
			PoemResponse poem = poemList.get(random.nextInt(poemList.size()));
			return poem.getContent();
		}
		else {
			return "";
		}
	}

}
