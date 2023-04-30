package userinterface;

public class ScoutInfo {
    private final String name;
    private final String troop;
    private final String phone;

    public ScoutInfo(String info) {
        int nameIndex = info.indexOf(" ") + 1;
        this.name = info.substring(nameIndex, info.length() - 6);
        this.troop = info.substring(info.length() - 5);
        //temp phone value
        this.phone = "1234567890";
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
