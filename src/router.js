// router.js
import { createRouter, createWebHistory } from 'vue-router'

// Layout (enthält die untere Navigation)
import AppLayout from './views/layout.vue'

// Hauptseiten Benutzer
import Startseite from './views/benutzer/hauptseiten/start.vue'
import Nachrichten from './views/beides/benachrichtigungen.vue'
import TierheimInfo from './views/benutzer/hauptseiten/tierheim.vue'
import Einstellungen from './views/beides/systemEinstellungen.vue'
import HundeBeschreibung from './views/beides/beschreibung.vue'
import ReservierungsUebersicht from './views/benutzer/hauptseiten/revuebersicht.vue'
import NaechsterTermin from './views/benutzer/hauptseiten/naechstertermin.vue'

// Buchungs-Prozess Seiten
import Zeitwahl from './views/reservierung/zeitwahl.vue'
import Hundewahl from './views/reservierung/hundewahl.vue'
import Ueberpruefung from './views/reservierung/ueberpruefen.vue'
import Erfolg from './views/reservierung/erfolgreichReserviert.vue'

// Hauptseiten (Admin)
import AdminStart from './views/admin/hauptseiten/start.vue'
import AdminMitglieder from './views/admin/hauptseiten/mitglieder.vue'
import AdminHunde from './views/admin/hauptseiten/hunde.vue'
import AdminReservierungen from './views/admin/hauptseiten/reservierungen.vue'
import AdminStatistik from './views/admin/hauptseiten/statistik.vue'
import AdminVeranstaltungen from './views/admin/hauptseiten/veranstaltungen.vue'
import AdminVeranstaltungHinzufuegen from './views/admin/hauptseiten/veranstaltunghinzufuegen.vue'
import AdminProfileinstellungen from './views/admin/hauptseiten/profileinstellungen.vue'

const routes = [
  {
    path: '/',
    component: AppLayout,
    children: [
      // STARTSEITE (Desktop Master-Detail-Zentrale)
      {
        path: '',
        component: Startseite,
        children: [
          // Zeigt am Anfang standardmäßig "Nächster Termin"
          { path: '', component: NaechsterTermin },
          // Zeigt "Nachrichten" auf der rechten Desktopseite
          { path: 'nachrichten', component: Nachrichten },
          // Zeigt "Reservierungsübersicht" auf der rechten Desktopseite
          { path: 'reservierungen', component: ReservierungsUebersicht },
        ],
      },

      // SEPARATE MOBILE PFADE (Laden die Seiten im Vollbild auf dem Handy)
      { path: 'benachrichtigungen', component: Nachrichten },
      { path: 'reservierungsuebersicht', component: ReservierungsUebersicht },

      // Weitere Hauptseiten
      { path: 'tierheim', component: TierheimInfo },
      { path: 'einstellungen', component: Einstellungen },
      { path: 'hund/:id', component: HundeBeschreibung },

      // Der Reservierungs-Ablauf
      { path: 'reservieren/zeit', component: Zeitwahl },
      { path: 'reservieren/hund', component: Hundewahl },
      { path: 'reservieren/ueberpruefen', component: Ueberpruefung },
      { path: 'reservieren/erfolg', component: Erfolg },

      // ADMIN-BEREICH
      { path: 'admin', component: AdminStart },
      { path: 'admin/mitglieder', component: AdminMitglieder },
      { path: 'admin/hunde', component: AdminHunde },
      { path: 'admin/reservierungen', component: AdminReservierungen },
      { path: 'admin/statistik', component: AdminStatistik },
      { path: 'admin/veranstaltungen', component: AdminVeranstaltungen },
      { path: 'admin/veranstaltungen/hinzufuegen', component: AdminVeranstaltungHinzufuegen },
      { path: 'admin/profileinstellungen', component: AdminProfileinstellungen },
    ],
  },
]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

export default router
