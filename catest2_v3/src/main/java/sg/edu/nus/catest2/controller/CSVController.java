package sg.edu.nus.catest2.controller;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.CSVWriter;

import sg.edu.nus.catest2.repo.*;
import sg.edu.nus.catest2.model.*;

@Controller
public class CSVController {

	@Autowired
	DepartmentRepository drepo;
	@Autowired
	CourseRepository crepo;
	@Autowired
	CourseApplicationRepository carepo;
	@Autowired
	AdminRepository arepo;
	@Autowired
	FacultyRepository frepo;
	@Autowired
	StudentRepository srepo;
	@Autowired
	UserRepository urepo;
	@Autowired
	GradeRepository grepo;
	@Autowired
	FacultyLeaveRepository flrepo;

	@RequestMapping("/csvdepartments")
	public void exportdepartments(HttpServletResponse response) throws Exception {

		// set file name and content type
		String filename = "departments.csv";

		response.setContentType("text/csv");
		response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"");

		// create a csv writer
		StatefulBeanToCsv<Department> writer = new StatefulBeanToCsvBuilder<Department>(response.getWriter())
				.withQuotechar(CSVWriter.NO_QUOTE_CHARACTER).withSeparator(CSVWriter.DEFAULT_SEPARATOR)
				.withOrderedResults(true).build();

		// write all users to csv file
		List<Department> departments = drepo.findAll();
		writer.write(departments);
	}
	
	@RequestMapping("/csvcourses")
	public void exportcourses(HttpServletResponse response) throws Exception {

		// set file name and content type
		String filename = "courses.csv";

		response.setContentType("text/csv");
		response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"");

		// create a csv writer
		StatefulBeanToCsv<Course> writer = new StatefulBeanToCsvBuilder<Course>(response.getWriter())
				.withQuotechar(CSVWriter.NO_QUOTE_CHARACTER).withSeparator(CSVWriter.DEFAULT_SEPARATOR)
				.withOrderedResults(true).build();

		// write all users to csv file
		List<Course> courses = crepo.findAll();
		writer.write(courses);
	}
	
	@RequestMapping("/csvcourseapplications")
	public void exportcourseapplications(HttpServletResponse response) throws Exception {

		// set file name and content type
		String filename = "courseapplications.csv";

		response.setContentType("text/csv");
		response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"");

		// create a csv writer
		StatefulBeanToCsv<CourseApplication> writer = new StatefulBeanToCsvBuilder<CourseApplication>(response.getWriter())
				.withQuotechar(CSVWriter.NO_QUOTE_CHARACTER).withSeparator(CSVWriter.DEFAULT_SEPARATOR)
				.withOrderedResults(true).build();

		// write all users to csv file
		List<CourseApplication> courseApplications = carepo.findAll();
		writer.write(courseApplications);
	}
	
	@RequestMapping("/csvadmins")
	public void exportadmins(HttpServletResponse response) throws Exception {

		// set file name and content type
		String filename = "admins.csv";

		response.setContentType("text/csv");
		response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"");

		// create a csv writer
		StatefulBeanToCsv<Admin> writer = new StatefulBeanToCsvBuilder<Admin>(response.getWriter())
				.withQuotechar(CSVWriter.NO_QUOTE_CHARACTER).withSeparator(CSVWriter.DEFAULT_SEPARATOR)
				.withOrderedResults(true).build();

		// write all users to csv file
		List<Admin> admins = arepo.findAll();
		writer.write(admins);
	}
	
	@RequestMapping("/csvfaculties")
	public void exportfaculties(HttpServletResponse response) throws Exception {

		// set file name and content type
		String filename = "faculties.csv";

		response.setContentType("text/csv");
		response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"");

		// create a csv writer
		StatefulBeanToCsv<Faculty> writer = new StatefulBeanToCsvBuilder<Faculty>(response.getWriter())
				.withQuotechar(CSVWriter.NO_QUOTE_CHARACTER).withSeparator(CSVWriter.DEFAULT_SEPARATOR)
				.withOrderedResults(true).build();

		// write all users to csv file
		List<Faculty> faculties = frepo.findAll();
		writer.write(faculties);
	}
	
	@RequestMapping("/csvstudents")
	public void exportstudents(HttpServletResponse response) throws Exception {

		// set file name and content type
		String filename = "students.csv";

		response.setContentType("text/csv");
		response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"");

		// create a csv writer
		StatefulBeanToCsv<Student> writer = new StatefulBeanToCsvBuilder<Student>(response.getWriter())
				.withQuotechar(CSVWriter.NO_QUOTE_CHARACTER).withSeparator(CSVWriter.DEFAULT_SEPARATOR)
				.withOrderedResults(true).build();

		// write all users to csv file
		List<Student> students = srepo.findAll();
		writer.write(students);
	}
	
	@RequestMapping("/csvusers")
	public void exportusers(HttpServletResponse response) throws Exception {

		// set file name and content type
		String filename = "users.csv";

		response.setContentType("text/csv");
		response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"");

		// create a csv writer
		StatefulBeanToCsv<User> writer = new StatefulBeanToCsvBuilder<User>(response.getWriter())
				.withQuotechar(CSVWriter.NO_QUOTE_CHARACTER).withSeparator(CSVWriter.DEFAULT_SEPARATOR)
				.withOrderedResults(true).build();

		// write all users to csv file
		List<User> users = urepo.findAll();
		writer.write(users);
	}
	
	@RequestMapping("/csvgrades")
	public void exportgrades(HttpServletResponse response) throws Exception {

		// set file name and content type
		String filename = "grades.csv";

		response.setContentType("text/csv");
		response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"");

		// create a csv writer
		StatefulBeanToCsv<Grade> writer = new StatefulBeanToCsvBuilder<Grade>(response.getWriter())
				.withQuotechar(CSVWriter.NO_QUOTE_CHARACTER).withSeparator(CSVWriter.DEFAULT_SEPARATOR)
				.withOrderedResults(true).build();

		// write all users to csv file
		List<Grade> grades = grepo.findAll();
		writer.write(grades);
	}
	
	@RequestMapping("/csvfacultyleaves")
	public void exportfacultyleaves(HttpServletResponse response) throws Exception {

		// set file name and content type
		String filename = "facultyleaves.csv";

		response.setContentType("text/csv");
		response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"");

		// create a csv writer
		StatefulBeanToCsv<FacultyLeave> writer = new StatefulBeanToCsvBuilder<FacultyLeave>(response.getWriter())
				.withQuotechar(CSVWriter.NO_QUOTE_CHARACTER).withSeparator(CSVWriter.DEFAULT_SEPARATOR)
				.withOrderedResults(true).build();

		// write all users to csv file
		List<FacultyLeave> facultyLeaves = flrepo.findAll();
		writer.write(facultyLeaves);
	}
}
