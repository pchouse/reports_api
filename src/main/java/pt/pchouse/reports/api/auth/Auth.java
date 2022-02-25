/*
 *  Copyright (C) 2022  PChouse - Reflexão Estudos e Sistemas Informáticos, lda
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Affero General Public License as
 *  published by the Free Software Foundation, either version 3 of the
 *  License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Affero General Public License for more details.
 *
 *  You should have received a copy of the GNU Affero General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package pt.pchouse.reports.api.auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;
import java.util.Objects;

/**
 *
 * @since 1.0.0
 */
@Component
@Scope(value = BeanDefinition.SCOPE_PROTOTYPE)
public class Auth implements IAuth {

    /**
     *
     * @since 1.0.0
     */
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * Remote Ip
     * @since 1.0.0
     */
    private String remoteIp;

    /**
     * The allowed ip defined in application.properties
     * @since 1.0.0
     */
    @Value("${client.allowIps}")
    private String[] allowIps;

    /**
     * The authorization implementation
     * @since 1.0.0
     */
    public Auth() {
        logger.debug("New instance of {}", this.getClass().getName());
    }

    /**
     * Get allowed client ips
     *
     * @return The allowed IPs in the application.properties
     * @since 1.0.0
     */
    public String[] getAllowIps() {
        return allowIps;
    }

    /**
     * Catch the remote IP from the web container
     * @since 1.0.0
     */
    public void catchRemoteIP() {
        if (remoteIp == null) {
            remoteIp = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                    .getRequest()
                    .getRemoteAddr();
        }
        logger.debug("Client remote IP {}", remoteIp);
    }

    /**
     * Get the client ip address
     *
     * @return The client remote IP
     * @since 1.0.0
     */
    public String getRemoteIp() {
        return remoteIp;
    }

    /**
     * Get if the remote ip is authorized to make requests or not
     *
     * @return Check if the client remote IP is allowed
     * @since 1.0.0
     */
    public boolean isRemoteIpAllowed() {

        if (allowIps == null || allowIps.length == 0) {
            return true;
        }

        String ip = getRemoteIp();

        if (Arrays.asList(allowIps).contains(ip)) {
            return true;
        }

        return Arrays.stream(allowIps).anyMatch(
                s -> {
                    boolean local = s.equals("localhost") || s.equals("127.0.0.1") || s.equals("::1") || s.equals("0:0:0:0:0:0:0:1");
                    boolean isLocal = ip.equals("127.0.0.1") || ip.equals("::1") || ip.equals("0:0:0:0:0:0:0:1");
                    return local && isLocal;
                }
        );
    }

    /**
     * Return if the client request is allowed or not.
     * In this implementation only verify if the remote is in the white list (application.config -> client.allowIps)
     *
     * @return Check if client is allowed
     * @since 1.0.0
     */
    @Override
    public boolean isAuthorized() {
        return isRemoteIpAllowed();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Auth auth = (Auth) o;
        return Objects.equals(remoteIp, auth.remoteIp) && Arrays.equals(allowIps, auth.allowIps);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(remoteIp);
        result = 31 * result + Arrays.hashCode(allowIps);
        return result;
    }
}
