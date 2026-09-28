<template>
  <div class="max-w-6xl mx-auto p-4 md:p-6 grid grid-cols-1 md:grid-cols-2 gap-16 items-start">
    
    <!-- LINKE SEITE: Aktive Hunde & Hinzufügen -->
    <div class="space-y-4">
      <div class="flex items-center justify-between mb-6">
        <h2 class="text-xl font-bold text-gray-800">Hinzufügen</h2>
        <button
          @click="geheZuPflegen"
          class="text-2xl leading-none text-gray-700 hover:text-gray-900 font-bold px-2"
          aria-label="Neuen Hund hinzufügen"
        >
          +
        </button>
      </div>

      <!-- Liste der verfügbaren Hunde -->
      <div class="space-y-3">
        <div
          v-for="hund in aktiveHunde"
          :key="hund.id"
          class="flex items-center justify-between p-3 rounded-full bg-[#D3DDD1] border border-gray-200 shadow-sm"
        >
          <div class="flex items-center gap-3">
            <img :src="hund.bild" class="w-10 h-10 rounded-full object-cover" />
            <div class="flex items-center gap-2">
              <span class="w-3 h-3 rounded-full" :class="hund.statusPunkt"></span>
              <span class="font-bold text-gray-800 text-sm">{{ hund.name }}</span>
            </div>
          </div>

          <!-- Aktionen -->
          <div class="flex items-center gap-4 pr-2">
            <!-- Bearbeiten -->
            <button @click="bearbeitenHund(hund)" aria-label="Hund bearbeiten">
              <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="w-4 h-4 text-gray-800 hover:text-black">
                <path stroke-linecap="round" stroke-linejoin="round" d="m16.862 4.487 1.687-1.688a1.875 1.875 0 1 1 2.652 2.652L10.582 16.07a4.5 4.5 0 0 1-1.897 1.13L6 18l.8-2.685a4.5 4.5 0 0 1 1.13-1.897l8.932-8.931Zm0 0L19.5 7.125M18 14v4.75A2.25 2.25 0 0 1 15.75 21H5.25A2.25 2.25 0 0 1 3 18.75V8.25A2.25 2.25 0 0 1 5.25 6H10" />
              </svg>
            </button>
            <!-- Sperren (rotes Verbots-Symbol) -->
            <button @click="sperreHund(hund.id)" aria-label="Hund sperren">
              <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="w-4 h-4 text-red-600 hover:text-red-800">
                <path stroke-linecap="round" stroke-linejoin="round" d="M18.364 18.364A9 9 0 0 0 5.636 5.636m12.728 12.728A9 9 0 0 1 5.636 5.636m12.728 12.728L5.636 5.636" />
              </svg>
            </button>
            <!-- Löschen -->
            <button @click="loescheHund(hund.id)" aria-label="Hund löschen">
              <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="w-4 h-4 text-gray-800 hover:text-red-700">
                <path stroke-linecap="round" stroke-linejoin="round" d="m14.74 9-.346 9m-4.788 0L9.26 9m9.968-3.21c.342.052.682.107 1.022.166m-1.022-.165L18.16 19.673a2.25 2.25 0 0 1-2.244 2.077H8.084a2.25 2.25 0 0 1-2.244-2.077L4.772 5.79m14.456 0a48.108 48.108 0 0 0-3.478-.397m-12 .562c.34-.059.68-.114 1.022-.165m0 0a48.11 48.11 0 0 1 3.478-.397m7.5 0v-.916c0-1.18-.91-2.164-2.09-2.201a51.964 51.964 0 0 0-3.32 0c-1.18.037-2.09 1.022-2.09 2.201v.916m7.5 0a48.667 48.667 0 0 0-7.5 0" />
              </svg>
            </button>
          </div>
        </div>
        <p v-if="aktiveHunde.length === 0" class="text-sm text-gray-500 italic">Keine aktiven Hunde vorhanden...</p>
      </div>
    </div>

    <!-- RECHTE SEITE: Gesperrte Hunde -->
    <div class="space-y-4">
      <h2 class="text-xl font-bold text-gray-800 mb-6">Gesperrte Hunde</h2>

      <div class="space-y-3">
        <div
          v-for="hund in gesperrteHunde"
          :key="hund.id"
          class="flex items-center justify-between p-3 rounded-full bg-[#D3DDD1] border border-gray-200 shadow-sm"
        >
          <div class="flex items-center gap-3">
            <img :src="hund.bild" class="w-10 h-10 rounded-full object-cover" />
            <div class="flex items-center gap-2">
              <span class="w-3 h-3 rounded-full" :class="hund.statusPunkt"></span>
              <div>
                <span class="font-bold text-gray-800 text-sm block leading-tight">{{ hund.name }}</span>
                <span class="text-[11px] text-gray-600 block leading-tight" v-if="hund.info">{{ hund.info }}</span>
              </div>
            </div>
          </div>

          <!-- Aktionen für gesperrte Hunde -->
          <div class="flex items-center gap-4 pr-2">
            <!-- Bearbeiten -->
            <button @click="bearbeitenHund(hund)" aria-label="Hund bearbeiten">
              <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="w-4 h-4 text-gray-800 hover:text-black">
                <path stroke-linecap="round" stroke-linejoin="round" d="m16.862 4.487 1.687-1.688a1.875 1.875 0 1 1 2.652 2.652L10.582 16.07a4.5 4.5 0 0 1-1.897 1.13L6 18l.8-2.685a4.5 4.5 0 0 1 1.13-1.897l8.932-8.931Zm0 0L19.5 7.125M18 14v4.75A2.25 2.25 0 0 1 15.75 21H5.25A2.25 2.25 0 0 1 3 18.75V8.25A2.25 2.25 0 0 1 5.25 6H10" />
              </svg>
            </button>
            <!-- Entsperren (Grüner Haken) -->
            <button @click="entsperreHund(hund.id)" aria-label="Hund entsperren">
              <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="2" stroke="currentColor" class="w-4 h-4 text-green-700 hover:text-green-900">
                <path stroke-linecap="round" stroke-linejoin="round" d="m4.5 12.75 6 6 9-13.5" />
              </svg>
            </button>
            <!-- Löschen -->
            <button @click="loescheGesperrtenHund(hund.id)" aria-label="Hund löschen">
              <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="w-4 h-4 text-gray-800 hover:text-red-700">
                <path stroke-linecap="round" stroke-linejoin="round" d="m14.74 9-.346 9m-4.788 0L9.26 9m9.968-3.21c.342.052.682.107 1.022.166m-1.022-.165L18.16 19.673a2.25 2.25 0 0 1-2.244 2.077H8.084a2.25 2.25 0 0 1-2.244-2.077L4.772 5.79m14.456 0a48.108 48.108 0 0 0-3.478-.397m-12 .562c.34-.059.68-.114 1.022-.165m0 0a48.11 48.11 0 0 1 3.478-.397m7.5 0v-.916c0-1.18-.91-2.164-2.09-2.201a51.964 51.964 0 0 0-3.32 0c-1.18.037-2.09 1.022-2.09 2.201v.916m7.5 0a48.667 48.667 0 0 0-7.5 0" />
              </svg>
            </button>
          </div>
        </div>
        <p v-if="gesperrteHunde.length === 0" class="text-sm text-gray-500 italic">Keine gesperrten Hunde...</p>
      </div>
    </div>

  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

