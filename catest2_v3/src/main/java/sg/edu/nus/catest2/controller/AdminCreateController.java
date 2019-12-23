package sg.edu.nus.catest2.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;

import sg.edu.nus.catest2.model.Admin;
import sg.edu.nus.catest2.model.Course;
import sg.edu.nus.catest2.model.Department;
import sg.edu.nus.catest2.model.Faculty;
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
public class AdminCreateController {
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
	
	@RequestMapping("/admin/create")
	public String create(Model model, 
			@SessionAttribute("usersession") Session session,
			@RequestParam("createType") Optional<String> type) {
		admin = arepo.getByAdminId(session.getSessionId());
		model.addAttribute("admin", admin);
		String createType = type.orElse("null");
		if (createType.equals("null")) {
			return "admin/home";
		} else {
			if (createType.equals("department")) {
				int currentpage = 1;
				int pagesize = 5;
				List<Department> departments = drepo.findAll();
				model.addAttribute("departments", departments);
				Page<Department> departmentPage = dserv
						.findPaginatedDepartment(PageRequest.of(currentpage - 1, pagesize), departments);
				model.addAttribute("departmentPage", departmentPage);
				int totalPages = departmentPage.getTotalPages();
				if (totalPages > 0) {
					List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed()
							.collect(Collectors.toList());
					model.addAttribute("pageNumbers", pageNumbers);
				}
				return "createdepartment";

			} else if (createType.equals("course")) {
				int currentpage = 1;
				int pagesize = 5;
				List<Course> courses = crepo.findAll();
				model.addAttribute("courses", courses);
				List<Faculty> faculties = frepo.findAll();
				model.addAttribute("faculties", faculties);
				List<Department> departments = drepo.findAll();
				model.addAttribute("departments", departments);
				Page<Course> coursePage = cserv.findPaginatedCourse(PageRequest.of(currentpage - 1, pagesize), courses);
				model.addAttribute("coursePage", coursePage);
				int totalPages = coursePage.getTotalPages();
				if (totalPages > 0) {
					List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed()
							.collect(Collectors.toList());
					model.addAttribute("pageNumbers", pageNumbers);
				}
				return "createcourse";

			} else if (createType.equals("student")) {
				int currentpage = 1;
				int pagesize = 5;
				List<Student> students = srepo.findAll();
				model.addAttribute("students", students);
				Page<Student> studentPage = sserv.findPaginatedStudent(PageRequest.of(currentpage - 1, pagesize),
						students);
				model.addAttribute("studentPage", studentPage);
				int totalPages = studentPage.getTotalPages();
				if (totalPages > 0) {
					List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed()
							.collect(Collectors.toList());
					model.addAttribute("pageNumbers", pageNumbers);
				}
				return "createstudent";
			} else if (createType.equals("faculty")) {
				int currentpage = 1;
				int pagesize = 5;
				List<Faculty> faculties = frepo.findAll();
				model.addAttribute("faculties", faculties);
				List<Department> departments = drepo.findAll();
				model.addAttribute("departments", departments);
				Page<Faculty> facultyPage = fserv.findPaginatedFaculty(PageRequest.of(currentpage - 1, pagesize),
						faculties);
				model.addAttribute("facultyPage", facultyPage);
				int totalPages = facultyPage.getTotalPages();
				if (totalPages > 0) {
					List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed()
							.collect(Collectors.toList());
					model.addAttribute("pageNumbers", pageNumbers);
				}
				return "createfaculty";
			} else {
				int currentpage = 1;
				int pagesize = 5;
				List<Admin> admins = arepo.findAll();
				model.addAttribute("admins", admins);
				Page<Admin> adminPage = aserv.findPaginatedAdmin(PageRequest.of(currentpage - 1, pagesize), admins);
				model.addAttribute("adminPage", adminPage);
				int totalPages = adminPage.getTotalPages();
				if (totalPages > 0) {
					List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed()
							.collect(Collectors.toList());
					model.addAttribute("pageNumbers", pageNumbers);
				}
				return "createadmin";
			}
		}
	}

