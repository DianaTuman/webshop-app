//package com.dianatuman.practicum.webshop.service;
//
//import com.dianatuman.practicum.webshop.dto.ItemDTO;
//import com.dianatuman.practicum.webshop.entity.Item;
//import org.junit.jupiter.api.Test;
//import org.springframework.data.domain.Page;
//
//import java.util.List;
//import java.util.Objects;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.assertj.core.api.Assertions.fail;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//
//public class ItemServiceTest extends BaseServiceTest {
//
//    @Test
//    public void getItemsTest() throws Exception {
//        var item1 = itemRepository.save(new Item("TestItem1", "ItemDescr", 11.0));
//        var item2 = itemRepository.save(new Item("TestItem2", "ItemDescr", 12.0));
//        Object modelItems = Objects.requireNonNull(mockMvc.perform(get("/items"))
//                .andReturn().getModelAndView()).getModel().get("items");
//        if (modelItems instanceof Page<?>) {
//            List<ItemDTO> items = ((Page<ItemDTO>) modelItems).toList();
//            assertThat(items).isNotEmpty();
//            assertThat(items).hasSize(2);
//            assertThat(items).contains(itemMapper.toDTO(item1), itemMapper.toDTO(item2));
//        } else {
//            fail();
//        }
//    }
//
//    @Test
//    public void getItemTest() throws Exception {
//        var item1 = itemRepository.save(new Item("TestItem1", "ItemDescr", 11.0));
//        ItemDTO item = (ItemDTO) Objects.requireNonNull(mockMvc.perform(get("/items/" + item1.getId()))
//                .andReturn().getModelAndView()).getModel().get("item");
//        assertThat(item).isEqualTo(itemMapper.toDTO(item1));
//    }
//
//    @Test
//    public void itemCartTest() throws Exception {
//        var item1 = itemRepository.save(new Item("TestItem1", "ItemDescr", 11.0));
//        String uriTemplate = "/items/" + item1.getId();
//
//        ItemDTO item = (ItemDTO) Objects.requireNonNull(mockMvc.perform(get(uriTemplate))
//                .andReturn().getModelAndView()).getModel().get("item");
//        assertThat(item.getCount()).isEqualTo(0);
//
//        mockMvc.perform(post(uriTemplate).param("action", "plus"));
//        item = (ItemDTO) Objects.requireNonNull(mockMvc.perform(get(uriTemplate))
//                .andReturn().getModelAndView()).getModel().get("item");
//        assertThat(item.getCount()).isEqualTo(1);
//
//        mockMvc.perform(post(uriTemplate).param("action", "delete"));
//        item = (ItemDTO) Objects.requireNonNull(mockMvc.perform(get(uriTemplate))
//                .andReturn().getModelAndView()).getModel().get("item");
//        assertThat(item.getCount()).isEqualTo(0);
//
//        mockMvc.perform(post(uriTemplate).param("action", "plus"));
//        mockMvc.perform(post(uriTemplate).param("action", "plus"));
//        item = (ItemDTO) Objects.requireNonNull(mockMvc.perform(get(uriTemplate))
//                .andReturn().getModelAndView()).getModel().get("item");
//        assertThat(item.getCount()).isEqualTo(2);
//
//        mockMvc.perform(post(uriTemplate).param("action", "minus"));
//        item = (ItemDTO) Objects.requireNonNull(mockMvc.perform(get(uriTemplate))
//                .andReturn().getModelAndView()).getModel().get("item");
//        assertThat(item.getCount()).isEqualTo(1);
//
//        Object modelItems = Objects.requireNonNull(mockMvc.perform(get("/cart/items"))
//                .andReturn().getModelAndView()).getModel().get("items");
//        if (modelItems instanceof List) {
//            List<ItemDTO> items = (List<ItemDTO>) modelItems;
//            assertThat(items).isNotEmpty();
//            assertThat(items).hasSize(1);
//            assertThat(items).contains(itemMapper.toDTO(item1));
//            assertThat(items.getFirst().getCount()).isEqualTo(1);
//        } else {
//            fail();
//        }
//    }
//}
