package Client;


import Server.Deposit;
import Server.ParseTheJson;
import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.ArrayList;

public class Calculating {
    int initialBalance;
    int amount;
    int initialAndAmountSam;
    int initialAndAmountSub;
    String customerName;
    ParseTheJson parseTheJson = new ParseTheJson();
    ParseXmlFile parseXmlFile = new ParseXmlFile();

    public void Calcute() {
        final String xmlFilePath = "C:\\Users\\hamid\\Desktop\\response.xml";

        ArrayList<Terminal> terminalArrayList = parseXmlFile.ParseTheXmlFile();
        ArrayList<Deposit> depositArrayList = parseTheJson.ParseTheJson();

        try {
            DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
            Document document = documentBuilder.newDocument();
            Element root = document.createElement("results");
            document.appendChild(root);
            for (int i = 0; i < terminalArrayList.size(); i++) {

                if (terminalArrayList.get(i).getType().equalsIgnoreCase("deposit")) {

                    initialBalance = Integer.parseInt(String.valueOf(depositArrayList.get(i).getInitialBalance()));
                    amount = Integer.parseInt(String.valueOf(terminalArrayList.get(i).getAmount()));
                    initialAndAmountSam = amount += initialBalance;
                    customerName = depositArrayList.get(i).getCustomer();

                    if (initialAndAmountSam > Integer.parseInt(String.valueOf(depositArrayList.get(i).getUpperBound()))) {

                        System.out.println("مشتری محترم آقا/خانم " + depositArrayList.get(i).getCustomer() + " سقف موجودی شما پر شده است");

                    } else {

                        Element result = document.createElement("result");
                        root.appendChild(result);
                        Attr attr = document.createAttribute("newInitialBalance");
                        attr.setValue(String.valueOf(initialAndAmountSam));
                        result.setAttributeNode(attr);
                        Attr attr1 = document.createAttribute("CustomerName");
                        attr1.setValue(customerName);
                        result.setAttributeNode(attr1);
                        Attr attr2=document.createAttribute("type");
                        attr2.setValue(terminalArrayList.get(i).getType());
                        result.setAttributeNode(attr2);
                    }
                }

                if (terminalArrayList.get(i).getType().equalsIgnoreCase("withdraw")) {

                    initialBalance = Integer.parseInt(String.valueOf(depositArrayList.get(i).getInitialBalance()));
                    amount = Integer.parseInt(String.valueOf(terminalArrayList.get(i).getAmount()));
                    initialAndAmountSub = initialBalance - amount;
                    customerName = depositArrayList.get(i).getCustomer();

                    if (initialBalance < amount) {

                        System.out.println("مشتری محترم آقا/خانم " + depositArrayList.get(i).getCustomer() + " نمیتوانید مبلغ " + amount + " را بر داشت کنید");
                    } else {

                        Element result = document.createElement("result");
                        root.appendChild(result);
                        Attr attr = document.createAttribute("newInitialBalance");
                        attr.setValue(String.valueOf(initialAndAmountSam));
                        result.setAttributeNode(attr);
                        Attr attr1 = document.createAttribute("CustomerName");
                        attr1.setValue(customerName);
                        result.setAttributeNode(attr1);
                        Attr attr2=document.createAttribute("type");
                        attr2.setValue(terminalArrayList.get(i).getType());
                        result.setAttributeNode(attr2);

                    }

                }

            }
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource domSource = new DOMSource(document);
            StreamResult streamResult = new StreamResult(new File(xmlFilePath));
            transformer.transform(domSource, streamResult);
            System.out.println("Done creating XML File");


        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerConfigurationException r) {
            r.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

}


