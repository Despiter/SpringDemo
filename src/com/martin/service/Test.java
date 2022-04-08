package com.martin.service;

import com.martin.spring.MartinApplicationContext;

public class Test {
    public static void main(String[] args) {
        MartinApplicationContext martinApplicationContext = new MartinApplicationContext(AppConfig.class);

        UserService userService = (UserService) martinApplicationContext.getBean("userService");

    }
}
