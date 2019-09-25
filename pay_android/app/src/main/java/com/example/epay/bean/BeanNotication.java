package com.example.epay.bean;


/**
 * 系统通知
 * Created by aa on 2015/7/31.
 */
public class BeanNotication   {
    private String NoticationContent="";
    private String NoticationTime="";

    public String getNoticationContent() {
        return NoticationContent;
    }

    public void setNoticationContent(String noticationContent) {
        NoticationContent = noticationContent;
    }

    public String getNoticationTime() {
        return NoticationTime;
    }

    public void setNoticationTime(String noticationTime) {
        NoticationTime = noticationTime;
    }
}
