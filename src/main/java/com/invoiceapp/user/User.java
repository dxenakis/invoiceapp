package com.invoiceapp.user;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatus status = UserStatus.ACTIVE;

    /** Ελάχιστο state για revoke όλων των refresh του χρήστη */
    @Column(name = "refresh_version", nullable = false)
    private Integer refreshVersion = 1;

    // getters / setters

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public UserStatus getStatus() { return status; }
    public void setStatus(UserStatus status) { this.status = status; }

    public Integer getRefreshVersion() { return refreshVersion; }
    public void setRefreshVersion(Integer refreshVersion) { this.refreshVersion = refreshVersion; }

    public void incrementRefreshVersion() {
        this.refreshVersion = (this.refreshVersion == null ? 1 : this.refreshVersion + 1);
    }
}
