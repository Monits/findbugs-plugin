package samples.jsr305.nullness;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.annotation.concurrent.GuardedBy;

import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.Subscription;
import rx.exceptions.Exceptions;
import rx.functions.Action0;
import rx.observers.SerializedSubscriber;
import rx.schedulers.Schedulers;

/**
 * Rx Operator: Store items in a buffer, emits items if the buffer is full or by timeout.
 * If the buffer is full the operator will emit the items.
 * If the timeout is consumed and the buffer is not empty the items will be emitted.
 * onComplete emits remaining items.
 * onError will not emit any remaining items.
 */
public final class ComplexGenerics<T> implements Observable.Operator<List<T>, T> {
    private final long timeout;
    private final TimeUnit unit;
    private final int count;
    private final Scheduler scheduler;
    private Subscription timeoutSubscription;

    /**
     * @param count   the maximum size of the buffer. Once a buffer reaches this size, it is emitted
     * @param timeout the amount of time all chunks must be actively collect values before being emitted
     * @param unit    the {@link TimeUnit} defining the unit of time for the timeout
     */
    public ComplexGenerics(final int count, final long timeout, final TimeUnit unit) {
        this(count, timeout, unit, Schedulers.computation());
    }

    /**
     * @param count   the maximum size of the buffer. Once a buffer reaches this size, it is emitted
     * @param timeout the amount of time all chunks must be actively collect values before being emitted
     * @param unit    the {@link TimeUnit} defining the unit of time for the timeout
     * @param scheduler the {@link Scheduler} to use for timeout
     */
    public ComplexGenerics(final int count, final long timeout, final TimeUnit unit, final Scheduler scheduler) {
        this.count = count;
        this.timeout = timeout >= 0 ? timeout : 0;
        this.unit = unit;
        this.scheduler = scheduler;
    }

    @Override
    public Subscriber<? super T> call(final Subscriber<? super List<T>> child) {
        final Scheduler.Worker inner = scheduler.createWorker();
        final SerializedSubscriber<List<T>> serialized = new SerializedSubscriber<>(child);
        final BufferSubscriber bufferSubscriber = new BufferSubscriber(serialized, inner);
        bufferSubscriber.add(inner);
        child.add(bufferSubscriber);
        return bufferSubscriber;
    }

    private final class BufferSubscriber extends Subscriber<T> {
        /* default */ final Subscriber<? super List<T>> child;
        /* default */ final Scheduler.Worker inner;
        /* default */ List<T> chunk;
        /* default */ boolean done;

        BufferSubscriber(final Subscriber<? super List<T>> child, final Scheduler.Worker inner) {
            this.child = child;
            this.inner = inner;
            this.chunk = new ArrayList<>();
        }

        @Override
        public void onNext(final T t) {
            synchronized (this) {
                unsubscribeTimeout();
                if (done) {
                    return;
                }
                chunk.add(t);
                if (chunk.size() == count) {
                    emit();
                } else {
                    scheduleTimeout();
                }
            }
        }

        @Override
        public void onError(final Throwable e) {
            synchronized (this) {
                unsubscribeTimeout();
                done = true;
                chunk = null;
                child.onError(e);
                unsubscribe();
            }
        }

        @Override
        public void onCompleted() {
            synchronized (this) {
                unsubscribeTimeout();
                emit();
                done = true;
                chunk = null;
                child.onCompleted();
                unsubscribe();
            }
        }

        private void unsubscribeTimeout() {
            if (timeoutSubscription != null && !timeoutSubscription.isUnsubscribed()) {
                timeoutSubscription.unsubscribe();
            }
        }

        private void scheduleTimeout() {
            timeoutSubscription = inner.schedule(new Action0() {
                @Override
                public void call() {
                    synchronized (this) {
                        emit();
                    }
                }
            }, timeout, unit);
        }

        @GuardedBy("this")
        private void emit() {
            if (done) {
                return;
            }
            final List<T> toEmit;
            toEmit = chunk;
            chunk = new ArrayList<>();
            if (toEmit != null && !toEmit.isEmpty()) {
                try {
                    child.onNext(toEmit);
                } catch (final Throwable t) {
                    Exceptions.throwOrReport(t, this);
                }
            }
        }

        @Override
        public String toString() {
            return "BufferSubscriber{"
                    + "child=" + child
                    + ", inner=" + inner
                    + ", chunk=" + chunk
                    + ", done=" + done
                    + '}';
        }
    }

    @Override
    public String toString() {
        return "BufferTimeoutOperator{"
                + "timeout=" + timeout
                + ", unit=" + unit
                + ", count=" + count
                + ", scheduler=" + scheduler
                + ", timeoutSubscription=" + timeoutSubscription
                + '}';
    }
}