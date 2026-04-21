package mx.unison.presentation.home;

import mx.unison.presentation.navigation.Navigator;

public class HomePresenter {
    private final Navigator navigator;

    public HomePresenter(Navigator navigator){
        this.navigator = navigator;
    }

    public void onProductosClicked(){
        navigator.navigateToProductosPanel();
    }

    public void onAlmacenesClicked(){
        navigator.navigateToAlmacenesPanel();
    }
}
