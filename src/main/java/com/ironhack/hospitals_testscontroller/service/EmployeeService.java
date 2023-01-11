package com.ironhack.hospitals_testscontroller.service;

import com.ironhack.hospitals_testscontroller.dto.EmployeeDTO;
import com.ironhack.hospitals_testscontroller.enums.Status;
import com.ironhack.hospitals_testscontroller.model.Employee;
import com.ironhack.hospitals_testscontroller.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmployeeService {
    private final EmployeeRepository employeeRepository;

    public List<EmployeeDTO> findAll() {

        List<EmployeeDTO> employeesDTO = new ArrayList<>();
        List<Employee> employees = employeeRepository.findAll();
        for (Employee employee : employees){
            employeesDTO.add(EmployeeDTO.fromEmployee(employee));
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

    public EmployeeDTO createEmployee(EmployeeDTO employeeDTO) {
        var employee = Employee.fromDTO(employeeDTO);
        employeeRepository.save(employee);
        return EmployeeDTO.fromEmployee(employee);
    }

    public EmployeeDTO changeStatusOrDepartmentEmployee(Long id, Optional<Status> status, Optional <String> department) {

        var employee = employeeRepository.findById(id).orElseThrow();
        status.ifPresent(employee::setStatus);
        department.ifPresent(employee::setDepartment);
        employeeRepository.save(employee);
        return EmployeeDTO.fromEmployee(employee);
    }

    //Option with PUT to change all or some params of employee
    public EmployeeDTO updateEmployee(Long id, String department, String name, Status status) {
        var employee = employeeRepository.findById(id).orElseThrow();
        if(!department.isBlank()){employee.setDepartment(department);}
        if(!name.isBlank()){employee.setName(name);}
        if(!status.toString().isBlank()){employee.setStatus(status);}
        employeeRepository.save(employee);
        return EmployeeDTO.fromEmployee(employee);
    }
}