	@RequestMapping("admin/createdepartment")
	public String createDepartment(Model model,
			@SessionAttribute("usersession") Session session,
			@RequestParam("id") Optional<Integer> id,
			@RequestParam("name") Optional<String> name, @RequestParam(name = "page") Optional<Integer> page,
			@RequestParam(name = "size") Optional<Integer> size, @RequestParam(name = "flag") String flag) {
		admin = arepo.getByAdminId(session.getSessionId());
		model.addAttribute("admin", admin);
		int departmentId = id.orElse(0);
		String departmentName = name.orElse("null");
		int currentpage = page.orElse(1);
		int pagesize = size.orElse(5);

		if (departmentId <= 0 || departmentName.equals("null") || departmentName.isBlank()) {
			if (flag.equals("null")) {
				model.addAttribute("error", "null");
			} else {
				model.addAttribute("error", "error");
			}
			model.addAttribute("msg", "Department ID/Name is not valid");
			List<Department> departments = drepo.findAll();
			model.addAttribute("departments", departments);
			Page<Department> departmentPage = dserv.findPaginatedDepartment(PageRequest.of(currentpage - 1, pagesize),
					departments);
			model.addAttribute("departmentPage", departmentPage);
			int totalPages = departmentPage.getTotalPages();
			if (totalPages > 0) {
				List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
				model.addAttribute("pageNumbers", pageNumbers);
			}
			return "createdepartment";
		}

		else {
			if (drepo.getDepartmentByDepartmentId(departmentId) == null) {
				Department department = new Department();
				department.setDepartmentId(departmentId);
				department.setDepartmentName(departmentName);
				drepo.save(department);
				return "redirect:/admin/departmentlist";
			} else {
				model.addAttribute("error", "error");
				model.addAttribute("msg", "ID already exists!");
				List<Department> departments = drepo.findAll();
				model.addAttribute("departments", departments);
				Page<Department> departmentPage = dserv
						.findPaginatedDepartment(PageRequest.of(currentpage - 1, pagesize), departments);
				model.addAttribute("departmentPage", departmentPage);
				int totalPages = departmentPage.getTotalPages();
				if (totalPages > 0) {
					List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed()
							.collect(Collectors.toList());
					model.addAttribute("pageNumbers", pageNumbers);
				}
				return "createdepartment";
			}
		}
	}

	@RequestMapping("admin/createcourse")
	public String createCourse(Model model,
			@SessionAttribute("usersession") Session session,
			@RequestParam("code") Optional<String> code, @RequestParam("name") Optional<String> name,
			@RequestParam("csize") Optional<Integer> csize, @RequestParam("unit") Optional<Integer> unit,
			@RequestParam("start") Optional<String> start, @RequestParam("end") Optional<String> end,
			@RequestParam("did") Optional<Integer> deptid, @RequestParam("fid") Optional<Integer> facid,
			@RequestParam(name = "page") Optional<Integer> page, @RequestParam(name = "size") Optional<Integer> size,
			@RequestParam(name = "flag") String flag) {
		admin = arepo.getByAdminId(session.getSessionId());
		model.addAttribute("admin", admin);
		String courseCode = code.orElse("null");
		String courseName = name.orElse("null");
		int courseSize = csize.orElse(0);
		int courseUnit = unit.orElse(0);
		String courseStart = start.orElse("null");
		String courseEnd = end.orElse("null");
		int did = deptid.orElse(0);
		int fid = facid.orElse(0);
		int currentpage = page.orElse(1);
		int pagesize = size.orElse(5);
		List<Faculty> faculties = frepo.findAll();
		model.addAttribute("faculties", faculties);
		List<Department> departments = drepo.findAll();
		model.addAttribute("departments", departments);

		if (courseCode.equals("null") || courseCode.isBlank() || courseName.equals("null")
				|| courseName.isBlank() || courseSize <= 0 || courseUnit <= 0 || courseStart.equals("null")
				|| courseStart.isBlank() || courseEnd.equals("null") || courseEnd.isBlank() || did <= 0 || fid <= 0) {
			if (flag.equals("null")) {
				model.addAttribute("error", "null");
			} else {
				model.addAttribute("error", "error");
				model.addAttribute("msg", "Course attributes are not valid");
			}
			List<Course> courses = crepo.findAll();
			model.addAttribute("courses", courses);
			Page<Course> coursePage = cserv.findPaginatedCourse(PageRequest.of(currentpage - 1, pagesize), courses);
			model.addAttribute("coursePage", coursePage);
			int totalPages = coursePage.getTotalPages();
			if (totalPages > 0) {
				List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
				model.addAttribute("pageNumbers", pageNumbers);
			}
			return "createcourse";
		}

		else {
				Course course = new Course();
				course.setCourseCode(courseCode);
				course.setCourseName(courseName);
				course.setCourseSize(courseSize);
				course.setCourseUnit(courseUnit);
				LocalDate dstart = LocalDate.parse(courseStart);
				LocalDate dend = LocalDate.parse(courseEnd);
				if (dend.isBefore(dstart)) {
					model.addAttribute("error", "error");
					model.addAttribute("msg", "End Date cannot be earlier than Start Date!");
					List<Course> courses = crepo.findAll();
					model.addAttribute("courses", courses);
					Page<Course> coursePage = cserv.findPaginatedCourse(PageRequest.of(currentpage - 1, pagesize),
							courses);
					model.addAttribute("coursePage", coursePage);
					int totalPages = coursePage.getTotalPages();
					if (totalPages > 0) {
						List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed()
								.collect(Collectors.toList());
						model.addAttribute("pageNumbers", pageNumbers);
					}
					return "createcourse";
				}
				course.setCourseStart(dstart);
				course.setCourseEnd(dend);
				Department department = drepo.getDepartmentByDepartmentId(did);
				Faculty faculty = frepo.getByFacultyId(fid);
				if(department == null || faculty == null) {
					model.addAttribute("error", "error");
					model.addAttribute("msg", "Invalid Department/Faculty ID!");
					List<Course> courses = crepo.findAll();
					model.addAttribute("courses", courses);
					Page<Course> coursePage = cserv.findPaginatedCourse(PageRequest.of(currentpage - 1, pagesize), courses);
					model.addAttribute("coursePage", coursePage);
					int totalPages = coursePage.getTotalPages();
					if (totalPages > 0) {
						List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed()
								.collect(Collectors.toList());
						model.addAttribute("pageNumbers", pageNumbers);
					}
					return "createcourse";
				}
				course.setDepartment(department);
				course.setFaculty(faculty);
				crepo.save(course);
				return "redirect:/admin/courselist";
			
		}
	}

