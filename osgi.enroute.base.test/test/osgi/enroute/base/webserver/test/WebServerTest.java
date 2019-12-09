package osgi.enroute.base.webserver.test;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleException;
import org.osgi.service.http.HttpService;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.ScriptResult;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import aQute.bnd.osgi.Builder;
import aQute.bnd.osgi.EmbeddedResource;
import aQute.bnd.osgi.Jar;
import aQute.bnd.osgi.JarResource;
import aQute.launchpad.Launchpad;
import aQute.launchpad.LaunchpadBuilder;
import aQute.launchpad.Service;
import aQute.launchpad.junit.LaunchpadRunner;
import osgi.enroute.webserver.capabilities.RequireWebServerExtender;

@SuppressWarnings("resource")
@RequireWebServerExtender
@RunWith(LaunchpadRunner.class)
@Ignore
public class WebServerTest {
	private static final String	HTML_BODY_TEST_BODY_HTML	= "<html><body>test</body></html>";

	static LaunchpadBuilder		builder						= new LaunchpadBuilder().bndrun("test.bndrun")
		.snapshot()
		.debug()
		.export("com.gargoylesoftware*")
		.export("org.apache.*");

	@Service
	HttpService s;
	@Service
	Launchpad	lp;

	@Test
	public void testWebResource() throws Exception {
		try (Builder b = new Builder()) {
			b.setProperties(new File("resources/webresources.bnd"));
			Jar jar = b.build();
			jar.getManifest()
				.write(System.out);
			try (JarResource jarResource = new JarResource(jar)) {
				Bundle bundle = lp.getBundleContext()
					.installBundle("test", jarResource.openInputStream());
				try {
					bundle.start();

					try (WebClient webClient = new WebClient(BrowserVersion.FIREFOX_60);) {
						webClient.getOptions()
							.setTimeout(0);
						final HtmlPage page = webClient.getPage("http://localhost:8080/test.html");

						//
						// Test if we downloaded our own script in web/test.js
						//

						ScriptResult result = page.executeJavaScript("foo");
						assertEquals("Yes, we can", result.getJavaScriptResult());
					}
				} finally {
					bundle.uninstall();
				}
			}
		}
	}

	@Test
	public void testSimple() throws BundleException, Exception {
		Jar jar = new Jar("test");
		jar.putResource("static/test.html", new EmbeddedResource(HTML_BODY_TEST_BODY_HTML.getBytes(), 0));
		try (JarResource jarResource = new JarResource(jar)) {
			Bundle b = lp.getBundleContext()
				.installBundle("test", jarResource.openInputStream());
			try {
				b.start();

				try (WebClient webClient = new WebClient()) {
					final HtmlPage page = webClient.getPage("http://localhost:8080/test.html");
					assertEquals("test", page.asText());
				}
			} finally {
				b.uninstall();
			}
		}
	}
}
