package in.ranjitkokare.expensetrackerapi.repository;

import java.sql.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import in.ranjitkokare.expensetrackerapi.entity.Expense;



@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
	
	//SELECT * FROM tbl_expense WHERE category=?
	Page<Expense> findByCategory(String category, Pageable page);

	//SELECT * FROM tbl_expense WHERE name LIKE '%keyword%'
	Page<Expense> findByNameContaining(String name, Pageable page);
	
	//SELECT * FROM tbl_expense WHERE date BETWEEN 'startDate' AND 'endDate'
	Page<Expense> findByDateBetween(Date startDate, Date endDate, Pageable page);
}
