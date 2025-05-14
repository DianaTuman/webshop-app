package com.dianatuman.practicum.webshop.controller;

import com.dianatuman.practicum.webshop.dto.ItemDTO;
import com.dianatuman.practicum.webshop.service.ItemService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ItemsController.class)
public class ItemControllerTest extends BaseControllerTest {

    @MockitoBean
    ItemService itemService;

    @Test
    public void getItems_shouldReturnItemsPage() throws Exception {
        ItemDTO e1 = new ItemDTO("TestItem", "Item Description", 10.0, new byte[0]);
        e1.setId(1L);
        Page<ItemDTO> pagedListHolder = new PageImpl<>(List.of(e1), PageRequest.of(0, 10), 1);
        when(itemService.getItems(any(), any())).thenReturn(pagedListHolder);
        mockMvc.perform(get("/items"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("items"))
                .andExpect(model().attributeExists("items"))
                .andExpect(xpath("//a[@href='/items/1']").exists());

        verify(itemService, times(1)).getItems(any(), any());
    }

    @Test
    public void getItemId_shouldReturnItemPage() throws Exception {
        ItemDTO e1 = new ItemDTO("TestItem", "Item Description", 10.0, new byte[0]);
        e1.setId(1L);
        when(itemService.getItem(1)).thenReturn(e1);

        mockMvc.perform(get("/items/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("item"))
                .andExpect(model().attributeExists("item"))
                .andExpect(xpath("//div/p/b").string("TestItem"))
                .andExpect(xpath("//div/p/b[2]").string("10.0 руб."))
                .andExpect(xpath("//div/p/span").string("Item Description"));

        verify(itemService, times(1)).getItem(1);
    }

}
