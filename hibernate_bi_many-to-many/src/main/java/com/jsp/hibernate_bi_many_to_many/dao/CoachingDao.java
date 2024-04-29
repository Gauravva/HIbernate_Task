package com.jsp.hibernate_bi_many_to_many.dao;

import java.util.List;
import java.util.Scanner;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;

import com.jsp.hibernate_bi_many_to_many.dto.Course;
import com.jsp.hibernate_bi_many_to_many.dto.Student;

public class CoachingDao {

	private static EntityManagerFactory emf;
	private static EntityManager em;
	private static EntityTransaction et;
	static Scanner s;

	static void openConnection() {
		emf = Persistence.createEntityManagerFactory("hibernate");
		em = emf.createEntityManager();
		et = em.getTransaction();
		s = new Scanner(System.in);
	}

	static void closeConnection() {
		if (emf != null) {
			emf.close();
		}
		if (em != null) {
			em.close();
		}
		if (s != null) {
			s.close();
		}
		if (et != null) {
			if (et.isActive()) {
				et.rollback();
			}
		}

	}

	public void savePersonCourse(Student student, List<Course> courses) {
		openConnection();
		et.begin();
		for (Course course : courses) {
			Course existingCourse = checkCourses(course.getCname(), em);
			if (existingCourse != null) {
				student.getCourses().add(existingCourse);
			} else {
				student.getCourses().add(course);
				em.persist(course);
			}
		}
		em.persist(student);
		et.commit();
		System.out.println("Record Inserted Successfully");
		closeConnection();
	}

	public static Course checkCourses(String name, EntityManager em) {
		Query query = em.createQuery("Select c From Course c where cname = :name");
		query.setParameter("name", name);
		List<Course> list = query.getResultList();
		if (!list.isEmpty()) {
			return list.get(0);
		}
		return null;
	}

	public void updateStudentCourse(int sid, String oldCourse, Course newCourse) {
		openConnection();

		Student student = em.find(Student.class, sid);
		Query query1 = em.createQuery("Select c From Course c where cname = :Oname");
		query1.setParameter("Oname", oldCourse);
		Query query2 = em.createQuery("Select c From Course c where cname = :Nname");
		query2.setParameter("Nname", newCourse.getCname());

		if (student != null && !query1.getResultList().isEmpty() && !query2.getResultList().isEmpty()) {

			for (Course course : student.getCourses()) {

				if (course.getCname().equals(oldCourse)) {
					student.getCourses().set(student.getCourses().indexOf(course),
							(Course) query2.getResultList().get(0));
					System.out.println("First If block");
					et.begin();
					em.merge(student);
					et.commit();
					// Note:If we want to update a course and OldCourse and new Course is already
					// exist in the database
					// Then we dont need to persist and merge the newCourse instead of after set the
					// course to the student entity
					// then you have to only perform commit() ,before commit() you have to call
					// et.begin() method
					// once you commit() then that changes you made are reflect in database in
					// Mapping_Table
					
					//Note2:Because Student table is responsible to manage the Mapping table or relationship
					//so when we update the course of student then we have to merge the student object not course
					//object
					break;
				}
			}
		} else if (student != null && !query1.getResultList().isEmpty() && query2.getResultList().isEmpty()) {

			for (Course course : student.getCourses()) {
				if (course.getCname().equals(oldCourse) && query2.getResultList().isEmpty())
					student.getCourses().set(student.getCourses().indexOf(course), newCourse);
				et.begin();
				em.persist(newCourse);
				et.commit();
				break;
			}
		} else {
			System.out.println(oldCourse + " Not Found in your Course list");
			System.out
					.println("If You want to add " + newCourse.getCname() + "\n" + "1.Add New_Course\n" + "2.Not Add");
			System.out.println("Please Enter Your Choice");
			int choice = s.nextInt();
			if (1 == choice) {
				student.getCourses().add(newCourse);
				et.begin();
				em.persist(newCourse);
				et.commit();
			}
		}
		closeConnection();
	}
}
