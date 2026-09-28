<template>
  <div class="max-w-md md:max-w-3xl mx-auto p-4 md:p-8 space-y-6 md:space-y-8">
    
    <!-- KW Auswahl -->
    <div class="flex gap-2 md:gap-4 justify-center">
      <button 
        v-for="kw in weeks" 
        :key="kw.number" 
        @click="selectWeek(kw)"
        :class="['px-4 md:px-8 py-2 md:py-3 rounded md:rounded-lg text-sm md:text-lg font-medium transition', 
                 selectedWeek.number === kw.number ? 'bg-[#536B53] text-white' : 'bg-gray-200 hover:bg-gray-300']"
      >
        KW {{ kw.number }}
      </button>
    </div>

    <!-- Tag Auswahl -->
    <div class="flex justify-between items-center bg-gray-100 p-2 md:p-6 rounded md:rounded-xl text-lg md:text-xl">
      <button @click="changeDay(-1)" :disabled="isToday" class="p-2">◀</button>
      <span class="font-bold text-lg md:text-2xl">{{ format(currentDate, 'EEEE dd.MM.', { locale: de }) }}</span>
      <button @click="changeDay(1)" :disabled="isEndOfWeek" class="p-2">▶</button>
    </div>

    <!-- Flex-Row bleibt immer aktiv -->
    <div class="flex items-end gap-4 md:gap-8">
      
      <!-- Zeitblöcke Liste -->
      <div class="space-y-2 md:space-y-4 flex-grow">
        <div v-for="(slot, index) in timeSlots" :key="slot.start" class="flex items-center gap-2 md:gap-4">
          <!-- HIER WURDE DIE SCHRIFTGRÖSSE ANGEPASST -->
          <span class="text-xs md:text-lg w-24 md:w-36 font-semibold">{{ slot.start }} - {{ slot.end }} Uhr</span>
          
          <button 
            @click="toggleSlot(slot, index)"
            :disabled="slot.booked || isPastTime(slot)"
            :class="['flex-grow h-10 md:h-14 rounded md:rounded-xl transition-all duration-200', 
              (slot.booked || isPastTime(slot)) ? 'bg-red-800 cursor-not-allowed' : 
              selectedIndices.includes(index) ? 'bg-green-600' : 'bg-gray-300 hover:bg-gray-400']"
          ></button>
        </div>
      </div>

      <!-- Button -->
      <div class="flex-shrink-0">
        <button 
          @click="saveAndContinue" 
          class="text-4xl md:text-6xl hover:scale-110 transition-transform p-2 md:p-4"
        >
          ✅
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { addDays, subDays, getISOWeek, startOfWeek, endOfWeek, addWeeks, format, startOfToday, isSameDay, isBefore, parse } from 'date-fns'
import { de } from 'date-fns/locale'

const router = useRouter()
const route = useRoute()
const today = startOfToday()
const currentDate = ref(today)

const timeSlots = ref([])
const gassiZeitenBackend = ref([])

// Wochentag-Mapping für das Backend (Enum)
const tagesMapping = ['SONNTAG', 'MONTAG', 'DIENSTAG', 'MITTWOCH', 'DONNERSTAG', 'FREITAG', 'SAMSTAG']

// Gassi-Geh-Zeiten vom Backend laden
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
    timeSlots.value = tagesZeiten.map(g => ({
      start: g.von ? g.von.substring(0, 5) : '10:00',
      end: g.bis ? g.bis.substring(0, 5) : '10:30',
      booked: false
    }))
  } else {
    // Fallback falls für diesen Tag keine Zeiten im Backend stehen
    timeSlots.value = [
      { start: '10:00', end: '10:30', booked: false },
      { start: '10:30', end: '11:00', booked: false },
      { start: '11:00', end: '11:30', booked: false },
      { start: '11:30', end: '12:00', booked: false }
    ]
  }
}

onMounted(() => {
  ladeZeitenVomBackend()
})

const weeks = [0, 1, 2].map(i => ({ 
  number: getISOWeek(addWeeks(today, i)),
  startDate: startOfWeek(addWeeks(today, i), { weekStartsOn: 1 }) 
}));
const selectedWeek = ref(weeks[0]);

const isToday = computed(() => isSameDay(currentDate.value, today));
const isEndOfWeek = computed(() => {
  const end = endOfWeek(selectedWeek.value.startDate, { weekStartsOn: 1 });
  return isSameDay(currentDate.value, end);
});

const selectWeek = (kw) => {
  selectedWeek.value = kw;
  currentDate.value = kw.startDate < today ? today : kw.startDate;
  selectedIndices.value = [];
  aktualisiereSlotsFuerAktuellenTag();
};

const changeDay = (dir) => {
  const nextDate = dir > 0 ? addDays(currentDate.value, 1) : subDays(currentDate.value, 1);
  if (nextDate >= today && nextDate >= selectedWeek.value.startDate && nextDate <= endOfWeek(selectedWeek.value.startDate, { weekStartsOn: 1 })) {
    currentDate.value = nextDate;
    selectedIndices.value = [];
    aktualisiereSlotsFuerAktuellenTag();
  }
};

const selectedIndices = ref([]);

const isPastTime = (slot) => {
  if (!isSameDay(currentDate.value, today)) return false;
  const now = new Date();
  const slotTime = parse(slot.start, 'HH:mm', new Date());
  return isBefore(slotTime, now);
};

const toggleSlot = (slot, index) => {
  if (selectedIndices.value.includes(index)) {
    if (index === Math.min(...selectedIndices.value) || index === Math.max(...selectedIndices.value)) {
      selectedIndices.value = selectedIndices.value.filter(i => i !== index);
    }
  } else {
    if (selectedIndices.value.length === 0) {
      selectedIndices.value.push(index);
    } else {
      const min = Math.min(...selectedIndices.value);
      const max = Math.max(...selectedIndices.value);
      if (index === min - 1 || index === max + 1) selectedIndices.value.push(index);
    }
  }
};

const saveAndContinue = () => {
  if (selectedIndices.value.length > 0) {
    const sorted = [...selectedIndices.value].sort((a,b) => a-b);
    
    // Bestehende Daten auslesen (falls bereits etwas da war)
    const existingData = JSON.parse(localStorage.getItem('terminData') || '{}')

    const vonZeit = timeSlots.value[sorted[0]].start + ':00'
    const bisZeit = timeSlots.value[sorted[sorted.length-1]].end + ':00'

    // Daten inklusive TEMPORÄRER MITGLIEDS-ID im localStorage speichern
    localStorage.setItem('terminData', JSON.stringify({ 
      ...existingData,
      mitgliedId: existingData.mitgliedId || 1, // Temporäre Benutzer-ID (z. B. 1)
      datum: format(currentDate.value, 'yyyy-MM-dd'),
      von: vonZeit,   
      bis: bisZeit,   
      start: vonZeit, 
      end: bisZeit    
    }));

    // Weiterleitung basierend auf dem aktuellen Pfad (Admin oder normaler Benutzer)
    const currentPath = route?.path || window.location.pathname
    if (currentPath.startsWith('/app/admin')) {
      router.push('/app/admin/hundewahl')
    } else {
      router.push('/app/reservierung/hundewahl')
    }
  }
};
</script>