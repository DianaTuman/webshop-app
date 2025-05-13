package com.dianatuman.practicum.webshop.service;

import com.dianatuman.practicum.webshop.dto.OrderDTO;
import com.dianatuman.practicum.webshop.entity.Item;
import com.dianatuman.practicum.webshop.entity.Order;
import com.dianatuman.practicum.webshop.entity.OrderItem;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

public class OrderServiceTest extends BaseServiceTest {

    @Test
    public void getOrdersTest() throws Exception {
        Item item1 = itemRepository.save(new Item("TestItem1", "ItemDescr", 11.0));
        Order order1 = orderRepository.save(new Order());
        orderItemRepository.save(new OrderItem(order1, item1, 1));
        Object modelOrders = Objects.requireNonNull(mockMvc.perform(get("/orders"))
                .andReturn().getModelAndView()).getModel().get("orders");
        if (modelOrders instanceof List) {
            List<OrderDTO> orders = (List<OrderDTO>) modelOrders;
            assertThat(orders).isNotEmpty();
            assertThat(orders).hasSize(1);
            OrderDTO first = orders.getFirst();
            assertThat(first).isEqualTo(new OrderDTO(order1.getId(), List.of(itemMapper.toDTO(item1))));
            assertThat(first.getItems().getFirst().getCount()).isEqualTo(1);
            assertThat(first.getTotalSum()).isEqualTo(11.0);
        } else {
            fail();
        }
    }

    @Test
    public void getOrderTest() throws Exception {
        Item item1 = itemRepository.save(new Item("TestItem1", "ItemDescr", 11.0));
        Order order1 = orderRepository.save(new Order());
        orderItemRepository.save(new OrderItem(order1, item1, 5));
        OrderDTO order = (OrderDTO) Objects.requireNonNull(mockMvc.perform(get("/orders/" + order1.getId()))
                .andReturn().getModelAndView()).getModel().get("order");
        assertThat(order).isEqualTo(new OrderDTO(order1.getId(), List.of(itemMapper.toDTO(item1))));
        assertThat(order.getItems().getFirst().getCount()).isEqualTo(5);
        assertThat(order.getTotalSum()).isEqualTo(55.0);
    }

    @Test
    public void buyOrderTest() throws Exception {
        Item item1 = itemRepository.save(new Item("TestItem1", "ItemDescr", 11.0));
        Item item2 = itemRepository.save(new Item("TestItem2", "ItemDescr", 12.0));

        mockMvc.perform(post("/cart/items/" + item1.getId()).param("action", "plus"));
        mockMvc.perform(post("/cart/items/" + item2.getId()).param("action", "plus"));
        mockMvc.perform(post("/cart/buy"));

        Object modelOrders = Objects.requireNonNull(mockMvc.perform(get("/orders"))
                .andReturn().getModelAndView()).getModel().get("orders");
        if (modelOrders instanceof List) {
            List<OrderDTO> orders = (List<OrderDTO>) modelOrders;
            assertThat(orders).isNotEmpty();
            assertThat(orders).hasSize(1);
            OrderDTO first = orders.getFirst();
            assertThat(first.getItems()).hasSize(2);
            assertThat(first.getTotalSum()).isEqualTo(23.0);
            assertThat(first.getItems()).contains(itemMapper.toDTO(item1), itemMapper.toDTO(item2));
        } else {
            fail();
        }

    }
}
