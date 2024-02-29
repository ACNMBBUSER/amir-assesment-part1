package com.example.author.entity;

import org.hibernate.validator.constraints.Length;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name="author")
public class Author {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="author_id")
	private long id;
	
	@Column(nullable = false, length = 20, unique = true)
	@NotBlank(message = "First Name cannot be blank")
	@Length(min = 5, max = 20, message = "First Name must be between 5-20 characters")
	private String firstName;
	
	@Column(nullable = false, length = 20, unique = true)
	@NotBlank(message = "Last Name cannot be blank")
	@Length(min = 5, max = 20, message = "Last Name must be between 5-20 characters")
	private String lastName;
	
	private String penName;
	
	private int age;
	
	@Column(nullable = false, length = 100, unique = true)
	@NotBlank(message = "Nationality cannot be blank")
	@Length(min = 5, max = 50, message = "Nationality must be between 5-50 characters")
	private String nationality;
	
    //private String BookId;

	
	//private Author() {} -- No Args Constructor
	
	//This is AllargsConstructor
	/*private Author(long id,String firstName,String lastName,
			String penName,int age,String nationality) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.penName = penName;
		this.age = age;
		this.nationality = nationality;
	};*/
	
	//This an example of toString Method
	/*@Override 
	public String toString() {
	return "ToStringExample(" + this.getId() + ", " 
							  + this.getFirstName() + ", " 
	                          + this.getLastName() + ", " 
	                          + this.getPenName() + ", " 
	                          + this.getAge() + ", " 
	                          + this.getNationality() + ")";
    }*/

}
