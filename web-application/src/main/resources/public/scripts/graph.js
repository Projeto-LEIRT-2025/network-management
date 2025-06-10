import * as d3 from "https://cdn.jsdelivr.net/npm/d3@7/+esm";

window.addEventListener("load", init)

async function init() {

    await loadGraph({})

}

async function loadGraph(credentials) {

    const response = await fetch("http://localhost:8080/api/v1/routers/configuration/network", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(credentials),
    })

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

                        const username = data.get("username");
                        const password = data.get("password");

                        theCredentials[routerId] = {
                            username,
                            password
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
        showNotification("An error occurred while retrieving the network")
        return
    }

    const json = await response.json();
    const nodes = json.data.nodes
    const links = json.data.edges

    const graphElement = document.getElementById("graph");
    graphElement.style.justifyContent = "start";
    graphElement.innerHTML = ""

    if (nodes.length === 0) {
        graphElement.style.alignItems = "center";
        graphElement.style.justifyContent = "center";
        graphElement.innerHTML = "<h2 style='opacity: 0.8'>There is no router in the network</h2>"
        return
    }

    const graphHeader = document.createElement("div")
    graphHeader.classList.add("graph-header");
    graphHeader.innerHTML = `
        <button id="toggle-labels">Toggle Labels</button>
        <div class="zoom">
            <span id="zoom-value">1.00</span>
            <span id="zoom-in">+</span>
            <span id="zoom-out">-</span>
        </div>
    `
    graphElement.appendChild(graphHeader);

    const graphContentElement = document.createElement("div")
    graphContentElement.classList.add("graph-content")
    graphElement.appendChild(graphContentElement);

    const svgElement = document.createElementNS("http://www.w3.org/2000/svg", "svg");

    svgElement.setAttribute("width", "1280");
    svgElement.setAttribute("height", "720");
    graphContentElement.appendChild(svgElement);

    document.getElementById("toggle-labels").addEventListener("click", () => {

        const labelsVisible = d3.selectAll(".labels text").style("display") !== "none";

        d3.selectAll(".labels text")
            .style("display", labelsVisible ? "none" : "block");
    });

    const maxZoom = 2;
    const minZoom = 0.5;
    const svg = d3.select("svg");
    const zoomGroup = svg.append("g");
    const zoom = d3.zoom()
        .scaleExtent([minZoom, maxZoom])
        .on("zoom", (event) => {
            zoomGroup.attr("transform", event.transform);
            document.getElementById("zoom-value").innerHTML = event.transform.k.toFixed(2);
        });


    document.getElementById("zoom-in").addEventListener("click", () => {

        const transform = d3.zoomTransform(svg.node());
        const newScale = Math.min(transform.k + 0.1, maxZoom);

        svg.transition()
            .duration(300)
            .call(
                zoom.transform,
                d3.zoomIdentity.translate(transform.x, transform.y).scale(newScale)
            );

    })

    document.getElementById("zoom-out").addEventListener("click", () => {

        const transform = d3.zoomTransform(svg.node());
        const newScale = Math.max(transform.k - 0.1, minZoom);

        svg.transition()
            .duration(300)
            .call(
                zoom.transform,
                d3.zoomIdentity.translate(transform.x, transform.y).scale(newScale)
            );

    })

    svg.call(zoom);

    const width = +svg.attr("width");
    const height = +svg.attr("height");
    const distance = 100
    const degrees = {};

    nodes.forEach(node => degrees[node.id] = 0);
    links.forEach(link => {
        degrees[link.source.id || link.source]++;
        degrees[link.target.id || link.target]++;
    });

    const sortedNodes = [...nodes].sort((a, b) => degrees[b.id] - degrees[a.id]);
    const nodesPerRow = Math.floor(width / distance);
    Math.ceil(nodes.length / nodesPerRow);
    sortedNodes.forEach((node, i) => {

        const row = Math.floor(i / nodesPerRow);
        const col = i % nodesPerRow;
        const nodesInThisRow = Math.min(nodesPerRow, nodes.length - row * nodesPerRow);
        const startX = (width - (nodesInThisRow - 1) * distance) / 2;

        node.x = startX + col * distance;
        node.y = distance + row * distance;

        node.fx = node.x;
        node.fy = node.y;
    });

    const simulation = d3.forceSimulation(nodes)
        .force("link", d3.forceLink(links).id(d => d.id).distance(200))
        .force("charge", d3.forceManyBody().strength(-300))
        .force("center", d3.forceCenter(width / 2, height / 2));

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
        .attr("r", 20)
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
    });

}

