package sg.edu.nus.catest2.controller;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import sg.edu.nus.catest2.model.Course;
import sg.edu.nus.catest2.model.CourseApplication;
import sg.edu.nus.catest2.model.Grade;
import sg.edu.nus.catest2.model.Student;
import sg.edu.nus.catest2.mvcmodel.Session;
import sg.edu.nus.catest2.repo.CourseApplicationRepository;
import sg.edu.nus.catest2.repo.CourseRepository;
import sg.edu.nus.catest2.repo.GradeRepository;
import sg.edu.nus.catest2.repo.StudentRepository;
import sg.edu.nus.catest2.service.CourseApplicationService;
import sg.edu.nus.catest2.service.CourseService;
import sg.edu.nus.catest2.service.GradeService;
import sg.edu.nus.catest2.service.StudentService;

@Controller
@RequestMapping("/student")
@SessionAttributes("usersession")
public class StudentController {

	private final StudentService stuservice;
	@Autowired
	StudentRepository stuRepo;
	@Autowired
	CourseApplicationService caservice;
	@Autowired
	CourseApplicationRepository carepo;
	@Autowired
	CourseService courservice;
	@Autowired
	CourseRepository courserepo;
	@Autowired
	GradeService gservice;
	@Autowired
	GradeRepository grepo;
	
	public static Student student;

	public StudentController(StudentService stuservice, StudentRepository stuRepo, CourseApplicationService caservice,
			CourseApplicationRepository carepo, CourseService courservice, CourseRepository courserepo) {
		super();
		this.stuservice = stuservice;
		this.stuRepo = stuRepo;
		this.caservice = caservice;
		this.carepo = carepo;
		this.courservice = courservice;
		this.courserepo = courserepo;
	}

	//Personal Information of Student
	@GetMapping("/info")
	public String studentInfo(Model model,@SessionAttribute("usersession") Session session){
		System.out.print("Student Info Method with "+session);
		
		student = stuRepo.getStudentByStudentId(session.getSessionId());
		model.addAttribute("student", student);
		
		student=stuRepo.findById(student.getStudentId()).get();
		if(student!=null) {
			model.addAttribute("student",student);
		}	
		//return "studentlistAPP";
		return "redirect:/student/home";
	}
	
