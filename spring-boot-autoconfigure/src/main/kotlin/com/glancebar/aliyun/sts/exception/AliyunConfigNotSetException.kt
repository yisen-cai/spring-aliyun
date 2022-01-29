package com.glancebar.aliyun.sts.exception

import org.springframework.beans.factory.BeanCreationException


/**
 * Customized exception to store details info.
 * @author yishen.cai
 */
class AliyunConfigNotSetException(
    // spring active profile setting
    val activeProfile: String,
    // javax.violations messages
    val violations: String,
) : BeanCreationException(violations) {

}