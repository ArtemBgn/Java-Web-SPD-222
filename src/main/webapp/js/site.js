console.log("Script works");
document.addEventListener('DOMContentLoaded', function() {
    const signupButton = document.getElementById("signup-button");
    if(signupButton) { signupButton.onclick = signupButtonClick; }
    const prodButton = document.getElementById("addproduct_button");
    if(prodButton) { prodButton.onclick = prodButtonClick; }
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