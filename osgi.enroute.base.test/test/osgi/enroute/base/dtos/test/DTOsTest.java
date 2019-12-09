package osgi.enroute.base.dtos.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.osgi.dto.DTO;

import aQute.launchpad.Launchpad;
import aQute.launchpad.LaunchpadBuilder;
import aQute.launchpad.Service;
import aQute.launchpad.junit.LaunchpadRunner;
import osgi.enroute.dto.api.DTOs;
import osgi.enroute.dto.api.DTOs.Difference;
import osgi.enroute.dto.api.TypeReference;

@SuppressWarnings("resource")
@RunWith(LaunchpadRunner.class)
public class DTOsTest {
	static LaunchpadBuilder	builder	= new LaunchpadBuilder().bndrun("test.bndrun");

	@Service
	private DTOs dtos;
	@Service
	Launchpad				lp;

	/*
	 * Simple conversion
	 */

	@Test
	public void testSimple() throws Exception {

		assertEquals(100D, dtos.convert("100")
			.to(double.class), 0.1D);
		assertEquals(10D, dtos.convert(10f)
			.to(double.class), 0.1D);
		assertEquals(100D, dtos.convert(100L)
			.to(double.class), 0.1D);

		assertEquals(Arrays.asList(100F), dtos.convert(100L)
			.to(new TypeReference<List<Float>>() {}));

		long[] expected = new long[] {
			0x40L, 0x41L, 0x42L
		};
		byte[] source = "@AB".getBytes();
		long[] result = dtos.convert(source)
			.to(long[].class);

		assertTrue(Arrays.equals(expected, result));
	}

	/*
	 * Show Map -> Interface
	 */
	enum Option {
		bar,
		don,
		zun
	}

	interface FooMap {
		short port();

		String host();

		Set<Option> options();
	}

	@Test
	public void testInterfaceAsMap() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();

		map.put("port", 10);
		map.put("host", "localhost");
		map.put("options", Arrays.asList("bar", "don", "zun"));

		FooMap foomap = dtos.convert(map)
			.to(FooMap.class);

		assertEquals((short) 10, foomap.port());
		assertEquals("localhost", foomap.host());
		assertEquals(EnumSet.allOf(Option.class), foomap.options());
	}

	/*
	 * Show DTO to map
	 */

	public static class MyData extends DTO {
		public short	port;
		public String	host;
		public Option[]	options;
	}

	@Test
	public void testDtoAsMap() throws Exception {
		MyData m = new MyData();
		m.port = 20;
		m.host = "example.com";
		m.options = new Option[] {
			Option.bar, Option.don, Option.zun
		};

		Map<String, Object> map = dtos.asMap(m);

		assertEquals(Arrays.asList("host", "options", "port"), new ArrayList<String>(map.keySet()));
		assertEquals((short) 20, map.get("port"));
		assertEquals("example.com", map.get("host"));
	}

	/*
	 * Show JSON
	 */

	@Test
	public void testJSON() throws Exception {
		MyData m = new MyData();
		m.port = 20;
		m.host = "example.com";
		m.options = new Option[] {
			Option.bar, Option.don, Option.zun
		};

		String json = dtos.encoder(m)
			.put();
		assertEquals("{\"host\":\"example.com\",\"options\":[\"bar\",\"don\",\"zun\"],\"port\":20}", json);
	}

	@Test
	public void testDiff() throws Exception {
		MyData source = new MyData();
		source.port = 20;
		source.host = "example.com";
		source.options = new Option[] {
			Option.bar, Option.don, Option.zun
		};

		MyData copy = dtos.deepCopy(source);

		assertFalse(source == copy);
		assertTrue(dtos.equals(source, copy));

		List<Difference> diff = dtos.diff(source, copy);
		assertEquals(0, diff.size());

		copy.port = 10;
		diff = dtos.diff(source, copy);
		assertEquals(1, diff.size());
		assertEquals("port", diff.get(0).path[0]);
	}
}
