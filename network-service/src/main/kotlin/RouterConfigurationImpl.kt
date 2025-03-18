package com.github.projeto

import org.apache.commons.net.telnet.TelnetClient
import java.io.BufferedReader
import java.io.BufferedWriter

class RouterConfigurationImpl : RouterConfiguration {

    private val telnetClient = TelnetClient()
    private val username = "admin"
    private val password = "pepe"
    private val regex = Regex("\\[$username@[\\w-]+] >")

    init {
        telnetClient.connect("localhost", 2323)
    }

    private fun <T> executeCommand(command: String, mapper: (String) -> Response<T>): Response<T> {

        var executed = false
        val reader = telnetClient.inputStream.bufferedReader()
        val writer = telnetClient.outputStream.bufferedWriter()

        while (!executed) {

            val line = reader.readNonBlocking()

            if (line == null) {
                Thread.sleep(100)
                continue
            }

            println("Received: $line")

            if (line.trim() == "Login:") writer.writeAndFlush("$username\r\n")
            else if (line.trim() == "Password:") writer.writeAndFlush("$password\r\n")
            else if (regex.matches(line.trim())) {
                writer.writeAndFlush("$command\r\n")
                executed = true
            }

        }

        val response = StringBuilder()

        while (reader.ready()) {

            val line = reader.readNonBlocking()

            if (line == null || regex.matches(line.trim()) || line.trim().contains(command)) {
                Thread.sleep(100)
                continue
            }

            response.append("$line\n")
        }

        writer.writeAndFlush("\r\n") //for the next command

        if (response.isNotEmpty())
            response.deleteCharAt(response.length - 1)

        return mapper(response.toString())
    }

    private fun executeCommand(command: String) =
        executeCommand(command) { Response(raw = it, data = Unit) }

    override fun showInterfaces() =
        executeCommand("/interface print") { Response(raw = it, data = parseNetworkInterfaces(it)) }

    override fun addStaticRoute(interfaceName: String, ipAddress: String) =
        executeCommand("/ip route add dst-address=$ipAddress gateway=$interfaceName")

    override fun removeStaticRoute(vararg number: Int) =
        executeCommand("/ip route remove numbers=${number.joinToString(",")}")

    override fun enableInterface(interfaceName: String) =
        executeCommand("/interface enable $interfaceName")

    override fun disableInterface(interfaceName: String) =
        executeCommand("/interface disable $interfaceName")

    override fun setIpAddress(interfaceName: String, ipAddress: String) =
        executeCommand("/ip address add address=$ipAddress interface=$interfaceName")

    override fun removeIpAddress(vararg number: Int) =
        executeCommand("/ip address remove numbers=${number.joinToString(",")}")

    private fun BufferedReader.readNonBlocking(): String? {

        val buffer = StringBuilder()

        while (this.ready()) {

            val char = this.read()

            if (char == -1) break

            val theChar = char.toChar()

            if (theChar == '\n' || theChar == '\r') break

            buffer.append(theChar)
        }

        return if (buffer.isNotEmpty()) buffer.toString() else null
    }

    private fun BufferedWriter.writeAndFlush(data: String) {
        this.write(data)
        this.flush()
    }

}