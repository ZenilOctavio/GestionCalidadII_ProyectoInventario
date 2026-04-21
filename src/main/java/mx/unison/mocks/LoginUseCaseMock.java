package mx.unison.mocks;

import mx.unison.presentation.login.LoginService;

public class LoginUseCaseMock implements LoginService {
    public boolean shouldFail = false;

    @Override
    public boolean withUsernamePassword(String username, String password) {
        return !shouldFail;
    }
}

