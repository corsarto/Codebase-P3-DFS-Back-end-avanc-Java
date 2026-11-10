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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/equipment")
@Tag(name = "Equipment", description = "List, borrow and return IT equipment.")
public class EquipmentController {
    private final EquipmentService service;

    public EquipmentController(EquipmentService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Get all equipment", description = "Returns all the equipment stored in the database.")
    @ApiResponse(responseCode = "200", description = "List of all equipment.")
    public List<Equipment> findAll() {
        return service.findAll();
    }

    @PutMapping("/{id}/borrow")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Borrow a piece of equipment", description = "Borrows a piece of equipment for a person (first name and last name). The equipment must be available.")
    @ApiResponse(responseCode = "204", description = "Equipment borrowed successfully.")
    @ApiResponse(
        responseCode = "400",
        description = "Equipment is not available, or first name / last name is missing.",
        content = @Content(schema = @Schema(implementation = String.class))
    )
    @ApiResponse(
        responseCode = "404",
        description = "Equipment not found.",
        content = @Content(schema = @Schema(implementation = String.class))
    )
    public void borrow(@PathVariable Long id, @Valid @RequestBody BorrowRequest request) {
        service.borrowEquipment(id, request.firstName(), request.lastName());
    }

    @PutMapping("/{id}/return")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Return a piece of equipment", description = "Returns a borrowed piece of equipment and makes it available again, so it can be borrowed again.")
    @ApiResponse(responseCode = "204", description = "Equipment returned successfully.")
    @ApiResponse(
        responseCode = "400",
        description = "Equipment is already available (not borrowed).",
        content = @Content(schema = @Schema(implementation = String.class))
    )
    @ApiResponse(
        responseCode = "404",
        description = "Equipment not found.",
        content = @Content(schema = @Schema(implementation = String.class))
    )
    public void returnEquipment(@PathVariable Long id) {
        service.returnEquipment(id);
    }
}