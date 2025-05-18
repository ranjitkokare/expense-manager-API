package in.ranjitkokare.expensetrackerapi.service;

import in.ranjitkokare.expensetrackerapi.entity.User;
import in.ranjitkokare.expensetrackerapi.entity.UserModel;
import in.ranjitkokare.expensetrackerapi.exceptions.ItemAlreadyExistsException;
import in.ranjitkokare.expensetrackerapi.exceptions.ResourceNotFoundException;
import in.ranjitkokare.expensetrackerapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

	private final PasswordEncoder bcryptEncoder;
	//inject UserRepository
	private final UserRepository userRepository;

	@Override
	public User createUser(UserModel user) {
		//before saving user details check for the email existence
		if(Boolean.TRUE.equals(userRepository.existsByEmail(user.getEmail()))) {
			throw new ItemAlreadyExistsException("User is laready registerd with email:"+user.getEmail());
		}
		User newUser = new User();
		BeanUtils.copyProperties(user, newUser);//src Obj , dest obj
		newUser.setPassword(bcryptEncoder.encode(newUser.getPassword()));
		return userRepository.save(newUser);//pass newUser entity
	}

	@Override
	public User readUser() {
		Long userId = getLoggedInUser().getId();
		return userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found for the id:"+userId));
	}

	@Override
	public User updateUser(UserModel user) {
		User existingUser = readUser();
		//										get from model class or from entity class
		existingUser.setName(user.getName() !=null ? user.getName() : existingUser.getName());
		existingUser.setEmail(user.getEmail() !=null ? user.getEmail() : existingUser.getEmail());
		existingUser.setPassword(user.getPassword() !=null ? bcryptEncoder.encode(user.getPassword()) : existingUser.getPassword());
		existingUser.setAge(user.getAge() !=null ? user.getAge() : existingUser.getAge());
		return userRepository.save(existingUser);
	}

	@Override
	public void deleteUser() {
		User existingUser = readUser();	//return existing user
		userRepository.delete(existingUser);

	}

	@Override
	public User getLoggedInUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		String email = authentication.getName();

		return userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found for the email: "+email));
	}


}
