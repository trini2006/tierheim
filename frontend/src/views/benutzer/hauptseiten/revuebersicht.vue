<template>
  <div class="w-full text-gray-800 bg-white flex flex-col items-center">
    <!-- Content-Bereich: Passt sich flexibel an die Spalte oder den Bildschirm an -->
    <main class="w-full px-4 py-6 flex flex-col items-center max-w-md mx-auto space-y-6">
      
      <!-- Fall, wenn keine Reservierungen vorhanden sind -->
      <div v-if="reservierungen.length === 0" class="text-sm text-gray-500 italic py-8">
        Keine aktiven Termine vorhanden.
      </div>

      <!-- Schleife über alle geladenen Reservierungen des Mitglieds -->
      <div 
        v-for="termin in reservierungen" 
        :key="termin.id" 
        class="w-full bg-gray-50 border border-gray-200 rounded-2xl p-4 shadow-sm flex flex-col items-center"
      >
        <!-- Grüne Hundekarte -->
        <div class="w-full bg-[#BFCCB5] rounded-2xl p-4 flex gap-4 items-center shadow-sm mb-4">
          <div
            class="w-20 h-20 md:w-24 md:h-24 rounded-xl bg-[#94A684]/40 flex flex-col items-center justify-center text-gray-800 overflow-hidden"
          >
            <!-- Falls ein Bild vorhanden ist, anzeigen, ansonsten Pfote -->
            <img v-if="termin.hund?.bild" :src="termin.hund.bild" class="w-full h-full object-cover rounded-xl" />
            <template v-else>
              <span class="text-2xl md:text-3xl">🐾</span>
              <span class="text-[10px] font-semibold uppercase tracking-wider text-gray-700 mt-1">
                {{ termin.hund?.name || 'Hund' }}
              </span>
            </template>
          </div>
          <div>
            <h3 class="font-bold text-gray-900 text-base">{{ termin.hund?.name || 'Gassi-Hund' }}</h3>
            <span class="text-xs text-gray-700">Status: {{ termin.status || 'AKTIV' }}</span>
          </div>
        </div>

        <!-- Termin-Details -->
        <div class="w-full max-w-[260px] space-y-2.5 mb-6 text-left">
          <div class="flex justify-between items-center">
            <span class="font-bold text-xs md:text-sm text-gray-700 w-16">Datum:</span>
            <span class="text-xs md:text-sm text-gray-900 font-medium flex-1 pl-2">
              {{ termin.datum }}
            </span>
          </div>
          <div class="flex justify-between items-center">
            <span class="font-bold text-xs md:text-sm text-gray-700 w-16">Uhrzeit:</span>
            <span class="text-xs md:text-sm text-gray-900 font-medium flex-1 pl-2">
              {{ termin.von }} - {{ termin.bis }} Uhr
            </span>
          </div>
        </div>

        <!-- Stornieren Button -->
        <button
          @click="stornieren(termin.id)"
          class="w-full max-w-[200px] bg-[#FF8B8B] hover:bg-[#ff7575] text-gray-900 font-bold py-2.5 px-4 rounded-full text-xs shadow-sm transition-all text-center hover:scale-[1.02] active:scale-[0.98]"
        >
          Termin stornieren
        </button>
      </div>

    </main>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'

// Erkennt, ob die Seite eingebettet auf dem Desktop genutzt wird oder als eigene Mobile-Seite
defineProps({
  isEmbedded: {
    type: Boolean,
    default: false,
  },
})

const router = useRouter()

// Reaktive Variable für die Reservierungen des Mitglieds
const reservierungen = ref([])

// Funktion zum Laden der Reservierungen vom Backend
const ladeReservierungen = async () => {
  try {
    // Beispiel: Mitglieds-ID (z. B. aus dem localStorage oder Auth-State)
    const mitgliedId = localStorage.getItem('mitgliedId') || 1

    // Anfrage über den Vite-Proxy (/api) an das Backend
    const res = await fetch(`/api/reservierung/mitglied/${mitgliedId}`)
    
    if (res.ok) {
      const data = await res.json()
      reservierungen.value = data
    } else {
      console.error('Fehler beim Laden der Reservierungen:', res.status)
    }
  } catch (error) {
    console.error('Netzwerkfehler beim Laden der Reservierungen:', error)
  }
}

// Beim Laden der Komponente sofort ausführen
onMounted(() => {
  ladeReservierungen()
})

const goBack = () => {
  router.back()
}

// Echte Stornierungs-Logik per API
const stornieren = async (id) => {
  if (!confirm('Möchten Sie diesen Termin wirklich stornieren?')) return

  try {
    const res = await fetch(`/api/reservierung/${id}`, {
      method: 'DELETE', // oder PUT, je nachdem wie dein Backend es erwartet
    })

    if (res.ok) {
      alert('Termin wurde erfolgreich storniert.')
      await ladeReservierungen() // Liste aktualisieren
    } else {
      alert('Fehler beim Stornieren des Termins.')
    }
  } catch (error) {
    console.error('Netzwerkfehler beim Stornieren:', error)
  }
}
</script>
