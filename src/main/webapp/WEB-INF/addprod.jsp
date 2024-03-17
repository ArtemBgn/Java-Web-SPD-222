<%--
  Created by IntelliJ IDEA.
  User: bart-
  Date: 17.03.2024
  Time: 16:59
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<h1>Форма додавання продукту</h1>

<div class="row">
    <form class="col s12" method="post">
        <div class="row">
            <div class="input-field col s6">
                <input type="text" id="product_name" placeholder="Введіть назву продукту" aria-label="Назва продукту" class="validate">
                <label for="product_name">Назва продукту</label>
            </div>
            <div class="input-field col s6">
                <input type="number" id="product_price" placeholder="Введіть вартість продукту" aria-label="Вартість продукту" class="validate">
                <label for="product_price">Вартість продукту</label>
            </div>
        </div>
        <div class="row">
            <div class="input-field col s12">
                <input type="text" id="product_description" placeholder="Опишіть продукт" aria-label="Опишіть продукт" class="validate">
                <label for="product_description">Опис продукту</label>
            </div>
        </div>
        <div class="row">
            <div class="col s12">
                <span>Виберіть категорію продукту</span><!---->
                <label>
                    <input name="product_category" type="radio" value="kids" checked />
                    <span>Дитячі товари</span>
                </label>
                <label>
                    <input name="product_category" type="radio" value="clothes" />
                    <span>Одяг</span>
                </label>
                <label>
                    <input name="product_category" type="radio" value="cottage" class="with-gap"  />
                    <span>Дачні товари</span>
                </label>
                <label>
                    <input name="product_category" type="radio" value="auto" />
                    <span>Автотовари</span>
                </label>
                <label>
                    <input name="product_category" type="radio" value="telephone" />
                    <span>Телефони</span>
                </label>
                <label>
                    <input name="product_category" type="radio" value="electronics" />
                    <span>Електроніка</span>
                </label>
            </div>
        </div>
        <div class="row">
            <div class="file-field input-field col s12">
                <div class="btn lime">
                    <span>Фото</span>
                    <input type="file" name="product_photo">
                </div>
                <div class="file-path-wrapper">
                    <input class="file-path validate" type="text">
                </div>
            </div>
        </div>
        <div class="input-field col s6">
            <button type="button" id="addproduct_button" class="btn lime right"><i class="material-icons left">task_alt</i>Додати продукт</button>
        </div>
    </form>
</div>