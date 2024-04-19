const openBtn = document.getElementById("create");
const closeBtn = document.getElementById("cancel");
const enterBtn = document.getElementById("enter");
const inputFile = document.getElementById('input-file');

openBtn.addEventListener("click", () => {
    create.classList.add("open");
});

closeBtn.addEventListener("click", () => {
    create.classList.remove("open");
});

inputFile.addEventListener('change', (event) => {
    const file = event.target.files[0];
    const reader = new FileReader();
  
    reader.onload = (event) => {
      const image = new Image();
      image.src = event.target.result;
  
      // Add the image to the page
      document.body.appendChild(image);
    };
  
    reader.readAsDataURL(file);
  });