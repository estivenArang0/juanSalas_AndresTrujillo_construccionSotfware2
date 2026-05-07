package com.banco.config;

import com.banco.domain.model.entity.*;
import com.banco.domain.model.valueobject.*;
import com.banco.domain.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.time.LocalDate;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataSeeder implements CommandLineRunner {
    private final UserRepository usuarioRepo;
    private final NaturalPersonClientRepository naturalRepo;
    private final CompanyClientRepository empresaRepo;
    private final BankAccountRepository accountRepo;
    private final PasswordEncoder encoder;

    @Override
    public void run(String... args) {
        if (usuarioRepo.existsByUsername("analista")) {
            log.info("Datos de prueba ya cargados.");
            return;
        }

        // 1. Cliente Persona Natural
        NaturalPersonClient cliente1 = NaturalPersonClient.builder()
                .identificationNumber("123456789").fullName("Juan Pérez García")
                .email("juan@mail.com").phone("3001234567")
                .birthDate(LocalDate.of(1990, 5, 15))
                .address("Calle 10 #20-30 Medellín").status(UserStatus.ACTIVE).build();
        naturalRepo.save(cliente1);
        usuarioRepo.save(User.builder().fullName("Juan Pérez García")
                .identificationNumber("123456789").email("juan@mail.com")
                .phone("3001234567").birthDate(LocalDate.of(1990, 5, 15))
                .address("Calle 10 #20-30").role(UserRole.NATURAL_PERSON_CLIENT)
                .status(UserStatus.ACTIVE).username("juan").passwordHash(encoder.encode("juan123"))
                .relatedEntityId("123456789").build());

        // 2. Cliente Persona Natural 2 (representante legal de empresa)
        NaturalPersonClient cliente2 = NaturalPersonClient.builder()
                .identificationNumber("987654321").fullName("María López Ruiz")
                .email("maria@mail.com").phone("3109876543")
                .birthDate(LocalDate.of(1985, 3, 20))
                .address("Carrera 50 #60-70 Medellín").status(UserStatus.ACTIVE).build();
        naturalRepo.save(cliente2);
        usuarioRepo.save(User.builder().fullName("María López Ruiz")
                .identificationNumber("987654321").email("maria@mail.com")
                .phone("3109876543").birthDate(LocalDate.of(1985, 3, 20))
                .address("Carrera 50 #60-70").role(UserRole.NATURAL_PERSON_CLIENT)
                .status(UserStatus.ACTIVE).username("maria").passwordHash(encoder.encode("maria123"))
                .relatedEntityId("987654321").build());

        // 3. Empresa Cliente
        CompanyClient empresa = CompanyClient.builder()
                .taxId("900123456-1").businessName("Tech Solutions SAS")
                .email("contacto@techsolutions.com").phone("6014567890")
                .address("Av. El Dorado 68D-35 Bogotá").legalRepresentativeId("987654321")
                .status(UserStatus.ACTIVE).build();
        empresaRepo.save(empresa);
        usuarioRepo.save(User.builder().fullName("Tech Solutions SAS")
                .identificationNumber("900123456-1").email("contacto@techsolutions.com")
                .phone("6014567890").address("Av. El Dorado 68D-35")
                .role(UserRole.COMPANY_CLIENT).status(UserStatus.ACTIVE)
                .username("techsolutions").passwordHash(encoder.encode("tech123"))
                .relatedEntityId("900123456-1").build());

        // 4. Empleados del banco
        usuarioRepo.save(User.builder().fullName("Carlos Ventanilla")
                .identificationNumber("111111111").email("cajero@banco.com")
                .phone("3001111111").birthDate(LocalDate.of(1995, 1, 10))
                .address("Banco Central").role(UserRole.TELLER_EMPLOYEE)
                .status(UserStatus.ACTIVE).username("cajero").passwordHash(encoder.encode("cajero123")).build());

        usuarioRepo.save(User.builder().fullName("Ana Comercial")
                .identificationNumber("222222222").email("comercial@banco.com")
                .phone("3002222222").birthDate(LocalDate.of(1992, 6, 15))
                .address("Banco Central").role(UserRole.COMMERCIAL_EMPLOYEE)
                .status(UserStatus.ACTIVE).username("comercial").passwordHash(encoder.encode("comercial123")).build());

        usuarioRepo.save(User.builder().fullName("Luis Analista")
                .identificationNumber("333333333").email("analista@banco.com")
                .phone("3003333333").birthDate(LocalDate.of(1988, 9, 20))
                .address("Banco Central").role(UserRole.INTERNAL_ANALYST)
                .status(UserStatus.ACTIVE).username("analista").passwordHash(encoder.encode("analista123")).build());

        // 5. Empleados de la empresa Tech Solutions
        usuarioRepo.save(User.builder().fullName("Pedro Operativo")
                .identificationNumber("444444444").email("pedro@techsolutions.com")
                .phone("3004444444").birthDate(LocalDate.of(1993, 4, 25))
                .address("Empresa").role(UserRole.COMPANY_EMPLOYEE)
                .status(UserStatus.ACTIVE).username("pedro_emp").passwordHash(encoder.encode("pedro123"))
                .associatedCompanyId("900123456-1").relatedEntityId("900123456-1").build());

        usuarioRepo.save(User.builder().fullName("Sofía Supervisora")
                .identificationNumber("555555555").email("sofia@techsolutions.com")
                .phone("3005555555").birthDate(LocalDate.of(1982, 11, 5))
                .address("Empresa").role(UserRole.COMPANY_SUPERVISOR)
                .status(UserStatus.ACTIVE).username("sofia_sup").passwordHash(encoder.encode("sofia123"))
                .associatedCompanyId("900123456-1").relatedEntityId("900123456-1").build());

        // 6. Cuentas bancarias de prueba
        accountRepo.save(BankAccount.builder().accountNumber("1001-0001")
                .accountType(AccountType.SAVINGS).holderId("123456789")
                .balance(Money.of(new BigDecimal("5000000"), "COP"))
                .status(AccountStatus.ACTIVE).openingDate(LocalDate.now()).build());

        accountRepo.save(BankAccount.builder().accountNumber("1001-0002")
                .accountType(AccountType.CHECKING).holderId("987654321")
                .balance(Money.of(new BigDecimal("2000000"), "COP"))
                .status(AccountStatus.ACTIVE).openingDate(LocalDate.now()).build());

        accountRepo.save(BankAccount.builder().accountNumber("2001-0001")
                .accountType(AccountType.SAVINGS).holderId("900123456-1")
                .balance(Money.of(new BigDecimal("50000000"), "COP"))
                .status(AccountStatus.ACTIVE).openingDate(LocalDate.now()).build());

        log.info("✅ Datos de prueba cargados correctamente.");
        log.info("👤 Usuarios: juan/juan123 | maria/maria123 | cajero/cajero123 | comercial/comercial123 | analista/analista123 | pedro_emp/pedro123 | sofia_sup/sofia123 | techsolutions/tech123");
    }
}
