package sample.common.service

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.IOException
import java.net.Inet4Address
import java.net.InetAddress
import java.net.NetworkInterface
import java.net.UnknownHostException

//Based on Spring Cloud version of InetUtils
object InetLocalUtils {
    val LOGGER: Logger = LoggerFactory.getLogger(InetLocalUtils::class.java)
    fun findFirstNonLoopbackAddress(): InetAddress? {
        var result: InetAddress? = null
        try {
            var lowest = Int.MAX_VALUE
            val nics = NetworkInterface.getNetworkInterfaces()
            while (nics.hasMoreElements()) {
                val ifc = nics.nextElement()
                if (ifc.isUp) {
                    if (ifc.index < lowest || result == null) {
                        lowest = ifc.index
                    } else if (result != null) {
                        continue
                    }

                    val addrs = ifc.inetAddresses
                    while (addrs.hasMoreElements()) {
                        val address = addrs.nextElement()
                        if (address is Inet4Address && !address.isLoopbackAddress()) {
                            result = address
                        }
                    }
                }
            }
        } catch (ex: IOException) {
            LOGGER.error("Cannot get first non-loopback address", ex)
        }
        if (result != null) {
            return result
        }
        try {
            return InetAddress.getLocalHost()
        } catch (e: UnknownHostException) {
            LOGGER.warn("Unable to retrieve localhost")
        }
        return null
    }

}