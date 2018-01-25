package com.hanz.model;

/**
 * Created by hanzhao on 2018/1/21.
 */
public class NLM {
    private double id;
    private String categary; //药品分类
    private String zhName; //药品名称（中文）
    private String enName;//英文对照
    private String CAS;
    private String Canonical;
    private String Molecula;
    private String MeSH;
    private String Depositor;

    public double getId() {
        return id;
    }

    public void setId(double id) {
        this.id = id;
    }

    public String getCategary() {
        return categary;
    }

    public void setCategary(String categary) {
        this.categary = categary;
    }

    public String getZhName() {
        return zhName;
    }

    public void setZhName(String zhName) {
        this.zhName = zhName;
    }

    public String getEnName() {
        return enName;
    }

    public void setEnName(String enName) {
        this.enName = enName;
    }

    public String getCAS() {
        return CAS;
    }

    public void setCAS(String CAS) {
        this.CAS = CAS;
    }

    public String getCanonical() {
        return Canonical;
    }

    public void setCanonical(String canonical) {
        Canonical = canonical;
    }

    public String getMolecula() {
        return Molecula;
    }

    public void setMolecula(String molecula) {
        Molecula = molecula;
    }

    public String getMeSH() {
        return MeSH;
    }

    public void setMeSH(String meSH) {
        MeSH = meSH;
    }

    public String getDepositor() {
        return Depositor;
    }

    public void setDepositor(String depositor) {
        Depositor = depositor;
    }
}
