<template>
  <div class="max-w-7xl mx-auto p-4 md:p-6 space-y-6 bg-white min-h-screen">
    
    <!-- HEADER: KW-Auswahl, Datum-Navigation & Archiv-Button -->
    <div class="flex flex-col md:flex-row items-center justify-between gap-4">
      
      <!-- Kalenderwochen (KW) -->
      <div class="flex items-center gap-2 flex-wrap justify-center">
        <button 
          v-for="kw in kalenderWochen" 
          :key="kw.nummer"
          @click="waehleKW(kw.nummer)"
          :class="['px-4 py-2 rounded-xl font-medium text-sm transition-colors', aktiveKW === kw.nummer ? 'bg-[#8FA18C] text-white shadow' : 'bg-[#D3DDD1] text-gray-700 hover:bg-[#c2cebf]']"
        >
          KW {{ kw.nummer }}
        </button>
      </div>

      <!-- Tag / Datum Blätterer mit Pfeilen -->
      <div class="flex items-center gap-3">
        <!-- Zurück-Pfeil (Deaktiviert, wenn man am Wochenanfang oder heutigen Tag ist) -->
        <button 
          @click="tagZurueck" 
          :disabled="istErlaubterTagZurueck"
          :class="['p-1 rounded-full transition-colors', istErlaubterTagZurueck ? 'opacity-30 cursor-not-allowed' : 'hover:bg-gray-100']"
          aria-label="Vorheriger Tag"
        >
          <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="2.5" stroke="currentColor" class="w-5 h-5 text-gray-800">
            <path stroke-linecap="round" stroke-linejoin="round" d="M15.75 19.5 8.25 12l7.5-7.5" />
          </svg>
        </button>
        
        <div class="px-5 py-2 bg-[#8FA18C] text-white font-medium rounded-xl shadow-sm text-sm">
          {{ formatiertesDatum }}
        </div>

        <!-- Vor-Pfeil (Deaktiviert am Ende der KW) -->
        <button 
          @click="tagVorkommen" 
          :disabled="istLetzterTagDerWoche"
          :class="['p-1 rounded-full transition-colors', istLetzterTagDerWoche ? 'opacity-30 cursor-not-allowed' : 'hover:bg-gray-100']"
          aria-label="Nächster Tag"
        >
          <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="2.5" stroke="currentColor" class="w-5 h-5 text-gray-800">
            <path stroke-linecap="round" stroke-linejoin="round" d="m8.25 4.5 7.5 7.5-7.5 7.5" />
          </svg>
        </button>
      </div>

      <!-- Archiv Button -->
      <div class="hidden md:block">
        <button class="px-4 py-2 bg-[#D3DDD1] hover:bg-[#c2cebf] text-gray-700 rounded-xl font-medium text-sm transition-colors">
          Archiv
        </button>
      </div>
    </div>


    <!-- ========================================== -->
    <!-- MOBILE ANSICHT (Listen-Layout) -->
    <!-- ========================================== -->
    <div class="block md:hidden space-y-4">
      <h3 class="text-lg font-bold text-gray-800 text-center mb-4">{{ wochenTagString }}</h3>

      <div class="space-y-3">
        <div 
          v-for="res in aktuelleReservierungen" 
          :key="res.id"
          class="flex items-center justify-between p-3 rounded-full bg-[#D3DDD1] shadow-sm border border-gray-200"
        >
          <div class="flex items-center gap-3">
            <img :src="res.bild" class="w-11 h-11 rounded-full object-cover" />
            <div>
              <span class="font-bold text-gray-800 text-sm block leading-tight">{{ res.hundName }}, {{ res.telefon }}</span>
              <span class="text-xs text-gray-700 block leading-tight mt-0.5">{{ res.zeit }}</span>
            </div>
          </div>

          <!-- Rotes Verbots-Symbol zum Löschen -->
          <button @click="loescheReservierung(res.id)" class="pr-2" aria-label="Reservierung stornieren">
            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="w-6 h-6 text-red-600 hover:text-red-800">
              <path stroke-linecap="round" stroke-linejoin="round" d="M18.364 18.364A9 9 0 0 0 5.636 5.636m12.728 12.728A9 9 0 0 1 5.636 5.636m12.728 12.728L5.636 5.636" />
            </svg>
          </button>
        </div>

        <p v-if="aktuelleReservierungen.length === 0" class="text-center text-gray-500 italic py-8">
          Keine Reservierungen für diesen Tag.
        </p>
      </div>
    </div>


    <!-- ========================================================== -->
    <!-- TABLET & DESKTOP ANSICHT (Matrix-Layout) -->
    <!-- ========================================================== -->
    <div class="hidden md:block overflow-x-auto pt-4">
      <div class="min-w-[800px]">
        
        <!-- Hundereihe oben -->
        <div class="grid grid-cols-8 gap-4 items-end pb-6 border-b border-gray-100">
          <div class="text-xs text-gray-400">Zeit / Hund</div>
          <div v-for="hund in alleHunde" :key="hund.id" class="text-center space-y-2">
            <img :src="hund.bild" class="w-14 h-14 rounded-full object-cover mx-auto shadow-sm border" />
            <span class="font-bold text-sm text-gray-800 block truncate">{{ hund.name }}</span>
          </div>
        </div>

        <!-- Zeitslots-Matrix -->
        <div class="divide-y divide-gray-50">
          <div v-for="slot in zeitSlots" :key="slot" class="grid grid-cols-8 gap-4 items-center py-3">
            
            <div class="bg-[#D3DDD1] text-gray-800 font-medium text-xs py-2 px-3 rounded-xl text-center shadow-sm border border-gray-200/50">
              {{ slot }}
            </div>

            <div v-for="hund in alleHunde" :key="hund.id" class="flex justify-center">
              <div 
                :class="[
                  'w-full h-10 rounded-xl border border-gray-200/60 shadow-sm transition-colors',
                  istReserviert(hund.id, slot) ? 'bg-[#7A9A75]' : 'bg-[#E5E9E4]'
                ]"
              ></div>
            </div>

          </div>
        </div>

      </div>
    </div>

  </div>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue'

