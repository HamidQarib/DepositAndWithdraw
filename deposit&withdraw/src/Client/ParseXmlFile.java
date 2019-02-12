package Client;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;

public class ParseXmlFile implements Runnable {
    private Socket socket;
    private int port;
    private String address;
    Terminal terminal = new Terminal();
    InputStream fromServerStream;
    OutputStream toServerStream;
    DataInputStream reader;
    PrintWriter writer;

    ArrayList<Terminal> terminals = new ArrayList<>();


    public ArrayList<Terminal> ParseTheXmlFile() {
        try {

            File inputFile = new File("terminal.xml");
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = builderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(inputFile);
            Node transactions = document.getElementsByTagName("transactions").item(0);
            NodeList list = transactions.getChildNodes();


            for (int temp = 0; temp < list.getLength(); temp++) {
                Node node = list.item(temp);
                if (node instanceof Element) {
                    terminal = new Terminal();
                    terminal.setId(((Element) node).getAttribute("id"));
                    terminal.setAmount(((Element) node).getAttribute("amount"));
                    terminal.setType(((Element) node).getAttribute("type"));
                    terminal.setDeposit(((Element) node).getAttribute("deposit"));
                    terminals.add(terminal);
                }
            }
            Node server = document.getElementsByTagName("server").item(0);
            terminal.setIp(((Element) server).getAttribute("ip"));
            terminal.setPort(Integer.parseInt(((Element) server).getAttribute("port")));
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        return terminals;
    }

    @Override
    public void run() {
        ClientConnection();
        try {
            SendingAndReceivingData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ClientConnection() {
        port = terminal.getPort();
        address = terminal.getIp();
        try {
            Thread.sleep(30);
            socket = new Socket();
            socket.connect(new InetSocketAddress("", port), 200);
            System.out.println("connect to server");
        } catch (IOException | InterruptedException e) {
            System.out.println("Client Warning :" + e.getMessage());
        }
    }

    public void SendingAndReceivingData() throws Exception {
        fromServerStream = socket.getInputStream();
        toServerStream = socket.getOutputStream();
        reader = new DataInputStream(fromServerStream);
        writer = new PrintWriter(toServerStream, true);
        Calculating calcute = new Calculating();
        DataOutputStream dos = new DataOutputStream(toServerStream);
        DataInputStream dis = new DataInputStream(fromServerStream);
        ArrayList<Terminal> terminalArrayList = ParseTheXmlFile();
        for (Terminal terminal : terminalArrayList) {
            dos.writeInt(Integer.parseInt(terminal.getDeposit()));
            if (dis.readBoolean()) {
                calcute.Calcute();
            }
        }


    }

    }



