package com.latam.domain.birthday;

import java.io.Serializable;

import lombok.Data;

@Data
public class BirthdayResponse implements Serializable{
	
	private static final long serialVersionUID = 3710979771282534850L;
	Integer statusCode;
	String firstName;
	String lastName;
	Integer age;
	Integer daysToYourBirthDay;
	String poemText;
}
