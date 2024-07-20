package in.ranjitkokare.expensetrackerapi.repository;

import java.sql.Date;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import in.ranjitkokare.expensetrackerapi.entity.Expense;



@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
	
	//SELECT * FROM tbl_expense WHERE user_id=? AND category=?
	Page<Expense> findByUserIdAndCategory(Long userId, String category, Pageable page);

	//SELECT * FROM tbl_expense WHERE user_id=? AND name LIKE '%keyword%'
	Page<Expense> findByUserIdAndNameContaining(Long userId, String name, Pageable page);
	
	//SELECT * FROM tbl_expense WHERE user_id=? AND date BETWEEN 'startDate' AND 'endDate'
	Page<Expense> findByUserIdAndDateBetween(Long userId, Date startDate, Date endDate, Pageable page);
	
	
	
	//SELECT * FROM tbl_expense WHERE user_id=?
	Page<Expense> findByUserId(Long id, Pageable page);
	
	//SELECT * FROM tbl_expense WHERE user_id=? AND id=?
	Optional<Expense> findByUserIdAndId(Long userId, Long expenseId);//provides is isPresent() method
}
