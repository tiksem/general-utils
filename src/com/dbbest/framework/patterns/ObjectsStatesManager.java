package com.dbbest.framework.patterns;

import java.util.*;

/**
 * User: Tikhonenko.S
 * Date: 26.12.13
 * Time: 14:58
 */
public class ObjectsStatesManager<T, State> {
    private StateProvider<T, State> stateProvider;

    public ObjectsStatesManager(StateProvider<T, State> stateProvider) {
        this.stateProvider = stateProvider;
    }

    public StateRestorer changeStates(final Collection<T> objects, StateChanger<T> statesChangingAction){
        final List<State> states = new ArrayList<State>();
        for(T object : objects){
            states.add(stateProvider.getState(object));
            statesChangingAction.changeState(object);
        }

        // prevent objects collection modifying bugs
        final ArrayList<T> copiedObjects = new ArrayList<T>(objects);

        return new StateRestorer(){
            @Override
            public void restore() {
                Iterator<State> stateIterator = states.iterator();
                for(T object : copiedObjects){
                    stateProvider.restoreState(object, stateIterator.next());
                }
            }
        };
    }
}
