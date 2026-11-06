package com.equipit.equipment.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.equipit.equipment.model.Equipment;

public interface EquipmentRepository extends JpaRepository<Equipment, Long> { }