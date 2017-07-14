package moises.com.appcoer.ui.main;


import javax.inject.Inject;

import moises.com.appcoer.api.DataHandler;
import moises.com.appcoer.global.session.SessionHandler;

public class MainPresenter implements MainContract.Presenter{

    private MainContract.View manView;
    private DataHandler dataManager;
    private SessionHandler session;

    @Inject
    public MainPresenter(MainContract.View mainView, DataHandler dataManager,
                         SessionHandler session){
        this.manView = mainView;
        this.dataManager = dataManager;
        this.session = session;
    }

    /** IMPLEMENTATION CONTRACT PRESENTER */
    @Override
    public void onActivityCreated() {

    }

    @Override
    public void getBill() {

    }

    @Override
    public void onActivityDestroyed() {

    }
}
