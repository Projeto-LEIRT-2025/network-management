import * as d3 from "https://cdn.jsdelivr.net/npm/d3@7/+esm";

window.addEventListener("load", init)

function init() {

    initCharts()

    const updateBtn = document.getElementById("update");
    const lastHourBtn = document.getElementById("last-hour");
    const last3HoursBtn = document.getElementById("last-3-hours");

    updateBtn.addEventListener("click", () => updateIntervalWithCurrentDuration());
    lastHourBtn.addEventListener("click", () => updateIntervalFromNow(60 * 60 * 1000));
    last3HoursBtn.addEventListener("click", () => updateIntervalFromNow(3 * 60 * 60 * 1000));

}

function updateIntervalWithCurrentDuration() {
    const url = new URL(window.location);
    const params = url.searchParams;
    const startStr = params.get('start');
    const endStr = params.get('end');

    if (!startStr || !endStr) return;

    const startDate = new Date(startStr);
    const endDate = new Date(endStr);

    const intervalMs = endDate - startDate;
    if (intervalMs <= 0) return;

    const newEnd = new Date();
    const newStart = new Date(newEnd.getTime() - intervalMs);

    params.set('start', newStart.toISOString());
    params.set('end', newEnd.toISOString());

    window.history.replaceState(null, '', url);
    location.reload();
}

function updateIntervalFromNow(intervalMs) {
    const url = new URL(window.location);
    const params = url.searchParams;

    const newEnd = new Date();
    const newStart = new Date(newEnd.getTime() - intervalMs);

    params.set('start', newStart.toISOString());
    params.set('end', newEnd.toISOString());

    window.history.replaceState(null, '', url);
    location.reload();
}

function initCharts() {

    const rawData = window.interfacesStats;

    const metrics = [
        { key: "bytesIn", label: "Bytes In" },
        { key: "bytesOut", label: "Bytes Out" },
        { key: "packetsIn", label: "Packets In" },
        { key: "packetsOut", label: "Packets Out" },
        { key: "discardedPacketsIn", label: "Discarded Packets In" },
        { key: "discardedPacketsOut", label: "Discarded Packets Out" },
        { key: "errorPacketsIn", label: "Error Packets In" },
        { key: "errorPacketsOut", label: "Error Packets Out" }
    ];

    const metricsElement = document.getElementById("metrics");

    const groupedByInterface = d3.group(rawData, d => d.interfaceName);

    metrics.forEach(({ key, label }) => {

        const linesData = [];

        for (const [interfaceName, records] of groupedByInterface) {

            const points = records.map(d => ({
                date: new Date(d.timestamp),
                value: d[key]
            }));

            linesData.push({ interfaceName, values: points });
        }

        buildMultiLineChart(linesData, label);
    });

    function buildMultiLineChart(linesData, label) {
        const width = 928;
        const height = 500;
        const marginTop = 20;
        const marginRight = 30;
        const marginBottom = 30;
        const marginLeft = 50;

        const allDates = linesData.flatMap(d => d.values.map(v => v.date));
        const allValues = linesData.flatMap(d => d.values.map(v => v.value));

        const x = d3.scaleUtc(d3.extent(allDates), [marginLeft, width - marginRight]);
        const y = d3.scaleLinear([0, d3.max(allValues)], [height - marginBottom, marginTop]);

        const color = d3.scaleOrdinal()
            .domain(linesData.map(d => d.interfaceName))
            .range(d3.schemeCategory10);

        const line = d3.line()
            .x(d => x(d.date))
            .y(d => y(d.value));

        const svg = d3.create("svg")
            .attr("width", width)
            .attr("height", height)
            .attr("viewBox", [0, 0, width, height])
            .attr("style", "max-width: 100%; height: auto; height: intrinsic;")
            .style("width", "45%")
            .style("transition", "0.5s");

        svg.on("click", () => {

            const currentWidth = svg.style("width");

            if (currentWidth === "45%") {
                svg.style("width", "100%").style("height", "70vh");
            } else {
                svg.style("width", "45%").style("height", "auto");
            }

        });

        svg.append("g")
            .attr("transform", `translate(0,${height - marginBottom})`)
            .call(
                d3.axisBottom(x)
                    .ticks(d3.timeMinute.every(1))
                    .tickFormat(d3.timeFormat("%H:%M"))
                    .tickSizeOuter(0)
            );

        svg.append("g")
            .attr("transform", `translate(${marginLeft},0)`)
            .call(d3.axisLeft(y).ticks(height / 40))
            .call(g => g.select(".domain").remove())
            .call(g => g.selectAll(".tick line").clone()
                .attr("x2", width - marginLeft - marginRight)
                .attr("stroke-opacity", 0.1))
            .call(g => g.append("text")
                .attr("x", -marginLeft)
                .attr("y", 10)
                .attr("fill", "currentColor")
                .attr("text-anchor", "start")
                .text(label));

        svg.selectAll(".line")
            .data(linesData)
            .join("path")
            .attr("fill", "none")
            .attr("stroke", d => color(d.interfaceName))
            .attr("stroke-width", 1.5)
            .attr("d", d => line(d.values));

        const legend = svg.append("g")
            .attr("transform", `translate(${width - marginRight - 120},${marginTop})`)
            .attr("font-size", 12);

        linesData.forEach((d, i) => {
            const g = legend.append("g").attr("transform", `translate(0, ${i * 20})`);
            g.append("rect")
                .attr("width", 12)
                .attr("height", 12)
                .attr("fill", color(d.interfaceName));
            g.append("text")
                .attr("x", 16)
                .attr("y", 10)
                .attr("fill", "var(--primary)")
                .text(d.interfaceName);
        });

        metricsElement.appendChild(svg.node());
    }

}