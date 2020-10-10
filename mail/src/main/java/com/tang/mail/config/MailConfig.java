package com.tang.mail.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "mail")
public class MailConfig {

    private List<MailProperties> configs;

    public List<MailProperties> getConfigs() {
        return configs;
    }

    public void setConfigs(List<MailProperties> configs) {
        this.configs = configs;
    }

    public static class MailProperties {

        /**
         * 用户名
         */
        private String username;

        /**
         * 密码
         */
        private String password;

        /**
         * host
         */
        private String host;

        /**
         * 端口
         */
        private Integer port;

        /**
         * 协议
         */
        private String protocol;

        /**
         * 默认编码
         */
        private String defaultEncoding;


        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public Integer getPort() {
            return port;
        }

        public void setPort(Integer port) {
            this.port = port;
        }

        public String getProtocol() {
            return protocol;
        }

        public void setProtocol(String protocol) {
            this.protocol = protocol;
        }

        public String getDefaultEncoding() {
            return defaultEncoding;
        }

        public void setDefaultEncoding(String defaultEncoding) {
            this.defaultEncoding = defaultEncoding;
        }
    }

}
