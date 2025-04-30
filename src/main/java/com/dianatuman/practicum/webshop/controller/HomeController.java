package com.dianatuman.practicum.webshop.controller;

import org.springframework.stereotype.Controller;

@Controller
public class HomeController {

//    а) GET "/" - редирект на "/main/items"
//    б) GET "/main/items" - список всех товаров плиткой на главной странице
//    Параметры:
//    search - строка с поисков по названию/описанию товара (по умолчанию, пустая строка - все товары)
//    sort - сортировка перечисление NO, ALPHA, PRICE (по умолчанию, NO - не использовать сортировку)
//    pageSize - максимальное число товаров на странице (по умолчанию, 10)
//    pageNumber - номер текущей страницы (по умолчанию, 1)
//    Возвращает:
//    шаблон "main.html"
//    используется модель для заполнения шаблона:
//            "items" - List<List<Item>> - список товаров по N в ряд (id, title, decription, imgPath, count, price)
//            			"search" - строка поиска (по умолчанию, пустая строка - все товары)
//            			"sort" - сортировка перечисление NO, ALPHA, PRICE (по умолчанию, NO - не использовать сортировку)
//            			"paging":
//                                "pageNumber" - номер текущей страницы (по умолчанию, 1)
//            				"pageSize" - максимальное число товаров на странице (по умолчанию, 10)
//            				"hasNext" - можно ли пролистнуть вперед
//            				"hasPrevious" - можно ли пролистнуть назад

}
