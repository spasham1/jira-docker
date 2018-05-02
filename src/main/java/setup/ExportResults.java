package setup;

import static setup.Reporter.buildID;
import static setup.Reporter.browser;
import static setup.Reporter.platform;
import static setup.Reporter.host;

public class ExportResults {

    static Reporter report = new Reporter();
    static ConfigProperties config = new ConfigProperties();

    public static void main(String[] args) throws Exception {
        config.setParam("buildID", "temp123");
        config.setParam("host", "localhost");
        buildID = config.getParam("buildID");
        browser = config.getParam("browser");
        platform = config.getParam("platform");
        host = config.getParam("host");
        report.postXMLResults("http://10.0.75.1:8086", "junit_report", "junit_xml", "target/surefire-reports/TEST-steps.HomePageTest.xml");
    }

}
