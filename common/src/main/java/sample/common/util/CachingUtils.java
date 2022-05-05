package sample.common.util;

import com.github.benmanes.caffeine.cache.AsyncLoadingCache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.function.Function;
import java.util.function.Supplier;

public class CachingUtils {
    private static final String FIXED_KEY = "FIXED_KEY";

    private CachingUtils() {
    }

    public static <T> Function<String, T> of(@NotNull Duration duration, @NotNull Function<String, T> fn) {
        final LoadingCache<String, T> cache = Caffeine.newBuilder()
                .expireAfterWrite(duration)
                .build((String k) -> fn.apply(k));

        return (String key) -> cache.get(key);
    }

    public static <T> Supplier<T> of(@NotNull Duration duration, @NotNull Supplier<T> supplier) {
        Function<String, T> fn = of(duration, k -> supplier.get());
        return () -> fn.apply(FIXED_KEY);
    }

    public static <T> Function<String, Mono<T>> ofMono(@NotNull Duration duration, @NotNull Function<String, Mono<T>> fn) {
        final AsyncLoadingCache<String, T> cache = Caffeine.newBuilder()
                .expireAfterWrite(duration.multipliedBy(2))
                .refreshAfterWrite(duration)
                .buildAsync((k, e) ->
                        fn.apply(k)
                                .subscribeOn(Schedulers.fromExecutor(e))
                                .toFuture());

        return (k) -> Mono.fromFuture(cache.get(k));
    }

    public static <T> Mono<T> ofMonoFixedKey(@NotNull Duration duration, @NotNull Mono<T> mono) {
        Function<String, Mono<T>> monoFn = ofMono(duration, key -> mono);
        return Mono.defer(() -> monoFn.apply(FIXED_KEY));
    }
}