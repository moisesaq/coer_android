package moises.com.appcoer.ui.main;


import javax.inject.Inject;

import moises.com.appcoer.api.DataContract;
import moises.com.appcoer.global.session.SessionContract;

public class MainPresenter implements MainContract.Presenter{

    private MainContract.View manView;
    private DataContract dataManager;
    private SessionContract session;

    @Inject
    public MainPresenter(MainContract.View mainView, DataContract dataManager,
                         SessionContract session){
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
