package com.ironhack.hospital_security.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ironhack.hospital_security.dto.EmployeeDepartmentDTO;
import com.ironhack.hospital_security.dto.EmployeeResponseDTO;
import com.ironhack.hospital_security.dto.EmployeeStatusDTO;
import com.ironhack.hospital_security.enums.Role;
import com.ironhack.hospital_security.enums.Status;
import com.ironhack.hospital_security.model.Employee;
import com.ironhack.hospital_security.repository.EmployeeRepository;
import com.ironhack.hospital_security.repository.UserRepository;
import com.ironhack.hospital_security.security.SecurityConfig;
import com.ironhack.hospital_security.security.UserDetailsConfig;
import com.ironhack.hospital_security.security.UserDetailsServiceImpl;
import com.ironhack.hospital_security.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EmployeeController.class)
@Import({SecurityConfig.class, UserDetailsConfig.class})
class EmployeeControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    EmployeeService employeeService;

    @MockBean
    UserRepository userRepository;

    @MockBean
    UserDetailsServiceImpl userDetailsService;

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
    @WithMockUser(roles = "ADMIN")
    void filterDoctorsByDepartment() throws Exception {
        when(employeeRepository.findByDepartment("cardiology")).thenReturn(List.of(doctors.get(0), doctors.get(2)));
        String testDepartment = "cardiology";

        mockMvc.perform(get("/employees")
                        .param("department", testDepartment)
                        .param("status", "")
                        )
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
    @WithMockUser(roles = "ADMIN")
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
    @WithMockUser(roles = "ADMIN")
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
    @WithMockUser(roles = "ADMIN")
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