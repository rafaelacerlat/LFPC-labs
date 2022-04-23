package Lab5;

public class Production {
    private Character state;
    private String transition;

    Production(Character state, String transition ){
        this.state = state;
        this.transition = transition;
    }

    public Character getState(){
        return state;
    }

    public String getTransition(){
        return transition;
    }
}
