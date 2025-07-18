import * as d3 from "https://cdn.jsdelivr.net/npm/d3@7/+esm";

window.addEventListener("load", init);

async function init() {
    await loadGraph({});
}

async function loadGraph(credentials) {

    try {

        const response = await fetch(`${config.server}${config.routers_base_path}${config.configuration_base_path}${config.routers_network_path}`, {
            method: "POST",
            credentials: "include",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(credentials)
        });

        if (response.status === 401) {

            const json = await response.json();
            const routersId = json.data;
            const theCredentials = {};

            for (const routerId of routersId) {
                await new Promise(resolve => {
                    openModal(
                        `Router credentials for ${routerId}`,
                        [
                            { name: "username", label: "Username" },
                            { name: "password", label: "Password", type: "password" }
                        ],
                        async data => {
                            closeModal();
                            theCredentials[routerId] = {
                                username: data.get("username"),
                                password: data.get("password")
                            };
                            resolve();
                        }
                    );
                });
            }

            await loadGraph(theCredentials);
            return;
        }

        const json = await response.json();

        if (!response.ok) {
            showNotification(json.message, "error")
            return
        }

        const { nodes, edges: links } = json.data;

        const graphElement = document.getElementById("graph");
        graphElement.style.justifyContent = "start";
        graphElement.innerHTML = "";

        if (nodes.length === 0) {
            graphElement.style.alignItems = "center";
            graphElement.style.justifyContent = "center";
            graphElement.innerHTML = "<h2 style='opacity: 0.8'>There is no router in the network</h2>";
            return;
        }

        renderControls(graphElement);
        const svgElement = createSvg();
        document.querySelector(".graph-content").appendChild(svgElement);

        const width = +svgElement.getAttribute("width");
        const height = +svgElement.getAttribute("height");
        const svg = d3.select(svgElement);
        const zoomGroup = svg.append("g");

        configureZoom(svg, zoomGroup);
        renderGraph({ svg, zoomGroup, nodes, links, width, height });

    } catch (e) {
        showNotification("An error occurred", "error")
    }

}

function renderControls(container) {
    const header = document.createElement("div");
    header.classList.add("graph-header");
    header.innerHTML = `
        <button id="toggle-labels">Toggle Labels</button>
        <button id="toggle-interfaces">Toggle Interfaces</button>
        <div class="zoom">
            <span id="zoom-value">1.00</span>
            <span id="zoom-in">+</span>
            <span id="zoom-out">-</span>
        </div>
    `;
    container.appendChild(header);

    const content = document.createElement("div");
    content.classList.add("graph-content");
    container.appendChild(content);

    document.getElementById("toggle-labels").addEventListener("click", () => {
        const visible = d3.selectAll(".labels text").style("display") !== "none";
        d3.selectAll(".labels text").style("display", visible ? "none" : "block");
    });

    document.getElementById("toggle-interfaces").addEventListener("click", () => {
        const visible = d3.selectAll(".interfaces text").style("display") !== "none";
        d3.selectAll(".interfaces text").style("display", visible ? "none" : "block");
    });
}

function createSvg() {
    const svg = document.createElementNS("http://www.w3.org/2000/svg", "svg");
    svg.setAttribute("width", "1280");
    svg.setAttribute("height", "720");
    return svg;
}

function configureZoom(svg, zoomGroup) {

    const maxZoom = 2, minZoom = 0.5;
    const zoom = d3.zoom()
        .scaleExtent([minZoom, maxZoom])
        .on("zoom", e => {
            zoomGroup.attr("transform", e.transform);
            document.getElementById("zoom-value").textContent = e.transform.k.toFixed(2);
        })
        .on("start", () => svg.style("cursor", "grabbing"))
        .on("end", () => svg.style("cursor", "grab"));

    svg.call(zoom);

    svg.style("cursor", "grab");

    document.getElementById("zoom-in").addEventListener("click", () => zoomBy(svg, zoom, 0.1, maxZoom));
    document.getElementById("zoom-out").addEventListener("click", () => zoomBy(svg, zoom, -0.1, minZoom));
}

function zoomBy(svg, zoom, delta, limit) {
    const t = d3.zoomTransform(svg.node());
    const newScale = delta > 0 ? Math.min(t.k + delta, limit) : Math.max(t.k + delta, limit);
    svg.transition().duration(300).call(zoom.transform, d3.zoomIdentity.translate(t.x, t.y).scale(newScale));
}

