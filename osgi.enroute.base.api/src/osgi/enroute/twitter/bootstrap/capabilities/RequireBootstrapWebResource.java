package osgi.enroute.twitter.bootstrap.capabilities;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.osgi.annotation.bundle.Requirement;

import osgi.enroute.namespace.WebResourceNamespace;

/**
 * A Web Resource that provides Twitter's Bootstrap files.
 */
@Requirement(namespace = WebResourceNamespace.NS, filter = "(&(" + WebResourceNamespace.NS
		+ "=" + BootstrapConstants.BOOTSTRAP_WEB_RESOURCE_NAME + ")(version>="
		+ BootstrapConstants.BOOTSTRAP_WEB_RESOURCE_VERSION + "))")
@Retention(RetentionPolicy.CLASS)
public @interface RequireBootstrapWebResource {

	/**
	 * Define the default resource to return
	 * 
	 * @return the list of resources to include
	 */
	String[] resource() default "bootstrap.css";

	/**
	 * Define the priority of this web resources. The higher the priority, the
	 * earlier it is loaded when all web resources are combined.
	 * 
	 * @return the priority
	 */
	int priority() default 1000;
}
