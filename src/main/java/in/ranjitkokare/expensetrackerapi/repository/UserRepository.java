package in.ranjitkokare.expensetrackerapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import in.ranjitkokare.expensetrackerapi.entity.User;
import java.util.Optional;


//Jpa repository actually contain @repositiry annotation in SimpleJpaRepository
@Repository //then also we add no issue
public interface UserRepository extends JpaRepository<User, Long>{//(Entity name, Primary key type)
	
	//check email exists or not
	Boolean existsByEmail(String email);
	
	Optional<User> findByEmail(String email);
}
