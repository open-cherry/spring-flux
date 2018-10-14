package io.opencherry.springboot.flux.model;

public class User {

  private Long id;
  private Integer age;
  private String password;
  private String username;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Integer getAge() {
    return age;
  }

  public void setAge(Integer age) {
    this.age = age;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  @Override
  public String toString() {
    return "User{" +
            "id=" + id +
            ", age=" + age +
            ", password='" + password + '\'' +
            ", username='" + username + '\'' +
            '}';
  }
}
