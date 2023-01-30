package com.ironhack.hospitals_testscontroller.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ironhack.hospitals_testscontroller.dto.EmployeeDepartmentDTO;
import com.ironhack.hospitals_testscontroller.dto.EmployeeResponseDTO;
import com.ironhack.hospitals_testscontroller.dto.EmployeeStatusDTO;
import com.ironhack.hospitals_testscontroller.enums.Status;
import com.ironhack.hospitals_testscontroller.model.Employee;
import com.ironhack.hospitals_testscontroller.repository.EmployeeRepository;
import com.ironhack.hospitals_testscontroller.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.http.MediaType;


import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EmployeeController.class)
class EmployeeControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    EmployeeService employeeService;

    @MockBean
    EmployeeRepository employeeRepository;

    @Autowired
    ObjectMapper objectMapper;

    List<Employee> doctors = new ArrayList<>();

    @BeforeEach
    void setUp() {
        doctors = List.of(new Employee(5678564L, "cardiology", "alonso", Status.ON),
                new Employee(5678976L, "dentist", "pepe", Status.ON),
                new Employee(2736485L, "cardiology", "juan", Status.OFF));
    }


    @Test
    void filterDoctorsByDepartment() throws Exception {
        when(employeeRepository.findByDepartment("cardiology")).thenReturn(List.of(doctors.get(0), doctors.get(2)));
        String testDepartment = "cardiology";

        mockMvc.perform(get("/employees")
                        .param("department", testDepartment)
                        .param("status", ""))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.*", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].name").value("alonso"));
    }

    @Test
    void filterDoctorsByStatus() throws Exception {
        when(employeeRepository.findByDepartment("cardiology")).thenReturn(List.of(doctors.get(0), doctors.get(2)));
        when(employeeRepository.findByStatus(Status.ON)).thenReturn(List.of(doctors.get(0), doctors.get(1)));
        Status testStatus = Status.ON;
        String testDepartment = "";

        mockMvc.perform(get("/employees")
                        .param("department", testDepartment)
                        .param("status", String.valueOf(testStatus)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.*", hasSize(2)));
    }

    @Test
    void filterAllDoctors() throws Exception {
        when(employeeRepository.findAll()).thenReturn(doctors);
        mockMvc.perform(get("/employees"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.*", hasSize(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[2].name").value("juan"));
    }

    @Test
    void getDoctorById() throws Exception {
        when(employeeService.findById(5678976L)).thenReturn(doctors.get(1));
        var testId = 5678976L;
        mockMvc.perform(get("/employees/{id}", testId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("pepe"));
    }

    @Test
    void createEmployee() throws Exception {
        EmployeeResponseDTO response = EmployeeResponseDTO.fromEmployee(doctors.get(2));
        when(employeeService.createEmployee(response)).thenReturn(response);
        mockMvc.perform(post("/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(doctors.get(2))))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("juan"));
    }

    @Test
    void updateStatus() throws Exception {
        var testId = 2736485L;
        EmployeeStatusDTO statusDTO = new EmployeeStatusDTO(Status.OFF);
        EmployeeResponseDTO response = EmployeeResponseDTO.fromEmployee(doctors.get(2));

        when(employeeService.updateStatus(testId, statusDTO)).thenReturn(response);
        mockMvc.perform(patch("/employees/{id}/status", 2736485L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(statusDTO)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(String.valueOf(Status.OFF)));
    }

    @Test
    void updateDepartment() throws Exception {
        var testId = 2736485L;
        EmployeeDepartmentDTO departmentDTO = new EmployeeDepartmentDTO("cardiology");
        EmployeeResponseDTO response = EmployeeResponseDTO.fromEmployee(doctors.get(2));

        when(employeeService.updateDepartment(testId, departmentDTO)).thenReturn(response);
        mockMvc.perform(patch("/employees/{id}/department", 2736485L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(departmentDTO)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.department").value("cardiology"));


    }
}