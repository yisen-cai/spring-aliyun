package com.glancebar.aliyun

import com.glancebar.aliyun.sts.AliyunConfig
import com.glancebar.aliyun.sts.AliyunConfigParams
import com.glancebar.aliyun.sts.StsService
import com.glancebar.aliyun.sts.StsServiceApi
import com.glancebar.aliyun.sts.config.AliyunStsProperties
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


/**
 *
 * @author YISEN CAI
 */
@Configuration
@ConditionalOnClass(StsServiceApi::class)
@EnableConfigurationProperties(value = [AliyunStsProperties::class])
open class AliyunAutoConfiguration(
    private val aliyunStsProperties: AliyunStsProperties
) {

    @Bean
    open fun aliyunConfig(): AliyunConfig {
        val config = AliyunConfig()
        config[AliyunConfigParams.ALIYUN_STS_ACCESS_KEY_ID] = aliyunStsProperties.sts.accessKeyId
        config[AliyunConfigParams.ALIYUN_STS_ACCESS_KEY_SECRET] = aliyunStsProperties.sts.accessKeySecret
        config[AliyunConfigParams.ALIYUN_STS_ENDPOINT] = aliyunStsProperties.sts.endpoint
        config[AliyunConfigParams.ALIYUN_STS_ACM_ROLE] = aliyunStsProperties.sts.roleArn
        config[AliyunConfigParams.ALIYUN_STS_DURATION_SECONDS] = aliyunStsProperties.sts.durationSeconds
        return config
    }


    @Bean
    @ConditionalOnMissingBean
    open fun stsService(aliyunConfig: AliyunConfig): StsService {
        return StsServiceApi(aliyunConfig)
    }
}