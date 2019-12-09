package osgi.enroute.configurer.simple.provider;

import org.junit.Ignore;
import org.junit.runner.RunWith;

import aQute.launchpad.junit.LaunchpadRunner;

/*
 *
 *
 *
 */

@SuppressWarnings("deprecation")
@RunWith(LaunchpadRunner.class)
@Ignore
public class ConfigurerTest {
	//
	// private static final long DELAY = 1000;
	// static LaunchpadBuilder builder = new
	// LaunchpadBuilder().bndrun("bnd.bnd");
	//
	// @Service
	// ConfigurationAdmin cm;
	//
	// @Service
	// Launchpad lp;
	//
	// @Before
	// public void before() throws Exception {
	// System.setProperty("enRoute.configurer.extra",
	// "[\n" + " { \n" + " \"service.pid\": \"system\",\n" + " \"data\":
	// \"data\"\n" + " }\n" + "]\n" + "");
	// }
	//
	// @After
	// public void after() throws Exception {
	// cfg.deactivate();
	// }
	//
	// @Test
	// public void testBasic() throws Exception {
	// Bundle bundle = lp.bundle()
	// .addConfiguration(getClass().getResource("data/basic.json"))
	// .install();
	//
	// bundle.start();
	//
	// Thread.sleep(DELAY);
	// Configuration configuration = cm.getConfiguration("basic");
	// assertThat(configuration.getProperties()
	// .get("data")).isEqualTo("data");
	// bundle.uninstall();
	// }

	// @Test
	// public void testOverride() throws Exception {
	// Configuration override = cm.getConfiguration("override", "?");
	// Dictionary<String, Object> dict = new Hashtable<>();
	// dict.put("data", "original");
	// override.update(dict);
	//
	// Bundle bundle = lp.bundle()
	// .addResource("configuration/configuration.json",
	// getClass().getResource("data/override.json")))
	// .install();
	//
	// bundle.start();
	//
	// Thread.sleep(DELAY);
	// Configuration configuration = cm.getConfiguration("override");
	// assertThat(configuration.getProperties()
	// .get("data"), is("data"));
	// bundle.uninstall();
	// }
	//
	// @Test
	// public void testPrecious() throws Exception {
	// Configuration override = cm.getConfiguration("precious", "?");
	// Dictionary<String, Object> dict = new Hashtable<>();
	// dict.put("a", "original");
	// dict.put("b", "original");
	// override.update(dict);
	//
	// Bundle bundle = juf.bundle()
	// .addResource("configuration/configuration.json",
	// Resource.fromURL(getClass().getResource("data/precious.json")))
	// .install();
	//
	// bundle.start();
	//
	// Thread.sleep(DELAY);
	// Configuration configuration = cm.getConfiguration("precious");
	// assertThat(configuration.getProperties()
	// .get("a"), is("original"));
	// assertThat(configuration.getProperties()
	// .get("b"), is("B"));
	//
	// bundle.uninstall();
	// }
	//
	// @Test
	// public void testNoOverride() throws Exception {
	// Configuration override = cm.getConfiguration("nooverride", "?");
	// Dictionary<String, Object> dict = new Hashtable<>();
	// dict.put("data", "original");
	// override.update(dict);
	//
	// Bundle bundle = juf.bundle()
	// .addResource("configuration/configuration.json",
	// Resource.fromURL(getClass().getResource("data/nooverride.json")))
	// .install();
	//
	// bundle.start();
	//
	// Thread.sleep(DELAY);
	// Configuration configuration = cm.getConfiguration("nooverride");
	// assertThat(configuration.getProperties()
	// .get("data"), is("original"));
	// bundle.uninstall();
	// }
	//
	// @Test
	// public void testSystemPropertyExtra() throws Exception {
	// Thread.sleep(DELAY);
	// Configuration configuration = cm.getConfiguration("system");
	// assertThat(configuration.getProperties()
	// .get("data"), is("data"));
	// }
	//
	// @Test
	// public void testMacros() throws Exception {
	// Bundle bundle = juf.bundle()
	// .addResource("configuration/configuration.json",
	// Resource.fromURL(getClass().getResource("data/macro.json")))
	// .install();
	// bundle.start();
	// Thread.sleep(DELAY);
	// Dictionary<String, Object> configuration = cm.getConfiguration("macro",
	// "?")
	// .getProperties();
	// assertThat(configuration.get("bundleid"), is(bundle.getBundleId() + ""));
	// assertThat(configuration.get("def"), is("--"));
	// assertThat((String) configuration.get("location"), startsWith("generated
	// test-"));
	//
	// bundle.uninstall();
	// }
	//
	// @Test
	// public void testResource() throws Exception {
	// Bundle bundle = juf.bundle()
	// .addResource("configuration/configuration.json",
	// Resource.fromURL(getClass().getResource("data/resource.json")))
	// .addResource("foo.bar", new
	// EmbeddedResource("FOO".getBytes(StandardCharsets.UTF_8), 0))
	// .install();
	// bundle.start();
	// Thread.sleep(DELAY);
	//
	// Dictionary<String, Object> configuration =
	// cm.getConfiguration("resource", "?")
	// .getProperties();
	// assertNotNull("Configuration must exist", configuration);
	// String path = (String) configuration.get("resource");
	// File file = new File(path);
	// assertTrue("file must exist", file.isFile());
	// String content = IO.collect(file);
	//
	// assertThat(content, is("FOO"));
	//
	// bundle.uninstall();
	// }
	//
	// @Test
	// public void testProfile() throws Exception {
	// Bundle bundle = juf.bundle()
	// .addResource("configuration/configuration.json",
	// Resource.fromURL(getClass().getResource("data/profile.json")))
	// .addResource("foo.bar", new
	// EmbeddedResource("FOO".getBytes(StandardCharsets.UTF_8), 0))
	// .install();
	// bundle.start();
	// Thread.sleep(DELAY);
	//
	// Configuration configuration = cm.getConfiguration("profile", "?");
	// Dictionary<String, Object> dict = configuration.getProperties();
	// assertThat(dict.get("foo"), is("FOO"));
	//
	// bundle.stop();
	//
	// Map<String, Object> properties = new HashMap<>();
	// properties.put("launcher.arguments", new String[] {
	// "--profile", "bar"
	// });
	// cfg.setLauncher(null, properties);
	//
	// configuration.update(new Hashtable<>());
	//
	// Thread.sleep(DELAY);
	// bundle.start();
	// Thread.sleep(DELAY);
	//
	// configuration = cm.getConfiguration("profile", "?");
	// dict = configuration.getProperties();
	// assertThat(dict.get("foo"), is("BAR"));
	//
	// bundle.uninstall();
	// }
}
