const openBtn = document.getElementById("open-popup");
const closeBtn = document.getElementById("close");
const enterBtn = document.getElementById("enter");

openBtn.addEventListener("click", () => {
    container.classList.add("open");
});
closeBtn.addEventListener("click", () => {
    container.classList.remove("open");
});