package in.ranjitkokare.expensetrackerapi.service;

import in.ranjitkokare.expensetrackerapi.entity.User;
import in.ranjitkokare.expensetrackerapi.entity.UserModel;

public interface UserService {
	
	User createUser(UserModel user);
	
	//to read User information
	User readUser(Long id);
	
	//to update User
	User updateUser(UserModel user, Long id);
	
	//to delete user
	void deleteUser(Long id);
}
