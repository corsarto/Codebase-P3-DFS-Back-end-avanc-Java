package com.equipit.equipment.controller;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
    public ResponseEntity<Equipment> borrow(@PathVariable Long id, @Valid @RequestBody BorrowRequest request) {
        return ResponseEntity.ok(service.borrowEquipment(id, request.firstName(), request.lastName()));
    }

    @PutMapping("/{id}/return")
    public ResponseEntity<Equipment> returnEquipment(@PathVariable Long id) {
        return ResponseEntity.ok(service.returnEquipment(id));
    }
}