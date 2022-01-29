package com.glancebar.aliyun.sts.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import javax.validation.Valid
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull


/**
 * 自动装配所需的配置映射类
 *
 * @author YISEN CAI
 */
@ConstructorBinding
@ConfigurationProperties(prefix = "aliyun")
open class AliyunStsProperties(
    @field:NotNull(message = "{aliyun.sts.config.is-null}")
    @field:Valid val sts: Sts?
) {

    open class Sts(
        @field:NotNull(message = "not null.")
        @field:NotBlank(message = "{aliyun.config.sts.access-key-id.is-blank}")
        val accessKeyId: String?,
        @field:NotBlank(message = "{aliyun.config.sts.access-key-secret.is-blank}")
        val accessKeySecret: String?,
        @field:NotBlank(message = "{aliyun.config.sts.endpoint.is-blank}")
        val endpoint: String?,
        @field:NotBlank(message = "{aliyun.config.sts.role-arn.is-blank}")
        val roleArn: String?,
        @field:Min(value = 10, message = "{aliyun.config.sts.duration-seconds.is-less-than}")
        @field:NotNull(message = "{aliyun.config.sts.duration-seconds.is-less-than}")
        val durationSeconds: Long?
    )
}