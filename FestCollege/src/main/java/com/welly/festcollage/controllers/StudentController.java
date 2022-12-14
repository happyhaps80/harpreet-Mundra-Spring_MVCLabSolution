package com.welly.festcollage.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.welly.festcollage.model.Student;
import com.welly.festcollage.services.StudentServices;

@Controller
@RequestMapping("/students")
public class StudentController {

	@Autowired
	private StudentServices studentServices;

	// Add mapping for list
	@RequestMapping("/list")
	public String listStudents(Model theModel) {

		// get Students from db
		List<Student> theStudents = studentServices.findAll();

		// Add to the spring model
		theModel.addAttribute("Students", theStudents);

		return "list-Students";

	}

	@RequestMapping("/showFormForAdd")
	public String showFormForAdd(Model theModel) {

		// create model attribute to bind form data
		Student theStudent = new Student();

		theModel.addAttribute("Student", theStudent);

		return "student-form";
	}

	@RequestMapping("/showFormForUpdate")
	public String showFormForUpdate(@RequestParam("studentId") int theId, Model theModel) {

		// get the Student from the service
		Student theStudent = studentServices.findById(theId);

		// set Student as a model attribute to pre-populate the form
		theModel.addAttribute("Student", theStudent);

		// send over to our form
		return "student-form";
	}

	@PostMapping("/save")
	public String saveStudent(@RequestParam("id") int id, @RequestParam("name") String name,
			@RequestParam("department") String department, @RequestParam("country") String country) {

		System.out.println(id);
		Student theStudent;
		if (id != 0) {
			theStudent = studentServices.findById(id);
			theStudent.setName(name);
			theStudent.setDepartment(department);
			theStudent.setCountry(country);
		} else
			theStudent = new Student(id, name, department, country);
		// save the Student
		studentServices.save(theStudent);

		// use a redirect to prevent duplicate submissions
		return "redirect:/students/list";

	}

	@RequestMapping("/delete")
	public String delete(@RequestParam("StudentID") int theId) {

		// delete the Student
		studentServices.deleteById(theId);

		// redirect to /Student/list
		return "redirect:/students/list";

	}

	@RequestMapping("/search")
	public String search(@RequestParam("name") String name, @RequestParam("department") String department,
			Model theModel) {

		// check names, if both are empty then just give list of all Students

		if (name.trim().isEmpty() && department.trim().isEmpty()) {
			return "redirect:/students/list";
		} else {
			// else, search by first name and last name
			List<Student> theStudents = studentServices.searchBy(name, department);

			// add to the spring model
			theModel.addAttribute("Students", theStudents);

			// send to list-Books
			return "list-Students";
		}

	}
}
