package com.github.project.plugin.routeros

import com.github.project.api.router.RouterMonitoring
import com.github.project.api.router.response.NetworkInterface
import org.snmp4j.CommunityTarget
import org.snmp4j.PDU
import org.snmp4j.Snmp
import org.snmp4j.mp.SnmpConstants
import org.snmp4j.smi.*
import org.snmp4j.transport.DefaultUdpTransportMapping

private const val TOTAL_MEMORY_OID = ".1.3.6.1.2.1.25.2.3.1.5.65536"
private const val MEMORY_USED_OID = ".1.3.6.1.2.1.25.2.3.1.6.65536"
private const val UPTIME_OID = ".1.3.6.1.2.1.1.3.0"
private const val CPU_LOAD_OID = ".1.3.6.1.2.1.25.3.3.1.2"
private const val INTERFACE_NAME_OID = ".1.3.6.1.2.1.2.2.1.2"
private const val ACTUAL_MTU_OID = ".1.3.6.1.2.1.2.2.1.4"
private const val MAC_ADDRESS_OID = ".1.3.6.1.2.1.2.2.1.6"
private const val OPERATIONAL_STATUS_OID = ".1.3.6.1.2.1.2.2.1.8"
private const val BYTES_IN_OID = ".1.3.6.1.2.1.31.1.1.1.6"
private const val BYTES_OUT_OID = ".1.3.6.1.2.1.31.1.1.1.10"
private const val PACKETS_IN_OID = ".1.3.6.1.2.1.31.1.1.1.7"
private const val PACKETS_OUT_OID = ".1.3.6.1.2.1.31.1.1.1.11"
private const val ERRORS_IN_OID = ".1.3.6.1.2.1.2.2.1.14"
private const val ERRORS_OUT_OID = ".1.3.6.1.2.1.2.2.1.20"
private const val DISCARDS_IN_OID = ".1.3.6.1.2.1.2.2.1.13"
private const val DISCARDS_OUT_OID = ".1.3.6.1.2.1.2.2.1.19"

