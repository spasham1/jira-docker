package support;

import java.io.File;

import org.apache.commons.configuration.PropertiesConfiguration;

public class ConfigProperties {

    String prop_file = "src/test/resources/environment.properties";

    public void createDefaultProperties() throws Exception {
        File file = new File(prop_file);
        if (!file.exists()) {
            file.createNewFile();
        }
    }

    public String getParam(String key) throws Exception {
        PropertiesConfiguration config = new PropertiesConfiguration(prop_file);
        return (String) config.getProperty(key);
    }

    public void setParam(String key, String value) throws Exception {
        createDefaultProperties();
        if(!(System.getProperty(key)==null)) {
            value=System.getProperty(key);
        }
        PropertiesConfiguration config = new PropertiesConfiguration(prop_file);
        config.setProperty(key, value);
        config.save();
    }
}
