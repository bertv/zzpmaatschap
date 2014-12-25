package com.bv.zzpmaatschap.services;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ForgetMessage {
    private String msg;

    public ForgetMessage(String content) {
        this.msg = content;
    }

    public String getMsg() {
        return msg;
    }


}
