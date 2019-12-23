package sg.edu.nus.catest2.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;

import sg.edu.nus.catest2.model.Admin;
import sg.edu.nus.catest2.model.Course;
import sg.edu.nus.catest2.model.CourseApplication;
import sg.edu.nus.catest2.model.Department;
import sg.edu.nus.catest2.model.Faculty;
import sg.edu.nus.catest2.model.FacultyLeave;
import sg.edu.nus.catest2.model.Grade;
import sg.edu.nus.catest2.model.Student;
import sg.edu.nus.catest2.model.User;
import sg.edu.nus.catest2.mvcmodel.Session;
import sg.edu.nus.catest2.repo.AdminRepository;
import sg.edu.nus.catest2.repo.CourseApplicationRepository;
import sg.edu.nus.catest2.repo.CourseRepository;
import sg.edu.nus.catest2.repo.DepartmentRepository;
import sg.edu.nus.catest2.repo.FacultyLeaveRepository;
import sg.edu.nus.catest2.repo.FacultyRepository;
import sg.edu.nus.catest2.repo.GradeRepository;
import sg.edu.nus.catest2.repo.StudentRepository;
import sg.edu.nus.catest2.repo.UserRepository;
import sg.edu.nus.catest2.service.AdminService;
import sg.edu.nus.catest2.service.CourseApplicationService;
import sg.edu.nus.catest2.service.CourseService;
import sg.edu.nus.catest2.service.DepartmentService;
import sg.edu.nus.catest2.service.FacultyService;
import sg.edu.nus.catest2.service.GradeService;
import sg.edu.nus.catest2.service.StudentService;
import sg.edu.nus.catest2.service.UserService;

@Controller
public class AdminUpdateController {
	@Autowired
	AdminRepository arepo;
	@Autowired
	CourseApplicationRepository carepo;
	@Autowired
	DepartmentRepository drepo;
	@Autowired
	CourseRepository crepo;
	@Autowired
	StudentRepository srepo;
	@Autowired
	FacultyRepository frepo;
	@Autowired
	GradeRepository grepo;
	@Autowired
	FacultyLeaveRepository flrepo;
	@Autowired
	UserRepository urepo;
	@Autowired
	CourseApplicationService caserv;
	@Autowired
	DepartmentService dserv;
	@Autowired
	StudentService sserv;
	@Autowired
	FacultyService fserv;
	@Autowired
	CourseService cserv;
	@Autowired
	GradeService gserv;
	@Autowired
	UserService userv;
	@Autowired
	AdminService aserv;
	
	
	private static Admin admin = new Admin();
	

	@RequestMapping("/admin/updatedepartment")
	public String updatedepartment(Model model, 
			@SessionAttribute("usersession") Session session,
			@RequestParam(name = "id") Optional<Integer> objectid,
			@RequestParam(name = "confirm") Optional<String> cfm,
			@RequestParam(name = "updatename") Optional<String> uname) {
		admin = arepo.getByAdminId(session.getSessionId());
		model.addAttribute("admin", admin);
		String confirm = cfm.orElse("no");
		int id = objectid.orElse(0);

		if (id == 0 || confirm.equals("cancel")) {
			return "redirect:/admin/home";
		}
		// for updating department
		Department department = drepo.getDepartmentByDepartmentId(id);
		if (confirm.equals("no")) {
			model.addAttribute("department", department);
			return "confirmupdatedepartment";
		} else {
			String updatename = uname.orElse("null");
			department.setDepartmentName(updatename);
			drepo.save(department);
			model.addAttribute("department", department);
			model.addAttribute("msg", "found");
			List<Course> courses = crepo.getCoursesByDepartmentId(department.getDepartmentId());
			model.addAttribute("courses", courses);
			List<Faculty> faculties = frepo.getFacultiesByDepartmentId(department.getDepartmentId());
			model.addAttribute("faculties", faculties);

			return "viewdepartment";
		}
	}

