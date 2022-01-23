package com.glancebar.aliyun.results


/**
 *
 * @author YISEN CAI
 */
data class SignatureResult(
    val ossAccessKeyId: String,
    val policy: String,
    val signature: String,
    val securityToken: String? = null // STS signature
)