package com.example.demo.student;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public List<Student> getStudents(){
        return studentRepository.findAll();
    }
    public void addNewStudent(Student student) {
        Optional<Student> studentOptional = studentRepository.findStudentByEmail(student.getEmail());
        if (studentOptional.isPresent()){
            throw new IllegalStateException("Email taken");
        }
        studentRepository.save(student);
    }

    public void deleteStudent(long studentId) {
        boolean exist = studentRepository.existsById(studentId);
        if (!exist){
            throw new IllegalStateException("student with id " + studentId + " does not exist.");

        }
        studentRepository.deleteById(studentId);
    }

    @Transactional
    public void updateStudent(Long studentId, String name, String email){
        Student studnet = studentRepository.findById(studentId).orElseThrow(() ->
                new IllegalStateException("student with id " + studentId + " does not exist."));
        if (name != null && name.length() > 0 && !Objects.equals(studnet.getName(), name)){
            studnet.setName(name);
        }
        if (email != null && email.length() > 0 && !Objects.equals(studnet.getEmail(), email)){
            Optional<Student> studentOptional = studentRepository.findStudentByEmail(email);
            if (studentOptional.isPresent()){
                throw new IllegalStateException("email is taken.");
            }
            studnet.setEmail(email);
        }
    }
}
