package mx.unison.presentation.login;

import mx.unison.presentation.navigation.Navigator;
import mx.unison.presentation.login.LoginService;

public class LoginPresenter {
    private LoginView view;
    private final LoginService loginService;
    private final Navigator navigator;

    public LoginPresenter(LoginService loginService, Navigator navigator){
        this.loginService = loginService;
        this.navigator = navigator;
    }

    public void setView(LoginView view){
        this.view = view;
    }

    public void onLoginClicked(String user, String password){
        if (user.trim().isEmpty()){
            view.showError("El campo usuario esta vacio");
        }
        if (password.trim().isEmpty()){
            view.showError("El campo contraseña esta vacio");
        }

        var authUser = loginService.withUsernamePassword(user, password);

        var success = authUser.isPresent();

        if (success) {
            navigator.navigateToHome();
        } else {
            view.showError("Credenciales incorrectas");
            view.clearFields();
        }

    }
}
