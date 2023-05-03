package userinterface;

public class ScoutInfo {
    private final String name;
    private final String troop;
    private final String phone;

    public ScoutInfo(String info) {
        int firstSpaceIndex = info.indexOf(' ');
        int secondSpaceIndex = info.indexOf(' ', firstSpaceIndex + 1);
        this.name = info.substring(0, secondSpaceIndex);
        this.troop = info.substring(secondSpaceIndex + 1, secondSpaceIndex + 8);
        this.phone = info.substring(secondSpaceIndex + 8);
    }

    public String getName() {
        return name;
    }

    public String getTroop() {
        return troop;
    }

    public String getPhone(){
        return phone;
    }

    @Override
    public String toString() {
        return name + " (Troop " + troop + ") ";
    }
}

