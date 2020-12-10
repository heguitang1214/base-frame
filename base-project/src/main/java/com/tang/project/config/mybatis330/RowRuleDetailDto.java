package com.tang.project.config.mybatis330;

import lombok.Data;


/**
 * 返回数据模型
 */
@Data
public class RowRuleDetailDto {

    private String id;

    private String tableName;

    private String ruleField;

    private String ruleCondition;

    private String ruleValue;


    public RowRuleDetailDto(String id, String tableName, String ruleField, String ruleCondition, String ruleValue) {
        this.id = id;
        this.tableName = tableName;
        this.ruleField = ruleField;
        this.ruleCondition = ruleCondition;
        this.ruleValue = ruleValue;
    }

}