package com.equipit.equipment;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

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
        Optional<Equipment> equipment = repository.findById(id);
        if(!equipment.isPresent()) {

            throw new EquipmentNotFoundException("No equipment present.");

        } else {

            Equipment equipmentBorrow = equipment.get();
            if(!equipmentBorrow.isAvailable()) {

                throw new EquipmentUnavailableException("The equipment is no longer available.");

            }
            equipmentBorrow.borrow(firstName, lastName, LocalDate.now());

            return repository.save(equipmentBorrow);
        }
            
    }

    public Equipment returnEquipment(Long id) {
        Optional<Equipment> equipment = repository.findById(id);
        if(equipment.isEmpty()) {

            throw new EquipmentNotFoundException("The equipment is not present");

        } 

        Equipment equipmentReturned = equipment.get();

        if(equipmentReturned.isAvailable()) {

            throw new EquipmentAvailableException("The equipment is already available.");

        }

        equipmentReturned.returnedBorrow(LocalDate.now());
        return repository.save(equipmentReturned);
    }
}