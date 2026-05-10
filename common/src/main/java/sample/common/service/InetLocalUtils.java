package sample.common.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.Enumeration;

// Based on Spring Cloud version of InetUtils
public final class InetLocalUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(InetLocalUtils.class);

    private InetLocalUtils() {
    }

    public static InetAddress findFirstNonLoopbackAddress() {
        InetAddress result = null;
        try {
            int lowest = Integer.MAX_VALUE;
            Enumeration<NetworkInterface> nics = NetworkInterface.getNetworkInterfaces();
            while (nics.hasMoreElements()) {
                NetworkInterface ifc = nics.nextElement();
                if (ifc.isUp()) {
                    if (ifc.getIndex() < lowest || result == null) {
                        lowest = ifc.getIndex();
                    } else {
                        continue;
                    }

                    Enumeration<InetAddress> addrs = ifc.getInetAddresses();
                    while (addrs.hasMoreElements()) {
                        InetAddress address = addrs.nextElement();
                        if (address instanceof Inet4Address && !address.isLoopbackAddress()) {
                            result = address;
                        }
                    }
                }
            }
        } catch (IOException ex) {
            LOGGER.error("Cannot get first non-loopback address", ex);
        }
        if (result != null) {
            return result;
        }
        try {
            return InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            LOGGER.warn("Unable to retrieve localhost");
        }
        return null;
    }
}
