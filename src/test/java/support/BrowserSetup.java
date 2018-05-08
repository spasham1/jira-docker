package support;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHttpEntityEnclosingRequest;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.junit.Assert;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.SessionId;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;

public class BrowserSetup {

    protected WebDriver driver;
	private DesiredCapabilities capabilities = new DesiredCapabilities();
    
    public void openBrowser(String browser, String url) throws Exception {
    	String drivers= "./src/test/resources/drivers/";
		if (browser.equalsIgnoreCase("chrome")) {
    		ChromeOptions chrome_opts = new ChromeOptions();
    		System.setProperty("webdriver.chrome.driver", drivers+"chromedriver.exe");
    		driver = new ChromeDriver(chrome_opts);
		} else if (browser.equalsIgnoreCase("firefox")) {
    		FirefoxOptions firefox_opts = new FirefoxOptions();
    		System.setProperty("webdriver.gecko.driver", drivers+"geckodriver.exe");
    		firefox_opts.setCapability("marionette", true);
			driver = new FirefoxDriver(firefox_opts);
		} else if (browser.equalsIgnoreCase("internet explorer")||browser.equalsIgnoreCase("ie")) {
    		InternetExplorerOptions ie_opts = new InternetExplorerOptions();
    		System.setProperty("webdriver.ie.driver", drivers+"IEDriverServer.exe");
			driver = new InternetExplorerDriver(ie_opts);
		} else {
			Assert.fail("Invalid browser selection, choose from (ie, chrome, firefox)");
		}
		getUrl(url);
    }

//    public WebDriver driver(String node, String platform, String browser, String url) throws Exception {
//        String port = "4444";
//        if (browser.equalsIgnoreCase("chrome")) {
//            ChromeOptions chrome_opts = new ChromeOptions();
//            capabilities.setCapability(ChromeOptions.CAPABILITY, chrome_opts); //remote
//        }
//        if (browser.equalsIgnoreCase("firefox")) {
//            FirefoxOptions firefox_opts = new FirefoxOptions();
//            capabilities.setCapability(FirefoxOptions.FIREFOX_OPTIONS, firefox_opts); //remote
//        }
//        if(platform.equalsIgnoreCase("linux"))
//            capabilities.setPlatform(Platform.LINUX);
//        else if(platform.equalsIgnoreCase("windows"))
//            capabilities.setPlatform(Platform.WINDOWS);
//        else if(platform.equalsIgnoreCase("win10"))
//            capabilities.setPlatform(Platform.WIN10);
//        else if(platform.equalsIgnoreCase("")||platform.equalsIgnoreCase("ANY"))
//            capabilities.setPlatform(Platform.ANY);
//        driver = new RemoteWebDriver(new URL("http://"+node+":"+port+"/wd/hub"), capabilities);
//        return driver;
//    }

	public void openBrowser(String node, String browser, String url) throws Exception {
		openBrowser(node, "", browser, url);
	}

	public void openBrowser(String node, String platform, String browser, String url) throws Exception {
		String port = "4444";
		if (browser.equalsIgnoreCase("chrome")) {
			ChromeOptions chrome_opts = new ChromeOptions();
			capabilities.setCapability(ChromeOptions.CAPABILITY, chrome_opts); //remote
		}
		if (browser.equalsIgnoreCase("firefox")) {
			FirefoxOptions firefox_opts = new FirefoxOptions();
			capabilities.setCapability(FirefoxOptions.FIREFOX_OPTIONS, firefox_opts); //remote
		}
		if(platform.equalsIgnoreCase("linux"))
			capabilities.setPlatform(Platform.LINUX);
		else if(platform.equalsIgnoreCase("windows"))
			capabilities.setPlatform(Platform.WINDOWS);
		else if(platform.equalsIgnoreCase("win10"))
			capabilities.setPlatform(Platform.WIN10);
		else if(platform.equalsIgnoreCase("")||platform.equalsIgnoreCase("ANY"))
			capabilities.setPlatform(Platform.ANY);
		driver = new RemoteWebDriver(new URL("http://"+node+":"+port+"/wd/hub"), capabilities);
		getUrl(url);
	}

	private void getUrl(String url) {
		try {
			driver.get(url);
			driver.manage().window().maximize();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getHostPlatform() throws Exception {
		return System.getProperty("os.name");
		//Capabilities caps = ((RemoteWebDriver) driver).getCapabilities();
		//return String.valueOf(caps.getPlatform());
	}

	public String getHostName() throws Exception {
		InetAddress localMachine = InetAddress.getLocalHost();
		String hostName = localMachine.getHostName();
		return hostName;
	}

	public String getSessionId() {
        return ((RemoteWebDriver) driver).getSessionId().toString();
    }

    public String getGridNode(String hub) throws UnknownHostException
    {
        String hostDetail = null;
        int port = 4444;
        String errorMsg = "Failed to acquire remote webdriver node and port info. Root cause: ";

        try {
            HttpHost host = new HttpHost(hub, port);
            DefaultHttpClient client = new DefaultHttpClient();
            URL sessionURL = new URL("http://" + hub + ":" + port + "/grid/api/testsession?session=" + getSessionId());
            System.out.println("URL is : "+sessionURL);
            BasicHttpEntityEnclosingRequest r = new BasicHttpEntityEnclosingRequest("POST", sessionURL.toExternalForm());
            HttpResponse response = client.execute(host, r);
            //JSONObject object = extractObject(response);
            //URL myURL = new URL(object.getString("proxyId"));
            JsonObject myjsonobject =extractObject(response);
            JsonElement url = myjsonobject.get("proxyId");
            System.out.println(url.getAsString());
            URL myURL = new URL(url.getAsString());
            if ((myURL.getHost() != null) && (myURL.getPort() != -1)) {
                hostDetail = myURL.getHost();
            }

        } catch (Exception e) {
            //logger.log(Level.SEVERE, errorMsg, e);
            throw new RuntimeException(errorMsg, e);
        }
        return hostDetail;
    }

    private JsonObject extractObject(HttpResponse resp) throws IOException {
        BufferedReader rd = new BufferedReader(new InputStreamReader(resp.getEntity().getContent()));
        StringBuffer s = new StringBuffer();
        String line;
        while ((line = rd.readLine()) != null) {
            s.append(line);
        }
        rd.close();
        JsonParser parser = new JsonParser();
        JsonObject objToReturn = (JsonObject)parser.parse(s.toString());
        System.out.println(objToReturn.toString());
        System.out.println(objToReturn.get("proxyId"));
        return objToReturn;
    }
}