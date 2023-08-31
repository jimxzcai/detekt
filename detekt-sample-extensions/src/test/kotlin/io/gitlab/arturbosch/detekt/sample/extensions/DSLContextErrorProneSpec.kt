package io.gitlab.arturbosch.detekt.sample.extensions

import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.sample.extensions.code
import io.gitlab.arturbosch.detekt.sample.extensions.rules.DSLContextErrorProne
import io.gitlab.arturbosch.detekt.sample.extensions.rules.TooManyFunctions
import io.gitlab.arturbosch.detekt.test.lint
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class DSLContextErrorProneSpec {

    private val subject = DSLContextErrorProne(Config.empty)

    @Test
    fun `this is DSLContext `() {
        val findings = subject.lint(code)
//        Assertions.assertThat(findings).hasSize(1)
    }
}

private val code: String = """
    fun getLatestTemplate(
        dslContext: DSLContext,
        projectId: String,
        templateId: String
    ): TTemplateRecord {
        with(TTemplate.T_TEMPLATE) {
            return dslContext.selectFrom(this)
                .where(PROJECT_ID.eq(projectId))
                .and(ID.eq(templateId))
                .orderBy(CREATED_TIME.desc(), VERSION.desc())
                .limit(1)
                .fetchOne() ?: throw NotFoundException("流水线模板不存在")
        }
    }
""".trimIndent()
