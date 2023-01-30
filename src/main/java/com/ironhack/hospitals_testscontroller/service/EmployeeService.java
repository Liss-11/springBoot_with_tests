package com.ironhack.hospitals_testscontroller.service;

import com.ironhack.hospitals_testscontroller.dto.EmployeeDepartmentDTO;
import com.ironhack.hospitals_testscontroller.dto.EmployeeResponseDTO;
import com.ironhack.hospitals_testscontroller.dto.EmployeeStatusDTO;
import com.ironhack.hospitals_testscontroller.enums.Status;
import com.ironhack.hospitals_testscontroller.model.Employee;
import com.ironhack.hospitals_testscontroller.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.management.BadAttributeValueExpException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmployeeService {
    private final EmployeeRepository employeeRepository;

    public List<EmployeeResponseDTO> findAll() {

        List<EmployeeResponseDTO> employeesDTO = new ArrayList<>();
        List<Employee> employees = employeeRepository.findAll();
        for (Employee employee : employees){
            employeesDTO.add(EmployeeResponseDTO.fromEmployee(employee));
        }
        return employeesDTO;
    }

    public Employee findById(Long employeeId){

        return employeeRepository.findById(employeeId).orElseThrow();
    }

    public List<Employee> findByStatus(Status status) {
        return employeeRepository.findByStatus(status);
    }

    public List<Employee> findByDepartment(String department) {

        return employeeRepository.findByDepartment(department);
    }

    public EmployeeResponseDTO createEmployee(EmployeeResponseDTO employeeDTO) {
        var employee = Employee.fromDTO(employeeDTO);
        employeeRepository.save(employee);
        return EmployeeResponseDTO.fromEmployee(employee);
    }

    public EmployeeResponseDTO updateStatus(Long id, EmployeeStatusDTO statusDTO) {
        Optional<Employee> employee = employeeRepository.findById(id);
        if (employee.isPresent()) {
            employee.get().setStatus(statusDTO.getStatus());
            employeeRepository.save(employee.get());
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The doctor with this ID does not exist");
        }
        return EmployeeResponseDTO.fromEmployee(employee.get());
    }

    public EmployeeResponseDTO updateDepartment(Long id, EmployeeDepartmentDTO departmentDTO) {
        Optional<Employee> employee = employeeRepository.findById(id);
        if (employee.isPresent()) {
            employee.get().setDepartment(departmentDTO.getDepartment());
            employeeRepository.save(employee.get());
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The doctor with this ID does not exist");
        }
        return EmployeeResponseDTO.fromEmployee(employee.get());
    }
}
