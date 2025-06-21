package com.github.project.webapplication.filters

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ObjectNode
import com.github.project.networkservice.dto.CredentialsDto
import com.github.project.networkservice.exceptions.RouterLoginException
import jakarta.servlet.FilterChain
import jakarta.servlet.ReadListener
import jakarta.servlet.ServletInputStream
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletRequestWrapper
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.web.servlet.HandlerExceptionResolver
import org.springframework.web.util.ContentCachingRequestWrapper
import java.io.ByteArrayInputStream
import java.util.concurrent.ConcurrentHashMap

@Configuration
class RouterCredentialsFilter(

    private val handlerExceptionResolver: HandlerExceptionResolver,

    @Value("\${ROUTERS_BASE_PATH}")
    private val routersBasePath: String,

    @Value("\${CONFIGURATION_BASE_PATH}")
    private val configurationBasePath: String,

    @Value("\${ROUTERS_NETWORK_PATH}")
    private val routersNetworkPath: String

) : OncePerRequestFilter() {

    private val cache = ConcurrentHashMap<Long, CredentialsDto>()

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {

        val objectMapper = ObjectMapper()
        val uri = request.requestURI

        try {

            if (uri.equals("$routersBasePath$configurationBasePath$routersNetworkPath")) {

                val wrapper = ContentCachingRequestWrapper(request)
                val json = wrapper.inputStream.bufferedReader().readText()
                val body = objectMapper.readValue(
                    json, object : TypeReference<MutableMap<Long, CredentialsDto>>() {}
                )

                body.forEach { (id, credentials) -> cache[id] = credentials }

                cache.filter {
                    cache.keys.any {
                        !body.containsKey(it)
                    }
                }.forEach { body[it.key] = it.value }

                val newBodyBytes = objectMapper.writeValueAsBytes(body)

                filterChain.doFilter(buildWrappedRequest(request, newBodyBytes), response)
                return
            }

            val routerConfigRegex = Regex("""$routersBasePath/(\d+)$configurationBasePath""")

            if (routerConfigRegex.containsMatchIn(uri)) {

                val match = routerConfigRegex.find(uri)
                val id = match?.groupValues?.get(1)?.toLongOrNull()

                if (id != null) {

                    val wrapper = ContentCachingRequestWrapper(request)
                    val json = wrapper.inputStream.bufferedReader().readText()
                    val rootNode = objectMapper.readTree(json)

                    if (rootNode.has("credentials")) {

                        val credentialsNode = rootNode["credentials"]
                        val hasUserAndPass = credentialsNode.hasNonNull("username") && credentialsNode.hasNonNull("password")

                        if (!hasUserAndPass) {

                            val cached = cache[id]

                            if (credentialsNode !is ObjectNode || cached == null) {
                                handlerExceptionResolver.resolveException(request, response, null, RouterLoginException(id))
                                return
                            }

                            credentialsNode.put("username", cached.username)
                            credentialsNode.put("password", cached.password)

                            val newBodyBytes = objectMapper.writeValueAsBytes(rootNode)

                            filterChain.doFilter(buildWrappedRequest(wrapper, newBodyBytes), response)
                        }else {
                            cache[id] = CredentialsDto(credentialsNode["username"].asText(), credentialsNode["password"].asText())
                            filterChain.doFilter(buildWrappedRequest(request, wrapper.contentAsByteArray), response)
                        }

                    }else {

                        if (rootNode.has("username") && rootNode.has("password")) {
                            cache[id] = CredentialsDto(rootNode["username"].asText(), rootNode["password"].asText())
                            filterChain.doFilter(buildWrappedRequest(request, wrapper.contentAsByteArray), response)
                        }else {

                            val cached = cache[id]

                            if (cached != null && rootNode is ObjectNode) {

                                val updated = rootNode.apply {
                                    put("username", cached.username)
                                    put("password", cached.password)
                                }

                                val newBodyBytes = objectMapper.writeValueAsBytes(updated)

                                filterChain.doFilter(buildWrappedRequest(wrapper, newBodyBytes), response)
                            }

                        }

                    }

                    return
                }

            }

        }catch (e: Exception) {
            handlerExceptionResolver.resolveException(request, response, null, e)
            return
        }

        filterChain.doFilter(request, response)
    }

    private fun buildWrappedRequest(
        originalRequest: HttpServletRequest,
        newBodyBytes: ByteArray
    ): HttpServletRequest {
        return object : HttpServletRequestWrapper(originalRequest) {
            override fun getInputStream(): ServletInputStream {
                val inputStream = ByteArrayInputStream(newBodyBytes)
                return object : ServletInputStream() {
                    override fun read() = inputStream.read()
                    override fun isFinished() = inputStream.available() == 0
                    override fun isReady() = true
                    override fun setReadListener(readListener: ReadListener) {}
                }
            }

            override fun getContentLength() = newBodyBytes.size
            override fun getContentLengthLong() = newBodyBytes.size.toLong()
        }
    }

}