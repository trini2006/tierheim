import { ref, computed } from 'vue'
import { leseEmpfaengerPfad } from '@/components/utils/empfaenger.js'

// gemeinsamer Zustand für alle Aufrufer
const berechtigung = ref(typeof Notification !== 'undefined' ? Notification.permission : 'denied')
const aktiv = ref(false)
const laeuft = ref(false)
const fehler = ref('')

const unterstuetzt = () =>
  'serviceWorker' in navigator && 'PushManager' in window && 'Notification' in window

const istIos = () =>
  /iphone|ipad|ipod/i.test(navigator.userAgent) ||
  (navigator.platform === 'MacIntel' && navigator.maxTouchPoints > 1)

const istInstalliert = () =>
  window.matchMedia('(display-mode: standalone)').matches || window.navigator.standalone === true

const JSON_HEADER = { 'Content-Type': 'application/json' }

const base64ZuBytes = (base64Url) => {
  const padding = '='.repeat((4 - (base64Url.length % 4)) % 4)
  const base64 = (base64Url + padding).replace(/-/g, '+').replace(/_/g, '/')
  return Uint8Array.from(atob(base64), (c) => c.charCodeAt(0))
}

const holeSchluessel = async () => {
  const res = await fetch('/api/push/public-key')
  if (!res.ok) return ''
  return (await res.json()).publicKey ?? ''
}

const meldeAnServer = async (abo) => {
  const pfad = leseEmpfaengerPfad()
  if (!pfad) return false
  const { endpoint, keys } = abo.toJSON()
  const res = await fetch(`/api/push/abonnement/${pfad}`, {
    method: 'POST',
    headers: JSON_HEADER,
    body: JSON.stringify({ endpoint, keys }),
  })
  return res.ok
}

const meldeAbAmServer = (endpoint) =>
  fetch('/api/push/abonnement/entfernen', {
    method: 'POST',
    headers: JSON_HEADER,
    body: JSON.stringify({ endpoint }),
  })

const aktuellesAbo = async () => {
  const reg = await navigator.serviceWorker.getRegistration()
  return reg ? reg.pushManager.getSubscription() : null
}

export function usePush() {
  const status = computed(() => {
    if (istIos() && !istInstalliert()) return 'ios-installieren'
    if (!unterstuetzt()) return 'nicht-unterstuetzt'
    if (berechtigung.value === 'denied') return 'blockiert'
    return aktiv.value ? 'an' : 'aus'
  })

  // beim Öffnen der Einstellungen: Ist dieses Gerät schon angemeldet?
  const pruefeStatus = async () => {
    if (!unterstuetzt()) return
    berechtigung.value = Notification.permission
    aktiv.value = berechtigung.value === 'granted' && !!(await aktuellesAbo())
  }

  const aktivieren = async () => {
    fehler.value = ''
    laeuft.value = true
    try {
      // Zuerst fragen: Safari verlangt, dass die Frage direkt aus dem Klick kommt
      berechtigung.value = await Notification.requestPermission()
      if (berechtigung.value !== 'granted') {
        fehler.value = 'Benachrichtigungen wurden im Browser nicht erlaubt.'
        return false
      }
      const schluessel = await holeSchluessel()
      if (!schluessel) {
        fehler.value = 'Push ist auf dem Server nicht eingerichtet.'
        return false
      }
      await navigator.serviceWorker.register('/sw.js')
      const reg = await navigator.serviceWorker.ready
      const neuerSchluessel = base64ZuBytes(schluessel)

      let abo = await reg.pushManager.getSubscription()

      if (abo) {
        const alt = new Uint8Array(abo.options.applicationServerKey ?? new ArrayBuffer(0))
        const gleich =
          alt.length === neuerSchluessel.length && alt.every((b, i) => b === neuerSchluessel[i])
        if (!gleich) {
          await abo.unsubscribe() // Abo gehört zu einem alten Schlüssel
          abo = null
        }
      }
      if (!abo) {
        abo = await reg.pushManager.subscribe({
          userVisibleOnly: true,
          applicationServerKey: neuerSchluessel,
        })
      }

      aktiv.value = await meldeAnServer(abo)
      if (!aktiv.value) fehler.value = 'Die Anmeldung beim Server ist fehlgeschlagen.'
      return aktiv.value
    } catch (e) {
      console.error('Push konnte nicht aktiviert werden:', e)
      fehler.value = 'Push konnte nicht eingeschaltet werden.'
      return false
    } finally {
      laeuft.value = false
    }
  }

  const deaktivieren = async () => {
    fehler.value = ''
    laeuft.value = true
    try {
      const abo = await aktuellesAbo()
      if (abo) {
        await meldeAbAmServer(abo.endpoint)
        await abo.unsubscribe()
      }
      aktiv.value = false
      return true
    } catch (e) {
      console.error('Push konnte nicht ausgeschaltet werden:', e)
      fehler.value = 'Push konnte nicht ausgeschaltet werden.'
      return false
    } finally {
      laeuft.value = false
    }
  }

  // Beim Abmelden: Gerät beim Server abmelden, damit der nächste Nutzer keine fremden Nachrichten bekommt.
  // Das Abo im Browser bleibt bestehen und wird beim nächsten Login wieder zugeordnet (synchronisiere).
  const beimAbmelden = async () => {
    try {
      const abo = unterstuetzt() ? await aktuellesAbo() : null
      if (abo) await meldeAbAmServer(abo.endpoint)
    } catch (e) {
      console.error('Push-Abmeldung fehlgeschlagen:', e)
    }
    aktiv.value = false
  }

  // Nach dem Login: bestehendes Abo dem aktuell angemeldeten Konto zuordnen
  const synchronisiere = async () => {
    try {
      if (!unterstuetzt() || Notification.permission !== 'granted') return
      const abo = await aktuellesAbo()
      aktiv.value = !!abo && (await meldeAnServer(abo))
    } catch (e) {
      console.error('Push-Synchronisierung fehlgeschlagen:', e)
    }
  }

  return {
    status,
    laeuft,
    fehler,
    pruefeStatus,
    aktivieren,
    deaktivieren,
    beimAbmelden,
    synchronisiere,
  }
}
