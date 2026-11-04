package com.equipit.equipment;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class EquipmentServiceTest {
    @Mock
    private EquipmentRepository repository;

    private EquipmentService service;

    @BeforeEach
    void setUp() {
        service = new EquipmentService(repository);
    }

    @Test
    void borrowsAvailableEquipment() {
        Equipment equipment = new Equipment("Laptop", "Computer");
        when(repository.findById(1L)).thenReturn(Optional.of(equipment));
        when(repository.save(equipment)).thenReturn(equipment);

        Equipment result = service.borrowEquipment(1L, "Ada", "Lovelace");

        assertThat(result.isAvailable()).isFalse();
        assertThat(result.getBorrowerFirstName()).isEqualTo("Ada");
        verify(repository).save(equipment);
    }

    @Test
    void rejectsUnavailableEquipment() {
        Equipment equipment = new Equipment("Laptop", "Computer");
        equipment.borrow("Ada", "Lovelace", java.time.LocalDate.now());
        when(repository.findById(1L)).thenReturn(Optional.of(equipment));

        assertThatThrownBy(() -> service.borrowEquipment(1L, "Grace", "Hopper"))
            .isInstanceOf(EquipmentUnavailableException.class);
    }

    @Test
    void returnsEquipment() {
        Equipment equipment = new Equipment("Laptop", "Computer");
        equipment.borrow("Ada", "Lovelace", java.time.LocalDate.now());
        when(repository.findById(1L)).thenReturn(Optional.of(equipment));
        when(repository.save(any(Equipment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Equipment result = service.returnEquipment(1L);

        assertThat(result.isAvailable()).isTrue();
        assertThat(result.getBorrowerFirstName()).isNull();
        assertThat(result.getLastReturnedAt()).isNotNull();
    }

    @Test
    void rejectsUnknownEquipment() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.returnEquipment(99L))
            .isInstanceOf(EquipmentNotFoundException.class);
    }
}
