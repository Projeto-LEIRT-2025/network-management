import * as d3 from "https://cdn.jsdelivr.net/npm/d3@7/+esm";

window.addEventListener("load", init)

async function init() {

    const response = await fetch("http://localhost:8080/api/v1/routers/configuration/network")

    if (!response.ok) {
        showNotification("An error occurred while retrieving the network")
        return
    }

    const json = await response.json();

    const graphElement = document.getElementById("graph");
    graphElement.style.justifyContent = "start";
    graphElement.innerHTML = ""

    const graphHeader = document.createElement("div")
    graphHeader.classList.add("graph-header");
    graphHeader.innerHTML = '<button id="toggle-labels">Toggle Labels</button>'
    graphElement.appendChild(graphHeader);

    const graphContentElement = document.createElement("div")
    graphContentElement.classList.add("graph-content")
    graphElement.appendChild(graphContentElement);

    const svgElement = document.createElementNS("http://www.w3.org/2000/svg", "svg");

    svgElement.setAttribute("width", "600");
    svgElement.setAttribute("height", "400");
    graphContentElement.appendChild(svgElement);

    document.getElementById("toggle-labels").addEventListener("click", () => {

        const labelsVisible = d3.selectAll(".labels text").style("display") !== "none";

        d3.selectAll(".labels text")
            .style("display", labelsVisible ? "none" : "block");
    });

    const nodes = json.data.nodes
    const links = json.data.edges

    console.log(links)


    const svg = d3.select("svg");
    const width = +svg.attr("width");
    const height = +svg.attr("height");

    const simulation = d3.forceSimulation(nodes)
        .force("link", d3.forceLink(links).id(d => d.id).distance(200))
        .force("charge", d3.forceManyBody().strength(-300))
        .force("center", d3.forceCenter(width / 2, height / 2));

    const link = svg.append("g")
        .attr("stroke", "#999")
        .attr("stroke-opacity", 0.6)
        .selectAll("line")
        .data(links)
        .join("line")
        .attr("stroke-width", 2);

    const node = svg.append("g")
        .attr("stroke", "#fff")
        .attr("stroke-width", 1.5)
        .selectAll("circle")
        .data(nodes)
        .join("circle")
        .attr("r", 20)
        .attr("fill", "var(--node)");

    const label = svg.append("g")
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