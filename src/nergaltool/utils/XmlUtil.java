package nergaltool.utils;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;

public class XmlUtil {
    public static Document newDocument() throws ParserConfigurationException {
        return getDocumentBuilder().newDocument();
    }

    public static Document readFileGetDocument(String path) throws ParserConfigurationException, IOException, SAXException {
        return getDocumentBuilder().parse(path);
    }

    private static DocumentBuilder getDocumentBuilder() throws ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        return factory.newDocumentBuilder();
    }

    public static void saveDocumentToFile(String path, Document document) throws TransformerException {
        TransformerFactory tff = TransformerFactory.newInstance();
        Transformer tf = tff.newTransformer();
        tf.transform(new DOMSource(document), new StreamResult(new File(path)));
    }
}
