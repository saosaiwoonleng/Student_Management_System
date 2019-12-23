package sg.edu.nus.catest2.controller;

import java.util.*;
import java.time.LocalDate;
import java.time.format.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;

import sg.edu.nus.catest2.repo.*;
import sg.edu.nus.catest2.model.*;
import sg.edu.nus.catest2.mvcmodel.*;
import sg.edu.nus.catest2.service.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

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
	@Autowired
	FacultyLeaveService flserv;

	private static Admin admin = new Admin();

	@RequestMapping("/home")
	public String home(Model model, @SessionAttribute("usersession") Session session,
			@RequestParam(name = "page") Optional<Integer> page, @RequestParam(name = "size") Optional<Integer> size) {

		if (session.getSessionId() == 0) {
			return "redirect:/login/welcome";
		}

		int currentpage = page.orElse(1);
		int pagesize = size.orElse(5);

		admin = arepo.getByAdminId(session.getSessionId());
		model.addAttribute("admin", admin);
		List<CourseApplication> courseApplications = caserv.getPendingCourseApplications();
		if (courseApplications.isEmpty()) {
			String msg = "null";
			model.addAttribute("msg", msg);
		} else {
			model.addAttribute("courseApplications", courseApplications);
			String msg = "found";
			model.addAttribute("msg", msg);

			Page<CourseApplication> courseApplicationPage = caserv
					.findPaginatedCourseApplication(PageRequest.of(currentpage - 1, pagesize), courseApplications);
			model.addAttribute("courseApplicationPage", courseApplicationPage);

			int totalPages = courseApplicationPage.getTotalPages();
			if (totalPages > 0) {
				List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
				model.addAttribute("pageNumbers", pageNumbers);
			}
		}

		return "adminhome";
	}

	@RequestMapping("/approvereject")
	public String approvereject(Model model, 
			@RequestParam("type") String type, 
			@RequestParam("approval") String confirm, 
			@RequestParam("id") int id) {
		if(type.equals("ca")) {
			if (confirm.equals("yes")) {
				CourseApplication courseApplication = carepo.getCourseApplicationByApplicationId(id);
				courseApplication.setStatus("Approved");
				carepo.save(courseApplication);
				Grade grade = new Grade();
				grade.setGrade("Null");
				grade.setCourse(courseApplication.getCourse());
				grade.setFaculty(courseApplication.getCourse().getFaculty());
				grade.setStudent(courseApplication.getStudent());
				grepo.save(grade);
				return "redirect:/admin/home";
			} else {
				CourseApplication courseApplication = carepo.getCourseApplicationByApplicationId(id);
				courseApplication.setStatus("Rejected");
				carepo.save(courseApplication);
				return "redirect:/admin/home";
			}
		}
		else {
			if (confirm.equals("yes")) {
				FacultyLeave facultyLeave = flrepo.getFacultyLeaveByLeaveId(id);
				facultyLeave.setStatus("Approved");
				flrepo.save(facultyLeave);
				return "redirect:/admin/facultyleavelist";
			} else {
				FacultyLeave facultyLeave = flrepo.getFacultyLeaveByLeaveId(id);
				facultyLeave.setStatus("Rejected");
				flrepo.save(facultyLeave);
				return "redirect:/admin/facultyleavelist";
			}
		}
	}

	@RequestMapping("/departmentlist")
	public String departmentlist(Model model, @RequestParam(name = "page") Optional<Integer> page,
			@RequestParam(name = "size") Optional<Integer> size, @RequestParam(name = "name") Optional<String> name) {
		model.addAttribute("admin", admin);
		int currentpage = page.orElse(1);
		int pagesize = size.orElse(5);
		String searchname = name.orElse("null");

		if (searchname.equals("null")) {
			List<Department> departments = drepo.findAll();
			model.addAttribute("departments", departments);
			model.addAttribute("searched", "null");
			Page<Department> departmentPage = dserv.findPaginatedDepartment(PageRequest.of(currentpage - 1, pagesize),
					departments);
			model.addAttribute("departmentPage", departmentPage);
			int totalPages = departmentPage.getTotalPages();
			if (totalPages > 0) {
				List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
				model.addAttribute("pageNumbers", pageNumbers);
			}
			return "departmentlist";
		} else {
			List<Department> departments = drepo.searchDepartment(searchname);
			if (departments.isEmpty()) {
				model.addAttribute("found", "nil");
			} else {
				model.addAttribute("found", "found");
			}
			model.addAttribute("departments", departments);
			model.addAttribute("searched", searchname);
			Page<Department> departmentPage = dserv.findPaginatedDepartment(PageRequest.of(currentpage - 1, pagesize),
					departments);
			model.addAttribute("departmentPage", departmentPage);
			int totalPages = departmentPage.getTotalPages();
			if (totalPages > 0) {
				List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
				model.addAttribute("pageNumbers", pageNumbers);
			}
			return "departmentlist";
		}
	}

	@RequestMapping("/studentlist")
	public String studentlist(Model model, @RequestParam(name = "page") Optional<Integer> page,
			@RequestParam(name = "size") Optional<Integer> size, @RequestParam(name = "name") Optional<String> name) {

		model.addAttribute("admin", admin);
		int currentpage = page.orElse(1);
		int pagesize = size.orElse(5);
		String searchname = name.orElse("null");

		if (searchname.equals("null")) {
			List<Student> students = srepo.findAll();
			model.addAttribute("students", students);
			model.addAttribute("searched", "null");
			Page<Student> studentPage = sserv.findPaginatedStudent(PageRequest.of(currentpage - 1, pagesize), students);
			model.addAttribute("studentPage", studentPage);
			int totalPages = studentPage.getTotalPages();
			if (totalPages > 0) {
				List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
				model.addAttribute("pageNumbers", pageNumbers);
			}
			return "studentlist";
		} else {
			List<Student> students = srepo.searchStudent(searchname);
			if (students.isEmpty()) {
				model.addAttribute("found", "nil");
			} else {
				model.addAttribute("found", "found");
			}
			model.addAttribute("students", students);
			model.addAttribute("searched", searchname);
			Page<Student> studentPage = sserv.findPaginatedStudent(PageRequest.of(currentpage - 1, pagesize), students);
			model.addAttribute("studentPage", studentPage);
			int totalPages = studentPage.getTotalPages();
			if (totalPages > 0) {
				List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
				model.addAttribute("pageNumbers", pageNumbers);
			}
			return "studentlist";
		}

	}

	@RequestMapping("/facultylist")
	public String facultylist(Model model, @RequestParam(name = "page") Optional<Integer> page,
			@RequestParam(name = "size") Optional<Integer> size, @RequestParam(name = "name") Optional<String> name) {

		model.addAttribute("admin", admin);
		int currentpage = page.orElse(1);
		int pagesize = size.orElse(5);
		String searchname = name.orElse("null");
		if (searchname.equals("null")) {
			List<Faculty> faculties = frepo.findAll();
			model.addAttribute("faculties", faculties);
			model.addAttribute("searched", "null");
			Page<Faculty> facultyPage = fserv.findPaginatedFaculty(PageRequest.of(currentpage - 1, pagesize),
					faculties);
			model.addAttribute("facultyPage", facultyPage);
			int totalPages = facultyPage.getTotalPages();
			if (totalPages > 0) {
				List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
				model.addAttribute("pageNumbers", pageNumbers);
			}
			return "facultylist";
		} else {
			List<Faculty> faculties = frepo.searchFaculty(searchname);
			if (faculties.isEmpty()) {
				model.addAttribute("found", "nil");
			} else {
				model.addAttribute("found", "found");
			}
			model.addAttribute("faculties", faculties);
			model.addAttribute("searched", searchname);
			Page<Faculty> facultyPage = fserv.findPaginatedFaculty(PageRequest.of(currentpage - 1, pagesize),
					faculties);
			model.addAttribute("facultyPage", facultyPage);
			int totalPages = facultyPage.getTotalPages();
			if (totalPages > 0) {
				List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
				model.addAttribute("pageNumbers", pageNumbers);
			}
			return "facultylist";
		}
	}

	@RequestMapping("/adminlist")
	public String adminlist(Model model, @RequestParam(name = "page") Optional<Integer> page,
			@RequestParam(name = "size") Optional<Integer> size, @RequestParam(name = "name") Optional<String> name) {

		model.addAttribute("admin", admin);
		int currentpage = page.orElse(1);
		int pagesize = size.orElse(5);
		String searchname = name.orElse("null");
		if (searchname.equals("null")) {
			List<Admin> admins = arepo.findAll();
			model.addAttribute("admins", admins);
			model.addAttribute("searched", "null");
			Page<Admin> adminPage = aserv.findPaginatedAdmin(PageRequest.of(currentpage - 1, pagesize), admins);
			model.addAttribute("adminPage", adminPage);
			int totalPages = adminPage.getTotalPages();
			if (totalPages > 0) {
				List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
				model.addAttribute("pageNumbers", pageNumbers);
			}
			return "adminlist";
		} else {
			List<Admin> admins = arepo.searchAdmin(searchname);
			if (admins.isEmpty()) {
				model.addAttribute("found", "nil");
			} else {
				model.addAttribute("found", "found");
			}
			model.addAttribute("admins", admins);
			model.addAttribute("searched", searchname);
			Page<Admin> adminPage = aserv.findPaginatedAdmin(PageRequest.of(currentpage - 1, pagesize), admins);
			model.addAttribute("adminPage", adminPage);
			int totalPages = adminPage.getTotalPages();
			if (totalPages > 0) {
				List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
				model.addAttribute("pageNumbers", pageNumbers);
			}
			return "adminlist";
		}
	}

	@RequestMapping("/courselist")
	public String courselist(Model model, @RequestParam(name = "page") Optional<Integer> page,
			@RequestParam(name = "size") Optional<Integer> size, @RequestParam(name = "name") Optional<String> name) {
		model.addAttribute("admin", admin);
		int currentpage = page.orElse(1);
		int pagesize = size.orElse(5);
		String searchname = name.orElse("null");
		if (searchname.equals("null")) {
			List<Course> courses = crepo.findAll();
			model.addAttribute("courses", courses);
			model.addAttribute("searched", "null");
			Page<Course> coursePage = cserv.findPaginatedCourse(PageRequest.of(currentpage - 1, pagesize), courses);
			model.addAttribute("coursePage", coursePage);
			int totalPages = coursePage.getTotalPages();
			if (totalPages > 0) {
				List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
				model.addAttribute("pageNumbers", pageNumbers);
			}
			return "courselist";
		} else {
			List<Course> courses = crepo.searchCourse(searchname);
			if (courses.isEmpty()) {
				model.addAttribute("found", "nil");
			} else {
				model.addAttribute("found", "found");
			}
			model.addAttribute("courses", courses);
			model.addAttribute("searched", searchname);
			Page<Course> coursePage = cserv.findPaginatedCourse(PageRequest.of(currentpage - 1, pagesize), courses);
			model.addAttribute("coursePage", coursePage);
			int totalPages = coursePage.getTotalPages();
			if (totalPages > 0) {
				List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
				model.addAttribute("pageNumbers", pageNumbers);
			}
			return "courselist";
		}
	}

	@RequestMapping("/gradelist")
	public String gradelist(Model model, @RequestParam(name = "page") Optional<Integer> page,
			@RequestParam(name = "size") Optional<Integer> size, @RequestParam(name = "name") Optional<String> name) {
		model.addAttribute("admin", admin);
		int currentpage = page.orElse(1);
		int pagesize = size.orElse(5);
		String searchname = name.orElse("null");
		if (searchname.equals("null")) {
			List<Grade> grades = grepo.findAll();
			model.addAttribute("grades", grades);
			model.addAttribute("searched", "null");
			Page<Grade> gradePage = gserv.findPaginatedGrade(PageRequest.of(currentpage - 1, pagesize), grades);
			model.addAttribute("gradePage", gradePage);
			int totalPages = gradePage.getTotalPages();
			if (totalPages > 0) {
				List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
				model.addAttribute("pageNumbers", pageNumbers);
			}
			return "gradelist";
		} else {
			List<Grade> grades = grepo.searchGrade(searchname);
			if (grades.isEmpty()) {
				model.addAttribute("found", "nil");
			} else {
				model.addAttribute("found", "found");
			}
			model.addAttribute("grades", grades);
			model.addAttribute("searched", searchname);
			Page<Grade> gradePage = gserv.findPaginatedGrade(PageRequest.of(currentpage - 1, pagesize), grades);
			model.addAttribute("gradePage", gradePage);
			int totalPages = gradePage.getTotalPages();
			if (totalPages > 0) {
				List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
				model.addAttribute("pageNumbers", pageNumbers);
			}
			return "gradelist";
		}
	}

	@RequestMapping("/userlist")
	public String userlist(Model model, @RequestParam(name = "page") Optional<Integer> page,
			@RequestParam(name = "size") Optional<Integer> size, @RequestParam(name = "sort") Optional<String> sort) {
		model.addAttribute("admin", admin);

		int currentpage = page.orElse(1);
		int pagesize = size.orElse(5);
		String sorting = sort.orElse("all");

		if (sorting.equals("student")) {
			model.addAttribute("sort", sorting);
			List<User> users = urepo.getAllStudentUsers();
			if (users.isEmpty()) {
				String msg = "null";
				model.addAttribute("msg", msg);
			} else {
				model.addAttribute("users", users);
				Page<User> userPage = userv.findPaginatedUser(PageRequest.of(currentpage - 1, pagesize), users);
				model.addAttribute("userPage", userPage);

				int totalPages = userPage.getTotalPages();
				if (totalPages > 0) {
					List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed()
							.collect(Collectors.toList());
					model.addAttribute("pageNumbers", pageNumbers);
				}
				String msg = "found";
				model.addAttribute("msg", msg);

			}
			return "userlist";
		} else if (sorting.equals("faculty")) {
			model.addAttribute("sort", sorting);
			List<User> users = urepo.getAllFacultyUsers();
			if (users.isEmpty()) {
				String msg = "null";
				model.addAttribute("msg", msg);
			} else {
				model.addAttribute("users", users);
				Page<User> userPage = userv.findPaginatedUser(PageRequest.of(currentpage - 1, pagesize), users);
				model.addAttribute("userPage", userPage);

				int totalPages = userPage.getTotalPages();
				if (totalPages > 0) {
					List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed()
							.collect(Collectors.toList());
					model.addAttribute("pageNumbers", pageNumbers);
				}
				String msg = "found";
				model.addAttribute("msg", msg);

			}
			return "userlist";
		}

		else if (sorting.equals("admin")) {
			model.addAttribute("sort", sorting);
			List<User> users = urepo.getAllAdminUsers();
			if (users.isEmpty()) {
				String msg = "null";
				model.addAttribute("msg", msg);
			} else {
				model.addAttribute("users", users);
				Page<User> userPage = userv.findPaginatedUser(PageRequest.of(currentpage - 1, pagesize), users);
				model.addAttribute("userPage", userPage);

				int totalPages = userPage.getTotalPages();
				if (totalPages > 0) {
					List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed()
							.collect(Collectors.toList());
					model.addAttribute("pageNumbers", pageNumbers);
				}
				String msg = "found";
				model.addAttribute("msg", msg);

			}
			return "userlist";
		} else {
			model.addAttribute("sort", sorting);
			List<User> users = urepo.findAll();
			if (users.isEmpty()) {
				String msg = "null";
				model.addAttribute("msg", msg);
			} else {
				model.addAttribute("users", users);
				Page<User> userPage = userv.findPaginatedUser(PageRequest.of(currentpage - 1, pagesize), users);
				model.addAttribute("userPage", userPage);

				int totalPages = userPage.getTotalPages();
				if (totalPages > 0) {
					List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed()
							.collect(Collectors.toList());
					model.addAttribute("pageNumbers", pageNumbers);
				}
				String msg = "found";
				model.addAttribute("msg", msg);

			}
			return "userlist";
		}

	}

	@RequestMapping("/courseapplicationlist")
	public String courseApplicationlist(Model model, @RequestParam(name = "page") Optional<Integer> page,
			@RequestParam(name = "size") Optional<Integer> size, @RequestParam(name = "sort") Optional<String> sort) {
		model.addAttribute("admin", admin);

		int currentpage = page.orElse(1);
		int pagesize = size.orElse(5);
		String sorting = sort.orElse("all");

		if (sorting.equals("approved")) {
			model.addAttribute("sort", sorting);
			List<CourseApplication> courseApplications = caserv.getApprovedCourseApplications();
			if (courseApplications.isEmpty()) {
				String msg = "null";
				model.addAttribute("msg", msg);
			} else {
				model.addAttribute("courseApplications", courseApplications);
				Page<CourseApplication> courseApplicationPage = caserv
						.findPaginatedCourseApplication(PageRequest.of(currentpage - 1, pagesize), courseApplications);
				model.addAttribute("courseApplicationPage", courseApplicationPage);

				int totalPages = courseApplicationPage.getTotalPages();
				if (totalPages > 0) {
					List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed()
							.collect(Collectors.toList());
					model.addAttribute("pageNumbers", pageNumbers);
				}
				String msg = "found";
				model.addAttribute("msg", msg);

			}
			return "courseapplicationlist";
		} else if (sorting.equals("pending")) {
			model.addAttribute("sort", sorting);
			List<CourseApplication> courseApplications = caserv.getPendingCourseApplications();
			if (courseApplications.isEmpty()) {
				String msg = "null";
				model.addAttribute("msg", msg);
			} else {
				model.addAttribute("courseApplications", courseApplications);
				Page<CourseApplication> courseApplicationPage = caserv
						.findPaginatedCourseApplication(PageRequest.of(currentpage - 1, pagesize), courseApplications);
				model.addAttribute("courseApplicationPage", courseApplicationPage);

				int totalPages = courseApplicationPage.getTotalPages();
				if (totalPages > 0) {
					List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed()
							.collect(Collectors.toList());
					model.addAttribute("pageNumbers", pageNumbers);
				}
				String msg = "found";
				model.addAttribute("msg", msg);
			}
			return "courseapplicationlist";
		}

		else if (sorting.equals("rejected")) {
			model.addAttribute("sort", sorting);
			List<CourseApplication> courseApplications = caserv.getRejectedCourseApplications();
			if (courseApplications.isEmpty()) {
				String msg = "null";
				model.addAttribute("msg", msg);
			} else {
				model.addAttribute("courseApplications", courseApplications);
				Page<CourseApplication> courseApplicationPage = caserv
						.findPaginatedCourseApplication(PageRequest.of(currentpage - 1, pagesize), courseApplications);
				model.addAttribute("courseApplicationPage", courseApplicationPage);

				int totalPages = courseApplicationPage.getTotalPages();
				if (totalPages > 0) {
					List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed()
							.collect(Collectors.toList());
					model.addAttribute("pageNumbers", pageNumbers);
				}
				String msg = "found";
				model.addAttribute("msg", msg);
			}
			return "courseapplicationlist";
		}

		else {
			model.addAttribute("sort", sorting);
			List<CourseApplication> courseApplications = carepo.findAll();
			if (courseApplications.isEmpty()) {
				String msg = "null";
				model.addAttribute("msg", msg);
			} else {
				model.addAttribute("courseApplications", courseApplications);
				Page<CourseApplication> courseApplicationPage = caserv
						.findPaginatedCourseApplication(PageRequest.of(currentpage - 1, pagesize), courseApplications);
				model.addAttribute("courseApplicationPage", courseApplicationPage);

				int totalPages = courseApplicationPage.getTotalPages();
				if (totalPages > 0) {
					List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed()
							.collect(Collectors.toList());
					model.addAttribute("pageNumbers", pageNumbers);
				}
				String msg = "found";
				model.addAttribute("msg", msg);
			}
			return "courseapplicationlist";
		}

	}
	
	@RequestMapping("/facultyleavelist")
	public String facultyleavelist(Model model, @RequestParam(name = "page") Optional<Integer> page,
			@RequestParam(name = "size") Optional<Integer> size, 
			@RequestParam(name = "sort") Optional<String> sort,
			@RequestParam(name="start") Optional<String> start,
			@RequestParam(name="end") Optional<String> end) {
		model.addAttribute("admin", admin);

		int currentpage = page.orElse(1);
		int pagesize = size.orElse(5);
		String sorting = sort.orElse("all");
		String startd = start.orElse("null");
		String endd = end.orElse("null");

		if (sorting.equals("approved")) {
			if(startd.equals("null") && endd.equals("null")) {
				model.addAttribute("sort", sorting);
				List<FacultyLeave> facultyLeaves = flserv.getApprovedFacultyLeaves();
				if (facultyLeaves.isEmpty()) {
					String msg = "null";
					model.addAttribute("msg", msg);
				} else {
					model.addAttribute("facultyLeaves", facultyLeaves);
					Page<FacultyLeave> facultyLeavePage = flserv
							.findPaginatedFacultyLeave(PageRequest.of(currentpage - 1, pagesize), facultyLeaves);
					model.addAttribute("facultyLeavePage", facultyLeavePage);

					int totalPages = facultyLeavePage.getTotalPages();
					if (totalPages > 0) {
						List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed()
								.collect(Collectors.toList());
						model.addAttribute("pageNumbers", pageNumbers);
					}
					String msg = "found";
					model.addAttribute("msg", msg);
				}
			}
			else {
				model.addAttribute("sort", sorting);
				LocalDate dstart = LocalDate.parse(startd);
				LocalDate dend = LocalDate.parse(endd);
				if(dend.isBefore(dstart)) {
					List<FacultyLeave> facultyLeaves = flrepo.findAll();
					Page<FacultyLeave> facultyLeavePage = flserv
							.findPaginatedFacultyLeave(PageRequest.of(currentpage - 1, pagesize), facultyLeaves);
					model.addAttribute("facultyLeavePage", facultyLeavePage);

					int totalPages = facultyLeavePage.getTotalPages();
					if (totalPages > 0) {
						List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed()
								.collect(Collectors.toList());
						model.addAttribute("pageNumbers", pageNumbers);
					}
					model.addAttribute("facultyLeaves", facultyLeaves);
					model.addAttribute("msg", "found");
					model.addAttribute("sort", "all");
					model.addAttribute("emsg", "yes");
					return "facultyleavelist";
				}
				
				List<FacultyLeave> facultyLeaves = flserv.getRangeFacultyLeaves(dstart, dend);
				if (facultyLeaves.isEmpty()) {
					String msg = "null";
					model.addAttribute("msg", msg);
				} else {
					model.addAttribute("facultyLeaves", facultyLeaves);
					Page<FacultyLeave> facultyLeavePage = flserv
							.findPaginatedFacultyLeave(PageRequest.of(currentpage - 1, pagesize), facultyLeaves);
					model.addAttribute("facultyLeavePage", facultyLeavePage);

					int totalPages = facultyLeavePage.getTotalPages();
					if (totalPages > 0) {
						List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed()
								.collect(Collectors.toList());
						model.addAttribute("pageNumbers", pageNumbers);
					}
					String msg = "found";
					model.addAttribute("msg", msg);
					model.addAttribute("display", "yes");
					model.addAttribute("start", startd);
					model.addAttribute("end", endd);
				}
			}
			return "facultyleavelist";
		} else if (sorting.equals("pending")) {
			model.addAttribute("sort", sorting);
			List<FacultyLeave> facultyLeaves = flserv.getPendingFacultyLeaves();
			if (facultyLeaves.isEmpty()) {
				String msg = "null";
				model.addAttribute("msg", msg);
			} else {
				model.addAttribute("facultyLeaves", facultyLeaves);
				Page<FacultyLeave> facultyLeavePage = flserv
						.findPaginatedFacultyLeave(PageRequest.of(currentpage - 1, pagesize), facultyLeaves);
				model.addAttribute("facultyLeavePage", facultyLeavePage);

				int totalPages = facultyLeavePage.getTotalPages();
				if (totalPages > 0) {
					List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed()
							.collect(Collectors.toList());
					model.addAttribute("pageNumbers", pageNumbers);
				}
				String msg = "found";
				model.addAttribute("msg", msg);
			}
			return "facultyleavelist";
		}

		else if (sorting.equals("rejected")) {
			model.addAttribute("sort", sorting);
			List<FacultyLeave> facultyLeaves = flserv.getRejectedFacultyLeaves();
			if (facultyLeaves.isEmpty()) {
				String msg = "null";
				model.addAttribute("msg", msg);
			} else {
				model.addAttribute("facultyLeaves", facultyLeaves);
				Page<FacultyLeave> facultyLeavePage = flserv
						.findPaginatedFacultyLeave(PageRequest.of(currentpage - 1, pagesize), facultyLeaves);
				model.addAttribute("facultyLeavePage", facultyLeavePage);

				int totalPages = facultyLeavePage.getTotalPages();
				if (totalPages > 0) {
					List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed()
							.collect(Collectors.toList());
					model.addAttribute("pageNumbers", pageNumbers);
				}
				String msg = "found";
				model.addAttribute("msg", msg);
			}
			return "facultyleavelist";
		}

		else {
			model.addAttribute("sort", sorting);
			List<FacultyLeave> facultyLeaves = flrepo.findAll();
			if (facultyLeaves.isEmpty()) {
				String msg = "null";
				model.addAttribute("msg", msg);
			} else {
				model.addAttribute("facultyLeaves", facultyLeaves);
				Page<FacultyLeave> facultyLeavePage = flserv
						.findPaginatedFacultyLeave(PageRequest.of(currentpage - 1, pagesize), facultyLeaves);
				model.addAttribute("facultyLeavePage", facultyLeavePage);

				int totalPages = facultyLeavePage.getTotalPages();
				if (totalPages > 0) {
					List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed()
							.collect(Collectors.toList());
					model.addAttribute("pageNumbers", pageNumbers);
				}
				String msg = "found";
				model.addAttribute("msg", msg);
			}
			return "facultyleavelist";
		}

	}

	@RequestMapping("/viewdepartment")
	public String viewDepartment(Model model, @RequestParam(name = "viewdepartment") Optional<Integer> searchid) {
		int search = searchid.orElse(0);
		model.addAttribute("admin", admin);
		if (search == 0) {
			String msg = "null";
			model.addAttribute("msg", msg);
		} else {
			Department department = drepo.getDepartmentByDepartmentId(search);
			model.addAttribute("department", department);
			String msg = "found";
			model.addAttribute("msg", msg);
			List<Course> courses = crepo.getCoursesByDepartmentId(search);
			model.addAttribute("courses", courses);
			List<Faculty> faculties = frepo.getFacultiesByDepartmentId(search);
			model.addAttribute("faculties", faculties);
		}
		return "viewdepartment";
	}

	@RequestMapping("/viewstudent")
	public String viewStudent(Model model, @RequestParam(name = "viewstudent") Optional<Integer> searchid) {
		int search = searchid.orElse(0);
		model.addAttribute("admin", admin);
		if (search == 0) {
			String msg = "null";
			model.addAttribute("msg", msg);
		} else {
			Student student = srepo.getStudentByStudentId(search);
			model.addAttribute("student", student);
			String msg = "found";
			model.addAttribute("msg", msg);
			List<CourseApplication> courseApplications = carepo.getCourseApplicationsByStudentId(search);
			if (courseApplications.isEmpty()) {
				model.addAttribute("searchca", "null");
			} else {
				model.addAttribute("searchca", "found");
			}
			model.addAttribute("courseApplications", courseApplications);
		}
		return "viewstudent";
	}

	@RequestMapping("/viewfaculty")
	public String viewFaculty(Model model, @RequestParam(name = "viewfaculty") Optional<Integer> searchid) {
		int search = searchid.orElse(0);
		model.addAttribute("admin", admin);
		if (search == 0) {
			String msg = "null";
			model.addAttribute("msg", msg);
		} else {
			Faculty faculty = frepo.getByFacultyId(search);
			model.addAttribute("faculty", faculty);
			String msg = "found";
			model.addAttribute("msg", msg);
			List<Course> courses = crepo.getCoursesByFacultyId(search);
			if (courses.isEmpty()) {
				model.addAttribute("searchcourse", "null");
			} else {
				model.addAttribute("searchcourse", "found");
			}
			model.addAttribute("courses", courses);
		}
		return "viewfaculty";
	}
	
	@RequestMapping("/viewfacultyleave")
	public String viewFacultyLeave(Model model, @RequestParam(name = "viewfacultyleave") Optional<Integer> searchid) {
		int search = searchid.orElse(0);
		model.addAttribute("admin", admin);
		if (search == 0) {
			String msg = "null";
			model.addAttribute("msg", msg);
		} else {
			FacultyLeave facultyLeave = flrepo.getFacultyLeaveByLeaveId(search);
			model.addAttribute("facultyLeave", facultyLeave);
			String msg = "found";
			model.addAttribute("msg", msg);
		}
		return "viewfacultyleave";
	}

	@RequestMapping("/viewadmin")
	public String viewAdmin(Model model, @RequestParam(name = "viewadmin") Optional<Integer> searchid) {
		int search = searchid.orElse(0);
		model.addAttribute("admin", admin);
		if (search == 0) {
			String msg = "null";
			model.addAttribute("msg", msg);
		} else {
			Admin admin = arepo.getByAdminId(search);
			model.addAttribute("admind", admin);
			String msg = "found";
			model.addAttribute("msg", msg);
		}
		return "viewadmin";
	}

	@RequestMapping("/viewgrade")
	public String viewGrade(Model model, @RequestParam(name = "viewgrade") Optional<Integer> searchid) {
		int search = searchid.orElse(0);
		model.addAttribute("admin", admin);
		if (search == 0) {
			String msg = "null";
			model.addAttribute("msg", msg);
		} else {
			Grade grade = grepo.getGradeByGradeId(search);
			model.addAttribute("grade", grade);
			String msg = "found";
			model.addAttribute("msg", msg);
		}
		return "viewgrade";
	}

	@RequestMapping("/viewcourse")
	public String viewCourse(Model model, @RequestParam(name = "viewcourse") Optional<Integer> searchid) {
		int search = searchid.orElse(0);
		model.addAttribute("admin", admin);
		if (search == 0) {
			String msg = "null";
			model.addAttribute("msg", msg);
		} else {
			Course course = crepo.getCourseByCourseId(search);
			model.addAttribute("course", course);
			String msg = "found";
			model.addAttribute("msg", msg);
			List<CourseApplication> courseApplications = carepo.getCourseApplicationsByCourseId(search);
			if (courseApplications.isEmpty()) {
				model.addAttribute("searchca", "null");
			} else {
				model.addAttribute("searchca", "found");
			}
			model.addAttribute("courseApplications", courseApplications);
		}
		return "viewcourse";
	}
	
	@RequestMapping("/viewuser")
	public String viewUser(Model model, @RequestParam(name = "viewuser") Optional<Integer> searchid) {
		int search = searchid.orElse(0);
		model.addAttribute("admin", admin);
		if (search == 0) {
			String msg = "null";
			model.addAttribute("msg", msg);
		} else {
			User user = urepo.getUserByUserId(search);
			String msg = "found";
			model.addAttribute("msg", msg);
			if(user.getFacultyId() == 0 && user.getAdminId() == 0) {
				Student student = srepo.getStudentByStudentId(user.getStudentId());
				model.addAttribute("user", user);
				model.addAttribute("student", student);
				model.addAttribute("type", "student");
				return "viewuser";
			}
			else if(user.getAdminId() == 0 && user.getStudentId()==0) {
				Faculty faculty = frepo.getByFacultyId(user.getFacultyId());
				model.addAttribute("user", user);
				model.addAttribute("faculty", faculty);
				model.addAttribute("type", "faculty");
				return "viewuser";
			}
			else {
				Admin admind = arepo.getByAdminId(user.getAdminId());
				model.addAttribute("user", user);
				model.addAttribute("admind", admind);
				model.addAttribute("type", "admin");
				return "viewuser";
			}
		}
		return "viewuser";
	}

	@RequestMapping("/viewcourseapplication")
	public String viewCourseApplication(Model model,
			@RequestParam(name = "viewcourseapplication") Optional<Integer> searchid) {
		int search = searchid.orElse(0);
		model.addAttribute("admin", admin);
		if (search == 0) {
			String msg = "null";
			model.addAttribute("msg", msg);
		} else {
			CourseApplication courseApplication = carepo.getCourseApplicationByApplicationId(search);
			model.addAttribute("courseApplication", courseApplication);
			String msg = "found";
			model.addAttribute("msg", msg);

		}
		return "viewcourseapplication";
	}

	@RequestMapping("/edit/{id}")
	public String EditForm(Model model,@PathVariable("id") Integer id) {
		Admin admind =arepo.getByAdminId(id);
		model.addAttribute("admind",admind);
		model.addAttribute("admin", admin);
		return "confirmupdateadmin";
	
	}
}
