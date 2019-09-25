package com.example.epay.bean;

import java.io.Serializable;

/**
 * Created by liujin on 2018/5/9.
 */

public class CodeBean implements Serializable {
    private String codeURL="";
    private String coverURL="";
    private String iconURL="";
    private String brandName="";
    private String muuid="";
    private String deskNO="";

    public String getCodeURL() {
        return codeURL;
    }

    public void setCodeURL(String codeURL) {
        this.codeURL = codeURL;
    }

    public String getCoverURL() {
        return coverURL;
    }

    public void setCoverURL(String coverURL) {
        this.coverURL = coverURL;
    }

    public String getIconURL() {
        return iconURL;
    }

    public void setIconURL(String iconURL) {
        this.iconURL = iconURL;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getMuuid() {
        return muuid;
    }

    public void setMuuid(String muuid) {
        this.muuid = muuid;
    }

    public String getDeskNO() {
        return deskNO;
    }

    public void setDeskNO(String deskNO) {
        this.deskNO = deskNO;
    }
}
