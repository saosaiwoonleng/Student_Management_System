package sg.edu.nus.catest2.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import sg.edu.nus.catest2.repo.*;
import sg.edu.nus.catest2.model.*;
import java.util.*;

@Service
public class DepartmentService {

	@Autowired
	DepartmentRepository drepo;

	public Page<Department> findPaginatedDepartment(Pageable pageable,List<Department> dlist) {
		List<Department> departments = dlist;
		int pageSize = pageable.getPageSize();
		int currentPage = pageable.getPageNumber();
		int startItem = currentPage * pageSize;
		List<Department> list;

		if (departments.size() < startItem) {
			list = Collections.emptyList();
		} else {
			int toIndex = Math.min(startItem + pageSize, departments.size());
			list = departments.subList(startItem, toIndex);
		}

		Page<Department> departmentPage = new PageImpl<Department>(list, PageRequest.of(currentPage, pageSize),
				departments.size());

		return departmentPage;
	}

}
