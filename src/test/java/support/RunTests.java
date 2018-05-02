package support;

import org.junit.runner.RunWith;
import cucumber.api.junit.Cucumber;
import cucumber.api.CucumberOptions;

/**
 * Created by SPasham on 22/01/2018.
 */

@RunWith(Cucumber.class)
@CucumberOptions(
		features = "src/test/resources/features/",
        glue = {"steps"},
		plugin = { "html:target/reports/cucumber-html-report",
				   "json:target/reports/cucumber.json",
				   "junit:target/reports/cucumber-results.xml",
				   "pretty:target/reports/cucumber-pretty.txt"}
		)

public class RunTests {}