	// Retrieve all applied courses of login Student
	@GetMapping("/home")
	public String studentHome(Model model,@SessionAttribute("usersession") Session session,
								@RequestParam(name = "page") Optional<Integer> page, 
								@RequestParam(name = "size") Optional<Integer> size,
								@RequestParam(name = "sort") Optional<String> sort){
		
		if(session.getSessionId() == 0) {
			return "redirect:/login/welcome";
		}
		int currentpage = page.orElse(1);
		int pagesize = size.orElse(5);
		String sorting = sort.orElse("all");
		
		System.out.print("Student Info Method with "+session+"CHECK SORT:"+sorting);
		student = stuRepo.getStudentByStudentId(session.getSessionId());
		model.addAttribute("student", student);
		
		student=stuRepo.findById(student.getStudentId()).get();
		if(student!=null) {
			model.addAttribute("student",student);
		}	
		
		
		model.addAttribute("sort", sorting);
		List<CourseApplication> courseLists= caservice.getCourseApplicationsByStudentId(student.getStudentId());
		model.addAttribute("courseLists", courseLists);

		Page<CourseApplication> courseApplicationPage = caservice.findPaginatedCourseApplication
				(PageRequest.of(currentpage - 1, pagesize),courseLists);
        model.addAttribute("courseApplicationPage", courseApplicationPage);
        
        
 
        int totalPages = courseApplicationPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                .boxed()
                .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        return "courseapplicationlist_Stu";
       		
	}
	//All course Application list
	@RequestMapping("/courseapplicationlist")
	public String courseApplicationlist(
			Model model,
			@RequestParam(name = "page") Optional<Integer> page, 
			@RequestParam(name = "size") Optional<Integer> size,
			@RequestParam(name = "sort") Optional<String> sort) {
		
		model.addAttribute("student", student);
		student=stuRepo.findById(student.getStudentId()).get();
		System.out.println("All course Application list:Start!!!\n Session of Student:"+student);
		
		if(student!=null) {
			model.addAttribute("student",student);
		}	
		
		
		int currentpage = page.orElse(1);
		int pagesize = size.orElse(5);
		String sorting = sort.orElse("all");
		
		if(sorting.equals("approved")) {
			
			System.out.print("CHECK SORT:"+sorting);
			
			model.addAttribute("sort", sorting);
			List<CourseApplication> courseApplications = caservice.getApprovedCourseByStudentId(student.getStudentId());
			System.out.println("**Reterive getApprovedCourseByStudentId**:"+courseApplications);
			
			model.addAttribute("courseApplications", courseApplications);
			
			System.out.println("Sorting value:"+courseApplications);
			
			Page<CourseApplication> courseApplicationPage = caservice.findPaginatedCourseApplication
					(PageRequest.of(currentpage - 1, pagesize),courseApplications);
	        model.addAttribute("courseApplicationPage", courseApplicationPage);
	 
	        int totalPages = courseApplicationPage.getTotalPages();
	        if (totalPages > 0) {
	            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
	                .boxed()
	                .collect(Collectors.toList());
	            model.addAttribute("pageNumbers", pageNumbers);
	        }
	        return "courseapplicationlist_Stu";
		}
		else if(sorting.equals("pending")) {
			System.out.print("CHECK SORT:"+sorting);
			
			model.addAttribute("sort", sorting);
			List<CourseApplication> courseApplications = caservice.getPendingCourseByStudentId(student.getStudentId());
			model.addAttribute("courseApplications", courseApplications);
			
			Page<CourseApplication> courseApplicationPage = caservice.findPaginatedCourseApplication
					(PageRequest.of(currentpage - 1, pagesize),courseApplications);
	        model.addAttribute("courseApplicationPage", courseApplicationPage);
	 
	        int totalPages = courseApplicationPage.getTotalPages();
	        if (totalPages > 0) {
	            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
	                .boxed()
	                .collect(Collectors.toList());
	            model.addAttribute("pageNumbers", pageNumbers);
	        }
	        return "courseapplicationlist_Stu";
		}
		else if(sorting.equals("rejected")) {
			System.out.print("CHECK SORT:"+sorting);
			
			model.addAttribute("sort", sorting);
			List<CourseApplication> courseApplications = caservice.getRejectCourseByStudentId(student.getStudentId());
			model.addAttribute("courseApplications", courseApplications);
			
			Page<CourseApplication> courseApplicationPage = caservice.findPaginatedCourseApplication
					(PageRequest.of(currentpage - 1, pagesize),courseApplications);
	        model.addAttribute("courseApplicationPage", courseApplicationPage);
	 
	        int totalPages = courseApplicationPage.getTotalPages();
	        if (totalPages > 0) {
	            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
	                .boxed()
	                .collect(Collectors.toList());
	            model.addAttribute("pageNumbers", pageNumbers);
	        }
	        return "courseapplicationlist_Stu";
		}
		else {
			model.addAttribute("sort", sorting);
			System.out.print("CHECK SORT:"+sorting);
						
			List<CourseApplication> courseApplications = carepo.findAll();
			model.addAttribute("courseApplications", courseApplications);
			Page<CourseApplication> courseApplicationPage = caservice.findPaginatedCourseApplication
					(PageRequest.of(currentpage - 1, pagesize),courseApplications);
	        model.addAttribute("courseApplicationPage", courseApplicationPage);
	 
	        int totalPages = courseApplicationPage.getTotalPages();
	        if (totalPages > 0) {
	            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
	                .boxed()
	                .collect(Collectors.toList());
	            model.addAttribute("pageNumbers", pageNumbers);
	        }
	        return "redirect:/student/home";
		}
	}
		@GetMapping("/allCourses")
		public String courselist(
				Model model,@RequestParam(name = "page") Optional<Integer> page, 
				@RequestParam(name = "size") Optional<Integer> size,
				@RequestParam(name = "sort") Optional<String> sort) {

			int currentpage = page.orElse(1);
			int pagesize = size.orElse(5);
			String sorting = sort.orElse("all");
			System.out.println("**Status of Sorting:**"+sorting);
			model.addAttribute("student", student);
			List<CourseApplication> courseApplications = (List<CourseApplication>) caservice.findAll();
			
			System.out.println("**CHECK courseApplications**"+courseApplications);
			model.addAttribute("courseApplications", courseApplications);
			Page<CourseApplication> courseApplicationPage = caservice.findPaginatedCourseApplication
					(PageRequest.of(currentpage - 1, pagesize),courseApplications);
	        model.addAttribute("courseApplicationPage", courseApplicationPage);
	        
	        System.out.println("**CHECK courseApplicationPage**"+courseApplicationPage);
	        
	        int totalPages = courseApplicationPage.getTotalPages();
	        if (totalPages > 0) {
	            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
	                .boxed()
	                .collect(Collectors.toList());
	            model.addAttribute("pageNumbers", pageNumbers);
	        }
	        
	        return "courseapplicationlist_Stu";
		}
		
