<template>
    <div class="max-w-6xl mx-auto p-4 md:p-6">
      
      <!-- Haupt-Grid: Links Mitglieder, Rechts Zeitwahl -->
      <div class="grid grid-cols-1 md:grid-cols-2 gap-8 items-start">
        
        <!-- LINKE SEITE: Mitglieder Auswahl -->
        <div class="space-y-4">
          <h2 class="text-xl font-bold text-gray-800">Wähle ein Mitglied aus...</h2>
          
          <div class="space-y-3">
            <div 
              v-for="mitglied in mitgliederListe" 
              :key="mitglied.id"
              @click="waehleMitglied(mitglied)"
              class="flex items-center justify-between p-4 rounded-3xl shadow-sm border cursor-pointer transition-all"
              :class="ausgewaehltesMitglied?.id === mitglied.id ? 'bg-[#b8c9b4] border-emerald-700 ring-2 ring-emerald-600' : 'bg-[#D3DDD1] border-gray-200 hover:bg-[#c2cebf]'"
            >
              <div class="flex items-center gap-3">
                <div class="w-10 h-10 rounded-full bg-emerald-900 flex items-center justify-center text-white font-bold">
                  👤
                </div>
                <span class="font-bold text-gray-800 text-sm md:text-base">{{ mitglied.telefon }}</span>
              </div>
              <span class="w-3 h-3 rounded-full" :class="mitglied.aktiv ? 'bg-green-500' : 'bg-orange-400'"></span>
            </div>

            <p v-if="mitgliederListe.length === 0" class="text-sm text-gray-500 italic">
              Keine Mitglieder gefunden.
            </p>
          </div>
        </div>
 
        <!-- RECHTE SEITE: Zeitwahl (Mehrfachauswahl, angelehnt an Bild 1) -->
        <div :class="{'opacity-50 pointer-events-none': !ausgewaehltesMitglied}" class="transition-opacity space-y-4">
          
          <!-- KW Auswahl zentriert über dem Datums-Block -->
          <div class="flex justify-center gap-2">
            <button 
              v-for="kw in weeks" 
              :key="kw.number" 
              @click="selectWeek(kw)"
              class="px-4 py-2 rounded-xl text-xs font-bold transition-colors shadow-sm"
              :class="selectedWeek.number === kw.number ? 'bg-[#4A6B5D] text-white' : 'bg-gray-200 text-gray-700 hover:bg-gray-300'"
            >
              KW {{ kw.number }}
            </button>
          </div>
 
          <!-- Datum-Navigationsleiste (wie in Bild 1) -->
          <div class="flex items-center justify-between bg-gray-50 p-4 rounded-2xl border border-gray-100 shadow-sm">
            <button @click="changeDay(-1)" :disabled="isToday" class="p-2 rounded-xl bg-white shadow-sm font-bold text-blue-600 hover:bg-gray-100 disabled:opacity-30">
              ◀
            </button>
            <span class="text-base font-bold text-gray-900">
              {{ format(currentDate, 'EEEE dd.MM.', { locale: de }) }}
            </span>
            <button @click="changeDay(1)" :disabled="isEndOfWeek" class="p-2 rounded-xl bg-white shadow-sm font-bold text-blue-600 hover:bg-gray-100 disabled:opacity-30">
              ▶
            </button>
          </div>
 
          <!-- Zeitblöcke (Mehrfachauswahl per Array) -->
          <div class="space-y-3 pt-2">
            <div v-for="zeit in zeitBloecke" :key="zeit.id" class="flex items-center justify-between">
              <span class="text-xs font-semibold text-gray-700 w-28">{{ zeit.label }}</span>
              <div 
                @click="toggleZeit(zeit.id)"
                class="flex-1 ml-4 h-12 rounded-2xl cursor-pointer transition-colors flex items-center px-4 text-xs font-bold shadow-sm"
                :class="[
                  zeit.booked ? 'bg-red-800 cursor-not-allowed text-white' : 
                  ausgewaehlteZeiten.includes(zeit.id) ? 'bg-emerald-700 text-white' : 'bg-gray-200 hover:bg-gray-300 text-gray-700'
                ]"
              >
                {{ zeit.booked ? 'Belegt' : ausgewaehlteZeiten.includes(zeit.id) ? 'Ausgewählt' : '' }}
              </div>
            </div>
          </div>
 
        </div>
 
      </div>
 
      <!-- UNTERER BEREICH: Weiter-Button -->
      <div class="flex justify-end mt-8">
        <button 
          @click="weiterZuHundewahl"
          :disabled="!ausgewaehltesMitglied || ausgewaehlteZeiten.length === 0"
          class="flex items-center gap-3 px-8 py-3 rounded-full shadow-md font-bold transition-all"
          :class="(!ausgewaehltesMitglied || ausgewaehlteZeiten.length === 0) ? 'bg-gray-300 text-gray-500 cursor-not-allowed' : 'bg-[#D3DDD1] hover:bg-[#c2cebf] text-gray-800 border border-gray-300'"
        >
          <span>Weiter</span>
          <div class="w-8 h-8 rounded-full bg-emerald-600 flex items-center justify-center text-white">
            ✓
          </div>
        </button>
      </div>
 
    </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { addDays, subDays, getISOWeek, startOfWeek, endOfWeek, addWeeks, format, startOfToday, isSameDay } from 'date-fns'
