package sg.edu.nus.catest2.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import sg.edu.nus.catest2.model.Grade;
import sg.edu.nus.catest2.model.Student;
import sg.edu.nus.catest2.repo.GradeRepository;
import sg.edu.nus.catest2.repo.StudentRepository;

@Service
public class StudentService {
	
	@Autowired
	StudentRepository srepo;
	@Autowired
	GradeRepository grepo;
	
	public Page<Student> findPaginatedStudent(Pageable pageable, List<Student> studentlist) {
		List<Student> students = studentlist;
		int pageSize = pageable.getPageSize();
		int currentPage = pageable.getPageNumber();
		int startItem = currentPage * pageSize;
		List<Student> list;

		if (students.size() < startItem) {
			list = Collections.emptyList();
		} else {
			int toIndex = Math.min(startItem + pageSize, students.size());
			list = students.subList(startItem, toIndex);
		}

		Page<Student> studentPage = new PageImpl<Student>(list, PageRequest.of(currentPage, pageSize),
				students.size());

		return studentPage;
	}
	
	public List<Student> searchStudentName(String name){
		List<Student> students = srepo.findAll();
		List<Student> filtered = new ArrayList<>();
		for(Student s: students) {
			if(s.getFirstName().toLowerCase().equals(name.toLowerCase())) {
				filtered.add(s);
			}
			else if(s.getMiddleName().toLowerCase().equals(name.toLowerCase())) {
				filtered.add(s);
			}
			else if(s.getSurname().toLowerCase().equals(name.toLowerCase())) {
				filtered.add(s);
			}
			else {
				continue;
			}
		}
		return filtered;
	}
	
	public void save(@Valid Student student) {
		// TODO Auto-generated method stub
		srepo.save(student);
	}

	public Student getStudentByStudentId(int studentId) {
		// TODO Auto-generated method stub
		return srepo.getStudentByStudentId(studentId);
	}

	public List<Grade> getGradesByStudentId(int stuId) {
		// TODO Auto-generated method stub
		return grepo.getGradesByStudentId(stuId);
	}

}
