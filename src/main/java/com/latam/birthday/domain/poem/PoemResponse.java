package com.latam.birthday.domain.poem;

import java.io.Serializable;

import lombok.Data;

@Data
public class PoemResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3729175989138002017L;
	String title;
	String content;
	String url;
	Poet poet;
}