// Aktive Hunde (Linke Seite)
const aktiveHunde = ref([
  { id: 1, name: 'Wambo', statusPunkt: 'bg-green-500', ursruenglicherPunkt: 'bg-green-500', bild: 'https://images.unsplash.com/photo-1543466835-00a7907e9de1' },
  { id: 2, name: 'Aris', statusPunkt: 'bg-green-500', ursruenglicherPunkt: 'bg-green-500', bild: 'https://images.unsplash.com/photo-1552053831-71594a27632d' },
  { id: 3, name: 'Mira', statusPunkt: 'bg-orange-400', ursruenglicherPunkt: 'bg-orange-400', bild: 'https://images.unsplash.com/photo-1537151608828-ea2b11777ee8' },
  { id: 4, name: 'Richy', statusPunkt: 'bg-orange-400', ursruenglicherPunkt: 'bg-orange-400', bild: 'https://images.unsplash.com/photo-1583511655857-d19b40a7a54e' },
  { id: 5, name: 'Roxy', statusPunkt: 'bg-green-500', ursruenglicherPunkt: 'bg-green-500', bild: 'https://images.unsplash.com/photo-1517849845537-4d257902454a' },
])

// Gesperrte Hunde (Rechte Seite)
const gesperrteHunde = ref([
  { id: 6, name: 'Bonny', statusPunkt: 'bg-red-500', ursruenglicherPunkt: 'bg-orange-400', info: 'Probewohnen - 05.07.', bild: 'https://images.unsplash.com/photo-1561948955-570b270e7c36' }
])

const bearbeitenHund = (hund) => {
  localStorage.setItem('editHund', JSON.stringify(hund))
  router.push('/app/admin/hundepflegen')
}

const geheZuPflegen = () => {
  localStorage.removeItem('editHund') // Damit das Formular garantiert leer ist
  router.push('/app/admin/hundepflegen')
}

// Hund sperren: Verschiebt nach rechts und setzt den Punkt auf rot
const sperreHund = (id) => {
  const index = aktiveHunde.value.findIndex(h => h.id === id)
  if (index !== -1) {
    const [hund] = aktiveHunde.value.splice(index, 1)
    hund.statusPunkt = 'bg-red-500' // Punkt wird rot
    gesperrteHunde.value.push(hund)
  }
}

// Hund entsperren: Verschiebt nach links und stellt die Originalfarbe des Punktes wieder her
const entsperreHund = (id) => {
  const index = gesperrteHunde.value.findIndex(h => h.id === id)
  if (index !== -1) {
    const [hund] = gesperrteHunde.value.splice(index, 1)
    hund.statusPunkt = hund.ursruenglicherPunkt // Ursprüngliche Farbe wiederherstellen
    aktiveHunde.value.push(hund)
  }
}

// Aktiven Hund löschen
const loescheHund = (id) => {
  aktiveHunde.value = aktiveHunde.value.filter(h => h.id !== id)
}

// Gesperrten Hund löschen
const loescheGesperrtenHund = (id) => {
  gesperrteHunde.value = gesperrteHunde.value.filter(h => h.id !== id)
}

</script>