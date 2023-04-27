package userinterface;

public class ScoutInfo {
    private final String name;
    private final String troop;

    public ScoutInfo(String info) {
        int nameIndex = info.indexOf(" ") + 1;
        this.name = info.substring(nameIndex, info.length() - 6);
        this.troop = info.substring(info.length() - 5);
    }

    public String getName() {
        return name;
    }

    public String getTroop() {
        return troop;
    }

    @Override
    public String toString() {
        return name + " (Troop " + troop + ")";
    }
}
