package com.ironhack.hospital_security.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ironhack.hospital_security.dto.PatientDTO;
import com.ironhack.hospital_security.enums.Status;
import com.ironhack.hospital_security.model.Employee;
import com.ironhack.hospital_security.model.Patient;
import com.ironhack.hospital_security.security.SecurityConfig;
import com.ironhack.hospital_security.security.UserDetailsServiceImpl;
import com.ironhack.hospital_security.service.PatientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.web.servlet.function.RequestPredicates.param;

@WebMvcTest(PatientController.class)
@Import({SecurityConfig.class})
class PatientControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    PatientService patientService;

    @MockBean
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    ObjectMapper objectMapper;

    List<Employee> doctors = new ArrayList<>();
    List<Patient> patients = new ArrayList<>();

    @BeforeEach
    void setUp() {

        doctors = List.of(new Employee(5678564L, "cardiology", "alonso", Status.ON),
                new Employee(5678976L, "dentist", "pepe", Status.ON),
                new Employee(2736485L, "cardiology", "juan", Status.OFF));

        patients = List.of(new Patient("Lorena", LocalDate.parse("1992-11-25"), doctors.get(2)),
                new Patient("Antonio", LocalDate.parse("1991-12-22"), doctors.get(0)),
                new Patient("Aracelly", LocalDate.parse("2002-01-02"), doctors.get(0)),
                new Patient("Manoli", LocalDate.parse("1900-09-13"), doctors.get(1)));

        patients.get(2).setId(333333333L);
        patients.get(0).setId(123456L);
    }

    @Test
    void getAllPatientsTest() throws Exception {
        when(patientService.findAll()).thenReturn(patients);
        mockMvc.perform(get("/patients"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.*", hasSize(4)));

    }

    @Test
    void getPatientByIdTest() throws Exception {
        when(patientService.findById(123456L)).thenReturn(patients.get(0));
        mockMvc.perform(get("/patients/{id}", 123456L))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Lorena"));
    }

    @Test
    void getPatientsByDateOfBirthBetweenTest() throws Exception {
        when(patientService.findByDateOfBirthBetween(LocalDate.parse("1880-01-01"), LocalDate.parse("2000-01-01")))
                .thenReturn(List.of(patients.get(0), patients.get(1), patients.get(3)));
        mockMvc.perform(get("/patients/date_of_birth")
                .param("from", "1880-01-01")
                .param("to", "2000-01-01"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.*", hasSize(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].dateOfBirth").value("1992-11-25"));
    }

    @Test
    void getPatientsByDoctorDepartmentTest() throws Exception {
        when(patientService.findByEmployeeDepartment("cardiology"))
                .thenReturn(List.of(patients.get(0), patients.get(1), patients.get(2)));
        mockMvc.perform(get("/patients/doctor_department/{department}", "cardiology"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.*", hasSize(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].name").value("Lorena"));
    }

    @Test
    void getPatientsByDoctorWithStatusOffTest() throws Exception {
        when(patientService.findByEmployeeStatus(Status.OFF))
                .thenReturn(List.of(patients.get(0)));
        mockMvc.perform(get("/patients/doctors_Status_OFF"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.*", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].name").value("Lorena"));
    }


    @Test
    @WithMockUser(roles = "ADMIN")
    void createPatientTest() throws Exception {
        PatientDTO patientDTO = PatientDTO.fromPatient(patients.get(3));
        when(patientService.createPatient(patientDTO)).thenReturn(patients.get(3));
        mockMvc.perform(post("/patients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(patientDTO)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Manoli"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updatePatientTest() throws Exception {
        PatientDTO patientDTO = PatientDTO.fromPatient(patients.get(2));
        var testId = 333333333L;
        when(patientService.updatePatient(testId, patientDTO)).thenReturn(patients.get(2));
        mockMvc.perform(put("/patients/update_all/{id}", testId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(patientDTO)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Aracelly"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updatePatientParamsTest() throws Exception {
        var testId = 333333333L;
        var name = Optional.of("Aracelly");
        var dateOfBirth = Optional.of(LocalDate.parse("2002-01-02"));
        var doctor = Optional.of(doctors.get(0));

        when(patientService.updatePatientParams(testId, name, dateOfBirth, doctor))
                .thenReturn(patients.get(2));
        mockMvc.perform(put("/patients/update_some/{id}", testId)
                        .param("name","Aracelly")
                        .param("dateOfBirth", "2002-01-02")
                        .param("employee", objectMapper.writeValueAsString(doctors.get(0))))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Aracelly"));
    }

}