	@RequestMapping("/admin/updatecourse")
	public String updatecourse(Model model, @RequestParam(name = "id") Optional<Integer> objectid,
			@SessionAttribute("usersession") Session session,
			@RequestParam(name = "confirm") Optional<String> cfm,
			@RequestParam(name = "updatecode") Optional<String> ucode,
			@RequestParam(name = "updatesize") Optional<Integer> usize,
			@RequestParam(name = "updateunit") Optional<Integer> uunit,
			@RequestParam(name = "updatestart") Optional<String> ustart,
			@RequestParam(name = "updateend") Optional<String> uend,
			@RequestParam(name = "updatedid") Optional<Integer> udid,
			@RequestParam(name = "updatefid") Optional<Integer> ufid,
			@RequestParam(name = "updatename") Optional<String> uname) {
		admin = arepo.getByAdminId(session.getSessionId());
		model.addAttribute("admin", admin);
		String confirm = cfm.orElse("no");
		int id = objectid.orElse(0);
		List<Faculty> faculties = frepo.findAll();
		model.addAttribute("faculties", faculties);
		List<Department> departments = drepo.findAll();
		model.addAttribute("departments", departments);

		if (id == 0 || confirm.equals("cancel")) {
			return "redirect:/admin/home";
		}
		// for updating course
		Course course = crepo.getCourseByCourseId(id);
		if (confirm.equals("no")) {
			model.addAttribute("course", course);
			return "confirmupdatecourse";
		} else {
			String updatecode = ucode.orElse("null");
			String updatestart = ustart.orElse("null");
			String updateend = uend.orElse("null");
			int updatesize = usize.orElse(0);
			int updateunit = uunit.orElse(0);
			int updatedid = udid.orElse(0);
			int updatefid = ufid.orElse(0);
			String updatename = uname.orElse("null");

			if (updatesize <= 0 || updateunit <= 0 || updatedid <= 0 || updatefid <= 0 || updatename.equals("null")) {
				model.addAttribute("course", course);
				model.addAttribute("error", "yes");
				model.addAttribute("emsg", "Numbers cannot be negative!");
				return "confirmupdatecourse";
			}
			LocalDate dstart = LocalDate.parse(updatestart);
			LocalDate dend = LocalDate.parse(updateend);
			if (dend.isBefore(dstart)) {
				model.addAttribute("course", course);
				model.addAttribute("error", "yes");
				model.addAttribute("emsg", "Course end date cannot be before course start date!");
				return "confirmupdatecourse";
			}
			course.setCourseStart(dstart);
			course.setCourseEnd(dend);
			course.setCourseCode(updatecode);
			course.setCourseSize(updatesize);
			course.setCourseUnit(updateunit);
			course.setCourseName(updatename);
			Department department = drepo.getDepartmentByDepartmentId(updatedid);
			Faculty faculty = frepo.getByFacultyId(updatefid);
			if(department == null || faculty == null) {
				model.addAttribute("course", course);
				model.addAttribute("error", "yes");
				model.addAttribute("emsg", "Department/Faculty deos not exist!");
				return "confirmupdatecourse";
			}
			course.setDepartment(department);
			course.setFaculty(faculty);
			crepo.save(course);
			model.addAttribute("course", course);
			model.addAttribute("msg", "found");
			List<CourseApplication> courseApplications = carepo.getCourseApplicationsByCourseId(course.getCourseId());
			if (courseApplications.isEmpty()) {
				model.addAttribute("searchca", "null");
			} else {
				model.addAttribute("searchca", "found");
			}
			model.addAttribute("courseApplications", courseApplications);
			return "viewcourse";
		}

	}
	
