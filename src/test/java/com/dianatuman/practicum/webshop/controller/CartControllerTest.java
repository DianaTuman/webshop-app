package com.dianatuman.practicum.webshop.controller;

import com.dianatuman.practicum.webshop.dto.ItemDTO;
import com.dianatuman.practicum.webshop.dto.OrderDTO;
import com.dianatuman.practicum.webshop.service.ItemService;
import com.dianatuman.practicum.webshop.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = CartController.class)
public class CartControllerTest extends BaseControllerTest {

    @MockitoBean
    ItemService itemService;

    @MockitoBean
    OrderService orderService;

    @Test
    public void getCartItems_shouldReturnCartPage() throws Exception {
        ItemDTO e1 = new ItemDTO("TestItem", "Item Description", 10.0, new byte[0]);
        e1.setId(1L);
        e1.setCount(2);
        when(itemService.getCartItems()).thenReturn(List.of(e1));
        mockMvc.perform(get("/cart/items"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("cart"))
                .andExpect(model().attributeExists("items"))
                .andExpect(xpath("//table[2]/tr[2]/td/b").string("Итого: 20.0 руб."))
                .andExpect(xpath("//table[2]/tr[3]/td/form/button").string("Купить"));

        verify(itemService, times(1)).getCartItems();
    }
}
