package com.dianatuman.practicum.webshop.service;

import com.dianatuman.practicum.webshop.dto.ItemDTO;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.web.reactive.function.BodyInserters;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

public class ItemServiceTest extends BaseServiceTest {

    @Test
    public void getItemsTest() {
        var bodyBuilder = new MultipartBodyBuilder();
        bodyBuilder.part("itemName", "TestItem1");
        bodyBuilder.part("description", "description");
        bodyBuilder.part("price", "123");
        bodyBuilder.part("image", new byte[0], MediaType.IMAGE_PNG);

        webTestClient.post().uri("/items/add")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(bodyBuilder.build()))
                .exchange()
                .expectStatus()
                .is2xxSuccessful();

        webTestClient.get().uri("/items").exchange()
                .expectStatus()
                .is2xxSuccessful();
//        if (modelItems instanceof Page<?>) {
//            List<ItemDTO> items = ((Page<ItemDTO>) modelItems).toList();
//            assertThat(items).isNotEmpty();
//            assertThat(items).hasSize(2);
//        } else {
//            fail();
//        }
    }

//    @Test
//    public void getItemTest() throws Exception {
//        var item1 = itemRepository.save(new Item("TestItem1", "ItemDescr", 11.0));
//        ItemDTO item = (ItemDTO) webTestClient.get().uri("/items/" + item1.getId()).exchange()
//                .andReturn().getModelAndView()).getModel().get("item");
//        assertThat(item).isEqualTo(itemMapper.toDTO(item1));
//    }
//
//    @Test
//    public void itemCartTest() throws Exception {
//        var item1 = itemRepository.save(new Item("TestItem1", "ItemDescr", 11.0));
//        String uriTemplate = "/items/" + item1.getId();
//
//        ItemDTO item = (ItemDTO) webTestClient.get().uri(uriTemplate).exchange()
//                .andReturn().getModelAndView()).getModel().get("item");
//        assertThat(item.getCount()).isEqualTo(0);
//
//        webTestClient.post().uri(uriBuilder ->
//                        uriBuilder
//                                .path(uriTemplate)
//                                .queryParam("action", "plus")
//                                .build())
//                .exchange();
//        item = (ItemDTO) webTestClient.get().uri(uriTemplate).exchange()
//                .andReturn().getModelAndView()).getModel().get("item");
//        assertThat(item.getCount()).isEqualTo(1);
//
//        webTestClient.post().uri(uriBuilder ->
//                        uriBuilder
//                                .path(uriTemplate)
//                                .queryParam("action", "delete")
//                                .build())
//                .exchange();
//        item = (ItemDTO) webTestClient.get().uri(uriTemplate).exchange()
//                .andReturn().getModelAndView()).getModel().get("item");
//        assertThat(item.getCount()).isEqualTo(0);
//
//        webTestClient.post().uri(uriBuilder ->
//                        uriBuilder
//                                .path(uriTemplate)
//                                .queryParam("action", "plus")
//                                .build())
//                .exchange();
//        webTestClient.post().uri(uriBuilder ->
//                        uriBuilder
//                                .path(uriTemplate)
//                                .queryParam("action", "plus")
//                                .build())
//                .exchange();
//        item = (ItemDTO) webTestClient.get().uri(uriTemplate).exchange()
//                .andReturn().getModelAndView()).getModel().get("item");
//        assertThat(item.getCount()).isEqualTo(2);
//
//        webTestClient.post().uri(uriBuilder ->
//                        uriBuilder
//                                .path(uriTemplate)
//                                .queryParam("action", "minus")
//                                .build())
//                .exchange();
//        item = (ItemDTO) webTestClient.get().uri(uriTemplate).exchange()
//                .andReturn().getModelAndView()).getModel().get("item");
//        assertThat(item.getCount()).isEqualTo(1);
//
//        Object modelItems = webTestClient.get().uri("/cart/items").exchange()
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
}
