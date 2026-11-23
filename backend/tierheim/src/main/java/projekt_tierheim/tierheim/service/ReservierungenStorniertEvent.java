package projekt_tierheim.tierheim.service;

import projekt_tierheim.tierheim.db.Reservierung.Akteur;

import java.util.List;

public record ReservierungenStorniertEvent(List<Integer> reservierungsIds, Akteur akteur, String grund)
{ }
