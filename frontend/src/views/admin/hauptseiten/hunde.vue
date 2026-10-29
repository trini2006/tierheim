<template>
  <div class="max-w-6xl mx-auto p-4 md:p-6 grid grid-cols-1 md:grid-cols-2 gap-16 items-start">
    <!-- LINKE SEITE: Aktive Hunde -->
    <div class="space-y-4">
      <div class="flex items-center justify-between mb-6">
        <h2 class="text-xl font-bold text-gray-800">Aktive Hunde</h2>
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

          <!-- Aktionen (Nur Sperren) -->
          <div class="flex items-center gap-4 pr-2">
            <!-- Sperren (rotes Verbots-Symbol) -->
            <button @click="oeffneSperrModal(hund)" aria-label="Hund sperren">
              <svg
                xmlns="http://www.w3.org/2000/svg"
                fill="none"
                viewBox="0 0 24 24"
                stroke-width="1.5"
                stroke="currentColor"
                class="w-4 h-4 text-red-600 hover:text-red-800"
              >
                <path
                  stroke-linecap="round"
                  stroke-linejoin="round"
                  d="M18.364 18.364A9 9 0 0 0 5.636 5.636m12.728 12.728A9 9 0 0 1 5.636 5.636m12.728 12.728L5.636 5.636"
                />
              </svg>
            </button>
          </div>
        </div>
        <p v-if="aktiveHunde.length === 0" class="text-sm text-gray-500 italic">
          Keine aktiven Hunde vorhanden...
        </p>
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
                <span class="font-bold text-gray-800 text-sm block leading-tight">{{
                  hund.name
                }}</span>
                <span class="text-[11px] text-gray-600 block leading-tight" v-if="hund.info">{{
                  hund.info
                }}</span>
              </div>
            </div>
          </div>

          <!-- Aktionen für gesperrte Hunde (Nur Entsperren) -->
          <div class="flex items-center gap-4 pr-2">
            <!-- Entsperren (Grüner Haken) -->
            <button @click="entsperreHund(hund.id)" aria-label="Hund entsperren">
              <svg
                xmlns="http://www.w3.org/2000/svg"
                fill="none"
                viewBox="0 0 24 24"
                stroke-width="2"
                stroke="currentColor"
                class="w-4 h-4 text-green-700 hover:text-green-900"
              >
                <path stroke-linecap="round" stroke-linejoin="round" d="m4.5 12.75 6 6 9-13.5" />
              </svg>
            </button>
          </div>
        </div>
        <p v-if="gesperrteHunde.length === 0" class="text-sm text-gray-500 italic">
          Keine gesperrten Hunde...
        </p>
      </div>
    </div>

    <!-- SPERR-MODAL -->
    <div v-if="sperrModalAktiv" class="fixed inset-0 z-50 flex items-center justify-center bg-black bg-opacity-50 p-4">
      <div class="bg-white rounded-lg shadow-xl max-w-md w-full p-6 space-y-4">
        <h3 class="text-lg font-bold text-gray-800">Hund sperren: {{ ausgewaehlterHund?.name }}</h3>

        <div class="space-y-3">
          <div>
            <label class="block text-xs font-semibold text-gray-600 mb-1">Gesperrt von</label>
            <input
              type="date"
              v-model="sperrForm.gesperrtVon"
              @change="berechneBetroffeneReservierungen"
              class="w-full border border-gray-300 rounded p-2 text-sm"
            />
          </div>
          <div>
            <label class="block text-xs font-semibold text-gray-600 mb-1">Gesperrt bis</label>
            <input
              type="date"
              v-model="sperrForm.gesperrtBis"
              @change="berechneBetroffeneReservierungen"
              class="w-full border border-gray-300 rounded p-2 text-sm"
            />
          </div>
          <div>
            <label class="block text-xs font-semibold text-gray-600 mb-1">Sperrgrund (optional)</label>
            <textarea
              v-model="sperrForm.sperrGrund"
              placeholder="Grund für die Sperrung eingeben..."
              class="w-full border border-gray-300 rounded p-2 text-sm resize-none h-20"
            ></textarea>
          </div>

          <!-- Information zu betroffenen Reservierungen -->
          <div class="p-3 bg-gray-50 border border-gray-200 rounded text-sm text-gray-700">
            <span v-if="laedtBetroffene">Betroffene Reservierungen werden ermittelt...</span>
            <span v-else>
              Von dieser Sperrung wären <strong>{{ betroffeneReservierungenAnzahl }}</strong> Reservierung(en) betroffen.
            </span>
          </div>
        </div>

        <div class="flex justify-end gap-3 pt-2">
          <button
            @click="sperrModalAktiv = false"
            class="px-4 py-2 bg-gray-200 text-gray-800 text-sm rounded hover:bg-gray-300"
          >
            Abbrechen
          </button>
          <button
            @click="bestätigeSperrung"
            class="px-4 py-2 bg-red-600 text-white text-sm rounded hover:bg-red-700 font-semibold"
          >
            Sperrung bestätigen
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'

