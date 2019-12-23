package sg.edu.nus.catest2.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;

import sg.edu.nus.catest2.repo.*;
import sg.edu.nus.catest2.service.CourseApplicationService;
import sg.edu.nus.catest2.service.CourseService;
import sg.edu.nus.catest2.service.FacultyLeaveService;
import sg.edu.nus.catest2.service.FacultyService;
import sg.edu.nus.catest2.service.GradeService;
import sg.edu.nus.catest2.service.StudentService;
import sg.edu.nus.catest2.model.*;
import sg.edu.nus.catest2.mvcmodel.*;

@Controller
@RequestMapping("/faculty")
public class FacultyController {
	

	@Autowired
	FacultyRepository frepo;
	@Autowired
	DepartmentRepository drepo;
	@Autowired
	CourseRepository crepo;
	@Autowired
	CourseApplicationRepository carepo;
	@Autowired
	StudentRepository srepo;
	@Autowired
	GradeRepository grepo;
	@Autowired
	GradeService gserv;
	@Autowired
	StudentService sserv;
	@Autowired
	FacultyService fserv;
	@Autowired
	CourseService cserv;
	@Autowired
	CourseApplicationService caserv;
	
	private static Faculty faculty;
	private FacultyLeave facultyleave;
	
	private Page<Grade> gradePage;
	private Course course;
	

	@GetMapping("/home")                                      //list of the courses assigned to the faculty
	public String home(Model model,@SessionAttribute("usersession") Session session,
			@RequestParam(name = "page") Optional<Integer> page, 
			@RequestParam(name = "size") Optional<Integer> size) {
		
		if(session.getSessionId() == 0) {
			return "redirect:/login/welcome";
		}

		faculty = frepo.getByFacultyId(session.getSessionId());
		
		model.addAttribute("faculty", faculty);

		ArrayList<Course> courses = (ArrayList<Course>) crepo.findCoursesByFacultyId(faculty.getFacultyId());
	
		int currentpage = page.orElse(1);
		int pagesize = size.orElse(5);
		Page<Course> coursePage = cserv.findPaginatedCourse
				(PageRequest.of(currentpage - 1, pagesize),(courses));
        model.addAttribute("coursePage", coursePage);
        int totalPages = coursePage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                .boxed()
                .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
	return "facultyhome";
		
	}
	
	@GetMapping("/info")
	public String facultyInfo(Model model, @SessionAttribute("usersession") Session session) {
		faculty = frepo.getByFacultyId(session.getSessionId());
		model.addAttribute("faculty", faculty);
		return "facultyinfo";
	}
	
