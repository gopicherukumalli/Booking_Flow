package flows;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import com.google.gson.Gson;

import FlyModules.BrowserContants;
import FlyModules.flyaDealModule;
import FlyModules.passengersDetails;
import io.github.bonigarcia.wdm.WebDriverManager;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import pageObjects.BaseClass;
import pageObjects.Database;

public class FLYADEAL {
	static WebDriver driver;
	boolean status;
	private Database PnrDetails;
	String R_monYear;
	
	public static String flyAdealApiUrl;

	@Test 
	public void test() throws Exception {

		if (BrowserContants.ENV.equals("PRD")) {
			RestAssured.baseURI = BrowserContants.PRD_API_URL;   
			System.out.println(BrowserContants.PRD_API_URL);
			System.out.println("F3 Booking");
		} else if (BrowserContants.ENV.equals("STG")) {
			RestAssured.baseURI = BrowserContants.STG_API_URL;
			System.out.println(BrowserContants.STG_API_URL);
		}
		RequestSpecification request = RestAssured.given();
		request.header("Content-Type", "text/json");
		Response response = request.get("/GetBookingFromQueue?airline=f3&ordercolname=departuredate&orderby=asc");
		System.out.println("Response body: " + response.body().asString());
		String s = response.body().asString();
		System.out.println(s);
		int statusCode = response.getStatusCode();
		System.out.println("The status code recieved: " + statusCode);

		Gson g = new Gson();
		Database[] mcArray = g.fromJson(s, Database[].class);
		List<Database> p = Arrays.asList(mcArray);
		for (Database data : p) { 
			
				PnrDetails = data;
				passengersDetails.readPnrId(PnrDetails);

				Date depDate = new SimpleDateFormat("dd-MMM-yyyy").parse(data.DepartureDate);
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
				String strDate = formatter.format(depDate);
				System.out.println("strDate :" + strDate);

				// String flyAdealApiUrl = null;
				if (data.TripType.equals("OneWay")) {

					// flyAdealApiUrl="https://www.flyadeal.com/en/booking/select/?origin1="+data.From+"&destination1="+data.To+"&departure1="+strDate
					// +"&adt1="+data.Adults+"&chd1="+data.Childs+"&inf1="+data.Infants+"&currency=SAR";
					//flyAdealApiUrl="https://www.flyadeal.com/en/search-flight/?origin1="+data.From.toUpperCase()+"&destination1="+data.To.toUpperCase()+"&departure1="+strDate +"&adt1="+data.Adults+"&chd1="+data.Childs+"&inf1="+data.Infants+"&currency=SAR&source=airtrfx?utm_source=wego_meta&utm_medium=landingpage&utm_campaign=promomar&utm_content=herobanner";
					flyAdealApiUrl="https://www.flyadeal.com/en/search-flight/?origin1="+data.From.toUpperCase()+"&destination1="+data.To.toUpperCase()+"&departure1="+strDate +"&adt1="+data.Adults+"&chd1="+data.Childs+"&inf1="+data.Infants+"&currency=SAR";
					//flyAdealApiUrl = "https://www.flyadeal.com/en/booking/select?destination1=" + data.To + "&inf1="+ data.Infants + "&currency=SAR&source=airtrfx&chd1="+data.Childs+"&adt1="+data.Adults+"&origin1=" + data.From + "&promoCode1=OFF20&departure1=" + strDate + "";
				} else if (data.TripType.equals("RoundTrip")) {
					Date arrDate = new SimpleDateFormat("dd-MMM-yyyy").parse(data.ReturnDate);
					SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd");
					String arriveDate = formatter2.format(arrDate);
					System.out.println("arriveDate :" + arriveDate);
					// flyAdealApiUrl="https://www.flyadeal.com/en/booking/select/?origin1="+data.From+"&destination1="+data.To+"&departure1="+strDate+"&adt1="+data.Adults+"&chd1="+data.Childs+"&inf1="+data.Infants+"&origin2="+data.To+"&destination2="+data.From+"&departure2="+arriveDate+"&adt2="+data.Adults+"&chd2="+data.Childs+"&inf2="+data.Infants+"&currency=SAR";
					flyAdealApiUrl = "https://www.flyadeal.com/en/search-flight/?inf2=" + data.Infants+ "&destination1=" + data.To + "&inf1=" + data.Infants + "&destination2=" + data.From+ "&source=airtrfx&departure2=" + arriveDate + "&adt2=" + data.Adults + "&adt1="+ data.Adults + "&departure1=" + strDate + "&origin2=" + data.To + "&currency=SAR&chd1="+ data.Childs + "&chd2=" + data.Childs + "&promoCode1=OFF20&promoCode2=OFF20&origin1="+ data.From + "";

				}

				System.out.println("API URL: " + flyAdealApiUrl);
				System.out.println(PnrDetails.PnrId);
				
				
				int adults = (data.Adults != null && !data.Adults.isEmpty()) ? Integer.parseInt(data.Adults) : 0;
				int children = (data.Childs != null && !data.Childs.isEmpty()) ? Integer.parseInt(data.Childs) : 0;
				int infants = (data.Infants != null && !data.Infants.isEmpty()) ? Integer.parseInt(data.Infants) : 0;

				int totalPassengers = adults + children + infants;
				System.out.println("Total Passengers: " + totalPassengers);
                // Keep as int for calculations
	            double scaleFactor;
	            // Determine the scale factor based on passenger count
	            switch (totalPassengers) {
	            case 9:
	                scaleFactor = 0.10;
	                break;
	            case 8:
	                scaleFactor = 0.10;
	                break;
	            case 7:
	                scaleFactor = 0.10;
	                break;
	            case 6:
	                scaleFactor = 0.10;
	                break;
	            case 5:
	                scaleFactor = 0.10;
	                break;
	            case 4:
	                scaleFactor = 0.25;
	                break;
	            case 3:
	                scaleFactor = 0.3;
	                break;
	            default:
	                scaleFactor = 0.35; // 1–2 pax
	        }
	            if ((data.From.toUpperCase().equals("RUH") && data.To.toUpperCase().equals("JED")) ||
	            	    (data.From.toUpperCase().equals("JED") && data.To.toUpperCase().equals("RUH"))) {
	            	    scaleFactor = 0.10;
	            	}
				
				FirefoxOptions options = new FirefoxOptions();
				options.addPreference("layout.css.devPixelsPerPx", String.valueOf(scaleFactor));
				options.addPreference("permissions.default.image", 2);
				options.addArguments("--headless");
				driver = new FirefoxDriver(options);
				driver.manage().window().maximize();
				driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
				driver.manage().deleteAllCookies();
				driver.get(flyAdealApiUrl);
				new BaseClass(driver);
				flyaDealModule.selectFlightAndFare_Fly(driver, PnrDetails, Float.parseFloat(PnrDetails.Amount),Float.parseFloat(PnrDetails.UserPaidAmount));
				flyaDealModule.enterPaxDetails(driver, PnrDetails);
                flyaDealModule.enterContactDetails(driver, PnrDetails, Float.parseFloat(PnrDetails.Amount),Float.parseFloat(PnrDetails.UserPaidAmount));
                
                
                //----The below two lines are for switching AUB and NEC-------

				flyaDealModule.enterCardDetails_NEC(PnrDetails);

				// flyaDealModule.enterCardDetails_AUBCC(PnrDetails);
				
                System.out.println("PNR ID:" + PnrDetails.PnrId + "  " + PnrDetails.Domain + " PASS ");
				Thread.sleep(5000);
			    flyaDealModule.browser_Quit(driver, PnrDetails);

			
		}

	}

@AfterMethod
public void stop() throws Exception
 {
	 
	 if (driver != null) {
	        driver.quit();
	    }
}

}	
