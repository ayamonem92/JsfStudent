package com.faces.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import com.faces.model.*;

@ManagedBean
@SessionScoped
public class StudentController {
	private List<Student> students;
	private StudentDao studentDao;
	private Logger logger = Logger.getLogger(getClass().getName());
	
	public StudentController() throws Exception {
		students = new ArrayList<Student>();
		
		studentDao = StudentDao.getInstance();
	}
	
	public List<Student> getStudents() {
		return students;
	}

	public void loadStudents() {

		logger.info("Loading students");
		
		students.clear();

		try {
			
			// get all students from database
			students = studentDao.getStudents();
			
		} catch (Exception exc) {
			// send this to server logs
			logger.log(Level.SEVERE, "Error loading students", exc);
			
			// add error message for JSF page
			addErrorMessage(exc);
		}
	}
				
	private void addErrorMessage(Exception exc) {
		FacesMessage message = new FacesMessage("Error: " + exc.getMessage());
		FacesContext.getCurrentInstance().addMessage(null, message);
	}
	public String addStudent(Student student) throws Exception{
		studentDao.addStudent(student);
		return "students?faces-redirect=true";
	}
	public String loadStudent(int studentId) {
		
		logger.info("loading student: " + studentId);
		
		try {
			// get student from database
			Student theStudent = studentDao.getStudent(studentId);
			
			// put in the request attribute ... so we can use it on the form page
			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();		

			Map<String, Object> requestMap = externalContext.getRequestMap();
			requestMap.put("student", theStudent);	
			
		} catch (Exception exc) {
			// send this to server logs
			logger.log(Level.SEVERE, "Error loading student id:" + studentId, exc);
			
			// add error message for JSF page
			addErrorMessage(exc);
			
			return null;
		}
				
		return "update-student.xhtml";
	}	
	
	public String updateStudent(Student theStudent) {

		logger.info("updating student: " + theStudent);
		
		try {
			
			// update student in the database
			studentDao.updateStudent(theStudent);
			
		} catch (Exception exc) {
			// send this to server logs
			logger.log(Level.SEVERE, "Error updating student: " + theStudent, exc);
			
			// add error message for JSF page
			addErrorMessage(exc);
			
			return null;
		}
		
		return "students?faces-redirect=true";		
	}
	public void deleteStudent(int studentID) throws Exception{
		studentDao.deleteStudent(studentID);
	}
}