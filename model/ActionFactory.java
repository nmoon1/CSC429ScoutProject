package model;

public class ActionFactory {
    
    public static Action createAction(String actionName) throws Exception {
        switch(actionName) {
            case "RemoveTree": return new RemoveTreeAction();
            case "RegisterScout": return new RegisterScoutAction();
            case "UpdateScout": return new UpdateScoutAction(); 
            default: return null;
        }
    }
}