	@RequestMapping("admin/createstudent")
	public String createStudent(Model model,
			@SessionAttribute("usersession") Session session,
			@RequestParam("fname") Optional<String> fname, @RequestParam("mname") Optional<String> mname,
			@RequestParam("sname") Optional<String> sname, @RequestParam("mobile") Optional<Integer> mobile,
			@RequestParam("address") Optional<String> address, @RequestParam("email") Optional<String> email,
			@RequestParam("age") Optional<Integer> age, @RequestParam(name = "page") Optional<Integer> page,
			@RequestParam("gender") Optional<String> gender, @RequestParam(name = "size") Optional<Integer> size,
			@RequestParam(name = "flag") String flag) {
		admin = arepo.getByAdminId(session.getSessionId());
		model.addAttribute("admin", admin);

		String studentfname = fname.orElse("null");
		String studentmname = mname.orElse("null");
		String studentsname = sname.orElse("null");
		String studentaddr = address.orElse("null");
		String studentemail = email.orElse("null");
		String studentgender = gender.orElse("null");
		int studentage = age.orElse(0);
		int studentmobile = mobile.orElse(0);
		int currentpage = page.orElse(1);
		int pagesize = size.orElse(5);

		if (studentfname.equals("null") || studentfname.isBlank() || studentmname.equals("null")
				|| studentmname.isBlank() || studentmobile <= 0 || studentsname.equals("null") || studentsname.isBlank()
				|| studentaddr.equals("null") || studentaddr.isBlank() || studentemail.isBlank()
				|| studentemail.equals("null") || studentage <= 0) {
			if (flag.equals("null")) {
				model.addAttribute("error", "null");
			} else {
				model.addAttribute("error", "error");
			}
			model.addAttribute("msg", "Student attributes are not valid");
			List<Student> students = srepo.findAll();
			model.addAttribute("students", students);
			Page<Student> studentPage = sserv.findPaginatedStudent(PageRequest.of(currentpage - 1, pagesize), students);
			model.addAttribute("studentPage", studentPage);
			int totalPages = studentPage.getTotalPages();
			if (totalPages > 0) {
				List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
				model.addAttribute("pageNumbers", pageNumbers);
			}
			return "createstudent";
		}

		else {
				Student student = new Student();
				student.setFirstName(studentfname);
				student.setMiddleName(studentmname);
				student.setSurname(studentsname);
				student.setAge(studentage);
				student.setGender(studentgender);
				student.setAddress(studentaddr);
				student.setEmail(studentemail);
				student.setMobileNum(studentmobile);
				srepo.save(student);
				model.addAttribute("student", student);
				model.addAttribute("type", "student");
				return "createuser";
			
		}
	}

