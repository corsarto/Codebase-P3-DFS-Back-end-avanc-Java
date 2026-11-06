package com.equipit.equipment.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.equipit.equipment.exception.EquipmentAvailableException;
import com.equipit.equipment.exception.EquipmentNotFoundException;
import com.equipit.equipment.exception.EquipmentUnavailableException;
import com.equipit.equipment.model.Equipment;
import com.equipit.equipment.repository.EquipmentRepository;

@Service
public class EquipmentService {
    private final EquipmentRepository repository;

    public EquipmentService(EquipmentRepository repository) {
        this.repository = repository;
    }

    public List<Equipment> findAll() {
        return repository.findAll();
    }

    public Equipment borrowEquipment(Long id, String firstName, String lastName) {
        Equipment equipmentBorrow = findEquipmentOrThrow(id);
        if(!equipmentBorrow.isAvailable()) {
            throw new EquipmentUnavailableException("The equipment is no longer available.");
        }
        equipmentBorrow.borrow(firstName, lastName, LocalDate.now());
        return repository.save(equipmentBorrow);            
    }

    public Equipment returnEquipment(Long id) {
        Equipment equipmentReturned = findEquipmentOrThrow(id);
        if(equipmentReturned.isAvailable()) {
            throw new EquipmentAvailableException("The equipment is already available.");
        }
        equipmentReturned.returnedBorrow(LocalDate.now());
        return repository.save(equipmentReturned);
    }

    private Equipment findEquipmentOrThrow(Long id) {
        Optional<Equipment> equipment = repository.findById(id);
        if(equipment.isEmpty()) {
            throw new EquipmentNotFoundException("The equipment is not present");
        } 
        return equipment.get();
    }
}