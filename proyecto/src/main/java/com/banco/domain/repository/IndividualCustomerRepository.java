package com.banco.domain.repository;

import com.banco.domain.model.IndividualCustomer;
import java.util.List;
import java.util.Optional;

public interface IndividualCustomerRepository {
    IndividualCustomer guardar(IndividualCustomer cliente);
    Optional<IndividualCustomer> buscarPorId(String identificationId);
    List<IndividualCustomer> listarTodos();
    boolean existePorId(String identificationId);
}