	@RequestMapping("admin/createfaculty")
	public String createFaculty(Model model,
			@SessionAttribute("usersession") Session session,
			@RequestParam("fname") Optional<String> fname, @RequestParam("mname") Optional<String> mname,
			@RequestParam("sname") Optional<String> sname, @RequestParam("mobile") Optional<Integer> mobile,
			@RequestParam("email") Optional<String> email, @RequestParam("did") Optional<Integer> did,
			@RequestParam(name = "page") Optional<Integer> page, @RequestParam(name = "size") Optional<Integer> size,
			@RequestParam(name = "flag") String flag) {
		admin = arepo.getByAdminId(session.getSessionId());
		model.addAttribute("admin", admin);

		String facultyfname = fname.orElse("null");
		String facultymname = mname.orElse("null");
		String facultysname = sname.orElse("null");
		String facultyemail = email.orElse("null");
		int facultymobile = mobile.orElse(0);
		int deptid = did.orElse(0);
		int currentpage = page.orElse(1);
		int pagesize = size.orElse(5);
		List<Department> departments = drepo.findAll();
		model.addAttribute("departments", departments);

		if (facultyfname.equals("null") || facultyfname.isBlank() || facultymname.equals("null")
				|| facultymname.isBlank() || facultymobile <= 0 || facultysname.equals("null") || facultysname.isBlank()
				|| facultyemail.isBlank() || facultyemail.equals("null") || deptid <= 0) {
			if (flag.equals("null")) {
				model.addAttribute("error", "null");
			} else {
				model.addAttribute("error", "error");
			}
			model.addAttribute("msg", "Faculty attributes are not valid");
			List<Faculty> faculties = frepo.findAll();
			model.addAttribute("faculties", faculties);
			Page<Faculty> facultyPage = fserv.findPaginatedFaculty(PageRequest.of(currentpage - 1, pagesize),
					faculties);
			model.addAttribute("facultyPage", facultyPage);
			int totalPages = facultyPage.getTotalPages();
			if (totalPages > 0) {
				List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
				model.addAttribute("pageNumbers", pageNumbers);
			}
			return "createfaculty";
		}

		else {
				Faculty faculty = new Faculty();
				faculty.setFirstName(facultyfname);
				faculty.setMiddleName(facultymname);
				faculty.setSurname(facultysname);
				faculty.setEmail(facultyemail);
				faculty.setMobileNum(facultymobile);
				Department department = drepo.getDepartmentByDepartmentId(deptid);
				if(department == null) {
					model.addAttribute("error", "error");
					model.addAttribute("msg", "Department does not exist!");
					List<Faculty> faculties = frepo.findAll();
					model.addAttribute("faculties", faculties);
					Page<Faculty> facultyPage = fserv.findPaginatedFaculty(PageRequest.of(currentpage - 1, pagesize),
							faculties);
					model.addAttribute("facultyPage", facultyPage);
					int totalPages = facultyPage.getTotalPages();
					if (totalPages > 0) {
						List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
						model.addAttribute("pageNumbers", pageNumbers);
					}
					return "createfaculty";
				}
				faculty.setDepartment(department);
				frepo.save(faculty);
				model.addAttribute("faculty", faculty);
				model.addAttribute("type", "faculty");
				return "createuser";
			
		}
	}

