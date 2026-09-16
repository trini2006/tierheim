<template>
  <div class="grid grid-cols-1 md:grid-cols-2 gap-8 max-w-6xl mx-auto items-start p-4 md:p-6">
    <!-- LINKER BEREICH: Mitgliederliste -->
    <div class="w-full max-w-md mx-auto md:mx-0">
      <div class="flex items-center justify-between mb-3">
        <h2 class="text-sm tracking-wide uppercase text-gray-600">Hinzufügen</h2>
        <button
          @click="neuesMitglied"
          class="text-2xl leading-none text-gray-700 hover:text-gray-900"
          aria-label="Neues Mitglied hinzufügen"
        >
          +
        </button>
      </div>

      <div class="space-y-3">
        <div
          v-for="mitglied in mitglieder"
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
                mitglied.erfahrung === 'gruen' ? 'bg-green-500' : 'bg-orange-500',
              ]"
            ></span>
            <span class="text-sm text-gray-800">{{ mitglied.nummer }}</span>
          </div>
          <div class="flex items-center gap-3">
            <button @click="bearbeiten(mitglied)" aria-label="Mitglied bearbeiten">
              <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="w-4 h-4 text-gray-800">
                <path stroke-linecap="round" stroke-linejoin="round" d="m16.862 4.487 1.687-1.688a1.875 1.875 0 1 1 2.652 2.652L10.582 16.07a4.5 4.5 0 0 1-1.897 1.13L6 18l.8-2.685a4.5 4.5 0 0 1 1.13-1.897l8.932-8.931Zm0 0L19.5 7.125M18 14v4.75A2.25 2.25 0 0 1 15.75 21H5.25A2.25 2.25 0 0 1 3 18.75V8.25A2.25 2.25 0 0 1 5.25 6H10" />
              </svg>
            </button>
            <button @click="loeschen(mitglied.id)" aria-label="Mitglied löschen">
              <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="w-4 h-4 text-gray-800">
                <path stroke-linecap="round" stroke-linejoin="round" d="m14.74 9-.346 9m-4.788 0L9.26 9m9.968-3.21c.342.052.682.107 1.022.166m-1.022-.165L18.16 19.673a2.25 2.25 0 0 1-2.244 2.077H8.084a2.25 2.25 0 0 1-2.244-2.077L4.772 5.79m14.456 0a48.108 48.108 0 0 0-3.478-.397m-12 .562c.34-.059.68-.114 1.022-.165m0 0a48.11 48.11 0 0 1 3.478-.397m7.5 0v-.916c0-1.18-.91-2.164-2.09-2.201a51.964 51.964 0 0 0-3.32 0c-1.18.037-2.09 1.022-2.09 2.201v.916m7.5 0a48.667 48.667 0 0 0-7.5 0" />
              </svg>
            </button>
          </div>
        </div>

        <p v-if="mitglieder.length === 0" class="text-sm text-gray-500 italic">
          Noch keine Mitglieder vorhanden.
        </p>
      </div>
    </div>

    <!-- RECHTER BEREICH: Mitglied bearbeiten / anlegen -->
    <div class="w-full max-w-md mx-auto md:mx-0 mt-8 md:mt-12">
      <div class="space-y-4">
        <!-- Mitgliedsnummer -->
        <div class="flex items-center gap-2">
          <span class="text-sm text-gray-800 w-36 shrink-0">Mitgliedsnummer:</span>
          <input
            v-if="editField === 'nummer'"
            v-model="form.nummer"
            @blur="editField = null"
            @keyup.enter="editField = null"
            class="border-b border-gray-400 text-sm focus:outline-none bg-transparent flex-1"
          />
          <span v-else class="text-sm text-gray-800 flex-1">{{ nummerAnzeige }}</span>
          <button @click="editField = 'nummer'" aria-label="Mitgliedsnummer bearbeiten">
            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="w-4 h-4 text-gray-800">
              <path stroke-linecap="round" stroke-linejoin="round" d="m16.862 4.487 1.687-1.688a1.875 1.875 0 1 1 2.652 2.652L10.582 16.07a4.5 4.5 0 0 1-1.897 1.13L6 18l.8-2.685a4.5 4.5 0 0 1 1.13-1.897l8.932-8.931Zm0 0L19.5 7.125M18 14v4.75A2.25 2.25 0 0 1 15.75 21H5.25A2.25 2.25 0 0 1 3 18.75V8.25A2.25 2.25 0 0 1 5.25 6H10" />
            </svg>
          </button>
        </div>

        <!-- Passwort -->
        <div class="flex items-center gap-2">
          <span class="text-sm text-gray-800 w-36 shrink-0">Passwort:</span>
          <input
            v-if="editField === 'passwort'"
            v-model="form.passwort"
            @blur="editField = null"
            @keyup.enter="editField = null"
            class="border-b border-gray-400 text-sm focus:outline-none bg-transparent flex-1"
          />
          <span v-else class="text-sm text-gray-800 flex-1">{{ form.passwort }}</span>
          <button @click="editField = 'passwort'" aria-label="Passwort bearbeiten">
            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="w-4 h-4 text-gray-800">
              <path stroke-linecap="round" stroke-linejoin="round" d="m16.862 4.487 1.687-1.688a1.875 1.875 0 1 1 2.652 2.652L10.582 16.07a4.5 4.5 0 0 1-1.897 1.13L6 18l.8-2.685a4.5 4.5 0 0 1 1.13-1.897l8.932-8.931Zm0 0L19.5 7.125M18 14v4.75A2.25 2.25 0 0 1 15.75 21H5.25A2.25 2.25 0 0 1 3 18.75V8.25A2.25 2.25 0 0 1 5.25 6H10" />
            </svg>
          </button>
        </div>

        <!-- Erfahrung -->
        <div class="flex items-center gap-3">
          <span class="text-sm text-gray-800 w-36 shrink-0">Erfahrung:</span>
          <button
            @click="form.erfahrung = 'gruen'"
            :class="[
              'w-5 h-5 rounded-full transition-all',
              form.erfahrung === 'gruen'
                ? 'bg-green-500 ring-2 ring-offset-1 ring-green-700'
                : 'bg-green-300 opacity-60 hover:opacity-100',
            ]"
            aria-label="Grün auswählen"
          ></button>
          <button
            @click="form.erfahrung = 'orange'"
            :class="[
              'w-5 h-5 rounded-full transition-all',
              form.erfahrung === 'orange'
                ? 'bg-orange-500 ring-2 ring-offset-1 ring-orange-700'
                : 'bg-orange-300 opacity-60 hover:opacity-100',
            ]"
            aria-label="Orange auswählen"
          ></button>
        </div>

        <!-- Anlegen Button -->
        <div class="flex justify-center">
          <button
            @click="anlegen"
            class="mt-2 px-6 py-2 rounded-full bg-[#D3DDD1] border border-gray-200 shadow-sm text-sm text-gray-800 hover:bg-[#c2cebf] transition-colors"
          >
            Anlegen
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'

