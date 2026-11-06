<template>
  <div
    :class="{ 'opacity-50 pointer-events-none': deaktiviert }"
    class="transition-opacity space-y-4"
  >
    <!-- KW Auswahl zentriert über dem Datums-Block -->
    <div class="flex justify-center gap-2">
      <button
        v-for="kw in weeks"
        :key="kw.number"
        @click="selectWeek(kw)"
        class="px-4 py-2 rounded-xl text-xs font-bold transition-colors shadow-sm"
        :class="
          selectedWeek.number === kw.number
            ? 'bg-[#4A6B5D] text-white'
            : 'bg-gray-200 text-gray-700 hover:bg-gray-300'
        "
      >
        KW {{ kw.number }}
      </button>
    </div>

    <!-- Datum-Navigationsleiste (wie in Bild 1) -->

    <div
      class="flex items-center justify-between bg-gray-50 p-4 rounded-2xl border border-gray-100 shadow-sm"
    >
      <button
        @click="changeDay(-1)"
        :disabled="isToday"
        class="p-2 rounded-xl bg-white shadow-sm font-bold text-blue-600 hover:bg-gray-100 disabled:opacity-30"
      >
        ◀
      </button>
      <span class="text-base font-bold text-gray-900">
        {{ format(currentDate, 'EEEE dd.MM.', { locale: de }) }}
      </span>
      <button
        @click="changeDay(1)"
        :disabled="isEndOfWeek"
        class="p-2 rounded-xl bg-white shadow-sm font-bold text-blue-600 hover:bg-gray-100 disabled:opacity-30"
      >
        ▶
      </button>
    </div>

    <!-- Zeitblöcke (Mehrfachauswahl per Array) -->
    <div class="space-y-3 pt-2">
      <!-- Slot-Liste: Abstand zwischen den Fenstern, damit man die Pause sieht -->
      <div
        v-for="(zeit, i) in zeitBloecke"
        :key="zeit.id"
        class="flex items-center justify-between"
        :class="{ 'mt-6': i > 0 && zeit.fenster !== zeitBloecke[i - 1].fenster }"
      >
        <span class="text-xs font-semibold text-gray-700 w-28">{{ zeit.label }}</span>
        <div
          @click="toggleZeit(zeit.id)"
          class="flex-1 ml-4 h-12 rounded-2xl cursor-pointer transition-colors flex items-center px-4 text-xs font-bold shadow-sm"
          :class="[
            zeit.booked
              ? 'bg-red-800 cursor-not-allowed text-white'
              : ausgewaehlteZeiten.includes(zeit.id)
                ? 'bg-emerald-700 text-white'
                : 'bg-gray-200 hover:bg-gray-300 text-gray-700',
          ]"
        >
          {{ zeit.booked ? 'Belegt' : ausgewaehlteZeiten.includes(zeit.id) ? 'Ausgewählt' : '' }}
        </div>
      </div>
    </div>

    <p v-if="zeitBloecke.length === 0" class="text-sm text-gray-500 italic">
      An diesem Tag sind keine Reservierungen möglich.
    </p>
    <!-- HIER: Hinweise zur Auswahl -->
    <p v-if="hinweis" class="text-xs text-red-700">{{ hinweis }}</p>
    <p
      v-else-if="ausgewaehlteZeiten.length > 0 && ausgewaehlteZeiten.length < minSlots"
      class="text-xs text-gray-600"
    >
      Mindestens {{ minGassi }} Minuten
    </p>
  </div>
</template>

<script setup>
import { ref, computed, watch, onMounted } from 'vue'
import {
  addDays,
  subDays,
  getISOWeek,
  startOfWeek,
  endOfWeek,
  addWeeks,
  format,
  startOfToday,
  isSameDay,
} from 'date-fns'
import { de } from 'date-fns/locale'
import { erzeugeSlots, SLOT_MINUTEN } from '@/components/utils/slots.js'

const props = defineProps({
  deaktiviert: { type: Boolean, default: false }, // Admin: solange kein Mitglied gewählt ist
  heuteSperren: { type: Boolean, default: false }, // Benutzer: nur bis zum Vortag buchbar
})
const emit = defineEmits(['change']) // liefert die Auswahl oder null

const VORLAUF_MINUTEN = 180 // Admin: heute frühestens 3 h vor Beginn
const tierheimId = 1
const tagesMapping = ['SO', 'MO', 'DI', 'MI', 'DO', 'FR', 'SA']

const today = startOfToday()
const currentDate = ref(today)
const ausgewaehlteZeiten = ref([])
const zeitBloecke = ref([])
const gassiZeitenBackend = ref([])
const hinweis = ref('')
const maxGassi = ref(null)
const minGassi = ref(null)

// ---------- Wochen / Tage ----------
const weeks = [0, 1, 2].map((i) => ({
  number: getISOWeek(addWeeks(today, i)),
  startDate: startOfWeek(addWeeks(today, i), { weekStartsOn: 1 }),
}))
const selectedWeek = ref(weeks[0])
const isToday = computed(() => isSameDay(currentDate.value, today))
const isEndOfWeek = computed(() =>
  isSameDay(currentDate.value, endOfWeek(selectedWeek.value.startDate, { weekStartsOn: 1 })),
)

