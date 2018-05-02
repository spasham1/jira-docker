package reporting;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.InetAddress;
import java.util.Map;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.influxdb.*;
import org.influxdb.InfluxDB.LogLevel;
import org.influxdb.dto.*;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/*
    Author: spasham 02/05/18
    NOTE: running this file
    mvn clean install
    mvn exec:java -Dexec.mainClass="reporting.Reporter" -Dexec.args="junit_report junit_xml target/report.xml 010"  -Dexec.cleanupDaemonThreads=false
    Or java -jar target\airbus-1.0-SNAPSHOT-jar-with-dependencies.jar junit_report junit_xml target/report_all_9.xml 010
*/

public class Reporter {

    // Connect to InfluxDB
    private InfluxDB influxDB;
    private Map<String, String> tags;

    private static String influxdb_url;
    private static String database;
    private static String measurement;
    private static String xml_file;
    private static String buildID;
    private static String browser;
    private static String platform;
    private static String host;

    public String getHostName() throws Exception {
        InetAddress localMachine = InetAddress.getLocalHost();
        String hostName = localMachine.getHostName();
        return hostName;
    }

    public static Reporter reporter;

    public void connectToInflux(String url, String user, String pass) throws InterruptedException  {
        this.influxDB = InfluxDBFactory.connect(url, user, pass);
        boolean influxDBstarted = false;
        do {
            Pong response;
            try {
                response = this.influxDB.ping();
                if (!response.getVersion().equalsIgnoreCase("unknown")) {
                    influxDBstarted = true;
                } else {
                    System.out.println("\nCould not connect to InfluxDB");
                }
            } catch (Exception e) {
                System.out.println("\nFailed to connect to InfluxDB: "+url+"\n");
            }
            Thread.sleep(100L);
        } while (!influxDBstarted);
        this.influxDB.setLogLevel(LogLevel.NONE);
    }

    public void connectToInflux() throws Exception  {
        connectToInflux("http://localhost:8086", "root", "root");
    }

    public void createInfluxDB(String dbName) {
        // Creates only if it doesn't already exist
        this.influxDB.createDatabase(dbName);
    }

    public void deleteInfluxDB(String dbName) {
        // Delete database
        influxDB.deleteDatabase(dbName);
        System.out.println("Database '"+dbName+"' deleted");
    }

    public void writeToInfluxDB(String dbName, String measurement, String value) throws Exception {
        BatchPoints bp = defaultPoints(dbName);
        bp.point(constructPoint(measurement, value));
        //addField("value", value);
        influxDB.write(bp);
    }

    private BatchPoints defaultPoints(String dbName) throws Exception {
        createInfluxDB(dbName);
        BatchPoints batchPoints = BatchPoints
                .database(dbName)
                .consistency(InfluxDB.ConsistencyLevel.ALL)
                .retentionPolicy("autogen")
                .tag("Host", getHostName())
                .build();
        return batchPoints;
    }

    private Point constructPoint(String measurement, String value) {
        Point.Builder builder = Point.measurement(measurement)
                .addField("Duration", Double.valueOf(value))
                .addField("Value", value)
                .time(System.currentTimeMillis(), TimeUnit.MILLISECONDS);
        for (Map.Entry<String, String> tag : tags.entrySet()) {
            builder = builder.tag(tag.getKey(), tag.getValue());
        }
        return builder.build();
    }

    public void addTag(String key, String value) {
        if (tags == null) {
            tags = new HashMap<String, String>();
        }
        tags.put(key, value);
    }

    private void addXMLTags(String test, String status, String build, String browser, String platform, String host) throws Exception {
        String testName=null;
        if(test.contains(" ")) {
            testName = test.substring(test.lastIndexOf(" ")).replaceAll(" ", "");
        }
        addTag("TestName", testName);
        addTag("TestStatus", status);
        addTag("Build", build);
        addTag("Browser", browser);
        addTag("Platform", platform);
        addTag("Host", host);
    }

    private void postXMLResultsToInflux(String database, String measurement, String pathToXMLFile) throws Exception {
        File fXmlFile = new File(System.getProperty("user.dir")+"/"+pathToXMLFile);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        System.out.println(System.getProperty("user.dir")+"/"+pathToXMLFile);
        Document doc = dBuilder.parse(fXmlFile);

        doc.getDocumentElement().normalize();

        NodeList nList = doc.getElementsByTagName("testsuite");
        for (int temp = 0; temp < nList.getLength(); temp++) {
            Node nNode = nList.item(temp);
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;
                //Note: XML testcase time=0 denotes fail
                if(eElement.getAttribute("time")=="0") {
                    //failed immeditately
                    addXMLTags(eElement.getAttribute("name"), "FAIL", buildID, browser, platform, host);
                    writeToInfluxDB(database, measurement, eElement.getAttribute("time"));
                    System.out.println(eElement.getNodeName());
                } else {
                    if(!eElement.hasChildNodes()) {
                        addXMLTags(eElement.getAttribute("name"), "PASS", buildID, browser, platform, host);
                        writeToInfluxDB(database, measurement, eElement.getAttribute("time"));
                    } else {
                        //NodeList nChildNodes = eElement.getChildNodes();
                        if(eElement.getLastChild().getPreviousSibling().hasChildNodes()) {
                            addXMLTags(eElement.getAttribute("name"), "FAIL", buildID, browser, platform, host);
                            writeToInfluxDB(database, measurement, eElement.getAttribute("time"));
                        } else {
                            addXMLTags(eElement.getAttribute("name"), "PASS", buildID, browser, platform, host);
                            writeToInfluxDB(database, measurement, eElement.getAttribute("time"));
                        }
                    }
                }
            }
        }

        //Notify
        System.out.println("Test Results posted to InfluxDB/Grafana");
    }

    public void postXMLResults(String influxURL, String database, String measurement, String pathToXML) throws Exception {
        postXMLResults(influxURL, "root","root", database, measurement, pathToXML);
    }

    public void postXMLResults(String influxURL, String user, String pass, String database, String measurement, String pathToXML) throws Exception {
        try {
            connectToInflux(influxURL, user, pass);
            postXMLResultsToInflux(database, measurement, pathToXML);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        reporter = new Reporter();
        if(args.length<8){
            throw new IllegalArgumentException("[influxdb_url, database, measurement, pathToXML, buildID, browser, platform, host] are required !");
        } else {
            influxdb_url=args[0];
            database = args[1];
            measurement = args[2];
            xml_file = args[3];
            buildID = args[4];
            browser = args[5];
            platform = args[6];
            host = args[7];
        }
        try {
            reporter.postXMLResults(influxdb_url, database, measurement, xml_file);
            //reporter.deleteInfluxDB("junit_report");
        } catch (FileNotFoundException e) {
            System.out.println("File not found! Please specify a valid path within the project directory");
        }
    }

}