		@GetMapping("/edit/{id}")
		public String EditForm(Model model,@PathVariable("id") Integer id) {
			Student stu =stuRepo.findById(id).get();
			model.addAttribute("student",stu);
			return "studentEditForm";
		
		}

		@PostMapping("/save")
		public String saveCourseApplication(@Valid Student student, BindingResult bindingResult,@SessionAttribute("usersession") Session session) {
			if (bindingResult.hasErrors()) {
				return "studentEditForm";
			}else {
				student.setStudentId(session.getSessionId());
				this.stuservice.save(student);
				return "redirect:/student/home";
			}

		}
		
		
		@GetMapping("/list")
		public String listAll(Model model) {

			ArrayList<Course> clist=new ArrayList<Course>();
			clist=(ArrayList<Course>) courservice.findAll();
			ArrayList<Course> availableCourses=new ArrayList<Course>();
			
			ArrayList<CourseApplication> calist=new ArrayList<CourseApplication>();
			calist=(ArrayList<CourseApplication>) caservice.findAll();
			
			for(Course c:clist) {
				int stucount=0;
				for(CourseApplication ca:calist) {
					if(ca.getCourse()==c && ca.getStatus().equals("Approved")) {
						stucount+=1;
					}
					//System.out.println("CourseApplication in Course loop"+stucount);
				}
				System.out.println("Student Count in Course loop"+stucount);
				if(stucount<c.getCourseSize()) {
					availableCourses.add(c);
				}
			}
			System.out.println("Available Course"+availableCourses);
					
			model.addAttribute("student",student);
			model.addAttribute("courseLists",availableCourses);
			return "studentCourseForm";
		}
		
		
		@GetMapping("/courseDetails/{courseId}")
		public String courseDetails(Model model,
									@PathVariable("courseId") int courseId,
									@SessionAttribute("usersession") Session session){
			
			System.out.print("Coures Info Method with "+courseId);
			String msg=null;
			
			model.addAttribute("student", student);
			
			Course course=courservice.findById(courseId).get();
			if(course!=null) {
				model.addAttribute("courseDetail",course);
			}				
			
			CourseApplication checkApplied = caservice.findByCourseAndStudent(course.getCourseId(),session.getSessionId());
			System.out.println("CHECK StuID:"+session.getSessionId());
			System.out.println("CHECK CouresID:"+checkApplied);
			
			if(checkApplied==null) {
								
				msg="apply";
				model.addAttribute("msg",msg);
				System.out.println("Message CHECK:"+msg);
				return "studentCourseDetail";
			}else {
				msg="already";
				model.addAttribute("msg",msg);
				System.out.println("Message CHECK:"+msg);
				return "studentCourseDetail";
			}
						
		}
		
		
		
		@PostMapping("/appliedCourse/{courseId}")
		public String appliedCourse(Model model,@PathVariable("courseId") int courseId,
								@SessionAttribute("usersession") Session session) {
			System.out.println("**Applied Course Method:**"+courseId);
			String msg=null;
			Course c=courservice.getCourseByCourseId(courseId);
			Student s=stuservice.getStudentByStudentId(session.getSessionId());
			
			CourseApplication ca=new CourseApplication();
			ca.setCourse(c);
			ca.setStudent(s);;
			ca.setStatus("Pending");
		
			model.addAttribute("student", student);
			caservice.save(ca);
			System.out.println(caservice);
			String stuMail=s.getEmail();
			//Calling gmail method
			sendEmail(stuMail,courseId);
			return "redirect:/student/courseapplicationlist";
		}
		
