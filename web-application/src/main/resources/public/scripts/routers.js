window.addEventListener('load', init)

function init() {

    const routers = document.querySelectorAll(".router");

    for (let router of routers) {

        const deleteButton = router.querySelector(".delete");

        if (deleteButton)
            deleteButton.addEventListener("click", event => deleteHandler(event, router));

        const updateButton = router.querySelector(".update");

        if (updateButton)
            updateButton.addEventListener("click", event => updateHandler(event, router));

    }

    document.getElementById("open-modal").addEventListener("click", () => {
        openModal(
            "Create Router",
            [
                { name: "ip_address", label: "IP Address" },
                { name: "model", label: "Model" },
                { name: "vendor", label: "Vendor" }
            ],
            async data => {

                const ipAddress = data.get("ip_address");
                const model = data.get("model");
                const vendor = data.get("vendor");

                if (ipAddress.trim() === "" || model.trim() === "" || vendor.trim() === "") {
                    showNotification("The fields cannot be empty", "error");
                    return;
                }

                const response = await fetch(`${config.server}${config.routers_base_path}`, {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json"
                    },
                    body: JSON.stringify(
                        {
                            ip_address: ipAddress,
                            model: model,
                            vendor: vendor
                        }
                    )
                })

                const json = await response.json()

                if (!response.ok) {
                    showNotification(json.message, "error");
                    return;
                }

                showNotification(json.message, "success");

                const router = json.data

                const routerDiv = document.createElement("div");
                routerDiv.classList.add("router");
                routerDiv.setAttribute("key", router.id)

                const ipAddressLabel = document.createElement("label");
                ipAddressLabel.classList.add("ip_address")

                const ipAddressSpan = document.createElement("span");
                ipAddressSpan.textContent = "IP Address";

                const ipAddressInput = document.createElement("input");
                ipAddressInput.type = "text";
                ipAddressInput.value = router.ip_address;

                ipAddressLabel.appendChild(ipAddressSpan);
                ipAddressLabel.appendChild(ipAddressInput);
                routerDiv.append(ipAddressLabel);

                const modelLabel = document.createElement("label");
                modelLabel.classList.add("model")

                const modelSpan = document.createElement("span");
                modelSpan.textContent = "Model";

                const modelInput = document.createElement("input");
                modelInput.type = "text";
                modelInput.value = router.model;

                modelLabel.appendChild(modelSpan);
                modelLabel.appendChild(modelInput);
                routerDiv.append(modelLabel);

                const vendorLabel = document.createElement("label");
                vendorLabel.classList.add("vendor")

                const vendorSpan = document.createElement("span");
                vendorSpan.textContent = "Vendor";

                const vendorInput = document.createElement("input");
                vendorInput.type = "text";
                vendorInput.value = router.vendor;

                vendorLabel.appendChild(vendorSpan);
                vendorLabel.appendChild(vendorInput);
                routerDiv.append(vendorLabel);

                const buttons = document.createElement("div");
                buttons.classList.add("buttons");

                const updateBtn = document.createElement("button")
                updateBtn.classList.add("update")
                updateBtn.textContent = "Update";
                updateBtn.onclick = event => updateHandler(event, routerDiv);
                buttons.appendChild(updateBtn);

                const deleteBtn = document.createElement("button")
                deleteBtn.classList.add("delete")
                deleteBtn.textContent = "Delete";
                deleteBtn.onclick = event => deleteHandler(event, routerDiv);
                buttons.appendChild(deleteBtn);

                routerDiv.appendChild(buttons);

                const routersDiv = document.querySelector(".routers");
                routersDiv.append(routerDiv);

                closeModal()
            }

        );
    });

}

async function updateHandler(event, router) {

    const id = router.getAttribute("key");
    const ipAddress = router.querySelector(".ip_address input").value;
    const model = router.querySelector(".model input").value;
    const vendor = router.querySelector(".vendor input").value;

    const response = await fetch(`${config.server}${config.routers_base_path}/${id}`, {
        method: "PUT",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(
            {
                ip_address: ipAddress,
                model: model,
                vendor: vendor
            }
        )
    })

    const json = await response.json();

    if (!response.ok) {
        showNotification(json.message, "error");
        return null;
    }

    showNotification(json.message, "success");
}

async function deleteHandler(event, router) {

    const id = router.getAttribute('key');

    const response = await fetch(`${config.server}${config.routers_base_path}/${id}`, {
        method: "DELETE"
    });

    const json = await response.json();

    if (!response.ok) {
        showNotification(json.message, "error");
        return null;
    }

    router.remove();
    showNotification(json.message, "success");
}