const mitglieder = ref([])

const selectedId = ref(null)
const editField = ref(null)
const form = ref({ nummer: '', passwort: '', erfahrung: 'gruen' })

const nummerAnzeige = computed(() => {
  const n = form.value.nummer
  return n && n.length > 4 ? n.slice(0, 4) + '...' : n
})

// Daten beim Laden der Komponente vom Backend abrufen
const ladeMitglieder = async () => {
  try {
    const res = await fetch('/mitglied/all')
    if (res.ok) {
      const data = await res.json()
      mitglieder.value = data.map((m) => ({
        id: m.mitgliedsnummer,
        nummer: String(m.mitgliedsnummer),
        passwort: m.passwort,
        erfahrung: m.erfahrung,
      }))
    }
  } catch (e) {
    console.error('Fehler beim Laden der Mitglieder:', e)
  }
}

onMounted(ladeMitglieder)

const neuesMitglied = () => {
  selectedId.value = null
  editField.value = null
  form.value = { nummer: '', passwort: '', erfahrung: 'gruen' }
}

const bearbeiten = (mitglied) => {
  selectedId.value = mitglied.id
  editField.value = null
  form.value = { ...mitglied }
}

const loeschen = async (id) => {
  try {
    const res = await fetch(`/mitglied/${id}`, {
      method: 'DELETE',
    })
    if (res.ok) {
      mitglieder.value = mitglieder.value.filter((m) => m.id !== id)
      if (selectedId.value === id) {
        neuesMitglied()
      }
    }
  } catch (e) {
    console.error('Fehler beim Löschen:', e)
  }
}

const anlegen = async () => {
  if (!form.value.nummer) return

  const payload = {
    mitgliedsnummer: parseInt(form.value.nummer, 10),
    passwort: form.value.passwort,
    erfahrung: form.value.erfahrung,
  }

  try {
    if (selectedId.value) {
      // Bestehendes Mitglied aktualisieren (PUT)
      const res = await fetch(`/mitglied/${selectedId.value}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload),
      })
      if (res.ok) {
        await ladeMitglieder()
        neuesMitglied()
      }
    } else {
      // Neues Mitglied anlegen (POST)
      const res = await fetch('/mitglied/new', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload),
      })
      if (res.ok) {
        await ladeMitglieder()
        neuesMitglied()
      }
    }
  } catch (e) {
    console.error('Fehler beim Speichern:', e)
  }
}
</script>