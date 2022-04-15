package io.github.lunaiskey.pyrexprison.player.armor.gemstones;

import java.util.UUID;

public enum GemStoneType {

    DIAMOND,
    RUBY,
    EMERALD,
    SAPPHIRE,
    AMBER,
    TOPAZ,
    JADE,
    OPAL,
    JASPER,
    AMETHYST,
    ;

    public String getSkullTexture() {
        String str = "";
        switch (this) {
            case DIAMOND -> str = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTc3MmIzODgyZTE1NGMyMjU4OGU4MDFkZmFmMDI5Mzc4OTdjODg5ZTk5ZWYwZDkyZDhmNWJmYTM5MDJjZWI2MCJ9fX0=";
            case RUBY -> str = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDZkODEwNjhjYmRmNGEzNjQyMzFhMjY0NTNkNmNkNjYwYTAwOTVmOWNkODc5NTMwN2M1YmU2Njc0Mjc3MTJlIn19fQ==";
            case EMERALD -> str = "e3RleHR1cmVzOntTS0lOOnt1cmw6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDQxYzZlMjQ5ZGE4MTRmYmE3NjA0MzA1YWM0OGZmY2ZjM2U1MGYxNTJiY2M4ZjBiYWVlYTMyOWU1MmQ4MTViMyJ9fX0=";
            case SAPPHIRE -> str = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGEwYWY5OWU4ZDg3MDMxOTRhODQ3YTU1MjY4Y2Y1ZWY0YWM0ZWIzYjI0YzBlZDg2NTUxMzM5ZDEwYjY0NjUyOSJ9fX0=";
            case AMBER -> str = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTczYmNmYzM5ZWI4NWRmMTg0ODUzNTk4NTIxNDA2MGExYmQxYjNiYjQ3ZGVmZTQyMDE0NzZlZGMzMTY3MTc0NCJ9fX0=";
            case TOPAZ -> str = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjYzOTI3NzNkMTE0YmUzMGFlYjNjMDljOTBjYmU2OTFmZmVhY2ViMzk5YjUzMGZlNmZiNTNkZGMwY2VkMzcxNCJ9fX0=";
            case JADE -> str = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODIyODJjNmJiODM0M2UwZjBkNjFlZTA3NDdkYWRhNzUzNDRmMzMyZTlmZjBhY2FhM2FkY2RmMDkzMjFkM2RkIn19fQ==";
            case OPAL -> str = "e3RleHR1cmVzOntTS0lOOnt1cmw6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzE4YTRlZDQxYjlkMGUwNzFiZmMxMjNiNmE3YWZhZDM0MDE4NGI4Y2U2Zjc1OTcxNjRmNzZhMTg5YzYyMTkxMSJ9fX0=";
            case JASPER -> str = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTczNTExZTUwNGMzMTZiMTM5ZWRiMzVmZWJlNzNlZjU5MWMwZjQ1NWU4Y2FmOWVlMzUzYmMxMmI2YzE0YTkyMiJ9fX0=";
            case AMETHYST -> str = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzFkYjU5MjYwODk1NTc4ZDM3ZTU5NTA1ODgwNjAyZGU5NDBiMDg4ZTVmZmY4ZGEzZTY1MjAxZDczOWM4NmU4NCJ9fX0=";
        }
        return str;
    }

    public UUID getSkullUUID() {
        UUID uuid = UUID.fromString("022cca33-3e93-4890-a6e7-6d0c8b44e449");;
        switch (this) {
            case DIAMOND -> uuid = UUID.fromString("46a1f161-f967-4e86-ab1c-092a68645cc4");
            case RUBY -> uuid = UUID.fromString("a5397f9d-5a9b-4a25-9655-dbebe393e17b");
            case EMERALD -> uuid = UUID.fromString("234b2ba5-0e80-4815-b7a5-953042a8e5e0");
            case SAPPHIRE -> uuid = UUID.fromString("6865c46d-57b5-4342-be16-1ad4e1bd995d");
            case AMBER -> uuid = UUID.fromString("c03de23a-7265-4ec2-ac2a-1a4b1c7905b7");
            case TOPAZ -> uuid = UUID.fromString("0638c209-a618-44e6-b372-410f3fd7db02");
            case JADE -> uuid = UUID.fromString("2701b03a-7f00-482e-927f-42a4f114a68a");
            case OPAL -> uuid = UUID.fromString("88ca930f-5d1d-465c-a0dd-56ed4b9bea1a");
            case JASPER -> uuid = UUID.fromString("4ef6de3c-d7e1-44d8-bb5b-52f7ccb87505");
            case AMETHYST -> uuid = UUID.fromString("7d33e0eb-4a47-4aea-ad47-1355f460b403");
        }
        return uuid;
    }
}
