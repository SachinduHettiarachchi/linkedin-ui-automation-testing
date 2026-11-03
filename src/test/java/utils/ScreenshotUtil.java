
package utils;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ScreenshotUtil {

    public static void captureScreenshot(
        WebDriver driver, String testName
    ) {

        System.out.println("===== SCREENSHOT METHOD STARTED =====");

        try {

            if (driver == null) {
                System.out.println("ERROR: WebDriver is null.");
                return;
            }

            System.out.println("Browser session: " + driver.getTitle());

            File screenshot =
                ((TakesScreenshot) driver)
                .getScreenshotAs(OutputType.FILE);

            System.out.println("Screenshot captured in memory.");

            Path folder = Path.of(
                System.getProperty("user.dir"),
                "screenshots"
            );

            Files.createDirectories(folder);

            String timestamp = LocalDateTime.now().format(
                DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")
            );

            String fileName = testName + "_" + timestamp + ".png";

            Path destination = folder.resolve(fileName);

            Files.copy(
                screenshot.toPath(),
                destination,
                java.nio.file.StandardCopyOption.REPLACE_EXISTING
            );

            System.out.println("SUCCESS: Screenshot saved!");
            System.out.println("Location: " + destination.toAbsolutePath());

        } catch (Exception e) {

            System.out.println("SCREENSHOT ERROR:");
            e.printStackTrace();
        }

        System.out.println("===== SCREENSHOT METHOD FINISHED =====");
    }
}
