<template>
  <div>
    <div class="flex items-start gap-8 flex-wrap mb-6">
      <!-- Bild-Bereich -->
      <div class="flex flex-col items-center gap-2 shrink-0">
        <div
          class="w-36 h-36 rounded-full overflow-hidden border-4 border-gray-800 flex items-center justify-center bg-red-700"
        >
          <img
            v-if="form.bildUrl"
            :src="form.bildUrl"
            alt="Veranstaltungsbild"
            class="w-full h-full object-cover"
          />
          <span v-else class="text-white text-xs text-center px-3">
            {{ form.titel || 'Kein Bild' }}
          </span>
        </div>
        <button
          @click="bildBearbeiten"
          class="px-4 py-1.5 rounded-full bg-[#D3DDD1] border border-gray-200 text-xs text-gray-800 hover:bg-[#c2cebf] transition-colors"
        >
          Bild bearbeiten
        </button>
      </div>

      <!-- Titel / Datum / Uhrzeit -->
      <!-- Jede Zeile hat ihr eigenes Grid (Label 80px | Feld 260px | Icon 28px) statt einem gemeinsamen
           Grid über alle Zeilen. So verschieben Fehlermeldungen darunter nicht die Spalten der nächsten Zeile. -->
      <div class="space-y-4 flex-1 min-w-[240px]">
        <!-- Titel -->
        <div>
          <div class="grid grid-cols-[80px_260px_28px] gap-x-3 items-center">
            <span class="text-sm text-gray-800">Titel:</span>
            <input
              ref="titelInput"
              v-model="form.titel"
              type="text"
              placeholder="Titel eingeben"
              maxlength="40"
              class="border-b text-sm focus:outline-none bg-transparent w-full"
              :class="titelFehler ? 'border-red-500' : 'border-gray-400'"
            />
            <button @click="titelInput?.focus()" aria-label="Titel bearbeiten">
              <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="w-4 h-4 text-gray-800">
                <path stroke-linecap="round" stroke-linejoin="round" d="m16.862 4.487 1.687-1.688a1.875 1.875 0 1 1 2.652 2.652L10.582 16.07a4.5 4.5 0 0 1-1.897 1.13L6 18l.8-2.685a4.5 4.5 0 0 1 1.13-1.897l8.932-8.931Zm0 0L19.5 7.125M18 14v4.75A2.25 2.25 0 0 1 15.75 21H5.25A2.25 2.25 0 0 1 3 18.75V8.25A2.25 2.25 0 0 1 5.25 6H10" />
              </svg>
            </button>
          </div>
          <!-- pl-[92px] = Breite der Label-Spalte (80px) + Abstand (gap-x-3 = 12px), damit der Text unter dem Eingabefeld startet -->
          <p class="text-xs mt-1 pl-[92px]" :class="titelFehler ? 'text-red-600' : 'text-gray-400'">
            {{ titelFehler || `${form.titel.length}/40 Zeichen` }}
          </p>
        </div>

        <!-- Datum -->
        <div>
          <div class="grid grid-cols-[80px_260px_28px] gap-x-3 items-center">
            <span class="text-sm text-gray-800">Datum:</span>
            <!-- Text-Feld statt type="date": so kontrollieren wir jede einzelne Ziffer selbst
                 (Tag max. 2, Monat max. 2, Jahr hart auf 4 Stellen), unabhängig vom Browser -->
            <input
              ref="datumTextInput"
              :value="datumEingabeText"
              @input="datumEingabeVerarbeiten"
              type="text"
              inputmode="numeric"
              placeholder="TT.MM.JJJJ"
              maxlength="10"
              class="border-b text-sm focus:outline-none bg-transparent w-full"
              :class="datumFehler ? 'border-red-500' : 'border-gray-400'"
            />
            <!-- Versteckter nativer Datums-Picker: liefert nur die Kalenderauswahl fürs Icon,
                 wird nie selbst angezeigt oder direkt bearbeitet -->
            <input
              ref="datumPickerInput"
              v-model="form.datum"
              type="date"
              :min="heuteISO"
              :max="maxDatumISO"
              class="sr-only"
              tabindex="-1"
              aria-hidden="true"
            />
            <button @click="datumPickerInput?.showPicker?.() ?? datumPickerInput?.focus()" aria-label="Datum bearbeiten">
              <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="w-4 h-4 text-gray-800">
                <path stroke-linecap="round" stroke-linejoin="round" d="M6.75 3v2.25M17.25 3v2.25M3 18.75V7.5a2.25 2.25 0 0 1 2.25-2.25h13.5A2.25 2.25 0 0 1 21 7.5v11.25m-18 0A2.25 2.25 0 0 0 5.25 21h13.5A2.25 2.25 0 0 0 21 18.75m-18 0v-7.5A2.25 2.25 0 0 1 5.25 9h13.5A2.25 2.25 0 0 1 21 11.25v7.5" />
              </svg>
            </button>
          </div>
          <p v-if="datumFehler" class="text-xs mt-1 pl-[92px] text-red-600">
            {{ datumFehler }}
          </p>
        </div>

        <!-- Uhrzeit -->
        <div class="grid grid-cols-[80px_260px_28px] gap-x-3 items-center">
          <span class="text-sm text-gray-800">Uhrzeit:</span>
          <input
            ref="uhrzeitInput"
            v-model="form.uhrzeit"
            type="time"
            class="border-b border-gray-400 text-sm focus:outline-none bg-transparent w-full"
          />
          <button @click="uhrzeitInput?.showPicker?.() ?? uhrzeitInput?.focus()" aria-label="Uhrzeit bearbeiten">
            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="w-4 h-4 text-gray-800">
              <path stroke-linecap="round" stroke-linejoin="round" d="M12 6v6h4.5m4.5 0a9 9 0 1 1-18 0 9 9 0 0 1 18 0Z" />
            </svg>
          </button>
        </div>
      </div>
    </div>

    <hr class="border-gray-300 mb-6" />

    <div class="flex flex-col md:flex-row gap-10">
      <!-- Abstimmungen-Toggle -->
      <div class="flex items-center gap-4 shrink-0">
        <span class="text-sm font-semibold text-gray-800">Abstimmungen</span>
        <button
          @click="form.abstimmungen = !form.abstimmungen"
          :class="[
            'w-11 h-6 rounded-full relative transition-colors',
            form.abstimmungen ? 'bg-emerald-600' : 'bg-gray-300',
          ]"
          aria-label="Abstimmungen umschalten"
        >
          <span
            :class="[
              'absolute top-0.5 w-5 h-5 rounded-full bg-white shadow transition-transform',
              form.abstimmungen ? 'translate-x-5' : 'translate-x-0.5',
            ]"
          ></span>
        </button>
      </div>

      <!-- Aktuelle Teilnehmer: zusätzliches ml-8 rückt den Block etwas nach rechts -->
      <div class="flex-1 md:ml-8">
        <h3 class="text-sm font-semibold text-gray-800 mb-3">Aktuelle Teilnehmer</h3>
        <div class="space-y-2">
          <div
            v-for="teilnehmer in form.teilnehmer"
            :key="teilnehmer.id"
            class="flex items-center gap-3 p-3 rounded-full bg-[#D3DDD1] border border-gray-200"
          >
            <span
              :class="[
                'w-3 h-3 rounded-full shrink-0',
                teilnehmer.erfahrung === 'gruen' ? 'bg-green-500' : 'bg-orange-500',
              ]"
            ></span>
            <span class="text-sm text-gray-800">{{ teilnehmer.nummer }}</span>
          </div>

          <p v-if="form.teilnehmer.length === 0" class="text-sm text-gray-500 italic">
            Noch keine Teilnehmer.
          </p>
        </div>
      </div>
    </div>

    <!-- Anlegen/Speichern-Button: zentriert -->
    <div class="flex justify-center mt-8">
      <button
        @click="speichern"
        :disabled="!istGueltig"
        :class="[
          'px-6 py-2 rounded-full border shadow-sm text-sm transition-colors',
          istGueltig
            ? 'bg-[#D3DDD1] border-gray-200 text-gray-800 hover:bg-[#c2cebf]'
            : 'bg-gray-200 border-gray-200 text-gray-400 cursor-not-allowed',
        ]"
      >
        {{ istNeu ? 'Anlegen' : 'Speichern' }}
      </button>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'

