// router.js
import { createRouter, createWebHistory } from 'vue-router'

// Layout (enthält die untere Navigation)
import AppLayout from './views/layout.vue'

// Hauptseiten
import Startseite from './views/benutzer/hauptseiten/start.vue'
import Nachrichten from './views/benutzer/hauptseiten/benachrichtigungen.vue'
import TierheimInfo from './views/benutzer/hauptseiten/tierheim.vue'
import Einstellungen from './views/benutzer/hauptseiten/systemEinstellungen.vue'
import HundeBeschreibung from './views/benutzer/hauptseiten/beschreibung.vue'
import ReservierungsUebersicht from './views/benutzer/hauptseiten/revuebersicht.vue'
import NaechsterTermin from './views/benutzer/hauptseiten/naechstertermin.vue'

// Buchungs-Prozess Seiten
import Zeitwahl from './views/reservierung/zeitwahl.vue'
import Hundewahl from './views/reservierung/hundewahl.vue'
import Ueberpruefung from './views/reservierung/ueberpruefen.vue'
import Erfolg from './views/reservierung/erfolgreichReserviert.vue'

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
    ],
  },
]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

export default router
