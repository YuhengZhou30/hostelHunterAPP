package com.hh.hostelhunter.Data;


public class User {
    private String username;
    private String phoneNumber;
    private String email;
    private String password;
    private String urlFoto;
    private int id;
    public User(String username, String phoneNumber, String email, String password) {
        this.username = username;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.password = password;
        this.urlFoto="";
    }

    public User( String email, String password) {
        this.username = null;
        this.phoneNumber = null;
        this.email = email;
        this.password = password;
    }

    // Métodos getter y setter para los atributos de la clase

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    // Setter para urlFoto
    public void setUrlFoto(String urlFoto) {
        this.urlFoto = urlFoto;
    }

    // Getter para urlFoto
    public String getUrlFoto() {
        return urlFoto;
    }

    @Override
    public String toString() {
        return "{" +
                "\"nombre\":\"" + username + "\"," +
                "\"telefono\":\"" + phoneNumber + "\"," +
                "\"email\":\"" + email + "\"," +
                "\"contraseña\":\"" + password + "\"," +
                "\"tipo\":\"" + "usuario" + "\"" +
                '}';
    }

    public String login() {
        return "{" +
                "\"email\":\"" + email + "\"," +
                "\"tipo\":\"" + "usuario" + "\"," +
                "\"contraseña\":\"" + password + "\"" +
                '}';
    }

}
