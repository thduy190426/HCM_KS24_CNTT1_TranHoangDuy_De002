package com.rk.employeemanagement.controller;

import com.rk.employeemanagement.model.Employee;
import com.rk.employeemanagement.service.EmployeeService;
import jakarta.servlet.ServletContext;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/")
public class EmployeeController {

    private final EmployeeService employeeService;
    private final ServletContext servletContext;

    @Autowired
    public EmployeeController(EmployeeService employeeService, ServletContext servletContext) {
        this.employeeService = employeeService;
        this.servletContext = servletContext;
    }

    @GetMapping({"/", "/list"})
    public String listEmployees(@RequestParam(name = "keyword", required = false) String keyword, Model model) {
        List<Employee> employees = employeeService.searchEmployees(keyword);
        model.addAttribute("employees", employees);
        model.addAttribute("keyword", keyword);
        return "list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("employee", new Employee());
        return "form";
    }

    @PostMapping("/save")
    public String saveEmployee(@Valid @ModelAttribute("employee") Employee employee,
                               BindingResult bindingResult,
                               Model model) {

        if (bindingResult.hasErrors()) {
            return "form";
        }

        MultipartFile avatarFile = employee.getAvatarFile();
        if (avatarFile != null && !avatarFile.isEmpty()) {
            try {
                String uploadsDir = servletContext.getRealPath("/uploads/");
                File directory = new File(uploadsDir);
                if (!directory.exists()) {
                    directory.mkdirs();
                }

                String originalFileName = avatarFile.getOriginalFilename();
                String extension = originalFileName != null ? originalFileName.substring(originalFileName.lastIndexOf(".")) : ".jpg";
                String newFileName = UUID.randomUUID().toString() + extension;

                File destinationFile = new File(uploadsDir + File.separator + newFileName);
                avatarFile.transferTo(destinationFile);

                employee.setAvatar(newFileName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (employee.getId() != null) {
            Employee existingEmp = employeeService.getEmployeeById(employee.getId());
            if (existingEmp != null) {
                employee.setAvatar(existingEmp.getAvatar());
            }
        }

        employeeService.saveEmployee(employee);
        return "redirect:/list";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") String id, Model model) {
        Employee employee = employeeService.getEmployeeById(id);
        if (employee != null) {
            model.addAttribute("employee", employee);
            return "form";
        }
        return "redirect:/list";
    }

    @GetMapping("/delete/{id}")
    public String deleteEmployee(@PathVariable("id") String id) {
        employeeService.deleteEmployee(id);
        return "redirect:/list";
    }
}