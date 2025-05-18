package in.ranjitkokare.expensetrackerapi.entity;

import lombok.Data;

import java.util.Date;

@Data
public class ErrorObject {//hold information about the Exception

	private Integer statusCode;

	private String message;

	private Date timestamp;
}
