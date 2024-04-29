package com.jsp.hibernate_bi_many_to_many.dto;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;



import lombok.Data;

@Entity
@Data
public class Student {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int sid;
	private String sname;
	private String city;
	
	@ManyToMany
	@JoinTable(name="Mapping_Table" ,joinColumns = 
	{@JoinColumn(name="S_id")},inverseJoinColumns = {@JoinColumn(name="C_id")})
	private List<Course> courses = new ArrayList<>();
}
