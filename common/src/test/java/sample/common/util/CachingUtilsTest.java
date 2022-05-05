package sample.common.util;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;

class CachingUtilsTest {
    @Test
    void testSyncCaching() {
        Random random = new Random();
        Function<String, Integer> fn = (k) -> random.nextInt();
        Function<String, Integer> wrappedFn = CachingUtils.of(Duration.ofSeconds(10), fn);
        int result1 = wrappedFn.apply("key1");

        assertThat(wrappedFn.apply("key1")).isEqualTo(result1);
        assertThat(wrappedFn.apply("key1")).isEqualTo(result1);
        assertThat(wrappedFn.apply("key2")).isNotEqualTo(result1);
    }

    @Test
    void testSupplierCaching() {
        Random random = new Random();
        Supplier<Integer> fn = () -> random.nextInt();
        Supplier<Integer> wrappedSupplier = CachingUtils.of(Duration.ofSeconds(60), fn);
        int result1 = wrappedSupplier.get();

        assertThat(wrappedSupplier.get()).isEqualTo(result1);
        assertThat(wrappedSupplier.get()).isEqualTo(result1);
    }

    private Mono<String> get(String key) {
        Random random = ThreadLocalRandom.current();
        return Mono.fromSupplier(() -> key + random.nextInt());
    }

    @Test
    void testMonoCaching() {
        Function<String, Mono<String>> fn = (k) -> get(k);
        Function<String, Mono<String>> wrappedFn = CachingUtils.ofMono(Duration.ofSeconds(10), fn);
        StepVerifier.create(wrappedFn.apply("key1"))
                .assertNext(result1 -> {
                    StepVerifier.create(wrappedFn.apply("key1"))
                            .assertNext(result2 -> {
                                assertThat(result2).isEqualTo(result1);
                            })
                            .verifyComplete();
                    StepVerifier.create(wrappedFn.apply("key1"))
                            .assertNext(result2 -> {
                                assertThat(result2).isEqualTo(result1);
                            })
                            .verifyComplete();

                    StepVerifier.create(wrappedFn.apply("key2"))
                            .assertNext(result2 -> {
                                assertThat(result2).isNotEqualTo(result1);
                            })
                            .verifyComplete();
                })
                .verifyComplete();
    }

    @Test
    void testMonoCachingFixedKey() {
        Random random = new Random();
        Mono<Integer> mono = Mono.fromCallable(() -> random.nextInt());
        Mono<Integer> wrapped = CachingUtils.ofMonoFixedKey(Duration.ofSeconds(10), mono);
        StepVerifier.create(wrapped)
                .assertNext(result1 -> {
                    StepVerifier.create(wrapped)
                            .assertNext(result2 -> {
                                assertThat(result2).isEqualTo(result1);
                            })
                            .verifyComplete();
                })
                .verifyComplete();
    }
}