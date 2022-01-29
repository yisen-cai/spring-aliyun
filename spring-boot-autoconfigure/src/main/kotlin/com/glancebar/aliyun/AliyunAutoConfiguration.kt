package com.glancebar.aliyun

import com.glancebar.aliyun.sts.AliyunConfig
import com.glancebar.aliyun.sts.AliyunConfigParams
import com.glancebar.aliyun.sts.StsService
import com.glancebar.aliyun.sts.StsServiceApi
import com.glancebar.aliyun.sts.config.AliyunStsProperties
import com.glancebar.aliyun.sts.exception.AliyunConfigNotSetException
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.support.ReloadableResourceBundleMessageSource
import org.springframework.core.env.Environment
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean
import java.util.*


/**
 * Aliyun自动装配
 *
 * @author YISEN CAI
 */
@Configuration
@ConditionalOnClass(StsServiceApi::class)
@EnableConfigurationProperties(value = [AliyunStsProperties::class])
open class AliyunAutoConfiguration(
    /**
     * Auto-inject aliyun related configurations.
     */
    private val aliyunStsProperties: AliyunStsProperties,
    private val environment: Environment
) {

    /**
     * Define aliyun related properties' config bean.
     */
    @Bean
    open fun aliyunConfig(): AliyunConfig {
        validateAliyunConfigs()
        val config = AliyunConfig()
        aliyunStsProperties.sts!!.let {
            config[AliyunConfigParams.ALIYUN_STS_ACCESS_KEY_ID] = aliyunStsProperties.sts.accessKeyId
            config[AliyunConfigParams.ALIYUN_STS_ACCESS_KEY_SECRET] = aliyunStsProperties.sts.accessKeySecret
            config[AliyunConfigParams.ALIYUN_STS_ENDPOINT] = aliyunStsProperties.sts.endpoint
            config[AliyunConfigParams.ALIYUN_STS_ACM_ROLE] = aliyunStsProperties.sts.roleArn
            config[AliyunConfigParams.ALIYUN_STS_DURATION_SECONDS] = aliyunStsProperties.sts.durationSeconds
            return config
        }
    }


    private fun validateAliyunConfigs() {
        val violations = aliyunValidatorFactory().validator.validate(aliyunStsProperties)
        if (violations.isNotEmpty()) {
            val violationMessages = StringBuilder()
            val profileMessages = StringBuilder()
            violations.forEach {
                violationMessages.append(it.message).append("\n")
            }
            if (environment.activeProfiles.isEmpty()) {
                profileMessages.append("default").append("\n")
            }
            environment.activeProfiles.forEach {
                profileMessages.append(it).append("\n")
            }
            throw AliyunConfigNotSetException("\n$profileMessages",  "\n$violationMessages")
        }
    }


    /**
     * 根据基础配置创建Aliyun相关工具类Bean
     */
    @Bean
    @ConditionalOnMissingBean
    open fun stsService(aliyunConfig: AliyunConfig): StsService {
        return StsServiceApi(aliyunConfig)
    }



    /**
     * validator相关消息配置
     */
    @Bean
    open fun aliyunValidatorFactory(): LocalValidatorFactoryBean{
        val bean = LocalValidatorFactoryBean()
        bean.setValidationMessageSource(messageSource)
        return bean
    }


    /**
     * Analyzer doesn't support constructor injection.
     */
    companion object {
        val messageSource = ReloadableResourceBundleMessageSource()
        init {
            messageSource.setBasename("classpath:aliyun-messages")
            // no .properties
            messageSource.setDefaultEncoding("UTF-8")
            messageSource.setDefaultLocale(Locale.SIMPLIFIED_CHINESE)
        }
    }
}