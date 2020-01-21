/**
 * Copyright 1&1 Internet AG, https://github.com/1and1/
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.oneandone.maven.shared.versionpolicies

import org.apache.maven.shared.release.versions.VersionParseException
import spock.lang.Specification

public class SuffixVersionPolicyTest extends Specification implements AbstractVersionPolicyTrait {

    def 'Has a default constructor used with injection in Maven'() {
        when:
        new SuffixVersionPolicy()

        then:
        noExceptionThrown()
    }

    def 'Should throw exception if suffix not set'() {
        setup:
        def subjectUnderTest = new SuffixVersionPolicy();
        subjectUnderTest.mavenProject = createMavenProject();

        when :
        subjectUnderTest.getReleaseVersion(VPR_DOES_NOT_MATTER)

        then:
        thrown VersionParseException
    }

    def 'Should generate suffix'() {
        setup :
        def subjectUnderTest = new SuffixVersionPolicy();
        def mavenProject = createMavenProject();
        mavenProject.properties.setProperty(SuffixVersionPolicy.SUFFIX_IDENTIFIER,".suffix")
        subjectUnderTest.mavenProject = mavenProject

        expect:
        subjectUnderTest.getReleaseVersion(VPR_DOES_NOT_MATTER).version == '1.suffix'
    }

    def 'Should interpolate suffix using project properties'() {
        setup :
        def subjectUnderTest = new SuffixVersionPolicy();
        def mavenProject = createMavenProject();
        mavenProject.properties.setProperty("suffixVal","myval")
        mavenProject.properties.setProperty(SuffixVersionPolicy.SUFFIX_IDENTIFIER,'.$\\{suffixVal}')
        subjectUnderTest.mavenProject = mavenProject

        expect:
        subjectUnderTest.getReleaseVersion(VPR_DOES_NOT_MATTER).version == '1.myval'
    }

    def 'Assures development version is SNAPSHOT'() {
        setup :
        def subjectUnderTest = new SuffixVersionPolicy();
        def mavenProject = createMavenProject();
        mavenProject.properties.setProperty(SuffixVersionPolicy.SUFFIX_IDENTIFIER,".suffix")
        subjectUnderTest.mavenProject = mavenProject

        expect:
        subjectUnderTest.getDevelopmentVersion(VPR_DOES_NOT_MATTER).version == '1-SNAPSHOT'
    }

}