function renderGraph({ zoomGroup, nodes, links, width, height }) {

    const radius = 20

    const simulation = d3.forceSimulation(nodes)
        .force("link", d3.forceLink(links).id(d => d.id).distance(200))
        .force("charge", d3.forceManyBody().strength(-300))
        .force("center", d3.forceCenter(width / 2, height / 2))
        .force("collision", d3.forceCollide().radius(50));

    const link = zoomGroup.append("g")
        .attr("stroke", "#999")
        .attr("stroke-opacity", 0.6)
        .selectAll("line")
        .data(links)
        .join("line")
        .attr("stroke-width", 2);

    const node = zoomGroup.append("g")
        .attr("stroke", "#fff")
        .attr("stroke-width", 1.5)
        .selectAll("circle")
        .data(nodes)
        .join("circle")
        .attr("r", radius)
        .attr("fill", "var(--node)");
    nodeEvent(node)

    const label = zoomGroup.append("g")
        .attr("class", "labels")
        .selectAll("text")
        .data(nodes)
        .join("text")
        .text(d => d.label)
        .attr("font-size", 16)
        .attr("dy", -30)
        .attr("text-anchor", "middle")
        .attr("fill", "var(--primary)");

    const interfaceLabel = zoomGroup.append("g")
        .attr("class", "interfaces")
        .selectAll("g")
        .data(links)
        .join("g");

    const sourceGroup = interfaceLabel.append("g");
    menuEvent(sourceGroup, "source")

    sourceGroup.append("circle")
        .attr("r", 4)
        .attr("fill", d => color(d.source_interface.status))
        .attr("stroke", "#fff")
        .attr("stroke-width", 1)
        .on("mouseover", (event, d) => {
            d3.select(event.currentTarget.parentNode).select("text").style("display", "block");
        })
        .on("mouseout", (event, d) => {
            d3.select(event.currentTarget.parentNode).select("text").style("display", "none");
        });

    sourceGroup.append("text")
        .attr("class", "interfaces")
        .attr("display", "none")
        .attr("font-size", 12)
        .attr("fill", d => color(d.source_interface.status))
        .attr("x", 6)
        .attr("y", 4)
        .text(d => d.source_interface.name);

    const targetGroup = interfaceLabel.append("g");
    menuEvent(targetGroup, "target")

    targetGroup.append("circle")
        .attr("r", 4)
        .attr("fill", d => color(d.target_interface.status))
        .attr("stroke", "#fff")
        .attr("stroke-width", 1)
        .on("mouseover", (event, d) => {
            d3.select(event.currentTarget.parentNode).select("text").style("display", "block");
        })
        .on("mouseout", (event, d) => {
            d3.select(event.currentTarget.parentNode).select("text").style("display", "none");
        });

    targetGroup.append("text")
        .attr("class", "interfaces")
        .attr("display", "none")
        .attr("font-size", 12)
        .attr("fill", d => color(d.target_interface.status))
        .attr("x", 6)
        .attr("y", 4)
        .text(d => d.target_interface.name);

    simulation.on("tick", () => {
        link
            .attr("x1", d => d.source.x)
            .attr("y1", d => d.source.y)
            .attr("x2", d => d.target.x)
            .attr("y2", d => d.target.y);

        node
            .attr("cx", d => d.x)
            .attr("cy", d => d.y);

        label
            .attr("x", d => d.x)
            .attr("y", d => d.y);

        sourceGroup.attr("transform", d => {
            const dx = d.target.x - d.source.x;
            const dy = d.target.y - d.source.y;
            const len = Math.sqrt(dx*dx + dy*dy);
            const x = d.source.x + (dx / len) * radius;
            const y = d.source.y + (dy / len) * radius;
            return `translate(${x},${y})`;
        });

        targetGroup.attr("transform", d => {
            const dx = d.source.x - d.target.x;
            const dy = d.source.y - d.target.y;
            const len = Math.sqrt(dx*dx + dy*dy);
            const x = d.target.x + (dx / len) * radius;
            const y = d.target.y + (dy / len) * radius;
            return `translate(${x},${y})`;
        });

    });

    function color(status) {
        if (status === "UP") return "var(--green)";
        if (status === "DOWN") return "var(--red)";
        return "var(--orange)";
    }

}

