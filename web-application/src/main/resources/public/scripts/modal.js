window.addEventListener("load", init)

function init() {

    const closeModalElement = document.getElementById("close-modal");

    closeModalElement.addEventListener("click", closeModal);

}

function closeModal() {

    const modal = document.getElementById("modal");

    modal.style.display = "none";

}

function openModal(title, fields, onSubmit) {

    const modal = document.getElementById("modal");
    const modalTitle = document.getElementById("modal-title");
    const modalForm = document.getElementById("modal-form");

    modalTitle.textContent = title;
    modalForm.innerHTML = "";

    fields.forEach(field => {

        const label = document.createElement("label");
        label.textContent = field.label;

        const input = document.createElement("input");
        input.name = field.name;
        input.type = field.type || "text";

        if (field.value)
            input.value = field.value;

        label.appendChild(input);
        modalForm.appendChild(label);
    });

    const submit = document.createElement("button");
    submit.type = "submit";
    submit.textContent = "Submit";

    modalForm.appendChild(submit);

    modalForm.onsubmit = async (event) => {

        event.preventDefault();

        const formData = new Map();
        const inputs = modalForm.querySelectorAll("input");

        inputs.forEach(input => {
            formData.set(input.name, input.value);
        });

        onSubmit(formData);
    };

    modal.style.display = "block";
}