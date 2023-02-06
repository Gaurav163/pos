package com.increff.pos.util;

import com.increff.pos.model.ApiException;
import com.increff.pos.model.OrderData;
import com.increff.pos.model.OrderItemData;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class PdfUtil {
    private static final String tempurl = "src/main/resources/template";
    private static final String pdfurl = "/home/gaurav_inc/pos/invoices/";

    public static void convertToPDF(OrderData order) throws ApiException {
        System.out.println(order);
        // the XSL FO file
        try {
            File xsltFile = new File(tempurl + "/invoice.xsl");
            String xml = toxml(order);
            StreamSource xmlSource = new StreamSource(new StringReader(xml));
            FopFactory fopFactory = FopFactory.newInstance(new File(".").toURI());
            FOUserAgent foUserAgent = fopFactory.newFOUserAgent();
            OutputStream out = Files.newOutputStream(Paths.get(pdfurl + "invoice-" + order.getId() + ".pdf"));
            try {
                Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, foUserAgent, out);
                TransformerFactory factory = TransformerFactory.newInstance();
                Transformer transformer = factory.newTransformer(new StreamSource(xsltFile));
                Result res = new SAXResult(fop.getDefaultHandler());
                transformer.transform(xmlSource, res);
            } finally {
                out.close();
            }
        } catch (Exception e) {
            throw new ApiException("Error while generating invoice ," + e.getMessage());
        }
    }

    private static String toxml(OrderData order) {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.newDocument();

            Element rootElement = doc.createElement("OrderData");
            doc.appendChild(rootElement);

            Element id = doc.createElement("id");
            id.appendChild(doc.createTextNode(order.getId().toString()));
            rootElement.appendChild(id);

            Element date = doc.createElement("date");
            date.appendChild(doc.createTextNode(order.getDate()));
            rootElement.appendChild(date);

            Element time = doc.createElement("time");
            time.appendChild(doc.createTextNode(order.getTime()));
            rootElement.appendChild(time);

            Element orderItems = doc.createElement("order-items");
            List<OrderItemData> items = order.getItems();
            Long index = 1L;
            Double netTotal = 0.0;
            Long netQuantity = 0L;
            for (OrderItemData item : items) {
                Element orderItem = doc.createElement("item");

                Element id_ = doc.createElement("sn");
                id_.appendChild(doc.createTextNode(Long.toString(index)));
                orderItem.appendChild(id_);

                Element name = doc.createElement("name");
                name.appendChild(doc.createTextNode(item.getName()));
                orderItem.appendChild(name);

                Element barcode = doc.createElement("barcode");
                barcode.appendChild(doc.createTextNode(item.getBarcode()));
                orderItem.appendChild(barcode);

                Element sellingPrice = doc.createElement("sellingPrice");
                sellingPrice.appendChild(doc.createTextNode(item.getSellingPrice().toString()));
                orderItem.appendChild(sellingPrice);

                Element quantity = doc.createElement("quantity");
                quantity.appendChild(doc.createTextNode(item.getQuantity().toString()));
                orderItem.appendChild(quantity);

                double total_1 = item.getQuantity() * item.getSellingPrice();
                Element total = doc.createElement("total");
                total.appendChild(doc.createTextNode(String.format("%.2f", total_1)));
                orderItem.appendChild(total);

                orderItems.appendChild(orderItem);

                netQuantity += item.getQuantity();
                netTotal += total_1;
                index += 1;

            }

            rootElement.appendChild(orderItems);

            Element itemCount = doc.createElement("items-count");
            itemCount.appendChild(doc.createTextNode(Long.toString(netQuantity)));
            rootElement.appendChild(itemCount);

            Element totalBill = doc.createElement("total-bill");
            totalBill.appendChild(doc.createTextNode(String.format("%.2f", netTotal)));
            rootElement.appendChild(totalBill);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StringWriter out = new StringWriter();
            StreamResult result = new StreamResult(out);
            transformer.transform(source, result);
            String xml = out.toString();
            return xml;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}