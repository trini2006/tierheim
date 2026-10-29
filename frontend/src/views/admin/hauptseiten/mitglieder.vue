<template>
  <div class="max-w-6xl mx-auto p-4 md:p-6 grid grid-cols-1 md:grid-cols-2 gap-16 items-start">
    <!-- LINKER BEREICH: Mitgliederliste -->
    <div class="space-y-4">
      <div class="flex items-center justify-between mb-6">
        <h2 class="text-xl font-bold text-gray-800">Mitglieder</h2>
      </div>

      <!-- Suchfeld für Mitglieder -->
      <div>
        <input
          type="text"
          v-model="suchbegriffMitglieder"
          placeholder="Mitglied suchen..."
          class="w-full px-4 py-2 rounded-full border border-gray-200 bg-gray-50 text-sm focus:outline-none focus:border-gray-400"
        />
      </div>

      <div class="space-y-3">
        <div
          v-for="mitglied in gefilterteMitglieder"
          :key="mitglied.id"
          :class="[
            'flex items-center justify-between p-3 rounded-full shadow-sm border transition-colors',
            selectedId === mitglied.id
              ? 'bg-[#b8c9b4] border-emerald-700'
              : 'bg-[#D3DDD1] border-gray-200',
          ]"
        >
          <div class="flex items-center gap-3">
            <span
              :class="[
                'w-3 h-3 rounded-full shrink-0',
                mitglied.erfahrung === true ? 'bg-orange-500' : 'bg-green-500',
              ]"
            ></span>
            <span class="text-sm font-bold text-gray-800">{{ mitglied.nummer }}</span>
          </div>
          <div class="flex items-center gap-4 pr-2">
            <button @click="bearbeiten(mitglied)" aria-label="Mitglied bearbeiten" class="cursor-pointer">
              <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="w-4 h-4 text-gray-800 hover:text-black">
                <path stroke-linecap="round" stroke-linejoin="round" d="m16.862 4.487 1.687-1.688a1.875 1.875 0 1 1 2.652 2.652L10.582 16.07a4.5 4.5 0 0 1-1.897 1.13L6 18l.8-2.685a4.5 4.5 0 0 1 1.13-1.897l8.932-8.931Zm0 0L19.5 7.125M18 14v4.75A2.25 2.25 0 0 1 15.75 21H5.25A2.25 2.25 0 0 1 3 18.75V8.25A2.25 2.25 0 0 1 5.25 6H10" />
              </svg>
            </button>
            <button @click="loeschen(mitglied.id)" aria-label="Mitglied löschen" class="cursor-pointer">
              <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="w-4 h-4 text-gray-800 hover:text-red-700">
                <path stroke-linecap="round" stroke-linejoin="round" d="m14.74 9-.346 9m-4.788 0L9.26 9m9.968-3.21c.342.052.682.107 1.022.166m-1.022-.165L18.16 19.673a2.25 2.25 0 0 1-2.244 2.077H8.084a2.25 2.25 0 0 1-2.244-2.077L4.772 5.79m14.456 0a48.108 48.108 0 0 0-3.478-.397m-12 .562c.34-.059.68-.114 1.022-.165m0 0a48.11 48.11 0 0 1 3.478-.397m7.5 0v-.916c0-1.18-.91-2.164-2.09-2.201a51.964 51.964 0 0 0-3.32 0c-1.18.037-2.09 1.022-2.09 2.201v.916m7.5 0a48.667 48.667 0 0 0-7.5 0" />
              </svg>
            </button>
          </div>
        </div>

        <p v-if="gefilterteMitglieder.length === 0" class="text-sm text-gray-500 italic">
          Keine Mitglieder gefunden...
        </p>
      </div>
    </div>

    <!-- RECHTER BEREICH: Mitglied bearbeiten -->
    <div class="space-y-4">
      <h2 class="text-xl font-bold text-gray-800 mb-6">Mitglied bearbeiten</h2>

      <div v-if="selectedId !== null" class="space-y-4 bg-gray-50 border border-gray-200 p-6 rounded-2xl shadow-sm">
        <div class="flex items-center gap-2">
          <span class="text-sm font-semibold text-gray-700 w-36 shrink-0">Mitgliedsnummer:</span>
          <input
            v-if="editField === 'nummer'"
            v-model="formNummer"
            @blur="editField = null"
            @keyup.enter="editField = null"
            class="border-b border-gray-400 text-sm focus:outline-none bg-transparent flex-1 py-1"
          />
          <span v-else class="text-sm text-gray-900 flex-1">{{ formNummer }}</span>
          <button @click="editField = 'nummer'" aria-label="Mitgliedsnummer bearbeiten" class="cursor-pointer">
            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="w-4 h-4 text-gray-800 hover:text-black">
              <path stroke-linecap="round" stroke-linejoin="round" d="m16.862 4.487 1.687-1.688a1.875 1.875 0 1 1 2.652 2.652L10.582 16.07a4.5 4.5 0 0 1-1.897 1.13L6 18l.8-2.685a4.5 4.5 0 0 1 1.13-1.897l8.932-8.931Zm0 0L19.5 7.125M18 14v4.75A2.25 2.25 0 0 1 15.75 21H5.25A2.25 2.25 0 0 1 3 18.75V8.25A2.25 2.25 0 0 1 5.25 6H10" />
            </svg>
          </button>
        </div>

        <!-- Erfahrungsauswahl (false = grün, true = orange/gelb) -->
        <div class="flex items-center gap-3 pt-2">
          <span class="text-sm font-semibold text-gray-700 w-36 shrink-0">Erfahrung:</span>
          <button
            type="button"
            @click="formErfahrung = false"
            :class="[
              'w-5 h-5 rounded-full transition-all cursor-pointer',
              formErfahrung === false
                ? 'bg-green-500 ring-2 ring-offset-1 ring-green-700'
                : 'bg-green-300 opacity-60 hover:opacity-100',
            ]"
            aria-label="Keine Erfahrung (grün)"
          ></button>
          <button
            type="button"
            @click="formErfahrung = true"
            :class="[
              'w-5 h-5 rounded-full transition-all cursor-pointer',
              formErfahrung === true
                ? 'bg-orange-500 ring-2 ring-offset-1 ring-orange-700'
                : 'bg-orange-300 opacity-60 hover:opacity-100',
            ]"
            aria-label="Erfahrung nötig (orange/gelb)"
          ></button>
        </div>

        <div class="flex justify-end pt-4">
          <button
            @click="speichern"
            class="px-6 py-2 rounded-full bg-[#D3DDD1] border border-gray-200 shadow-sm text-sm font-bold text-gray-800 hover:bg-[#c2cebf] transition-colors cursor-pointer"
          >
            Änderungen speichern
          </button>
        </div>
      </div>
      <div v-else class="text-sm text-gray-500 italic p-4 bg-gray-50 rounded-2xl border border-gray-200">
        Bitte wählen Sie links ein Mitglied aus, um es zu bearbeiten.
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'

