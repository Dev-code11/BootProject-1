package com.example.demo.Forms;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContactSearchForm {
	
@NotBlank(message = "selection of a field is required")
 private String field;

@NotBlank(message = "search bar can't be null")
private String value;


}