	@RequestMapping("/admin/updatefacultyleave")
	public String updatefacultyleave(Model model, @RequestParam(name = "id") Optional<Integer> objectid,
			@SessionAttribute("usersession") Session session,
			@RequestParam(name = "confirm") Optional<String> cfm,
			@RequestParam(name = "updatestatus") Optional<String> ustatus,
			@RequestParam(name = "updatestart") Optional<String> ustart,
			@RequestParam(name = "updateend") Optional<String> uend,
			@RequestParam(name = "updatefid") Optional<Integer> ufid) {
		admin = arepo.getByAdminId(session.getSessionId());
		model.addAttribute("admin", admin);
		String confirm = cfm.orElse("no");
		int id = objectid.orElse(0);

		if (id == 0 || confirm.equals("cancel")) {
			return "redirect:/admin/home";
		}
		// for updating faculty leave
		FacultyLeave facultyLeave = flrepo.getFacultyLeaveByLeaveId(id);
		if (confirm.equals("no")) {
			model.addAttribute("facultyLeave", facultyLeave);
			return "confirmupdatefacultyleave";
		} else {
			String updatestatus = ustatus.orElse("null");
			String updatestart = ustart.orElse("null");
			String updateend = uend.orElse("null");
			int updatefid = ufid.orElse(0);

			if (updatefid <= 0) {
				model.addAttribute("facultyLeave", facultyLeave);
				model.addAttribute("error", "yes");
				model.addAttribute("emsg", "Faculty ID cannot be negative!");
				return "confirmupdatefacultyleave";
			}
			if (updatestatus.equals("Approved") || updatestatus.equals("Pending") || updatestatus.equals("Rejected")) {
				LocalDate dstart = LocalDate.parse(updatestart);
				LocalDate dend = LocalDate.parse(updateend);
				if (dend.isBefore(dstart)) {
					model.addAttribute("facultyLeave", facultyLeave);
					model.addAttribute("error", "yes");
					model.addAttribute("emsg", "Leave end date cannot be before leave start date!");
					return "confirmupdatefacultyleave";
				}
				facultyLeave.setLeaveStart(dstart.plusDays(1));
				facultyLeave.setLeaveEnd(dend.plusDays(1));
				facultyLeave.setStatus(updatestatus);
				Faculty faculty = frepo.getByFacultyId(updatefid);
				if(faculty == null) {
					model.addAttribute("facultyLeave", facultyLeave);
					model.addAttribute("error", "yes");
					model.addAttribute("emsg", "Faculty does not exist!");
					return "confirmupdatefacultyleave";
				}
				facultyLeave.setFaculty(faculty);
				flrepo.save(facultyLeave);
				model.addAttribute("facultyLeave", facultyLeave);
				model.addAttribute("msg", "found");
				return "viewfacultyleave";
			}
			else {
				model.addAttribute("facultyLeave", facultyLeave);
				model.addAttribute("error", "yes");
				model.addAttribute("emsg", "Status can only be: 'Approved'/'Pending'/'Rejected'");
				return "confirmupdatefacultyleave";
			}
		}

	}

	@RequestMapping("/admin/updatestudent")
	public String updatestudent(Model model, @RequestParam(name = "id") Optional<Integer> objectid,
			@SessionAttribute("usersession") Session session,
			@RequestParam(name = "confirm") Optional<String> cfm,
			@RequestParam(name = "updatefname") Optional<String> ufname,
			@RequestParam(name = "updatemname") Optional<String> umname,
			@RequestParam(name = "updatesname") Optional<String> usname,
			@RequestParam(name = "updategender") Optional<String> ugender,
			@RequestParam(name = "updateaddress") Optional<String> uaddress,
			@RequestParam(name = "updateage") Optional<Integer> uage,
			@RequestParam(name = "updateemail") Optional<String> uemail,
			@RequestParam(name = "updatemobile") Optional<Integer> umobile) {
		admin = arepo.getByAdminId(session.getSessionId());
		model.addAttribute("admin", admin);
		String confirm = cfm.orElse("no");
		int id = objectid.orElse(0);

		if (id == 0 || confirm.equals("cancel")) {
			return "redirect:/admin/home";
		}
		// for updating student
		Student student = srepo.getStudentByStudentId(id);
		if (confirm.equals("no")) {
			model.addAttribute("student", student);
			return "confirmupdatestudent";
		} else {
			String updatefname = ufname.orElse("null");
			String updatemname = umname.orElse("null");
			String updatesname = usname.orElse("null");
			String updategender = ugender.orElse("null");
			String updateaddress = uaddress.orElse("null");
			String updateemail = uemail.orElse("null");
			int updateage = uage.orElse(0);
			int updatemobile = umobile.orElse(0);

			if (updateage <= 0 || updatemobile <= 0 || updatefname.equals("null") || updatemname.equals("null")
					|| updatesname.equals("null") || updategender.equals("null") || updateaddress.equals("null")
					|| updateemail.equals("null")) {
				model.addAttribute("student", student);
				model.addAttribute("error", "yes");
				model.addAttribute("emsg", "Age/Mobile is not valid!");
				return "confirmupdatestudent";
			}
			student.setFirstName(updatefname);
			student.setMiddleName(updatemname);
			student.setSurname(updatesname);
			student.setGender(updategender);
			student.setAge(updateage);
			student.setAddress(updateaddress);
			student.setEmail(updateemail);
			srepo.save(student);
			model.addAttribute("student", student);
			model.addAttribute("msg", "found");
			List<CourseApplication> courseApplications = carepo
					.getCourseApplicationsByStudentId(student.getStudentId());
			if (courseApplications.isEmpty()) {
				model.addAttribute("searchca", "null");
			} else {
				model.addAttribute("searchca", "found");
			}
			model.addAttribute("courseApplications", courseApplications);
			return "viewstudent";
		}

	}