const route = useRoute()
const router = useRouter()

// Erkennt anhand der Route, ob eine neue Veranstaltung angelegt oder eine bestehende bearbeitet wird
const istNeu = computed(() => !route.params.id || route.params.id === 'neu')

// Beispielhafte Daten; in echt via fetch/API anhand route.params.id aus dem Backend laden
const beispielDaten = {
  1: {
    titel: 'Tag der offenen Tür',
    datum: '2026-05-30',
    uhrzeit: '14:00',
    bildUrl: '',
    abstimmungen: true,
    teilnehmer: [
      { id: 1, nummer: '1233567893', erfahrung: 'orange' },
      { id: 2, nummer: '5718239471', erfahrung: 'gruen' },
      { id: 3, nummer: '6734846293', erfahrung: 'gruen' },
    ],
  },
}

const form = ref(
  istNeu.value
    ? { titel: '', datum: '', uhrzeit: '', bildUrl: '', abstimmungen: false, teilnehmer: [] }
    : { ...beispielDaten[route.params.id] }
)

// Refs auf die Eingabefelder, damit die Icon-Buttons daneben sie fokussieren
// bzw. den nativen Picker öffnen können
const titelInput = ref(null)
const datumTextInput = ref(null)
const datumPickerInput = ref(null)
const uhrzeitInput = ref(null)

