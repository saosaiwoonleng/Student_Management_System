package sg.edu.nus.catest2.service;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import sg.edu.nus.catest2.model.User;
import sg.edu.nus.catest2.repo.UserRepository;

@Service
public class UserService {
	
	@Autowired
	UserRepository urepo;
	
	public Page<User> findPaginatedUser(Pageable pageable, List<User> userlist) {
		List<User> users = userlist;
		int pageSize = pageable.getPageSize();
		int currentPage = pageable.getPageNumber();
		int startItem = currentPage * pageSize;
		List<User> list;

		if (users.size() < startItem) {
			list = Collections.emptyList();
		} else {
			int toIndex = Math.min(startItem + pageSize, users.size());
			list = users.subList(startItem, toIndex);
		}

		Page<User> userPage = new PageImpl<User>(list, PageRequest.of(currentPage, pageSize),
				users.size());

		return userPage;
	}

}