const mitglieder = ref([])
const suchbegriffMitglieder = ref('')
const selectedId = ref(null)
const editField = ref(null)

const formNummer = ref('')
const formPasswort = ref('')
const formErfahrung = ref(false)

const gefilterteMitglieder = computed(() => {
  if (!suchbegriffMitglieder.value.trim()) return mitglieder.value
  return mitglieder.value.filter((m) =>
    m.nummer.toLowerCase().includes(suchbegriffMitglieder.value.toLowerCase())
  )
})

const ladeMitglieder = async () => {
  try {
    const res = await fetch('/api/mitglied/all')
    if (res.ok) {
      const data = await res.json()
      mitglieder.value = data.map((m) => ({
        id: m.mitgliedsnummer,
        nummer: String(m.mitgliedsnummer),
        passwort: m.passwort,
        erfahrung: Boolean(m.erfahrung),
      }))
    }
  } catch (e) {
    console.error('Fehler beim Laden der Mitglieder:', e)
  }
}

onMounted(ladeMitglieder)

const bearbeiten = (mitglied) => {
  selectedId.value = mitglied.id
  editField.value = null
  formNummer.value = mitglied.nummer
  formPasswort.value = mitglied.passwort || ''
  formErfahrung.value = Boolean(mitglied.erfahrung)
}

const loeschen = async (id) => {
  try {
    const res = await fetch(`/api/mitglied/${id}`, {
      method: 'DELETE',
    })
    if (res.ok) {
      mitglieder.value = mitglieder.value.filter((m) => m.id !== id)
      if (selectedId.value === id) {
        selectedId.value = null
      }
    }
  } catch (e) {
    console.error('Fehler beim Löschen:', e)
  }
}

const speichern = async () => {
  if (selectedId.value === null || !formNummer.value) return

  const payload = {
    mitgliedsnummer: parseInt(formNummer.value, 10),
    passwort: formPasswort.value || 'default',
    erfahrung: formErfahrung.value, // true oder false
  }

  try {
    const res = await fetch(`/api/mitglied/${selectedId.value}`, {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(payload),
    })
    if (res.ok) {
      await ladeMitglieder()
      alert('Erfolgreich gespeichert!')
    } else {
      alert('Fehler beim Speichern.')
    }
  } catch (e) {
    console.error('Fehler beim Speichern:', e)
  }
}
</script>