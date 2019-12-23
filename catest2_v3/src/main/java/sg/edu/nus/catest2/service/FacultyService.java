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

import sg.edu.nus.catest2.model.Faculty;
import sg.edu.nus.catest2.model.FacultyLeave;
import sg.edu.nus.catest2.model.Student;
import sg.edu.nus.catest2.repo.FacultyRepository;

@Service
public class FacultyService {
	@Autowired
	FacultyRepository frepo;
	
	public void save(@Valid Faculty faculty) {
		frepo.save(faculty);
	}
	
	public Page<Faculty> findPaginatedFaculty(Pageable pageable,List<Faculty> facultylist) {
		List<Faculty> faculties = facultylist;
		int pageSize = pageable.getPageSize();
		int currentPage = pageable.getPageNumber();
		int startItem = currentPage * pageSize;
		List<Faculty> list;

		if (faculties.size() < startItem) {
			list = Collections.emptyList();
		} else {
			int toIndex = Math.min(startItem + pageSize, faculties.size());
			list = faculties.subList(startItem, toIndex);
		}

		Page<Faculty> facultyPage = new PageImpl<Faculty>(list, PageRequest.of(currentPage, pageSize),
				faculties.size());

		return facultyPage;
	}
	
	public List<Faculty> searchFacultyName(String name){
		List<Faculty> faculties = frepo.findAll();
		List<Faculty> filtered = new ArrayList<>();
		for(Faculty f: faculties) {
			if(f.getFirstName().toLowerCase().equals(name.toLowerCase())) {
				filtered.add(f);
			}
			else if(f.getMiddleName().toLowerCase().equals(name.toLowerCase())) {
				filtered.add(f);
			}
			else if(f.getSurname().toLowerCase().equals(name.toLowerCase())) {
				filtered.add(f);
			}
			else {
				continue;
			}
		}
		return filtered;
	}

	
	public Faculty getFacultyByFacultyId(int fId) {
		// TODO Auto-generated method stub
		return frepo.getByFacultyId(fId);
	}

}
