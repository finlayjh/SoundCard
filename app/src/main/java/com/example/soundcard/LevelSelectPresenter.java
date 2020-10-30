package com.example.soundcard;


public class LevelSelectPresenter implements LevelSelectContract.Presenter{

    private LevelSelectContract.View view;
    static UserManager userManager;

    public LevelSelectPresenter(LevelSelectContract.View view, UserManager userRepo){
        this.view = view;
        this.userManager = userRepo;
    }

    public void setUserCase(String buttonName){
        switch(buttonName){
            case "ABC":
                userManager.setCurrentCase(Case.UPPERCASE);
                break;
            case "abc":
                userManager.setCurrentCase(Case.LOWERCASE);
                break;
            case "AaBbCc":
                userManager.setCurrentCase(Case.BOTH);
                break;
        }
    }
}