function nodeEvent(node) {

    node.on("contextmenu", (event, d) => {

        event.preventDefault();

        const offsetX = 0;
        const offsetY = 10;
        const x = event.clientX + offsetX;
        const y = event.clientY + offsetY;
        const routerId = d.id;

        openMenu(x, y, [
            {
                name: "Device Metrics",
                onClick: async () => {

                    closeMenu()
                    const end = new Date()
                    const start = new Date(end.getTime() - 15 * 60 * 1000)

                    window.open(`${config.server}/routers/${routerId}/metrics?start=${encodeURIComponent(start.toISOString())}&end=${encodeURIComponent(end.toISOString())}`, '_blank')

                }
            },
            {
                name: "Interfaces Metrics",
                onClick: async () => {

                    closeMenu()
                    const end = new Date()
                    const start = new Date(end.getTime() - 15 * 60 * 1000)

                    window.open(`${config.server}/routers/${routerId}/interfaces/metrics?start=${encodeURIComponent(start.toISOString())}&end=${encodeURIComponent(end.toISOString())}`, '_blank')

                }
            },
            {
                name: "Enable Interface",
                onClick: async () => {

                    closeMenu();
                    openModal(
                        "Enable interface",
                        [
                            { name: "interface", label: "Interface name" }
                        ],
                        async data => {

                            const interfaceName = data.get("interface");

                            if (interfaceName.trim() === "") {
                                showNotification("The fields cannot be empty", "error");
                                return;
                            }

                            await optionOnClick(
                                `${config.server}${config.routers_base_path}/${routerId}${config.configuration_base_path}${config.configuration_interfaces_path}/${interfaceName}${config.configuration_interfaces_enable_path}`,
                                "POST",
                                {},
                                message => showNotification(message, 'success'),
                                message => showNotification(message, "error")
                            )

                        }
                    )

                }
            },
            {
                name: "Disable Interface",
                onClick: async () => {

                    closeMenu();
                    openModal(
                        "Disable interface",
                        [
                            { name: "interface", label: "Interface name" }
                        ],
                        async data => {

                            const interfaceName = data.get("interface");

                            if (interfaceName.trim() === "") {
                                showNotification("The fields cannot be empty", "error");
                                return;
                            }

                            await optionOnClick(
                                `${config.server}${config.routers_base_path}/${routerId}${config.configuration_base_path}${config.configuration_interfaces_path}/${interfaceName}${config.configuration_interfaces_disable_path}`,
                                "POST",
                                {},
                                message => showNotification(message, 'success'),
                                message => showNotification(message, "error")
                            )

                        }
                    )

                }
            },
            {
                name: "Remove IP Address",
                onClick: async () => {

                    closeMenu();
                    openModal(
                        "Remove IP Address",
                        [
                            { name: "interface", label: "Interface name" }
                        ],
                        async data => {

                            const interfaceName = data.get("interface");

                            if (interfaceName.trim() === "") {
                                showNotification("The fields cannot be empty", "error");
                                return;
                            }

                            await optionOnClick(
                                `${config.server}${config.routers_base_path}/${routerId}${config.configuration_base_path}${config.configuration_address_path}`,
                                "DELETE",
                                {
                                    credentials: {},
                                    interface_name: interfaceName
                                },
                                message => showNotification(message, 'success'),
                                message => showNotification(message, "error")
                            )

                        }
                    )

                }
            },
            {
                name: "Set IP Address",
                onClick: async () => {

                    closeMenu();
                    openModal(
                        "Set IP Address",
                        [
                            { name: "interface", label: "Interface name" },
                            { name: "ip_address", label: "IP Address" },
                            { name: "prefix", label: "Prefix" }
                        ],
                        async data => {

                            const interfaceName = data.get("interface");
                            const ipAddress = data.get("ip_address");
                            const prefix = data.get("prefix");

                            if (interfaceName.trim() === "" || ipAddress.trim() === "" || prefix.trim() === "") {
                                showNotification("The fields cannot be empty", "error");
                                return;
                            }

                            await optionOnClick(
                                `${config.server}${config.routers_base_path}/${routerId}${config.configuration_base_path}${config.configuration_address_path}`,
                                "POST",
                                {
                                    credentials: {},
                                    interface_name: interfaceName,
                                    ip_address: ipAddress,
                                    prefix: prefix
                                },
                                message => showNotification(message, 'success'),
                                message => showNotification(message, "error")
                            )

                        }
                    )

                }
            },
            {
                name: "Create Address Pool",
                onClick: async () => {

                    closeMenu();
                    openModal(
                        "Create Address Pool",
                        [
                            { name: "pool_name", label: "Pool name"},
                            { name: "range_start", label: "Range start" },
                            { name: "range_end", label: "Range end" }
                        ],
                        async data => {

                            const poolName = data.get("pool_name");
                            const rangeStart = data.get("range_start");
                            const rangeEnd = data.get("range_end");

                            if (poolName.trim() === "" || rangeStart.trim() === "" || rangeEnd.trim() === "") {
                                showNotification("The fields cannot be empty", "error");
                                return;
                            }

                            await optionOnClick(
                                `${config.server}${config.routers_base_path}/${routerId}${config.configuration_base_path}${config.configuration_address_pool_path}/${poolName}`,
                                "POST",
                                {
                                    credentials: {},
                                    range_start: rangeStart,
                                    range_end: rangeEnd
                                },
                                message => showNotification(message, 'success'),
                                message => showNotification(message, "error")
                            )

                        }
                    )

                }
            },
            {
                name: "Create DHCP Server",
                onClick: async () => {

                    closeMenu();
                    openModal(
                        "Create DHCP Server",
                        [
                            { name: "name", label: "Server name" },
                            { name: "pool_name", label: "Pool name" },
                            { name: "interface_name", label: "Interface name" }
                        ],
                        async data => {

                            const name = data.get("name");
                            const poolName = data.get("pool_name");
                            const interfaceName = data.get("interface_name");

                            if (name.trim() === "" || poolName.trim() === "" || interfaceName.trim() === "") {
                                showNotification("The fields cannot be empty", "error");
                                return;
                            }

                            await optionOnClick(
                                `${config.server}${config.routers_base_path}/${routerId}${config.configuration_base_path}${config.configuration_dhcp_server_path}/${name}`,
                                "POST",
                                {
                                    credentials: {},
                                    pool_name: poolName,
                                    interface_name: interfaceName
                                },
                                message => showNotification(message, 'success'),
                                message => showNotification(message, "error")
                            )

                        }
                    )

                }
            },
            {
                name: "Create DHCP Server Network",
                onClick: async () => {

                    closeMenu();
                    openModal(
                        "Create DHCP Server Network",
                        [
                            { name: "network", label: "Network" },
                            { name: "prefix", label: "Prefix" },
                            { name: "gateway", label: "Gateway" }
                        ],
                        async data => {

                            const network = data.get("network");
                            const prefix = data.get("prefix");
                            const gateway = data.get("gateway");

                            if (network.trim() === "" || prefix.trim() === "" || gateway.trim() === "") {
                                showNotification("The fields cannot be empty", "error");
                                return;
                            }

                            await optionOnClick(
                                `${config.server}${config.routers_base_path}/${routerId}${config.configuration_base_path}${config.configuration_dhcp_server_network_path}`,
                                "POST",
                                {
                                    credentials: {},
                                    network: network,
                                    prefix: prefix,
                                    gateway: gateway
                                },
                                message => showNotification(message, 'success'),
                                message => showNotification(message, "error")
                            )

                        }
                    )

                }
            },
            {
                name: "Create DHCP Server Relay",
                onClick: async () => {

                    closeMenu();
                    openModal(
                        "Create DHCP Server Relay",
                        [
                            { name: "name", label: "Server name" },
                            { name: "pool_name", label: "Pool name" },
                            { name: "interface_name", label: "Interface name" },
                            { name: "relay_address", label: "Relay address"}
                        ],
                        async data => {

                            const name = data.get("name");
                            const poolName = data.get("pool_name");
                            const interfaceName = data.get("interface_name");
                            const relayAddress = data.get("relay_address");

                            if (name.trim() === "" || poolName.trim() === "" || interfaceName.trim() === "" || relayAddress.trim() === "") {
                                showNotification("The fields cannot be empty", "error");
                                return;
                            }

                            await optionOnClick(
                                `${config.server}${config.routers_base_path}/${routerId}${config.configuration_base_path}${config.configuration_dhcp_server_relay_path}/${name}`,
                                "POST",
                                {
                                    credentials: {},
                                    pool_name: poolName,
                                    interface_name: interfaceName,
                                    relay_address: relayAddress
                                },
                                message => showNotification(message, 'success'),
                                message => showNotification(message, "error")
                            )

                        }
                    )

                }
            },
            {
                name: "Enable DHCP Relay",
                onClick: async () => {

                    closeMenu();
                    openModal(
                        "Enable DHCP Relay",
                        [
                            { name: "name", label: "Name" }
                        ],
                        async data => {

                            const name = data.get("name");

                            if (name.trim() === "") {
                                showNotification("The fields cannot be empty", "error");
                                return;
                            }

                            await optionOnClick(
                                `${config.server}${config.routers_base_path}/${routerId}${config.configuration_base_path}${config.configuration_dhcp_relay_path}/${name}${config.configuration_dhcp_relay_enable_path}`,
                                "POST",
                                {},
                                message => showNotification(message, 'success'),
                                message => showNotification(message, "error")
                            )

                        }
                    )

                }
            },
            {
                name: "Disable DHCP Relay",
                onClick: async () => {

                    closeMenu();
                    openModal(
                        "Disable DHCP Relay",
                        [
                            { name: "name", label: "Name" }
                        ],
                        async data => {

                            const name = data.get("name");

                            if (name.trim() === "") {
                                showNotification("The fields cannot be empty", "error");
                                return;
                            }

                            await optionOnClick(
                                `${config.server}${config.routers_base_path}/${routerId}${config.configuration_base_path}${config.configuration_dhcp_relay_path}/${name}${config.configuration_dhcp_relay_disable_path}`,
                                "POST",
                                {},
                                message => showNotification(message, 'success'),
                                message => showNotification(message, "error")
                            )

                        }
                    )

                }
            },
            {
                name: "Delete DHCP Relay",
                onClick: async () => {

                    closeMenu();
                    openModal(
                        "Delete DHCP Relay",
                        [
                            { name: "name", label: "Name" }
                        ],
                        async data => {

                            const name = data.get("name");

                            if (name.trim() === "") {
                                showNotification("The fields cannot be empty", "error");
                                return;
                            }

                            await optionOnClick(
                                `${config.server}${config.routers_base_path}/${routerId}${config.configuration_base_path}${config.configuration_dhcp_relay_path}/${name}`,
                                "DELETE",
                                {},
                                message => showNotification(message, 'success'),
                                message => showNotification(message, "error")
                            )

                        }
                    )

                }
            },
            {
                name: "Create DHCP Relay",
                onClick: async () => {

                    closeMenu();
                    openModal(
                        "Create DHCP Relay",
                        [
                            { name: "name", label: "Name" },
                            { name: "interface_name", label: "Interface name" },
                            { name: "server_address", label: "Server address"}
                        ],
                        async data => {

                            const name = data.get("name");
                            const interfaceName = data.get("interface_name");
                            const serverAddress = data.get("server_address");

                            if (name.trim() === "" || interfaceName.trim() === "" || serverAddress.trim() === "") {
                                showNotification("The fields cannot be empty", "error");
                                return;
                            }

                            await optionOnClick(
                                `${config.server}${config.routers_base_path}/${routerId}${config.configuration_base_path}${config.configuration_dhcp_relay_path}/${name}`,
                                "POST",
                                {
                                    credentials: {},
                                    interface_name: interfaceName,
                                    server_address: serverAddress
                                },
                                message => showNotification(message, 'success'),
                                message => showNotification(message, "error")
                            )

                        }
                    )

                }
            },
            {
                name: "Enable SNMP",
                onClick: async () => {

                    closeMenu();

                    await optionOnClick(
                        `${config.server}${config.routers_base_path}/${routerId}${config.configuration_base_path}${config.configuration_snmp_enable_path}`,
                        "POST",
                        {},
                        message => showNotification(message, 'success'),
                        message => showNotification(message, "error")
                    )

                }
            },
            {
                name: "Disable SNMP",
                onClick: async () => {

                    closeMenu();

                    await optionOnClick(
                        `${config.server}${config.routers_base_path}/${routerId}${config.configuration_base_path}${config.configuration_snmp_disable_path}`,
                        "POST",
                        {},
                        message => showNotification(message, 'success'),
                        message => showNotification(message, "error")
                    )
                }

            },
            {
                name: "Change SNMP version",
                onClick: async () => {

                    closeMenu();
                    openModal(
                        "Change SNMP version",
                        [
                            { name: "version", label: "SNMP Version"}
                        ],
                        async data => {

                            const version = data.get("version");

                            if (version.trim() === "") {
                                showNotification("The fields cannot be empty", "error");
                                return;
                            }

                            await optionOnClick(
                                `${config.server}${config.routers_base_path}/${routerId}${config.configuration_base_path}${config.configuration_snmp_version_path}`,
                                "POST",
                                {
                                    credentials: {},
                                    version: version
                                },
                                message => showNotification(message, 'success'),
                                message => showNotification(message, "error")
                            )

                        }
                    )

                }

            },
            {
                name: "Add Static Route",
                onClick: async () => {

                    closeMenu();
                    openModal(
                        "Add Static Route",
                        [
                            { name: "ip_address", label: "IP Address" },
                            { name: "prefix", label: "Prefix" },
                            { name: "gateway", label: "Gateway" }
                        ],
                        async data => {

                            const ipAddress = data.get("ip_address");
                            const prefix = data.get("prefix");
                            const gateway = data.get("gateway");

                            if (ipAddress.trim() === "" || prefix.trim() === "" || gateway.trim() === "") {
                                showNotification("The fields cannot be empty", "error");
                                return;
                            }

                            await optionOnClick(
                                `${config.server}${config.routers_base_path}/${routerId}${config.configuration_base_path}${config.configuration_route_static_path}`,
                                "POST",
                                {
                                    credentials: {},
                                    ip_address: ipAddress,
                                    prefix: prefix,
                                    gateway: gateway
                                },
                                message => showNotification(message, 'success'),
                                message => showNotification(message, "error")
                            )

                        }
                    )

                }

            },
            {
                name: "Remove Static Route",
                onClick: async () => {

                    closeMenu();
                    openModal(
                        "Remove Static Route",
                        [
                            { name: "identifiers", label: "Identifiers" }
                        ],
                        async data => {

                            const identifiers = data.get("identifiers").split(",");
                            const error = identifiers.some(id => !Number.isInteger(Number(id)));

                            if (error) {
                                showNotification("The field is not a identifier", "error");
                                return;
                            }

                            await optionOnClick(
                                `${config.server}${config.routers_base_path}/${routerId}${config.configuration_base_path}${config.configuration_route_static_path}`,
                                "DELETE",
                                {
                                    credentials: {},
                                    identifiers: identifiers
                                },
                                message => showNotification(message, 'success'),
                                message => showNotification(message, "error")
                            )

                        }
                    )

                }

            },
            {
                name: "Create OSPF Process",
                onClick: async () => {

                    closeMenu();
                    openModal(
                        "Create OSPF Process",
                        [
                            {name: "process_id", label: "Process ID"},
                            {name: "router_id", label: "Router ID"}
                        ],
                        async data => {

                            const processId = data.get("process_id");
                            const theRouterId = data.get("router_id");

                            if (processId.trim() === "" || theRouterId.trim() === "") {
                                showNotification("The fields cannot be empty", "error");
                                return;
                            }

                            await optionOnClick(
                                `${config.server}${config.routers_base_path}/${routerId}${config.configuration_base_path}${config.configuration_ospf_process_path}`,
                                "POST",
                                {
                                    credentials: {},
                                    process_id: processId,
                                    router_id: theRouterId
                                },
                                message => showNotification(message, 'success'),
                                message => showNotification(message, "error")
                            )

                        }
                    )
                }

            },
            {
                name: "Create OSPF Area",
                onClick: async () => {

                    closeMenu();
                    openModal(
                        "Create OSPF Area",
                        [
                            { name: "area_id", label: "Area ID" },
                            { name: "process_id", label: "Process ID" }
                        ],
                        async data => {

                            const areaId = data.get("area_id");
                            const processId = data.get("process_id");

                            if (processId.trim() === "" || areaId.trim() === "") {
                                showNotification("The fields cannot be empty", "error");
                                return;
                            }

                            await optionOnClick(
                                `${config.server}${config.routers_base_path}/${routerId}${config.configuration_base_path}${config.configuration_ospf_area_path}`,
                                "POST",
                                {
                                    credentials: {},
                                    area_id: areaId,
                                    process_id: processId
                                },
                                message => showNotification(message, 'success'),
                                message => showNotification(message, "error")
                            )

                        }
                    )

                }

            },
            {
                name: "Add OSPF Interface",
                onClick: async () => {

                    closeMenu();
                    openModal(
                        "Add OSPF Interface",
                        [
                            { name: "interface_name", label: "Interface" },
                            { name: "area_name", label: "Area Name" },
                            { name: "network_type", label: "Network Type" },
                            { name: "cost", label: "Cost" }
                        ],
                        async data => {

                            const interfaceName = data.get("interface_name");
                            const areaName = data.get("area_name");
                            const networkType = data.get("network_type");
                            const cost = data.get("cost");

                            if (!Number.isInteger(Number(cost))) {
                                showNotification("The cost has to be a number", "error");
                                return;
                            }

                            if (interfaceName.trim() === "" || areaName.trim() === "" || networkType.trim() === "") {
                                showNotification("The fields cannot be empty", "error");
                                return;
                            }

                            await optionOnClick(
                                `${config.server}${config.routers_base_path}/${routerId}${config.configuration_base_path}${config.configuration_ospf_interface_path}`,
                                "POST",
                                {
                                    credentials: {},
                                    interface_name: interfaceName,
                                    area_name: areaName,
                                    network_type: networkType,
                                    cost: cost
                                },
                                message => showNotification(message, 'success'),
                                message => showNotification(message, "error")
                            )

                        }
                    )

                }

            },
            {
                name: "Add OSPF Network",
                onClick: async () => {

                    closeMenu();
                    openModal(
                        "Add OSPF Network",
                        [
                            { name: "network", label: "Network" },
                            { name: "prefix", label: "Prefix" },
                            { name: "area_name", label: "Area Name" }
                        ],
                        async data => {

                            const network = data.get("network");
                            const prefix = data.get("prefix");
                            const areaName = data.get("area_name");

                            if (!Number.isInteger(Number(prefix))) {
                                showNotification("The prefix has to be a number between 0 and 32", "error");
                                return
                            }

                            if (network.trim() === "" || areaName.trim() === "") {
                                showNotification("The fields cannot be empty", "error");
                                return;
                            }

                            await optionOnClick(
                                `${config.server}${config.routers_base_path}/${routerId}${config.configuration_base_path}${config.configuration_ospf_network_path}`,
                                "POST",
                                {
                                    credentials: {},
                                    network : network,
                                    prefix: prefix,
                                    area_name: areaName
                                },
                                message => showNotification(message, 'success'),
                                message => showNotification(message, "error")
                            )

                        }
                    )

                }

            }

        ])

    })

}

