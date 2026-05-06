package com.banco.domain.repository;

import com.banco.domain.model.CorporateCustomer;
import java.util.List;
import java.util.Optional;

public interface CorporateCustomerRepository {
    CorporateCustomer guardar(CorporateCustomer empresa);
    Optional<CorporateCustomer> buscarPorNit(String taxId);
    List<CorporateCustomer> listarTodos();
    boolean existePorNit(String taxId);
}
