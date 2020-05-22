package com.latam.birthday.controller;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.latam.birthday.service.BirthdayService;
import com.latam.domain.birthday.BirthdayResponse;

@RestController
@RequestMapping("birthday/v1")
public class BirthdayController {
	
	@Autowired
	BirthdayService birthdayService;
	
	@GetMapping("/poems")
	public ResponseEntity<BirthdayResponse> getBirthdayPoem(
			@RequestParam(value = "fullName") String fullName,
            @RequestParam(value = "birthDay") String birthDay) throws ParseException{
		BirthdayResponse response = birthdayService.getPoems(fullName, birthDay);
		return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
