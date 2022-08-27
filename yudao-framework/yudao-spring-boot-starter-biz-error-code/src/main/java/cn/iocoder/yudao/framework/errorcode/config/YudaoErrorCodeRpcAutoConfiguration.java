package cn.iocoder.yudao.framework.errorcode.config;

import cn.iocoder.yudao.module.system.api.errorcode.ErrorCodeApi;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

/**
 * 错误码用到 Feign 的配置项
 *
 * @author 芋道源码
 */
@Configuration(proxyBeanMethods = false)
@EnableFeignClients(clients = ErrorCodeApi.class) // 主要是引入相关的 API 服务
public class YudaoErrorCodeRpcAutoConfiguration {
}
