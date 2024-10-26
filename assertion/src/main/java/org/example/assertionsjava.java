package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class assertionsjava {
    static class Data {
        public int age;
        public String name;
        public String gender;
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();

        // Step 2: Load the JSON data
        String jsonData = new String(Files.readAllBytes(Paths.get("C://Users//saurabh//Downloads//java caw//assertion//src//main//java//org//example//test.json//")));
        JSONArray jsonArray = new JSONArray(jsonData);

        // Step 3: Navigate to the web page
        driver.get("https://testpages.herokuapp.com/styled/tag/dynamic-table.html");
        Thread.sleep(4000);
        driver.findElement(By.xpath("/html/body/div/div[3]/details/summary")).click();
        Thread.sleep(3000);

        String jsondaata ="[{\"name\":\"Bob\",\"age\":20,\"gender\":\"male\"}," +
                "{\"name\":\"George\",\"age\":42,\"gender\":\"male\"}," +
                "{\"name\":\"Sara\",\"age\":42,\"gender\":\"female\"}," +
                "{\"name\":\"Conor\",\"age\":40,\"gender\":\"male\"}," +
                "{\"name\":\"Jennifer\",\"age\":42,\"gender\":\"female\"}]";

        WebElement textarea = driver.findElement(By.id("jsondata"));
        textarea.clear(); // Clear any existing text
        textarea.sendKeys(jsondaata); // Enter JSON data

        WebElement targetElement = driver.findElement(By.id("refreshtable")); // Adjust the selector

        // Step 4: Scroll down to the target element using JavaScript
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView(true);", targetElement);
        Thread.sleep(4000);
        // Assume there's a button to submit the JSON data
        WebElement submitButton = driver.findElement(By.id("refreshtable")); // Adjust the selector
        submitButton.click();
        // Replace with your URL

        List<Map<String, String>> tableData = new ArrayList<>();
        List<WebElement> rows = driver.findElements(By.xpath("//table[@id='dynamictable']/tr"));

        for (int i = 1; i < rows.size(); i++) { // Start from index 1 to skip the header row
            List<WebElement> cells = rows.get(i).findElements(By.xpath(".//td")); // Find cells within the current row
            if (cells.size() >= 3) { // Ensure there are enough fields
                Map<String, String> rowMap = new HashMap<>();
                rowMap.put("name", cells.get(0).getText().trim());
                rowMap.put("age", cells.get(1).getText().trim());
                rowMap.put("gender", cells.get(2).getText().trim());
                tableData.add(rowMap);
            }
        }
        // Step 7: Print the data from the JSON file
        System.out.println("JSON Data:");
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            System.out.println("Name: " + jsonObject.getString("name") +
                    ", Age: " + jsonObject.getInt("age") +
                    ", Gender: " + jsonObject.getString("gender"));
        }

        // Step 8: Print the data from the web table
        System.out.println("\nWeb Table Data:");
        for (Map<String, String> value : tableData) {
            System.out.println("Name: " + value.get("name") +
                    ", Age: " + value.get("age") +
                    ", Gender: " + value.get("gender"));
        }

        // Step 9: Compare the data and print match messages
        System.out.println("\nComparison Results:");
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String nameToCompare = jsonObject.getString("name");
            int ageToCompare = jsonObject.getInt("age");
            String genderToCompare = jsonObject.getString("gender");

            boolean foundMatch = false;
            for (Map<String, String> row : tableData) {
                if (row.get("name").equals(nameToCompare) &&
                        Integer.toString(ageToCompare).equals(row.get("age")) &&
                        row.get("gender").equals(genderToCompare)) {
                    foundMatch = true;
                    break;
                }
            }

            if (foundMatch) {
                System.out.println("Match found for: " + nameToCompare);
            } else {
                System.out.println("No match for: " + nameToCompare);
            }
        }

        // Step 10: Close the driver
        driver.quit();
    }
}