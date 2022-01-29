package com.glancebar.aliyun

import com.glancebar.aliyun.AliyunAutoConfiguration.Companion.messageSource
import com.glancebar.aliyun.sts.exception.AliyunConfigNotSetException
import org.springframework.boot.diagnostics.AbstractFailureAnalyzer
import org.springframework.boot.diagnostics.FailureAnalysis
import java.util.*


/**
 * Customized FailureAnalyzer will catch the exception between the Generic exception and
 * extract Description and Action info.
 * @see <a href="https://www.baeldung.com/spring-boot-failure-analyzer">Tutorial</a>
 * @author yishen.cai
 */
class AliyunConfigNotSetFailureAnalyzer : AbstractFailureAnalyzer<AliyunConfigNotSetException>() {

    override fun analyze(rootFailure: Throwable?, cause: AliyunConfigNotSetException?): FailureAnalysis {
        return FailureAnalysis(getDescription(cause), getAction(cause), cause)
    }

    private fun getAction(cause: AliyunConfigNotSetException?): String {
        if (cause != null) {
            return messageSource.getMessage(
                "aliyun.config.violation.action",
                arrayOf(cause.activeProfile),
                Locale.SIMPLIFIED_CHINESE
            )
        }
        return messageSource.getMessage("aliyun.config.violation.action.default", null, Locale.SIMPLIFIED_CHINESE)
    }

    private fun getDescription(cause: AliyunConfigNotSetException?): String {
        if (cause != null) {
            return messageSource.getMessage(
                "aliyun.config.violation.description",
                arrayOf(cause.violations, cause.activeProfile),
                Locale.SIMPLIFIED_CHINESE
            )
        }
        return messageSource.getMessage("aliyun.config.violation.description.default", null, Locale.SIMPLIFIED_CHINESE)
    }
}