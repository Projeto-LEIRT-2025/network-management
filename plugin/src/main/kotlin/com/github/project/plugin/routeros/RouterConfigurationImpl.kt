package com.github.project.plugin.routeros

import com.github.project.api.router.RouterConfiguration
import com.github.project.api.router.response.*
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
    private val regex = Regex("\\[$username@[\\w-]+]")

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
            else if (regex.containsMatchIn(line.trim())) {
                writer.writeAndFlush("$command\r\n")
                break
            }

        }

        var started = false
        val response = StringBuilder()

        while (true) {

            val line = reader.readLine()
                .replace("<", "").replace(">", "").trim()

            if (line.isBlank()) {
                if (started)
                    response.append('\n')
                continue
            }

            if (!started) {

                if (regex.containsMatchIn(line))
                    started = true

                continue
            }

            if (command.contains(line))
                continue

            if (regex.matches(line))
                break

            if (regex.containsMatchIn(line))
                continue

            response.append("$line ")
        }

        writer.writeAndFlush("\r\n") //for the next command

        return mapper(response.toString().trim())
    }

    override fun login(): Boolean {

        val reader = telnetClient.inputStream.bufferedReader()
        val writer = telnetClient.outputStream.bufferedWriter()

        while (true) {

            val line = reader.readChunk()

            if (line.isBlank()) continue

            if (line.trim() == "Login:") writer.writeAndFlush("$username\r\n")
            else if (line.trim() == "Password:") writer.writeAndFlush("$password\r\n")
            else if (line.trim().contains("Login failed")) return false
            else if (regex.containsMatchIn(line.trim())) {
                writer.writeAndFlush("\r\n")
                return true
            }

        }

    }

    private fun executeCommand(command: String) =
        executeCommand(command) { Response(raw = it, data = Unit) }

    override fun addStaticRoute(gateway: String, ipAddress: String, prefix: Int) =
        executeCommand("/ip route add dst-address=$ipAddress/$prefix gateway=$gateway")

    override fun removeStaticRoute(vararg number: Int) =
        executeCommand("/ip route remove numbers=${number.joinToString(",")}")

    override fun enableInterface(interfaceName: String) =
        executeCommand("/interface enable $interfaceName")

    override fun disableInterface(interfaceName: String) =
        executeCommand("/interface disable $interfaceName")

    override fun setIpAddress(interfaceName: String, ipAddress: String, prefix: Int) =
        executeCommand("/ip address add address=$ipAddress/$prefix interface=$interfaceName")

    override fun removeIpAddress(interfaceName: String) =
        executeCommand("/ip address remove [find interface=$interfaceName]")

    override fun getInterfacesMac(): Response<List<InterfaceMac>> =
        executeCommand("/interface print detail without-paging") { parseInterfacesMac(it) }

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

    override fun addOSPFNetworks(network: String, prefix: Int, areaName: String) =
        executeCommand("/routing ospf interface-template add network=$network/$prefix area=$areaName")

    override fun addOSPFInterface(interfaceName: String, areaName: String, networkType: String, cost: Int) =
        executeCommand("/routing ospf interface-template add interfaces=$interfaceName area=$areaName type=$networkType cost=$cost")

    override fun createAddressPool(name: String, rangeStart: String, rangeEnd: String) =
        executeCommand("/ip pool add name=$name ranges=$rangeStart-$rangeEnd")

    override fun createDHCPServer(name: String, pool: String, interfaceName: String) =
        executeCommand("/ip dhcp-server add address-pool=$pool interface=$interfaceName name=$name disabled=no")

    override fun createDHCPServerRelay(name: String, pool: String, interfaceName: String, relayAddress: String): Response<Unit> =
        executeCommand("/ip dhcp-server add address-pool=$pool interface=$interfaceName name=$name relay=$relayAddress disabled=no")

    override fun createDHCPServerNetwork(network: String, prefix: Int, gateway: String): Response<Unit> =
        executeCommand("/ip dhcp-server network add address=$network/$prefix gateway=$gateway")

    override fun createDHCPRelay(name: String, interfaceName: String, serverAddress: String) =
        executeCommand("/ip dhcp-relay add name=$name interface=$interfaceName dhcp-server=$serverAddress")

    override fun enableDHCPRelay(name: String) =
        executeCommand("/ip dhcp-relay enable $name")

    override fun disableDHCPRelay(name: String) =
        executeCommand("/ip dhcp-relay disable $name")

    override fun removeDHCPRelay(name: String) =
        executeCommand("/ip dhcp-relay remove $name")

    override fun getNeighbors() =
        executeCommand("/ip neighbor print detail without-paging") { parseNeighbors(it) }

    private fun parseNeighbors(raw: String): Response<List<Neighbor>> {

        val cleanedRaw = removeAnsiCodes(raw)
        val neighbors = mutableListOf<Neighbor>()
        val entryRegex = Regex("""(\w[\w-]*)=("[^"]*"|\S+)""")

        for (line in cleanedRaw.lines()) {
            val pairs = entryRegex.findAll(line)
                .map { match ->
                    val key = match.groupValues[1]
                    val value = match.groupValues[2].trim('"')
                    key to value
                }
                .toMap()

            val neighbor = Neighbor(
                connectedInterface = pairs["interface"] ?: "",
                interfaceName = pairs["interface-name"] ?: "",
                ipAddress = pairs["address"] ?: "",
                mac = pairs["mac-address"] ?: ""
            )

            neighbors.add(neighbor)
        }

        return Response(
            raw = raw,
            data = neighbors
        )
    }


    private fun parseInterfacesMac(raw: String): Response<List<InterfaceMac>> {

        val cleanedRaw = removeAnsiCodes(raw)
        val interfaces = cleanedRaw.lines()
            .map { it.trim() }
            .filter { it.isNotEmpty() }
            .mapNotNull { line ->
                val nameRegex = Regex("""name="([^"]+)"""")
                val macRegex = Regex("""mac-address=([\dA-F:]{17})""", RegexOption.IGNORE_CASE)
                val name = nameRegex.find(line)?.groupValues?.get(1)
                val mac = macRegex.find(line)?.groupValues?.get(1)

                if (name != null && mac != null && mac != "00:00:00:00:00:00") {
                    InterfaceMac(name, mac)
                } else {
                    null
                }
            }

        return Response(
            raw = raw,
            data = interfaces
        )
    }

    fun removeAnsiCodes(text: String): String {
        val ansiRegex = Regex("""\x1B\[[0-9;]*[mK]""")
        return ansiRegex.replace(text, "")
    }

    private fun BufferedReader.readChunk(): String {

        val buffer = StringBuilder()

        do {

            val char = this.read()

            if (char == -1) break

            val theChar = char.toChar()

            if (theChar == '\r' || theChar == '\n') {
                break
            }

            buffer.append(theChar)

        } while (this.ready())

        return buffer.toString()
    }

    private fun BufferedWriter.writeAndFlush(data: String) {
        this.write(data)
        this.flush()
    }

}