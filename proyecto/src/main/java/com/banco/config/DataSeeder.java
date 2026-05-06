package com.banco.config;

import com.banco.domain.model.*;
import com.banco.domain.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataSeeder implements CommandLineRunner {
    private final UserRepository usuarioRepo;
    private final IndividualCustomerRepository naturalRepo;
    private final CorporateCustomerRepository empresaRepo;
    private final BankAccountRepository accountRepo;
    private final PasswordEncoder encoder;

    @Override
    public void run(String... args) {
        if (usuarioRepo.existePorUsername("analista")) {
            log.info("Datos de prueba ya cargados.");
            return;
        }

        // 1. Cliente Persona Natural
        IndividualCustomer cliente1 = IndividualCustomer.builder()
                .identificationId("123456789").fullName("Juan Pérez García")
                .email("juan@mail.com").phone("3001234567")
                .birthDate(LocalDate.of(1990, 5, 15))
                .address("Calle 10 #20-30 Medellín").status(UserStatus.ACTIVE).build();
        naturalRepo.guardar(cliente1);
        usuarioRepo.guardar(User.builder().fullName("Juan Pérez García")
                .identificationId("123456789").email("juan@mail.com")
                .phone("3001234567").birthDate(LocalDate.of(1990, 5, 15))
                .address("Calle 10 #20-30").systemRole(SystemRole.INDIVIDUAL_CUSTOMER)
                .userStatus(UserStatus.ACTIVE).username("juan").password(encoder.encode("juan123"))
                .relatedId("123456789").build());

        // 2. Cliente Persona Natural 2 (representante legal de empresa)
        IndividualCustomer cliente2 = IndividualCustomer.builder()
                .identificationId("987654321").fullName("María López Ruiz")
                .email("maria@mail.com").phone("3109876543")
                .birthDate(LocalDate.of(1985, 3, 20))
                .address("Carrera 50 #60-70 Medellín").status(UserStatus.ACTIVE).build();
        naturalRepo.guardar(cliente2);
        usuarioRepo.guardar(User.builder().fullName("María López Ruiz")
                .identificationId("987654321").email("maria@mail.com")
                .phone("3109876543").birthDate(LocalDate.of(1985, 3, 20))
                .address("Carrera 50 #60-70").systemRole(SystemRole.INDIVIDUAL_CUSTOMER)
                .userStatus(UserStatus.ACTIVE).username("maria").password(encoder.encode("maria123"))
                .relatedId("987654321").build());

        // 3. Empresa Cliente
        CorporateCustomer empresa = CorporateCustomer.builder()
                .taxId("900123456-1").companyName("Tech Solutions SAS")
                .email("contacto@techsolutions.com").phone("6014567890")
                .address("Av. El Dorado 68D-35 Bogotá").legalRepresentativeId("987654321")
                .status(UserStatus.ACTIVE).build();
        empresaRepo.guardar(empresa);
        usuarioRepo.guardar(User.builder().fullName("Tech Solutions SAS")
                .identificationId("900123456-1").email("contacto@techsolutions.com")
                .phone("6014567890").address("Av. El Dorado 68D-35")
                .systemRole(SystemRole.CORPORATE_CUSTOMER).userStatus(UserStatus.ACTIVE)
                .username("techsolutions").password(encoder.encode("tech123"))
                .relatedId("900123456-1").build());

        // 4. Empleados del banco
        usuarioRepo.guardar(User.builder().fullName("Carlos Ventanilla")
                .identificationId("111111111").email("cajero@banco.com")
                .phone("3001111111").birthDate(LocalDate.of(1995, 1, 10))
                .address("Banco Central").systemRole(SystemRole.TELLER_EMPLOYEE)
                .userStatus(UserStatus.ACTIVE).username("cajero").password(encoder.encode("cajero123")).build());

        usuarioRepo.guardar(User.builder().fullName("Ana Comercial")
                .identificationId("222222222").email("comercial@banco.com")
                .phone("3002222222").birthDate(LocalDate.of(1992, 6, 15))
                .address("Banco Central").systemRole(SystemRole.COMMERCIAL_EMPLOYEE)
                .userStatus(UserStatus.ACTIVE).username("comercial").password(encoder.encode("comercial123")).build());

        usuarioRepo.guardar(User.builder().fullName("Luis Analista")
                .identificationId("333333333").email("analista@banco.com")
                .phone("3003333333").birthDate(LocalDate.of(1988, 9, 20))
                .address("Banco Central").systemRole(SystemRole.INTERNAL_ANALYST)
                .userStatus(UserStatus.ACTIVE).username("analista").password(encoder.encode("analista123")).build());

        // 5. Empleados de la empresa Tech Solutions
        usuarioRepo.guardar(User.builder().fullName("Pedro Operativo")
                .identificationId("444444444").email("pedro@techsolutions.com")
                .phone("3004444444").birthDate(LocalDate.of(1993, 4, 25))
                .address("Empresa").systemRole(SystemRole.CORPORATE_EMPLOYEE)
                .userStatus(UserStatus.ACTIVE).username("pedro_emp").password(encoder.encode("pedro123"))
                .associatedCompanyId("900123456-1").relatedId("900123456-1").build());

        usuarioRepo.guardar(User.builder().fullName("Sofía Supervisora")
                .identificationId("555555555").email("sofia@techsolutions.com")
                .phone("3005555555").birthDate(LocalDate.of(1982, 11, 5))
                .address("Empresa").systemRole(SystemRole.CORPORATE_SUPERVISOR)
                .userStatus(UserStatus.ACTIVE).username("sofia_sup").password(encoder.encode("sofia123"))
                .associatedCompanyId("900123456-1").relatedId("900123456-1").build());

        // 6. Cuentas bancarias de prueba
        accountRepo.guardar(BankAccount.builder().accountNumber("1001-0001")
                .accountType(AccountType.SAVINGS).holderId("123456789")
                .currentBalance(new BigDecimal("5000000")).currency("COP")
                .accountStatus(AccountStatus.ACTIVE).openingDate(LocalDate.now()).build());

        accountRepo.guardar(BankAccount.builder().accountNumber("1001-0002")
                .accountType(AccountType.CHECKING).holderId("987654321")
                .currentBalance(new BigDecimal("2000000")).currency("COP")
                .accountStatus(AccountStatus.ACTIVE).openingDate(LocalDate.now()).build());

        accountRepo.guardar(BankAccount.builder().accountNumber("2001-0001")
                .accountType(AccountType.CORPORATE).holderId("900123456-1")
                .currentBalance(new BigDecimal("50000000")).currency("COP")
                .accountStatus(AccountStatus.ACTIVE).openingDate(LocalDate.now()).build());

        log.info("✅ Datos de prueba cargados correctamente.");
        log.info("👤 Usuarios: juan/juan123 | maria/maria123 | cajero/cajero123 | comercial/comercial123 | analista/analista123 | pedro_emp/pedro123 | sofia_sup/sofia123 | techsolutions/tech123");
    }
}
