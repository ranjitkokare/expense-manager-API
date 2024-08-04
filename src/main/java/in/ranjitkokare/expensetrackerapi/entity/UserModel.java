package in.ranjitkokare.expensetrackerapi.entity;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserModel {
	
	@NotBlank(message = "Name should not be empty")
	private String name;
	
	@NotNull(message = "Email should notbe empty")
	@Email(message = "Enter valid email")
	private String email;

	@NotNull(message = "Password should not be empty")
	@Size(min = 5, message = "Password should be at least 5 characters")
	private String password;

	private Long age = 0L;//default value set to 0 i.e. if user not provide it will automatically set to 0  
}
//Why we used UserModel.class?
// 1st way Because of @JsonIgnore annotation. If we used User Entity instead of UserModel then while 
// binding the or saving to database this annotation will ignore password to the DB.
// We will unable to save password field to DB

//2nd way is save to DB using Entity class and 
//returning to client from DB you can create new model class and you can map it

