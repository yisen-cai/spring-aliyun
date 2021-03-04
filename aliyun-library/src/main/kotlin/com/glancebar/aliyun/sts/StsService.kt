package com.glancebar.aliyun.sts

import com.aliyuncs.auth.sts.AssumeRoleResponse
import com.glancebar.aliyun.results.SignatureResult

/**
 *
 * @author Ethan Gary
 */
interface StsService {

    fun stsAuthorizeThenGenerateArn(
        roleArn: String,
        roleSessionName: String,
        policy: String? = null
    ): AssumeRoleResponse


    fun generateSignature(roleSessionName: String): SignatureResult

    fun generateStsSignature(roleSessionName: String): SignatureResult
}