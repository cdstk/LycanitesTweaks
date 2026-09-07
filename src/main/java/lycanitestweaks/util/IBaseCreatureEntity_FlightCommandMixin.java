package lycanitestweaks.util;

public interface IBaseCreatureEntity_FlightCommandMixin {

    enum COMMAND {
        NONE,
        FLY,
        LAND
    }

    void lycanitesTweaks$setFlightCommand(COMMAND command);
    COMMAND lycanitesTweaks$getFlightCommand();
}
