package src;

public enum MonsterType {
    Orion,
    Troll,
    Wizard,
    Alien,
    TX5;

    public String getImageName() {
        switch (this) {
            case Troll: return "m_troll.gif";
            case TX5: return "m_tx5.gif";
            case Orion: return "m_orion.gif";
            case Wizard: return "m_wizard.gif";
            case Alien: return "M_alien.gif";

            default: {
                assert false;
            }
        }
        return null;
    }
}
