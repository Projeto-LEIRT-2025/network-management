import * as d3 from "https://cdn.jsdelivr.net/npm/d3@7/+esm";

window.addEventListener("load", init);

async function init() {
    await loadGraph({});
}

async function loadGraph(credentials) {

    const response = await fetch(`${config.server}${config.routers_base_path}${config.configuration_base_path}${config.routers_network_path}`, {
        method: "POST",
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

    if (!response.ok) {
        showNotification("An error occurred while retrieving the network");
        return;
    }

    const { nodes, edges: links } = (await response.json()).data;

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
        });

    svg.call(zoom);

    document.getElementById("zoom-in").addEventListener("click", () => zoomBy(svg, zoom, 0.1, maxZoom));
    document.getElementById("zoom-out").addEventListener("click", () => zoomBy(svg, zoom, -0.1, minZoom));
}

function zoomBy(svg, zoom, delta, limit) {
    const t = d3.zoomTransform(svg.node());
    const newScale = delta > 0 ? Math.min(t.k + delta, limit) : Math.max(t.k + delta, limit);
    svg.transition().duration(300).call(zoom.transform, d3.zoomIdentity.translate(t.x, t.y).scale(newScale));
}

function renderGraph({ svg, zoomGroup, nodes, links, width, height }) {

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
    menuEvent(sourceGroup)

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
    menuEvent(targetGroup)

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

function menuEvent(group) {

    group.on("contextmenu", (event, d) => {

        event.preventDefault();

        const offsetX = 0;
        const offsetY = 10;
        const x = event.clientX + offsetX;
        const y = event.clientY + offsetY;
        const routerId = d.source.id;
        const interfaceName = d.target_interface.name;
        const group = d3.select(event.currentTarget.parentNode);

        openMenu(x, y, [
            {
                name: "Habilitar Interface",
                onClick: async () => {

                    showNotification("Please, wait...", 'success')
                    closeMenu()
                    const response = await fetch(`${config.server}${config.routers_base_path}/${routerId}${config.configuration_base_path}${config.configuration_interfaces_path}/${interfaceName}${config.configuration_interfaces_enable_path}`, {
                        method: "POST",
                        headers: {
                            "Content-Type": "application/json"
                        },
                        body: JSON.stringify({})
                    });

                    if (response.ok) {

                        showNotification("Interface enabled", 'success')

                        group.select("circle").attr("fill", "var(--green)");
                        group.select("text").attr("fill", "var(--green)");
                    }else {
                        const json = await response.json();
                        showNotification(json.message, 'error')
                    }
                }
            },
            {
                name: "Desabilitar Interface",
                onClick: async () => {

                    showNotification("Please, wait...", 'success')
                    closeMenu()
                    const response = await fetch(`${config.server}${config.routers_base_path}/${routerId}${config.configuration_base_path}${config.configuration_interfaces_path}/${interfaceName}${config.configuration.configuration_interfaces_disable_path}`, {
                        method: "POST",
                        headers: {
                            "Content-Type": "application/json"
                        },
                        body: JSON.stringify({})
                    });

                    if (response.ok) {

                        showNotification("Interface disabled", 'success')

                        group.select("circle").attr("fill", "var(--red)");
                        group.select("text").attr("fill", "var(--red)");
                    }else {
                        const json = await response.json();
                        showNotification(json.message, 'error')
                    }

                }
            }
        ])

    })

}