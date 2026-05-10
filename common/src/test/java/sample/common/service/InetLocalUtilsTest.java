package sample.common.service;

import org.junit.jupiter.api.Test;

import java.net.InetAddress;

import static org.assertj.core.api.Assertions.assertThat;

class InetLocalUtilsTest {

    @Test
    void testInetUtils() {
        InetAddress address = InetLocalUtils.findFirstNonLoopbackAddress();
        assertThat(address).isNotNull();
    }
}
