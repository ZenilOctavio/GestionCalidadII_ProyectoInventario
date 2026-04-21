package mx.unison.mocks;

import mx.unison.presentation.login.LoginView;

public class LoginViewMock implements LoginView {
    private int llamadasError = 0;
    private String ultimoMensaje;
    private boolean cargando = false;

    @Override
    public void showError(String message) {
        this.llamadasError++;
        this.ultimoMensaje = message;
    }

    @Override
    public void clearFields() {

    }


    // Métodos de verificación para los asserts
    public boolean seLlamoAError() { return llamadasError > 0; }
    public int getTotalErrores() { return llamadasError; }
    public String getUltimoMensaje() { return ultimoMensaje; }
}