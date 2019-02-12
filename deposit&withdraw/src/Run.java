import Client.ParseXmlFile;
import Client.Terminal;
import Server.ParseTheJson;

import java.util.ArrayList;

public class Run extends Thread {
    public static void main(String[] args) throws InterruptedException {
        ParseTheJson parseTheJson = new ParseTheJson();
        ParseXmlFile parseXmlFile = new ParseXmlFile();
        parseXmlFile.ParseTheXmlFile();
        parseTheJson.ParseTheJson();
      Thread thread=new Thread(parseTheJson);
      Thread thread1=new Thread(parseXmlFile);
        thread.start();
        thread1.start();
        thread.join();
        thread1.join();
        Thread.sleep(2000);







    }
}
