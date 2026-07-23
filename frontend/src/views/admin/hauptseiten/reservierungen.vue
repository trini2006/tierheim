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

        <!-- Zeitslots-Matrix (Nicht anklickbar, gleicher Stil) -->
        <div class="divide-y divide-gray-50">
          <div v-for="slot in zeitSlots" :key="slot" class="grid grid-cols-8 gap-4 items-center py-3">
            
            <!-- Zeitspalte links im einheitlichen Stil -->
            <div class="bg-[#D3DDD1] text-gray-800 font-medium text-xs py-2 px-3 rounded-xl text-center shadow-sm border border-gray-200/50">
              {{ slot }}
            </div>

            <!-- Spalten für jeden Hund (Nur Anzeige) -->
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
import { ref, computed } from 'vue'

// Hilfsfunktion: Berechnet die Kalenderwoche (KW) für ein Datum
const getKW = (d) => {
  const date = new Date(Date.UTC(d.getFullYear(), d.getMonth(), d.getDate()))
  date.setUTCDate(date.getUTCDate() + 4 - (date.getUTCDay() || 7))
  const yearStart = new Date(Date.UTC(date.getUTCFullYear(), 0, 1))
  return Math.ceil(((date - yearStart) / 86400000 + 1) / 7)
}

const heute = new Date()
const aktuelleKWNummer = getKW(heute)

// Kalenderwochen dynamisch um die aktuelle Woche herum aufgebaut
const kalenderWochen = ref([
  { nummer: aktuelleKWNummer },
  { nummer: aktuelleKWNummer + 1 },
  { nummer: aktuelleKWNummer + 2 }
])

const aktiveKW = ref(aktuelleKWNummer)
const aktuellesDatumObj = ref(new Date(heute))

// Hilfsfunktion: Gibt den Montag der gewählten KW zurück
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

// Kalenderwoche wechseln
const waehleKW = (kwNummer) => {
  aktiveKW.value = kwNummer
  const montag = getMontagDerKW(kwNummer, heute.getFullYear())
  
  // Wenn es die aktuelle KW ist, springe auf heute. Ansonsten auf den Montag der KW.
  if (kwNummer === aktuelleKWNummer) {
    aktuellesDatumObj.value = new Date(heute)
  } else {
    aktuellesDatumObj.value = new Date(montag)
  }
}

// Prüfen ob man einen Tag zurückschalten darf (nicht vor heute und nicht vor KW-Montag)
const istErlaubterTagZurueck = computed(() => {
  const montagKW = getMontagDerKW(aktiveKW.value, heute.getFullYear())
  
  // Berechne den gestrigen/vorherigen Tag
  const vorherigesDatum = new Date(aktuellesDatumObj.value)
  vorherigesDatum.setDate(vorherigesDatum.getDate() - 1)

  // Sperren wenn der vorige Tag vor dem Montag der KW liegt ODER vor dem echten "heute"
  if (vorherigesDatum < montagKW) return true
  if (aktiveKW.value === aktuelleKWNummer && vorherigesDatum < new Date(heute.getFullYear(), heute.getMonth(), heute.getDate())) {
    return true
  }
  return false
})

// Prüfen ob man am Sonntag (letzter Tag der KW) ist
const istLetzterTagDerWoche = computed(() => {
  const d = new Date(aktuellesDatumObj.value)
  return d.getDay() === 0 // Sonntag
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

// Datum formatieren für den Button (z.B. "Mo 29.06.")
const formatiertesDatum = computed(() => {
  const wochentage = ['So', 'Mo', 'Di', 'Mi', 'Do', 'Fr', 'Sa']
  const tagWort = wochentage[aktuellesDatumObj.value.getDay()]
  const tagZahl = String(aktuellesDatumObj.value.getDate()).padStart(2, '0')
  const monat = String(aktuellesDatumObj.value.getMonth() + 1).padStart(2, '0')
  return `${tagWort} ${tagZahl}.${monat}.`
})

// Datum formatieren für die Überschrift Mobil (z.B. "Montag, 29.06.2026")
const wochenTagString = computed(() => {
  const options = { weekday: 'long', year: 'numeric', month: '2-digit', day: '2-digit' }
  return aktuellesDatumObj.value.toLocaleDateString('de-DE', options)
})

// Alle Hunde für die Desktop-Matrix
const alleHunde = ref([
  { id: 1, name: 'Wambo', bild: 'https://images.unsplash.com/photo-1543466835-00a7907e9de1' },
  { id: 2, name: 'Aris', bild: 'https://images.unsplash.com/photo-1552053831-71594a27632d' },
  { id: 3, name: 'Luna', bild: 'https://images.unsplash.com/photo-1537151608828-ea2b11777ee8' },
  { id: 4, name: 'DC', bild: 'https://images.unsplash.com/photo-1583511655857-d19b40a7a54e' },
  { id: 5, name: 'Pai', bild: 'https://images.unsplash.com/photo-1517849845537-4d257902454a' },
  { id: 6, name: 'Richy', bild: 'https://images.unsplash.com/photo-1561948955-570b270e7c36' },
  { id: 7, name: 'Roxy', bild: 'https://images.unsplash.com/photo-1534361960057-19889db9621e' },
])

// Zeitslots
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

// Reservierungsdaten (Mock-Daten)
const reservierungen = ref([
  { id: 1, hundId: 2, hundName: 'Aris', telefon: '1234555678', zeit: '12:00 - 13:30Uhr', bild: 'https://images.unsplash.com/photo-1552053831-71594a27632d', slot: '12:00 - 12:30' },
  { id: 2, hundId: 2, hundName: 'Aris', telefon: '1234555678', zeit: '12:00 - 13:30Uhr', bild: 'https://images.unsplash.com/photo-1552053831-71594a27632d', slot: '12:30 - 13:00' },
  { id: 3, hundId: 2, hundName: 'Aris', telefon: '1234555678', zeit: '12:00 - 13:30Uhr', bild: 'https://images.unsplash.com/photo-1552053831-71594a27632d', slot: '13:00 - 13:30' },
  { id: 4, hundId: 1, hundName: 'Wambo', telefon: '1223455678', zeit: '10:00 - 11:30Uhr', bild: 'https://images.unsplash.com/photo-1543466835-00a7907e9de1', slot: '10:00 - 10:30' },
  { id: 5, hundId: 1, hundName: 'Wambo', telefon: '1223455678', zeit: '10:00 - 11:30Uhr', bild: 'https://images.unsplash.com/photo-1543466835-00a7907e9de1', slot: '10:30 - 11:00' },
])

// Filtert die Liste für die mobile Ansicht
const aktuelleReservierungen = computed(() => {
  const uniqueMap = new Map()
  reservierungen.value.forEach(r => {
    if (!uniqueMap.has(r.hundId)) {
      uniqueMap.set(r.hundId, r)
    }
  })
  return Array.from(uniqueMap.values())
})

// Prüfen ob ein Slot für Desktop grün markiert ist
const istReserviert = (hundId, slot) => {
  return reservierungen.value.some(r => r.hundId === hundId && r.slot === slot)
}

// Mobil: Reservierung löschen
const loescheReservierung = (id) => {
  const res = reservierungen.value.find(r => r.id === id)
  if (res) {
    reservierungen.value = reservierungen.value.filter(r => r.hundId !== res.hundId)
  }
}
</script>