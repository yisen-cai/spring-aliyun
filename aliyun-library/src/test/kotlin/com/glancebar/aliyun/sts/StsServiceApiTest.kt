package com.glancebar.aliyun.sts

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.time.LocalDateTime
import java.time.ZoneOffset

//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class StsServiceApiTest {

    private lateinit var stsService: StsService
    private val aliyunConfig = AliyunConfig()

//    @BeforeAll
    internal fun setUp() {

        aliyunConfig[AliyunConfigParams.ALIYUN_STS_ACCESS_KEY_ID] = ""
        aliyunConfig[AliyunConfigParams.ALIYUN_STS_ACCESS_KEY_SECRET] = ""
        aliyunConfig[AliyunConfigParams.ALIYUN_STS_ENDPOINT] = "sts.cn-beijing.aliyuncs.com"
        aliyunConfig[AliyunConfigParams.ALIYUN_STS_ACM_ROLE] = ""
        aliyunConfig[AliyunConfigParams.ALIYUN_STS_DURATION_SECONDS] = 1000L

        stsService = StsServiceApi(aliyunConfig)
    }

//    @Test
    fun stsAuthorizeThenGenerateArn() {
        val response =
            stsService.stsAuthorizeThenGenerateArn(
                aliyunConfig[AliyunConfigParams.ALIYUN_STS_ACM_ROLE] as String,
                "role"
            )
        println(response)
        Assertions.assertNotNull(response)
    }

//    @Test
    fun generateSignature() {
        val result = stsService.generateSignature("test")
        Assertions.assertNotNull(result)
    }

//    @Test
    internal fun generateStsSignature() {
        val result = stsService.generateStsSignature("test")
        Assertions.assertNotNull(result)
    }

//    @Test
    internal fun testLocalDateTime() {
        val date = LocalDateTime.parse("2021-03-04T17:30:53Z".subSequence(0, 19)).toInstant(ZoneOffset.ofTotalSeconds(0))
        println(date)
        Assertions.assertNotNull(date)
    }
}