function menuEvent(group, side) {

    group.on("contextmenu", (event, d) => {

        event.preventDefault();

        const offsetX = 0;
        const offsetY = 10;
        const x = event.clientX + offsetX;
        const y = event.clientY + offsetY;
        const routerId = side === "source" ? d.source.id : d.target.id;
        const interfaceName = side === "source" ? d.source_interface.name : d.target_interface.name;
        const group = d3.select(event.currentTarget.parentNode);

        openMenu(x, y, [
            {
                name: "Enable Interface",
                onClick: async () => {

                    await optionOnClick(
                        `${config.server}${config.routers_base_path}/${routerId}${config.configuration_base_path}${config.configuration_interfaces_path}/${interfaceName}${config.configuration_interfaces_enable_path}`,
                        "POST",
                        {},
                        (message) => {
                            showNotification(message, 'success');
                            group.select("circle").attr("fill", "var(--green)");
                            group.select("text").attr("fill", "var(--green)");
                        },
                        (message) => showNotification(message, "error")
                    )

                }
            },
            {
                name: "Disable Interface",
                onClick: async () => {

                    await optionOnClick(
                        `${config.server}${config.routers_base_path}/${routerId}${config.configuration_base_path}${config.configuration_interfaces_path}/${interfaceName}${config.configuration_interfaces_disable_path}`,
                        "POST",
                        {},
                        (message) => {
                            showNotification(message, 'success');
                            group.select("circle").attr("fill", "var(--red)");
                            group.select("text").attr("fill", "var(--red)");
                        },
                        (message) => showNotification(message, "error")
                    )

                }
            },
            {
                name: "Remove IP Address",
                onClick: async () => {

                    await optionOnClick(
                        `${config.server}${config.routers_base_path}/${routerId}${config.configuration_base_path}${config.configuration_address_path}`,
                        "DELETE",
                        {
                            credentials: {},
                            interface_name: interfaceName
                        },
                        (message) => showNotification(message, 'success'),
                        (message) => showNotification(message, "error")
                    )

                }
            },
            {
                name: "Set IP Address",
                onClick: async () => {

                    closeMenu();
                    openModal(
                        "Set  IP Address",
                        [
                            { name: "ip_address", label: "IP Address" },
                            { name: "prefix", label: "Prefix" }
                        ],
                        async data => {

                            const ipAddress = data.get("ip_address");
                            const prefix = data.get("prefix");

                            if (ipAddress.trim() === "" || prefix.trim() === "") {
                                showNotification("The fields cannot be empty", "error");
                                return;
                            }

                            await optionOnClick(
                                `${config.server}${config.routers_base_path}/${routerId}${config.configuration_base_path}${config.configuration_address_path}`,
                                "POST",
                                {
                                    credentials: {},
                                    interface_name: interfaceName,
                                    ip_address: ipAddress,
                                    prefix: prefix
                                },
                                (message) => showNotification(message, 'success'),
                                (message) => showNotification(message, "error")
                            )

                        }
                    )

                }

            },
            {
                name: "Create DHCP Server",
                onClick: async () => {

                    closeMenu();
                    openModal(
                        "Create DHCP Server",
                        [
                            { name: "name", label: "Server name" },
                            { name: "pool_name", label: "Pool name" }
                        ],
                        async data => {

                            const name = data.get("name");
                            const poolName = data.get("pool_name");

                            if (name.trim() === "" || poolName.trim() === "") {
                                showNotification("The fields cannot be empty", "error");
                                return;
                            }

                            await optionOnClick(
                                `${config.server}${config.routers_base_path}/${routerId}${config.configuration_base_path}${config.configuration_dhcp_server_path}/${name}`,
                                "POST",
                                {
                                    credentials: {},
                                    pool_name: poolName,
                                    interface_name: interfaceName
                                },
                                message => showNotification(message, 'success'),
                                message => showNotification(message, "error")
                            )

                        }
                    )

                }
            },
            {
                name: "Create DHCP Server Relay",
                onClick: async () => {

                    closeMenu();
                    openModal(
                        "Create DHCP Server Relay",
                        [
                            { name: "name", label: "Server name" },
                            { name: "pool_name", label: "Pool name" },
                            { name: "relay_address", label: "Relay address"}
                        ],
                        async data => {

                            const name = data.get("name");
                            const poolName = data.get("pool_name");
                            const relayAddress = data.get("relay_address");

                            if (name.trim() === "" || poolName.trim() === "" || relayAddress.trim() === "") {
                                showNotification("The fields cannot be empty", "error");
                                return;
                            }

                            await optionOnClick(
                                `${config.server}${config.routers_base_path}/${routerId}${config.configuration_base_path}${config.configuration_dhcp_server_relay_path}/${name}`,
                                "POST",
                                {
                                    credentials: {},
                                    pool_name: poolName,
                                    interface_name: interfaceName,
                                    relay_address: relayAddress
                                },
                                message => showNotification(message, 'success'),
                                message => showNotification(message, "error")
                            )

                        }
                    )

                }
            },
            {
                name: "Create DHCP Relay",
                onClick: async () => {

                    closeMenu();
                    openModal(
                        "Create DHCP Relay",
                        [
                            { name: "name", label: "Name" },
                            { name: "server_address", label: "Server address"}
                        ],
                        async data => {

                            const name = data.get("name");
                            const serverAddress = data.get("server_address");

                            if (name.trim() === "" || serverAddress.trim() === "") {
                                showNotification("The fields cannot be empty", "error");
                                return;
                            }

                            await optionOnClick(
                                `${config.server}${config.routers_base_path}/${routerId}${config.configuration_base_path}${config.configuration_dhcp_relay_path}/${name}`,
                                "POST",
                                {
                                    credentials: {},
                                    interface_name: interfaceName,
                                    server_address: serverAddress
                                },
                                message => showNotification(message, 'success'),
                                message => showNotification(message, "error")
                            )

                        }
                    )

                }
            }
        ])

    })

}

async function optionOnClick(url, method, body, onSuccess, onError) {

    closeMenu();
    closeModal()
    showNotification("Please, wait...", 'success');

    try {

        const response = await fetch(url, {
            method: method,
            credentials: "include",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(body)
        });

        const json = await response.json();

        if (!response.ok) {
            onError(json.message || "An error occurred")
            return
        }

        onSuccess(json.message)
    } catch (e) {
        onError("An error occurred")
    }

}