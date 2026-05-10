package sample.common.util;

import com.github.benmanes.caffeine.cache.AsyncLoadingCache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.function.Function;
import java.util.function.Supplier;

public class CachingUtils {
    private static final String FIXED_KEY = "FIXED_KEY";

    private CachingUtils() {
    }

    public static <T> Function<String, T> of(Duration duration, Function<String, T> fn) {
        final LoadingCache<String, T> cache = Caffeine.newBuilder()
                .expireAfterWrite(duration)
                .build(fn::apply);

        return cache::get;
    }

    public static <T> Supplier<T> of(Duration duration, Supplier<T> supplier) {
        Function<String, T> fn = of(duration, k -> supplier.get());
        return () -> fn.apply(FIXED_KEY);
    }

    public static <T> Function<String, Mono<T>> ofMono(Duration duration, Function<String, Mono<T>> fn) {
        final AsyncLoadingCache<String, T> cache = Caffeine.newBuilder()
                .expireAfterWrite(duration.multipliedBy(2))
                .refreshAfterWrite(duration)
                .buildAsync((k, e) ->
                        fn.apply(k)
                                .subscribeOn(Schedulers.fromExecutor(e))
                                .toFuture());

        return (k) -> Mono.fromFuture(cache.get(k));
    }

    public static <T> Mono<T> ofMonoFixedKey(Duration duration, Mono<T> mono) {
        Function<String, Mono<T>> monoFn = ofMono(duration, key -> mono);
        return Mono.defer(() -> monoFn.apply(FIXED_KEY));
    }
}