package entities.university;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "students")
public class Student extends User {

    private BigDecimal avgGrade;
    private int attendance;
    private Set<Course> courses;

    public Student() {
        this.courses = new HashSet<>();
    }

    @Column(name = "avg_grade", precision = 10, scale = 5)
    public BigDecimal getAvgGrade() {
        return avgGrade;
    }

    public void setAvgGrade(BigDecimal avgGrade) {
        this.avgGrade = avgGrade;
    }

    @Column
    public int getAttendance() {
        return attendance;
    }

    public void setAttendance(int attendance) {
        this.attendance = attendance;
    }

    @ManyToMany(mappedBy = "students")
    public Set<Course> getCourses() {
        return courses;
    }

    public void setCourses(Set<Course> courses) {
        this.courses = courses;
    }
}
