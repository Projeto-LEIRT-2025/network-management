package com.github.projeto

import org.apache.commons.net.telnet.TelnetClient
import java.io.BufferedReader
import java.io.BufferedWriter

class RouterImpl : Router {

    private val telnetClient = TelnetClient()
    private val username = "admin"
    private val password = "pepe"
    private val regex = Regex("\\[$username@[\\w-]+] >")

    init {
        telnetClient.connect("localhost", 2323)
    }

    private fun executeCommand(command: String) {

        val reader = telnetClient.inputStream.bufferedReader()
        val writer = telnetClient.outputStream.bufferedWriter()

        var executed = false
        while (!executed) {

            val line = reader.readNonBlocking()

            if (line.isEmpty()) {
                Thread.sleep(100)
                continue
            }

            println("Recebido: $line")

            if (line.trim() == "Login:") writer.writeAndFlush("$username\r\n")
            else if (line.trim() == "Password:") writer.writeAndFlush("$password\r\n")
            else if (regex.matches(line.trim())) {
                writer.writeAndFlush("$command\r\n")
                executed = true
            }

        }

    }

    override fun showInterfaces() {
        executeCommand("/interface print")
    }

    override fun addStaticRoute(interfaceName: String, ipAddress: String) {
        executeCommand("/ip route add dst-address=$ipAddress gateway=$interfaceName")
    }

    override fun removeStaticRoute(vararg number: Int) {
        executeCommand("/ip route remove numbers=${number.joinToString(",")}")
    }

    override fun enableInterface(interfaceName: String) {
        executeCommand("/interface enable $interfaceName")
    }

    override fun disableInterface(interfaceName: String) {
        executeCommand("/interface disable $interfaceName")
    }

    override fun setIpAddress(interfaceName: String, ipAddress: String) {
        executeCommand("/ip address add address=$ipAddress interface=$interfaceName")
    }

    override fun removeIpAddress(vararg number: Int) {
        executeCommand("/ip address remove numbers=${number.joinToString(",")}")
    }

    private fun BufferedReader.readNonBlocking(): String {

        val buffer = StringBuilder()

        while (this.ready()) {

            val char = this.read()

            if (char == -1) break

            val theChar = char.toChar()
            buffer.append(theChar)

            if (theChar == '\r' || theChar == '\n') break
        }

        return buffer.toString()
    }

    private fun BufferedWriter.writeAndFlush(data: String) {
        this.write(data)
        this.flush()
    }

}