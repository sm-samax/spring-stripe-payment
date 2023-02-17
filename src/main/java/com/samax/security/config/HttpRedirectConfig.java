package com.samax.security.config;

import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.samax.security.constants.SecurityConstants;

@Configuration
public class HttpRedirectConfig {
	
	@Bean
	public TomcatServletWebServerFactory servletContainer() {
		TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory( ) {
			@Override
			protected void postProcessContext(Context context) {
				SecurityConstraint securityConstraint = new SecurityConstraint();
				securityConstraint.setUserConstraint(SecurityConstants.USER_CONSTRAINT);
				SecurityCollection collection = new SecurityCollection();
				collection.addPattern("/*");
				securityConstraint.addCollection(collection);
				context.addConstraint(securityConstraint);
			}
		};
		
		tomcat.addAdditionalTomcatConnectors(redirectConnector());
		return tomcat;
	}
	
	private Connector redirectConnector() {
		Connector connector = new Connector(TomcatServletWebServerFactory.DEFAULT_PROTOCOL);
		connector.setScheme(SecurityConstants.HTTP);
		connector.setPort(SecurityConstants.HTTP_PORT);
		connector.setRedirectPort(SecurityConstants.HTTPS_PORT);
		return connector;
	}
}
