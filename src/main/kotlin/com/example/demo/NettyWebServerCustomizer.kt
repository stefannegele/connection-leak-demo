package com.example.demo

import io.netty.handler.timeout.ReadTimeoutHandler
import org.springframework.boot.web.embedded.netty.NettyReactiveWebServerFactory
import org.springframework.boot.web.server.WebServerFactoryCustomizer
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit

@Component
class NettyWebServerCustomizer : WebServerFactoryCustomizer<NettyReactiveWebServerFactory> {
    override fun customize(factory: NettyReactiveWebServerFactory) {
        factory.addServerCustomizers({ server ->
            server.doOnConnection { connection ->
                connection.addHandlerFirst(ReadTimeoutHandler(100, TimeUnit.MILLISECONDS))
            }
        })
    }
}
