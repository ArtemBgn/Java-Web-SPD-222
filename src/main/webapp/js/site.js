console.log("Script works");
document.addEventListener('DOMContentLoaded', function() {
    const signupButton = document.getElementById("signup-button");
    if(signupButton) { signupButton.onclick = signupButtonClick; }
    const prodButton = document.getElementById("addproduct_button");
    if(prodButton) { prodButton.onclick = prodButtonClick; }
    // шукаємо кнопку автентифікації, якщо знаходимо - додаємо обробник
    const authButton = document.getElementById("auth-button");
    if(authButton) { authButton.onclick = authButtonClick; }
// налаштування модальних вікон
    var elems = document.querySelectorAll('.modal');
    M.Modal.init(elems, {
        "opacity": 0.5, // Opacity of the modal overlay.
        "inDuration": 250, // Transition in duration in milliseconds.
        "outDuration": 250, // Transition out duration in milliseconds.
        "onOpenStart": null, // Callback function called before modal is opened.
        "onOpenEnd": null, // Callback function called after modal is opened.
        "onCloseStart": null, // Callback function called before modal is closed.
        "onCloseEnd": null, // Callback function called after modal is closed.
        "preventScrolling": true, // Prevent page from scrolling while modal is open.
        "dismissible": true, // Allow modal to be dismissed by keyboard or overlay click.
        "startingTop": '4%', // Starting top offset
        "endingTop": '10%' // Ending top offset
    });
    checkAuth();
});

