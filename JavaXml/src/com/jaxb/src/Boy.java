package com.jaxb.src;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by haofan on 16-12-20.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.PROPERTY)
public class Boy {
    public String name = "CY";
    int age = 10;
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}

