package com.equipit.equipment.dto;

import jakarta.validation.constraints.NotBlank;

public record BorrowRequest(@NotBlank String firstName, @NotBlank String lastName) { }