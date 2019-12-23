package sg.edu.nus.catest2.service;

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
import sg.edu.nus.catest2.repo.GradeRepository;

@Service
public class GradeService {
	@Autowired
	GradeRepository grepo;
	
	public Page<Grade> findPaginatedGrade(Pageable pageable,List<Grade> gradelist) {
		List<Grade> grades = gradelist;
		int pageSize = pageable.getPageSize();
		int currentPage = pageable.getPageNumber();
		int startItem = currentPage * pageSize;
		List<Grade> list;

		if (grades.size() < startItem) {
			list = Collections.emptyList();
		} else {
			int toIndex = Math.min(startItem + pageSize, grades.size());
			list = grades.subList(startItem, toIndex);
		}

		Page<Grade> gradePage = new PageImpl<Grade>(list, PageRequest.of(currentPage, pageSize),
				grades.size());

		return gradePage;
	}
	
	//reo
	
	public void save(@Valid Grade grade) {
		grepo.save(grade);
	}
	
	public void findById(int gid) {
		// TODO Auto-generated method stub
		grepo.findById(gid);
		
	}

}
