package com.mrd.employee.service.impl;

import com.mrd.employee.dto.APIResponseDto;
import com.mrd.employee.dto.DepartmentDto;
import com.mrd.employee.dto.EmployeeDto;
import com.mrd.employee.dto.OrganizationDto;
import com.mrd.employee.entity.Employee;
import com.mrd.employee.repository.EmployeeRepository;
import com.mrd.employee.service.APIClient;
import com.mrd.employee.service.EmployeeService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@AllArgsConstructor
@Service
public class EmployeeServiceImpl implements EmployeeService {
    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    private EmployeeRepository employeeRepository;

    private ModelMapper modelMapper;

    //private RestTemplate restTemplate;
    private WebClient webClient;
    private APIClient apiClient;

    @Override
    public EmployeeDto saveEmployee(EmployeeDto employeeDTO) {

        //convert employee DTO to employee
        Employee employee = modelMapper.map(employeeDTO, Employee.class);

        Employee savedEmployee = employeeRepository.save(employee);
        //convert employee  to employee DTO
        EmployeeDto savedEmployeeDTO = modelMapper.map(savedEmployee, EmployeeDto.class);
        return savedEmployeeDTO;
    }

    //@CircuitBreaker(name = "${spring.application.name}", fallbackMethod = "getDefaultDepartment")
    @Retry(name = "${spring.application.name}", fallbackMethod = "getDefaultDepartment")
    @Override
    public APIResponseDto getEmployeeById(Long id) {
        LOGGER.info("inside getEmployeeById() method");
        Employee employee = employeeRepository.findById(id).get();
//call with Rest template
//        ResponseEntity<DepartmentDto> responseEntity = restTemplate.getForEntity("http://localhost:8080/api/departments/" + employee.getDepartmentCode(),
//                DepartmentDto.class);
//
//        DepartmentDto departmentDto = responseEntity.getBody();

        //call with web client
//        DepartmentDto departmentDto = webClient.get()
//                .uri("http://localhost:8080/api/departments/" + employee.getDepartmentCode())
//                .retrieve()
//                .bodyToMono(DepartmentDto.class)
//                .block();
        DepartmentDto departmentDto = apiClient.getDepartmentByCode(employee.getDepartmentCode());

        //call with web client ( cach 2)
        OrganizationDto organizationDto = webClient.get()
                .uri("http://localhost:8083/api/organizations/" + employee.getOrganizationCode())
                .retrieve()
                .bodyToMono(OrganizationDto.class)
                .block();

        APIResponseDto apiResponseDto = new APIResponseDto();
        apiResponseDto.setEmployee(modelMapper.map(employee, EmployeeDto.class));
        apiResponseDto.setDepartment(departmentDto);
        apiResponseDto.setOrganization(organizationDto);

        return apiResponseDto;
    }

    //fallback method for getEmployeeById method
    public APIResponseDto getDefaultDepartment(Long id, Exception e) {
        LOGGER.info("inside getDefaultDepartment() method");
        Employee employee = employeeRepository.findById(id).get();
        DepartmentDto departmentDto = new DepartmentDto(2L,"R&D Deartment", "RD001", "Research and Development Department");
        APIResponseDto apiResponseDto = new APIResponseDto();
        apiResponseDto.setEmployee(modelMapper.map(employee, EmployeeDto.class));
        apiResponseDto.setDepartment(departmentDto);

        return apiResponseDto;
    }
}
