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

import org.apache.maven.shared.release.versions.DefaultVersionInfo
import org.junit.AssumptionViolatedException
import spock.lang.Ignore
import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

class ArtifactoryVersionPolicyIT extends Specification implements AbstractVersionPolicyTrait {

    @Subject
    def subjectUnderTest = new ArtifactoryVersionPolicy(createMavenProject(), null);

    def 'New foss-parent version should always be bigger then 1.5.6'() {
        given:
        checkRepoJfrogIsReachable()
        def oldVersionInfo = new DefaultVersionInfo("1.5.6");

        when:
        def version = subjectUnderTest.getReleaseVersion(VPR_DOES_NOT_MATTER).version;
        def newVersionInfo = new DefaultVersionInfo(version);

        then:
        ! newVersionInfo.isSnapshot()
        newVersionInfo > oldVersionInfo
    }

    @Unroll('Handles new artifacts #snapshotVersion -> #releaseVersion')
    def 'No version found at Artifactory, i.e. first release'(String snapshotVersion, String releaseVersion) {
        given:
        checkRepoJfrogIsReachable()
        subjectUnderTest.mavenProject.artifactId = 'unknown-artifact-id-i-do-not-exist'
        subjectUnderTest.mavenProject.version = snapshotVersion

        when:
        def version = subjectUnderTest.getReleaseVersion(VPR_DOES_NOT_MATTER).version
        def newSnapshotVersion = subjectUnderTest.getDevelopmentVersion(VPR_DOES_NOT_MATTER).version
        def newVersionInfo = new DefaultVersionInfo(version);

        then:
        newSnapshotVersion == subjectUnderTest.mavenProject.version
        ! newVersionInfo.isSnapshot()
        newVersionInfo.releaseVersionString == releaseVersion

        where:
        snapshotVersion | releaseVersion
        '0.1-SNAPSHOT'  | '0.1.0'
        '1.0-SNAPSHOT'  | '1.0.0'
    }

    static void checkRepoJfrogIsReachable() {
        try {
            final URLConnection urlConnection = new URL('https://repo.jfrog.org/artifactory').openConnection()
            try {
                urlConnection.setConnectTimeout(5000)
                urlConnection.getInputStream().close()
            } catch (IOException ex) {
                throw new AssumptionViolatedException("Could not reach " + urlConnection.getURL(), ex)
            }
        } catch (MalformedURLException ex) {
            throw new AssumptionViolatedException("Malformed URL", ex)
        }
    }
}
