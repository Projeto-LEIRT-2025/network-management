package com.github.projeto

import org.apache.commons.net.telnet.TelnetClient
import java.io.BufferedReader
import java.io.BufferedWriter

class RouterConfigurationImpl(

    hostname: String,
    port: Int,
    private val username: String,
    private val password: String

) : RouterConfiguration {

    private val telnetClient = TelnetClient()
    private val regex = Regex("\\[$username@[\\w-]+] >")

    init {
        telnetClient.connect(hostname, port)
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

    override fun addStaticRoute(gateway: String, ipAddress: String) =
        executeCommand("/ip route add dst-address=$ipAddress gateway=$gateway")

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

    override fun enableSNMP() =
        executeCommand("/snmp set enabled=yes")

    override fun disableSNMP() =
        executeCommand("/snmp set enabled=no")

    override fun changeSNMPVersion(version: Int) =
        executeCommand("/snmp set trap-version=$version")

    override fun createAddressPool(name: String, address: String) =
        executeCommand("/ip pool add name=$name ranges=$address")

    override fun createDHCPServer(name: String, pool: String, interfaceName: String) =
        executeCommand("/ip dhcp-server add address-pool=$pool disabled=no interface=$interfaceName name=$name")

    override fun createDHCPRelay(name: String, interfaceName: String, serverAddress: String) {
        executeCommand("/ip dhcp-relay add name=$name interface=$interfaceName dhcp-server=$serverAddress")
    }

    override fun enableDHCPRelay(name: String) {
        executeCommand("/ip dhcp-relay enable $name")
    }

    override fun disableDHCPRelay(name: String) {
        executeCommand("/ip dhcp-relay disable $name")
    }

    override fun removeDHCPRelay(name: String) {
        executeCommand("/ip dhcp-relay remove $name")
    }

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