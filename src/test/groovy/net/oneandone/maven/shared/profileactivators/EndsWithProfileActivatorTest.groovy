package net.oneandone.maven.shared.profileactivators

import org.apache.maven.model.Activation
import org.apache.maven.model.ActivationProperty
import org.apache.maven.model.Profile
import org.apache.maven.model.profile.ProfileActivationContext
import spock.lang.Specification
import spock.lang.Subject

class EndsWithProfileActivatorTest extends Specification {

    def 'Check a property ends with a value'() {
        given:
        def activation = new Activation()
        activation.property = new ActivationProperty()
        activation.property.name = 'bar'
        activation.property.value = 'endswith:foo'
        def profile = Mock(Profile)
        profile.activation >> activation
        def profileActivationContext = Mock(ProfileActivationContext)
        profileActivationContext.systemProperties >> ['bar': 'foofoo']
        @Subject
        def sut = new EndsWithProfileActivator()
        expect:
        sut.isActive(profile, profileActivationContext, null)
    }

}