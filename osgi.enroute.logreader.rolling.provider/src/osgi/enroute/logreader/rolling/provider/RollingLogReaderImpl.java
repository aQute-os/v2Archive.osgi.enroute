package osgi.enroute.logreader.rolling.provider;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Stream;

import org.apache.felix.service.command.annotations.GogoCommand;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.log.LogEntry;
import org.osgi.service.log.LogLevel;
import org.osgi.service.log.LogListener;
import org.osgi.service.log.LogReaderService;
import org.osgi.service.log.Logger;
import org.osgi.service.log.LoggerFactory;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

import osgi.enroute.logreader.rolling.provider.RollingLogReaderImpl.Config;

/**
 * Implements a {@link LogReaderService} service that persists {@link LogEntry}s
 * to a disk file and can be configured to perform a file roll over strategy
 * based upon a maximum file size (in MB) policy. The maximum number of
 * persisted log files can also be specified. When the file size threshold has
 * been reached for the current log file a new log file will be created to
 * replace it. After file roll over, the previous log file will be closed and
 * archived according to the maximum number of log files policy parameter.
 * Default behavior is to perform a file roll over each time the service is
 * activated. This behavior can be changed by setting {@code -runkeep=true} in
 * bnd.bnd.
 */
@Designate(ocd = Config.class)
@GogoCommand(scope = "rolling", function = {
	"error", "warn", "info", "debug", "logfiles"
})
@Component(name = "osgi.enroute.logreader.rolling", immediate = true)
public class RollingLogReaderImpl extends Thread implements LogListener {
	private static final int				EXCEPTION_PAUSE_MILLIS	= 1000;
	private static final int				MAX_QUEUE_LENGTH		= 100;
	private static final int				MAX_WAIT_MILLIS			= 2000;
	private static final int				MAX_RETAINED_LOGFILES	= 10;
	private static final int				MAX_FILESIZE_MEGABYTES	= 1;

	private final BlockingQueue<LogEntry>	queue					= new LinkedBlockingQueue<>(MAX_QUEUE_LENGTH);

	private File							root;
	private Config							config;

	@Reference(service = LoggerFactory.class)
	private Logger							log;

	/**
	 * Service Properties for this service instance.
	 */
	@ObjectClassDefinition
	@interface Config {
		String root() default "messages";

		String format() default "%s %8s %s%n";

		int maxLogSizeMb() default MAX_FILESIZE_MEGABYTES;

		int maxRetainedLogs() default MAX_RETAINED_LOGFILES;

		LogLevel level() default LogLevel.DEBUG;
	}

	/**
	 * Called when this service instance has been activated
	 */
	@Activate
	void activate(BundleContext context, Config config) {
		this.config = config;
		File f = new File(config.root());
		if (f.isAbsolute()) {
			root = f;
		} else {
			root = context.getDataFile(config.root());
			if (root == null) {
				throw new IllegalStateException("File system support is not available, cannot create file object");
			}
		}
		root.mkdirs();
		if (!root.isDirectory()) {
			throw new IllegalStateException("Cannot create directory " + root);
		}

		start();
	}

	/**
	 * Called when this service instance has been deactivated
	 */
	@Deactivate
	void deactivate() throws InterruptedException {
		// Interrupt the blocking queue
		// consumer's thread
		interrupt();
		join(MAX_WAIT_MILLIS);
	}

	/**
	 * Listener method called for each LogEntry object created
	 *
	 * @see org.osgi.service.log.LogListener#logged(org.osgi.service.log.LogEntry)
	 * @param entry the LogEntry instance to log
	 */
	@Override
	public void logged(LogEntry entry) {
		if (entry.getLogLevel()
			.compareTo(config.level()) > 0) {
			return;
		}
		queue.offer(entry);
	}

	/**
	 * Overrides the Thread run method
	 *
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		try {
			RandomAccessFile file = rollover();
			long limit = (config.maxLogSizeMb() * (1024 * 1024));
			while (!isInterrupted()) {
				LogEntry entry = queue.take();

				String log = String.format(config.format(), getFormattedDate(true), entry.getLogLevel(),
					entry.getMessage(), entry.getException(), entry.getBundle()
						.getBundleId());

				file.write(log.getBytes(StandardCharsets.UTF_8));

				if (file.length() > limit) {
					file.close();
					file = rollover();
				}
			}
		} catch (InterruptedException e) {
			return;
		} catch (Exception e) {
			try {
				Thread.sleep(EXCEPTION_PAUSE_MILLIS);
			} catch (InterruptedException e1) {
				return;
			}
		}
	}

	/**
	 * Calculates next available log file name and performs a roll over
	 *
	 * @return new log file
	 */
	private RandomAccessFile rollover() throws IOException {
		String name = getFormattedDate(false);
		File f = new File(root, name + ".log");

		RandomAccessFile raf = new RandomAccessFile(f, "rwd");
		raf.seek(raf.length());
		purge();

		return raf;
	}

	/**
	 * Gets the current time stamp suitable for use in log messages
	 *
	 * @param addZoneOffset specifies whether to append the zone offset to the
	 *            time stamp
	 * @return formatted current local date time with optional zone offset
	 */
	private String getFormattedDate(boolean addZoneOffset) {
		if (addZoneOffset) {
			DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSZ");
			return ZonedDateTime.now()
				.format(FORMATTER);
		} else {
			return DateTimeFormatter.ISO_DATE_TIME.format(LocalDateTime.now());
		}
	}

	/**
	 * Deletes persisted log files according to the max retained log files
	 * policy configuration
	 */
	private void purge() {
		Stream.of(root.listFiles())
			.sorted((a, b) -> Long.compare(b.lastModified(), a.lastModified()))
			.skip(config.maxRetainedLogs())
			.forEach(ff -> ff.delete());
	}

	/**
	 * Sets the LogReader for this service instance
	 *
	 * @param lsr the OSGi LogReaderService
	 */
	@Reference
	void setLogReader(LogReaderService lsr) {
		lsr.addLogListener(this);
	}

	void unsetLogReader(LogReaderService lsr) {
		lsr.removeLogListener(this);
	}

	/**
	 * Emits an error message to the log file
	 *
	 * @param args the list of arguments to be used in the logged message
	 */
	public void error(String... args) {
		if (args != null) {
			log.error("{}", Stream.of(args)
				.reduce("", (a, b) -> a + " " + b));
		}
	}

	/**
	 * Emits a warning message to the log file
	 *
	 * @param args the list of arguments to be used in the logged message
	 */
	public void warn(String... args) {
		if (args != null) {
			log.warn("{}", Stream.of(args)
				.reduce("", (a, b) -> a + " " + b));
		}
	}

	/**
	 * Emits an informational message to the log file
	 *
	 * @param args the list of arguments to be used in the logged message
	 */
	public void info(String... args) {
		if (args != null) {
			log.info("{}", Stream.of(args)
				.reduce("", (a, b) -> a + " " + b));
		}
	}

	/**
	 * Emits a debug message to the log file
	 *
	 * @param args the list of arguments to be used in the logged message
	 */
	public void debug(String... args) {
		if (args != null) {
			log.debug("{}", Stream.of(args)
				.reduce("", (a, b) -> a + " " + b));
		}
	}

	/**
	 * Gets the list of persistent log files since last log file
	 * {@link #purge()} was executed
	 *
	 * @return array of log files
	 */
	public File[] logfiles() {
		return (root != null) ? root.listFiles() : null;
	}
}