const getKW = (d) => {
  const date = new Date(Date.UTC(d.getFullYear(), d.getMonth(), d.getDate()))
  date.setUTCDate(date.getUTCDate() + 4 - (date.getUTCDay() || 7))
  const yearStart = new Date(Date.UTC(date.getUTCFullYear(), 0, 1))
  return Math.ceil(((date - yearStart) / 86400000 + 1) / 7)
}

const heute = new Date()
const aktuelleKWNummer = getKW(heute)

const kalenderWochen = ref([
  { nummer: aktuelleKWNummer },
  { nummer: aktuelleKWNummer + 1 },
  { nummer: aktuelleKWNummer + 2 }
])

const aktiveKW = ref(aktuelleKWNummer)
const aktuellesDatumObj = ref(new Date(heute))

const getMontagDerKW = (kw, jahr) => {
  const simple = new Date(jahr, 0, 1 + (kw - 1) * 7)
  const dow = simple.getDay()
  const ISOweekStart = new Date(simple)
  if (dow <= 4) {
    ISOweekStart.setDate(simple.getDate() - simple.getDay() + 1)
  } else {
    ISOweekStart.setDate(simple.getDate() + 8 - simple.getDay())
  }
  return ISOweekStart
}

const waehleKW = (kwNummer) => {
  aktiveKW.value = kwNummer
  const montag = getMontagDerKW(kwNummer, heute.getFullYear())
  
  if (kwNummer === aktuelleKWNummer) {
    aktuellesDatumObj.value = new Date(heute)
  } else {
    aktuellesDatumObj.value = new Date(montag)
  }
}

const istErlaubterTagZurueck = computed(() => {
  const montagKW = getMontagDerKW(aktiveKW.value, heute.getFullYear())
  const vorherigesDatum = new Date(aktuellesDatumObj.value)
  vorherigesDatum.setDate(vorherigesDatum.getDate() - 1)

  if (vorherigesDatum < montagKW) return true
  if (aktiveKW.value === aktuelleKWNummer && vorherigesDatum < new Date(heute.getFullYear(), heute.getMonth(), heute.getDate())) {
    return true
  }
  return false
})

const istLetzterTagDerWoche = computed(() => {
  const d = new Date(aktuellesDatumObj.value)
  return d.getDay() === 0
})

const tagZurueck = () => {
  if (!istErlaubterTagZurueck.value) {
    aktuellesDatumObj.value.setDate(aktuellesDatumObj.value.getDate() - 1)
    aktuellesDatumObj.value = new Date(aktuellesDatumObj.value)
  }
}

