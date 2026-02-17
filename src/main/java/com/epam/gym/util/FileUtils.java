package com.epam.gym.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import java.io.BufferedReader;
import java.io.InputStreamReader;
<<<<<<< HEAD:src/main/java/com/epam/gym/util/ReadFile.java
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public  class ReadFile {
=======

// TODO:
//  [Optional]
//  It is more expected if class name is a noun like FileReader or FileUtils etc.
//  Verbs are usually used for method names
public class FileUtils {
>>>>>>> origin/feature/clean-project:src/main/java/com/epam/gym/util/FileUtils.java
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
<<<<<<< HEAD:src/main/java/com/epam/gym/util/ReadFile.java
    public static String [] getLines(String filePath) {

=======

    // TODO:
    //  Method signatures should be self-descriptive. Imagine someone else will use the class you wrote.
    //  Ideally, only by looking at a method signature they should understand what to expect from it.
    //  String[] getData(String data) -- what is 'data' param? What kind of data you get? Why array? etc.
    //  In comparison:
    //  String[] readLines(String filePath)
    //Done
    public static String [] readLines(String filePath) {
>>>>>>> origin/feature/clean-project:src/main/java/com/epam/gym/util/FileUtils.java
     return readFile(filePath).split("\n");
    }

}
