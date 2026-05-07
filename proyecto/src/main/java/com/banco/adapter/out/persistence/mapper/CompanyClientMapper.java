package com.banco.adapter.out.persistence.mapper;

import com.banco.adapter.out.persistence.entity.CompanyClientJpaEntity;
import com.banco.domain.model.entity.CompanyClient;
import org.springframework.stereotype.Component;

@Component
public class CompanyClientMapper {
    public CompanyClient toDomain(CompanyClientJpaEntity e) {
        if (e == null) return null;
        return CompanyClient.builder()
                .id(e.getId())
                .businessName(e.getBusinessName())
                .taxId(e.getTaxId())
                .email(e.getEmail())
                .phone(e.getPhone())
                .address(e.getAddress())
                .build();
    }

    public CompanyClientJpaEntity toEntity(CompanyClient d) {
        if (d == null) return null;
        return CompanyClientJpaEntity.builder()
                .id(d.getId())
                .businessName(d.getBusinessName())
                .taxId(d.getTaxId())
                .email(d.getEmail())
                .phone(d.getPhone())
                .address(d.getAddress())
                .build();
    }
}
