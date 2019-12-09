package osgi.enroute.scheduler.simple.provider;

import java.io.Closeable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.Callable;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.util.promise.Promise;
import org.osgi.util.promise.Success;

import osgi.enroute.scheduler.api.CancellablePromise;
import osgi.enroute.scheduler.api.CronJob;
import osgi.enroute.scheduler.api.Scheduler;

@Component(servicefactory = true)
public class DelegatedScheduler implements Scheduler {

	private InternalSchedulerImpl	delegate;
	private Set<Closeable>			closeables	= new HashSet<>();

	@SuppressWarnings("resource")
	@Deactivate
	void close() {
		while (true) {
			Closeable c;
			synchronized (closeables) {
				if (closeables.isEmpty())
					return;
				Iterator<Closeable> iterator = closeables.iterator();
				c = iterator.next();
				iterator.remove();
			}

			try {
				c.close();
			} catch (Throwable t) {
				// ignore
			}
		}
	}

	@Override
	public CancellablePromise<Instant> after(long ms) {
		return delegate.after(ms);
	}

	@Override
	public <T> CancellablePromise<T> after(Callable<T> callable, long ms) {
		return add(delegate.after(callable, ms));
	}

	public <T> Success<T, T> delay(long ms) {
		return delegate.delay(ms);
	}

	@Override
	public <T> CancellablePromise<T> before(Promise<T> promise, long timeout) {
		return add(delegate.before(promise, timeout));
	}

	@Override
	public Closeable schedule(RunnableWithException r, long first, long... ms) {
		return addCloseable(delegate.schedule(r, first, ms));
	}

	@Override
	public Closeable schedule(RunnableWithException r, String cronExpression) throws Exception {
		return addCloseable(delegate.schedule(r, cronExpression));
	}

	@Override
	public <T> Closeable schedule(Class<T> type, CronJob<T> job, String cronExpression) throws Exception {
		return addCloseable(delegate.schedule(type, job, cronExpression));
	}

	@Override
	public CancellablePromise<Instant> at(long epochTime) {
		return add(delegate.at(epochTime));
	}

	@Override
	public <T> CancellablePromise<T> at(Callable<T> callable, long epochTime) {
		return add(delegate.at(callable, epochTime));
	}

	private Closeable addCloseable(Closeable t) {
		synchronized (closeables) {
			closeables.add(t);
		}
		return () -> {
			synchronized (closeables) {
				if (!closeables.remove(t))
					return;
			}
			t.close();
		};
	}

	private <T> CancellablePromiseImpl<T> add(CancellablePromiseImpl<T> p) {
		addCloseable(p);
		p.onResolve(() -> closeables.remove(p));
		return p;
	}

	@Reference
	void setDelegate(InternalSchedulerImpl delegate) {
		this.delegate = delegate;
	}
}
