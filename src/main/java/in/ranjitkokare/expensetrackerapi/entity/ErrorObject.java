package in.ranjitkokare.expensetrackerapi.entity;

import java.util.Date;

import lombok.Data;

@Data
public class ErrorObject {//hold information about the Exception
	
	private Integer statusCode;
	
	private String message;

	private Date timestamp;
}
