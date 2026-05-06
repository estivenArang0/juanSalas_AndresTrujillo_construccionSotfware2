package com.bank.app.application.port.input;
import com.bank.app.application.dto.request.CreateNaturalPersonRequest;
import com.bank.app.application.dto.request.CreateCompanyClientRequest;
import com.bank.app.application.dto.response.UserResponse;
import com.bank.app.application.dto.response.NaturalPersonResponse;
import com.bank.app.application.dto.response.CompanyClientResponse;
public interface UserInputPort {
    NaturalPersonResponse registerNaturalPerson(CreateNaturalPersonRequest request);
    CompanyClientResponse registerCompany(CreateCompanyClientRequest request);
    UserResponse getUserById(Long id);
}
