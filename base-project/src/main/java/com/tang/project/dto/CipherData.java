package com.tang.project.dto;

import lombok.Data;

//import javax.persistence.Entity;
//import javax.persistence.GeneratedValue;
//import javax.persistence.Id;

//import static javax.persistence.GenerationType.AUTO;

@Data
//@Entity
public class CipherData {

    private Long id;

    /**
     * 初始化向量
     */
    private String iv;

    /**
     * 密钥
     */
    private String secureKey;
}