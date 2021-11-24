package com.endsoul.fully_security.student;

import io.vavr.collection.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("management/api/v1/students")
public class StudentManagementController {
  private static final List<Student> STUDENTS =
      List.of(
          new Student(1, "James Bond"),
          new Student(2, "Maria Jones"),
          new Student(3, "Anna Smith"));

  @GetMapping
  public List<Student> getAllStudents() {
    return STUDENTS;
  }

  @PostMapping
  public void registerNewStudent(@RequestBody Student student) {
    System.out.println(student);
  }

  @DeleteMapping("{studentId}")
  public void deleteStudent(@PathVariable Integer studentId) {
    System.out.println(studentId);
  }

  @PutMapping("{studentId}")
  public void updateStudent(@PathVariable Integer studentId, @RequestBody Student student) {
    System.out.printf("%s %s%n", studentId, student);
  }
}
