package com.ironhack.hospitals_testscontroller.controller;

import com.ironhack.hospitals_testscontroller.dto.EmployeeDepartmentDTO;
import com.ironhack.hospitals_testscontroller.dto.EmployeeResponseDTO;
import com.ironhack.hospitals_testscontroller.dto.EmployeeStatusDTO;
import com.ironhack.hospitals_testscontroller.enums.Status;
import com.ironhack.hospitals_testscontroller.model.Employee;
import com.ironhack.hospitals_testscontroller.repository.EmployeeRepository;
import com.ironhack.hospitals_testscontroller.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    private final EmployeeRepository employeeRepository;

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<Employee> filterDoctors(@RequestParam Optional<Status> status, @RequestParam Optional<String> department) {
        if (status.isPresent()) {
            return employeeRepository.findByStatus(status.get());
        } else if (department.isPresent()) {
            return employeeRepository.findByDepartment(department.get());
        } else {
            return employeeRepository.findAll();
        }
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Employee getDoctorById(@PathVariable("id") Long employee_id){
        return employeeService.findById(employee_id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EmployeeResponseDTO createEmployee(@Valid @RequestBody EmployeeResponseDTO employeeDTO){
        return employeeService.createEmployee(employeeDTO);
    }

    @PatchMapping("/{id}/status")
    @ResponseStatus(HttpStatus.OK)
    public EmployeeResponseDTO updateStatus(@PathVariable Long id, @Valid @RequestBody EmployeeStatusDTO employeeStatusDto) {
        return employeeService.updateStatus(id, employeeStatusDto);
    }

    @PatchMapping("/{id}/department")
    @ResponseStatus(HttpStatus.OK)
    public EmployeeResponseDTO updateDepartment(@PathVariable Long id, @Valid @RequestBody EmployeeDepartmentDTO employeeDepartmentDto) {
        return employeeService.updateDepartment(id, employeeDepartmentDto);
    }

}
