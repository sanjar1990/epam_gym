package com.epam.gym.util;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public abstract class ReadFile {
    private static String readFile(String filePath) {

        Resource resource = new ClassPathResource(filePath);

        StringBuilder content = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(resource.getInputStream()))) {

            String line;

            while ((line = reader.readLine()) != null) {
                content.append(line).append(System.lineSeparator());
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to read file: " + filePath, e);
        }


        return content.toString();
    }
    public static String [] getData(String data) {
     return readFile(data).split("\n");
    }

}
