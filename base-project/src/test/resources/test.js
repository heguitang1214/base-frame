var logger = org.slf4j.LoggerFactory.getLogger("jmx_activemq_kpitemp.js");
var util = new com.ai.toptea.jmx.monitor.javascript.platform.CassandraPlatformUtil();  //java 类实现公共方法


function collectEntranceFunction(kpiArray, jmx_hostname, jmx_port, jmx_username, jmx_pwd) {
    logger.info("execute jmx_activemq_kpitemp.js...");
    var msc = util.initMBeanServerConnAndReturn(jmx_hostname, jmx_port, jmx_username, jmx_pwd);
    var resultMap = new java.util.HashMap();
    for (var i = 0; i < kpiArray.length; i++) {
        resultMap.put(kpiArray[i], this[kpiArray[i]](msc));
    }
    util.closeJMXConnector();//关闭JMXConnector连接
    logger.info("execute jmx_activemq_kpitemp.js completed.");
    return resultMap;
}