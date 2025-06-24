window.addEventListener('load', init)

function init() {

    const enablePluginButtons = document.querySelectorAll('.enable-button');

    enablePluginButtons.forEach(button => {
        const name = button.getAttribute('data-name');
        button.addEventListener('click', () => enablePlugin(button, name))
    });

    const disablePluginButtons = document.querySelectorAll('.disable-button');

    disablePluginButtons.forEach(button => {
        const name = button.getAttribute('data-name');
        button.addEventListener('click', () => disablePlugin(button, name))
    });

    const deletePluginButtons = document.querySelectorAll('.delete-button');

    deletePluginButtons.forEach(button => {
        const name = button.getAttribute('data-name');
        button.addEventListener('click', () => deletePlugin(button, name))
    });

    const uploadButton = document.getElementById('upload');
    const fileInput = document.getElementById('fileInput');
    uploadButton.addEventListener('click', () => {
        fileInput.click();
    });

    fileInput.addEventListener('change', () => {
        const file = fileInput.files[0];
        if (file) {
            uploadFile(file);
        }
    });

}

async function deletePlugin(button, name) {

    try {

        const response= await fetch(`${config.server}${config.plugins_base_path}/${name}`, {
            method: 'DELETE'
        })

        const json = await response.json()

        if (!response.ok) {
            showNotification(json.message, "error")
            return
        }

        showNotification("Plugin deleted successfully.", 'success');

        const row = button.closest('tr');

        if (row)
            row.remove();

    } catch (e) {
        showNotification("An error occurred", "error")
    }

}

async function enablePlugin(button, name) {

    try {

        const response = await fetch(`${config.server}${config.plugins_base_path}/${name}/enable`, {
            method: 'POST'
        })

        const json = await response.json()

        if (!response.ok) {
            showNotification(json.message, "error")
            return
        }

        showNotification("Plugin enabled successfully.", 'success');

        const newButton = document.createElement('button')
        newButton.className = 'disable-button'
        newButton.setAttribute('data-name', name)
        newButton.textContent = 'Disable'

        newButton.addEventListener('click', () => disablePlugin(newButton, name))

        button.parentNode.replaceChild(newButton, button)

    } catch (e) {
        showNotification("An error occurred", "error")
    }

}

async function disablePlugin(button, name) {

    try {

        const response = await fetch(`${config.server}${config.plugins_base_path}/${name}/disable`, {
            method: 'POST'
        })

        const json = await response.json()

        if (!response.ok) {
            showNotification(json.message, "error")
            return
        }

        showNotification("Plugin disabled successfully.", 'success');

        const newButton = document.createElement('button')
        newButton.className = 'enable-button'
        newButton.setAttribute('data-name', name)
        newButton.textContent = 'Enable'

        newButton.addEventListener('click', () => enablePlugin(newButton, name))

        button.parentNode.replaceChild(newButton, button)

    } catch (e) {
        showNotification("An error occurred", "error")
    }


}

async function uploadFile(file) {

    const formData = new FormData();
    formData.append('file', file);

    try {

        const response = await fetch(`${config.server}${config.plugins_base_path}/upload`, {
            method: 'POST',
            body: formData,
        })

        const json = await response.json()

        if (!response.ok) {
            showNotification(json.message, "error")
            return
        }

        const plugin = json.data;
        const tbody = document.querySelector('.plugins tbody');
        const tr = document.createElement('tr');

        tr.innerHTML = `
            <td>${plugin.name}</td>
            <td>${plugin.author}</td>
            <td>${plugin.description}</td>
            <td>
                <div class="buttons">
                    ${plugin.enabled ?
            `<button data-name="${plugin.name}" class="disable-button">Disable</button>` :
            `<button data-name="${plugin.name}" class="enable-button">Enable</button>`
        }
                    <button data-name="${plugin.name}" class="delete-button">Delete</button>
                </div>
            </td>
        `;

        tbody.appendChild(tr);

        const enableButton = tr.querySelector('.enable-button');
        if (enableButton) {
            enableButton.addEventListener('click', () => enablePlugin(enableButton, plugin.name));
        }

        const disableButton = tr.querySelector('.disable-button');
        if (disableButton) {
            disableButton.addEventListener('click', () => disablePlugin(disableButton, plugin.name));
        }

        const deleteButton = tr.querySelector('.delete-button');
        deleteButton.addEventListener('click', () => deletePlugin(deleteButton, plugin.name));

        showNotification('Plugin uploaded successfully', 'success');

    } catch(e) {
        showNotification("An error occurred", "error")
    }

}