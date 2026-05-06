package com.banco.adapter.out.persistence.mapper;

import com.banco.adapter.out.persistence.entity.UserEntity;
import com.banco.domain.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public User toDomain(UserEntity e) {
        if (e == null) return null;
        return User.builder()
                .userId(e.getIdUsuario()).relatedId(e.getIdRelacionado())
                .fullName(e.getNombreCompleto()).identificationId(e.getIdIdentificacion())
                .email(e.getCorreoElectronico()).phone(e.getTelefono())
                .birthDate(e.getFechaNacimiento()).address(e.getDireccion())
                .systemRole(e.getRolSistema()).userStatus(e.getEstadoUsuario())
                .username(e.getUsername()).password(e.getPassword())
                .associatedCompanyId(e.getIdEmpresaAsociada()).build();
    }
    public UserEntity toEntity(User d) {
        if (d == null) return null;
        return UserEntity.builder()
                .userId(d.getIdUsuario()).relatedId(d.getIdRelacionado())
                .fullName(d.getNombreCompleto()).identificationId(d.getIdIdentificacion())
                .email(d.getCorreoElectronico()).phone(d.getTelefono())
                .birthDate(d.getFechaNacimiento()).address(d.getDireccion())
                .systemRole(d.getRolSistema()).userStatus(d.getEstadoUsuario())
                .username(d.getUsername()).password(d.getPassword())
                .associatedCompanyId(d.getIdEmpresaAsociada()).build();
    }
}
