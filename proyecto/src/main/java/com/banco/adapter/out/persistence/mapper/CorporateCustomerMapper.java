package com.banco.adapter.out.persistence.mapper;

import com.banco.adapter.out.persistence.entity.CorporateCustomerEntity;
import com.banco.domain.model.CorporateCustomer;
import org.springframework.stereotype.Component;

@Component
public class CorporateCustomerMapper {
    public CorporateCustomer toDomain(CorporateCustomerEntity e) {
        if (e == null) return null;
        return CorporateCustomer.builder()
                .taxId(e.getNit()).companyName(e.getRazonSocial())
                .email(e.getCorreoElectronico()).phone(e.getTelefono())
                .address(e.getDireccion()).legalRepresentativeId(e.getIdRepresentanteLegal())
                .status(e.getEstado()).build();
    }
    public CorporateCustomerEntity toEntity(CorporateCustomer d) {
        if (d == null) return null;
        return CorporateCustomerEntity.builder()
                .taxId(d.getNit()).companyName(d.getRazonSocial())
                .email(d.getCorreoElectronico()).phone(d.getTelefono())
                .address(d.getDireccion()).legalRepresentativeId(d.getIdRepresentanteLegal())
                .status(d.getEstado()).build();
    }
}
