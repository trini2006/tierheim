<template>
  <div class="flex items-center justify-center min-h-screen w-full">
    
    <!-- LADEZUSTAND -->
    <div v-if="loading" class="bg-gray-700 text-white p-8 rounded-2xl shadow-2xl text-center m-4">
      <h2 class="text-3xl font-bold mb-2">Wird geladen...</h2>
      <p class="text-lg">Bitte einen Moment Geduld.</p>
    </div>

    <!-- ERFOLG BANNER -->
    <div v-else-if="success" class="bg-green-700 text-white p-8 rounded-2xl shadow-2xl text-center m-4">
      <h2 class="text-3xl font-bold mb-2">Erfolg!</h2>
      <p class="text-lg">Sie haben erfolgreich reserviert.</p>
    </div>

    <!-- FEHLER BANNER -->
    <div v-else class="bg-red-600 text-white p-8 rounded-2xl shadow-2xl text-center m-4">
      <h2 class="text-3xl font-bold mb-2">Fehlgeschlagen!</h2>
      <p class="text-lg">Reservierung konnte nicht gespeichert werden.</p>
    </div>
    
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'

const router = useRouter()
const route = useRoute()

const loading = ref(true)
const success = ref(false)

onMounted(async () => {
  try {
    const bookingFinal = JSON.parse(localStorage.getItem('bookingFinal') || '{}')

    // Entspricht exakt deinem ReservierungDTO:
    // mitgliedId, hundId, datum, zeitAb, zeitBis
    const payload = {
      mitgliedId: bookingFinal.mitgliedId,
      hundId: bookingFinal.dog?.id,
      datum: bookingFinal.datum,
      zeitAb: bookingFinal.von ? (bookingFinal.von.length === 5 ? bookingFinal.von + ':00' : bookingFinal.von) : '10:00:00',
      zeitBis: bookingFinal.bis ? (bookingFinal.bis.length === 5 ? bookingFinal.bis + ':00' : bookingFinal.bis) : '10:30:00'
    }

    const res = await fetch('/new', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(payload)
    })

    if (res.ok) {
      success.value = true
      loading.value = false

      setTimeout(() => {
        localStorage.removeItem('terminData')
        localStorage.removeItem('bookingFinal')

        if (route.path.startsWith('/app/admin')) {
          router.push('/app/admin')
        } else {
          router.push('/app')
        }
      }, 2000)
    } else {
      throw new Error('Server antwortete mit Fehler')
    }
  } catch (e) {
    console.error('Reservierungsfehler:', e)
    success.value = false
    loading.value = false

    // Bei Fehler zurück zum Anfang des Prozesses
    setTimeout(() => {
      if (route.path.startsWith('/app/admin')) {
        router.push('/app/admin/termin-mitglied')
      } else {
        router.push('/app/reservierung/zeitwahl')
      }
    }, 2500)
  }
})
</script>