import { de } from 'date-fns/locale'
 
const router = useRouter()
const today = startOfToday()
const currentDate = ref(today)
 
// Beispiel-Mitglied zum Testen
const mitgliederListe = ref([
  { id: 9999, telefon: 'Beispiel-Mitglied (0176123456)', aktiv: true }
])
const ausgewaehltesMitglied = ref(null)
const ausgewaehlteZeiten = ref([])
const zeitBloecke = ref([])
const gassiZeitenBackend = ref([])

const tagesMapping = ['SONNTAG', 'MONTAG', 'DIENSTAG', 'MITTWOCH', 'DONNERSTAG', 'FREITAG', 'SAMSTAG']

// Kalenderwochen (aktuelle Woche + die nächsten 2)
const weeks = [0, 1, 2].map(i => ({ 
  number: getISOWeek(addWeeks(today, i)),
  startDate: startOfWeek(addWeeks(today, i), { weekStartsOn: 1 }) 
}))
const selectedWeek = ref(weeks[0])

const isToday = computed(() => isSameDay(currentDate.value, today))
const isEndOfWeek = computed(() => {
  const end = endOfWeek(selectedWeek.value.startDate, { weekStartsOn: 1 })
  return isSameDay(currentDate.value, end)
})

const selectWeek = (kw) => {
  selectedWeek.value = kw
  currentDate.value = kw.startDate < today ? today : kw.startDate
  ausgewaehlteZeiten.value = []
  aktualisiereSlotsFuerAktuellenTag()
}

const changeDay = (dir) => {
  const nextDate = dir > 0 ? addDays(currentDate.value, 1) : subDays(currentDate.value, 1)
  if (nextDate >= today && nextDate >= selectedWeek.value.startDate && nextDate <= endOfWeek(selectedWeek.value.startDate, { weekStartsOn: 1 })) {
    currentDate.value = nextDate
    ausgewaehlteZeiten.value = []
    aktualisiereSlotsFuerAktuellenTag()
  }
}

// Mitglieder vom Backend laden und ans Beispiel anhängen
const ladeMitglieder = async () => {
  try {
    const res = await fetch('/mitglied/all')
    if (res.ok) {
      const data = await res.json()
      const backendMitglieder = data.map((m) => ({
        id: m.id ?? m.mitgliedsnummer,
        telefon: String(m.telefon || m.mitgliedsnummer),
        aktiv: typeof m.erfahrung === 'boolean' ? m.erfahrung : (m.erfahrung === 'true' || m.erfahrung === true),
      }))
      if (backendMitglieder.length > 0) {
        mitgliederListe.value = [...mitgliederListe.value, ...backendMitglieder]
      }
    }
  } catch (e) {
    console.error('Fehler beim Laden der Mitglieder (Beispiel-Mitglied bleibt erhalten):', e)
  }
}