const tagGewechselt = () => {
  hinweis.value = ''
  ausgewaehlteZeiten.value = []
  aktualisiereSlotsFuerAktuellenTag()
}
const selectWeek = (kw) => {
  selectedWeek.value = kw
  currentDate.value = kw.startDate < today ? today : kw.startDate
  tagGewechselt()
}
const changeDay = (dir) => {
  const next = dir > 0 ? addDays(currentDate.value, 1) : subDays(currentDate.value, 1)
  if (
    next >= today &&
    next >= selectedWeek.value.startDate &&
    next <= endOfWeek(selectedWeek.value.startDate, { weekStartsOn: 1 })
  ) {
    currentDate.value = next
    tagGewechselt()
  }
}

// ---------- Laden ----------
const ladeZeitenVomBackend = async () => {
  try {
    const res = await fetch('/api/gassiGehZeiten/all')
    if (res.ok) {
      gassiZeitenBackend.value = await res.json()
      aktualisiereSlotsFuerAktuellenTag()
    }
  } catch (e) {
    console.error('Fehler beim Laden der Gassi-Geh-Zeiten:', e)
  }
}
const ladeTierheim = async () => {
  try {
    const res = await fetch(`/api/tierheim/${tierheimId}`)
    if (res.ok) {
      const t = await res.json()
      maxGassi.value = t.maxGassi
      minGassi.value = t.minGassi
    }
  } catch (e) {
    console.error('Fehler beim Laden des Tierheims:', e)
  }
}
onMounted(() => {
  ladeZeitenVomBackend()
  ladeTierheim()
})

// ---------- Slots ----------
const aktualisiereSlotsFuerAktuellenTag = () => {
  zeitBloecke.value = []
  const tagesZeiten = gassiZeitenBackend.value.filter(
    (g) => g.tag === tagesMapping[currentDate.value.getDay()],
  )
  if (tagesZeiten.length === 0) return

  if (isSameDay(currentDate.value, today)) {
    if (props.heuteSperren) return // Benutzer: heute nicht mehr buchbar
    const frueh = Math.min(
      ...tagesZeiten.map((g) => {
        const [h, m] = g.von.split(':').map(Number)
        return h * 60 + m
      }),
    )
    const jetzt = new Date()
    if (jetzt.getHours() * 60 + jetzt.getMinutes() >= frueh - VORLAUF_MINUTEN) return
  }

  zeitBloecke.value = erzeugeSlots(tagesZeiten).map((s) => ({
    ...s,
    label: `${s.label} Uhr`,
    vonFull: `${s.von}:00`,
    bisFull: `${s.bis}:00`,
    booked: false,
  }))
}

// ---------- Auswahl ----------
const maxSlots = computed(() => (maxGassi.value ? Math.floor(maxGassi.value / SLOT_MINUTEN) : 0))
const minSlots = computed(() => (minGassi.value ? Math.ceil(minGassi.value / SLOT_MINUTEN) : 1))
const auswahlGueltig = computed(
  () =>
    ausgewaehlteZeiten.value.length >= minSlots.value &&
    ausgewaehlteZeiten.value.length <= maxSlots.value,
)

function toggleZeit(zeitId) {
  if (props.deaktiviert) return
  if (maxSlots.value === 0) {
    hinweis.value = 'Tierheim-Einstellungen konnten nicht geladen werden'
    return
  }
  const slot = zeitBloecke.value.find((z) => z.id === zeitId)
  if (!slot || slot.booked) return
  hinweis.value = ''

  const sel = ausgewaehlteZeiten.value
  if (sel.length === 0) {
    ausgewaehlteZeiten.value = [zeitId]
    return
  }

  const ersterId = Math.min(...sel)
  const letzterId = Math.max(...sel)

  if (sel.includes(zeitId)) {
    ausgewaehlteZeiten.value =
      zeitId === ersterId || zeitId === letzterId ? sel.filter((id) => id !== zeitId) : [zeitId]
    return
  }
  const erster = zeitBloecke.value.find((z) => z.id === ersterId)
  const grenztAn = zeitId === ersterId - 1 || zeitId === letzterId + 1
  if (slot.fenster === erster.fenster && grenztAn) {
    if (sel.length >= maxSlots.value) {
      hinweis.value = `Maximal ${maxGassi.value} Minuten pro Spaziergang`
      return
    }
    ausgewaehlteZeiten.value = [...sel, zeitId]
  } else {
    ausgewaehlteZeiten.value = [zeitId]
  }
}

// ---------- Ergebnis an die Elternseite ----------
const auswahl = computed(() => {
  if (!auswahlGueltig.value) return null
  const gewaehlt = zeitBloecke.value
    .filter((z) => ausgewaehlteZeiten.value.includes(z.id))
    .sort((a, b) => a.id - b.id)
  const von = gewaehlt[0].vonFull
  const bis = gewaehlt[gewaehlt.length - 1].bisFull
  return {
    datum: format(currentDate.value, 'yyyy-MM-dd'),
    von,
    bis,
    start: von,
    end: bis,
    zeiten: [...ausgewaehlteZeiten.value],
  }
})
watch(auswahl, (a) => emit('change', a), { immediate: true })
</script>
