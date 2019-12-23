package sg.edu.nus.catest2.service;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import sg.edu.nus.catest2.model.Admin;
import sg.edu.nus.catest2.repo.AdminRepository;

@Service
public class AdminService {
	
	@Autowired 
	AdminRepository arepo;
	
	public Page<Admin> findPaginatedAdmin(Pageable pageable,List<Admin> adminlist) {
		List<Admin> admins = adminlist;
		int pageSize = pageable.getPageSize();
		int currentPage = pageable.getPageNumber();
		int startItem = currentPage * pageSize;
		List<Admin> list;

		if (admins.size() < startItem) {
			list = Collections.emptyList();
		} else {
			int toIndex = Math.min(startItem + pageSize, admins.size());
			list = admins.subList(startItem, toIndex);
		}

		Page<Admin> adminPage = new PageImpl<Admin>(list, PageRequest.of(currentPage, pageSize),
				admins.size());

		return adminPage;
	}

}
