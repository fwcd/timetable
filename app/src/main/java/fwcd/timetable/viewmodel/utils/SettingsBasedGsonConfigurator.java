package fwcd.timetable.viewmodel.utils;

import com.google.gson.GsonBuilder;

import fwcd.fructose.Observable;
import fwcd.timetable.model.json.GsonConfigurator;
import fwcd.timetable.viewmodel.settings.TimeTableAppSettings;

public class SettingsBasedGsonConfigurator implements GsonConfigurator {
    private final Observable<TimeTableAppSettings> settings;
    
    public SettingsBasedGsonConfigurator(Observable<TimeTableAppSettings> settings) {
        this.settings = settings;
    }
    
    @Override
    public void apply(GsonBuilder builder) {
        if (settings.get().shouldPrettyPrintJson()) {
            builder.setPrettyPrinting();
        }
    }
}
