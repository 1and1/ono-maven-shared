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

import spock.lang.Specification
import spock.lang.Subject

class ScmVersionPolicyTest extends Specification implements AbstractVersionPolicyTrait {
    @Subject
    def subjectUnderTest = new ScmVersionPolicy();


    def testGetReleaseVersion() {
        expect:
        subjectUnderTest.getReleaseVersion(VPR_DOES_NOT_MATTER) is null
    }

    def testGetDevelopmentVersion() throws Exception {
        expect:
        subjectUnderTest.getDevelopmentVersion(VPR_DOES_NOT_MATTER) is null
    }
}
