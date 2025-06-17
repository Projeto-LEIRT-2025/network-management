window.addEventListener("load", init)

window.addEventListener("click", (event) => {
    const menu = document.getElementById("menu");
    if (!menu.contains(event.target)) {
        closeMenu();
    }
});

function closeMenu() {
    const menu = document.getElementById("menu");
    menu.style.display = "none";
}

function openMenu(x, y, options) {

    const menu = document.getElementById("menu");
    menu.innerHTML = '';
    menu.style.display = "block";
    menu.style.position = "absolute";
    menu.style.left = `${x}px`;
    menu.style.top = `${y}px`;

    options.forEach(option => {

        const optionDiv = document.createElement("div");
        optionDiv.classList.add("option");
        menu.appendChild(optionDiv);

        optionDiv.innerHTML = `${option.name}`;
        optionDiv.addEventListener("click", option.onClick);

    })

}