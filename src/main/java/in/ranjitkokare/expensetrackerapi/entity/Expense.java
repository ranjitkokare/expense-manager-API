package in.ranjitkokare.expensetrackerapi.entity;

import java.math.BigDecimal;

import java.sql.Date;
import java.sql.Timestamp;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tbl_expenses")
public class Expense {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "expense_name")
	@NotBlank(message = "Expense name must not be null")
	@Size(min = 3,message = "Expense name must be atleast 3 characters")
	private String name;
	
	
	private String description;
	
	@NotNull(message = "Expense amount should not be null")
	@Column(name = "expense_amount")
	private BigDecimal amount;
	
	@NotBlank(message = "Category should not be null")
	private String category;
	
	@NotNull(message = "Date must not be null")
	private Date date;
	
	@Column(name = "created_at", nullable = false, updatable = false)
	@CreationTimestamp//Hibernate Annotaion
	private Timestamp createdAt;
	
	@Column(name = "updated_at")
	@UpdateTimestamp
	private Timestamp updatedAt;
	
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "user_id",nullable = false)//many expense are mapped to a single user
	@OnDelete(action = OnDeleteAction.CASCADE) //on delete of user all expenses should be deleted
	@JsonIgnore //whenever we fetch expense we ignore user we hide user
	private User user;
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
