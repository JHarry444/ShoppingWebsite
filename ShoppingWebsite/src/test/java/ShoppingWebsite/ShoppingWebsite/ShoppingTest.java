package ShoppingWebsite.ShoppingWebsite;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class ShoppingTest {

	private ChromeDriver driver;
	private static ExtentReports report;
	private static ExtentTest test;

	@BeforeClass
	public static void setupDriver() {
		System.setProperty("webdriver.chrome.driver", "C:\\Users\\Admin\\Documents\\chromedriver.exe");
		report = new ExtentReports("C:\\Users\\Admin\\Documents\\Tests" + "\\shoppingTest.html", true);
	}

	@Before
	public void setup() {
		driver = new ChromeDriver();
	}

	@After
	public void tearDown() {
		driver.close();
		report.flush();
	}

	/*
	 * @Test public void test() {
	 * driver.get("http://automationpractice.com/index.php");
	 * test.log(LogStatus.INFO, "Navigate to site.");
	 * driver.findElementByName("search_query").sendKeys("dress");
	 * driver.findElementByName("submit_search").click(); test.log(LogStatus.INFO,
	 * "Search for dress."); try { assertEquals("Element not found",
	 * "Printed Summer Dress", driver.findElement(By.
	 * cssSelector("#center_column > ul > li:nth-child(1) > div > div.right-block > h5 > a"
	 * )).getText()); test.log(LogStatus.PASS, "Successfully searched for dress.");
	 * } catch (AssertionError e) { test.log(LogStatus.FAIL,
	 * "Failed to find dress."); } }
	 */

	@Test
	public void testAccountCreation() throws InterruptedException {
		Map<String, String> values = new HashMap<>();
		test = report.startTest("Shopping Test", "Ceating an account");
		try (FileInputStream file = new FileInputStream(
				new File("C:\\Users\\Admin\\Documents\\Tests\\shoppingTest.xlsx"));
				XSSFWorkbook workbook = new XSSFWorkbook(file);) {
			XSSFSheet sheet = workbook.getSheetAt(0);
			for (int i = 0; i < 11; i++) {
				try {
					values.put(sheet.getRow(0).getCell(i).getStringCellValue(),
							sheet.getRow(1).getCell(i).getStringCellValue());
				} catch (Exception e) {
					values.put(sheet.getRow(0).getCell(i).getStringCellValue(),
					NumberToTextConverter.toText(sheet.getRow(1).getCell(i).getNumericCellValue()));
				}
			}
		} catch (IllegalStateException | IOException e) {
			e.printStackTrace();
			test.log(LogStatus.FATAL, "DDT OUTTA NOWHERE");
		}
		driver.get("http://automationpractice.com/index.php?controller=authentication&back=my-account");
		driver.findElement(By.id("email_create")).sendKeys(values.get("Email"));
		driver.findElement(By.cssSelector("#SubmitCreate > span")).click();
		test.log(LogStatus.INFO, "Entered email address.");
		Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(Duration.ofMillis(50000)).pollingEvery(Duration.ofMillis(500)).ignoring(NoSuchElementException.class);
		wait.until(new Function<WebDriver, WebElement>() { 
			public WebElement apply(WebDriver driver) {
				return driver.findElement(By.id("id_gender1"));
			}
		});
		driver.findElement(By.id("id_gender1")).click();
		driver.findElement(By.id("customer_firstname")).sendKeys(values.get("First Name"));
		driver.findElement(By.id("customer_lastname")).sendKeys(values.get("Last Name"));
		driver.findElement(By.id("passwd")).sendKeys(values.get("Password"));
		driver.findElement(By.id("address1")).sendKeys(values.get("Address"));
		driver.findElement(By.id("city")).sendKeys(values.get("City"));
		driver.findElement(By.id("postcode")).sendKeys(values.get("Post Code"));
		driver.findElement(By.id("id_country")).click();
		driver.findElement(By.cssSelector("#id_country > option:nth-child(2)")).click();
		driver.findElement(By.id("id_state")).click();
		driver.findElement(By.cssSelector("#id_state > option:nth-child(5)")).click();
		driver.findElement(By.id("phone_mobile")).sendKeys(values.get("Mobile Phone"));
		driver.findElement(By.id("alias")).clear();
		driver.findElement(By.id("alias")).sendKeys(values.get("Address Alias"));
		driver.findElement(By.cssSelector("#submitAccount > span")).click();
		test.log(LogStatus.INFO, "Submitted user data.");
		try {
			assertEquals("Account creation failed.", "MY ACCOUNT", driver.findElement(By.cssSelector("#center_column > h1")).getText());
			test.log(LogStatus.PASS, "Successfully created user.");
		} catch (AssertionError e) {
			test.log(LogStatus.FAIL, "Unsuccessfully created user.");
		}
				
	}
}
