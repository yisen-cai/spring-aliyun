package com.glancebar.aliyun.sts.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding


/**
 * 自动装配所需的配置映射类
 *
 * @author YISEN CAI
 */
@ConstructorBinding
@ConfigurationProperties(prefix = "aliyun")
data class AliyunStsProperties(
    val sts: Sts
) {

    data class Sts(
        val accessKeyId: String,
        val accessKeySecret: String,
        val endpoint: String,
        val roleArn: String,
        val durationSeconds: Long
    )
}