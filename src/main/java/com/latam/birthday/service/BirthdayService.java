package com.latam.birthday.service;

import java.text.ParseException;

import com.latam.domain.birthday.BirthdayResponse;

public interface BirthdayService {
	
	public BirthdayResponse getPoems(String fullName, String birthDay) throws ParseException;

}
