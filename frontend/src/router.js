import { createRouter, createWebHistory } from 'vue-router'
import { ref } from 'vue'

import LoginView from './views/login.vue'
import AppLayout from './views/layout.vue'

import Startseite from './views/benutzer/hauptseiten/start.vue'
//import Nachrichten from './views/beides/benachrichtigungen.vue'
import TierheimInfo from './views/benutzer/hauptseiten/tierheim.vue'
import Einstellungen from './views/beides/systemEinstellungen.vue'
import HundeBeschreibung from './views/beides/beschreibung.vue'

import { istMobilJetzt } from '@/composables/useIstMobil.js'
import ReservierungDetailMobile from './views/benutzer/hauptseiten/ReservierungsDetailMobile.vue'
import ReservierungDetail from './views/benutzer/hauptseiten/ReservierungsDetail.vue'

import Zeitwahl from './views/reservierung/zeitwahl.vue'
import Hundewahl from './views/reservierung/hundewahl.vue'
import Ueberpruefung from './views/reservierung/ueberpruefen.vue'
import Erfolg from './views/reservierung/erfolgreichReserviert.vue'

import AdminStart from './views/admin/hauptseiten/start.vue'
import AdminMitglieder from './views/admin/hauptseiten/mitglieder.vue'
import AdminHunde from './views/admin/hauptseiten/hunde.vue'
import AdminReservierungen from './views/admin/hauptseiten/reservierungen.vue'
import TerminMitglied from './views/admin/hauptseiten/terminMitg.vue'
//import AdminStatistik from './views/admin/hauptseiten/statistik.vue'
//import AdminVeranstaltungen from './views/admin/hauptseiten/veranstaltungen.vue'
//import AdminVeranstaltungHinzufuegen from './views/admin/hauptseiten/veranstaltunganlegen.vue'
import AdminEinstellungen from './views/beides/systemEinstellungen.vue'
import AdminProfileinstellungen from './views/admin/hauptseiten/profileinstellungen.vue'
import AdminHundepflegen from './views/admin/hauptseiten/hundepflegen.vue'

const routes = [
  {
    path: '/',
    name: 'Login',
    component: LoginView,
    meta: { title: 'Anmeldung' },
  },
  {
    path: '/app',
    component: AppLayout,
    meta: { requiresAuth: true, role: 'user' },
    children: [
      { path: '', component: Startseite, meta: { title: 'Willkommen' } },
      {
        path: 'reservierungen/:id',
        component: ReservierungDetailMobile,
        meta: { title: 'ReservierungsDetails' },
        beforeEnter: () => (istMobilJetzt() ? true : '/app'),
      },
      //{ path: 'nachrichten', component: Nachrichten, meta: { title: 'Nachrichten' } },
      { path: 'tierheim', component: TierheimInfo, meta: { title: 'Tierheim Weiden' } },
      { path: 'einstellungen', component: Einstellungen, meta: { title: 'Einstellungen' } },
      {
        path: 'hund/:id?', name: 'beschreibung', component: HundeBeschreibung, meta: { title: 'Hundedetails' },
      },

      { path: 'reservierung/zeitwahl', component: Zeitwahl, meta: { title: 'Termin auswählen' } },
      { path: 'reservierung/hundewahl', component: Hundewahl, meta: { title: 'Hund auswählen' } },
      {
        path: 'reservierung/ueberpruefen',
        component: Ueberpruefung,
        meta: { title: 'Details bestätigen' },
      },
      {
        path: 'reservierung/erfolgreichReserviert',
        component: Erfolg,
        meta: { title: 'Erfolgreich!' },
      },

      {
        path: 'admin',
        component: AdminStart,
        meta: { requiresAuth: true, role: 'admin' },
        children: [
          //{ path: 'nachrichten', component: Nachrichten },
          //{ path: 'veranstaltungen', component: AdminVeranstaltungen }
        ],
      },
      { path: 'admin/hundepflegen', component: AdminHundepflegen, meta: { title: 'Hund pflegen' } },
      { path: 'admin/erfolgreichReserviert', component: Erfolg, meta: { title: 'Erfolgreich!' } },
      {
        path: 'admin/ueberpruefen',
        component: Ueberpruefung,
        meta: { title: 'Details bestätigen' },
      },
      { path: 'admin/hundewahl', component: Hundewahl, meta: { title: 'Hund auswählen' } },
      {
        path: 'admin/termin-mitglied',
        component: TerminMitglied,
        meta: { title: 'Termin wählen' },
      },
      //{ path: 'admin/statistik', component: AdminStatistik, meta: { title: 'Statistik' } },
      {
        path: 'admin/reservierungen',
        component: AdminReservierungen,
        meta: { title: 'Reservierungen' },
      },
      {
        path: 'admin/mitglieder',
        component: AdminMitglieder,
        meta: { title: 'Mitgliederverwaltung' },
      },
      { path: 'admin/hunde', component: AdminHunde, meta: { title: 'Hundeverwaltung' } },
      //{ path: 'admin/veranstaltungen/hinzufuegen', component: AdminVeranstaltungHinzufuegen, meta: { title: 'Event hinzufügen' } },
      {
        path: 'admin/einstellungen',
        component: AdminEinstellungen,
        meta: { title: 'Einstellungen' },
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

export const previousPath = ref('')

// Navigation Guard: Überprüft jeden Seitenaufruf
router.beforeEach((to, from, next) => {
  previousPath.value = from.path || '/'

  const userRole = localStorage.getItem('userRole')

  if (to.name === 'Login' && userRole) {
    next(userRole === 'admin' ? '/app/admin' : '/app')
    return
  }

  if (to.meta.requiresAuth && !userRole) {
    next({ name: 'Login' })
    return
  }

  if (to.meta.role && userRole && to.meta.role !== userRole) {
    next(userRole === 'admin' ? '/app/admin' : '/app')
    return
  }

  next()
})

export default router