class RouterMonitoringImpl(

    hostname: String,
    port: String

) : RouterMonitoring {

    private val snmp: Snmp
    private val target: CommunityTarget<Address>

    init {

        val targetAddress = GenericAddress.parse("udp:$hostname/$port")

        this.target = CommunityTarget<Address>()
        this.target.address = targetAddress
        this.target.community = OctetString("public")
        this.target.version = SnmpConstants.version2c
        this.target.timeout = 1000
        this.target.retries = 1

        this.snmp = Snmp(DefaultUdpTransportMapping())
        this.snmp.listen()
    }

    override fun getTotalMemory(): Int {

        val oid = OID(TOTAL_MEMORY_OID)
        val pdu = PDU().apply {
            add(VariableBinding(oid))
            type = PDU.GET
        }

        val responseEvent = snmp.send(pdu, this.target)
        val pduResponse = responseEvent.response ?: return 0
        val totalMemory = pduResponse.getVariable(oid).toInt() //kibibyte

        return totalMemory / 1024 //1 kibibyte is 1024 bytes -> MiB (mebibyte)
    }

    override fun getMemoryUsage(): Int {

        val oid = OID(MEMORY_USED_OID)
        val pdu = PDU().apply {
            add(VariableBinding(oid))
            type = PDU.GET
        }

        val responseEvent = snmp.send(pdu, this.target)
        val pduResponse = responseEvent.response ?: return 0
        val memoryUsage = pduResponse.getVariable(oid).toInt() //kibibyte

        return memoryUsage / 1024 //1 kibibyte is 1024 bytes -> MiB (mebibyte)
    }

    override fun getUptime(): String {

        val oid = OID(UPTIME_OID)
        val pdu = PDU().apply {
            add(VariableBinding(oid))
            type = PDU.GET
        }

        val responseEvent = snmp.send(pdu, this.target)
        val pduResponse = responseEvent.response ?: return ""
        val uptime = pduResponse.getVariable(oid).toString()

        return uptime
    }

    override fun getCpuUsage(): Double {

        val oid = OID(CPU_LOAD_OID)
        val pdu = PDU().apply {
            add(VariableBinding(oid))
            type = PDU.GETBULK
            maxRepetitions = 32
            nonRepeaters = 0
        }

        val responseEvent = snmp.send(pdu, this.target)
        val response = responseEvent.response ?: return 0.0

        return response.variableBindings
            .filter { it.oid.startsWith(oid) }
            .map { it.variable.toInt() }
            .average()
    }

    override fun getBytesIn(index: Int): Long {

        val oid = OID("$BYTES_IN_OID.$index")
        val pdu = PDU().apply {
            add(VariableBinding(oid))
            type = PDU.GET
        }

        val responseEvent = snmp.send(pdu, this.target)
        val pduResponse = responseEvent.response ?: return 0
        val bytesIn = pduResponse.getVariable(oid).toLong()

        return bytesIn
    }

    override fun getBytesOut(index: Int): Long {

        val oid = OID("$BYTES_OUT_OID.$index")
        val pdu = PDU().apply {
            add(VariableBinding(oid))
            type = PDU.GET
        }

        val responseEvent = snmp.send(pdu, this.target)
        val pduResponse = responseEvent.response ?: return 0
        val bytesOut = pduResponse.getVariable(oid).toLong()

        return bytesOut
    }

    override fun getPacketsIn(index: Int): Long {

        val oid = OID("$PACKETS_IN_OID.$index")
        val pdu = PDU().apply {
            add(VariableBinding(oid))
            type = PDU.GET
        }

        val responseEvent = snmp.send(pdu, this.target)
        val pduResponse = responseEvent.response ?: return 0
        val packetsIn = pduResponse.getVariable(oid).toLong()

        return packetsIn
    }

    override fun getPacketsOut(index: Int): Long {

        val oid = OID("$PACKETS_OUT_OID.$index")
        val pdu = PDU().apply {
            add(VariableBinding(oid))
            type = PDU.GET
        }

        val responseEvent = snmp.send(pdu, this.target)
        val pduResponse = responseEvent.response ?: return 0
        val packetsOut = pduResponse.getVariable(oid).toLong()

        return packetsOut
    }

    override fun getErrorPacketsIn(index: Int): Long {

        val oid = OID("$ERRORS_IN_OID.$index")
        val pdu = PDU().apply {
            add(VariableBinding(oid))
            type = PDU.GET
        }

        val responseEvent = snmp.send(pdu, this.target)
        val pduResponse = responseEvent.response ?: return 0
        val errorsIn = pduResponse.getVariable(oid).toLong()

        return errorsIn
    }

    override fun getErrorPacketsOut(index: Int): Long {

        val oid = OID("$ERRORS_OUT_OID.$index")
        val pdu = PDU().apply {
            add(VariableBinding(oid))
            type = PDU.GET
        }

        val responseEvent = snmp.send(pdu, this.target)
        val pduResponse = responseEvent.response ?: return 0
        val errorsOut = pduResponse.getVariable(oid).toLong()

        return errorsOut
    }

    override fun getDiscardedPacketsIn(index: Int): Long {

        val oid = OID("$DISCARDS_IN_OID.$index")
        val pdu = PDU().apply {
            add(VariableBinding(oid))
            type = PDU.GET
        }

        val responseEvent = snmp.send(pdu, this.target)
        val pduResponse = responseEvent.response ?: return 0
        val discardsIn = pduResponse.getVariable(oid).toLong()

        return discardsIn
    }

    override fun getDiscardedPacketsOut(index: Int): Long {

        val oid = OID("$DISCARDS_OUT_OID.$index")
        val pdu = PDU().apply {
            add(VariableBinding(oid))
            type = PDU.GET
        }

        val responseEvent = snmp.send(pdu, this.target)
        val pduResponse = responseEvent.response ?: return 0
        val discardsOut = pduResponse.getVariable(oid).toLong()

        return discardsOut
    }

    override fun getNetworkInterfaces(): List<NetworkInterface> {

        val oids = listOf(
            OID(INTERFACE_NAME_OID),
            OID(ACTUAL_MTU_OID),
            OID(MAC_ADDRESS_OID),
            OID(OPERATIONAL_STATUS_OID)
        )

        val interfaces = mutableMapOf<Int, NetworkInterface.Builder>()

        val pdu = PDU().apply {
            oids.forEach { add(VariableBinding(it)) }
            type = PDU.GETBULK
            maxRepetitions = 32
            nonRepeaters = 0
        }

        val responseEvent = snmp.send(pdu, this.target)
        val pduResponse = responseEvent.response ?: return emptyList()
        val variableBindings = pduResponse.variableBindings

        for (vb in variableBindings) {

            val index = vb.oid.last()

            if (index <= 0)
                continue

            var builder = interfaces[index]

            if (builder == null) {
                builder = NetworkInterface.Builder().index(index)
                interfaces[index] = builder
            }

            when {
                vb.oid.startsWith(OID(INTERFACE_NAME_OID)) -> builder.name(vb.toValueString())
                vb.oid.startsWith(OID(ACTUAL_MTU_OID)) -> builder.actualMtu(vb.variable.toInt())
                vb.oid.startsWith(OID(MAC_ADDRESS_OID)) -> builder.macAddress(vb.toValueString())
                vb.oid.startsWith(OID(OPERATIONAL_STATUS_OID)) -> builder.operationalStatus(
                    NetworkInterface.OperationalStatus.getFromValue(
                        vb.variable.toInt()
                    )
                )
            }

        }

        return interfaces.values
            .filter { it.name.isNotBlank() && it.macAddress.isNotBlank() }
            .map { it.build() }
    }

}