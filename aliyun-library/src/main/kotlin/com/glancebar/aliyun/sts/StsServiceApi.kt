package com.glancebar.aliyun.sts

import com.aliyuncs.DefaultAcsClient
import com.aliyuncs.auth.BasicSessionCredentials
import com.aliyuncs.auth.sts.AssumeRoleRequest
import com.aliyuncs.auth.sts.AssumeRoleResponse
import com.aliyuncs.http.MethodType
import com.aliyuncs.profile.DefaultProfile
import com.glancebar.aliyun.MPUploadOssHelper
import com.glancebar.aliyun.results.SignatureResult
import java.time.LocalDateTime
import java.time.ZoneOffset


/**
 * STS authorization access.
 * https://help.aliyun.com/document_detail/100624.htm?spm=a2c4g.11186623.2.5.1d746d13rLl0lR#concept-xzh-nzk-2gb
 * @author Ethan Gary
 */
class StsServiceApi(
    private val aliyunConfig: AliyunConfig
) : StsService {

    @Volatile
    private var assumeRoleResponse: AssumeRoleResponse? = null

    /**
     * Sts authorize then generate arn
     *
     * @param roleArn               role-arn name
     * @param roleSessionName       app user name
     * @param policy                aliyun authorities json string
     * @return                      AssumeRoleResponse
     */
    override fun stsAuthorizeThenGenerateArn(
        roleArn: String,
        roleSessionName: String,
        policy: String?
    ): AssumeRoleResponse {

        DefaultProfile.addEndpoint(
            "", "",
            "Sts",
            aliyunConfig[AliyunConfigParams.ALIYUN_STS_ENDPOINT] as String
        )

        val profile = DefaultProfile.getProfile(
            "",
            aliyunConfig[AliyunConfigParams.ALIYUN_STS_ACCESS_KEY_ID] as String,
            aliyunConfig[AliyunConfigParams.ALIYUN_STS_ACCESS_KEY_SECRET] as String
        )

        val client = DefaultAcsClient(profile)

        val request = AssumeRoleRequest()
        request.method = MethodType.GET
        request.roleArn = roleArn
        request.roleSessionName = roleSessionName
        request.policy = policy
        request.durationSeconds = aliyunConfig[AliyunConfigParams.ALIYUN_STS_DURATION_SECONDS] as Long
        return client.getAcsResponse(request)
    }

    override fun generateSignature(roleSessionName: String): SignatureResult {
        return MPUploadOssHelper(aliyunConfig).createUploadParams()
    }

    override fun generateStsSignature(roleSessionName: String): SignatureResult {
        refreshACM(roleSessionName)
        return MPUploadOssHelper(
            BasicSessionCredentials(
                assumeRoleResponse!!.credentials.accessKeyId,
                assumeRoleResponse!!.credentials.accessKeySecret,
                assumeRoleResponse!!.credentials.securityToken
            )
        ).createUploadParams()
    }

    @Synchronized
    private fun refreshACM(sessionName: String) {
        if (assumeRoleResponse == null ||
            LocalDateTime.parse(assumeRoleResponse!!.credentials.expiration).toInstant(ZoneOffset.ofTotalSeconds(0))
                .toEpochMilli() < System.currentTimeMillis()
        ) {
            assumeRoleResponse =
                stsAuthorizeThenGenerateArn(aliyunConfig[AliyunConfigParams.ALIYUN_STS_ACM_ROLE] as String, sessionName)
        }
    }
}