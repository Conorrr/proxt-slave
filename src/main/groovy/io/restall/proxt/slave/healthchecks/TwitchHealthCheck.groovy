package io.restall.proxt.slave.healthchecks

import groovy.transform.CompileStatic
import ratpack.exec.Blocking
import ratpack.exec.Promise
import ratpack.health.HealthCheck
import ratpack.registry.Registry

@CompileStatic
class TwitchHealthCheck implements HealthCheck {

    final String URL = "http://usher.ttvnw.net"

    @Override
    String getName() {
        return "twitch"
    }

    @Override
    Promise<HealthCheck.Result> check(Registry registry) throws Exception {
        Blocking.get {
            new Socket().connect(new InetSocketAddress(new URL(URL).getHost(), 80), 2000)
            true
        }.map { Boolean isReachable ->
            HealthCheck.Result.healthy("able to reach usher")
        }.onError(UnknownHostException, { UnknownHostException e ->
            HealthCheck.Result.unhealthy("unable to resolve %s", URL)
        })
    }
}
