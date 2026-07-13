import { createRouter, createWebHistory } from 'vue-router'

// Layout (enthält die untere Navigation)
import AppLayout from './views/layout.vue'

// Hauptseiten
import Startseite from './views/benutzer/hauptseiten/start.vue'
import Nachrichten from './views/benutzer/hauptseiten/benachrichtigungen.vue'
import TierheimInfo from './views/benutzer/hauptseiten/tierheim.vue'
import Einstellungen from './views/benutzer/hauptseiten/systemEinstellungen.vue'
import HundeBeschreibung from './views/benutzer/hauptseiten/beschreibung.vue'

// Buchungs-Prozess Seiten
import Zeitwahl from './views/reservierung/zeitwahl.vue'
import Hundewahl from './views/reservierung/hundewahl.vue'
import Ueberpruefung from './views/reservierung/ueberpruefen.vue'
import Erfolg from './views/reservierung/erfolgreichReserviert.vue'

const routes = [
  {
    path: '/',
    component: AppLayout, // Das Hauptlayout mit der Navigation unten
    children: [
      { path: '', component: Startseite }, // Standard-Startseite
      { path: 'nachrichten', component: Nachrichten }, // /nachrichten
      { path: 'tierheim', component: TierheimInfo }, // /tierheim
      { path: 'einstellungen', component: Einstellungen }, // /einstellungen
      { path: 'hund/:id', component: HundeBeschreibung }, // /hund/mambo

      // Der Reservierungs-Ablauf (wird nacheinander durchlaufen)
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
