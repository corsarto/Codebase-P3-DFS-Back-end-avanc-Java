package com.equipit.equipment.controller;

import jakarta.validation.Valid;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.equipit.equipment.dto.BorrowRequest;
import com.equipit.equipment.model.Equipment;
import com.equipit.equipment.service.EquipmentService;

@RestController
@RequestMapping("/api/equipment")
public class EquipmentController {
    private final EquipmentService service;

    public EquipmentController(EquipmentService service) {
        this.service = service;
    }

    @GetMapping
    public List<Equipment> findAll() {
        return service.findAll();
    }

    @PutMapping("/{id}/borrow")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void borrow(@PathVariable Long id, @Valid @RequestBody BorrowRequest request) {
        service.borrowEquipment(id, request.firstName(), request.lastName());
    }

    @PutMapping("/{id}/return")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void returnEquipment(@PathVariable Long id) {
        service.returnEquipment(id);
    }
}