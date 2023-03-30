package model;

public class ActionFactory {
    
    public static Action createAction(String actionName) throws Exception {
        switch(actionName) {
            case "RemoveTree": return new RemoveTreeAction();
            case "RegisterScout": return new RegisterScoutAction();
            default: return null;
        }
    }
}