const ladeZeitenVomBackend = async () => {
  try {
    const res = await fetch('/gassiGehZeiten/all')
    if (res.ok) {
      gassiZeitenBackend.value = await res.json()
      aktualisiereSlotsFuerAktuellenTag()
    }
  } catch (e) {
    console.error('Fehler beim Laden der Gassi-Geh-Zeiten:', e)
  }
}

const aktualisiereSlotsFuerAktuellenTag = () => {
  const wochentagEnum = tagesMapping[currentDate.value.getDay()]
  const tagesZeiten = gassiZeitenBackend.value.filter(g => g.tag === wochentagEnum)

  if (tagesZeiten.length > 0) {
    zeitBloecke.value = tagesZeiten.map((g, index) => {
      const vonStr = g.von ? g.von.substring(0, 5) : '10:00'
      const bisStr = g.bis ? g.bis.substring(0, 5) : '10:30'
      return {
        id: index + 1,
        label: `${vonStr} - ${bisStr} Uhr`,
        vonFull: g.von ? (g.von.length === 5 ? g.von + ':00' : g.von) : '10:00:00',
        bisFull: g.bis ? (g.bis.length === 5 ? g.bis + ':00' : g.bis) : '10:30:00',
        booked: false
      }
    })
  } else {
    zeitBloecke.value = [
      { id: 1, label: '10:00 - 10:30 Uhr', vonFull: '10:00:00', bisFull: '10:30:00', booked: false },
      { id: 2, label: '10:30 - 11:00 Uhr', vonFull: '10:30:00', bisFull: '11:00:00', booked: false },
      { id: 3, label: '11:00 - 11:30 Uhr', vonFull: '11:00:00', bisFull: '11:30:00', booked: false },
      { id: 4, label: '11:30 - 12:00 Uhr', vonFull: '11:30:00', bisFull: '12:00:00', booked: false },
    ]
  }
}

onMounted(() => {
  ladeMitglieder()
  ladeZeitenVomBackend()
})
 
function waehleMitglied(mitglied) {
  ausgewaehltesMitglied.value = mitglied
}
 
function toggleZeit(zeitId) {
  if (!ausgewaehltesMitglied.value) return
  const slot = zeitBloecke.value.find(z => z.id === zeitId)
  if (slot && slot.booked) return
  
  const index = ausgewaehlteZeiten.value.indexOf(zeitId)
  if (index > -1) {
    ausgewaehlteZeiten.value.splice(index, 1)
  } else {
    ausgewaehlteZeiten.value.push(zeitId)
  }
}
 
function weiterZuHundewahl() {
  if (ausgewaehltesMitglied.value && ausgewaehlteZeiten.value.length > 0) {
    const selectedObjects = zeitBloecke.value.filter(z => ausgewaehlteZeiten.value.includes(z.id))
                                              .sort((a, b) => a.id - b.id)
    
    const vonZeit = selectedObjects[0].vonFull
    const bisZeit = selectedObjects[selectedObjects.length - 1].bisFull

    const daten = {
      mitgliedId: ausgewaehltesMitglied.value.id,
      telefon: ausgewaehltesMitglied.value.telefon,
      datum: format(currentDate.value, 'yyyy-MM-dd'),
      von: vonZeit,
      bis: bisZeit,
      start: vonZeit,
      end: bisZeit,
      zeiten: ausgewaehlteZeiten.value
    }
    localStorage.setItem('terminData', JSON.stringify(daten))
    router.push('/app/admin/hundewahl')
  }
}
</script>