package com.github.projeto

import org.snmp4j.CommunityTarget
import org.snmp4j.PDU
import org.snmp4j.Snmp
import org.snmp4j.mp.SnmpConstants
import org.snmp4j.smi.*
import org.snmp4j.transport.DefaultUdpTransportMapping

private const val MEMORY_USED_OID = ".1.3.6.1.2.1.25.2.3.1.6.65536"

class RouterMonitoringImpl : RouterMonitoring {

    private val snmp: Snmp
    private val target: CommunityTarget<Address>

    init {

        val targetAddress = GenericAddress.parse("udp:172.20.0.2/161")

        this.target = CommunityTarget<Address>()
        this.target.address = targetAddress
        this.target.community = OctetString("public")
        this.target.version = SnmpConstants.version2c
        this.target.timeout = 1000
        this.target.retries = 1

        this.snmp = Snmp(DefaultUdpTransportMapping())
        this.snmp.listen()
    }

    override fun getMemoryUsed(): Int {

        val oid = OID(MEMORY_USED_OID)
        val pdu = PDU()

        pdu.add(VariableBinding(oid))
        pdu.type = PDU.GET

        val responseEvent = snmp.get(pdu, this.target)
        val pduResponse = responseEvent.response
        val memoryUsed = pduResponse.getVariable(oid).toInt()

        return memoryUsed / 1024
    }

}