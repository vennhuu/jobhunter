package vn.vennhuu.jobhunter.domain.response;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonFormat;

public class ResUpdateUserDTO {
    private long id ;
    private String name ;
    private String gender ;
    private String address ;
    private int age ;
    private Instant updatedAt ;
    private CompanyUser company ; 
    private RoleUser role ; 
    

    public static class CompanyUser {
        private long id ;
        private String name ;

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static class RoleUser {
        private long id ;
        private String name ;

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
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
    public CompanyUser getCompany() {
        return company;
    }
    public void setCompany(CompanyUser company) {
        this.company = company;
    }
    public RoleUser getRole() {
        return role;
    }
    public void setRole(RoleUser role) {
        this.role = role;
    }
    
    
}
