package osgi.enroute.base.authorization.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.osgi.service.useradmin.Authorization;
import org.osgi.service.useradmin.Group;
import org.osgi.service.useradmin.Role;
import org.osgi.service.useradmin.User;
import org.osgi.service.useradmin.UserAdmin;

import aQute.launchpad.Launchpad;
import aQute.launchpad.LaunchpadBuilder;
import aQute.launchpad.Service;
import aQute.launchpad.junit.LaunchpadRunner;
import osgi.enroute.authorization.api.Authority;
import osgi.enroute.authorization.api.AuthorityAdmin;
import osgi.enroute.authorization.api.SecurityVerifier;

@SuppressWarnings("resource")
@RunWith(LaunchpadRunner.class)
@Ignore
public class AuthorizationTest {

	static LaunchpadBuilder	builder	= new LaunchpadBuilder().bndrun("test.bndrun");

	@Service
	Authority		authority;
	@Service
	AuthorityAdmin	admin;
	@Service
	UserAdmin		userAdmin;

	@Service
	Launchpad		lp;

	/**
	 * Very little we can actually test since this depends on external state and
	 * secrets ...
	 */
	interface SomeDomain {
		boolean admin();

		boolean admin(String arg);

		boolean nothing(String arg);
	}

	@Test
	public void testAuthorization() throws Exception {
		assertNotNull(admin);
		assertNotNull(authority);
		assertNotNull(userAdmin);

		User peter = (User) userAdmin.createRole("peter", Role.USER);
		Group admin = (Group) userAdmin.createRole("admin", Role.GROUP);
		Group adminDomain = (Group) userAdmin.createRole("admin;x*", Role.GROUP);

		admin.addMember(peter);
		adminDomain.addMember(peter);

		Authorization auth = userAdmin.getAuthorization(peter);
		List<String> roles = Arrays.asList(auth.getRoles());
		assertNotNull(roles);
		assertEquals(3, roles.size());

		assertTrue(roles.contains("admin"));
		assertTrue(roles.contains("admin;x*"));

		this.admin.call("peter", new Callable<Integer>() {

			@Override
			public Integer call() throws Exception {

				assertEquals("peter", authority.getUserId());

				// By hand verifier

				authority.checkPermission("admin");
				authority.checkPermission("admin", "xyz");

				assertFalse(authority.hasPermission("admin", "abc"));
				assertFalse(authority.hasPermission("nothing", "abc"));

				// Security verifier

				SomeDomain security = SecurityVerifier.createVerifier(SomeDomain.class, authority);

				assertTrue(security.admin());
				assertTrue(security.admin("xyz"));
				assertFalse(security.admin("abc"));
				assertFalse(security.nothing("abc"));

				return null;
			}
		});
	}
}