	@RequestMapping("admin/createadmin")
	public String createAdmin(Model model,
			@SessionAttribute("usersession") Session session,
			@RequestParam("fname") Optional<String> fname, @RequestParam("mname") Optional<String> mname,
			@RequestParam("sname") Optional<String> sname, @RequestParam("mobile") Optional<Integer> mobile,
			@RequestParam("email") Optional<String> email, @RequestParam(name = "page") Optional<Integer> page,
			@RequestParam(name = "size") Optional<Integer> size, @RequestParam(name = "flag") String flag) {
		admin = arepo.getByAdminId(session.getSessionId());
		model.addAttribute("admin", admin);

		String adminfname = fname.orElse("null");
		String adminmname = mname.orElse("null");
		String adminsname = sname.orElse("null");
		String adminemail = email.orElse("null");
		int adminmobile = mobile.orElse(0);
		int currentpage = page.orElse(1);
		int pagesize = size.orElse(5);

		if (adminfname.equals("null") || adminfname.isBlank() || adminmname.equals("null")
				|| adminmname.isBlank() || adminmobile <= 0 || adminsname.equals("null") || adminsname.isBlank()
				|| adminemail.isBlank() || adminemail.equals("null")) {
			if (flag.equals("null")) {
				model.addAttribute("error", "null");
			} else {
				model.addAttribute("error", "error");
			}
			model.addAttribute("msg", "Admin attributes are not valid");
			List<Admin> admins = arepo.findAll();
			model.addAttribute("admins", admins);
			Page<Admin> adminPage = aserv.findPaginatedAdmin(PageRequest.of(currentpage - 1, pagesize), admins);
			model.addAttribute("adminPage", adminPage);
			int totalPages = adminPage.getTotalPages();
			if (totalPages > 0) {
				List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
				model.addAttribute("pageNumbers", pageNumbers);
			}
			return "createadmin";
		}

		else {
				Admin admin = new Admin();
				admin.setFirstName(adminfname);
				admin.setMiddleName(adminmname);
				admin.setSurname(adminsname);
				admin.setMobileNum(adminmobile);
				admin.setEmail(adminemail);
				arepo.save(admin);
				model.addAttribute("admind", admin);
				model.addAttribute("type", "admin");
				return "createuser";
			
		}
	}

	@RequestMapping("admin/createuser")
	public String createUser(Model model, @RequestParam("type") Optional<String> ftype,
			@SessionAttribute("usersession") Session session,
			@RequestParam("id") Optional<Integer> fid, @RequestParam("username") Optional<String> fusername,
			@RequestParam("password") Optional<String> fpassword,
			@RequestParam("cfmpassword") Optional<String> cpassword) {
		admin = arepo.getByAdminId(session.getSessionId());
		model.addAttribute("admin", admin);
		String type = ftype.orElse("null");
		String username = fusername.orElse("null");
		String password = fpassword.orElse("null");
		String cfmpassword = cpassword.orElse("null");
		int id = fid.orElse(0);

		if (type.equals("student")) {
			if (!password.equals(cfmpassword)) {
				Student student = srepo.getStudentByStudentId(id);
				model.addAttribute("student", student);
				model.addAttribute("error", "yes");
				model.addAttribute("emsg", "Password does not match!");
				model.addAttribute("type", "student");
				return "createuser";
			}
			if (urepo.findByUserName(username) == null) {
				User user = new User(id, 0, 0, username, password);
				urepo.save(user);
				return "redirect:/admin/userlist";
			} else {
				Student student = srepo.getStudentByStudentId(id);
				model.addAttribute("student", student);
				model.addAttribute("error", "yes");
				model.addAttribute("emsg", "Username already exists!");
				model.addAttribute("type", "student");
				return "createuser";
			}
		} else if (type.equals("faculty")) {
			if (!password.equals(cfmpassword)) {
				Faculty faculty = frepo.getByFacultyId(id);
				model.addAttribute("faculty", faculty);
				model.addAttribute("error", "yes");
				model.addAttribute("emsg", "Password does not match!");
				model.addAttribute("type", "faculty");
				return "createuser";
			}
			if (urepo.findByUserName(username) == null) {
				User user = new User(0, id, 0, username, password);
				urepo.save(user);
				return "redirect:/admin/userlist";
			} else {
				Faculty faculty = frepo.getByFacultyId(id);
				model.addAttribute("faculty", faculty);
				model.addAttribute("error", "yes");
				model.addAttribute("emsg", "Username already exists!");
				model.addAttribute("type", "faculty");
				return "createuser";
			}
		} else {
			if (!password.equals(cfmpassword)) {
				Admin admin = arepo.getByAdminId(id);
				model.addAttribute("admind", admin);
				model.addAttribute("error", "yes");
				model.addAttribute("emsg", "Password does not match!");
				model.addAttribute("type", "admin");
				return "createuser";
			}
			if (urepo.findByUserName(username) == null) {
				User user = new User(0, 0, id, username, password);
				urepo.save(user);
				return "redirect:/admin/userlist";
			} else {
				Admin admin = arepo.getByAdminId(id);
				model.addAttribute("admind", admin);
				model.addAttribute("error", "yes");
				model.addAttribute("emsg", "Username already exists!");
				model.addAttribute("type", "admin");
				return "createuser";
			}
		}
	}

}