	@RequestMapping("/admin/updatefaculty")
	public String updatefaculty(Model model, @RequestParam(name = "id") Optional<Integer> objectid,
			@SessionAttribute("usersession") Session session,
			@RequestParam(name = "confirm") Optional<String> cfm,
			@RequestParam(name = "updatefname") Optional<String> ufname,
			@RequestParam(name = "updatemname") Optional<String> umname,
			@RequestParam(name = "updatesname") Optional<String> usname,
			@RequestParam(name = "updatedid") Optional<Integer> udid,
			@RequestParam(name = "updateemail") Optional<String> uemail,
			@RequestParam(name = "updatemobile") Optional<Integer> umobile) {
		admin = arepo.getByAdminId(session.getSessionId());
		model.addAttribute("admin", admin);
		String confirm = cfm.orElse("no");
		int id = objectid.orElse(0);
		List<Department> departments = drepo.findAll();
		model.addAttribute("departments", departments);

		if (id == 0 || confirm.equals("cancel")) {
			return "redirect:/admin/home";
		}
		// for updating faculty
		Faculty faculty = frepo.getByFacultyId(id);
		if (confirm.equals("no")) {
			model.addAttribute("faculty", faculty);
			return "confirmupdatefaculty";
		} else {
			String updatefname = ufname.orElse("null");
			String updatemname = umname.orElse("null");
			String updatesname = usname.orElse("null");
			String updateemail = uemail.orElse("null");
			int updatemobile = umobile.orElse(0);
			int updatedid = udid.orElse(0);

			if (updatedid <= 0 || updatemobile <= 0 || updatefname.equals("null") || updatemname.equals("null")
					|| updatesname.equals("null") || updateemail.equals("null")) {
				model.addAttribute("faculty", faculty);
				model.addAttribute("error", "yes");
				model.addAttribute("emsg", "Department ID/Mobile Number is not valid!");
				return "confirmupdatefaculty";
			}

			faculty.setFirstName(updatefname);
			faculty.setMiddleName(updatemname);
			faculty.setSurname(updatesname);
			faculty.setEmail(updateemail);
			faculty.setMobileNum(updatemobile);
			Department department = drepo.getDepartmentByDepartmentId(updatedid);
			if(department == null) {
				model.addAttribute("faculty", faculty);
				model.addAttribute("error", "yes");
				model.addAttribute("emsg", "Department does not exist!");
				return "confirmupdatefaculty";
			}
			faculty.setDepartment(department);
			frepo.save(faculty);
			model.addAttribute("faculty", faculty);
			model.addAttribute("msg", "found");
			List<Course> courses = crepo.getCoursesByFacultyId(faculty.getFacultyId());
			if (courses.isEmpty()) {
				model.addAttribute("searchcourse", "null");
			} else {
				model.addAttribute("searchcourse", "found");
			}
			model.addAttribute("courses", courses);
			return "viewfaculty";
		}

	}
	
