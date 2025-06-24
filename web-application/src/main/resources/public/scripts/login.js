window.addEventListener('load', init)

function init() {

    const button = document.getElementById('login')
    button.addEventListener('click', (e) => submitLogin(e))

}

async function submitLogin(e) {

    e.preventDefault()

    const username = document.getElementById('username').value
    const password = document.getElementById('password').value

    if (username.trim() === '' || password.trim() === '') {
        showNotification("Please enter username and password", "error")
        return
    }

    try {

        const response = await fetch("/api/v1/auth/login", {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ username, password })
        })

        if (!response.ok) {
            showNotification("Failed to login", "error")
            return
        }

        showNotification("Successfully logged in", "success")
        window.location.href = "/"

    } catch (error) {
        showNotification("Failed to login", "error")
    }

}