	@PostMapping("/editfaculty")
	public String editFaculty(@Valid Faculty faculty1, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return "facultyinfo";
		}else {
			faculty1.setFacultyId(faculty.getFacultyId());
			faculty1.setDepartment(faculty.getDepartment());;
			this.fserv.save(faculty1);
			return "redirect:/faculty/home";
		}

	}
	
	@PostMapping("/editgrade")
	public String editGrade(Model model, @RequestParam(name = "gradeid") int gid, @RequestParam(name = "grade")String g) {

		model.addAttribute("faculty", faculty);
		Grade grade = new Grade();
		grade.setFaculty(faculty);
		grade.setGradeId(gid);
		grade.setGrade(g);
		Grade gre = grepo.findByGradeId(gid);
		grade.setCourse(gre.getCourse());
		grade.setStudent(gre.getStudent());
		model.addAttribute("grade", grade);
		return "facultyviewgrade";
	}
	
	@RequestMapping("/updategrade")
	public String updategrade(Model model, @RequestParam("id") int id,
			@RequestParam("grade") String grade) {

		Grade ugrade = grepo.findByGradeId(id);
		ugrade.setGrade(grade);
		grepo.save(ugrade);
		model.addAttribute("grade", ugrade);
		model.addAttribute("faculty", faculty);
	
		return "redirect:/faculty/viewcourse?facultyviewcourse=" + ugrade.getCourse().getCourseId();
	}
	
	@GetMapping("/studentlist")                          //list of students of the courses faculty teaches
	public String allStudentListByFaculty(Model model,
		@RequestParam(name = "page") Optional<Integer> page, 
		@RequestParam(name = "size") Optional<Integer> size){
			
		model.addAttribute("faculty", faculty);
		List<Course> courses = (List<Course>) crepo.findCoursesByFacultyId(faculty.getFacultyId());                                 
		model.addAttribute("courses", courses);                    //the courses that the current facultyid teaches
		
		ArrayList<CourseApplication> courseapplications = new ArrayList<CourseApplication>();
		ArrayList<Student> students = new ArrayList<Student>();
	
		for (Course c : courses) {																//each courseid have multiple row so iterate one by one
			List<CourseApplication> cas =  (List<CourseApplication>) carepo.findByCourseId(c.getCourseId());				
			for (CourseApplication ca: cas) {														//add each row to the courseapplication
				courseapplications.add(ca);	
				students.add(ca.getStudent());
			}
		}
		model.addAttribute("courseapplications", courseapplications);
		model.addAttribute("students", students);
		ArrayList<Grade> grades = new ArrayList<Grade>();

		for (Course c : courses) {
			for (Student s : students) {
				grades.add(grepo.findByCourseIdStudentId(c.getCourseId(), s.getStudentId()));	

			}
		}
		
		model.addAttribute("grades", grades);

		
		int currentpage = page.orElse(1);
		int pagesize = size.orElse(5);
		Page<CourseApplication> courseApplicationPage = caserv.findPaginatedCourseApplication
				(PageRequest.of(currentpage - 1, pagesize),(courseapplications));
        model.addAttribute("courseApplicationPage", courseApplicationPage);
        int totalPages = courseApplicationPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                .boxed()
                .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        return "facultystudentlist";
	}
	@RequestMapping("/viewstudent")
	public String viewStudent(Model model, @RequestParam(name = "facultyviewstudent") Optional<Integer> searchid) {
		int search = searchid.orElse(0);
		model.addAttribute("faculty", faculty);
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
			ArrayList<Grade> grades = new ArrayList<Grade>();
			for (CourseApplication ca : courseApplications) {
				grades.add(grepo.findByCourseIdStudentId(ca.getCourse().getCourseId(), ca.getStudent().getStudentId()));
			}


			model.addAttribute("grades", grades);
			
		}
		return "facultyviewstudent";
	}
	
	@GetMapping("/viewcourse")
	public String viewCourse(Model model, @RequestParam(name = "facultyviewcourse") Optional<Integer> searchid,
			@RequestParam(name = "page") Optional<Integer> page, 
			@RequestParam(name = "size") Optional<Integer> size) {
		int search = searchid.orElse(0);
		model.addAttribute("faculty", faculty);
		if (search == 0) {
			String msg = "null";
			model.addAttribute("msg", msg);
		} else {
			Course course = crepo.getCourseByCourseIdAndFacultyId(search, faculty.getFacultyId());
			model.addAttribute("course", course);
			String msg = "found";
			model.addAttribute("msg", msg);
			List<CourseApplication> courseApplications = carepo.findByCourseIdAndFacultyId(search, faculty.getFacultyId());
			if (courseApplications.isEmpty()) {
				model.addAttribute("searchstudent", "null");
			} else {
				model.addAttribute("searchstudent", "found");
			}
			model.addAttribute("courseApplications", courseApplications);
			ArrayList<Grade> grades = new ArrayList<Grade>();
			for (CourseApplication ca : courseApplications) {
				grades.add(grepo.findByCourseIdStudentId(ca.getCourse().getCourseId(), ca.getStudent().getStudentId()));

			}

			int currentpage = page.orElse(1);
			int pagesize = size.orElse(5);
			Page<Grade> gradePage = gserv.findPaginatedGrade
					(PageRequest.of(currentpage - 1, pagesize),(grades));
	        model.addAttribute("gradePage", gradePage);

	        this.course = course;
	        this.gradePage = gradePage;
	        
	        int totalPages = gradePage.getTotalPages();
	        if (totalPages > 0) {
	            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
	                .boxed()
	                .collect(Collectors.toList());
	            model.addAttribute("pageNumbers", pageNumbers);
	        }
			model.addAttribute("grades", grades);
			
		}
		return "facultyviewcourse";
	}
	
	@RequestMapping("/searchstudent")
	public String searchStudent(Model model, @RequestParam(name = "searchstudent") String searchinput, @RequestParam(name = "course") Integer courseId, @RequestParam(name = "page") Optional<Integer> page, 
			@RequestParam(name = "size") Optional<Integer> size) {
		model.addAttribute("faculty", faculty);
		List<CourseApplication> casearch = caserv.getApprovedAndLikeStudentName(courseId, searchinput);
		
		if (casearch.isEmpty()) {
			model.addAttribute("searchstudent", "null");
			model.addAttribute("msg", "null");
		}
		else {
			model.addAttribute("msg", "found");
			model.addAttribute("searchstudent", "found");
			ArrayList<Grade> grades = new ArrayList<Grade>();
			for (CourseApplication ca : casearch) {
				grades.add(grepo.findByCourseIdStudentId(ca.getCourse().getCourseId(), ca.getStudent().getStudentId()));
			};
			int currentpage = page.orElse(1);
			int pagesize = size.orElse(5);
			Page<Grade> gradePage = gserv.findPaginatedGrade
					(PageRequest.of(currentpage - 1, pagesize),(grades));
	        model.addAttribute("gradePage", gradePage);
	        
	        int totalPages = gradePage.getTotalPages();
	        if (totalPages > 0) {
	            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
	                .boxed()
	                .collect(Collectors.toList());
	            model.addAttribute("pageNumbers", pageNumbers);
	        }
		}
		Course course = crepo.getCourseByCourseId(courseId);
		model.addAttribute("course", course);
		return "facultyviewcourse";
	}
	
}
