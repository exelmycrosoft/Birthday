package com.latam.birthday.domain.poem;

import java.io.Serializable;

import lombok.Data;

@Data
public class Poet implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7735820760774155573L;
	String name;
	String url;
}
