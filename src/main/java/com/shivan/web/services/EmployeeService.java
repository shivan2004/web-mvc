package com.shivan.web.services;

import com.shivan.web.dto.EmployeeDTO;
import com.shivan.web.entites.EmployeeEntity;
import com.shivan.web.repositories.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.util.ReflectionUtils;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final ModelMapper modelMapper;

    public Optional<EmployeeDTO> getEmployeeById(Long employeeId) {
//        Optional<EmployeeEntity> employeeEntity =  employeeRepository.findById(employeeId);
//        return employeeEntity.map(employeeEntity1 -> modelMapper.map(employeeEntity1, EmployeeDTO.class));
        return employeeRepository.findById(employeeId).map(employeeEntity -> modelMapper.map(employeeEntity, EmployeeDTO.class));
    }

    public List<EmployeeDTO> getAllEmployees() {
        List<EmployeeEntity> employeeEntities =  employeeRepository.findAll();
        return employeeEntities
                .stream()
                .map(employeeEntity -> modelMapper.map(employeeEntity, EmployeeDTO.class))
                .collect(Collectors.toList());
    }

    public EmployeeDTO createNewEmployee(EmployeeDTO inputEmployee) {
        EmployeeEntity toSaveEntity = modelMapper.map(inputEmployee, EmployeeEntity.class);
        EmployeeEntity savedEmployee = employeeRepository.save(toSaveEntity);
        return modelMapper.map(savedEmployee, EmployeeDTO.class);
    }


    public EmployeeDTO updateEmployeeById(Long employeeId, EmployeeDTO employeeDTO) {
        EmployeeEntity employeeEntity = modelMapper.map(employeeDTO, EmployeeEntity.class);
        employeeEntity.setId(employeeId);
        EmployeeEntity savedEmployeeEntity = employeeRepository.save(employeeEntity);
        return modelMapper.map(savedEmployeeEntity, EmployeeDTO.class);
    }

    public boolean deleteEmployeeById(Long employeeId) {

        if(!isExistsByEmployeeId(employeeId)) return false;
        employeeRepository.deleteById(employeeId);
        return true;
    }

    public boolean isExistsByEmployeeId(Long employeeId) {
        return employeeRepository.existsById(employeeId);
    }

    public EmployeeDTO partialUpdateById(Long employeeId, Map<String, Object> updates) {
        if(!isExistsByEmployeeId(employeeId)) return null;
        EmployeeEntity employeeEntity = employeeRepository.findById(employeeId).orElse(null);

        updates.forEach((key, value) -> {
            Field fieldToBeUpdated = ReflectionUtils.findRequiredField(EmployeeEntity.class, key);
            fieldToBeUpdated.setAccessible(true);
            ReflectionUtils.setField(fieldToBeUpdated, employeeEntity, value);
//            fieldToBeUpdated.set(false);
        });
        return modelMapper.map(employeeRepository.save(employeeEntity), EmployeeDTO.class);
    }
}
