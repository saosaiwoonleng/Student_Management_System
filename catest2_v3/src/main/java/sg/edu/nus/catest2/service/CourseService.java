package sg.edu.nus.catest2.service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import sg.edu.nus.catest2.model.Course;
import sg.edu.nus.catest2.repo.CourseRepository;

@Service
public class CourseService {
	
	@Autowired
	CourseRepository crepo;
	
	public Page<Course> findPaginatedCourse(Pageable pageable,List<Course> courselist) {
		List<Course> courses = courselist;
		int pageSize = pageable.getPageSize();
		int currentPage = pageable.getPageNumber();
		int startItem = currentPage * pageSize;
		List<Course> list;

		if (courses.size() < startItem) {
			list = Collections.emptyList();
		} else {
			int toIndex = Math.min(startItem + pageSize, courses.size());
			list = courses.subList(startItem, toIndex);
		}

		Page<Course> coursePage = new PageImpl<Course>(list, PageRequest.of(currentPage, pageSize),
				courses.size());

		return coursePage;
	}
	
	public Collection<? extends Course> findAll() {
		// TODO Auto-generated method stub
		return crepo.findAll();
	}

	public Optional<Course> findById(int courseId) {
		// TODO Auto-generated method stub
		return crepo.findById(courseId);
	}

	public Course getCourseByCourseId(int courseId) {
		// TODO Auto-generated method stub
		return crepo.getCourseByCourseId(courseId);
	}

}
