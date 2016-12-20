package com.jaxb.src;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by haofan on 16-12-20.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.PROPERTY)
public class Boy {
    private String name = "haofan";

    @XmlElement
    private int age = 26;
    private int score = 100;

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

}

