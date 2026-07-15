// router.js
import { createRouter, createWebHistory } from 'vue-router'
import { ref } from 'vue' // Wichtig für den Pfad-Speicher

// Layout
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
import AdminEinstellungen from './views/beides/systemEinstellungen.vue'
import AdminProfileinstellungen from './views/admin/hauptseiten/profileinstellungen.vue'

const routes = [
  {
    path: '/',
    component: AppLayout,
    children: [
      {
        path: '',
        component: Startseite,
        meta: { title: 'Willkommen' },
        children: [
          { path: '', component: NaechsterTermin },
          { path: 'nachrichten', component: Nachrichten },
          { path: 'reservierungen', component: ReservierungsUebersicht },
        ],
      },
      {
        path: 'benachrichtigungen',
        component: Nachrichten,
        meta: { title: 'Nachrichten' },
      },
      {
        path: 'revuebersicht',
        component: ReservierungsUebersicht,
        meta: { title: 'Reservierungsübersicht' },
      },
      {
        path: 'tierheim',
        component: TierheimInfo,
        meta: { title: 'Tierheim Info' },
      },
      {
        path: 'einstellungen',
        component: Einstellungen,
        meta: { title: 'Einstellungen' },
      },
      {
        path: 'hund/:id',
        component: HundeBeschreibung,
        meta: { title: 'Details' },
      },
      {
        path: 'reservieren/zeit',
        component: Zeitwahl,
        meta: { title: 'Zeit auswählen' },
      },
      {
        path: 'reservieren/hund',
        component: Hundewahl,
        meta: { title: 'Hund auswählen' },
      },
      {
        path: 'reservieren/ueberpruefen',
        component: Ueberpruefung,
        meta: { title: 'Überprüfen' },
      },
      {
        path: 'reservieren/erfolg',
        component: Erfolg,
        meta: { title: 'Erfolgreich!' },
      },
      // ADMIN-BEREICH
      {
        path: 'admin',
        component: AdminStart,
        meta: { title: 'Tierheim Weiden' },
      },
      {
        path: 'admin/mitglieder',
        component: AdminMitglieder,
        meta: { title: 'Mitgliederverwaltung' },
      },
      {
        path: 'admin/hunde',
        component: AdminHunde,
        meta: { title: 'Hundeverwaltung' },
      },
      {
        path: 'admin/reservierungen',
        component: AdminReservierungen,
        meta: { title: 'Reservierungsliste' },
      },
      {
        path: 'admin/statistik',
        component: AdminStatistik,
        meta: { title: 'Statistiken' },
      },
      {
        path: 'admin/veranstaltungen',
        component: AdminVeranstaltungen,
        meta: { title: 'Veranstaltungen' },
      },
      {
        path: 'admin/veranstaltungen/hinzufuegen',
        component: AdminVeranstaltungHinzufuegen,
        meta: { title: 'Event hinzufügen' },
      },
      {
        path: 'admin/einstellungen',
        component: AdminEinstellungen,
        meta: { title: 'AdminEinstellungen' },
      },
      {
        path: 'admin/profileinstellungen',
        component: AdminProfileinstellungen,
        meta: { title: 'Profileinstellungen' },
      },
    ],
  },
]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

// NEU: Globaler Speicher für den vorherigen Pfad
export const previousPath = ref('') 

// Navigation Guard: Merkt sich den Pfad VOR jedem Wechsel
router.beforeEach((to, from, next) => {
  previousPath.value = from.path || '/'
  next()
})

export default router