package com.jsp.hibernate_bi_many_to_many.driver;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.jsp.hibernate_bi_many_to_many.dao.CoachingDao;
import com.jsp.hibernate_bi_many_to_many.dto.Course;
import com.jsp.hibernate_bi_many_to_many.dto.Student;

public class CoachingDriver {
//Note:Problem to solve when user enter the value the value should be coverted in lowerCase
	//becoz when we retrive the value database return the Value which is enter by user
	//input format eg:Java and when we try to compare with Java==java then will not able
	//to update the value
	//Solve this problem in update method
	//And check that value is successfully converted in lowecase or not
	static CoachingDao dao = new CoachingDao();
	static Scanner s = new Scanner(System.in);
	
	static void insert() {
	
		List<Course> list =  new ArrayList<>();
		Student student = new Student();
		System.out.println("Enter the your Name");
		student.setSname(s.next().toLowerCase());
		System.out.println("Enter the your City");
		student.setCity(s.next().toLowerCase());
		System.out.println("How much Courses You want to Add?");
		int choice = s.nextInt();
		for(int i = 1;i<=choice;i++) {
			System.out.println("Enter "+i+" Course");
			Course course = new Course();
			course.setCname(s.next().toLowerCase());
			list.add(course);
		}
		dao.savePersonCourse(student, list);
	}
	
	static void update() {
		System.out.println("Enter Student Id");
		int sid = s.nextInt();
		System.out.println("Enter Your old Course Name");
		String oldCourse= s.next();
		System.out.println("Enter Your New Course Name");
		String newName = s.next().toLowerCase();
		Course newCourse = new Course();
		newCourse.setCname(newName.toLowerCase());
		dao.updateStudentCourse(sid, oldCourse.toLowerCase(), newCourse);
	}
	public static void main(String[] args) {
		
//		insert();
		update();
		
		
		
	}
}