const TITEL_MAX_LAENGE = 40

// Heutiges Datum als YYYY-MM-DD (lokale Zeitzone, nicht UTC) — dient als
// Minimum fürs Datumsfeld (native Sperre) und für die Vergangenheits-Prüfung unten
const heute = new Date()
const heuteISO = `${heute.getFullYear()}-${String(heute.getMonth() + 1).padStart(2, '0')}-${String(heute.getDate()).padStart(2, '0')}`

// Spätestes wählbares Datum: genau ein Jahr ab heute — dient als Maximum fürs
// Datumsfeld (native Sperre) und für die Zukunfts-Prüfung unten
const maxDatum = new Date(heute.getFullYear() + 1, heute.getMonth(), heute.getDate())
const maxDatumISO = `${maxDatum.getFullYear()}-${String(maxDatum.getMonth() + 1).padStart(2, '0')}-${String(maxDatum.getDate()).padStart(2, '0')}`

// Titel-Validierung: darf nicht leer und nicht länger als 40 Zeichen sein
// (maxlength="40" im Input verhindert das Tippen darüber hinaus bereits,
// diese Prüfung greift zusätzlich z.B. bei bereits vorhandenen/eingefügten Werten)
const titelFehler = computed(() => {
  if (!form.value.titel.trim()) {
    return 'Bitte einen Titel eingeben.'
  }
  if (form.value.titel.length > TITEL_MAX_LAENGE) {
    return `Titel darf maximal ${TITEL_MAX_LAENGE} Zeichen lang sein.`
  }
  return ''
})

