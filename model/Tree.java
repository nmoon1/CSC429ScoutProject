package model;

import impresario.IModel;
import impresario.IView;

import javafx.scene.Scene;
import userinterface.View;
import userinterface.ViewFactory;

public class Tree extends TLC implements IView, IModel  {
    //tree can update, remove, and add a tree
    //tree can update, add a tree type

    private static final String myTableName = "Tree";
    protected Properties dependancies;

    private String updateStatusMessage = "";

    //empty contrsuctor
    public Tree(){
        super(myTableName);

        persistantState = new Properties();  
    }

    //constructor for existing tree
    public Tree(int treeBARCODE) throws InvalidPrimaryKeyException {
        super(myTableName);

        setDependencies();
        String query = "SELECT * FROM " + myTableName + " WHERE (treeBARCODE = " + treeBARCODE + ")";
        
        //must get a tree
        

    }

}
