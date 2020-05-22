package com.latam.birthday;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BirthdayApplication {
    public boolean someLibraryMethod() {
        return true;
    }
    
    public static void main(String[] args) {
    	SpringApplication.run(BirthdayApplication.class, args);
    }
}
