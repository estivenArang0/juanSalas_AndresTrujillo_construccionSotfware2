package com.banco.adapter.out.persistence.mapper;

import com.banco.adapter.out.persistence.entity.IndividualCustomerEntity;
import com.banco.domain.model.IndividualCustomer;
import org.springframework.stereotype.Component;

@Component
public class IndividualCustomerMapper {
    public IndividualCustomer toDomain(IndividualCustomerEntity e) {
        if (e == null) return null;
        return IndividualCustomer.builder()
                .identificationId(e.getIdIdentificacion()).fullName(e.getNombreCompleto())
                .email(e.getCorreoElectronico()).phone(e.getTelefono())
                .birthDate(e.getFechaNacimiento()).address(e.getDireccion())
                .status(e.getEstado()).build();
    }
    public IndividualCustomerEntity toEntity(IndividualCustomer d) {
        if (d == null) return null;
        return IndividualCustomerEntity.builder()
                .identificationId(d.getIdIdentificacion()).fullName(d.getNombreCompleto())
                .email(d.getCorreoElectronico()).phone(d.getTelefono())
                .birthDate(d.getFechaNacimiento()).address(d.getDireccion())
                .status(d.getEstado()).build();
    }
}
