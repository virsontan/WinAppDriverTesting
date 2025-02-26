package WinAppDriverTest;

import io.appium.java_client.windows.WindowsDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Assert;
import org.testng.annotations.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;


public class NotePadTest {
    private static WindowsDriver NotepadSession = null;

    @BeforeMethod
    public void setUp(){
        try{
            DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
            desiredCapabilities.setCapability("app", "C:\\Windows\\System32\\notepad.exe");
            desiredCapabilities.setCapability("platform", "Windows");
            desiredCapabilities.setCapability("deviceName", "WindowsPC");

            NotepadSession = new WindowsDriver(new URL("http://127.0.0.1:4723/"), desiredCapabilities);

            NotepadSession.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @AfterMethod
    public void cleanUp(){
        if (NotepadSession != null) {
            NotepadSession.close();
            try {
                NotepadSession.findElementByName("Don't Save").click();
                System.out.println("'Don't Save' button has been clicked");
            } catch (Exception e) {
                System.out.println("Notepad closed successfully");
            }
        }
        NotepadSession.quit();
        NotepadSession = null;
    }

    @Test
    public void AboutNotepadTest(){
        NotepadSession.findElement(By.name("Help")).click();
//        if (NotepadSession.findElement(By.xpath("Menu[@ClassName=\"#32768\"][@Name=\"Help\"]")).isDisplayed()) {
//            System.out.println("Help menu dropdown is clicked");
//        }
        //Check if the Menu for Help is displayed
        if (NotepadSession.findElementByClassName("#32768").isDisplayed()) {
            System.out.println("Help menu dropdown is clicked");
        }
        NotepadSession.findElement(By.name("About Notepad")).click();
        //Check if the About Notepad Dialog box displayed
        if(NotepadSession.findElementByClassName("#32770").isDisplayed()) {
            //if it was displayed check the tex it contains()
            if(NotepadSession.findElementByAccessibilityId("13587").getText().contains("The Windows 10 Home Single Language operating system and its user interface are protected by trademark and other pending or existing intellectual property rights in the United States and other countries/regions.11111")) {
                System.out.println("Text in the About Notepad Dialog box is correct");
            }
        }
        NotepadSession.findElement(By.name("OK")).click();
        //Check if the Dialog Box is still open
        boolean isDialogBoxClosed;
        try {
            NotepadSession.findElementByClassName("#32770").isDisplayed();
            isDialogBoxClosed = false;
        } catch (Exception e) {
            isDialogBoxClosed = true;
        }
        if (isDialogBoxClosed) {
            System.out.println("Dialog Box has been closed successfully");
        } else {
            System.out.println("Dialog Box has is still open");
        }

    }

    @Test
    public void sendTextTest(){
        NotepadSession.findElementByName("Text Editor").sendKeys("This is WinAppDriver automation" + "\n" + "This is a new line..");
        NotepadSession.findElementByName("Text Editor").clear();
    }

    @Test
    public void TimeAndDateTest(){
        NotepadSession.findElementByName("Edit").click();
        NotepadSession.findElementByAccessibilityId("26").click();
        String value = NotepadSession.findElementByName("Text Editor").getAttribute("Value.Value");
        System.out.println("Time and Date is: " + value);
        Assert.assertTrue(value.contains("02/26/2025"));
    }
}