	@RequestMapping("/admin/updateuser")
	public String updateuser(Model model, @RequestParam(name = "id") Optional<Integer> objectid,
			@SessionAttribute("usersession") Session session,
			@RequestParam(name = "confirm") Optional<String> cfm,
			@RequestParam(name = "updateuname") Optional<String> uname,
			@RequestParam(name = "updatepass") Optional<String> upass) {
		admin = arepo.getByAdminId(session.getSessionId());
		model.addAttribute("admin", admin);
		String confirm = cfm.orElse("no");
		int id = objectid.orElse(0);

		if (id == 0 || confirm.equals("cancel")) {
			return "redirect:/admin/home";
		}
		// for updating user
		User user = urepo.getUserByUserId(id);
		if (confirm.equals("no")) {
			model.addAttribute("user", user);
			if(user.getAdminId() == 0 && user.getFacultyId() == 0) {
				Student student = srepo.getStudentByStudentId(user.getStudentId());
				model.addAttribute("student", student);
				model.addAttribute("type", "student");
			}
			else if(user.getAdminId() == 0 && user.getStudentId() == 0) {
				Faculty faculty = frepo.getByFacultyId(user.getFacultyId());
				model.addAttribute("faculty", faculty);
				model.addAttribute("type", "faculty");
			}
			else {
				Admin admind = arepo.getByAdminId(user.getAdminId());
				model.addAttribute("admind", admind);
				model.addAttribute("type", "admin");
			}
			return "confirmupdateuser";
		} else {
			String updateuname = uname.orElse("null");
			String updatepass = upass.orElse("null");

			if (updateuname.equals("null") || updateuname.isBlank()||updateuname.isEmpty()
					|| updatepass.equals("null") || updatepass.isBlank()||updateuname.isEmpty()) {
				model.addAttribute("user", user);
				model.addAttribute("error", "yes");
				model.addAttribute("emsg", "Username/Password is not valid!");
				if(user.getAdminId() == 0 && user.getFacultyId() == 0) {
					Student student = srepo.getStudentByStudentId(user.getStudentId());
					model.addAttribute("student", student);
					model.addAttribute("type", "student");
				}
				else if(user.getAdminId() == 0 && user.getStudentId() == 0) {
					Faculty faculty = frepo.getByFacultyId(user.getFacultyId());
					model.addAttribute("faculty", faculty);
					model.addAttribute("type", "faculty");
				}
				else {
					Admin admind = arepo.getByAdminId(user.getAdminId());
					model.addAttribute("admind", admind);
					model.addAttribute("type", "admin");
				}
				return "confirmupdateuser";
			}
			User temp = urepo.findByUserName(updateuname);
			if(temp != null) {
				model.addAttribute("user", user);
				model.addAttribute("error", "yes");
				model.addAttribute("emsg", "Username already exists!");
				if(user.getAdminId() == 0 && user.getFacultyId() == 0) {
					Student student = srepo.getStudentByStudentId(user.getStudentId());
					model.addAttribute("student", student);
					model.addAttribute("type", "student");
				}
				else if(user.getAdminId() == 0 && user.getStudentId() == 0) {
					Faculty faculty = frepo.getByFacultyId(user.getFacultyId());
					model.addAttribute("faculty", faculty);
					model.addAttribute("type", "faculty");
				}
				else {
					Admin admind = arepo.getByAdminId(user.getAdminId());
					model.addAttribute("admind", admind);
					model.addAttribute("type", "admin");
				}
				return "confirmupdateuser";
			}
			user.setUserName(updateuname);
			user.setPassword(updatepass);
			urepo.save(user);
			model.addAttribute("user", user);
			model.addAttribute("msg", "found");
			if(user.getAdminId() == 0 && user.getFacultyId() == 0) {
				Student student = srepo.getStudentByStudentId(user.getStudentId());
				model.addAttribute("student", student);
				model.addAttribute("type", "student");
			}
			else if(user.getAdminId() == 0 && user.getStudentId() == 0) {
				Faculty faculty = frepo.getByFacultyId(user.getFacultyId());
				model.addAttribute("faculty", faculty);
				model.addAttribute("type", "faculty");
			}
			else {
				Admin admind = arepo.getByAdminId(user.getAdminId());
				model.addAttribute("admind", admind);
				model.addAttribute("type", "admin");
			}
			return "viewuser";
		}

	}

	@RequestMapping("/admin/updateadmin")
	public String updateadmin(Model model, @RequestParam(name = "id") Optional<Integer> objectid,
			@SessionAttribute("usersession") Session session,
			@RequestParam(name = "confirm") Optional<String> cfm,
			@RequestParam(name = "updatefname") Optional<String> ufname,
			@RequestParam(name = "updatemname") Optional<String> umname,
			@RequestParam(name = "updatesname") Optional<String> usname,
			@RequestParam(name = "updateemail") Optional<String> uemail,
			@RequestParam(name = "updatemobile") Optional<Integer> umobile) {
		admin = arepo.getByAdminId(session.getSessionId());
		model.addAttribute("admin", admin);
		String confirm = cfm.orElse("no");
		int id = objectid.orElse(0);

		if (id == 0 || confirm.equals("cancel")) {
			return "redirect:/admin/home";
		}
		// for updating admin
		Admin admin = arepo.getByAdminId(id);
		if (confirm.equals("no")) {
			model.addAttribute("admind", admin);
			return "confirmupdateadmin";
		} else {
			String updatefname = ufname.orElse("null");
			String updatemname = umname.orElse("null");
			String updatesname = usname.orElse("null");
			String updateemail = uemail.orElse("null");
			int updatemobile = umobile.orElse(0);

			if (updatemobile <= 0 || updatefname.equals("null") || updatemname.equals("null")
					|| updatesname.equals("null") || updateemail.equals("null")) {
				model.addAttribute("admind", admin);
				model.addAttribute("error", "yes");
				model.addAttribute("emsg", "Mobile Number is not valid!");
				return "confirmupdateadmin";
			}

			admin.setFirstName(updatefname);
			admin.setMiddleName(updatemname);
			admin.setSurname(updatesname);
			admin.setEmail(updateemail);
			admin.setMobileNum(updatemobile);
			arepo.save(admin);
			model.addAttribute("admind", admin);
			model.addAttribute("msg", "found");
			return "viewadmin";
		}

	}

