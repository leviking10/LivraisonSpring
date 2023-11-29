package com.casamancaise.services;

import com.casamancaise.dao.EmployeeRepository;
import com.casamancaise.dto.EmployeeDto;
import com.casamancaise.entities.Employee;
import com.casamancaise.mapping.EmployeeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class EmployeeServiceImpl implements EmployeeService{
    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository, EmployeeMapper employeeMapper) {
        this.employeeRepository = employeeRepository;
        this.employeeMapper = employeeMapper;
    }

    @Override
    public EmployeeDto createEmployee(EmployeeDto employeeDto) {
        // Convert DTO to entity
        Employee employee = employeeMapper.toEntity(employeeDto);
        // Save entity to the database
        employee = employeeRepository.save(employee);
        // Return the saved entity back as DTO
        return employeeMapper.toDto(employee);
    }

    @Override
    public EmployeeDto getEmployeeById(Long id) {
        // Retrieve the employee by id
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + id));
        // Convert entity to DTO
        return employeeMapper.toDto(employee);
    }

    @Override
    public List<EmployeeDto> getAllEmployees() {
        // Retrieve all employees
        List<Employee> employees = employeeRepository.findAll();
        // Convert list of entities to list of DTOs
        return employees.stream()
                .map(employeeMapper::toDto)
                .toList();
    }

    @Override
    public EmployeeDto updateEmployee(Long id, EmployeeDto employeeDto) {
        // Retrieve the employee by id
        Employee existingEmployee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + id));
        // Map the DTO to the existing entity
        employeeMapper. updateEmployeeFromDto(employeeDto, existingEmployee);
        // Save the updated entity to the database
        existingEmployee = employeeRepository.save(existingEmployee);
        // Convert the updated entity to DTO
        return employeeMapper.toDto(existingEmployee);
    }

    @Override
    public void deleteEmployee(Long id) {
        // Delete the employee by id
        employeeRepository.deleteById(id);
    }
}