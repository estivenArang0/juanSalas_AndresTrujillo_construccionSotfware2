package com.banco.application.port.input;
import com.banco.application.dto.request.CreateNaturalPersonRequest;
import com.banco.application.dto.request.CreateCompanyClientRequest;
import com.banco.application.dto.response.UserResponse;
import com.banco.application.dto.response.NaturalPersonResponse;
import com.banco.application.dto.response.CompanyClientResponse;
public interface UserInputPort {
    NaturalPersonResponse registerNaturalPerson(CreateNaturalPersonRequest request);
    CompanyClientResponse registerCompany(CreateCompanyClientRequest request);
    UserResponse getUserById(Long id);
}
