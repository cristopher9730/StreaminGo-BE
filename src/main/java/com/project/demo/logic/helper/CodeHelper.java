package com.project.demo.logic.helper;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.UUID;

public class CodeHelper {
    public static String generateResetCode(int length) {
        String uuid = UUID.randomUUID().toString();
        String randomAlphanumeric = RandomStringUtils.randomAlphanumeric(length);
        return uuid + "-" + randomAlphanumeric;
    }
}
