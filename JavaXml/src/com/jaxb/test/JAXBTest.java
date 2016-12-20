package com.jaxb.test;
import com.jaxb.src.Boy;

import java.io.StringReader;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
/**
 * Created by odl on 16-12-20.
 */
public class JAXBTest {

    public static void main(String[] args) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(Boy.class);

        Marshaller marshaller = context.createMarshaller();
        Unmarshaller unmarshaller = context.createUnmarshaller();

        //Java Object convert to xml
        Boy boy = new Boy();
        marshaller.marshal(boy, System.out);
        System.out.println();

        //Xml convert to Java Object
        String xml = "<boy><name>David</name></boy>";
        Boy boy1 = (Boy) unmarshaller.unmarshal(new StringReader(xml));
        System.out.println(boy1.name);
    }
}