function signupButtonClick(e) {
    //шукаємо блок для вставки повідомлення про помилку
    const errorDiv = document.getElementById('for-error-message');
    if(!errorDiv) {throw  "errorDiv form not found";}
    //шукаємо форму - батьківській елемент кнопки (e.target)
    const signupForm = e.target.closest('form');
    if(! signupForm) {throw "Signup form not found";}
    // всередені форми signupForm знаходимо елементи
    const nameInput = signupForm.querySelector('input[name="user-name"]');
    if(! nameInput) {throw "nameInput not found";}
    const emailInput = signupForm.querySelector('input[name="user-email"]');
    if(! emailInput) {throw "emailInput not found";}
    const passwordInput = signupForm.querySelector('input[name="user-password"]');
    if(! passwordInput) {throw "passwordInput not found";}
    const repeatpasswordInput = signupForm.querySelector('input[name="user-repeat"]');
    if(! repeatpasswordInput) {throw "repeatpasswordInput not found";}
    const avatarInput = signupForm.querySelector('input[name="user-avatar"]');
    if(! avatarInput) {throw "avatarInput not found";}

    //// Валідація даних
    let isFormValid = true ;
    if(nameInput.value == "") {
        nameInput.classList.remove("valid");
        nameInput.classList.add("invalid");
        isFormValid = false;
    }
    else {
        let abets = [" ","A","B","C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "'", "А", "Б", "В", "Г", "Ґ", "Д", "Е", "Є", "Ж", "З", "И", "І", "Ї", "Й", "К", "Л", "М", "Н", "О", "П", "Р", "С", "Т", "У", "Ф", "Х", "Ц", "Ч", "Ш", "Щ", "Ь", "Ю", "Я"];
        let nameuser2 = nameInput.value.toLocaleUpperCase();
        let nameuser = nameuser2.split('');
        var step;
        for (step = 0; step < nameuser.length; step++)
        {
            if(!abets.includes(nameuser[step]))
            {
                nameInput.classList.remove("valid");
                nameInput.classList.add("invalid");
                isFormValid = false;
            }
            else {
                nameInput.classList.remove("invalid");
                nameInput.classList.add("valid");
            }
        }
    }
    if(emailInput.value == "") {
        emailInput.classList.remove("valid");
        emailInput.classList.add("invalid");
        isFormValid = false;
    }
    else {
        var reg= /^[a-zA-Z][0-9a-zA-Z_]{2,21}@[a-zA-Z]{2,12}\.[a-zA-Z]{2,12}/i;
        if(!reg.test(emailInput.value))
        {
            emailInput.classList.remove("valid");
            emailInput.classList.add("invalid");
            isFormValid = false;
        }
        else {
            emailInput.classList.remove("invalid");
            emailInput.classList.add("valid");
        }
    }
    if(passwordInput.value == "") {
        passwordInput.classList.remove("valid");
        passwordInput.classList.add("invalid");
        isFormValid = false;
    }
    else {
        passwordInput.classList.remove("invalid");
        passwordInput.classList.add("valid");
    }
    if((repeatpasswordInput.value == "")||(passwordInput.value != repeatpasswordInput.value)) {
        repeatpasswordInput.classList.remove("valid");
        repeatpasswordInput.classList.add("invalid");
        isFormValid = false;
    }
    else {
        repeatpasswordInput.classList.remove("invalid");
        repeatpasswordInput.classList.add("valid");
    }

    if(avatarInput.value == "") {
        avatarInput.classList.remove("valid");
        avatarInput.classList.add("invalid");
        isFormValid = false;
    }
    else {
        var x = avatarInput.value.indexOf(".");
        var extension = avatarInput.value.slice(x+1);
        let extensions = ["bmp", "jpg", "jpeg", "gif", "png", "ico", "jfif"];
        //let exists = extensions.includes(extension);
        if(!extensions.includes(extension)) {
            avatarInput.classList.remove("valid");
            avatarInput.classList.add("invalid");
            isFormValid = false;
        }
        else {
            avatarInput.classList.remove("invalid");
            avatarInput.classList.add("valid");
        }
        //console.log(avatarInput.value + ' ' + extension + ' ' + exists);
    }

    if(!isFormValid) return;
    ///кінець валідації

    //Формуємо дані для передачі на бекенд
    const formData = new FormData();
    formData.append( "name-user", nameInput.value ) ;
    formData.append( "email-user", emailInput.value ) ;
    formData.append( "password-user", passwordInput.value ) ;
    if( avatarInput.files.length > 0 ) {
        formData.append( "avatar-user", avatarInput.files[0] ) ;
    }
    // передаємо - формуємо запит
    fetch(window.location.href, { method: 'POST', body: formData } )
        .then( r => {
            if(!r.ok) {
                console.log("УСПІШНО!!");
                errorDiv.innerText = "";
                window.alert( "Ви успішно зареєструвалися!" );
                window.location = '/Java_Web_SPD_222' ;
            }
            else {
                console.log("не успішно!!!!");
                passwordInput.value = "";
                repeatpasswordInput.value = "";
                errorDiv.classList.add("red-text");
                errorDiv.innerText = "Виникли помилки при реєстрації.\n Код помилки: " + r.status.toString();
                //window.alert( r.status );
            }
        });
}
function prodButtonClick(e) {
    //шукаємо форму - батьківській елемент кнопки (e.target)
    const prodForm = e.target.closest('form');
    if(!prodForm) {throw "Signup form not found";}
    // всередені форми signupForm знаходимо елементи
    //const nameInput = signupForm.querySelector('input[name="user-name"]');
    //if(! nameInput) {throw "nameInput not found";}
    //const emailInput = signupForm.querySelector('input[name="user-email"]');
    //if(! emailInput) {throw "emailInput not found";}
    //const passwordInput = signupForm.querySelector('input[name="user-password"]');
    //if(! passwordInput) {throw "passwordInput not found";}
    //const avatarInput = signupForm.querySelector('input[name="user-avatar"]');
    //if(! avatarInput) {throw "avatarInput not found";}

    const nameInput = document.getElementById("product_name");
    if(!nameInput) { throw "product_name not found"; }
    const priceInput = document.getElementById("product_price");
    if(!priceInput) { throw "product_price not found"; }
    const descriptionInput = document.getElementById("product_description");
    if(!descriptionInput) { throw "product_description not found"; }
    const categoryInput = prodForm.querySelector('input[name="product_category"]');
    if(!categoryInput) { throw "product_category not found"; }
    const photoInput = prodForm.querySelector('input[name="product_photo"]');
    if(!photoInput) {throw "photoInput not found";}


    //// Валідація даних - Home Work
    let isFormValid = true ;
    if(nameInput.value === "") {
        nameInput.classList.remove("valid");
        nameInput.classList.add("invalid");
        isFormValid = false;
    }
    else {
        let abets = [" ","A","B","C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "'", "А", "Б", "В", "Г", "Ґ", "Д", "Е", "Є", "Ж", "З", "И", "І", "Ї", "Й", "К", "Л", "М", "Н", "О", "П", "Р", "С", "Т", "У", "Ф", "Х", "Ц", "Ч", "Ш", "Щ", "Ь", "Ю", "Я"];
        let nameuser2 = nameInput.value.toLocaleUpperCase();
        let nameuser = nameuser2.split('');
        var step;
        for (step = 0; step < nameuser.length; step++)
        {
            if(!abets.includes(nameuser[step]))
            {
                nameInput.classList.remove("valid");
                nameInput.classList.add("invalid");
                isFormValid = false;
            }
            else {
                nameInput.classList.remove("invalid");
                nameInput.classList.add("valid");
            }
        }
    }
    if(priceInput.value === "") {
        priceInput.classList.remove("valid");
        priceInput.classList.add("invalid");
        isFormValid = false;
    }
    else {
        if(priceInput.value<=0)
        {
            priceInput.classList.remove("valid");
            priceInput.classList.add("invalid");
            isFormValid = false;
        }
        else {
            priceInput.classList.remove("invalid");
            priceInput.classList.add("valid");
        }
    }
    if(photoInput.value === "") {
        photoInput.classList.remove("valid");
        photoInput.classList.add("invalid");
        console.log("Завантаження продукту буде без ФОТО-!");
        //isFormValid = false;
    }
    else {
        var x = photoInput.value.indexOf(".");
        var extension = photoInput.value.slice(x+1);
        let extensions = ["bmp", "jpg", "jpeg", "gif", "png", "ico"];
        //let exists = extensions.includes(extension);
        if(!extensions.includes(extension)) {
            photoInput.classList.remove("valid");
            photoInput.classList.add("invalid");
            console.log("Завантаження продукту буде без ФОТО!");
            //isFormValid = false;
        }
        else {
            photoInput.classList.remove("invalid");
            photoInput.classList.add("valid");
        }
        //console.log(avatarInput.value + ' ' + extension + ' ' + exists);
    }

    if(!isFormValid) return;
    ///кінець валідації

    //Формуємо дані для передачі на бекенд
    const formData = new FormData();
    formData.append( "product_name", nameInput.value ) ;
    formData.append( "product_price", priceInput.value ) ;
    formData.append( "product_description", descriptionInput.value ) ;
    formData.append( "product_category", categoryInput.value ) ;
    if( photoInput.files.length > 0 ) {
        formData.append( "product_photo", photoInput.files[0] ) ;
    }//*/
    // передаємо - формуємо запит
    fetch(window.location.href, { method: 'POST', body: formData } )
        .then( r => r.json())
        .then( j => {
            console.log(j);
            /*if( j.status == 1) { // реєстрація успішна
                alert( 'реєстрація успішна' );
                window.location = '/' ; // переходимо на головну сторінку
            }
            else { // помилка реєстрації (повідомлення у полі message)
                allert( j.data.message );
            }*/
        } );
}
function getContext() {
    //window.location.pathname.split('/')[1]
    return window.location.pathname.split('/')[1];
}
function authButtonClick(e) {
    const emailInput = document.querySelector('input[name="auth-email"]');
    if( ! emailInput ) { throw "'auth-email' not found" ; }
    const passwordInput = document.querySelector('input[name="auth-password"]');
    if( ! passwordInput ) { throw "'auth-password' not found" ; }

    //console.log( emailInput.value, passwordInput.value ) ;
    fetch(`/${getContext()}/auth?email=${emailInput.value}&password=${passwordInput.value}`, {
        method: 'GET'
    })
        .then( r => r.json() )
        .then( j => {
            if(j.data == null || typeof j.data.token == "undefined") {
                document.getElementById("modal-auth-message").innerText = "У вході відмовлено";
            }
            //else if(j.data.email == emailInput.value) {}
            else {
                //авторизація токенами передбачає їх збереження з метою їх подальшого використання
                //для того щоб токени були доступні після перезавантаження
                //їх розміщують до постійного сховища браузера - localStorage...
                localStorage.setItem("auth-token", j.data.token);
                //localStorage.setItem("auth-email", "emailInput.value.toString()");
                window.location.reload();
            }
        } ) ;
}
 function checkAuth() {
     //...при завантажені сторінки перевіряємо наявність даних автентифікуції
     // у localStorage
     const authToken = localStorage.getItem("auth-token");
     if(authToken) {
         //перевіряємо на валідність і одержуємо дані про користувача
         fetch(`/${getContext()}/auth?token=${authToken}`, {
             method: 'POST'
         })
             .then( r => r.json() )
             .then( j => {
                 if(j.meta.status == 'success') {
                     // замінити кнопку входу
                     document.querySelector('[data-auth="avatar"]').innerHTML = `<a href="#auth-modal" class="modal-trigger"><img title="${j.data.name}" class="nav-avatar" src="/${getContext()}/img/avatar/${j.data.avatar}" /></a>`
                     const product = document.querySelector('[data-auth="product"]');
                     if(product) {
                         fetch(`/${getContext()}/product.jsp`)
                             .then(r=>r.text())
                             .then(t => {
                                 product.innerHTML = t;
                                 document.getElementById("add-product-button")
                                     .addEventListener('click', addProductClick);
                             });
                     }
                 }
             })
         //.then( console.log );
     }
 }
function addProductClick(e) {
    // збираємо дані з форми додавання продукту
    const form = e.target.closest('form');
    const name = form.querySelector("#product-name").value.trim();
    const price = form.querySelector("#product-price").value.trim();
    const description = form.querySelector("#product-description").value.trim();
    const fileInput = form.querySelector("#product-img");
    // Проводимо валідацію

    // Формуємо дані для передачі на сервер
    const formData = new FormData();
    formData.append("name", name);
    formData.append("price", price);
    formData.append("description", description);
    formData.append("image", fileInput.files[0]);
    formData.append("token", localStorage.getItem("auth-token"));
    // надсилаємо дані на сервер
    fetch(`/${getContext()}/shop-api`, {
        method: 'POST',
        body: formData
    })
        .then(r => r.json())
        .then( console.log );
}