// Neue Version sofort aktivieren
self.addEventListener('install', () => self.skipWaiting())
self.addEventListener('activate', (event) => event.waitUntil(self.clients.claim()))

// Push vom Server -> Benachrichtigung anzeigen
self.addEventListener('push', (event) => {
  let daten = {}
  try {
    daten = event.data ? event.data.json() : {}
  } catch {
    daten = { body: event.data ? event.data.text() : '' }
  }

  event.waitUntil(
    self.registration.showNotification(daten.title || 'Gassi-App', {
      body: daten.body || '',
      icon: '/icons/icon-192.png',
      data: { url: daten.url || '/app/nachrichten' },
      silent: false, // Ton erlauben (ist Standard)
      vibrate: [200, 100, 200], // nur Handys
      requireInteraction: true, // bleibt am PC stehen, bis man sie wegklickt
    }),
  )
})

// Klick auf die Benachrichtigung -> App öffnen bzw. nach vorne holen
self.addEventListener('notificationclick', (event) => {
  event.notification.close()
  const ziel = new URL(event.notification.data?.url || '/app/nachrichten', self.location.origin)
    .href

  event.waitUntil(
    (async () => {
      const fenster = await self.clients.matchAll({ type: 'window', includeUncontrolled: true })
      for (const f of fenster) {
        if ('focus' in f) {
          await f.focus()
          if ('navigate' in f) await f.navigate(ziel)
          return
        }
      }
      await self.clients.openWindow(ziel)
    })(),
  )
})
