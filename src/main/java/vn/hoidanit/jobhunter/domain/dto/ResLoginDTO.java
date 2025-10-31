package vn.hoidanit.jobhunter.domain.dto;


public class ResLoginDTO {
    public static UserLogin UserLogin;
    private String accessToken ;
    private UserLogin userLogin ;
    
    public static class UserLogin {
        private long id ;
        private String email ;
        private String name ;

        public UserLogin() {
        }

        public UserLogin(long id, String email, String name) {
            this.id = id;
            this.email = email;
            this.name = name;
        }

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

    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public UserLogin getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(UserLogin userLogin) {
        this.userLogin = userLogin;
    }

    
    
}