	@RequestMapping("/admin/updatecourseapplication")
	public String updatecourseapplication(Model model, @RequestParam(name = "id") Optional<Integer> objectid,
			@SessionAttribute("usersession") Session session,
			@RequestParam(name = "confirm") Optional<String> cfm,
			@RequestParam(name = "updatestatus") Optional<String> ustatus,
			@RequestParam(name = "updatecid") Optional<Integer> ucid,
			@RequestParam(name = "updatesid") Optional<Integer> usid) {
		admin = arepo.getByAdminId(session.getSessionId());
		model.addAttribute("admin", admin);
		String confirm = cfm.orElse("no");
		int id = objectid.orElse(0);
		List<Student> students = srepo.findAll();
		model.addAttribute("students", students);
		List<Course> courses = crepo.findAll();
		model.addAttribute("courses", courses);

		if (id == 0 || confirm.equals("cancel")) {
			return "redirect:/admin/home";
		}
		// for updating course application
		CourseApplication courseApplication = carepo.getCourseApplicationByApplicationId(id);
		if (confirm.equals("no")) {
			model.addAttribute("courseApplication", courseApplication);
			return "confirmupdatecourseapplication";
		} else {
			String updatestatus = ustatus.orElse("null");
			int updatecid = ucid.orElse(0);
			int updatesid = usid.orElse(0);
			if (updatecid <= 0 || updatesid <= 0 || updatestatus.equals("null")) {
				model.addAttribute("courseApplication", courseApplication);
				model.addAttribute("error", "yes");
				model.addAttribute("emsg", "Student/Course ID is not valid!");
				return "confirmupdatecourseapplication";
			}
			if (updatestatus.equals("Approved") || updatestatus.equals("Pending") || updatestatus.equals("Rejected")) {
				courseApplication.setStatus(updatestatus);
				Course course = crepo.getCourseByCourseId(updatecid);
				Student student = srepo.getStudentByStudentId(updatesid);
				if(student == null || course == null) {
					model.addAttribute("courseApplication", courseApplication);
					model.addAttribute("error", "yes");
					model.addAttribute("emsg", "Student/Course does not exist!");
					return "confirmupdatecourseapplication";
				}
				courseApplication.setCourse(course);
				courseApplication.setStudent(student);
				carepo.save(courseApplication);
				model.addAttribute("courseApplication", courseApplication);
				model.addAttribute("msg", "found");
				return "viewcourseapplication";
			}
			else {
				model.addAttribute("courseApplication", courseApplication);
				model.addAttribute("error", "yes");
				model.addAttribute("emsg", "Status can only be: 'Approved'/'Pending'/'Rejected'");
				return "confirmupdatecourseapplication";
			}
		}

	}

	@RequestMapping("admin/updategrade")
	public String updategrade(Model model, @RequestParam(name = "id") Optional<Integer> objectid,
			@SessionAttribute("usersession") Session session,
			@RequestParam(name = "confirm") Optional<String> cfm,
			@RequestParam(name = "updategrade") Optional<String> ugrade) {
		admin = arepo.getByAdminId(session.getSessionId());
		model.addAttribute("admin", admin);
		String confirm = cfm.orElse("no");
		int id = objectid.orElse(0);
		if (id == 0 || confirm.equals("cancel")) {
			return "redirect:/admin/home";
		}
		// for updating grade
		Grade grade = grepo.getGradeByGradeId(id);
		if (confirm.equals("no")) {
			model.addAttribute("grade", grade);
			return "confirmupdategrade";
		} else {
			String updategrade = ugrade.orElse("null");
			if (updategrade.equals("null")) {
				model.addAttribute("grade", grade);
				model.addAttribute("error", "yes");
				model.addAttribute("emsg", "Grade must be filled up!");
				return "confirmupdategrade";
			}
			grade.setGrade(updategrade);
			grepo.save(grade);
			model.addAttribute("grade", grade);
			model.addAttribute("msg", "found");
			return "viewgrade";
		}

	}

}
