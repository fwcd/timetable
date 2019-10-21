package fwcd.timetable.model.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * A chainable operation on a Gson builder.
 */
public interface GsonConfigurator {
    GsonConfigurator IDENTITY = b -> {};
    
    void apply(GsonBuilder builder);
    
    default GsonConfigurator andThen(GsonConfigurator next) {
        return b -> {
            apply(b);
            next.apply(b);
        };
    }

    default Gson create() {
        GsonBuilder builder = new GsonBuilder();
        apply(builder);
        return builder.create();
    }
}
