package com.dianatuman.practicum.webshop.controller;

import com.dianatuman.practicum.webshop.dto.ItemDTO;
import com.dianatuman.practicum.webshop.dto.OrderDTO;
import com.dianatuman.practicum.webshop.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = OrdersController.class)
public class OrderControllerTest extends BaseControllerTest {

    @MockitoBean
    OrderService orderService;

    @Test
    public void getOrders_shouldReturnOrdersPage() throws Exception {
        when(orderService.getOrders()).thenReturn(
                List.of(new OrderDTO(1L, new ArrayList<>())));
        mockMvc.perform(get("/orders"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("orders"))
                .andExpect(model().attributeExists("orders"))
                .andExpect(xpath("//h2/a").string("Заказ №1"))
                .andExpect(xpath("//a[@href='/orders/1?newOrder=false']").exists());

        verify(orderService, times(1)).getOrders();
    }

    @Test
    public void getOrdersId_shouldReturnOrderPage() throws Exception {
        ItemDTO e1 = new ItemDTO("TestItem", "Item Description", 10.0, new byte[0]);
        ItemDTO e2 = new ItemDTO("TestItem2", "Item Descr", 12.0, new byte[0]);
        e1.setCount(1);
        e2.setCount(1);
        when(orderService.getOrder(1)).thenReturn(new OrderDTO(1L, List.of(e1, e2)));

        mockMvc.perform(get("/orders/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("order"))
                .andExpect(model().attributeExists("order"))
                .andExpect(xpath("//h2").string("Заказ №1"))
                .andExpect(xpath("//h3").string("Сумма: 22.0 руб."));

        verify(orderService, times(1)).getOrder(1);
    }
}
