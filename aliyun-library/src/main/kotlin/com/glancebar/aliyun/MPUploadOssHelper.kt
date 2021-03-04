package com.glancebar.aliyun

import com.aliyuncs.auth.AlibabaCloudCredentials
import com.aliyuncs.auth.BasicSessionCredentials
import com.aliyuncs.auth.Signer
import com.glancebar.aliyun.results.SignatureResult
import com.glancebar.aliyun.sts.AliyunConfig
import com.glancebar.aliyun.sts.AliyunConfigParams
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.*


/**
 *
 * @author Ethan Gary
 */
class MPUploadOssHelper(
    private val aliyunConfig: AliyunConfig? = null,
    private var alibabaCloudCredentials: AlibabaCloudCredentials? = null,
    private val timeout: Int = 1,
    private val maxSize: Int = 10
) {

    constructor(aliyunConfig: AliyunConfig) : this(aliyunConfig, null) {
        alibabaCloudCredentials = BasicSessionCredentials(
            aliyunConfig[AliyunConfigParams.ALIYUN_STS_ACCESS_KEY_ID] as String,
            aliyunConfig[AliyunConfigParams.ALIYUN_STS_ACCESS_KEY_SECRET] as String,
            null,
            aliyunConfig[AliyunConfigParams.ALIYUN_STS_DURATION_SECONDS] as Long
        )
    }

    constructor(alibabaCloudCredentials: AlibabaCloudCredentials) : this(null, alibabaCloudCredentials)

    fun createUploadParams(): SignatureResult {
        val policy = getPolicyBase64()
        val signature = signature(policy)

        return SignatureResult(
            alibabaCloudCredentials!!.accessKeyId,
            policy,
            signature!!,
            (alibabaCloudCredentials as BasicSessionCredentials).sessionToken
        )
    }

    private fun getPolicyBase64(): String {
        val date = LocalDateTime.now()
        date.plusHours(timeout.toLong())
        val dateTime = OffsetDateTime.of(date, ZoneOffset.of("Z"))
        val actual = dateTime.format(DateTimeFormatter.ISO_DATE_TIME)

        val policyText = """
            {
                "expiration": "$actual",
                "conditions": [
                    ["content-length-range", 0, ${this.maxSize * 1024 * 1024}]
                 ]
            }
        """.trimIndent()
        return Base64.getEncoder().encodeToString(policyText.toByteArray())
    }

    private fun signature(policy: String): String? {
        return Signer.getSigner(alibabaCloudCredentials).signString(policy, alibabaCloudCredentials!!.accessKeySecret)
    }
}