		@RequestMapping("/calculateGpa/{stuId}")
		//public String StudentGpa(Model model,@PathVariable("stuId") int stuId) {
		public String StudentGpa(Model model,@PathVariable("stuId") int stuId,
				@SessionAttribute("usersession") Session session) {
			
			ArrayList<Grade> gradelist=(ArrayList<Grade>) stuservice.getGradesByStudentId(stuId);
			
			 String  grade[]= new String[]{"A+","A","A-","B+","B","B-","C+","C","D+","D","F"};
			 double gradept[]=new double[] {5.0,5.0,4.5,4.0,3.5,3.0,2.5,2.0,1.5,1.0,0};
			
			 int courseunit=0;			 		
			 double totalGradept=0.0;
			 double Gpa= 0.0 ;
			 double totalUnit=0;
			 
			 for(int i = 0; i<gradelist.size();i++) {
				 
				 if(!gradelist.get(i).getGrade().equals("Null")) {
					 courseunit=gradelist.get(i).getCourse().getCourseUnit();
					 totalUnit+=courseunit;	
					 System.out.println("Course Unit"+courseunit);
					 System.out.println("Gradelist"+gradelist.get(i).getGrade());
					 
						 for(int j=0;j<grade.length;j++) {
							 if(gradelist.get(i).getGrade().equals(grade[j])) {
								 System.out.println("Grade Point in j loop:"+gradept[j]);
								 totalGradept+=courseunit*gradept[j];
								 System.out.println("TotalGrade Point in j loop:"+totalGradept);
						}
					}
				 }
				
				 				 			 
			 }
			 System.out.println("CHECK GPA totalGradept"+totalGradept);
			 System.out.println("CHECK totalUnit"+totalUnit);
			
			 DecimalFormat df2 = new DecimalFormat("#.##");
			
			 Gpa=(totalGradept/totalUnit);
			 Gpa=Double.parseDouble(df2.format(Gpa));
			 
			 System.out.println("CHECK GPA"+Gpa);
			 model.addAttribute("Gpa", Gpa);
			 model.addAttribute("student",student);
			 model.addAttribute("gradeLists",gradelist);
			 
			 if(session.getUserType().equals("admin")) {
				 model.addAttribute("type", "admin");
				 Student temp = stuservice.getStudentByStudentId(stuId);
				 model.addAttribute("student", temp);
			 }
			 else if(session.getUserType().equals("faculty")) {
				 model.addAttribute("type", "faculty");
				 Student temp = stuservice.getStudentByStudentId(stuId);
				 model.addAttribute("student", temp);
			 }
			 else {
				 model.addAttribute("type", "student");
			 }
			 return "studentEduRec";
		}
		
		@GetMapping("/eduRec/{stuId}")
		public String StudentEduRecord(Model model,@PathVariable("stuId") int stuId) {
			
			System.out.println("Student EduRecord:");
			ArrayList<Grade> gradeList=(ArrayList<Grade>) stuservice.getGradesByStudentId(stuId);
			model.addAttribute("student",student);
			model.addAttribute("gradeLists",gradeList);
						
			return "studentEduRec";
		}
		@GetMapping("/deleteCoures/{caId}")
		public String deleteCoures(Model model,@PathVariable("caId") int caId) {
			
			caservice.deleteById(caId);
			model.addAttribute("student",student);
			return "redirect:/student/home";
		}
		
		@Autowired
	    private JavaMailSender javaMailSender;
		
		void sendEmail(String stuMail,int courseId) {
			

	        SimpleMailMessage msg = new SimpleMailMessage();
	        
	        msg.setFrom("no-reply@national.best.uni.com");
	        System.out.println("SetForm CHECK****"+msg);
	        msg.setSubject("You Have Successfully Applied the New Course.");
	        msg.setTo(stuMail);
	        Course course=courservice.getCourseByCourseId(courseId);
	        msg.setText("Course Detail\r\n"
	        		+"Course Code:\t" + course.getCourseCode()+"\r\n"
	        		+"Course Name:\t" + course.getCourseName()+"\r\n"
	        		+"Course StartDate:"+ course.getCourseStart()+"\r\n"
	        		+"Course EndDate:\t"+course.getCourseEnd()+"\r\n");
	        System.out.println("SetForm CHECK befor send****"+msg);
	        javaMailSender.send(msg);

	    }
	
		
		
}
