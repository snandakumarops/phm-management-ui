package com.redhat.rest.example.demorest;

public class FormData {

    String product;
    String subProduct;
    String clientAction;
    String clientActionSubType;
    String residency;
    String audience;
    String region;
    String country;
    String subChannel;

    public FormData(String product, String subProduct, String clientAction, String clientActionSubType, String residency, String audience, String region, String country) {
        this.product = product;
        this.subProduct = subProduct;
        this.clientAction = clientAction;
        this.clientActionSubType = clientActionSubType;
        this.residency = residency;
        this.audience = audience;
        this.region = region;
        this.country = country;
    }

    public FormData() {

    }

    public String getSubChannel() {
        return subChannel;
    }

    public void setSubChannel(String subChannel) {
        this.subChannel = subChannel;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getSubProduct() {
        return subProduct;
    }

    public void setSubProduct(String subProduct) {
        this.subProduct = subProduct;
    }

    public String getClientAction() {
        return clientAction;
    }

    public void setClientAction(String clientAction) {
        this.clientAction = clientAction;
    }

    public String getClientActionSubType() {
        return clientActionSubType;
    }

    public void setClientActionSubType(String clientActionSubType) {
        this.clientActionSubType = clientActionSubType;
    }

    public String getResidency() {
        return residency;
    }

    public void setResidency(String residency) {
        this.residency = residency;
    }

    public String getAudience() {
        return audience;
    }

    public void setAudience(String audience) {
        this.audience = audience;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public String toString() {
        return "FormData{" +
                "product='" + product + '\'' +
                ", subProduct='" + subProduct + '\'' +
                ", clientAction='" + clientAction + '\'' +
                ", clientActionSubType='" + clientActionSubType + '\'' +
                ", residency='" + residency + '\'' +
                ", audience='" + audience + '\'' +
                ", region='" + region + '\'' +
                ", country='" + country + '\'' +
                '}';
    }
}
