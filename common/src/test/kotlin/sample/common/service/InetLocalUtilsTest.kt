package sample.common.service

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.net.InetAddress

class InetLocalUtilsTest {

    @Test
    fun testInetUtils() {
        val address: InetAddress? = InetLocalUtils.findFirstNonLoopbackAddress()
        assertThat(address).isNotNull
    }
}