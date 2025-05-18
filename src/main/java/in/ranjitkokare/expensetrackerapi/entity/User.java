package in.ranjitkokare.expensetrackerapi.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tbl_user")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	@Column(unique = true)//user will not able to use duplicate email
	private String email;

	@JsonIgnore //whenever we bind json property to this java field it is going to ignore that value
	private String password; //because not to show user

	private Long age;

	@Column(name = "created_at", nullable = false, updatable = false)
	@CreationTimestamp//Hibernate Annotaion
	private Timestamp createdAt;

	@Column(name = "updated_at")
	@UpdateTimestamp
	private Timestamp updatedAt;

}
