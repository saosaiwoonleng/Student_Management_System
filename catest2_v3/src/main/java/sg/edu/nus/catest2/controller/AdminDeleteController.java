package sg.edu.nus.catest2.controller;

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
public class AdminDeleteController {
	
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
	
	@RequestMapping("/admin/delete")
	public String delete(Model model, 
			@SessionAttribute("usersession") Session session,
			@RequestParam(name = "deleteType") Optional<String> deleteType,
			@RequestParam(name = "id") Optional<Integer> objectid,
			@RequestParam(name = "confirm") Optional<String> cfm) {
		admin = arepo.getByAdminId(session.getSessionId());
		model.addAttribute("admin", admin);
		String delete = deleteType.orElse("null");
		String confirm = cfm.orElse("no");
		int id = objectid.orElse(0);
		if (delete.equals("null") || id == 0 || confirm.equals("cancel")) {
			return "redirect:/admin/home";
		}
		// for deleting department
		if (delete.equals("department")) {
			Department department = drepo.getDepartmentByDepartmentId(id);
			if (confirm.equals("no")) {
				model.addAttribute("department", department);
				model.addAttribute("type", "department");
				return "confirmdelete";
			} else {
				List<Course> courses = crepo.getCoursesByDepartmentId(id);
				for (Course c : courses) {
					List<CourseApplication> courseApplications = carepo
							.getCourseApplicationsByCourseId(c.getCourseId());
					for (CourseApplication ca : courseApplications) {
						carepo.delete(ca);
					}
					List<Grade> grades = grepo.getGradesByCourseId(c.getCourseId());
					for (Grade g : grades) {
						grepo.delete(g);
					}
					crepo.delete(c);
				}
				List<Faculty> faculties = frepo.getFacultiesByDepartmentId(id);
				for (Faculty f : faculties) {
					List<FacultyLeave> facultyLeaves = flrepo.getFacultyLeaveByFacultyId(f.getFacultyId());
					for (FacultyLeave fl : facultyLeaves) {
						flrepo.delete(fl);
					}
					frepo.delete(f);
				}
				drepo.delete(department);
			}
			return "redirect:/admin/departmentlist";
		}
		// for deleting course
		else if (delete.equals("course")) {
			Course course = crepo.getCourseByCourseId(id);
			Department department = drepo.getDepartmentByDepartmentId(course.getDepartment().getDepartmentId());
			Faculty faculty = frepo.getByFacultyId(course.getFaculty().getFacultyId());
			if (confirm.equals("no")) {
				model.addAttribute("course", course);
				model.addAttribute("department", department);
				model.addAttribute("faculty", faculty);
				model.addAttribute("type", "course");
				return "confirmdelete";
			} else {
				List<CourseApplication> courseApplications = carepo.getCourseApplicationsByCourseId(id);
				for (CourseApplication ca : courseApplications) {
					carepo.delete(ca);
				}
				List<Grade> grades = grepo.getGradesByCourseId(id);
				for (Grade g : grades) {
					grepo.delete(g);
				}
				crepo.delete(course);
			}
			return "redirect:/admin/courselist";
		}

		// for deleting course applications
		else if (delete.equals("courseApplication")) {
			CourseApplication courseApplication = carepo.getCourseApplicationByApplicationId(id);
			if (confirm.equals("no")) {
				model.addAttribute("courseApplication", courseApplication);
				model.addAttribute("type", "courseApplication");
				return "confirmdelete";
			} else {
				carepo.delete(courseApplication);
			}
			return "redirect:/admin/courseapplicationlist";
		}

		// for deleting faculty
		else if (delete.equals("faculty")) {
			Faculty faculty = frepo.getByFacultyId(id);
			List<Course> courses = crepo.getCoursesByFacultyId(id);
			if (confirm.equals("no")) {
				if (courses.isEmpty()) {
					model.addAttribute("deletesafe", "safe");
				} else {
					model.addAttribute("deletesafe", "unsafe");
				}
				model.addAttribute("courses", courses);
				model.addAttribute("faculty", faculty);
				model.addAttribute("type", "faculty");
				return "confirmdelete";
			} else {
				for (Course c : courses) {
					List<CourseApplication> courseApplications = carepo
							.getCourseApplicationsByCourseId(c.getCourseId());
					for (CourseApplication ca : courseApplications) {
						carepo.delete(ca);
					}
					List<Grade> grades = grepo.getGradesByCourseId(c.getCourseId());
					for (Grade g : grades) {
						grepo.delete(g);
					}
					crepo.delete(c);
				}
				List<FacultyLeave> facultyLeaves = flrepo.getFacultyLeaveByFacultyId(faculty.getFacultyId());
				for (FacultyLeave fl : facultyLeaves) {
					flrepo.delete(fl);
				}
				User user = urepo.getByFacultyId(faculty.getFacultyId());
				urepo.delete(user);
				frepo.delete(faculty);
			}
			return "redirect:/admin/facultylist";
		}

		// for deleting student
		else if (delete.equals("student")) {
			Student student = srepo.getStudentByStudentId(id);
			List<CourseApplication> courseApplications = carepo.getCourseApplicationsByStudentId(id);
			if (confirm.equals("no")) {
				if (courseApplications.isEmpty()) {
					model.addAttribute("deletesafe", "safe");
				} else {
					model.addAttribute("deletesafe", "unsafe");
				}
				model.addAttribute("student", student);
				model.addAttribute("courseApplications", courseApplications);
				model.addAttribute("type", "student");
				return "confirmdelete";
			} else {
				List<Grade> grades = grepo.getGradesByStudentId(id);
				for (Grade g : grades) {
					grepo.delete(g);
				}
				for (CourseApplication ca : courseApplications) {
					carepo.delete(ca);
				}
				User user = urepo.getByStudentId(student.getStudentId());
				urepo.delete(user);
				srepo.delete(student);
			}
			return "redirect:/admin/studentlist";
		}

		// for deleting admin
		else if (delete.equals("admin")) {
			Admin admin = arepo.getByAdminId(id);
			if (confirm.equals("no")) {
				model.addAttribute("admind", admin);
				model.addAttribute("type", "admin");
				return "confirmdelete";
			} else {
				User user = urepo.getByAdminId(admin.getAdminId());
				urepo.delete(user);
				arepo.delete(admin);
			}
			return "redirect:/admin/adminlist";
		}

		// for deleting grade
		else if (delete.equals("grade")) {
			Grade grade = grepo.getGradeByGradeId(id);
			if (confirm.equals("no")) {
				model.addAttribute("grade", grade);
				model.addAttribute("type", "grade");
				return "confirmdelete";
			} else {
				grepo.delete(grade);
			}
			return "redirect:/admin/gradelist";
		}
		return "redirect:/admin/home";
	}

}
