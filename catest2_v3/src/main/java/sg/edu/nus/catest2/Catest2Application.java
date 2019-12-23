package sg.edu.nus.catest2;

import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import sg.edu.nus.catest2.model.*;
import sg.edu.nus.catest2.repo.*;


@SpringBootApplication
public class Catest2Application {

	private static final Logger log = LoggerFactory.getLogger(Catest2Application.class);

	public static void main(String[] args) {
		SpringApplication.run(Catest2Application.class, args);
	
	}

	@Bean
	public CommandLineRunner dept(FacultyRepository frepo , FacultyLeaveRepository flrepo,
			DepartmentRepository drepo,CourseRepository crepo,StudentRepository srepo,
			CourseApplicationRepository carepo,GradeRepository grepo,AdminRepository arepo,UserRepository urepo) {
		return (args) -> {
			
			if(urepo.findAll().isEmpty()) {
				Admin admin1 = new Admin("First", "Admin", "Ever", 91902484, "first_admin@gmail.com");
				Admin admin2 = new Admin("Second","Admin","Ever",90905678,"second_admin@gmail.com");
				arepo.save(admin1);
				arepo.save(admin2);
				admin1.setAdminId(1);
				admin2.setAdminId(2);
				
				User user1 = new User(0, 0, 1, "admin1", "admin1");
				User user2 = new User(0, 0, 2, "admin2", "admin2");
				urepo.save(user1);
				urepo.save(user2);
				user1.setUserId(3);
				user2.setUserId(4);
				
				Department department1 = new Department(1, "Mathematics");
				Department department2 = new Department(2,"Science");
				Department department3 = new Department(3,"Information Technology");
				drepo.save(department1);
				drepo.save(department2);
				drepo.save(department3);
				
				Faculty faculty1 = new Faculty("Math", "Lecturer", "Chan", "ilovemath@gmail.com", 82135698, department1);
				Faculty faculty2 = new Faculty("Science", "Lecturer", "Ong", "sciencerocks@gmail.com", 90672354, department2);
				Faculty faculty3 = new Faculty("IT", "Lecturer", "Lim", "ITisthebest@gmail.com", 96783745, department3);
				frepo.save(faculty1);
				frepo.save(faculty2);
				frepo.save(faculty3);
				faculty1.setFacultyId(5);
				faculty2.setFacultyId(6);
				faculty3.setFacultyId(7);
				
				User user3 = new User(0, 5, 0, "faculty1", "faculty1");
				User user4 = new User(0, 6, 0, "faculty2", "faculty2");
				User user5 = new User(0, 7, 0, "faculty3", "faculty3");
				urepo.save(user3);
				urepo.save(user4);
				urepo.save(user5);
				user3.setUserId(8);
				user4.setUserId(9);
				user5.setUserId(10);
				
				String sdate1 = "2019-08-15";
				String sdate2 = "2019-10-15";
				String sdate3 = "2019-11-15";
				String sdate4 = "2019-12-15";
				LocalDate date1 = LocalDate.parse(sdate1);
				LocalDate date2 = LocalDate.parse(sdate2);
				LocalDate date3 = LocalDate.parse(sdate3);
				LocalDate date4 = LocalDate.parse(sdate4);
				
				Course c1 = new Course("MATH101", "Basic Maths", date1, date2, 4, 2, department1, faculty1);
				Course c2 = new Course("MATH202", "Intermediate Maths", date3, date4, 6, 2, department1, faculty1);
				Course c3 = new Course("SCI101", "Basic Science", date1, date2, 4, 2, department2, faculty2);
				Course c4 = new Course("SCI202", "Intermediate Science", date3, date4, 6, 2, department2, faculty2);
				Course c5 = new Course("IT101", "Basic Programming", date1, date2, 4, 2, department3, faculty3);
				Course c6 = new Course("IT202", "Intermediate Science", date3, date4, 6, 2, department3, faculty3);
				crepo.save(c1);
				crepo.save(c2);
				crepo.save(c3);
				crepo.save(c4);
				crepo.save(c5);
				crepo.save(c6);
				c1.setCourseId(11);
				c2.setCourseId(12);
				c3.setCourseId(13);
				c4.setCourseId(14);
				c5.setCourseId(15);
				c6.setCourseId(16);		
				
				Student s1 = new Student("John", "Lennon", "Tan", 95678493, "Dover Road", "john69@gmail.com", "M", 19);
				Student s2 = new Student("Janet","Louise","Chan",86759045,"Clementi Road","janet567@yahoo.com","F", 23);
				Student s3 = new Student("Ken", "Beary", "Wong", 95341493, "Tiong Bahru Road", "kennyboy@gmail.com", "M", 21);
				srepo.save(s1);
				srepo.save(s2);
				srepo.save(s3);
				s1.setStudentId(17);
				s2.setStudentId(18);
				s3.setStudentId(19);
				
				User user6 = new User(17, 0, 0, "student1", "student1");
				User user7 = new User(18, 0, 0, "student2", "student2");
				User user8 = new User(19, 0, 0, "student3", "student3");
				urepo.save(user6);
				urepo.save(user7);
				urepo.save(user8);
				user6.setUserId(20);
				user7.setUserId(21);
				user8.setUserId(22);

				CourseApplication ca1 = new CourseApplication(s1, c1, "Pending");
				CourseApplication ca2 = new CourseApplication(s1,c2,"Approved");
				CourseApplication ca3 = new CourseApplication(s1,c3,"Pending");
				CourseApplication ca4 = new CourseApplication(s2,c1,"Pending");
				CourseApplication ca5 = new CourseApplication(s2,c2,"Pending");
				CourseApplication ca6 = new CourseApplication(s2,c3,"Pending");
				CourseApplication ca7 = new CourseApplication(s3,c1,"Rejected");
				CourseApplication ca8 = new CourseApplication(s3,c2,"Pending");
				CourseApplication ca9 = new CourseApplication(s3,c3,"Pending");
				carepo.save(ca1);
				carepo.save(ca2);
				carepo.save(ca3);
				carepo.save(ca4);
				carepo.save(ca5);
				carepo.save(ca6);
				carepo.save(ca7);
				carepo.save(ca8);
				carepo.save(ca9);
				ca1.setApplicationId(23);
				ca2.setApplicationId(24);
				ca3.setApplicationId(25);
				ca4.setApplicationId(26);
				ca5.setApplicationId(27);
				ca6.setApplicationId(28);
				ca7.setApplicationId(29);
				ca8.setApplicationId(30);
				ca9.setApplicationId(31);
				
				Grade grade1 = new Grade(s1, c2, faculty1, "Null");
				grepo.save(grade1);
				grade1.setGradeId(32);
				
				FacultyLeave fl1 = new FacultyLeave(faculty1, date2, date4, "Pending");
				FacultyLeave fl2 = new FacultyLeave(faculty2, date3, date4, "Approved");
				FacultyLeave fl3 = new FacultyLeave(faculty3, date1, date3, "Pending");
				flrepo.save(fl1);
				flrepo.save(fl2);
				flrepo.save(fl3);
		
			}
			
			
			
		};

	}
	
}
