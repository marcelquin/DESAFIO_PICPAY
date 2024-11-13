package App.Security.DTO;


import App.Security.Model.UserRole;

public record RegisterDTO(String login, String password, UserRole role) {
}
