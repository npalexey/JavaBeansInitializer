package com.nikitiuk.javabeansinitializer.wrongway;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class WrongReader {
    public static void main(String[] args) {
        String location = "src/main/resources/beans.xml";
        System.out.println(parseXML(location).toString());
    }

    /*public <T> T executeQuery(Class<T> expectedResultClass,
                              String someArg, Object... otherArgs) {...}*/

    private static List<String> parseXML(String fileName) {
        List<String> empList = new ArrayList<>();
        String emp = "";
        XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
        try {
            XMLEventReader xmlEventReader = xmlInputFactory.createXMLEventReader(new FileInputStream(fileName));
            while(xmlEventReader.hasNext()){
                XMLEvent xmlEvent = xmlEventReader.nextEvent();
                if (xmlEvent.isStartElement()){
                    StartElement startElement = xmlEvent.asStartElement();
                    if(startElement.getName().getLocalPart().equals("import")){
                        Attribute srcAttr = startElement.getAttributeByName(new QName("src"));
                        if(srcAttr != null){
                            empList.add("\n import " + srcAttr.getValue() + ";");
                        }
                    }
                    else if(startElement.getName().getLocalPart().equals("bean")){
                        xmlEvent = xmlEventReader.nextEvent();
                        Attribute idAttr = startElement.getAttributeByName(new QName("id"));
                        Attribute classAttr = startElement.getAttributeByName(new QName("class"));
                        if(idAttr != null){
                            empList.add("\n public class " + idAttr.getValue() + " implements java.io.Serializable {\n public " + idAttr.getValue() + "() {\n}");
                        }
                    }
                    /*}else if(startElement.getName().getLocalPart().equals("")){
                        xmlEvent = xmlEventReader.nextEvent();
                        (xmlEvent.asCharacters().getData());
                    }*/
                }
                if(xmlEvent.isEndElement()){
                    EndElement endElement = xmlEvent.asEndElement();
                    if(endElement.getName().getLocalPart().equals("beans")){
                        empList.add(emp);
                    }
                }
            }

        } catch (FileNotFoundException | XMLStreamException e) {
            e.printStackTrace();
        }
        return empList;
    }
}
