package com.github.project.plugin.routeros

import com.github.project.api.router.RouterConfiguration
import com.github.project.api.router.response.Response
import org.apache.commons.net.telnet.TelnetClient
import java.io.BufferedReader
import java.io.BufferedWriter

class RouterConfigurationImpl(

    hostname: String,
    port: String,
    private val username: String,
    private val password: String

) : RouterConfiguration {

    private val telnetClient = TelnetClient()
    private val regex = Regex("\\[$username@[\\w-]+] >")

    init {
        telnetClient.connect(hostname, port.toInt())
    }

    private fun <T> executeCommand(command: String, mapper: (String) -> Response<T>): Response<T> {

        val reader = telnetClient.inputStream.bufferedReader()
        val writer = telnetClient.outputStream.bufferedWriter()

        while (true) {

            val line = reader.readChunk()

            if (line.isBlank()) continue

            if (line.trim() == "Login:") writer.writeAndFlush("$username\r\n")
            else if (line.trim() == "Password:") writer.writeAndFlush("$password\r\n")
            else if (regex.matches(line.trim())) {
                writer.writeAndFlush("$command\r\n")
                break
            }

        }

        var started = false
        val response = StringBuilder()

        while (true) {

            val line = reader.readChunk().trim()

            if (line.isBlank()) continue

            if (!started) {

                if (regex.containsMatchIn(line) && line.contains(command)) {
                    started = true
                }

                continue
            }

            if (regex.matches(line)) {
                break
            }

            response.appendLine(line)
        }

        response.lastIndexOf('\n').let {

            if (it != -1)
                response.deleteCharAt(it)

        }

        writer.writeAndFlush("\r\n") //for the next command

        return mapper(response.toString())
    }

    private fun executeCommand(command: String) =
        executeCommand(command) { Response(raw = it, data = Unit) }

    override fun addStaticRoute(gateway: String, ipAddress: String, mask: Int) =
        executeCommand("/ip route add dst-address=$ipAddress/$mask gateway=$gateway")

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

    override fun createOSPFProcess(processId: String, routerId: String) =
        executeCommand("/routing ospf instance add name=$processId router-id=$routerId")

    override fun createOSPFArea(areaId: String, processId: String) =
        executeCommand("/routing ospf area add area-id=$areaId instance=$processId")

    override fun addOSPFNetworks(network: String, mask: Int, areaName: String) =
        executeCommand("/routing ospf interface-template add network=$network/$mask area=$areaName")

    override fun addOSPFInterface(interfaceName: String, areaName: String, networkType: String, cost: Int) =
        executeCommand("/routing ospf interface-template add interfaces=$interfaceName area=$areaName type=$networkType cost=$cost")

    override fun createAddressPool(name: String, address: String, mask: Int) =
        executeCommand("/ip pool add name=$name ranges=$address/$mask")

    override fun createDHCPServer(name: String, pool: String, interfaceName: String) =
        executeCommand("/ip dhcp-server add address-pool=$pool interface=$interfaceName name=$name disabled=no")

    override fun createDHCPServerRelay(name: String, pool: String, interfaceName: String, relayAddress: String): Response<Unit> =
        executeCommand("/ip dhcp-server add address-pool=$pool interface=$interfaceName name=$name relay=$relayAddress disabled=no")

    override fun createDHCPServerNetwork(network: String, mask: Int, gateway: String): Response<Unit> =
        executeCommand("/ip dhcp-server network add address=$network/$mask gateway=$gateway")

    override fun createDHCPRelay(name: String, interfaceName: String, serverAddress: String) =
        executeCommand("/ip dhcp-relay add name=$name interface=$interfaceName dhcp-server=$serverAddress")

    override fun enableDHCPRelay(name: String) =
        executeCommand("/ip dhcp-relay enable $name")

    override fun disableDHCPRelay(name: String) =
        executeCommand("/ip dhcp-relay disable $name")

    override fun removeDHCPRelay(name: String) =
        executeCommand("/ip dhcp-relay remove $name")

    override fun getNeighbors() =
        executeCommand("/ip neighbor print detail") //falta o parse

    private fun BufferedReader.readChunk(): String {

        val buffer = StringBuilder()

        do {

            val char = this.read()

            if (char == -1) break

            val theChar = char.toChar()

            if (theChar == '\r' || theChar == '\n') break

            buffer.append(theChar)

        } while (this.ready())

        return buffer.toString()
    }

    private fun BufferedWriter.writeAndFlush(data: String) {
        this.write(data)
        this.flush()
    }

}