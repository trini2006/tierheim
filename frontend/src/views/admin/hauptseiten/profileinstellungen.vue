<template>
  <div>
    <!-- Kopfbereich: Logo, Vereinsname, Leitfarbe -->
    <div class="flex items-start gap-8 flex-wrap mb-10">
      <!-- Logo -->
      <div class="flex flex-col items-center gap-2 shrink-0">
        <div
          class="w-32 h-32 rounded-full overflow-hidden border border-gray-300 flex items-center justify-center bg-white"
        >
          <img
            v-if="form.bildUrl"
            :src="form.bildUrl"
            alt="Vereinslogo"
            class="w-full h-full object-cover"
          />
          <span v-else class="text-gray-400 text-xs text-center px-3">Kein Bild</span>
        </div>
        <button
          @click="bildBearbeiten"
          class="px-4 py-1.5 rounded-full bg-[#D3DDD1] border border-gray-200 text-xs text-gray-800 hover:bg-[#c2cebf] transition-colors"
        >
          Bild bearbeiten
        </button>
      </div>

      <!-- Name + Leitfarbe -->
      <div class="pt-1">
        <div class="flex items-center gap-2 mb-2">
          <input
            v-if="editiertName"
            ref="nameInput"
            v-model="form.vereinsname"
            @blur="editiertName = false"
            @keyup.enter="editiertName = false"
            type="text"
            class="text-2xl font-bold text-gray-800 border-b border-gray-400 bg-transparent focus:outline-none"
          />
          <h2 v-else class="text-2xl font-bold text-gray-800">{{ form.vereinsname }}</h2>
          <button @click="nameBearbeiten" aria-label="Vereinsname bearbeiten">
            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="w-5 h-5 text-gray-800">
              <path stroke-linecap="round" stroke-linejoin="round" d="m16.862 4.487 1.687-1.688a1.875 1.875 0 1 1 2.652 2.652L10.582 16.07a4.5 4.5 0 0 1-1.897 1.13L6 18l.8-2.685a4.5 4.5 0 0 1 1.13-1.897l8.932-8.931Zm0 0L19.5 7.125M18 14v4.75A2.25 2.25 0 0 1 15.75 21H5.25A2.25 2.25 0 0 1 3 18.75V8.25A2.25 2.25 0 0 1 5.25 6H10" />
            </svg>
          </button>
        </div>

        <div class="flex items-center gap-2">
          <span class="text-sm text-gray-800">Leitfarbe</span>
          <button
            @click="leitfarbeInput?.click()"
            :style="{ backgroundColor: form.leitfarbe }"
            class="w-5 h-5 rounded-full border border-gray-300"
            aria-label="Leitfarbe ändern"
          ></button>
          <!-- Versteckter nativer Farbwähler, wird nur über den Kreis oben ausgelöst -->
          <input
            ref="leitfarbeInput"
            v-model="form.leitfarbe"
            type="color"
            class="sr-only"
            tabindex="-1"
            aria-hidden="true"
          />
        </div>
      </div>
    </div>

    <!-- Zwei-Spalten-Bereich -->
    <div class="grid grid-cols-1 md:grid-cols-2 gap-x-16 gap-y-10">
      <!-- LINKE SPALTE -->
      <div class="space-y-10">
        <!-- Kontaktdaten -->
        <div>
          <div class="flex items-center gap-2 mb-3">
            <h3 class="text-base font-bold text-gray-800">Kontaktdaten</h3>
            <button @click="editiertKontakt = !editiertKontakt" aria-label="Kontaktdaten bearbeiten">
              <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="w-4 h-4 text-gray-800">
                <path stroke-linecap="round" stroke-linejoin="round" d="m16.862 4.487 1.687-1.688a1.875 1.875 0 1 1 2.652 2.652L10.582 16.07a4.5 4.5 0 0 1-1.897 1.13L6 18l.8-2.685a4.5 4.5 0 0 1 1.13-1.897l8.932-8.931Zm0 0L19.5 7.125M18 14v4.75A2.25 2.25 0 0 1 15.75 21H5.25A2.25 2.25 0 0 1 3 18.75V8.25A2.25 2.25 0 0 1 5.25 6H10" />
              </svg>
            </button>
          </div>

          <div v-if="!editiertKontakt" class="space-y-1 text-sm text-gray-800">
            <p>{{ form.kontakt.strasse }}</p>
            <p>{{ form.kontakt.plzOrt }}</p>
            <p>{{ form.kontakt.telefon }}</p>
            <p>{{ form.kontakt.notfall }} (Notfall)</p>
            <p>{{ form.kontakt.webseite }}</p>
          </div>
          <div v-else class="space-y-2">
            <input v-model="form.kontakt.strasse" type="text" placeholder="Straße, Hausnummer" class="w-full text-sm border-b border-gray-400 bg-transparent focus:outline-none" />
            <input v-model="form.kontakt.plzOrt" type="text" placeholder="PLZ Ort" class="w-full text-sm border-b border-gray-400 bg-transparent focus:outline-none" />
            <input v-model="form.kontakt.telefon" type="tel" placeholder="Telefon" class="w-full text-sm border-b border-gray-400 bg-transparent focus:outline-none" />
            <input v-model="form.kontakt.notfall" type="tel" placeholder="Notfall-Telefon" class="w-full text-sm border-b border-gray-400 bg-transparent focus:outline-none" />
            <input v-model="form.kontakt.webseite" type="text" placeholder="Webseite" class="w-full text-sm border-b border-gray-400 bg-transparent focus:outline-none" />
          </div>
        </div>

        <!-- Gassi Zeiten -->
        <div>
          <div class="flex items-center gap-2 mb-3">
            <h3 class="text-base font-bold text-gray-800">Gassi Zeiten</h3>
            <button @click="editiertGassiZeiten = !editiertGassiZeiten" aria-label="Gassi Zeiten bearbeiten">
              <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="w-4 h-4 text-gray-800">
                <path stroke-linecap="round" stroke-linejoin="round" d="m16.862 4.487 1.687-1.688a1.875 1.875 0 1 1 2.652 2.652L10.582 16.07a4.5 4.5 0 0 1-1.897 1.13L6 18l.8-2.685a4.5 4.5 0 0 1 1.13-1.897l8.932-8.931Zm0 0L19.5 7.125M18 14v4.75A2.25 2.25 0 0 1 15.75 21H5.25A2.25 2.25 0 0 1 3 18.75V8.25A2.25 2.25 0 0 1 5.25 6H10" />
              </svg>
            </button>
          </div>

          <div v-if="!editiertGassiZeiten" class="space-y-1 text-sm text-gray-800">
            <p v-for="zeit in form.gassiZeiten" :key="zeit.tag">
              <span class="inline-block w-8 font-semibold">{{ zeit.tag }}</span>
              {{ zeit.von }} - {{ zeit.bis }} Uhr
            </p>
          </div>
          <div v-else class="space-y-2">
            <div v-for="zeit in form.gassiZeiten" :key="zeit.tag" class="flex items-center gap-2">
              <span class="inline-block w-8 text-sm font-semibold text-gray-800">{{ zeit.tag }}</span>
              <input v-model="zeit.von" type="time" class="text-sm border-b border-gray-400 bg-transparent focus:outline-none" />
              <span class="text-sm text-gray-500">-</span>
              <input v-model="zeit.bis" type="time" class="text-sm border-b border-gray-400 bg-transparent focus:outline-none" />
            </div>
          </div>
        </div>
      </div>

      <!-- RECHTE SPALTE -->
      <div>
        <div class="flex items-center gap-2 mb-3">
          <h3 class="text-base font-bold text-gray-800">Kalenderdaten</h3>
          <button @click="editiertKalender = !editiertKalender" aria-label="Kalenderdaten bearbeiten">
            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="w-4 h-4 text-gray-800">
              <path stroke-linecap="round" stroke-linejoin="round" d="m16.862 4.487 1.687-1.688a1.875 1.875 0 1 1 2.652 2.652L10.582 16.07a4.5 4.5 0 0 1-1.897 1.13L6 18l.8-2.685a4.5 4.5 0 0 1 1.13-1.897l8.932-8.931Zm0 0L19.5 7.125M18 14v4.75A2.25 2.25 0 0 1 15.75 21H5.25A2.25 2.25 0 0 1 3 18.75V8.25A2.25 2.25 0 0 1 5.25 6H10" />
            </svg>
          </button>
        </div>

        <div class="space-y-3 text-sm text-gray-800">
          <!-- Zeitblock: immer per Klick wählbar, unabhängig vom Bearbeiten-Modus -->
          <div class="flex items-center gap-3">
            <span>Zeitblock:</span>
            <button
              v-for="option in [30, 45, 60]"
              :key="option"
              @click="form.kalender.zeitblock = option"
              :class="[
                'px-3 py-1 rounded-full text-sm transition-colors',
                form.kalender.zeitblock === option
                  ? 'bg-[#b8c9b4] border border-emerald-700'
                  : 'bg-[#D3DDD1] border border-gray-200 hover:bg-[#c2cebf]',
              ]"
            >
              {{ option }}
            </button>
          </div>

          <div v-if="!editiertKalender" class="space-y-2">
            <p>Standard Ruhezeiten (Hund): {{ form.kalender.standardRuhezeitHund }}</p>
            <p>max. Gassi: {{ form.kalender.maxGassi }}</p>
            <p>min. Gassi: {{ form.kalender.minGassi }}</p>
          </div>
          <div v-else class="space-y-2">
            <div class="flex items-center gap-2">
              <span class="w-48 shrink-0">Standard Ruhezeiten (Hund):</span>
              <input v-model.number="form.kalender.standardRuhezeitHund" type="number" min="0" class="w-20 border-b border-gray-400 bg-transparent focus:outline-none" />
            </div>
            <div class="flex items-center gap-2">
              <span class="w-48 shrink-0">max. Gassi:</span>
              <input v-model.number="form.kalender.maxGassi" type="number" min="0" class="w-20 border-b border-gray-400 bg-transparent focus:outline-none" />
            </div>
            <div class="flex items-center gap-2">
              <span class="w-48 shrink-0">min. Gassi:</span>
              <input v-model.number="form.kalender.minGassi" type="number" min="0" class="w-20 border-b border-gray-400 bg-transparent focus:outline-none" />
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Speichern-Button: zentriert -->
    <div class="flex justify-center mt-10">
      <button
        @click="speichern"
        class="px-6 py-2 rounded-full bg-[#D3DDD1] border border-gray-200 shadow-sm text-sm text-gray-800 hover:bg-[#c2cebf] transition-colors"
      >
        Speichern
      </button>
    </div>
  </div>
