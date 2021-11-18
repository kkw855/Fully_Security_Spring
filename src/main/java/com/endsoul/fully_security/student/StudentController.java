package com.endsoul.fully_security.student;

import io.vavr.collection.List;
import io.vavr.control.Option;
import java.util.Objects;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/students")
public class StudentController {

  private static final List<Student> STUDENTS = List.of(
      new Student(1, "James Bond"),
      new Student(2, "Maria Jones"),
      new Student(3, "Anna Smith")
  );

  @GetMapping("{studentId}")
  public ResponseEntity<Student> getStudent(@PathVariable("studentId") final Integer studentId) {
    final Option<Student> student = STUDENTS.find((s) -> Objects.equals(s.getStudentId(), studentId));

    // Some: 200 OK, None: 404 Not Found
    return ResponseEntity.of(student.toJavaOptional());
  }
}
