package com.tang.project.entry;


import lombok.Data;

@Data
public class UserData {
    private Long id;

    /**
     * 脱敏的身份证
     */
    private String idcard;

    /**
     * 身份证加密ID
     */
    private Long idcardCipherId;

    /**
     * 身份证密文
     */
    private String idcardCipherText;

    /**
     * 脱敏的姓名
     */
    private String name;

    /**
     * 姓名加密ID
     */
    private Long nameCipherId;

    /**
     * 姓名密文
     */
    private String nameCipherText;
}