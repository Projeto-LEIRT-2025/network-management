import * as d3 from "https://cdn.jsdelivr.net/npm/d3@7/+esm";

window.addEventListener("load", init);

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

    const rawData = window.deviceStats;

    function parseUptime(uptimeStr) {
        const parts = uptimeStr.split(':');
        if (parts.length !== 3) return 0;
        const h = +parts[0];
        const m = +parts[1];
        const s = parseFloat(parts[2]);
        return h * 3600 + m * 60 + s;
    }

    const data = rawData.map(d => ({
        date: new Date(d.timestamp),
        cpuUsage: d.cpuUsage * 100,
        freeMemory: d.freeMemory,
        memoryUsage: d.memoryUsage,
        totalMemory: d.totalMemory,
        uptimeSeconds: parseUptime(d.uptime)
    }));

    const metrics = [
        { key: "cpuUsage", label: "CPU Usage (%)" },
        { key: "freeMemory", label: "Free Memory (MiB)" },
        { key: "memoryUsage", label: "Memory Usage (MiB)" },
        { key: "totalMemory", label: "Total Memory (MiB)" },
        { key: "uptimeSeconds", label: "Uptime (seconds)" }
    ];

    const metricsElement = document.getElementById("metrics");

    metrics.forEach(({ key, label }) => {
        const metricData = data.map(d => ({ date: d.date, value: d[key] }));
        buildChart(metricData, label);
    });

    function buildChart(data, label) {

        const width = 928;
        const height = 500;
        const marginTop = 20;
        const marginRight = 30;
        const marginBottom = 30;
        const marginLeft = 40;

        const x = d3.scaleUtc(d3.extent(data, d => d.date), [marginLeft, width - marginRight]);
        const y = d3.scaleLinear([0, d3.max(data, d => d.value)], [height - marginBottom, marginTop]);

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

        svg.append("path")
            .attr("fill", "none")
            .attr("stroke", "var(--line)")
            .attr("stroke-width", 1.5)
            .attr("d", line(data));

        metricsElement.appendChild(svg.node());
    }

}