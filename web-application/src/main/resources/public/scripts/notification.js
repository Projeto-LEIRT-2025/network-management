function showNotification(message, type) {

    const notification = document.getElementById("notification");

    notification.classList.value = "";
    notification.textContent = message;
    notification.classList.add(type, "show");

    setTimeout(() => {

        notification.classList.remove("show");
        notification.classList.add("hide");

    }, 3000);

}