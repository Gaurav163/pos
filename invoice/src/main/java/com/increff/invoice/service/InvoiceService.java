package com.increff.invoice.service;

import com.increff.invoice.model.ApiException;
import com.increff.invoice.model.OrderForm;
import com.increff.invoice.model.OrderItemForm;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
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
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.StringReader;
import java.io.StringWriter;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.List;

@Service
public class InvoiceService {
    @Value("${resourcesPath}")
    private String resourcesPath;

    public String generateInvoice(OrderForm order) throws ApiException {
        // the XSL FO file
        try {
            File xsltFile = new File(resourcesPath + "/invoice.xsl");
            String xml = orderToXML(order);
            StreamSource xmlSource = new StreamSource(new StringReader(xml));
            FopFactory fopFactory = FopFactory.newInstance(new File(".").toURI());
            FOUserAgent foUserAgent = fopFactory.newFOUserAgent();
            ByteArrayOutputStream str = new ByteArrayOutputStream();

            Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, foUserAgent, str);
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer(new StreamSource(xsltFile));
            Result res = new SAXResult(fop.getDefaultHandler());
            transformer.transform(xmlSource, res);

            String invoice = Base64.getEncoder().encodeToString(str.toByteArray());
            return invoice;
        } catch (Exception e) {
            throw new ApiException("Error while generating invoice ," + e.getMessage());
        }
    }

    private String orderToXML(OrderForm order) {
        try {
            ZonedDateTime currentTime = ZonedDateTime.now();
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.newDocument();

            Element rootElement = doc.createElement("OrderData");
            doc.appendChild(rootElement);

            Element id = doc.createElement("id");
            id.appendChild(doc.createTextNode(order.getId().toString()));
            rootElement.appendChild(id);

            Element date = doc.createElement("date");
            date.appendChild(doc.createTextNode(currentTime.toLocalDate().toString()));
            rootElement.appendChild(date);

            Element time = doc.createElement("time");
            time.appendChild(doc.createTextNode(currentTime.truncatedTo(ChronoUnit.SECONDS).toLocalTime().toString()));
            rootElement.appendChild(time);

            Element orderItems = doc.createElement("order-items");
            List<OrderItemForm> items = order.getItems();
            Long index = 1L;
            Double netTotal = 0.0;
            Long netQuantity = 0L;
            for (OrderItemForm item : items) {
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
                sellingPrice.appendChild(doc.createTextNode(String.format("%.2f", item.getSellingPrice())));
                orderItem.appendChild(sellingPrice);

                Element brand = doc.createElement("brand");
                brand.appendChild(doc.createTextNode(item.getBrand()));
                orderItem.appendChild(brand);

                Element category = doc.createElement("category");
                category.appendChild(doc.createTextNode(item.getCategory()));
                orderItem.appendChild(category);

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
