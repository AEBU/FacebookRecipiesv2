package ec.edu.lexus.facebookrecipies.lib.base;

/**
 * Created by Alexis on 26/09/2017.
 */

public interface EventBus {
    void register(Object subscriber);
    void unregister(Object subscriber);
    void post(Object event);

}