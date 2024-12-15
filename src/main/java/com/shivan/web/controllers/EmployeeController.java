package com.shivan.web.controllers;

import com.shivan.web.entites.EmployeeEntity;
import com.shivan.web.repositories.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeRepository employeeRepository;

    @GetMapping(path = "/{employeeId}")
    public EmployeeEntity getEmployeeById(@PathVariable Long employeeId) {
        return employeeRepository.findById(employeeId).orElse(null);
    }

    @GetMapping
    public List<EmployeeEntity> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @PostMapping
    public EmployeeEntity createNewEmployee(@RequestBody EmployeeEntity inputEmployee) {
        return employeeRepository.save(inputEmployee);
    }
}

