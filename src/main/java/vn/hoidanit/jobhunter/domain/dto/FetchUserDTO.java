package vn.hoidanit.jobhunter.domain.dto;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonFormat;

public class FetchUserDTO {
    private long id ; 
    private String email ; 
    private String name ;
    private String gender ;
    private String address ;
    private int age;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss a" , timezone = "GMT+7")
    private Instant updatedAt ;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss a" , timezone = "GMT+7")
    private Instant createdAt ;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    
}
