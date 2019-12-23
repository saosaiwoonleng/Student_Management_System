package sg.edu.nus.catest2.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import sg.edu.nus.catest2.model.FacultyLeave;
import sg.edu.nus.catest2.repo.FacultyLeaveRepository;

@Service
public class FacultyLeaveService {
	
	@Autowired
	FacultyLeaveRepository flrepo;
	
	public List<FacultyLeave> getPendingFacultyLeaves(){
		List<FacultyLeave> list = flrepo.findAll();
		List<FacultyLeave> facultyLeaves = new ArrayList<FacultyLeave>();
		for(FacultyLeave fl: list) {
			if(fl.getStatus().equals("Pending")) {
				facultyLeaves.add(fl);
			}
		}
		return facultyLeaves;
	}
	
	public List<FacultyLeave> getApprovedFacultyLeaves(){
		List<FacultyLeave> list = flrepo.findAll();
		List<FacultyLeave> facultyLeaves = new ArrayList<FacultyLeave>();
		for(FacultyLeave fl: list) {
			if(fl.getStatus().equals("Approved")) {
				facultyLeaves.add(fl);
			}
		}
		return facultyLeaves;
	}
	
	public List<FacultyLeave> getRejectedFacultyLeaves(){
		List<FacultyLeave> list = flrepo.findAll();
		List<FacultyLeave> facultyLeaves = new ArrayList<FacultyLeave>();
		for(FacultyLeave fl: list) {
			if(fl.getStatus().equals("Rejected")) {
				facultyLeaves.add(fl);
			}
		}
		return facultyLeaves;
	}
	
	public List<FacultyLeave> getRangeFacultyLeaves(LocalDate start, LocalDate end){
		List<FacultyLeave> list = getApprovedFacultyLeaves();
		List<FacultyLeave> facultyLeaves = new ArrayList<FacultyLeave>();
		for(FacultyLeave fl: list) {
			if((fl.getLeaveStart().isAfter(start) && fl.getLeaveStart().isBefore(end))
					|| (fl.getLeaveEnd().isAfter(start) && fl.getLeaveEnd().isBefore(end))
					|| fl.getLeaveStart().isEqual(start)
					|| fl.getLeaveEnd().isEqual(start)
					|| fl.getLeaveStart().isEqual(end)
					|| fl.getLeaveEnd().isEqual(end)
					|| (fl.getLeaveStart().isBefore(start) && fl.getLeaveEnd().isAfter(end))) {
				facultyLeaves.add(fl);
			}
		}
		return facultyLeaves;
	}
	
	public Page<FacultyLeave> findPaginatedFacultyLeave(Pageable pageable,
			List<FacultyLeave> facultyLeaves) {
		int pageSize = pageable.getPageSize();
		int currentPage = pageable.getPageNumber();
		int startItem = currentPage * pageSize;
		List<FacultyLeave> list;

		if (facultyLeaves.size() < startItem) {
			list = Collections.emptyList();
		} else {
			int toIndex = Math.min(startItem + pageSize, facultyLeaves.size());
			list = facultyLeaves.subList(startItem, toIndex);
		}

		Page<FacultyLeave> facultyLeavePage = new PageImpl<FacultyLeave>(list, PageRequest.of(currentPage, pageSize),
				facultyLeaves.size());

		return facultyLeavePage;
	}
	
	public ArrayList<FacultyLeave> getFacultyLeaveByFacultyId(int fId) {
		// TODO Auto-generated method stub
		return (ArrayList<FacultyLeave>) flrepo.getFacultyLeaveByFacultyId(fId);
	}

	public void save(FacultyLeave fl) {
		// TODO Auto-generated method stub
		flrepo.save(fl);
	}

	public void deleteById(int flId) {
		// TODO Auto-generated method stub
		flrepo.deleteById(flId);
	}

	public ArrayList<FacultyLeave> getApprovedFacultyLeaveByFacultyId(int facultyId) {
		// TODO Auto-generated method stub
		return flrepo.getApprovedFacultyLeaveByFacultyId(facultyId);
	}

	public ArrayList<FacultyLeave> getPendingFacultyLeaveByFacultyId(int facultyId) {
		// TODO Auto-generated method stub
		return flrepo.getPendingFacultyLeaveByFacultyId(facultyId);
	}

	public ArrayList<FacultyLeave> getRejectedFacultyLeaveByFacultyId(int facultyId) {
		// TODO Auto-generated method stub
		return flrepo.getRejectedFacultyLeaveByFacultyId(facultyId);
	}

}
