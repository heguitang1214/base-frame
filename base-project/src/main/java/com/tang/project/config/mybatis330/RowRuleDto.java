package com.tang.project.config.mybatis330;

import lombok.Data;

import java.util.List;


/**
 * 返回数据模型
 */
@Data
public class RowRuleDto {

    private String id;

    private String name;

    private String joiner;

    private List<RowRuleDetailDto> rowRuleDetailDtoList;

    public RowRuleDto(String id, String name, String joiner, List<RowRuleDetailDto> rowRuleDetailDtoList) {
        this.id = id;
        this.name = name;
        this.joiner = joiner;
        this.rowRuleDetailDtoList = rowRuleDetailDtoList;
    }
}