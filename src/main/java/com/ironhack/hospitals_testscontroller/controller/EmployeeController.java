package com.ironhack.hospitals_testscontroller.controller;

import com.ironhack.hospitals_testscontroller.dto.EmployeeDTO;
import com.ironhack.hospitals_testscontroller.enums.Status;
import com.ironhack.hospitals_testscontroller.model.Employee;
import com.ironhack.hospitals_testscontroller.service.EmployeeService;
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

    @GetMapping
    public List<EmployeeDTO> getAllDoctors(){
        return employeeService.findAll();
    }

    @GetMapping("/{id}")
    public Employee getDoctorById(@PathVariable("id") Long employee_id){
        return employeeService.findById(employee_id);
    }

    @GetMapping("/status/{value}")
    public List<Employee> getDoctorByStatus(@PathVariable ("value") Status status){
        return employeeService.findByStatus(status);
    }

    @GetMapping("/department/{name}")
    public List<Employee> getDoctorsByDepartment(@PathVariable ("name") String department){
        return employeeService.findByDepartment(department);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EmployeeDTO createEmployee(EmployeeDTO employeeDTO){
        return employeeService.createEmployee(employeeDTO);
    }

    @PatchMapping("/{id}")
    public EmployeeDTO changeStatusEmployee(@PathVariable (name = "id") Long id,
                                            /*@RequestParam (required = false) Status status,
                                            @RequestParam (required = false) String department){*/
                                            @RequestParam Optional <Status> status,
                                            @RequestParam Optional<String> department){
        return employeeService.changeStatusOrDepartmentEmployee(id, status, department);

    }


    @PutMapping("/{id}")
    public EmployeeDTO updateEmployee(@PathVariable (name = "id") Long id,
                                      @RequestParam (required = false) String department,
                                      @RequestParam (required = false) String name,
                                      @RequestParam (required = false) Status status){
        return employeeService.updateEmployee(id, department, name, status);

    }

}