</template>

<script setup>
import { ref, nextTick } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

// Beispielhafte Daten; in echt via fetch/API aus dem Backend laden
const form = ref({
  vereinsname: 'Tierheim Weiden',
  leitfarbe: '#9c2b2b',
  bildUrl: '',
  kontakt: {
    strasse: 'Schustermooslohe 96',
    plzOrt: '92637 Weiden i. d. Oberpfalz',
    telefon: '0961 25780',
    notfall: '0176 47159671',
    webseite: 'tierheim-weiden.de',
  },
  kalender: {
    zeitblock: 30,
    standardRuhezeitHund: 60,
    maxGassi: 120,
    minGassi: 30,
  },
  gassiZeiten: [
    { tag: 'Mo', von: '10:00', bis: '14:00' },
    { tag: 'Mi', von: '10:00', bis: '14:00' },
    { tag: 'Fr', von: '10:00', bis: '14:00' },
    { tag: 'Sa', von: '10:00', bis: '11:30' },
  ],
})

const nameInput = ref(null)
const leitfarbeInput = ref(null)

const editiertName = ref(false)
const editiertKontakt = ref(false)
const editiertKalender = ref(false)
const editiertGassiZeiten = ref(false)

const nameBearbeiten = async () => {
  editiertName.value = true
  await nextTick()
  nameInput.value?.focus()
}

const bildBearbeiten = () => {
  // TODO: Bild-Upload/Auswahl an das Backend anbinden
}

const speichern = () => {
  // TODO: an das Backend senden (PUT/PATCH fürs Profil)
  editiertKontakt.value = false
  editiertKalender.value = false
  editiertGassiZeiten.value = false
  router.push('/start')
}
</script>