const API_BASE = 'http://localhost:8085'
const tierheimId = 1

// Aktive Hunde (Linke Seite)
const aktiveHunde = ref([])
// Gesperrte Hunde (Rechte Seite)
const gesperrteHunde = ref([])

// Modal-Zustände
const sperrModalAktiv = ref(false)
const ausgewaehlterHund = ref(null)
const betroffeneReservierungenAnzahl = ref(0)
const laedtBetroffene = ref(false)

const sperrForm = ref({
  gesperrtVon: '',
  gesperrtBis: '',
  sperrGrund: ''
})

const farbeFuerErfahrung = (h) => (h.erfahrung ? 'bg-orange-500' : 'bg-green-500')

// Hunde beim Laden der Komponente vom Backend abrufen
const ladeHunde = async () => {
  try {
    const response = await fetch(`${API_BASE}/hund/${tierheimId}/all`)
    if (response.ok) {
      const heute = new Date().toLocaleDateString('sv-SE')
      const isAktuellGesperrt = (h) =>
        h.istGesperrt && h.gesperrtVon <= heute && h.gesperrtBis >= heute

      const alleHunde = await response.json()

      aktiveHunde.value = alleHunde
        .filter((h) => !isAktuellGesperrt(h))
        .map((h) => ({...h, statusPunkt: farbeFuerErfahrung(h)}))

      gesperrteHunde.value = alleHunde
        .filter((h) => isAktuellGesperrt(h))
        .map((h) => ({...h, statusPunkt: 'bg-red-500', info: h.sperrGrund || 'Gesperrt'}))
    } else {
      console.error('Fehler beim Laden der Hunde')
    }
  } catch (error) {
    console.error('Verbindungsfehler zum Backend:', error)
  }
}

onMounted(() => {
  ladeHunde()
})

// Modal für Sperrung öffnen und Standarddaten initialisieren
const oeffneSperrModal = async (hund) => {
  ausgewaehlterHund.value = hund

  const heute = new Date()
  const morgen = new Date()
  morgen.setDate(heute.getDate() + 1)

  sperrForm.value = {
    gesperrtVon: heute.toLocaleDateString('sv-SE'),
    gesperrtBis: morgen.toLocaleDateString('sv-SE'),
    sperrGrund: ''
  }

  sperrModalAktiv.value = true
  await berechneBetroffeneReservierungen()
}

// Betroffene Reservierungen vom Backend abfragen
const berechneBetroffeneReservierungen = async () => {
  if (!ausgewaehlterHund.value) return
  laedtBetroffene.value = true
  try {
    const response = await fetch(
      `${API_BASE}/reservierung/all?hundId=${ausgewaehlterHund.value.id}&von=${sperrForm.value.gesperrtVon}&bis=${sperrForm.value.gesperrtBis}`
    )
    if (response.ok) {
      const data = await response.json()
      betroffeneReservierungenAnzahl.value = data.length || 0
    } else {
      betroffeneReservierungenAnzahl.value = 0
    }
  } catch (error) {
    console.error('Fehler beim Ermitteln der betroffenen Reservierungen:', error)
    betroffeneReservierungenAnzahl.value = 0
  } finally {
    laedtBetroffene.value = false
  }
}

// Hund sperren: Schickt ein PUT-Request an das Backend und schließt das Modal
const bestätigeSperrung = async () => {
  if (!ausgewaehlterHund.value) return
  try {
    const sperrDaten = {
      gesperrtVon: sperrForm.value.gesperrtVon,
      gesperrtBis: sperrForm.value.gesperrtBis,
      sperrGrund: sperrForm.value.sperrGrund?.trim() || 'Gesperrt durch Admin',
    }

    const response = await fetch(`${API_BASE}/hund/sperren/${ausgewaehlterHund.value.id}`, {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(sperrDaten),
    })

    if (response.ok) {
      sperrModalAktiv.value = false
      await ladeHunde()
    } else {
      alert(await response.text())
    }
  } catch (error) {
    console.error('Fehler beim Sperren des Hundes:', error)
  }
}

// Hund entsperren: Schickt ein PUT-Request an das Backend und verschiebt ihn nach links
const entsperreHund = async (id) => {
  try {
    const response = await fetch(`${API_BASE}/hund/entsperren/${id}`, {
      method: 'PUT',
    })

    if (response.ok) {
      await ladeHunde()
    } else {
      alert(await response.text())
    }
  } catch (error) {
    console.error('Fehler beim Entsperren des Hundes:', error)
  }
}
</script>
