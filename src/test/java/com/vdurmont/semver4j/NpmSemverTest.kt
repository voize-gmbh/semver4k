package com.vdurmont.semver4j

import com.vdurmont.semver4j.Semver.SemverType
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class NpmSemverTest(private val version: String, private val rangeExpression: String, private val expected: Boolean) {
    @Test
    fun test() {
        Assert.assertEquals("$version , $rangeExpression", expected, Semver(version, SemverType.NPM).satisfies(rangeExpression))
    }

    companion object {
        @get:Parameterized.Parameters
        @JvmStatic
        val parameters: Iterable<Array<Any>>
            get() = listOf(
                    arrayOf("1.2.3", "1.2.3", true),
                    arrayOf("1.2.4", "1.2.3", false),
                    arrayOf("1.2.3", "1.2", true),
                    arrayOf("1.2.4", "1.3", false),
                    arrayOf("1.2.3", "1", true),
                    arrayOf("1.2.4", "2", false),
                    arrayOf("1.2.4-beta+exp.sha.5114f85", "1.2.3 - 2.3.4", false),
                    arrayOf("1.2.4", "1.2.3 - 2.3.4", true),
                    arrayOf("1.2.3", "1.2.3 - 2.3.4", true),
                    arrayOf("2.3.4", "1.2.3 - 2.3.4", true),
                    arrayOf("2.3.0-alpha", "1.2.3 - 2.3.0-beta", true),
                    arrayOf("2.3.4", "1.2.3 - 2.3", true),
                    arrayOf("2.3.4", "1.2.3 - 2", true),
                    arrayOf("4.4", "3.X - 4.X", true),
                    arrayOf("1.0.0", "1.2.3 - 2.3.4", false),
                    arrayOf("3.0.0", "1.2.3 - 2.3.4", false),
                    arrayOf("2.4.3", "1.2.3 - 2.3", false),
                    arrayOf("2.3.0-rc1", "1.2.3 - 2.3.0-beta", false),
                    arrayOf("3.0.0", "1.2.3 - 2", false),
                    arrayOf("3.1.5", "", true),
                    arrayOf("3.1.5", "*", true),
                    arrayOf("0.0.0", "*", true),
                    arrayOf("1.0.0-beta", "*", true),
                    arrayOf("3.1.5-beta", "3.1.x", false),
                    arrayOf("3.1.5-beta+exp.sha.5114f85", "3.1.x", false),
                    arrayOf("3.1.5+exp.sha.5114f85", "3.1.x", true),
                    arrayOf("3.1.5", "3.1.x", true),
                    arrayOf("3.1.5", "3.1.X", true),
                    arrayOf("3.1.5", "3.x", true),
                    arrayOf("3.1.5", "3.*", true),
                    arrayOf("3.1.5", "3.1", true),
                    arrayOf("3.1.5", "3", true),
                    arrayOf("3.2.5", "3.1.x", false),
                    arrayOf("3.0.5", "3.1.x", false),
                    arrayOf("4.0.0", "3.x", false),
                    arrayOf("2.0.0", "3.x", false),
                    arrayOf("3.2.5", "3.1", false),
                    arrayOf("3.0.5", "3.1", false),
                    arrayOf("4.0.0", "3", false),
                    arrayOf("2.0.0", "3", false),
                    arrayOf("1.2.4-beta", "~1.2.3", false),
                    arrayOf("1.2.4-beta+exp.sha.5114f85", "~1.2.3", false),
                    arrayOf("1.2.3", "~1.2.3", true),
                    arrayOf("1.2.7", "~1.2.3", true),
                    arrayOf("1.2.2", "~1.2", true),
                    arrayOf("1.2.0", "~1.2", true),
                    arrayOf("1.3.0", "~1", true),
                    arrayOf("1.0.0", "~1", true),
                    arrayOf("1.2.3", "~1.2.3-beta.2", true),
                    arrayOf("1.2.3-beta.4", "~1.2.3-beta.2", true),
                    arrayOf("1.2.4", "~1.2.3-beta.2", true),
                    arrayOf("1.3.0", "~1.2.3", false),
                    arrayOf("1.2.2", "~1.2.3", false),
                    arrayOf("1.1.0", "~1.2", false),
                    arrayOf("1.3.0", "~1.2", false),
                    arrayOf("2.0.0", "~1", false),
                    arrayOf("0.0.0", "~1", false),
                    arrayOf("1.2.3-beta.1", "~1.2.3-beta.2", false),
                    arrayOf("1.2.3", "^1.2.3", true),
                    arrayOf("1.2.4", "^1.2.3", true),
                    arrayOf("1.3.0", "^1.2.3", true),
                    arrayOf("0.2.3", "^0.2.3", true),
                    arrayOf("0.2.4", "^0.2.3", true),
                    arrayOf("0.0.3", "^0.0.3", true),
                    arrayOf("0.0.3+exp.sha.5114f85", "^0.0.3", true),
                    arrayOf("0.0.3", "^0.0.3-beta", true),
                    arrayOf("0.0.3-pr.2", "^0.0.3-beta", true),
                    arrayOf("1.2.2", "^1.2.3", false),
                    arrayOf("2.0.0", "^1.2.3", false),
                    arrayOf("0.2.2", "^0.2.3", false),
                    arrayOf("0.3.0", "^0.2.3", false),
                    arrayOf("0.0.4", "^0.0.3", false),
                    arrayOf("0.0.3-alpha", "^0.0.3-beta", false),
                    arrayOf("0.0.4", "^0.0.3-beta", false),
                    arrayOf("2.0.0", "=2.0.0", true),
                    arrayOf("2.0.0", "=2.0", true),
                    arrayOf("2.0.1", "=2.0", true),
                    arrayOf("2.0.0", "=2", true),
                    arrayOf("2.0.1", "=2", true),
                    arrayOf("2.0.1", "=2.0.0", false),
                    arrayOf("1.9.9", "=2.0.0", false),
                    arrayOf("1.9.9", "=2.0", false),
                    arrayOf("1.9.9", "=2", false),
                    arrayOf("2.0.1", ">2.0.0", true),
                    arrayOf("3.0.0", ">2.0.0", true),
                    arrayOf("3.0.0", ">2.0", true),
                    arrayOf("3.0.0", ">2", true),
                    arrayOf("2.0.0", ">2.0.0", false),
                    arrayOf("1.9.9", ">2.0.0", false),
                    arrayOf("2.0.0", ">2.0", false),
                    arrayOf("1.9.9", ">2.0", false),
                    arrayOf("2.0.1", ">2", false),
                    arrayOf("2.0.0", ">2", false),
                    arrayOf("1.9.9", ">2", false),
                    arrayOf("1.9.9", "<2.0.0", true),
                    arrayOf("1.9.9", "<2.0", true),
                    arrayOf("1.9.9", "<2", true),
                    arrayOf("2.0.0", "<2.0.0", false),
                    arrayOf("2.0.1", "<2.0.0", false),
                    arrayOf("3.0.0", "<2.0.0", false),
                    arrayOf("2.0.0", "<2.0", false),
                    arrayOf("2.0.1", "<2.0", false),
                    arrayOf("3.0.0", "<2.0", false),
                    arrayOf("2.0.0", "<2", false),
                    arrayOf("2.0.1", "<2", false),
                    arrayOf("3.0.0", "<2", false),
                    arrayOf("2.0.0", ">=2.0.0", true),
                    arrayOf("2.0.1", ">=2.0.0", true),
                    arrayOf("3.0.0", ">=2.0.0", true),
                    arrayOf("2.0.0", ">=2.0", true),
                    arrayOf("3.0.0", ">=2.0", true),
                    arrayOf("2.0.0", ">=2", true),
                    arrayOf("2.0.1", ">=2", true),
                    arrayOf("3.0.0", ">=2", true),
                    arrayOf("1.9.9", ">=2.0.0", false),
                    arrayOf("1.9.9", ">=2.0", false),
                    arrayOf("1.9.9", ">=2", false),
                    arrayOf("1.9.9", "<=2.0.0", true),
                    arrayOf("2.0.0", "<=2.0.0", true),
                    arrayOf("1.9.9", "<=2.0", true),
                    arrayOf("2.0.0", "<=2.0", true),
                    arrayOf("2.0.1", "<=2.0", true),
                    arrayOf("1.9.9", "<=2", true),
                    arrayOf("2.0.0", "<=2", true),
                    arrayOf("2.0.1", "<=2", true),
                    arrayOf("2.0.1", "<=2.0.0", false),
                    arrayOf("3.0.0", "<=2.0.0", false),
                    arrayOf("3.0.0", "<=2.0", false),
                    arrayOf("3.0.0", "<=2", false),
                    arrayOf("2.0.1", ">2.0.0 <3.0.0", true),
                    arrayOf("2.0.1", ">2.0 <3.0", false),
                    arrayOf("1.2.0", "1.2 <1.2.8", true),
                    arrayOf("1.2.7", "1.2 <1.2.8", true),
                    arrayOf("1.1.9", "1.2 <1.2.8", false),
                    arrayOf("1.2.9", "1.2 <1.2.8", false),
                    arrayOf("1.2.3", "1.2.3 || 1.2.4", true),
                    arrayOf("1.2.4", "1.2.3 || 1.2.4", true),
                    arrayOf("1.2.5", "1.2.3 || 1.2.4", false),
                    arrayOf("1.2.2", ">1.2.1 <1.2.8 || >2.0.0", true),
                    arrayOf("1.2.7", ">1.2.1 <1.2.8 || >2.0.0", true),
                    arrayOf("2.0.1", ">1.2.1 <1.2.8 || >2.0.0", true),
                    arrayOf("1.2.1", ">1.2.1 <1.2.8 || >2.0.0", false),
                    arrayOf("2.0.0", ">1.2.1 <1.2.8 || >2.0.0", false),
                    arrayOf("1.2.2", ">1.2.1 <1.2.8 || >2.0.0 <3.0.0", true),
                    arrayOf("1.2.7", ">1.2.1 <1.2.8 || >2.0.0 <3.0.0", true),
                    arrayOf("2.0.1", ">1.2.1 <1.2.8 || >2.0.0 <3.0.0", true),
                    arrayOf("2.5.0", ">1.2.1 <1.2.8 || >2.0.0 <3.0.0", true),
                    arrayOf("1.2.1", ">1.2.1 <1.2.8 || >2.0.0 <3.0.0", false),
                    arrayOf("1.2.8", ">1.2.1 <1.2.8 || >2.0.0 <3.0.0", false),
                    arrayOf("2.0.0", ">1.2.1 <1.2.8 || >2.0.0 <3.0.0", false),
                    arrayOf("3.0.0", ">1.2.1 <1.2.8 || >2.0.0 <3.0.0", false),
                    arrayOf("1.2.2", "1.2.2 - 1.2.7 || 2.0.1 - 2.9.9", true),
                    arrayOf("1.2.7", "1.2.2 - 1.2.7 || 2.0.1 - 2.9.9", true),
                    arrayOf("2.0.1", "1.2.2 - 1.2.7 || 2.0.1 - 2.9.9", true),
                    arrayOf("2.5.0", "1.2.2 - 1.2.7 || 2.0.1 - 2.9.9", true),
                    arrayOf("1.2.1", "1.2.2 - 1.2.7 || 2.0.1 - 2.9.9", false),
                    arrayOf("1.2.8", "1.2.2 - 1.2.7 || 2.0.1 - 2.9.9", false),
                    arrayOf("2.0.0", "1.2.2 - 1.2.7 || 2.0.1 - 2.9.9", false),
                    arrayOf("3.0.0", "1.2.2 - 1.2.7 || 2.0.1 - 2.9.9", false),
                    arrayOf("1.2.0", "1.2 <1.2.8 || >2.0.0", true),
                    arrayOf("1.2.7", "1.2 <1.2.8 || >2.0.0", true),
                    arrayOf("1.2.7", "1.2 <1.2.8 || >2.0.0", true),
                    arrayOf("2.0.1", "1.2 <1.2.8 || >2.0.0", true),
                    arrayOf("1.1.0", "1.2 <1.2.8 || >2.0.0", false),
                    arrayOf("1.2.9", "1.2 <1.2.8 || >2.0.0", false),
                    arrayOf("2.0.0", "1.2 <1.2.8 || >2.0.0", false)
            )
    }
}