// Datum-Validierung: prüft der Reihe nach 4-stelliges Jahr, echtes Kalenderdatum
// (fängt z.B. einen ungültigen 31.04. ab), nicht in der Vergangenheit und
// höchstens ein Jahr in der Zukunft
const datumFehler = computed(() => {
  if (!form.value.datum) {
    return 'Bitte ein Datum eingeben.'
  }
  // \d+ statt \d{4} beim Jahr, damit eine falsche Länge unten mit einer eigenen,
  // klaren Fehlermeldung erkannt wird statt nur generisch "ungültig" zu melden
  const teile = form.value.datum.match(/^(\d+)-(\d{2})-(\d{2})$/)
  if (!teile) {
    return 'Bitte ein gültiges Datum eingeben.'
  }
  const [, jahrText, monatText, tagText] = teile
  if (jahrText.length !== 4) {
    return 'Das Jahr muss 4-stellig sein.'
  }
  const jahr = Number(jahrText)
  const monat = Number(monatText)
  const tag = Number(tagText)
  const datum = new Date(form.value.datum)
  const istEchtesDatum =
    !isNaN(datum.getTime()) &&
    datum.getUTCFullYear() === jahr &&
    datum.getUTCMonth() + 1 === monat &&
    datum.getUTCDate() === tag
  if (!istEchtesDatum) {
    return 'Bitte ein gültiges Datum eingeben.'
  }
  if (form.value.datum < heuteISO) {
    return 'Das Datum darf nicht in der Vergangenheit liegen.'
  }
  if (form.value.datum > maxDatumISO) {
    return 'Das Datum darf höchstens ein Jahr in der Zukunft liegen.'
  }
  return ''
})

// Speichern ist nur möglich, wenn beide Felder gültig sind
const istGueltig = computed(() => !titelFehler.value && !datumFehler.value)

// Anzeigetext des Datumsfelds (TT.MM.JJJJ), getrennt vom internen ISO-Wert in form.datum
const datumEingabeText = ref('')

// Wandelt ISO (YYYY-MM-DD) in die Anzeige TT.MM.JJJJ um
const isoZuAnzeige = (iso) => {
  if (!iso) return ''
  const [jahr, monat, tag] = iso.split('-')
  if (!jahr || !monat || !tag) return ''
  return `${tag}.${monat}.${jahr}`
}

// Hält die Textanzeige synchron, wenn form.datum sich von außen ändert
// (z.B. weil über das Kalender-Icon der versteckte native Picker benutzt wurde)
watch(
  () => form.value.datum,
  (neuesDatum) => {
    datumEingabeText.value = isoZuAnzeige(neuesDatum)
  },
  { immediate: true }
)

// Verarbeitet jede Ziffer selbst: nur Zahlen erlaubt, Punkte automatisch gesetzt,
// Tag/Monat je max. 2 Ziffern, Jahr hart auf 4 Ziffern begrenzt (max. 8 Ziffern insgesamt).
// Dadurch ist es unabhängig vom Browser unmöglich, mehr als 4 Ziffern beim Jahr einzugeben.
const datumEingabeVerarbeiten = (event) => {
  const ziffern = event.target.value.replace(/\D/g, '').slice(0, 8)

  let formatiert = ziffern
  if (ziffern.length > 4) {
    formatiert = `${ziffern.slice(0, 2)}.${ziffern.slice(2, 4)}.${ziffern.slice(4)}`
  } else if (ziffern.length > 2) {
    formatiert = `${ziffern.slice(0, 2)}.${ziffern.slice(2)}`
  }

  datumEingabeText.value = formatiert
  event.target.value = formatiert

  // Erst wenn Tag, Monat und Jahr vollständig (8 Ziffern) eingegeben sind, ins ISO-Format
  // übernehmen — davor bleibt form.datum leer, wodurch die bestehende Validierung
  // "Bitte ein Datum eingeben." zeigt
  if (ziffern.length === 8) {
    const tag = ziffern.slice(0, 2)
    const monat = ziffern.slice(2, 4)
    const jahr = ziffern.slice(4, 8)
    form.value.datum = `${jahr}-${monat}-${tag}`
  } else {
    form.value.datum = ''
  }
}

const bildBearbeiten = () => {
  // TODO: Bild-Upload/Auswahl an das Backend anbinden
}

const speichern = () => {
  if (!istGueltig.value) return

  // TODO: bei istNeu ein POST, sonst ein PUT an das Backend senden
  router.push('/veranstaltungen')
}
</script>