package osgi.enroute.web.simple.test;

import org.osgi.service.configurator.annotations.RequireConfigurator;

import osgi.enroute.webserver.capabilities.RequireWebServerExtender;

@RequireWebServerExtender
@RequireConfigurator
public interface BundleConfig {

}
