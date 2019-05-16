package osgi.enroute.google.angular.capabilities;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.osgi.annotation.bundle.Requirement;

import osgi.enroute.namespace.WebResourceNamespace;

/**
 * A Web Resource that provides Google's Angular JS javascript files.
 */
@Requirement(namespace = WebResourceNamespace.NS, filter = "(&(" + WebResourceNamespace.NS
 + "="
		+ AngularConstants.ANGULAR_WEB_RESOURCE_NAME + ")(version>=" + AngularConstants.ANGULAR_WEB_RESOURCE_VERSION
		+ "))")
@Retention(RetentionPolicy.CLASS)
public @interface RequireAngularWebResource {

	/**
	 * Define the default resource to return
	 * 
	 * @return the list of resources to include
	 */
	String[] resource() default {"angular.js", "angular-route.js"};

	/**
	 * Define the priority of this web resources. The higher the priority, the
	 * earlier it is loaded when all web resources are combined.
	 * 
	 * @return the priority
	 */
	int priority() default 1000;
}
