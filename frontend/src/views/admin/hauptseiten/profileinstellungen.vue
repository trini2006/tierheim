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
            v-if="tierheim.bild"
            :src="tierheim.bild"
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
            v-model="tierheim.name"
            @blur="editiertName = false"
            @keyup.enter="editiertName = false"
            type="text"
            class="text-2xl font-bold text-gray-800 border-b border-gray-400 bg-transparent focus:outline-none"
          />
          <h2 v-else class="text-2xl font-bold text-gray-800">{{ tierheim.name }}</h2>
          <button @click="nameBearbeiten" aria-label="Vereinsname bearbeiten">
            <svg
              xmlns="http://www.w3.org/2000/svg"
              fill="none"
              viewBox="0 0 24 24"
              stroke-width="1.5"
              stroke="currentColor"
              class="w-5 h-5 text-gray-800"
            >
              <path
                stroke-linecap="round"
                stroke-linejoin="round"
                d="m16.862 4.487 1.687-1.688a1.875 1.875 0 1 1 2.652 2.652L10.582 16.07a4.5 4.5 0 0 1-1.897 1.13L6 18l.8-2.685a4.5 4.5 0 0 1 1.13-1.897l8.932-8.931Zm0 0L19.5 7.125M18 14v4.75A2.25 2.25 0 0 1 15.75 21H5.25A2.25 2.25 0 0 1 3 18.75V8.25A2.25 2.25 0 0 1 5.25 6H10"
              />
            </svg>
          </button>
        </div>

        <div class="flex items-center gap-2">
          <span class="text-sm text-gray-800">Leitfarbe</span>
          <button
            @click="leitfarbeInput?.click()"
            :style="{ backgroundColor: tierheim?.farbe || '#D3DDD1' }"
            class="w-5 h-5 rounded-full border border-gray-300"
            aria-label="Leitfarbe ändern"
          ></button>
          <!-- Versteckter nativer Farbwähler, wird nur über den Kreis oben ausgelöst -->
          <input
            ref="leitfarbeInput"
            v-model="tierheim.farbe"
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
            <button
              @click="editiertKontakt = !editiertKontakt"
              aria-label="Kontaktdaten bearbeiten"
            >
              <svg
                xmlns="http://www.w3.org/2000/svg"
                fill="none"
                viewBox="0 0 24 24"
                stroke-width="1.5"
                stroke="currentColor"
                class="w-4 h-4 text-gray-800"
              >
                <path
                  stroke-linecap="round"
                  stroke-linejoin="round"
                  d="m16.862 4.487 1.687-1.688a1.875 1.875 0 1 1 2.652 2.652L10.582 16.07a4.5 4.5 0 0 1-1.897 1.13L6 18l.8-2.685a4.5 4.5 0 0 1 1.13-1.897l8.932-8.931Zm0 0L19.5 7.125M18 14v4.75A2.25 2.25 0 0 1 15.75 21H5.25A2.25 2.25 0 0 1 3 18.75V8.25A2.25 2.25 0 0 1 5.25 6H10"
                />
              </svg>
            </button>
          </div>

          <div v-if="!editiertKontakt" class="space-y-1 text-sm text-gray-800">
            <p>{{ tierheim.strasse }} {{ tierheim.hausnummer }}</p>
            <p>{{ tierheim.plz }} {{ tierheim.ort }}</p>
            <p>{{ tierheim.telefonnummer }}</p>
            <p v-if="tierheim.notfallnummer">{{ tierheim.notfallnummer }} (Notfall)</p>
            <p class="pt-2">
              <a
                :href="tierheim.link"
                target="_blank"
                class="hover:underline text-emerald-800 font-semibold"
              >
                {{ tierheim.showLink }}
              </a>
            </p>
          </div>
          <div v-else class="space-y-2">
            <input
              v-model="tierheim.strasse"
              type="text"
              placeholder="Straße, Hausnummer"
              class="w-full text-sm border-b border-gray-400 bg-transparent focus:outline-none"
            />
            <input
              v-model="tierheim.plz"
              type="text"
              placeholder="PLZ Ort"
              class="w-full text-sm border-b border-gray-400 bg-transparent focus:outline-none"
            />
            <input
              v-model="tierheim.telefonnummer"
              type="tel"
              placeholder="Telefon"
              class="w-full text-sm border-b border-gray-400 bg-transparent focus:outline-none"
            />
            <input
              v-model="tierheim.notNummer"
              type="tel"
              placeholder="Notfall-Telefon"
              class="w-full text-sm border-b border-gray-400 bg-transparent focus:outline-none"
            />
            <input
              v-model="tierheim.link"
              type="text"
              placeholder="Webseite"
              class="w-full text-sm border-b border-gray-400 bg-transparent focus:outline-none"
            />
          </div>
        </div>

        <!-- Gassi Zeiten -->
        <div>
          <div class="flex items-center gap-2 mb-3">
            <h3 class="text-base font-bold text-gray-800">Gassi Zeiten</h3>
            <button
              @click="editiertGassiZeiten = !editiertGassiZeiten"
              aria-label="Gassi Zeiten bearbeiten"
            >
              <svg
                xmlns="http://www.w3.org/2000/svg"
                fill="none"
                viewBox="0 0 24 24"
                stroke-width="1.5"
                stroke="currentColor"
                class="w-4 h-4 text-gray-800"
              >
                <path
                  stroke-linecap="round"
                  stroke-linejoin="round"
                  d="m16.862 4.487 1.687-1.688a1.875 1.875 0 1 1 2.652 2.652L10.582 16.07a4.5 4.5 0 0 1-1.897 1.13L6 18l.8-2.685a4.5 4.5 0 0 1 1.13-1.897l8.932-8.931Zm0 0L19.5 7.125M18 14v4.75A2.25 2.25 0 0 1 15.75 21H5.25A2.25 2.25 0 0 1 3 18.75V8.25A2.25 2.25 0 0 1 5.25 6H10"
                />
              </svg>
            </button>
          </div>

          <div v-if="!editiertGassiZeiten" class="space-y-1 text-sm text-gray-800">
            <p v-for="zeit in gassiZeiten" :key="zeit.id">
              <span class="inline-block w-8 font-semibold">{{ zeit.tag }}</span>
              {{ formatiereUhrzeit(zeit.von) }} - {{ formatiereUhrzeit(zeit.bis) }} Uhr
            </p>
          </div>
          <div v-else class="space-y-2">
            <div v-for="zeit in gassiZeiten" :key="zeit.id" class="flex items-center gap-2">
              <span class="inline-block w-8 text-sm font-semibold text-gray-800">{{
                zeit.tag
              }}</span>
              <input
                v-model="zeit.von"
                type="time"
                class="text-sm border-b border-gray-400 bg-transparent focus:outline-none"
              />
              <span class="text-sm text-gray-500">-</span>
              <input
                v-model="zeit.bis"
                type="time"
                class="text-sm border-b border-gray-400 bg-transparent focus:outline-none"
              />
            </div>
          </div>
        </div>
      </div>

      <!-- RECHTE SPALTE -->
      <div>
        <div class="flex items-center gap-2 mb-3">
          <h3 class="text-base font-bold text-gray-800">Einstellungen</h3>
          <button
            @click="editiertKalender = !editiertKalender"
            aria-label="Kalenderdaten bearbeiten"
          >
            <svg
              xmlns="http://www.w3.org/2000/svg"
              fill="none"
              viewBox="0 0 24 24"
              stroke-width="1.5"
              stroke="currentColor"
              class="w-4 h-4 text-gray-800"
            >
              <path
                stroke-linecap="round"
                stroke-linejoin="round"
                d="m16.862 4.487 1.687-1.688a1.875 1.875 0 1 1 2.652 2.652L10.582 16.07a4.5 4.5 0 0 1-1.897 1.13L6 18l.8-2.685a4.5 4.5 0 0 1 1.13-1.897l8.932-8.931Zm0 0L19.5 7.125M18 14v4.75A2.25 2.25 0 0 1 15.75 21H5.25A2.25 2.25 0 0 1 3 18.75V8.25A2.25 2.25 0 0 1 5.25 6H10"
              />
            </svg>
          </button>
        </div>

        <div class="space-y-3 text-sm text-gray-800">
          <!-- Zeitblock: immer per Klick wählbar, unabhängig vom Bearbeiten-Modus -->
          <p>Zeitblock: {{ tierheim.zeitblock }}</p>

          <div v-if="!editiertKalender" class="space-y-2">
            <div class="flex items-center gap-3">
              <span>Ruhezeiten Hund:</span>
              <button
                v-for="option in [30, 60]"
                :key="option"
                @click="tierheim.ruhezeit = option"
                :class="[
                  'px-3 py-1 rounded-full text-sm transition-colors',
                  tierheim.ruhezeit === option
                    ? 'bg-[#b8c9b4] border border-emerald-700'
                    : 'bg-[#D3DDD1] border border-gray-200 hover:bg-[#c2cebf]',
                ]"
              >
                {{ option }}
              </button>
            </div>
            <div class="flex items-center gap-3">
              <span>max. Spazierdauer:</span>
              <button
                v-for="option in [30, 60, 90, 120]"
                :key="option"
                @click="tierheim.maxGassi = option"
                :class="[
                  'px-3 py-1 rounded-full text-sm transition-colors',
                  tierheim.maxGassi === option
                    ? 'bg-[#b8c9b4] border border-emerald-700'
                    : 'bg-[#D3DDD1] border border-gray-200 hover:bg-[#c2cebf]',
                ]"
              >
                {{ option }}
              </button>
            </div>
            <div class="flex items-center gap-3">
              <span>min. Spazierdauer:</span>
              <button
                v-for="option in [30, 60]"
                :key="option"
                @click="tierheim.minGassi = option"
                :class="[
                  'px-3 py-1 rounded-full text-sm transition-colors',
                  tierheim.minGassi === option
                    ? 'bg-[#b8c9b4] border border-emerald-700'
                    : 'bg-[#D3DDD1] border border-gray-200 hover:bg-[#c2cebf]',
                ]"
              >
                {{ option }}
              </button>
            </div>
          </div>
          <div v-else class="space-y-2">
            <div class="flex items-center gap-2">
              <span class="w-48 shrink-0">Standard Ruhezeiten (Hund):</span>
              <input
                v-model.number="tierheim.ruhezeit"
                type="number"
                min="0"
                class="w-20 border-b border-gray-400 bg-transparent focus:outline-none"
              />
            </div>
            <div class="flex items-center gap-2">
              <span class="w-48 shrink-0">max. Gassi:</span>
              <input
                v-model.number="tierheim.maxGassi"
                type="number"
                min="0"
                class="w-20 border-b border-gray-400 bg-transparent focus:outline-none"
              />
            </div>
            <div class="flex items-center gap-2">
              <span class="w-48 shrink-0">min. Gassi:</span>
              <input
                v-model.number="tierheim.minGassi"
                type="number"
                min="0"
                class="w-20 border-b border-gray-400 bg-transparent focus:outline-none"
              />
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
import { ref, nextTick, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { formatiereUhrzeit } from '@/components/utils/datum.js'

const router = useRouter()

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

const tierheimId = 1
const tierheim = ref({})
const ladeTierheimDaten = async () => {
  try {
    const response = await fetch(`/api/tierheim/${tierheimId}`)
    if (response.ok) {
      const data = await response.json()
      tierheim.value = {
        name: data.name,
        strasse: data.strasse,
        hausnummer: data.hausnummer,
        plz: data.plz,
        ort: data.ort,
        telefonnummer: data.telefonnummer,
        notfallnummer: data.notfallnummer ? data.notfallnummer : '',
        email: data.email,
        link: data.link ? data.link : '',
        showLink: data.link ? data.link.split('/').at(2) : '',
        farbe: data.bannerfarbe,
        zeitblock: data.zeitblock,
        ruhezeit: data.ruhezeit,
        maxGassi: data.maxGassi,
        minGassi: data.minGassi,
      }
    } else {
      console.error('Fehler beim Laden des Tierheims')
    }
  } catch (error) {
    console.error('Verbindungsfehler zum Backend:', error)
  }
}

const gassiZeiten = ref([])
const ladeZeiten = async () => {
  try {
    const res = await fetch('/api/gassiGehZeiten/all')
    if (res.ok) {
      gassiZeiten.value = await res.json()
    }
  } catch (e) {
    console.error('Fehler beim Laden der Gassi-Geh-Zeiten:', e)
  }
}

onMounted(() => {
  ;(ladeTierheimDaten(), ladeZeiten())
})

const bildBearbeiten = () => {
  // TODO: Bild-Upload/Auswahl an das Backend anbinden
}

const speichern = () => {
  // TODO: an das Backend senden (PUT/PATCH fürs Profil)
  editiertKontakt.value = false
  editiertKalender.value = false
  editiertGassiZeiten.value = false
  router.push('/app/admin')
}
</script>
