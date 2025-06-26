package com.github.project.webapplication.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class AppProperties(

    @Value("\${SERVER}")
    val server: String,

    @Value("\${ROUTERS_BASE_PATH}")
    val routersBasePath: String,

    @Value("\${CONFIGURATION_BASE_PATH}")
    val configurationBasePath: String,

    @Value("\${METRICS_BASE_PATH}")
    val metricsBasePath: String,

    @Value("\${PLUGINS_BASE_PATH}")
    val pluginsBasePath: String,

    @Value("\${PLUGINS_ENABLE_PATH}")
    val pluginsEnablePath: String,

    @Value("\${PLUGINS_DISABLE_PATH}")
    val pluginsDisablePath: String,

    @Value("\${PLUGINS_UPLOAD_PATH}")
    val pluginsUploadPath: String,

    @Value("\${CONFIGURATION_ADDRESS_PATH}")
    val configurationAddressPath: String,

    @Value("\${CONFIGURATION_INTERFACES_PATH}")
    val configurationInterfacesPath: String,

    @Value("\${CONFIGURATION_INTERFACES_ENABLE_PATH}")
    val configurationInterfacesEnablePath: String,

    @Value("\${CONFIGURATION_INTERFACES_DISABLE_PATH}")
    val configurationInterfacesDisablePath: String,

    @Value("\${CONFIGURATION_SNMP_ENABLE_PATH}")
    val configurationSnmpEnablePath: String,

    @Value("\${CONFIGURATION_SNMP_DISABLE_PATH}")
    val configurationSnmpDisablePath: String,

    @Value("\${CONFIGURATION_SNMP_VERSION_PATH}")
    val configurationSnmpVersionPath: String,

    @Value("\${CONFIGURATION_OSPF_PROCESS_PATH}")
    val configurationOspfProcessPath: String,

    @Value("\${CONFIGURATION_OSPF_AREA_PATH}")
    val configurationOspfAreaPath: String,

    @Value("\${CONFIGURATION_OSPF_NETWORK_PATH}")
    val configurationOspfNetworkPath: String,

    @Value("\${CONFIGURATION_OSPF_INTERFACE_PATH}")
    val configurationOspfInterfacePath: String,

    @Value("\${CONFIGURATION_ROUTE_STATIC_PATH}")
    val configurationRouteStaticPath: String,

    @Value("\${CONFIGURATION_ADDRESS_POOL_PATH}")
    val configurationAddressPoolPath: String,

    @Value("\${CONFIGURATION_DHCP_SERVER_PATH}")
    val configurationDhcpServerPath: String,

    @Value("\${CONFIGURATION_DHCP_SERVER_RELAY_PATH}")
    val configurationDhcpServerRelayPath: String,

    @Value("\${CONFIGURATION_DHCP_SERVER_NETWORK_PATH}")
    val configurationDhcpServerNetworkPath: String,

    @Value("\${CONFIGURATION_DHCP_RELAY_PATH}")
    val configurationDhcpRelayPath: String,

    @Value("\${CONFIGURATION_DHCP_RELAY_ENABLE_PATH}")
    val configurationDhcpRelayEnablePath: String,

    @Value("\${CONFIGURATION_DHCP_RELAY_DISABLE_PATH}")
    val configurationDhcpRelayDisablePath: String,

    @Value("\${ROUTERS_NETWORK_PATH}")
    val routersNetworkPath: String,

    @Value("\${METRICS_INTERFACES_PATH}")
    val metricsInterfacesPath: String,

    @Value("\${METRICS_INTERFACES_STATS_PATH}")
    val metricsInterfacesStatsPath: String,

    @Value("\${METRICS_DEVICE_STATS_PATH}")
    val metricsDeviceStatsPath: String

)