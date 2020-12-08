package com.tang.project.config.mybatis330;

public class RowRule {

    private String attr;
    private String attrValue;
    private String joiner;
    private String tableName;

    public RowRule(String attr, String attrValue, String joiner, String tableName) {
        this.attr = attr;
        this.attrValue = attrValue;
        this.joiner = joiner;
        this.tableName = tableName;
    }

    public String getAttr() {
        return attr;
    }

    public void setAttr(String attr) {
        this.attr = attr;
    }

    public String getAttrValue() {
        return attrValue;
    }

    public void setAttrValue(String attrValue) {
        this.attrValue = attrValue;
    }

    public String getJoiner() {
        return joiner;
    }

    public void setJoiner(String joiner) {
        this.joiner = joiner;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
}