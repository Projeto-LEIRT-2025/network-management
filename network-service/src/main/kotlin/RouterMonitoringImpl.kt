package com.github.projeto

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
private const val BYTES_RECEIVED_OID = ".1.3.6.1.2.1.31.1.1.1.6"
private const val INTERFACE_NAME_OID = ".1.3.6.1.2.1.2.2.1.2"

class RouterMonitoringImpl : RouterMonitoring {

    private val snmp: Snmp
    private val target: CommunityTarget<Address>

    init {

        val targetAddress = GenericAddress.parse("udp:172.19.0.3/161")

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
        val pduResponse = responseEvent.response
        val totalMemory = pduResponse.getVariable(oid).toInt() //kibibyte

        return totalMemory / 1024 //1 kibibyte is 1024 bytes -> MiB (mebibyte)
    }

    override fun getMemoryUsed(): Int {

        val oid = OID(MEMORY_USED_OID)

        val pdu = PDU().apply {
            add(VariableBinding(oid))
            type = PDU.GET
        }

        val responseEvent = snmp.send(pdu, this.target)
        val pduResponse = responseEvent.response
        val memoryUsed = pduResponse.getVariable(oid).toInt() //kibibyte

        return memoryUsed / 1024 //1 kibibyte is 1024 bytes -> MiB (mebibyte)
    }

    override fun getUptime(): String {

        val oid = OID(UPTIME_OID)

        val pdu = PDU().apply {
            add(VariableBinding(oid))
            type = PDU.GET
        }

        val responseEvent = snmp.send(pdu, this.target)
        val pduResponse = responseEvent.response
        val uptime = pduResponse.getVariable(oid).toString()

        return uptime
    }

    override fun getCpuLoad(): Double {

        val oid = OID(CPU_LOAD_OID)

        val pdu = PDU().apply {
            add(VariableBinding(oid))
            type = PDU.GETBULK
            maxRepetitions = 32
            nonRepeaters = 0
        }

        val responseEvent = snmp.send(pdu, this.target)
        val response = responseEvent.response

        return response.variableBindings
            .filter { it.oid.startsWith(oid) }
            .map { it.variable.toInt() }
            .average()
    }

    override fun getBytesReceived(interfaceName: String): Long {

        val interfaceOid = getInterfaceOid(interfaceName) ?: return -1
        val oid = OID(BYTES_RECEIVED_OID + ".${interfaceOid.last()}")

        val pdu = PDU().apply {
            add(VariableBinding(oid))
            type = PDU.GET
        }

        val responseEvent = snmp.send(pdu, this.target)
        val pduResponse = responseEvent.response
        val bytesReceived = pduResponse.getVariable(oid).toLong()

        return bytesReceived

    }

    private fun getInterfaceOid(interfaceName: String): OID? {

        val oid = OID(INTERFACE_NAME_OID)

        val pdu = PDU().apply {
            add(VariableBinding(oid))
            type = PDU.GETBULK
            maxRepetitions = 16
            nonRepeaters = 0
        }

        val responseEvent = snmp.send(pdu, this.target)
        val response = responseEvent.response

        return response.variableBindings
            .filter { it.oid.startsWith(oid) && it.toValueString() == interfaceName }
            .map { it.oid }
            .firstOrNull()
    }

}