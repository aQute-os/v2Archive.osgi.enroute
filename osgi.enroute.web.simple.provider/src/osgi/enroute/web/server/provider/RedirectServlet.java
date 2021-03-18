package osgi.enroute.web.server.provider;

import java.net.URL;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.util.tracker.BundleTracker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import aQute.lib.io.IO;
import osgi.enroute.servlet.api.ConditionalServlet;
import osgi.enroute.web.server.exceptions.Redirect302Exception;

@Component(service = {
	ConditionalServlet.class
}, immediate = true, property = {
	"service.ranking:Integer=1000", "name=" + RedirectServlet.NAME,
}, name = RedirectServlet.NAME, configurationPolicy = ConfigurationPolicy.OPTIONAL)
public class RedirectServlet implements ConditionalServlet {
	final static Logger			logger			= LoggerFactory.getLogger(RedirectServlet.class);
	static final String			NAME			= "osgi.enroute.simple.redirect";

	/**
	 * Must start with a "/".
	 */
	private String				redirect		= "/index.html";

	final BundleTracker<URL>	bundles;
	boolean						neverReported	= true;

	@interface Config {
		String redirect();
	}

	@Activate
	public RedirectServlet(Config config, BundleContext context) throws Exception {
		bundles = new BundleTracker<URL>(context, Bundle.ACTIVE + Bundle.STARTING, null) {
			@Override
			public URL addingBundle(Bundle bundle, BundleEvent event) {
				String root = bundle.getHeaders()
					.get("Web-Root");
				if (root == null)
					return null;

				return bundle.getResource(root);
			}
		};
		bundles.open();

		if (config.redirect() != null)
			redirect = config.redirect();

		if (!redirect.startsWith("/"))
			redirect = "/" + redirect;
	}

	@Deactivate
	void close() {
		bundles.close();
	}

	@Override
	public boolean doConditionalService(HttpServletRequest rq, HttpServletResponse rsp) throws Exception {
		String path = rq.getRequestURI();

		Collection<URL> tracked = bundles.getTracked()
			.values();
		if (path != null && path.equals("/") && !tracked.isEmpty()) {

			if (tracked.size() > 1 && neverReported) {
				logger.warn("There are multiple web roots defined {}", tracked);
				neverReported = false;
			}

			URL first = tracked.iterator()
				.next();

			IO.copy(first.openStream(), rsp.getOutputStream());
			rsp.setStatus(200);
			return true;
		}

		// Redirect is disabled by configuring with an empty string.
		// Since the value will be prepended with "/", it means that when the
		// value is "/", no action is taken.
		if ("/".equals(redirect))
			return false;

		try {

			if (path == null || path.isEmpty() || path.equals("/")) {
				throw new Redirect302Exception(redirect);
			} else if (path.startsWith("/"))
				path = path.substring(1);

			if (path.endsWith("/")) {
				path = path.substring(0, path.length() - 1);
				throw new Redirect302Exception("/" + path + redirect);
			}

			return false;
		} catch (Redirect302Exception e) {
			rsp.setHeader("Location", e.getPath());
			rsp.sendRedirect(e.getPath());
			return true;
		}
	}

}