const tagVorkommen = () => {
  if (!istLetzterTagDerWoche.value) {
    aktuellesDatumObj.value.setDate(aktuellesDatumObj.value.getDate() + 1)
    aktuellesDatumObj.value = new Date(aktuellesDatumObj.value)
  }
}

const formatiertesDatum = computed(() => {
  const wochentage = ['So', 'Mo', 'Di', 'Mi', 'Do', 'Fr', 'Sa']
  const tagWort = wochentage[aktuellesDatumObj.value.getDay()]
  const tagZahl = String(aktuellesDatumObj.value.getDate()).padStart(2, '0')
  const monat = String(aktuellesDatumObj.value.getMonth() + 1).padStart(2, '0')
  return `${tagWort} ${tagZahl}.${monat}.`
})

const wochenTagString = computed(() => {
  const options = { weekday: 'long', year: 'numeric', month: '2-digit', day: '2-digit' }
  return aktuellesDatumObj.value.toLocaleDateString('de-DE', options)
})

const alleHunde = ref([])
const reservierungen = ref([])

const zeitSlots = [
  '10:00 - 10:30',
  '10:30 - 11:00',
  '11:00 - 11:30',
  '11:30 - 12:00',
  '12:00 - 12:30',
  '12:30 - 13:00',
  '13:00 - 13:30',
  '13:30 - 14:00'
]

// Daten vom Backend laden
const ladeDaten = async () => {
  try {
    // 1. Hunde laden (für die Matrix-Spalten)
    const hundeRes = await fetch('/hund/all')
    if (hundeRes.ok) {
      const hundeData = await hundeRes.json()
      alleHunde.value = hundeData.map(h => ({
        id: h.id,
        name: h.name,
        bild: h.bild || 'https://images.unsplash.com/photo-1543466835-00a7907e9de1'
      }))
    }

    // 2. Reservierungen laden (mit dem aktuellen Datum als Filter)
    const datumString = aktuellesDatumObj.value.toISOString().split('T')[0]
    const resRes = await fetch(`/reservierung/all?von=${datumString}&bis=${datumString}`)
    if (resRes.ok) {
      const resData = await resRes.json()
      
      reservierungen.value = resData
        .filter(r => r.status === 'AKTIV') // Nur aktive Reservierungen anzeigen
        .map(r => {
          // Extrahiere Uhrzeit / Slot aus den Start- und Endzeiten der Reservierung
          const startZeit = r.startZeit ? r.startZeit.substring(0, 5) : '10:00'
          const endZeit = r.endZeit ? r.endZeit.substring(0, 5) : '10:30'
          const slotStr = `${startZeit} - ${endZeit}`

          return {
            id: r.id,
            hundId: r.hund?.id,
            hundName: r.hund?.name || 'Unbekannt',
            telefon: r.mitglied?.mitgliedsnummer ? String(r.mitglied.mitgliedsnummer) : '',
            zeit: `${slotStr}Uhr`,
            bild: r.hund?.bild || 'https://images.unsplash.com/photo-1543466835-00a7907e9de1',
            slot: slotStr
          }
        })
    }
  } catch (e) {
    console.error('Fehler beim Laden der Reservierungsdaten:', e)
  }
}

// Bei initialem Laden und wenn sich das Datum ändert, Daten neu laden
onMounted(ladeDaten)
watch(aktuellesDatumObj, () => {
  ladeDaten()
})

const aktuelleReservierungen = computed(() => {
  const uniqueMap = new Map()
  reservierungen.value.forEach(r => {
    if (!uniqueMap.has(r.hundId)) {
      uniqueMap.set(r.hundId, r)
    }
  })
  return Array.from(uniqueMap.values())
})

const istReserviert = (hundId, slot) => {
  return reservierungen.value.some(r => r.hundId === hundId && r.slot === slot)
}

const loescheReservierung = async (id) => {
  try {
    const grund = prompt('Bitte geben Sie einen Stornierungsgrund ein:', 'Storniert durch Admin')
    if (grund === null) return // Abgebrochen

    const res = await fetch(`/reservierung/${id}?grund=${encodeURIComponent(grund)}`, {
      method: 'DELETE'
    })
    if (res.ok) {
      // Nach erfolgreicher Stornierung die Liste aktualisieren
      await ladeDaten()
    }
  } catch (e) {
    console.error('Fehler beim Stornieren der Reservierung:', e)
  }
}
</script>