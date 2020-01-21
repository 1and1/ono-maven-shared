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
package net.oneandone.maven.shared.versionpolicies;

import java.util.Properties;

import org.apache.maven.project.MavenProject;
import org.apache.maven.shared.release.policy.PolicyException;
import org.apache.maven.shared.release.policy.version.VersionPolicy;
import org.apache.maven.shared.release.policy.version.VersionPolicyRequest;
import org.apache.maven.shared.release.policy.version.VersionPolicyResult;
import org.apache.maven.shared.release.versions.DefaultVersionInfo;
import org.apache.maven.shared.release.versions.VersionParseException;
import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;
import org.codehaus.plexus.interpolation.InterpolationException;
import org.codehaus.plexus.interpolation.Interpolator;
import org.codehaus.plexus.interpolation.PropertiesBasedValueSource;
import org.codehaus.plexus.interpolation.StringSearchInterpolator;

import static java.lang.System.getProperties;

/**
 * A {@link VersionPolicy} implementation adds a suffix in to the current version to generate the next releaseVersion
 */
@Component(
        role = VersionPolicy.class,
        hint = "ONOSuffixVersionPolicy",
        description = "Adds a suffix in to the current version to generate the next releaseVersion."
)
public class SuffixVersionPolicy implements VersionPolicy {

    static final String SUFFIX_IDENTIFIER = "version-suffix";

    @Requirement
    MavenProject mavenProject;

    @Override
    public VersionPolicyResult getReleaseVersion(VersionPolicyRequest request) throws PolicyException, VersionParseException {
        Properties projectProperties = mavenProject.getProperties();
        if(!projectProperties.containsKey(SUFFIX_IDENTIFIER)){
            throw new VersionParseException("Please provide the version-suffix");
        }
        final DefaultVersionInfo currentSnapshot = new DefaultVersionInfo(mavenProject.getVersion());
        final VersionPolicyResult result = new VersionPolicyResult();
        String buildNumberSuffix = projectProperties.getProperty(SUFFIX_IDENTIFIER);

        final Interpolator interpolator = new StringSearchInterpolator("$\\{", "}");
        interpolator.addValueSource(new PropertiesBasedValueSource(getProperties()));
        interpolator.addValueSource(new PropertiesBasedValueSource(projectProperties));
        try {
            final String buildNumberSuffixValue = interpolator.interpolate(buildNumberSuffix);
            result.setVersion(currentSnapshot.getReleaseVersionString().concat(buildNumberSuffixValue));
        }catch (InterpolationException e){
            throw new PolicyException("Unable to determine the released version suffix.",e);
        }
        return result;
    }

    @Override
    public VersionPolicyResult getDevelopmentVersion(VersionPolicyRequest request) throws PolicyException, VersionParseException {
        final VersionPolicyResult versionPolicyResult = new VersionPolicyResult();
        versionPolicyResult.setVersion(mavenProject.getVersion());
        return versionPolicyResult;
    }
}
