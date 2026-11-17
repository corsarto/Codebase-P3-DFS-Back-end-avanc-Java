package com.equipit.equipment.controller;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;

import java.util.List;
import org.springframework.http.MediaType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.equipit.equipment.exception.EquipmentNotFoundException;
import com.equipit.equipment.exception.EquipmentUnavailableException;
import com.equipit.equipment.model.Equipment;
import com.equipit.equipment.service.EquipmentService;

@WebMvcTest(EquipmentController.class)
public class EquipmentControllerTest {

    @Autowired 
    private MockMvc mockMvc;

    @MockitoBean
    private EquipmentService equipmentService;

    @Test 
    void listsAllEquipment() throws Exception {
        Equipment equipment = new Equipment("Laptop", "Computer");
        when(equipmentService.findAll()).thenReturn(List.of(equipment));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/equipment"))
               .andExpect(MockMvcResultMatchers.status().isOk())
               .andExpect(MockMvcResultMatchers.jsonPath("$[*].name", hasItem("Laptop")));
    }

    @Test 
    void borrowsEquipment() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/api/equipment/1/borrow")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""
                        { "firstName": "John", "lastName": "Doe" }
                    """)) 
               .andExpect(MockMvcResultMatchers.status().isNoContent());
        verify(equipmentService).borrowEquipment(1L, "John", "Doe");
    }

    @Test 
    void returnsEquipment() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/api/equipment/1/return"))
               .andExpect(MockMvcResultMatchers.status().isNoContent());
        verify(equipmentService).returnEquipment(1L);
    }

    @Test 
    void rejectsBlankFirstName() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/api/equipment/1/borrow")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""
                        { "firstName": "", "lastName": "Doe" }
                    """)) 
               .andExpect(MockMvcResultMatchers.status().isBadRequest());
        verify(equipmentService, never()).borrowEquipment(anyLong(), anyString(), anyString());
    }

    @Test 
    void rejectsUnavailableEquipment() throws Exception {
        when(equipmentService.borrowEquipment(1L, "John", "Doe")).thenThrow(new EquipmentUnavailableException("The equipment is already borrowed"));
        mockMvc.perform(MockMvcRequestBuilders.put("/api/equipment/1/borrow")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""
                        { "firstName": "John", "lastName": "Doe" }
                    """))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("The equipment is already borrowed"));
    }

    @Test 
    void rejectsUnknownEquipment() throws Exception {
        when(equipmentService.returnEquipment(99L)).thenThrow(new EquipmentNotFoundException("The equipment is not present"));
        mockMvc.perform(MockMvcRequestBuilders.put("/api/equipment/99/return"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().string("The equipment is not present"));
    }
}
