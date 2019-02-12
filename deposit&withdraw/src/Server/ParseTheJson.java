package Server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;


public class ParseTheJson implements Runnable {
    private Socket socket = null;
    private ServerSocket serverSocket = null;
    private long serverPort;
    Deposit depositValues = new Deposit();
    InputStream fromClientStream;
    OutputStream toClientStream;

    public ArrayList<Deposit> ParseTheJson() {
        ArrayList<Deposit> arrayList = new ArrayList<>();

        try {
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(new FileReader("core.json"));
            JSONObject jsonObject = (JSONObject) obj;
            JSONArray deposit = (JSONArray) jsonObject.get("deposit");
            Iterator iterator = deposit.iterator();


            while (iterator.hasNext()) {
                JSONObject deposit1 = (JSONObject) iterator.next();
                depositValues = new Deposit();
                depositValues.setPort((Long) jsonObject.get("port"));
                depositValues.setId((String) deposit1.get("id"));
                depositValues.setCustomer((String) deposit1.get("customer"));
                depositValues.setInitialBalance((String) deposit1.get("initialBalance"));
                depositValues.setUpperBound((String) deposit1.get("upperBound"));
                arrayList.add(depositValues);
            }
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }


        return arrayList;
    }

    @Override
    public void run() {
        ServerConnection();
        try {
            SendingAndReceivingData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ServerConnection() {
        try {
            serverPort = depositValues.getPort();
            serverSocket = new ServerSocket((int) serverPort);
            System.out.println("Server created");
            System.out.println("Waiting for client....");

            socket = serverSocket.accept();
            System.out.println("connected");
        } catch (IOException e) {
            System.out.println("Server Warning :" + e.getMessage());
        }
    }
    public void SendingAndReceivingData() throws  Exception{
        fromClientStream=socket.getInputStream();
        toClientStream=socket.getOutputStream();
        DataInputStream dis = new DataInputStream (fromClientStream);
        DataOutputStream dos=new DataOutputStream(toClientStream);
        String f=new String(String.valueOf(dis.readInt()));
        ArrayList<Deposit>depositArrayList=ParseTheJson();
        for (Deposit deposit:depositArrayList){
        if (f.equals(deposit.getId())){
            dos.writeBoolean(true);
        }else {
            dos.writeBoolean(false);
        }

    }
}}

