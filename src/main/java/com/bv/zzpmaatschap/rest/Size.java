package com.bv.zzpmaatschap.rest;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
@XmlRootElement
public class Size implements Serializable {
    private static final long serialVersionUID = 268528170482451L;
    private long size;
    public Size(){

    }
    public Size(long size) {
        this.size = size;

    }

    public long getSize() {
        return size;
    }
}
