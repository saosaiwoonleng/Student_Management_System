package sg.edu.nus.catest2.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import sg.edu.nus.catest2.repo.*;
import sg.edu.nus.catest2.model.*;

@Service
public class CourseApplicationService {
	@Autowired
	CourseApplicationRepository carepo;
	
	public List<CourseApplication> getPendingCourseApplications(){
		List<CourseApplication> list = carepo.findAll();
		List<CourseApplication> courseApplications = new ArrayList<CourseApplication>();
		for(CourseApplication ca: list) {
			if(ca.getStatus().equals("Pending")) {
				courseApplications.add(ca);
			}
		}
		return courseApplications;
	}
	
	public List<CourseApplication> getApprovedCourseApplications(){
		List<CourseApplication> list = carepo.findAll();
		List<CourseApplication> courseApplications = new ArrayList<CourseApplication>();
		for(CourseApplication ca: list) {
			if(ca.getStatus().equals("Approved")) {
				courseApplications.add(ca);
			}
		}
		return courseApplications;
	}
	
	public List<CourseApplication> getRejectedCourseApplications(){
		List<CourseApplication> list = carepo.findAll();
		List<CourseApplication> courseApplications = new ArrayList<CourseApplication>();
		for(CourseApplication ca: list) {
			if(ca.getStatus().equals("Rejected")) {
				courseApplications.add(ca);
			}
		}
		return courseApplications;
	}
	
	public Page<CourseApplication> findPaginatedCourseApplication(Pageable pageable,
			List<CourseApplication> courseApplications) {
		int pageSize = pageable.getPageSize();
		int currentPage = pageable.getPageNumber();
		int startItem = currentPage * pageSize;
		List<CourseApplication> list;

		if (courseApplications.size() < startItem) {
			list = Collections.emptyList();
		} else {
			int toIndex = Math.min(startItem + pageSize, courseApplications.size());
			list = courseApplications.subList(startItem, toIndex);
		}

		Page<CourseApplication> courseApplicationPage = new PageImpl<CourseApplication>(list, PageRequest.of(currentPage, pageSize),
				courseApplications.size());

		return courseApplicationPage;
	}
	
	public List<CourseApplication> getCourseApplicationsByStudentId(int studentId) {
		// TODO Auto-generated method stub
		return carepo.getCourseApplicationsByStudentId(studentId);
	}

	public List<CourseApplication> getApprovedCourseByStudentId(int studentId) {
		// TODO Auto-generated method stub
		return carepo.getApprovedCourseByStudentId(studentId);
	}

	public List<CourseApplication> getPendingCourseByStudentId(int studentId) {
		// TODO Auto-generated method stub
		return carepo.getPendingCourseByStudentId(studentId);
	}

	public List<CourseApplication> findAll() {
		// TODO Auto-generated method stub
		return carepo.findAll();
	}

	public CourseApplication findByCourseAndStudent(int courseId, int sessionId) {
		// TODO Auto-generated method stub
		return carepo.findByCourseAndStudent(courseId,sessionId);
	}

	public void save(CourseApplication ca) {
		// TODO Auto-generated method stub
		carepo.save(ca);
	}

	public Optional<CourseApplication> findById(int caId) {
		// TODO Auto-generated method stub
		return carepo.findById(caId);
	}

	public void deleteById(int caid) {
		// TODO Auto-generated method stub
		carepo.deleteById(caid);
	}

	public List<CourseApplication> getRejectCourseByStudentId(int studentId) {
		// TODO Auto-generated method stub
		return carepo.getRejectCourseByStudentId(studentId);
	}
	
	public List<CourseApplication> getApprovedAndLikeStudentName(int courseId, String search){
		List<CourseApplication> ca1 = carepo.searchStudentName(search);
		List<CourseApplication> ca2 = new ArrayList<>();
		for(CourseApplication ca : ca1) {
			if(ca.getCourse().getCourseId() == courseId && ca.getStatus().equals("Approved")) {
				ca2.add(ca);
			}
		}
		return ca2;
	}

}
