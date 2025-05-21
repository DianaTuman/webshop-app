//package com.dianatuman.practicum.webshop.service;
//
//import com.dianatuman.practicum.webshop.dto.ItemDTO;
//import com.dianatuman.practicum.webshop.dto.OrderDTO;
//import com.dianatuman.practicum.webshop.entity.Item;
//import com.dianatuman.practicum.webshop.entity.Order;
//import org.junit.jupiter.api.Test;
//import reactor.core.publisher.Flux;
//
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.assertj.core.api.Assertions.fail;
//
//public class OrderServiceTest extends BaseServiceTest {
//
//    @Test
//    public void getOrdersTest() throws Exception {
//        var item1 = itemRepository.save(new Item("TestItem1", "ItemDescr", 11.0));
//        var order1 = orderRepository.save(new Order());
//        Object modelOrders = (webTestClient.get().uri("/orders")
//                .andReturn().getModelAndView()).getModel().get("orders");
//        if (modelOrders instanceof List) {
//            List<OrderDTO> orders = (List<OrderDTO>) modelOrders;
//            assertThat(orders).isNotEmpty();
//            assertThat(orders).hasSize(1);
//            OrderDTO first = orders.getFirst();
//            assertThat(first).isEqualTo(new OrderDTO(order1.getId(), Flux.just(itemMapper.toDTO(item1))));
//            assertThat(first.getItems().getFirst().getCount()).isEqualTo(1);
//            assertThat(first.getTotalSum()).isEqualTo(11.0);
//        } else {
//            fail();
//        }
//    }
//
//    @Test
//    public void getOrderTest() throws Exception {
//        var item1 = itemRepository.save(new Item("TestItem1", "ItemDescr", 11.0));
//        var order1 = orderRepository.save(new Order());
//        OrderDTO order = (OrderDTO) webTestClient.get().uri("/orders/" + order1.getId())
//                .andReturn().getModelAndView()a).getModel().get("order");
//        assertThat(order).isEqualTo(new OrderDTO(order1.getId(), Flux.just(itemMapper.toDTO(item1))));
//        assertThat(order.getItems().collectList().block().getFirst().getCount()).isEqualTo(5);
//        assertThat(order.getTotalSum()).isEqualTo(55.0);
//    }
//
//    @Test
//    public void buyOrderTest() throws Exception {
//        var item1 = itemRepository.save(new Item("TestItem1", "ItemDescr", 11.0));
//        var item2 = itemRepository.save(new Item("TestItem2", "ItemDescr", 12.0));
//
//        webTestClient.post().uri("/cart/items/" + item1.getId()).param("action", "plus"));
//        webTestClient.post().uri("/cart/items/" + item2.getId()).param("action", "plus"));
//        webTestClient.post().uri("/cart/buy");
//
//        Object modelOrders = webTestClient.get().uri("/orders")
//                .andReturn().getModelAndView().getModel().get("orders");
//
//        if (modelOrders instanceof List) {
//            List<OrderDTO> orders = (List<OrderDTO>) modelOrders;
//            assertThat(orders).isNotEmpty();
//            assertThat(orders).hasSize(1);
//            OrderDTO first = orders.getFirst();
//            List<ItemDTO> items = first.getItems().collectList().block();
//            assertThat(items).hasSize(2);
//            assertThat(first.getTotalSum()).isEqualTo(23.0);
//            assertThat(items).contains(itemMapper.toDTO(item1), itemMapper.toDTO(item2));
//        } else {
//            fail();
//        }
//
//    }
//}
