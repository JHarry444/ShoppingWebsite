package ShoppingWebsite.ShoppingWebsite;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;

public class ShoppingTest {
	
	private ChromeDriver driver = new ChromeDriver();
	
	@BeforeClass
	public static void setupDriver() {
		System.setProperty("webdriver.chrome.driver", "C:\\Users\\Admin\\Documents\\chromedriver.exe");
	}
	
	@Before
	public void setup() {
		driver = new ChromeDriver();
	}
	
	@After
	public void tearDown() {
		driver.close();
	}
	
	@Test
	public void test() {
		driver.get("http://automationpractice.com/index.php");
		driver.findElementByName("search_query").sendKeys("dress");
		driver.findElementByName("submit_search").click();
		assertEquals("Element not found", "Printed Summer Dress", driver.findElement(By.cssSelector("#center_column > ul > li:nth-child(1) > div > div.right-block > h5 > a")).getText());
	}
}
